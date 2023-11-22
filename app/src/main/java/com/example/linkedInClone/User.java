package com.example.linkedInClone;

public class User {
    String UserName, Email, passowrd, confirmPassword, gender, ShortBio, Skills;

    public User(String userName, String email, String passowrd, String confirmPassword, String gender, String shortBio, String skills) {
        this.UserName = userName;
        this.Email = email;
        this.passowrd = passowrd;
        this.confirmPassword = confirmPassword;
        this.gender = gender;
        ShortBio = shortBio;
        Skills = skills;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPassowrd() {
        return passowrd;
    }

    public void setPassowrd(String passowrd) {
        this.passowrd = passowrd;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getShortBio() {
        return ShortBio;
    }

    public void setShortBio(String shortBio) {
        ShortBio = shortBio;
    }

    public String getSkills() {
        return Skills;
    }

    public void setSkills(String skills) {
        Skills = skills;
    }
}
