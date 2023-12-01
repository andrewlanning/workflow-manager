package com.codewranglers.workflowmanager.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HomeController {

    @GetMapping("admin")
    @ResponseBody
    public String admin(){
        return "admin/index";
    }

    @GetMapping("manager")
    @ResponseBody
    public String manager(){
        return "manager/index";
    }

    @GetMapping("member")
    @ResponseBody
    public String member(){
        return "member/index";
    }
}

