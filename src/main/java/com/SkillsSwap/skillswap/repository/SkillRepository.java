package com.SkillsSwap.skillswap.repository;

import com.SkillsSwap.skillswap.model.Skill;
import com.SkillsSwap.skillswap.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SkillRepository extends JpaRepository<Skill, Long> {
    
    List<Skill> findByUser(User user);
    
    List<Skill> findByUserAndIsOffered(User user, boolean isOffered);
    
    List<Skill> findByNameContainingIgnoreCase(String name);
    
    @Query("SELECT s FROM Skill s WHERE s.name LIKE %?1% AND s.isOffered = ?2 AND s.isApproved = true")
    List<Skill> findByNameContainingAndIsOffered(String name, boolean isOffered);
    
    List<Skill> findByIsApproved(boolean isApproved);
    
    @Query("SELECT DISTINCT s.name FROM Skill s WHERE s.isApproved = true ORDER BY s.name")
    List<String> findAllDistinctSkillNames();
}