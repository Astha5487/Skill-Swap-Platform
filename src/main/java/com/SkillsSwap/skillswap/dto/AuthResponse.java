package com.SkillsSwap.skillswap.dto;

public class AuthResponse {

    private String token;
    private String username;
    private Long userId;
    private boolean isAdmin;

    // Constructors
    public AuthResponse() {
    }

    public AuthResponse(String token, String username, Long userId, boolean isAdmin) {
        this.token = token;
        this.username = username;
        this.userId = userId;
        this.isAdmin = isAdmin;
    }

    // Getters and Setters
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }
}