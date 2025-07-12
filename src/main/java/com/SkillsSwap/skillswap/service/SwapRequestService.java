package com.SkillsSwap.skillswap.service;

import com.SkillsSwap.skillswap.exception.BadRequestException;
import com.SkillsSwap.skillswap.exception.ResourceNotFoundException;
import com.SkillsSwap.skillswap.model.Skill;
import com.SkillsSwap.skillswap.model.SwapRequest;
import com.SkillsSwap.skillswap.model.SwapRequest.SwapStatus;
import com.SkillsSwap.skillswap.model.User;
import com.SkillsSwap.skillswap.repository.SkillRepository;
import com.SkillsSwap.skillswap.repository.SwapRequestRepository;
import com.SkillsSwap.skillswap.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class SwapRequestService {

    private final SwapRequestRepository swapRequestRepository;
    private final UserRepository userRepository;
    private final SkillRepository skillRepository;

    public SwapRequestService(SwapRequestRepository swapRequestRepository, 
                             UserRepository userRepository,
                             SkillRepository skillRepository) {
        this.swapRequestRepository = swapRequestRepository;
        this.userRepository = userRepository;
        this.skillRepository = skillRepository;
    }

    public List<SwapRequest> getAllSwapRequests() {
        return swapRequestRepository.findAll();
    }

    public SwapRequest getSwapRequestById(Long id) {
        return swapRequestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("SwapRequest", "id", id));
    }

    public Optional<SwapRequest> findSwapRequestById(Long id) {
        return swapRequestRepository.findById(id);
    }

    public List<SwapRequest> getSwapRequestsByRequester(User requester) {
        return swapRequestRepository.findByRequester(requester);
    }

    public List<SwapRequest> getSwapRequestsByProvider(User provider) {
        return swapRequestRepository.findByProvider(provider);
    }

    public List<SwapRequest> getSwapRequestsByUser(User user) {
        return swapRequestRepository.findByUser(user);
    }

    public List<SwapRequest> getSwapRequestsByUserAndStatus(User user, SwapStatus status) {
        return swapRequestRepository.findByUserAndStatus(user, status);
    }

    public List<SwapRequest> getSwapRequestsByStatus(SwapStatus status) {
        return swapRequestRepository.findByStatus(status);
    }

    @Transactional
    public SwapRequest createSwapRequest(Long requesterId, Long providerId, 
                                        Long requestedSkillId, Long offeredSkillId, 
                                        String message) {
        User requester = userRepository.findById(requesterId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", requesterId));

        User provider = userRepository.findById(providerId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", providerId));

        Skill requestedSkill = skillRepository.findById(requestedSkillId)
                .orElseThrow(() -> new ResourceNotFoundException("Skill", "id", requestedSkillId));

        Skill offeredSkill = skillRepository.findById(offeredSkillId)
                .orElseThrow(() -> new ResourceNotFoundException("Skill", "id", offeredSkillId));

        // Validate that the requested skill belongs to the provider and is offered
        if (!requestedSkill.getUser().getId().equals(providerId) || !requestedSkill.isOffered()) {
            throw new BadRequestException("Invalid requested skill. The skill must belong to the provider and be offered.");
        }

        // Validate that the offered skill belongs to the requester and is offered
        if (!offeredSkill.getUser().getId().equals(requesterId) || !offeredSkill.isOffered()) {
            throw new BadRequestException("Invalid offered skill. The skill must belong to the requester and be offered.");
        }

        // Validate that both skills are approved
        if (!requestedSkill.isApproved() || !offeredSkill.isApproved()) {
            throw new BadRequestException("Skills must be approved before creating a swap request.");
        }

        SwapRequest swapRequest = new SwapRequest(requester, provider, requestedSkill, offeredSkill, message);
        swapRequest.setRequestDate(LocalDateTime.now());
        swapRequest.setStatus(SwapStatus.PENDING);

        return swapRequestRepository.save(swapRequest);
    }

    @Transactional
    public SwapRequest acceptSwapRequest(Long id) {
        SwapRequest swapRequest = swapRequestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("SwapRequest", "id", id));

        if (swapRequest.getStatus() != SwapStatus.PENDING) {
            throw new BadRequestException("Swap request is not in PENDING status. Current status: " + swapRequest.getStatus());
        }

        swapRequest.setStatus(SwapStatus.ACCEPTED);
        swapRequest.setResponseDate(LocalDateTime.now());

        return swapRequestRepository.save(swapRequest);
    }

    @Transactional
    public SwapRequest rejectSwapRequest(Long id) {
        SwapRequest swapRequest = swapRequestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("SwapRequest", "id", id));

        if (swapRequest.getStatus() != SwapStatus.PENDING) {
            throw new BadRequestException("Swap request is not in PENDING status. Current status: " + swapRequest.getStatus());
        }

        swapRequest.setStatus(SwapStatus.REJECTED);
        swapRequest.setResponseDate(LocalDateTime.now());

        return swapRequestRepository.save(swapRequest);
    }

    @Transactional
    public SwapRequest completeSwapRequest(Long id) {
        SwapRequest swapRequest = swapRequestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("SwapRequest", "id", id));

        if (swapRequest.getStatus() != SwapStatus.ACCEPTED) {
            throw new BadRequestException("Swap request is not in ACCEPTED status. Current status: " + swapRequest.getStatus());
        }

        swapRequest.setStatus(SwapStatus.COMPLETED);

        return swapRequestRepository.save(swapRequest);
    }

    @Transactional
    public SwapRequest cancelSwapRequest(Long id) {
        SwapRequest swapRequest = swapRequestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("SwapRequest", "id", id));

        if (swapRequest.getStatus() != SwapStatus.PENDING && swapRequest.getStatus() != SwapStatus.ACCEPTED) {
            throw new BadRequestException("Swap request cannot be cancelled in its current status: " + swapRequest.getStatus());
        }

        swapRequest.setStatus(SwapStatus.CANCELLED);

        return swapRequestRepository.save(swapRequest);
    }

    public long countSwapRequestsByStatus(SwapStatus status) {
        return swapRequestRepository.countByStatus(status);
    }
}
