package com.SkillsSwap.skillswap.service;

import com.SkillsSwap.skillswap.exception.DuplicateResourceException;
import com.SkillsSwap.skillswap.exception.ResourceNotFoundException;
import com.SkillsSwap.skillswap.model.User;
import com.SkillsSwap.skillswap.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public User registerUser(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new DuplicateResourceException("User", "username", user.getUsername());
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setAdmin(false);
        user.setActive(true);

        return userRepository.save(user);
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));
    }

    public Optional<User> findUserById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public List<User> getAllPublicUsers() {
        return userRepository.findByIsPublicTrueAndIsActiveTrue();
    }

    public List<User> searchUsersByOfferedSkill(String skillName) {
        return userRepository.findByOfferedSkillsContaining(skillName);
    }

    public List<User> searchUsersByWantedSkill(String skillName) {
        return userRepository.findByWantedSkillsContaining(skillName);
    }

    @Transactional
    public User updateUser(User user) {
        return userRepository.save(user);
    }

    @Transactional
    public void deactivateUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        user.setActive(false);
        userRepository.save(user);
    }

    @Transactional
    public void activateUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        user.setActive(true);
        userRepository.save(user);
    }

    @Transactional
    public void makeAdmin(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        user.setAdmin(true);
        userRepository.save(user);
    }

    @Transactional
    public void removeAdmin(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        user.setAdmin(false);
        userRepository.save(user);
    }

    public List<User> getAllAdmins() {
        return userRepository.findByIsAdmin(true);
    }
}
