package com.SkillsSwap.skillswap.repository;

import com.SkillsSwap.skillswap.model.SwapRequest;
import com.SkillsSwap.skillswap.model.SwapRequest.SwapStatus;
import com.SkillsSwap.skillswap.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SwapRequestRepository extends JpaRepository<SwapRequest, Long> {
    
    List<SwapRequest> findByRequester(User requester);
    
    List<SwapRequest> findByProvider(User provider);
    
    List<SwapRequest> findByRequesterAndStatus(User requester, SwapStatus status);
    
    List<SwapRequest> findByProviderAndStatus(User provider, SwapStatus status);
    
    @Query("SELECT sr FROM SwapRequest sr WHERE (sr.requester = ?1 OR sr.provider = ?1) AND sr.status = ?2")
    List<SwapRequest> findByUserAndStatus(User user, SwapStatus status);
    
    @Query("SELECT sr FROM SwapRequest sr WHERE (sr.requester = ?1 OR sr.provider = ?1)")
    List<SwapRequest> findByUser(User user);
    
    List<SwapRequest> findByStatus(SwapStatus status);
    
    List<SwapRequest> findByRequestDateBetween(LocalDateTime start, LocalDateTime end);
    
    @Query("SELECT COUNT(sr) FROM SwapRequest sr WHERE sr.status = ?1")
    long countByStatus(SwapStatus status);
}