package com.SkillsSwap.skillswap.model;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    private String location;

    private String profilePhoto;

    @Column(nullable = false)
    private String availability;

    @Column(nullable = false)
    private boolean isPublic = true;

    @Column(nullable = false)
    private boolean isAdmin = false;

    @Column(nullable = false)
    private boolean isActive = true;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Skill> offeredSkills = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Skill> wantedSkills = new HashSet<>();

    @OneToMany(mappedBy = "requester")
    private Set<SwapRequest> sentRequests = new HashSet<>();

    @OneToMany(mappedBy = "provider")
    private Set<SwapRequest> receivedRequests = new HashSet<>();

    @OneToMany(mappedBy = "reviewer")
    private Set<Feedback> givenFeedbacks = new HashSet<>();

    @OneToMany(mappedBy = "recipient")
    private Set<Feedback> receivedFeedbacks = new HashSet<>();

    // Constructors
    public User() {
    }

    public User(String username, String password, String name, String availability) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.availability = availability;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getProfilePhoto() {
        return profilePhoto;
    }

    public void setProfilePhoto(String profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean isPublic) {
        this.isPublic = isPublic;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

    public Set<Skill> getOfferedSkills() {
        return offeredSkills;
    }

    public void setOfferedSkills(Set<Skill> offeredSkills) {
        this.offeredSkills = offeredSkills;
    }

    public Set<Skill> getWantedSkills() {
        return wantedSkills;
    }

    public void setWantedSkills(Set<Skill> wantedSkills) {
        this.wantedSkills = wantedSkills;
    }

    public Set<SwapRequest> getSentRequests() {
        return sentRequests;
    }

    public void setSentRequests(Set<SwapRequest> sentRequests) {
        this.sentRequests = sentRequests;
    }

    public Set<SwapRequest> getReceivedRequests() {
        return receivedRequests;
    }

    public void setReceivedRequests(Set<SwapRequest> receivedRequests) {
        this.receivedRequests = receivedRequests;
    }

    public Set<Feedback> getGivenFeedbacks() {
        return givenFeedbacks;
    }

    public void setGivenFeedbacks(Set<Feedback> givenFeedbacks) {
        this.givenFeedbacks = givenFeedbacks;
    }

    public Set<Feedback> getReceivedFeedbacks() {
        return receivedFeedbacks;
    }

    public void setReceivedFeedbacks(Set<Feedback> receivedFeedbacks) {
        this.receivedFeedbacks = receivedFeedbacks;
    }
}