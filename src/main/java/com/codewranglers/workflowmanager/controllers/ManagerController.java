package com.codewranglers.workflowmanager.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/manager")
public class ManagerController {

    @GetMapping("")
    public String renderManagerPortal(){
        return "/manager/index";
    }

    @GetMapping("/create_product")
    public String renderProductCreationPortal(){


        return "/manager/create_product";
    }

    @GetMapping("/add_operation")
    public String renderOperationCreationPortal(){
        return "/manager/add_operation";
    }
}
