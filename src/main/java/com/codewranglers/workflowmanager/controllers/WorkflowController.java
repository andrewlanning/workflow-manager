package com.codewranglers.workflowmanager.controllers;

import com.codewranglers.workflowmanager.models.data.LotRepository;
import com.codewranglers.workflowmanager.models.data.PartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WorkflowController {

    @Autowired
    private PartRepository partRepository;
    @Autowired
    private LotRepository lotRepository;

    @GetMapping("/parts")
    private String getAllParts(Model model){
        model.addAttribute("parts", partRepository.findAll());
        return "parts/index";
    }

    @GetMapping("/lots")
    private String getAlllots(Model model){
        model.addAttribute("lots", lotRepository.findAll());
        return "lots/index";
    }
}
