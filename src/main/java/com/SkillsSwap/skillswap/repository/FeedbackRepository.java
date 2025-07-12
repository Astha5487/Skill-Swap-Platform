package com.SkillsSwap.skillswap.repository;

import com.SkillsSwap.skillswap.model.Feedback;
import com.SkillsSwap.skillswap.model.SwapRequest;
import com.SkillsSwap.skillswap.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    
    List<Feedback> findByReviewer(User reviewer);
    
    List<Feedback> findByRecipient(User recipient);
    
    List<Feedback> findBySwapRequest(SwapRequest swapRequest);
    
    Optional<Feedback> findByReviewerAndSwapRequest(User reviewer, SwapRequest swapRequest);
    
    @Query("SELECT AVG(f.rating) FROM Feedback f WHERE f.recipient = ?1")
    Double getAverageRatingForUser(User user);
    
    List<Feedback> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
    
    @Query("SELECT f FROM Feedback f WHERE f.rating <= ?1")
    List<Feedback> findByRatingLessThanEqual(int rating);
    
    @Query("SELECT f FROM Feedback f WHERE f.rating >= ?1")
    List<Feedback> findByRatingGreaterThanEqual(int rating);
}