package com.SkillsSwap.skillswap.dto;

import com.SkillsSwap.skillswap.model.SwapRequest.SwapStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public class SwapRequestDTO {

    private Long id;

    @NotNull(message = "Requester ID is required")
    private Long requesterId;
    private String requesterUsername;

    @NotNull(message = "Provider ID is required")
    private Long providerId;
    private String providerUsername;

    @NotNull(message = "Requested skill ID is required")
    private Long requestedSkillId;
    private String requestedSkillName;

    @NotNull(message = "Offered skill ID is required")
    private Long offeredSkillId;
    private String offeredSkillName;

    private LocalDateTime requestDate;
    private LocalDateTime responseDate;
    private SwapStatus status;
    @Size(max = 300, message = "Message must be at most 300 characters")
    private String message;

    // Constructors
    public SwapRequestDTO() {
    }

    public SwapRequestDTO(Long id, Long requesterId, String requesterUsername, Long providerId, 
                         String providerUsername, Long requestedSkillId, String requestedSkillName, 
                         Long offeredSkillId, String offeredSkillName, LocalDateTime requestDate, 
                         LocalDateTime responseDate, SwapStatus status, String message) {
        this.id = id;
        this.requesterId = requesterId;
        this.requesterUsername = requesterUsername;
        this.providerId = providerId;
        this.providerUsername = providerUsername;
        this.requestedSkillId = requestedSkillId;
        this.requestedSkillName = requestedSkillName;
        this.offeredSkillId = offeredSkillId;
        this.offeredSkillName = offeredSkillName;
        this.requestDate = requestDate;
        this.responseDate = responseDate;
        this.status = status;
        this.message = message;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRequesterId() {
        return requesterId;
    }

    public void setRequesterId(Long requesterId) {
        this.requesterId = requesterId;
    }

    public String getRequesterUsername() {
        return requesterUsername;
    }

    public void setRequesterUsername(String requesterUsername) {
        this.requesterUsername = requesterUsername;
    }

    public Long getProviderId() {
        return providerId;
    }

    public void setProviderId(Long providerId) {
        this.providerId = providerId;
    }

    public String getProviderUsername() {
        return providerUsername;
    }

    public void setProviderUsername(String providerUsername) {
        this.providerUsername = providerUsername;
    }

    public Long getRequestedSkillId() {
        return requestedSkillId;
    }

    public void setRequestedSkillId(Long requestedSkillId) {
        this.requestedSkillId = requestedSkillId;
    }

    public String getRequestedSkillName() {
        return requestedSkillName;
    }

    public void setRequestedSkillName(String requestedSkillName) {
        this.requestedSkillName = requestedSkillName;
    }

    public Long getOfferedSkillId() {
        return offeredSkillId;
    }

    public void setOfferedSkillId(Long offeredSkillId) {
        this.offeredSkillId = offeredSkillId;
    }

    public String getOfferedSkillName() {
        return offeredSkillName;
    }

    public void setOfferedSkillName(String offeredSkillName) {
        this.offeredSkillName = offeredSkillName;
    }

    public LocalDateTime getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(LocalDateTime requestDate) {
        this.requestDate = requestDate;
    }

    public LocalDateTime getResponseDate() {
        return responseDate;
    }

    public void setResponseDate(LocalDateTime responseDate) {
        this.responseDate = responseDate;
    }

    public SwapStatus getStatus() {
        return status;
    }

    public void setStatus(SwapStatus status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
