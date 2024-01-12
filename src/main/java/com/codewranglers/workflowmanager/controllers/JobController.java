package com.codewranglers.workflowmanager.controllers;


import com.codewranglers.workflowmanager.models.*;
import com.codewranglers.workflowmanager.models.data.*;
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
import java.util.*;


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
    @Autowired
    private PartRepository partRepository;

    @GetMapping("")
    public String index(Model model) {
        Iterable<Job> allJobs = jobRepository.findAll();
        List<Job> sortedJobs = new ArrayList<>();

        for (Job j : allJobs){
            sortedJobs.add(j);
        }
        Collections.reverse(sortedJobs);

        model.addAttribute("jobs", sortedJobs);
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

        Job job = jobRepository.save(newJob);

        createParts(newJob.getProduct().getProductId(), newJob.getQuantity(), lot, job);
        return "redirect:/jobs";
    }

    @GetMapping("/edit_step/job_id/{jobId}")
    public String displayEditJobForm(Model model, @PathVariable int jobId) {
        Optional<Job> jobById = jobRepository.findById(jobId);
        if (jobById.isPresent()) {
            Job job = jobById.get();
            List<Operation> byproductProductId = operationRepository.findByproductProductId(job.getProduct().getProductId());
            model.addAttribute("steps", byproductProductId);
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
        if (jobById.isPresent()) {
            Job job = jobById.get();
            job.setCurrentStep(editedJob.getCurrentStep());
            jobRepository.save(job);
        }

        return "redirect:/jobs/edit/{jobId}";
    }

    @GetMapping("/edit/{jobId}")
    public String displayEditProductForm(Model model, @PathVariable int jobId) {
        Optional<Job> jobById = jobRepository.findById(jobId);
        if (jobById.isPresent()) {
            Job job = jobById.get();
            if (Boolean.TRUE.equals(job.getIsCompleted())) {
                return "redirect:/jobs";
            } else {
                model.addAttribute("job", job);
                model.addAttribute("dueDate", job.getDueDate());
                model.addAttribute("isCompleted", job.getIsCompleted());
                return "/jobs/job_edit";
            }
        } else {
            return "redirect:/jobs/edit";
        }
    }

    @PostMapping("/edit/{jobId}")
    public String processEditProductForm(@PathVariable int jobId,
                                         @ModelAttribute @Valid Job editedJob) {

        Optional<Job> jobById = jobRepository.findById(jobId);

        if (jobById.isPresent()) {
            Job job = jobById.get();
            job.setDueDate(editedJob.getDueDate());

            if (Boolean.FALSE.equals(job.getIsCompleted())) {
                if (Boolean.TRUE.equals(editedJob.getIsCompleted())) {
                    job.setIsCompleted(Boolean.TRUE);
                    job.setCompletionDate(LocalDate.now());
                }
            }
            jobRepository.save(job);
        }

        return "redirect:/jobs";
    }

    private String createWONumber() {
        Iterable<Job> jobs = jobRepository.findAll();
        int woNumber = 0;

        for (Job j : jobs) {
            woNumber = Integer.parseInt(j.getWorkOrderNumber().substring(2));
        }
        woNumber++;

        return "WO" + String.format(String.format("%04d", woNumber));
    }

    private Lot createLotNumber(int productId) {
        Optional<Product> byId = productRepository.findById(productId);
        Iterable<Lot> lots = lotRepository.findAll();
        Lot lot = new Lot();
        int lotNumber = 0;

        for (Lot l : lots) {
            lotNumber = Integer.parseInt(l.getLotNumber());
        }

        lotNumber++;

        lot.setLotNumber(String.format("%04d", lotNumber));
        lot.setProduct(byId.orElse(null));

        lotRepository.save(lot);

        return lot;
    }

    private void createParts(int productId, int quantity, Lot lot, Job job) {
        List<Part> byproductProductId = partRepository.findByproductProductId(productId);
        String productName = productRepository.findById(productId).orElse(null).getProductName();
        Part part = new Part();
        List<Part> totalParts = new ArrayList<>();
        part.setProduct(new Product(productId));
        if (byproductProductId.isEmpty()) {
            for (int i = 1; i < quantity + 1; i++) {
                part.setLot(lot);
                part.setSerNum("SN" + productId + "-" + productName.substring(0, 3).toUpperCase() + String.format("%03d", i));
                totalParts.add(new Part(part.getSerNum(), lot, part.getProduct(), job));
            }
        } else {
            int serNum = 0;
            for (Part p : byproductProductId) {
                serNum = Integer.parseInt(p.getSerNum().substring(8));
                p.setLot(lot);
            }
            for (int i = 1; i < quantity + 1; i++) {
                serNum++;
                part.setSerNum("SN" + part.getProduct().getProductId() + "-" + productName.substring(0, 3).toUpperCase() + String.format("%03d", serNum));
                totalParts.add(new Part(part.getSerNum(), lot, part.getProduct(), job));
            }
        }
        partRepository.saveAll(totalParts);
    }
}

