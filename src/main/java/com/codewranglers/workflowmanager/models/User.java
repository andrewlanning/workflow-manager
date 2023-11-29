package com.codewranglers.workflowmanager.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class User {

    @Id
    private Long id;
    private String username;
    private String password;
    private String firstname;
    private String lastname;
    private String email;
    private String role;

    private String recoveryQuestion1;
    private String recoveryQuestion2;
    private String recoveryAnswer1;
    private String recoveryAnswer2;

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", email='" + email + '\'' +
                ", role='" + role + '\'' +
                ", recoveryQuestion1='" + recoveryQuestion1 + '\'' +
                ", recoveryQuestion2='" + recoveryQuestion2 + '\'' +
                ", recoveryAnswer1='" + recoveryAnswer1 + '\'' +
                ", recoveryAnswer2='" + recoveryAnswer2 + '\'' +
                '}';
    }

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

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getRecoveryQuestion1() {
        return recoveryQuestion1;
    }

    public void setRecoveryQuestion1(String recoveryQuestion1) {
        this.recoveryQuestion1 = recoveryQuestion1;
    }

    public String getRecoveryQuestion2() {
        return recoveryQuestion2;
    }

    public void setRecoveryQuestion2(String recoveryQuestion2) {
        this.recoveryQuestion2 = recoveryQuestion2;
    }

    public String getRecoveryAnswer1() {
        return recoveryAnswer1;
    }

    public void setRecoveryAnswer1(String recoveryAnswer1) {
        this.recoveryAnswer1 = recoveryAnswer1;
    }

    public String getRecoveryAnswer2() {
        return recoveryAnswer2;
    }

    public void setRecoveryAnswer2(String recoveryAnswer2) {
        this.recoveryAnswer2 = recoveryAnswer2;
    }
}
