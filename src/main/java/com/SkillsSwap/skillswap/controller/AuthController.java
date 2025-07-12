package com.SkillsSwap.skillswap.controller;

import com.SkillsSwap.skillswap.dto.AuthRequest;
import com.SkillsSwap.skillswap.dto.AuthResponse;
import com.SkillsSwap.skillswap.dto.UserDTO;
import com.SkillsSwap.skillswap.model.User;
import com.SkillsSwap.skillswap.security.JwtTokenProvider;
import com.SkillsSwap.skillswap.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;

    public AuthController(AuthenticationManager authenticationManager, 
                         JwtTokenProvider jwtTokenProvider, 
                         UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody AuthRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginRequest.getUsername(),
                    loginRequest.getPassword()
                )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtTokenProvider.generateToken(authentication.getName());

            User user = userService.findUserByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

            AuthResponse response = new AuthResponse(
                jwt,
                user.getUsername(),
                user.getId(),
                user.isAdmin()
            );

            return ResponseEntity.ok(response);
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserDTO userDTO) {
        try {
            // Create a new user
            User user = new User();
            user.setUsername(userDTO.getUsername());
            user.setPassword(userDTO.getPassword());
            user.setName(userDTO.getName());
            user.setLocation(userDTO.getLocation());
            user.setProfilePhoto(userDTO.getProfilePhoto());
            user.setAvailability(userDTO.getAvailability());
            user.setPublic(userDTO.isPublic());

            User registeredUser = userService.registerUser(user);

            // Generate token for the new user
            String jwt = jwtTokenProvider.generateToken(registeredUser.getUsername());

            AuthResponse response = new AuthResponse(
                jwt,
                registeredUser.getUsername(),
                registeredUser.getId(),
                registeredUser.isAdmin()
            );

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
