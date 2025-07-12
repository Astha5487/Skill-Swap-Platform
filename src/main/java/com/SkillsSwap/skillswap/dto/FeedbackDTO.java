package com.SkillsSwap.skillswap.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public class FeedbackDTO {

    private Long id;

    @NotNull(message = "Reviewer ID is required")
    private Long reviewerId;
    private String reviewerUsername;

    @NotNull(message = "Recipient ID is required")
    private Long recipientId;
    private String recipientUsername;

    @NotNull(message = "Swap request ID is required")
    private Long swapRequestId;

    @NotNull(message = "Rating is required")
    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating must be at most 5")
    private int rating;

    @Size(max = 500, message = "Comment must be at most 500 characters")
    private String comment;
    private LocalDateTime createdAt;

    // Constructors
    public FeedbackDTO() {
    }

    public FeedbackDTO(Long id, Long reviewerId, String reviewerUsername, Long recipientId, 
                      String recipientUsername, Long swapRequestId, int rating, 
                      String comment, LocalDateTime createdAt) {
        this.id = id;
        this.reviewerId = reviewerId;
        this.reviewerUsername = reviewerUsername;
        this.recipientId = recipientId;
        this.recipientUsername = recipientUsername;
        this.swapRequestId = swapRequestId;
        this.rating = rating;
        this.comment = comment;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getReviewerId() {
        return reviewerId;
    }

    public void setReviewerId(Long reviewerId) {
        this.reviewerId = reviewerId;
    }

    public String getReviewerUsername() {
        return reviewerUsername;
    }

    public void setReviewerUsername(String reviewerUsername) {
        this.reviewerUsername = reviewerUsername;
    }

    public Long getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(Long recipientId) {
        this.recipientId = recipientId;
    }

    public String getRecipientUsername() {
        return recipientUsername;
    }

    public void setRecipientUsername(String recipientUsername) {
        this.recipientUsername = recipientUsername;
    }

    public Long getSwapRequestId() {
        return swapRequestId;
    }

    public void setSwapRequestId(Long swapRequestId) {
        this.swapRequestId = swapRequestId;
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
