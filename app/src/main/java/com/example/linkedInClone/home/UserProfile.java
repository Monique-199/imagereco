package com.example.linkedInClone.home;

import java.util.List;
public class UserProfile {
    private int profilePicture; // Resource ID of the profile picture
    private String username;
    private String gender;
    private String phoneNumber;
    private String email;
    private String shortBio;
    private List<String> skills;

    public UserProfile(int profilePicture, String username, String gender, String phoneNumber,String email,String shortBio, List<String> skills) {
        this.profilePicture = profilePicture;
        this.username = username;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.shortBio = shortBio;
        this.skills = skills;
    }

    public void setProfilePicture(int profilePicture) {
        this.profilePicture = profilePicture;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setShortBio(String shortBio) {
        this.shortBio = shortBio;
    }

    public void setSkills(List<String> skills) {
        this.skills = skills;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getProfilePicture() {
        return profilePicture;
    }

    public String getUsername() {
        return username;
    }

    public String getGender() {
        return gender;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getShortBio() {
        return shortBio;
    }

    public List<String> getSkills() {
        return skills;
    }
}