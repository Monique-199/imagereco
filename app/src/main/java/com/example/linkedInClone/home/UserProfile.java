package com.example.linkedInClone.home;

public class UserProfile {
    private String profilePictureUrl; // Resource ID of the profile picture
    private String userName;
    private String gender;
    private String phoneNumber;
    private String email;
    private String shortBio;
    private String skills;

    public UserProfile(String profilePicture, String username, String gender, String phoneNumber,String email,String shortBio, String skills) {
        this.profilePictureUrl = profilePicture;
        this.userName = username;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.shortBio = shortBio;
        this.skills = skills;
    }
    public UserProfile(){

    }

    public void setProfilePictureUrl(String profilePictureUrl) {
        this.profilePictureUrl = profilePictureUrl;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

    public void setSkills(String skills) {
        this.skills = skills;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }

    public String getUserName() {
        return userName;
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

    public String getSkills() {
        return skills;
    }
}