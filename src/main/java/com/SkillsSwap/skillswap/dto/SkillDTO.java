package com.SkillsSwap.skillswap.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class SkillDTO {

    private Long id;

    @NotBlank(message = "Skill name is required")
    @Size(max = 30, message = "Skill name must be at most 30 characters")
    private String name;

    @Size(max = 200, message = "Description must be at most 200 characters")
    private String description;

    @NotNull(message = "Skill type (offered/wanted) is required")
    private boolean isOffered;

    private Long userId;

    private String username;

    private boolean isApproved = true;

    // Constructors
    public SkillDTO() {
    }

    public SkillDTO(Long id, String name, String description, boolean isOffered, Long userId, String username, boolean isApproved) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.isOffered = isOffered;
        this.userId = userId;
        this.username = username;
        this.isApproved = isApproved;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isOffered() {
        return isOffered;
    }

    public void setOffered(boolean offered) {
        isOffered = offered;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isApproved() {
        return isApproved;
    }

    public void setApproved(boolean approved) {
        isApproved = approved;
    }
}
