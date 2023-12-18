package com.codewranglers.workflowmanager.controllers;

import com.codewranglers.workflowmanager.models.User;
import com.codewranglers.workflowmanager.models.data.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("")
    public String index(Model model) {
        model.addAttribute("title", "All Users");
        model.addAttribute("users", userRepository.findAll());
        return "/admin/user_management/index";
    }

    @GetMapping("add")
    public String displayAddUserForm(Model model) {
        model.addAttribute(new User());
        return "users/add";
    }

    @PostMapping("add")
    public String processAddUsersForm(@ModelAttribute @Valid User newUser, Errors errors, Model model) {
        if (errors.hasErrors()) {
            model.addAttribute("title", "Add User");
            return "users/add";
        }
        userRepository.save(newUser);
        return "redirect:/users";
    }

    @GetMapping("view/{userId}")
    public String displayViewUser(Model model, @PathVariable int userId) {
        Optional<User> optUser = userRepository.findById(userId);
        if (optUser.isPresent()) {
            User user = optUser.get();
            model.addAttribute("user", user);
            return "users/view";
        } else {
            return "redirect:/users";
        }
    }
    @GetMapping("edit/{userId}")
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
    @PostMapping("edit/{userId}")
    public String processEditUserForm(@PathVariable int userId,
                                     @ModelAttribute @Valid User editedUser,
                                     Errors errors, Model model) {

        if (errors.hasErrors()) {
            model.addAttribute("title", "Edit user");
            return "users/edit";
        }

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
        return "redirect:/users";
    }
    @GetMapping("delete/{userId}")
    public String deleteUser(@PathVariable int userId) {
        Optional<User> optUser = userRepository.findById(userId);
        if (optUser.isPresent()) {
            userRepository.deleteById(userId);
        }
        return "redirect:/users";
    }
}
