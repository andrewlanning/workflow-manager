package com.codewranglers.workflowmanager.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HomeController {

    @GetMapping("admin")
    @ResponseBody
    public String admin(){
        return "This is the Admin Portal";
    }

    @GetMapping("manager")
    @ResponseBody
    public String manager(){
        return "This is the manager Portal";
    }

    @GetMapping("member")
    @ResponseBody
    public String member(){
        return "This is the Quality/Assembly Portal";
    }
}

