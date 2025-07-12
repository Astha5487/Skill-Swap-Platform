package com.SkillsSwap.skillswap.config;

import com.SkillsSwap.skillswap.model.Skill;
import com.SkillsSwap.skillswap.model.User;
import com.SkillsSwap.skillswap.repository.SkillRepository;
import com.SkillsSwap.skillswap.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Configuration
public class DataInitializer {

    private final String[] predefinedSkills = {
            "Java", "React", "HTML", "CSS", "JavaScript", "Python", "C", "C++", "C#",
            "PHP", "Ruby", "Swift", "Kotlin", "Go", "Rust", "TypeScript", "Angular",
            "Vue.js", "Node.js", "Express.js", "Django", "Flask", "Spring", "Hibernate",
            "SQL", "MongoDB", "PostgreSQL", "MySQL", "Firebase", "AWS", "Docker",
            "Kubernetes", "Git", "DevOps", "Machine Learning", "Artificial Intelligence",
            "Data Science", "Blockchain", "UI/UX Design", "Graphic Design", "Photography",
            "Video Editing", "Content Writing", "Digital Marketing", "SEO", "Social Media Marketing"
    };

    @Bean
    public CommandLineRunner initData(UserRepository userRepository, 
                                     SkillRepository skillRepository,
                                     PasswordEncoder passwordEncoder) {
        return args -> {
            // Create admin user if it doesn't exist
            if (userRepository.findByUsername("admin").isEmpty()) {
                User adminUser = new User();
                adminUser.setName("Admin User");
                adminUser.setUsername("admin");
                adminUser.setPassword(passwordEncoder.encode("admin123"));
                adminUser.setAdmin(true);
                adminUser.setAvailability("Always");
                userRepository.save(adminUser);
                System.out.println("Admin user created");
            }

            // Create system user for predefined skills if it doesn't exist
            Optional<User> systemUserOpt = userRepository.findByUsername("system");
            User systemUser;

            if (systemUserOpt.isEmpty()) {
                systemUser = new User();
                systemUser.setName("System");
                systemUser.setUsername("system");
                systemUser.setPassword(passwordEncoder.encode("system123"));
                systemUser.setAvailability("Always");
                systemUser = userRepository.save(systemUser);
                System.out.println("System user created");
            } else {
                systemUser = systemUserOpt.get();
            }

            // Get existing skill names
            List<String> existingSkillNames = skillRepository.findAllDistinctSkillNames();

            // Add predefined skills if they don't exist
            for (String skillName : predefinedSkills) {
                if (!existingSkillNames.contains(skillName)) {
                    // Create as both offered and wanted skills

                    // Offered skill
                    Skill offeredSkill = new Skill();
                    offeredSkill.setName(skillName);
                    offeredSkill.setDescription("System-generated skill: " + skillName);
                    offeredSkill.setOffered(true);
                    offeredSkill.setUser(systemUser);
                    offeredSkill.setApproved(true);
                    skillRepository.save(offeredSkill);

                    // Wanted skill
                    Skill wantedSkill = new Skill();
                    wantedSkill.setName(skillName);
                    wantedSkill.setDescription("System-generated skill: " + skillName);
                    wantedSkill.setOffered(false);
                    wantedSkill.setUser(systemUser);
                    wantedSkill.setApproved(true);
                    skillRepository.save(wantedSkill);

                    System.out.println("Added predefined skill: " + skillName);
                }
            }

            System.out.println("Data initialization completed");
        };
    }
}
