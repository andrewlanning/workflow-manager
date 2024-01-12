package com.codewranglers.workflowmanager.controllers;


import com.codewranglers.workflowmanager.models.Image;
import com.codewranglers.workflowmanager.models.Job;
import com.codewranglers.workflowmanager.models.Lot;
import com.codewranglers.workflowmanager.models.Product;
import com.codewranglers.workflowmanager.models.data.JobRepository;
import com.codewranglers.workflowmanager.models.data.LotRepository;
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

    @GetMapping("/jobs/edit_step/job_id/{jobId}")
    public String displayEditProductForm(Model model, @PathVariable int jobId) {
        Optional<Job> jobById = jobRepository.findById(jobId);
        if (jobById.isPresent()) {
            Job job = jobById.get();
            model.addAttribute("title", "Edit Product");
            model.addAttribute("job", job);
            return "/product/edit";
        } else {
            return "/jobs/edit_step/job_id/{jobId}";
        }
    }

    @PostMapping("/jobs/edit_step/job_id/{jobId}")
    public String processEditProductForm(@PathVariable int jobId,
                                         @ModelAttribute @Valid Product editedProduct,
                                         Model model,
                                         @RequestParam(value = "productImage", required = false) MultipartFile productImage) throws IOException {

        Optional<Product> productById = productRepository.findById(productId);
        if (productById.isPresent()) {
            Product product = productById.get();
            product.setProductName(editedProduct.getProductName());
            product.setProductDescription(editedProduct.getProductDescription());
            if (productImage != null && !productImage.isEmpty()) {
                if (product.getImage() != null) {
                    imageRepository.delete(product.getImage());
                }
                String imageUrl = uploadImageAndGetUrl(productImage);
                Image newImage = new Image(imageUrl);
                product.setImage(newImage);
                newImage.setProduct(product);
            }
            productRepository.save(product);
        }
        return "redirect:/product";
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


