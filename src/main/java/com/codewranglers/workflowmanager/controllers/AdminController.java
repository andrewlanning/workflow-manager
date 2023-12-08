package com.codewranglers.workflowmanager.controllers;


import com.codewranglers.workflowmanager.models.User;
import com.codewranglers.workflowmanager.models.data.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("")
    public String renderAdminPortal(){
        return "/admin/index";
    }

    @GetMapping("/user_management")
    public String renderUserManagementPortal() {
        return "admin/user_management/index";
    }

    @GetMapping("/user_management/create_user")
    public String renderUserCreationPortal(Model model) {
        model.addAttribute("user", new User());
        return "/admin/user_management/create_user";
    }

    @PostMapping("/user_management/create_user")
    public String renderUserCreated(@ModelAttribute("user") User user) {

        // Formatting for username
        String username = user.getLastname().toLowerCase() + user.getFirstname().substring(0, 1).toLowerCase();

        // Formatting for email TODO: consider changing @company.com to something that the admin can set.
        String email = username + "@company.com";

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(user.getPassword());

        user.setPassword(encodedPassword);
        user.setUsername(username);
        user.setEmail(email);


        userRepository.save(user);
        return "redirect:/admin/user_management";
    }

    @GetMapping("/role_management")
    public String renderRoleManagementPortal() {
        return "/admin/role_management/index";
    }

    @GetMapping("/workflow_management")
    public String renderWorkflowManagementPortal() {
        return "/admin/workflow_management/index";
    }
}
