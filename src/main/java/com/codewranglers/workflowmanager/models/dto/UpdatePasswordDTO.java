package com.codewranglers.workflowmanager.models.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class UpdatePasswordDTO {
    @NotNull(message = "Password is required")
    @NotBlank(message = "Password is required")
    @Size(min = 7, max = 64, message = "Invalid password length")
    private String password;

    @NotNull(message = "Password is required")
    @NotBlank(message = "Password is required")
    @Size(min = 7, max = 64, message = "Invalid password length")
    private String confirmPassword;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}
