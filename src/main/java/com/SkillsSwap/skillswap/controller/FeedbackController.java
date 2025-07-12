package com.SkillsSwap.skillswap.controller;

import com.SkillsSwap.skillswap.dto.FeedbackDTO;
import com.SkillsSwap.skillswap.model.Feedback;
import com.SkillsSwap.skillswap.model.SwapRequest;
import com.SkillsSwap.skillswap.model.User;
import com.SkillsSwap.skillswap.service.FeedbackService;
import com.SkillsSwap.skillswap.service.SwapRequestService;
import com.SkillsSwap.skillswap.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/feedback")
public class FeedbackController {

    private final FeedbackService feedbackService;
    private final UserService userService;
    private final SwapRequestService swapRequestService;

    public FeedbackController(FeedbackService feedbackService,
                             UserService userService,
                             SwapRequestService swapRequestService) {
        this.feedbackService = feedbackService;
        this.userService = userService;
        this.swapRequestService = swapRequestService;
    }

    @GetMapping("/given")
    public ResponseEntity<List<FeedbackDTO>> getGivenFeedback() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        Optional<User> userOpt = userService.findUserByUsername(username);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            List<Feedback> feedbacks = feedbackService.getFeedbackByReviewer(user);
            List<FeedbackDTO> feedbackDTOs = feedbacks.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(feedbackDTOs);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @GetMapping("/received")
    public ResponseEntity<List<FeedbackDTO>> getReceivedFeedback() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        Optional<User> userOpt = userService.findUserByUsername(username);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            List<Feedback> feedbacks = feedbackService.getFeedbackByRecipient(user);
            List<FeedbackDTO> feedbackDTOs = feedbacks.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(feedbackDTOs);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<FeedbackDTO>> getFeedbackByUser(@PathVariable Long userId) {
        Optional<User> userOpt = userService.findUserById(userId);
        if (userOpt.isPresent()) {
            User user = userOpt.get();

            // Check if user is public or if the requester is the user themselves
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String currentUsername = auth.getName();
            boolean isCurrentUser = user.getUsername().equals(currentUsername);

            if (user.isPublic() || isCurrentUser || auth.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
                List<Feedback> feedbacks = feedbackService.getFeedbackByRecipient(user);
                List<FeedbackDTO> feedbackDTOs = feedbacks.stream()
                        .map(this::convertToDTO)
                        .collect(Collectors.toList());
                return ResponseEntity.ok(feedbackDTOs);
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/swap-request/{swapRequestId}")
    public ResponseEntity<List<FeedbackDTO>> getFeedbackBySwapRequest(@PathVariable Long swapRequestId) {
        Optional<SwapRequest> swapRequestOpt = swapRequestService.findSwapRequestById(swapRequestId);
        if (swapRequestOpt.isPresent()) {
            SwapRequest swapRequest = swapRequestOpt.get();

            // Check if the current user is a participant in the swap request
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String username = auth.getName();
            boolean isParticipant = swapRequest.getRequester().getUsername().equals(username) || 
                                   swapRequest.getProvider().getUsername().equals(username);
            boolean isAdmin = auth.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

            if (isParticipant || isAdmin) {
                List<Feedback> feedbacks = feedbackService.getFeedbackBySwapRequest(swapRequest);
                List<FeedbackDTO> feedbackDTOs = feedbacks.stream()
                        .map(this::convertToDTO)
                        .collect(Collectors.toList());
                return ResponseEntity.ok(feedbackDTOs);
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/rating/{userId}")
    public ResponseEntity<?> getAverageRatingForUser(@PathVariable Long userId) {
        Optional<User> userOpt = userService.findUserById(userId);
        if (userOpt.isPresent()) {
            User user = userOpt.get();

            // Check if user is public or if the requester is the user themselves
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String currentUsername = auth.getName();
            boolean isCurrentUser = user.getUsername().equals(currentUsername);

            if (user.isPublic() || isCurrentUser || auth.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
                Double averageRating = feedbackService.getAverageRatingForUser(user);
                return ResponseEntity.ok(averageRating);
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<?> createFeedback(@Valid @RequestBody FeedbackDTO feedbackDTO) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String username = auth.getName();

            Optional<User> userOpt = userService.findUserByUsername(username);
            if (userOpt.isPresent()) {
                User user = userOpt.get();

                // Verify that the reviewer is the current user
                if (!user.getId().equals(feedbackDTO.getReviewerId())) {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You can only give feedback as yourself");
                }

                Feedback feedback = feedbackService.createFeedback(
                    feedbackDTO.getReviewerId(),
                    feedbackDTO.getRecipientId(),
                    feedbackDTO.getSwapRequestId(),
                    feedbackDTO.getRating(),
                    feedbackDTO.getComment()
                );

                return ResponseEntity.status(HttpStatus.CREATED).body(convertToDTO(feedback));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteFeedback(@PathVariable Long id) {
        try {
            Optional<Feedback> feedbackOpt = feedbackService.findFeedbackById(id);
            if (feedbackOpt.isPresent()) {
                Feedback feedback = feedbackOpt.get();

                // Check if the current user is the reviewer or an admin
                Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                String username = auth.getName();
                boolean isReviewer = feedback.getReviewer().getUsername().equals(username);
                boolean isAdmin = auth.getAuthorities().stream()
                        .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

                if (isReviewer || isAdmin) {
                    feedbackService.deleteFeedback(id);
                    return ResponseEntity.ok().build();
                } else {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You don't have permission to delete this feedback");
                }
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/all")
    public ResponseEntity<List<FeedbackDTO>> getAllFeedback() {
        List<Feedback> feedbacks = feedbackService.getAllFeedback();
        List<FeedbackDTO> feedbackDTOs = feedbacks.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(feedbackDTOs);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/low-rating")
    public ResponseEntity<List<FeedbackDTO>> getLowRatingFeedback(@RequestParam(defaultValue = "2") int maxRating) {
        List<Feedback> feedbacks = feedbackService.getFeedbackByRatingLessThanEqual(maxRating);
        List<FeedbackDTO> feedbackDTOs = feedbacks.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(feedbackDTOs);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/high-rating")
    public ResponseEntity<List<FeedbackDTO>> getHighRatingFeedback(@RequestParam(defaultValue = "4") int minRating) {
        List<Feedback> feedbacks = feedbackService.getFeedbackByRatingGreaterThanEqual(minRating);
        List<FeedbackDTO> feedbackDTOs = feedbacks.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(feedbackDTOs);
    }

    // Helper method to convert Feedback to FeedbackDTO
    private FeedbackDTO convertToDTO(Feedback feedback) {
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
