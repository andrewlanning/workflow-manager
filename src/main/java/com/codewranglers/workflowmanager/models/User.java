package com.codewranglers.workflowmanager.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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

// TODO: Review security stuff with Luke Sperkowski

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String username; // Combo of lastname first initial (ie: lanninga)
    @NotNull
    private String pwhash; // Encoded w/ Bcrypt SHA256
    private String firstname;
    private String lastname;
    private String email; // Automatically generated using username + desired domain
    private Integer role; // 1:Product Manager, 2: Member, 3: Admin

    public User() {
    }

    public User(String password, String firstname, String lastname, Integer role) {
        this.pwhash = encoder.encode(password);
        this.firstname = firstname;
        this.lastname = lastname;
        this.role = role;
    }

    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public boolean isMatchingPassword(String password) {
        return encoder.matches(password, pwhash);
    }

    public Integer getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public Integer getRole() {
        return role;
    }

    public void setRole(Integer role) {
        this.role = role;
    }
}
