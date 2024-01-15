package com.codewranglers.workflowmanager.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HomeController {

    @GetMapping("/")
    public String renderTestPortal() {
        return "redirect:/login";
    }

    @GetMapping("/unauthorized")
    public String renderUnauthLanding() {
        return "unauth";
    }

    @GetMapping("/dashboard")
    public String renderDashboard() {
        return "dashboard";
    }
}