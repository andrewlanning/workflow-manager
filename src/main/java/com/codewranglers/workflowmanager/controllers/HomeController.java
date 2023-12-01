package com.codewranglers.workflowmanager.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.List;

@Controller
public class HomeController {

//    @GetMapping("admin")
//    @ResponseBody
//    public String admin(){
//        return "admin/index";
//    }
//
//    @GetMapping("manager")
//    @ResponseBody
//    public String manager(){
//        return "manager/index";
//    }
//
//    @GetMapping("member")
//    @ResponseBody
//    public String member(){
//        return "member/index";
//    }


    @GetMapping("dashboard")
    public String displayDashboard(Model model) {
        List<String> pages = new ArrayList<>();
        pages.add("User Creation");
        pages.add("Adjust Role Permissions");
        pages.add("Workflow Management");
        List<String> user = new ArrayList<>();
        user.add("Manufacturing Operator");
        user.add("Product Manager");
        user.add("Administrator");
        model.addAttribute("pages",pages);
        model.addAttribute("user", user);
        return "dashboard";
    }

}

