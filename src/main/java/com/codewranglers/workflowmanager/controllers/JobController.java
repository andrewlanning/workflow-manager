package com.codewranglers.workflowmanager.controllers;


import com.codewranglers.workflowmanager.models.*;
import com.codewranglers.workflowmanager.models.data.JobRepository;
import com.codewranglers.workflowmanager.models.data.LotRepository;
import com.codewranglers.workflowmanager.models.data.OperationRepository;
import com.codewranglers.workflowmanager.models.data.ProductRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


@Controller
@RequestMapping("/jobs")
public class JobController {

    @Autowired
    private JobRepository jobRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private LotRepository lotRepository;
    @Autowired
    private OperationRepository operationRepository;

    @GetMapping("")
    public String index(Model model) {
        Iterable<Job> allJobs = jobRepository.findAll();

        for (Job j : allJobs){
                j.setCurrentStep(Integer.parseInt(String.format("%03d", j.getCurrentStep())));
        }
        model.addAttribute("title", "All Jobs");
        model.addAttribute("jobs", allJobs);
        return "jobs/index";
    }

    @GetMapping("/add")
    public String displayAddJobForm(Model model) {
        model.addAttribute("products", productRepository.findAll());
        model.addAttribute(new Job());
        return "jobs/job_add";
    }

    @PostMapping("/add")
    public String processAddJobForm(@ModelAttribute("job") Job newJob, Errors errors, Model model) {
        if (errors.hasErrors()) {
            model.addAttribute("title", "Add Job");
            return "jobs/job_add";
        }
        newJob.setWorkOrderNumber(createWONumber());
        Lot lot = createLotNumber(newJob.getProduct().getProductId());
        newJob.setLot(lot);
        newJob.setCurrentStep(Integer.parseInt(String.format("%03d", 0)));
        newJob.setIsCompleted(Boolean.FALSE);
        newJob.setStartDate(LocalDate.now());
        jobRepository.save(newJob);
        return "redirect:/jobs";
    }

    @GetMapping("/edit_step/job_id/{jobId}")
    public String displayEditJobForm(Model model, @PathVariable int jobId) {
        Optional<Job> jobById = jobRepository.findById(jobId);
        if (jobById.isPresent()) {
            Job job = jobById.get();
            List<Operation> byproductProductId = operationRepository.findByproductProductId(job.getProduct().getProductId());
            model.addAttribute("steps", byproductProductId);
            model.addAttribute("title", "Edit Product");
            model.addAttribute("job", job);
            return "/jobs/job_edit_step";
        } else {
            return "/jobs/edit_step/job_id/{jobId}";
        }
    }

    @PostMapping("/edit_step/job_id/{jobId}")
    public String processEditJobForm(@PathVariable int jobId,
                                         @ModelAttribute Job editedJob,
                                         Model model) {
        Optional<Job> jobById = jobRepository.findById(jobId);
        if (jobById.isPresent()){
            Job job = jobById.get();
            job.setCurrentStep(editedJob.getCurrentStep());
            jobRepository.save(job);
        }

        return "redirect:/jobs";
    }

    private String createWONumber(){
        Iterable<Job> jobs = jobRepository.findAll();
        int woNumber = 0;

        for (Job j : jobs){
            woNumber = Integer.parseInt(j.getWorkOrderNumber().substring(2));
        }
        woNumber++;

        return "WO" + String.format(String.format("%04d", woNumber));
    }

    private Lot createLotNumber(int productId){
        Optional<Product> byId = productRepository.findById(productId);
        Iterable<Lot> lots = lotRepository.findAll();
        Lot lot = new Lot();
        int lotNumber = 0;

        for (Lot l : lots){
            lotNumber = Integer.parseInt(l.getLotNumber());
        }

        lotNumber++;

        lot.setLotNumber(String.format("%04d", lotNumber));
        lot.setProduct(byId.orElse(null));

        lotRepository.save(lot);

        return lot;
    }
}


