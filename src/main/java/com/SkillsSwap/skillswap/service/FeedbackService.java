package com.SkillsSwap.skillswap.service;

import com.SkillsSwap.skillswap.exception.BadRequestException;
import com.SkillsSwap.skillswap.exception.DuplicateResourceException;
import com.SkillsSwap.skillswap.exception.ResourceNotFoundException;
import com.SkillsSwap.skillswap.model.Feedback;
import com.SkillsSwap.skillswap.model.SwapRequest;
import com.SkillsSwap.skillswap.model.SwapRequest.SwapStatus;
import com.SkillsSwap.skillswap.model.User;
import com.SkillsSwap.skillswap.repository.FeedbackRepository;
import com.SkillsSwap.skillswap.repository.SwapRequestRepository;
import com.SkillsSwap.skillswap.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final UserRepository userRepository;
    private final SwapRequestRepository swapRequestRepository;

    public FeedbackService(FeedbackRepository feedbackRepository,
                          UserRepository userRepository,
                          SwapRequestRepository swapRequestRepository) {
        this.feedbackRepository = feedbackRepository;
        this.userRepository = userRepository;
        this.swapRequestRepository = swapRequestRepository;
    }

    public List<Feedback> getAllFeedback() {
        return feedbackRepository.findAll();
    }

    public Feedback getFeedbackById(Long id) {
        return feedbackRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Feedback", "id", id));
    }

    public Optional<Feedback> findFeedbackById(Long id) {
        return feedbackRepository.findById(id);
    }

    public List<Feedback> getFeedbackByReviewer(User reviewer) {
        return feedbackRepository.findByReviewer(reviewer);
    }

    public List<Feedback> getFeedbackByRecipient(User recipient) {
        return feedbackRepository.findByRecipient(recipient);
    }

    public List<Feedback> getFeedbackBySwapRequest(SwapRequest swapRequest) {
        return feedbackRepository.findBySwapRequest(swapRequest);
    }

    public Double getAverageRatingForUser(User user) {
        return feedbackRepository.getAverageRatingForUser(user);
    }

    @Transactional
    public Feedback createFeedback(Long reviewerId, Long recipientId, Long swapRequestId, int rating, String comment) {
        User reviewer = userRepository.findById(reviewerId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", reviewerId));

        User recipient = userRepository.findById(recipientId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", recipientId));

        SwapRequest swapRequest = swapRequestRepository.findById(swapRequestId)
                .orElseThrow(() -> new ResourceNotFoundException("SwapRequest", "id", swapRequestId));

        // Validate that the swap request is completed
        if (swapRequest.getStatus() != SwapStatus.COMPLETED) {
            throw new BadRequestException("Feedback can only be given for completed swap requests. Current status: " + swapRequest.getStatus());
        }

        // Validate that the reviewer is either the requester or provider of the swap request
        if (!swapRequest.getRequester().getId().equals(reviewerId) && !swapRequest.getProvider().getId().equals(reviewerId)) {
            throw new BadRequestException("Only participants of the swap can give feedback. User ID " + reviewerId + " is not a participant.");
        }

        // Validate that the recipient is the other participant of the swap request
        if (!swapRequest.getRequester().getId().equals(recipientId) && !swapRequest.getProvider().getId().equals(recipientId)) {
            throw new BadRequestException("Recipient must be a participant of the swap. User ID " + recipientId + " is not a participant.");
        }

        // Validate that the reviewer and recipient are different users
        if (reviewerId.equals(recipientId)) {
            throw new BadRequestException("Cannot give feedback to yourself.");
        }

        // Validate that the reviewer hasn't already given feedback for this swap request
        Optional<Feedback> existingFeedback = feedbackRepository.findByReviewerAndSwapRequest(reviewer, swapRequest);
        if (existingFeedback.isPresent()) {
            throw new DuplicateResourceException("Feedback", "reviewer and swap request", reviewerId + " and " + swapRequestId);
        }

        // Validate rating is between 1 and 5
        if (rating < 1 || rating > 5) {
            throw new BadRequestException("Rating must be between 1 and 5. Provided rating: " + rating);
        }

        Feedback feedback = new Feedback(reviewer, recipient, swapRequest, rating, comment);
        feedback.setCreatedAt(LocalDateTime.now());

        return feedbackRepository.save(feedback);
    }

    @Transactional
    public void deleteFeedback(Long id) {
        if (!feedbackRepository.existsById(id)) {
            throw new ResourceNotFoundException("Feedback", "id", id);
        }
        feedbackRepository.deleteById(id);
    }

    public List<Feedback> getFeedbackByRatingLessThanEqual(int rating) {
        return feedbackRepository.findByRatingLessThanEqual(rating);
    }

    public List<Feedback> getFeedbackByRatingGreaterThanEqual(int rating) {
        return feedbackRepository.findByRatingGreaterThanEqual(rating);
    }

    public List<Feedback> getFeedbackByDateRange(LocalDateTime start, LocalDateTime end) {
        return feedbackRepository.findByCreatedAtBetween(start, end);
    }
}
