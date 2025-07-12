package com.SkillsSwap.skillswap.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "feedbacks")
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "reviewer_id", nullable = false)
    private User reviewer;

    @ManyToOne
    @JoinColumn(name = "recipient_id", nullable = false)
    private User recipient;

    @ManyToOne
    @JoinColumn(name = "swap_request_id", nullable = false)
    private SwapRequest swapRequest;

    @Column(nullable = false)
    private int rating;

    private String comment;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    // Constructors
    public Feedback() {
    }

    public Feedback(User reviewer, User recipient, SwapRequest swapRequest, int rating, String comment) {
        this.reviewer = reviewer;
        this.recipient = recipient;
        this.swapRequest = swapRequest;
        this.rating = rating;
        this.comment = comment;
        this.createdAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getReviewer() {
        return reviewer;
    }

    public void setReviewer(User reviewer) {
        this.reviewer = reviewer;
    }

    public User getRecipient() {
        return recipient;
    }

    public void setRecipient(User recipient) {
        this.recipient = recipient;
    }

    public SwapRequest getSwapRequest() {
        return swapRequest;
    }

    public void setSwapRequest(SwapRequest swapRequest) {
        this.swapRequest = swapRequest;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}