package com.SkillsSwap.skillswap.controller;

import com.SkillsSwap.skillswap.dto.SkillDTO;
import com.SkillsSwap.skillswap.model.Skill;
import com.SkillsSwap.skillswap.model.User;
import com.SkillsSwap.skillswap.service.SkillService;
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
@RequestMapping("/api/skills")
public class SkillController {

    private final SkillService skillService;
    private final UserService userService;

    public SkillController(SkillService skillService, UserService userService) {
        this.skillService = skillService;
        this.userService = userService;
    }

    @GetMapping("/public/names")
    public ResponseEntity<List<String>> getAllSkillNames() {
        List<String> skillNames = skillService.getAllDistinctSkillNames();
        return ResponseEntity.ok(skillNames);
    }

    @GetMapping("/public/search")
    public ResponseEntity<List<SkillDTO>> searchSkillsByName(@RequestParam String name) {
        List<Skill> skills = skillService.searchSkillsByName(name);
        List<SkillDTO> skillDTOs = skills.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(skillDTOs);
    }

    @GetMapping("/public/offered")
    public ResponseEntity<List<SkillDTO>> searchOfferedSkillsByName(@RequestParam String name) {
        List<Skill> skills = skillService.searchOfferedSkillsByName(name);
        List<SkillDTO> skillDTOs = skills.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(skillDTOs);
    }

    @GetMapping("/public/wanted")
    public ResponseEntity<List<SkillDTO>> searchWantedSkillsByName(@RequestParam String name) {
        List<Skill> skills = skillService.searchWantedSkillsByName(name);
        List<SkillDTO> skillDTOs = skills.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(skillDTOs);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<SkillDTO>> getSkillsByUser(@PathVariable Long userId) {
        Optional<User> userOpt = userService.findUserById(userId);
        if (userOpt.isPresent()) {
            User user = userOpt.get();

            // Check if user is public or if the requester is the user themselves
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String currentUsername = auth.getName();
            boolean isCurrentUser = user.getUsername().equals(currentUsername);

            if (user.isPublic() || isCurrentUser || auth.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
                List<Skill> skills = skillService.getSkillsByUser(user);
                List<SkillDTO> skillDTOs = skills.stream()
                        .map(this::convertToDTO)
                        .collect(Collectors.toList());
                return ResponseEntity.ok(skillDTOs);
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/user/{userId}/offered")
    public ResponseEntity<List<SkillDTO>> getOfferedSkillsByUser(@PathVariable Long userId) {
        Optional<User> userOpt = userService.findUserById(userId);
        if (userOpt.isPresent()) {
            User user = userOpt.get();

            // Check if user is public or if the requester is the user themselves
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String currentUsername = auth.getName();
            boolean isCurrentUser = user.getUsername().equals(currentUsername);

            if (user.isPublic() || isCurrentUser || auth.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
                List<Skill> skills = skillService.getOfferedSkillsByUser(user);
                List<SkillDTO> skillDTOs = skills.stream()
                        .map(this::convertToDTO)
                        .collect(Collectors.toList());
                return ResponseEntity.ok(skillDTOs);
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/user/{userId}/wanted")
    public ResponseEntity<List<SkillDTO>> getWantedSkillsByUser(@PathVariable Long userId) {
        Optional<User> userOpt = userService.findUserById(userId);
        if (userOpt.isPresent()) {
            User user = userOpt.get();

            // Check if user is public or if the requester is the user themselves
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String currentUsername = auth.getName();
            boolean isCurrentUser = user.getUsername().equals(currentUsername);

            if (user.isPublic() || isCurrentUser || auth.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
                List<Skill> skills = skillService.getWantedSkillsByUser(user);
                List<SkillDTO> skillDTOs = skills.stream()
                        .map(this::convertToDTO)
                        .collect(Collectors.toList());
                return ResponseEntity.ok(skillDTOs);
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<?> addSkill(@Valid @RequestBody SkillDTO skillDTO) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String username = auth.getName();

            Optional<User> userOpt = userService.findUserByUsername(username);
            if (userOpt.isPresent()) {
                User user = userOpt.get();

                Skill skill = new Skill();
                skill.setName(skillDTO.getName());
                skill.setDescription(skillDTO.getDescription());
                skill.setOffered(skillDTO.isOffered());

                Skill savedSkill = skillService.addSkill(skill, user.getId());
                return ResponseEntity.status(HttpStatus.CREATED).body(convertToDTO(savedSkill));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateSkill(@PathVariable Long id, @Valid @RequestBody SkillDTO skillDTO) {
        try {
            Optional<Skill> skillOpt = skillService.findSkillById(id);
            if (skillOpt.isPresent()) {
                Skill skill = skillOpt.get();

                // Check if the current user is the owner of the skill
                Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                String username = auth.getName();
                boolean isOwner = skill.getUser().getUsername().equals(username);
                boolean isAdmin = auth.getAuthorities().stream()
                        .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

                if (isOwner || isAdmin) {
                    skill.setName(skillDTO.getName());
                    skill.setDescription(skillDTO.getDescription());
                    skill.setOffered(skillDTO.isOffered());

                    Skill updatedSkill = skillService.updateSkill(skill);
                    return ResponseEntity.ok(convertToDTO(updatedSkill));
                } else {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You don't have permission to update this skill");
                }
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSkill(@PathVariable Long id) {
        try {
            Optional<Skill> skillOpt = skillService.findSkillById(id);
            if (skillOpt.isPresent()) {
                Skill skill = skillOpt.get();

                // Check if the current user is the owner of the skill
                Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                String username = auth.getName();
                boolean isOwner = skill.getUser().getUsername().equals(username);
                boolean isAdmin = auth.getAuthorities().stream()
                        .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

                if (isOwner || isAdmin) {
                    skillService.deleteSkill(id);
                    return ResponseEntity.ok().build();
                } else {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You don't have permission to delete this skill");
                }
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}/approve")
    public ResponseEntity<?> approveSkill(@PathVariable Long id) {
        try {
            skillService.approveSkill(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}/reject")
    public ResponseEntity<?> rejectSkill(@PathVariable Long id) {
        try {
            skillService.rejectSkill(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/pending-approval")
    public ResponseEntity<List<SkillDTO>> getPendingApprovalSkills() {
        List<Skill> skills = skillService.getPendingApprovalSkills();
        List<SkillDTO> skillDTOs = skills.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(skillDTOs);
    }

    // Helper method to convert Skill to SkillDTO
    private SkillDTO convertToDTO(Skill skill) {
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
}
