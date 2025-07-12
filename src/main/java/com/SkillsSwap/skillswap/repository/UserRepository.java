package com.SkillsSwap.skillswap.repository;

import com.SkillsSwap.skillswap.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    Optional<User> findByUsername(String username);
    
    boolean existsByUsername(String username);
    
    List<User> findByIsPublicTrueAndIsActiveTrue();
    
    @Query("SELECT u FROM User u JOIN u.offeredSkills s WHERE s.name LIKE %?1% AND u.isPublic = true AND u.isActive = true")
    List<User> findByOfferedSkillsContaining(String skillName);
    
    @Query("SELECT u FROM User u JOIN u.wantedSkills s WHERE s.name LIKE %?1% AND u.isPublic = true AND u.isActive = true")
    List<User> findByWantedSkillsContaining(String skillName);
    
    List<User> findByIsAdmin(boolean isAdmin);
}