package com.SkillsSwap.skillswap.service;

import com.SkillsSwap.skillswap.exception.ResourceNotFoundException;
import com.SkillsSwap.skillswap.model.Skill;
import com.SkillsSwap.skillswap.model.User;
import com.SkillsSwap.skillswap.repository.SkillRepository;
import com.SkillsSwap.skillswap.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class SkillService {

    private final SkillRepository skillRepository;
    private final UserRepository userRepository;

    public SkillService(SkillRepository skillRepository, UserRepository userRepository) {
        this.skillRepository = skillRepository;
        this.userRepository = userRepository;
    }

    public List<Skill> getAllSkills() {
        return skillRepository.findAll();
    }

    public Skill getSkillById(Long id) {
        return skillRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Skill", "id", id));
    }

    public Optional<Skill> findSkillById(Long id) {
        return skillRepository.findById(id);
    }

    public List<Skill> getSkillsByUser(User user) {
        return skillRepository.findByUser(user);
    }

    public List<Skill> getOfferedSkillsByUser(User user) {
        return skillRepository.findByUserAndIsOffered(user, true);
    }

    public List<Skill> getWantedSkillsByUser(User user) {
        return skillRepository.findByUserAndIsOffered(user, false);
    }

    public List<Skill> searchSkillsByName(String name) {
        return skillRepository.findByNameContainingIgnoreCase(name);
    }

    public List<Skill> searchOfferedSkillsByName(String name) {
        return skillRepository.findByNameContainingAndIsOffered(name, true);
    }

    public List<Skill> searchWantedSkillsByName(String name) {
        return skillRepository.findByNameContainingAndIsOffered(name, false);
    }

    public List<String> getAllDistinctSkillNames() {
        return skillRepository.findAllDistinctSkillNames();
    }

    @Transactional
    public Skill addSkill(Skill skill, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        skill.setUser(user);

        // Admin approval only needed for offered skills
        if (skill.isOffered()) {
            skill.setApproved(false);
        } else {
            skill.setApproved(true);
        }

        return skillRepository.save(skill);
    }

    @Transactional
    public Skill updateSkill(Skill skill) {
        Skill existingSkill = skillRepository.findById(skill.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Skill", "id", skill.getId()));

        existingSkill.setName(skill.getName());
        existingSkill.setDescription(skill.getDescription());

        // If changing from wanted to offered, require approval
        if (!existingSkill.isOffered() && skill.isOffered()) {
            existingSkill.setApproved(false);
        }

        existingSkill.setOffered(skill.isOffered());

        return skillRepository.save(existingSkill);
    }

    @Transactional
    public void deleteSkill(Long id) {
        if (!skillRepository.existsById(id)) {
            throw new ResourceNotFoundException("Skill", "id", id);
        }
        skillRepository.deleteById(id);
    }

    @Transactional
    public void approveSkill(Long id) {
        Skill skill = skillRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Skill", "id", id));
        skill.setApproved(true);
        skillRepository.save(skill);
    }

    @Transactional
    public void rejectSkill(Long id) {
        Skill skill = skillRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Skill", "id", id));
        skill.setApproved(false);
        skillRepository.save(skill);
    }

    public List<Skill> getPendingApprovalSkills() {
        return skillRepository.findByIsApproved(false);
    }
}
