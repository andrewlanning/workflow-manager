package com.codewranglers.workflowmanager.controllers;

import com.codewranglers.workflowmanager.models.*;
import com.codewranglers.workflowmanager.models.data.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/manager")
public class ManagerController {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private OperationRepository operationRepository;
    @Autowired
    private LotRepository lotRepository;
    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("")
    public String renderManagerPortal(Model model) {
        List<String> pages = new ArrayList<>();
        pages.add("Jobs");
        pages.add("Products");
        pages.add("Operations");

        List<String> urlStrings = new ArrayList<>();
        urlStrings.add("jobs");
        urlStrings.add("product");
        urlStrings.add("operation");

        model.addAttribute("pages", pages);
        model.addAttribute("url", urlStrings);

        Iterable<Job> jobRepositoryAll = jobRepository.findAll();
        List<Job> inProgressJobs = new ArrayList<>();

        if (jobRepositoryAll != null){
            for (Job j : jobRepositoryAll){
                if (Boolean.FALSE.equals(j.getIsCompleted())){
                    inProgressJobs.add(j);
                }
            }
        }
        model.addAttribute("jobs", inProgressJobs);
        return "/manager/index";
    }

    @GetMapping("/view-workforce")
    public String renderWorkforceTable (Model model) {
        model.addAttribute("users", userRepository.findAll());
        return "/manager/view-workforce";
    }

    @GetMapping("/job/edit/{jobId}")
    public String jobEdit(@PathVariable int jobId){

        return "/manager/job_edit";

    }

}
