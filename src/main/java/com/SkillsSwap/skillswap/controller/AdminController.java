package com.SkillsSwap.skillswap.controller;

import com.SkillsSwap.skillswap.dto.FeedbackDTO;
import com.SkillsSwap.skillswap.dto.SkillDTO;
import com.SkillsSwap.skillswap.dto.SwapRequestDTO;
import com.SkillsSwap.skillswap.dto.UserDTO;
import com.SkillsSwap.skillswap.model.Feedback;
import com.SkillsSwap.skillswap.model.Skill;
import com.SkillsSwap.skillswap.model.SwapRequest;
import com.SkillsSwap.skillswap.model.User;
import com.SkillsSwap.skillswap.service.FeedbackService;
import com.SkillsSwap.skillswap.service.SkillService;
import com.SkillsSwap.skillswap.service.SwapRequestService;
import com.SkillsSwap.skillswap.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final UserService userService;
    private final SkillService skillService;
    private final SwapRequestService swapRequestService;
    private final FeedbackService feedbackService;

    public AdminController(UserService userService,
                          SkillService skillService,
                          SwapRequestService swapRequestService,
                          FeedbackService feedbackService) {
        this.userService = userService;
        this.skillService = skillService;
        this.swapRequestService = swapRequestService;
        this.feedbackService = feedbackService;
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<User> users = userService.getAllPublicUsers();
        List<UserDTO> userDTOs = users.stream()
                .map(this::convertUserToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(userDTOs);
    }

    @GetMapping("/users/admins")
    public ResponseEntity<List<UserDTO>> getAllAdmins() {
        List<User> admins = userService.getAllAdmins();
        List<UserDTO> adminDTOs = admins.stream()
                .map(this::convertUserToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(adminDTOs);
    }

    @PutMapping("/users/{id}/make-admin")
    public ResponseEntity<?> makeUserAdmin(@PathVariable Long id) {
        try {
            userService.makeAdmin(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/users/{id}/remove-admin")
    public ResponseEntity<?> removeUserAdmin(@PathVariable Long id) {
        try {
            userService.removeAdmin(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/skills/pending")
    public ResponseEntity<List<SkillDTO>> getPendingSkills() {
        List<Skill> pendingSkills = skillService.getPendingApprovalSkills();
        List<SkillDTO> skillDTOs = pendingSkills.stream()
                .map(this::convertSkillToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(skillDTOs);
    }

    @PutMapping("/skills/{id}/approve")
    public ResponseEntity<?> approveSkill(@PathVariable Long id) {
        try {
            skillService.approveSkill(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/skills/{id}/reject")
    public ResponseEntity<?> rejectSkill(@PathVariable Long id) {
        try {
            skillService.rejectSkill(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/swap-requests/stats")
    public ResponseEntity<Map<String, Long>> getSwapRequestStats() {
        Map<String, Long> stats = new HashMap<>();
        
        stats.put("pending", swapRequestService.countSwapRequestsByStatus(SwapRequest.SwapStatus.PENDING));
        stats.put("accepted", swapRequestService.countSwapRequestsByStatus(SwapRequest.SwapStatus.ACCEPTED));
        stats.put("rejected", swapRequestService.countSwapRequestsByStatus(SwapRequest.SwapStatus.REJECTED));
        stats.put("completed", swapRequestService.countSwapRequestsByStatus(SwapRequest.SwapStatus.COMPLETED));
        stats.put("cancelled", swapRequestService.countSwapRequestsByStatus(SwapRequest.SwapStatus.CANCELLED));
        
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/feedback/low-rating")
    public ResponseEntity<List<FeedbackDTO>> getLowRatingFeedback(@RequestParam(defaultValue = "2") int maxRating) {
        List<Feedback> feedbacks = feedbackService.getFeedbackByRatingLessThanEqual(maxRating);
        List<FeedbackDTO> feedbackDTOs = feedbacks.stream()
                .map(this::convertFeedbackToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(feedbackDTOs);
    }

    @GetMapping("/reports/user-activity")
    public ResponseEntity<Map<String, Object>> getUserActivityReport() {
        Map<String, Object> report = new HashMap<>();
        
        // Total users
        List<User> allUsers = userService.getAllPublicUsers();
        report.put("totalUsers", allUsers.size());
        
        // Total admins
        List<User> admins = userService.getAllAdmins();
        report.put("totalAdmins", admins.size());
        
        // Total skills
        List<Skill> allSkills = skillService.getAllSkills();
        report.put("totalSkills", allSkills.size());
        
        // Pending skills
        List<Skill> pendingSkills = skillService.getPendingApprovalSkills();
        report.put("pendingSkills", pendingSkills.size());
        
        // Swap request stats
        Map<String, Long> swapStats = new HashMap<>();
        swapStats.put("pending", swapRequestService.countSwapRequestsByStatus(SwapRequest.SwapStatus.PENDING));
        swapStats.put("accepted", swapRequestService.countSwapRequestsByStatus(SwapRequest.SwapStatus.ACCEPTED));
        swapStats.put("rejected", swapRequestService.countSwapRequestsByStatus(SwapRequest.SwapStatus.REJECTED));
        swapStats.put("completed", swapRequestService.countSwapRequestsByStatus(SwapRequest.SwapStatus.COMPLETED));
        swapStats.put("cancelled", swapRequestService.countSwapRequestsByStatus(SwapRequest.SwapStatus.CANCELLED));
        report.put("swapRequestStats", swapStats);
        
        // Total feedback
        List<Feedback> allFeedback = feedbackService.getAllFeedback();
        report.put("totalFeedback", allFeedback.size());
        
        return ResponseEntity.ok(report);
    }

    // Helper methods to convert entities to DTOs
    private UserDTO convertUserToDTO(User user) {
        return new UserDTO(
            user.getId(),
            user.getUsername(),
            user.getName(),
            user.getLocation(),
            user.getProfilePhoto(),
            user.getAvailability(),
            user.isPublic(),
            user.isAdmin(),
            user.isActive()
        );
    }

    private SkillDTO convertSkillToDTO(Skill skill) {
        return new SkillDTO(
            skill.getId(),
            skill.getName(),
            skill.getDescription(),
            skill.isOffered(),
            skill.getUser().getId(),
            skill.getUser().getUsername(),
            skill.isApproved()
        );
    }

    private SwapRequestDTO convertSwapRequestToDTO(SwapRequest swapRequest) {
        return new SwapRequestDTO(
            swapRequest.getId(),
            swapRequest.getRequester().getId(),
            swapRequest.getRequester().getUsername(),
            swapRequest.getProvider().getId(),
            swapRequest.getProvider().getUsername(),
            swapRequest.getRequestedSkill().getId(),
            swapRequest.getRequestedSkill().getName(),
            swapRequest.getOfferedSkill().getId(),
            swapRequest.getOfferedSkill().getName(),
            swapRequest.getRequestDate(),
            swapRequest.getResponseDate(),
            swapRequest.getStatus(),
            swapRequest.getMessage()
        );
    }

    private FeedbackDTO convertFeedbackToDTO(Feedback feedback) {
        return new FeedbackDTO(
            feedback.getId(),
            feedback.getReviewer().getId(),
            feedback.getReviewer().getUsername(),
            feedback.getRecipient().getId(),
            feedback.getRecipient().getUsername(),
            feedback.getSwapRequest().getId(),
            feedback.getRating(),
            feedback.getComment(),
            feedback.getCreatedAt()
        );
    }
}