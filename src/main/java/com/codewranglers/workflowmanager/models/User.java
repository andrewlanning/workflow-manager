package com.codewranglers.workflowmanager.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.validation.constraints.NotNull;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/*
 * This class provides the model for each User.
 * It is intended to be simplistic by nature for the sake of the project.
 *
 * Limitiations?
 * No recovery information...
 *
 * Created by Andrew Lanning 12-06-23
 */

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String username;

    @NotNull
    private String pwHash;

    private String firstname;
    private String lastname;
    private String email;
    private String role;

    public User() {}

    public User(String username, String password, String firstname, String lastname, String email, String role) {
        this.username = username;
        this.pwHash = encoder.encode(password);
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.role = role;
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

    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public boolean isMatchingPassword(String password) {
        return encoder.matches(password, pwHash);
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
