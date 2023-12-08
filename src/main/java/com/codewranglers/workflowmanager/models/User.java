package com.codewranglers.workflowmanager.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

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
    private String username; // Combo of lastname first initial (ie: lanninga)
    private String password; // Encoded w/ Bcrypt SHA256
    private String firstname;
    private String lastname;
    private String email; // Automatically generated using username + desired domain
    private Integer role; // 1:Product Manager, 2: Member, 3: Admin

    public User() {
    }

    public User(String password, String firstname, String lastname) {
        this.password = password;
        this.firstname = firstname;
        this.lastname = lastname;
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

    public Integer getRole() {
        return role;
    }

    public void setRole(Integer role) {
        this.role = role;
    }
}
