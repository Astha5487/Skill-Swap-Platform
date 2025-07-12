package com.SkillsSwap.skillswap.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "swap_requests")
public class SwapRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "requester_id", nullable = false)
    private User requester;

    @ManyToOne
    @JoinColumn(name = "provider_id", nullable = false)
    private User provider;

    @ManyToOne
    @JoinColumn(name = "requested_skill_id", nullable = false)
    private Skill requestedSkill;

    @ManyToOne
    @JoinColumn(name = "offered_skill_id", nullable = false)
    private Skill offeredSkill;

    @Column(nullable = false)
    private LocalDateTime requestDate;

    private LocalDateTime responseDate;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private SwapStatus status;

    private String message;

    // Constructors
    public SwapRequest() {
    }

    public SwapRequest(User requester, User provider, Skill requestedSkill, Skill offeredSkill, String message) {
        this.requester = requester;
        this.provider = provider;
        this.requestedSkill = requestedSkill;
        this.offeredSkill = offeredSkill;
        this.message = message;
        this.requestDate = LocalDateTime.now();
        this.status = SwapStatus.PENDING;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getRequester() {
        return requester;
    }

    public void setRequester(User requester) {
        this.requester = requester;
    }

    public User getProvider() {
        return provider;
    }

    public void setProvider(User provider) {
        this.provider = provider;
    }

    public Skill getRequestedSkill() {
        return requestedSkill;
    }

    public void setRequestedSkill(Skill requestedSkill) {
        this.requestedSkill = requestedSkill;
    }

    public Skill getOfferedSkill() {
        return offeredSkill;
    }

    public void setOfferedSkill(Skill offeredSkill) {
        this.offeredSkill = offeredSkill;
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

    // Enum for swap request status
    public enum SwapStatus {
        PENDING,
        ACCEPTED,
        REJECTED,
        COMPLETED,
        CANCELLED
    }
}