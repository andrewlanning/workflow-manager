package com.codewranglers.workflowmanager.controllers;


import com.codewranglers.workflowmanager.models.Job;
import com.codewranglers.workflowmanager.models.User;
import com.codewranglers.workflowmanager.models.data.JobRepository;
import com.codewranglers.workflowmanager.models.data.UserRepository;
import com.codewranglers.workflowmanager.models.dto.CreateUserDTO;
import com.codewranglers.workflowmanager.models.dto.UpdatePasswordDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JobRepository jobRepository;

    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @GetMapping("")
    public String renderAdminPortal(Model model) {
        List<String> pages = new ArrayList<>();
        pages.add("User Management");
        pages.add("Workflow Management");

        List<String> user = new ArrayList<>();
        user.add("Manufacturing Operator");
        user.add("Product Manager");
        user.add("Administrator");

        List<String> urlStrings = new ArrayList<>();
        urlStrings.add("usermanagement");
        urlStrings.add("workflowmanagement");

        model.addAttribute("pages", pages);
        model.addAttribute("user", user);
        model.addAttribute("url", urlStrings);
        return "/admin/index";
    }

    @GetMapping("/user_management")
    public String renderUserManagementPortal(Model model) {
        Iterable<User> users = userRepository.findAll();

        Map<User, String> userMap = new LinkedHashMap<>();

        for (User user : users) {
            if (user.getRole() == 1) {
                userMap.put(user, "Manager");
            }
            if (user.getRole() == 2) {
                userMap.put(user, "Member");
            }
            if (user.getRole() == 3) {
                userMap.put(user, "Administrator");
            }
        }
        model.addAttribute("users", userMap);
        return "admin/user_management/index";
    }

    @GetMapping("/user_management/create_user")
    public String renderUserCreationPortal(Model model) {
        model.addAttribute(new CreateUserDTO());
        return "admin/user_management/create_user";
    }


    @PostMapping("/user_management/create_user")
    public String renderUserCreated(@ModelAttribute @Valid CreateUserDTO createUserDTO, Errors errors, HttpServletRequest request) {

        if (errors.hasErrors()) {
            return "admin/user_management/create_user";
        }
        //check for existing user with username
        User existingUser = userRepository.findByUsername(createUserDTO.getUsername());
        if (existingUser != null) {
            errors.rejectValue("username", "username.alreadyExists", "That username is already in use.");
            return "admin/user_management/create_user";
        }
        //check password and confirmPassword match
        String password = createUserDTO.getPassword();
        String confirmPassword = createUserDTO.getConfirmPassword();
        if (!password.equals(confirmPassword)) {
            errors.rejectValue("password", "password.mismatch", "Passwords do not match.");
            return "admin/user_management/create_user";
        }

        User newUser = new User(createUserDTO.getFirstName(), createUserDTO.getLastName(), createUserDTO.getUsername(), createUserDTO.getEmail(), createUserDTO.getPassword(), createUserDTO.getRole());


        userRepository.save(newUser);
        return "redirect:/admin/user_management";
    }

    @GetMapping("/user_management/edit/{userId}")
    public String displayEditUserForm(Model model, @PathVariable int userId) {
        Optional<User> optUser = userRepository.findById(userId);
        if (optUser.isPresent()) {
            User user = optUser.get();
            model.addAttribute("title", "Edit User");
            model.addAttribute("user", user);
            return "admin/user_management/edit";
        } else {
            return "redirect:admin/user_management/edit";
        }
    }
    @PostMapping("/user_management/edit/{userId}")
    public String processEditUserForm(@PathVariable int userId,
                                      @ModelAttribute @Valid User editedUser,
                                      Errors errors, Model model) {

        Optional<User> optUser = userRepository.findById(userId);
        if (optUser.isPresent()) {
            User user = optUser.get();
            user.setUsername(editedUser.getUsername());
            user.setFirstname(editedUser.getFirstname());
            user.setLastname(editedUser.getLastname());
            user.setRole(editedUser.getRole());
            user.setEmail(editedUser.getEmail());
            userRepository.save(user);
        }
        if (errors.hasErrors()) {
            model.addAttribute("title", "Edit user");
            return "admin/user_management/edit";
        }

        return "redirect:/admin/user_management";
    }

    @GetMapping("/user_management/update_password/{userId}")
    public String displayUpdatePasswordForm(Model model, @PathVariable int userId) {
        model.addAttribute(new UpdatePasswordDTO());
        Optional<User> optUser = userRepository.findById(userId);
        if (optUser.isPresent()) {
            User user = optUser.get();
            model.addAttribute("user", user);
            return "admin/user_management/update_password";
        } else {
            return "redirect:admin/user_management";
        }
    }

    @PostMapping("/user_management/update_password/{userId}")
    public String processUpdatePasswordForm(@PathVariable int userId,
                                            @ModelAttribute @Valid UpdatePasswordDTO updatePasswordDTO,
                                            Errors errors) {
        if (errors.hasErrors()) {
            return ("redirect:/admin/user_management/update_password/" + userId);
        }

        String password = updatePasswordDTO.getPassword();
        String confirmPassword = updatePasswordDTO.getConfirmPassword();
        if (!password.equals(confirmPassword)) {
            errors.rejectValue("password", "password.mismatch", "Passwords do not match.");
            return ("redirect:/admin/user_management/update_password/" + userId);
        }

        Optional<User> optUser = userRepository.findById(userId);
        if (optUser.isPresent()) {
            User user = optUser.get();
            user.setPwhash(encoder.encode(password));
            userRepository.save(user);
        }
        return "redirect:/admin/user_management";
    }

    @GetMapping("/user_management/delete/{userId}")
    public String deleteUser(@PathVariable int userId) {
        Optional<User> optUser = userRepository.findById(userId);
        if (optUser.isPresent()) {
            userRepository.deleteById(userId);
        }
        return "redirect:/admin/user_management";
    }

    @GetMapping("/role_management")
    public String renderRoleManagementPortal() {
        return "/admin/role_management/index";
    }

    @GetMapping("/workflow_management")
    public String renderWorkflowManagementPortal(Model model) {

        Iterable<Job> jobRepositoryAll = jobRepository.findAll();
        List<Job> inProgressJobs = new ArrayList<>();

        if (jobRepositoryAll != null) {
            for (Job j : jobRepositoryAll) {
                if (Boolean.FALSE.equals(j.getIsCompleted())) {
                    inProgressJobs.add(j);
                }
            }
        }
        model.addAttribute("jobs", inProgressJobs);

        return "/admin/workflow_management/index";
    }

    @GetMapping("/view-workforce")
    public String renderWorkforceTable (Model model) {
        model.addAttribute("users", userRepository.findAll());
        return "/admin/workflow_management/view-workforce";
    }
}
