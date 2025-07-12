package com.SkillsSwap.skillswap.controller;

import com.SkillsSwap.skillswap.dto.SwapRequestDTO;
import com.SkillsSwap.skillswap.model.Skill;
import com.SkillsSwap.skillswap.model.SwapRequest;
import com.SkillsSwap.skillswap.model.SwapRequest.SwapStatus;
import com.SkillsSwap.skillswap.model.User;
import com.SkillsSwap.skillswap.service.SkillService;
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
@RequestMapping("/api/swap-requests")
public class SwapRequestController {

    private final SwapRequestService swapRequestService;
    private final UserService userService;
    private final SkillService skillService;

    public SwapRequestController(SwapRequestService swapRequestService, 
                                UserService userService,
                                SkillService skillService) {
        this.swapRequestService = swapRequestService;
        this.userService = userService;
        this.skillService = skillService;
    }

    @GetMapping
    public ResponseEntity<List<SwapRequestDTO>> getCurrentUserSwapRequests() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        Optional<User> userOpt = userService.findUserByUsername(username);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            List<SwapRequest> swapRequests = swapRequestService.getSwapRequestsByUser(user);
            List<SwapRequestDTO> swapRequestDTOs = swapRequests.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(swapRequestDTOs);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @GetMapping("/sent")
    public ResponseEntity<List<SwapRequestDTO>> getSentSwapRequests() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        Optional<User> userOpt = userService.findUserByUsername(username);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            List<SwapRequest> swapRequests = swapRequestService.getSwapRequestsByRequester(user);
            List<SwapRequestDTO> swapRequestDTOs = swapRequests.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(swapRequestDTOs);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @GetMapping("/received")
    public ResponseEntity<List<SwapRequestDTO>> getReceivedSwapRequests() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        Optional<User> userOpt = userService.findUserByUsername(username);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            List<SwapRequest> swapRequests = swapRequestService.getSwapRequestsByProvider(user);
            List<SwapRequestDTO> swapRequestDTOs = swapRequests.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(swapRequestDTOs);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<SwapRequestDTO>> getSwapRequestsByStatus(@PathVariable SwapStatus status) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        Optional<User> userOpt = userService.findUserByUsername(username);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            List<SwapRequest> swapRequests = swapRequestService.getSwapRequestsByUserAndStatus(user, status);
            List<SwapRequestDTO> swapRequestDTOs = swapRequests.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(swapRequestDTOs);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getSwapRequestById(@PathVariable Long id) {
        Optional<SwapRequest> swapRequestOpt = swapRequestService.findSwapRequestById(id);
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
                return ResponseEntity.ok(convertToDTO(swapRequest));
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You don't have permission to view this swap request");
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<?> createSwapRequest(@Valid @RequestBody SwapRequestDTO swapRequestDTO) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String username = auth.getName();

            Optional<User> userOpt = userService.findUserByUsername(username);
            if (userOpt.isPresent()) {
                User user = userOpt.get();

                // Verify that the requester is the current user
                if (!user.getId().equals(swapRequestDTO.getRequesterId())) {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You can only create swap requests for yourself");
                }

                SwapRequest swapRequest = swapRequestService.createSwapRequest(
                    swapRequestDTO.getRequesterId(),
                    swapRequestDTO.getProviderId(),
                    swapRequestDTO.getRequestedSkillId(),
                    swapRequestDTO.getOfferedSkillId(),
                    swapRequestDTO.getMessage()
                );

                return ResponseEntity.status(HttpStatus.CREATED).body(convertToDTO(swapRequest));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/{id}/accept")
    public ResponseEntity<?> acceptSwapRequest(@PathVariable Long id) {
        try {
            Optional<SwapRequest> swapRequestOpt = swapRequestService.findSwapRequestById(id);
            if (swapRequestOpt.isPresent()) {
                SwapRequest swapRequest = swapRequestOpt.get();

                // Check if the current user is the provider
                Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                String username = auth.getName();
                boolean isProvider = swapRequest.getProvider().getUsername().equals(username);

                if (isProvider) {
                    SwapRequest updatedSwapRequest = swapRequestService.acceptSwapRequest(id);
                    return ResponseEntity.ok(convertToDTO(updatedSwapRequest));
                } else {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Only the provider can accept a swap request");
                }
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/{id}/reject")
    public ResponseEntity<?> rejectSwapRequest(@PathVariable Long id) {
        try {
            Optional<SwapRequest> swapRequestOpt = swapRequestService.findSwapRequestById(id);
            if (swapRequestOpt.isPresent()) {
                SwapRequest swapRequest = swapRequestOpt.get();

                // Check if the current user is the provider
                Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                String username = auth.getName();
                boolean isProvider = swapRequest.getProvider().getUsername().equals(username);

                if (isProvider) {
                    SwapRequest updatedSwapRequest = swapRequestService.rejectSwapRequest(id);
                    return ResponseEntity.ok(convertToDTO(updatedSwapRequest));
                } else {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Only the provider can reject a swap request");
                }
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/{id}/complete")
    public ResponseEntity<?> completeSwapRequest(@PathVariable Long id) {
        try {
            Optional<SwapRequest> swapRequestOpt = swapRequestService.findSwapRequestById(id);
            if (swapRequestOpt.isPresent()) {
                SwapRequest swapRequest = swapRequestOpt.get();

                // Check if the current user is a participant
                Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                String username = auth.getName();
                boolean isParticipant = swapRequest.getRequester().getUsername().equals(username) || 
                                       swapRequest.getProvider().getUsername().equals(username);

                if (isParticipant) {
                    SwapRequest updatedSwapRequest = swapRequestService.completeSwapRequest(id);
                    return ResponseEntity.ok(convertToDTO(updatedSwapRequest));
                } else {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Only participants can complete a swap request");
                }
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<?> cancelSwapRequest(@PathVariable Long id) {
        try {
            Optional<SwapRequest> swapRequestOpt = swapRequestService.findSwapRequestById(id);
            if (swapRequestOpt.isPresent()) {
                SwapRequest swapRequest = swapRequestOpt.get();

                // Check if the current user is a participant
                Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                String username = auth.getName();
                boolean isParticipant = swapRequest.getRequester().getUsername().equals(username) || 
                                       swapRequest.getProvider().getUsername().equals(username);

                if (isParticipant) {
                    SwapRequest updatedSwapRequest = swapRequestService.cancelSwapRequest(id);
                    return ResponseEntity.ok(convertToDTO(updatedSwapRequest));
                } else {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Only participants can cancel a swap request");
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
    public ResponseEntity<List<SwapRequestDTO>> getAllSwapRequests() {
        List<SwapRequest> swapRequests = swapRequestService.getAllSwapRequests();
        List<SwapRequestDTO> swapRequestDTOs = swapRequests.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(swapRequestDTOs);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/status/{status}")
    public ResponseEntity<List<SwapRequestDTO>> getAllSwapRequestsByStatus(@PathVariable SwapStatus status) {
        List<SwapRequest> swapRequests = swapRequestService.getSwapRequestsByStatus(status);
        List<SwapRequestDTO> swapRequestDTOs = swapRequests.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(swapRequestDTOs);
    }

    // Helper method to convert SwapRequest to SwapRequestDTO
    private SwapRequestDTO convertToDTO(SwapRequest swapRequest) {
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
}
