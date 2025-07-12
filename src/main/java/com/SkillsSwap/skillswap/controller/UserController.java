package com.SkillsSwap.skillswap.controller;

import com.SkillsSwap.skillswap.dto.UserDTO;
import com.SkillsSwap.skillswap.model.User;
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
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/public")
    public ResponseEntity<List<UserDTO>> getAllPublicUsers() {
        List<User> users = userService.getAllPublicUsers();
        List<UserDTO> userDTOs = users.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(userDTOs);
    }

    @GetMapping("/search/offered-skills")
    public ResponseEntity<List<UserDTO>> searchUsersByOfferedSkill(@RequestParam String skillName) {
        List<User> users = userService.searchUsersByOfferedSkill(skillName);
        List<UserDTO> userDTOs = users.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(userDTOs);
    }

    @GetMapping("/search/wanted-skills")
    public ResponseEntity<List<UserDTO>> searchUsersByWantedSkill(@RequestParam String skillName) {
        List<User> users = userService.searchUsersByWantedSkill(skillName);
        List<UserDTO> userDTOs = users.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(userDTOs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        Optional<User> userOpt = userService.findUserById(id);

        if (userOpt.isPresent()) {
            User user = userOpt.get();

            // Check if user is public or if the requester is the user themselves
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String currentUsername = auth.getName();
            boolean isCurrentUser = user.getUsername().equals(currentUsername);

            if (user.isPublic() || isCurrentUser || auth.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
                return ResponseEntity.ok(convertToDTO(user));
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("This profile is private");
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/profile")
    public ResponseEntity<?> getCurrentUserProfile() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        Optional<User> userOpt = userService.findUserByUsername(username);
        if (userOpt.isPresent()) {
            return ResponseEntity.ok(convertToDTO(userOpt.get()));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/profile")
    public ResponseEntity<?> updateUserProfile(@Valid @RequestBody UserDTO userDTO) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        Optional<User> userOpt = userService.findUserByUsername(username);
        if (userOpt.isPresent()) {
            User user = userOpt.get();

            // Update user fields
            user.setName(userDTO.getName());
            user.setLocation(userDTO.getLocation());
            user.setProfilePhoto(userDTO.getProfilePhoto());
            user.setAvailability(userDTO.getAvailability());
            user.setPublic(userDTO.isPublic());

            // Don't allow users to change their admin status
            // Only update password if provided
            if (userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()) {
                // Password will be encoded in the service
                user.setPassword(userDTO.getPassword());
            }

            User updatedUser = userService.updateUser(user);
            return ResponseEntity.ok(convertToDTO(updatedUser));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}/deactivate")
    public ResponseEntity<?> deactivateUser(@PathVariable Long id) {
        try {
            userService.deactivateUser(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}/activate")
    public ResponseEntity<?> activateUser(@PathVariable Long id) {
        try {
            userService.activateUser(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // Helper method to convert User to UserDTO
    private UserDTO convertToDTO(User user) {
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
}
