package com.codewranglers.workflowmanager.controllers;

import com.codewranglers.workflowmanager.models.Job;
import com.codewranglers.workflowmanager.models.Operation;
import com.codewranglers.workflowmanager.models.data.JobRepository;
import com.codewranglers.workflowmanager.models.data.OperationRepository;
import com.codewranglers.workflowmanager.models.data.PartRepository;
import com.codewranglers.workflowmanager.models.data.ProductRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/member")
public class MemberController {

    @Autowired
    private JobRepository jobRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private OperationRepository operationRepository;
    @Autowired
    private PartRepository partRepository;


    // Looks through the job repository to find all jobs that have a field of is_complete set to False.
    // The function findByIsCompleteFalse is automatically generated by Spring Data JPA based on the method name.
    // It's hanging out over in JobRepository
    @GetMapping("")
    public String renderMemberPortal(Model model){
        List<Job> activeJobs = jobRepository.findByIsCompleteFalse();
        model.addAttribute("activeJobs", activeJobs);
        return "member/index";
    }

    // Mapping to get to the specific job id listed in the table using the operation_details template.
    @GetMapping("/job/{jobId}")
    public String jobSignOn(@PathVariable Integer jobId, Model model) {
        Job job = jobRepository.findById(jobId).orElse(null);

        Operation currentOperation = getCurrentOperation(job);

        model.addAttribute("job", job);
        model.addAttribute("currentOperation", currentOperation);

        return "member/job/operation_details";
    }

    // Completes the operation when there are still steps to move on to
    // Rest of the logic is handled by Thymeleaf in the template
    @GetMapping("/completeOperation/{jobId}/{opNumber}")
    public String completeOperation(@PathVariable Integer jobId, @PathVariable Integer opNumber) {
        Job job = jobRepository.findById(jobId).orElse(null);

        Operation currentOperation = getCurrentOperation(job);

        // Check if there are more operations
        if (currentOperation.getOpNumber() < job.getProduct().getOperationList().size()) {
            job.setCurrentStep(job.getCurrentStep() + 1);
            jobRepository.save(job);
        }
        return "redirect:/member/job/{jobId}";
    }

    // Completes the job when all out of steps on the previous mapping
    // Rest of the logic is handled by Thymeleaf in the template
    @GetMapping("/completeJob/{jobId}")
    public String completeJob(@PathVariable Integer jobId) {
        Job job = jobRepository.findById(jobId).orElse(null);
        job.setCurrentStep(0);
        job.setComplete(true);
        jobRepository.save(job);
        return "redirect:/member";
    }

    // Gets the current operation provided a given job.
    private Operation getCurrentOperation(Job job) {
        List<Operation> operations = job.getProduct().getOperationList();
        int currentStep = job.getCurrentStep();

        // Check if there are operations and if the currentStep is within bounds
        if (!operations.isEmpty() && currentStep >= 1 && currentStep <= operations.size()) {
            return operations.get(currentStep - 1);
        } else {
            return null;
        }
    }

    @GetMapping("/ncr/ncr_form")
    public String renderNCRForm(@RequestParam Integer jobId, Model model) {
        Job job = jobRepository.findById(jobId).orElse(null);

        model.addAttribute("job", job);

        return "member/ncr/ncr_form";
    }

    @PostMapping("/ncr/submit_ncr")
    public String submitNCR(@RequestParam Integer jobId, @RequestParam String nonConformanceName,
                            @RequestParam String nonConformanceDescription, @RequestParam String partSerialNumber) {

        return "redirect:/member/job/{jobId}";
    }

    @GetMapping("/ncr/cancel_ncr")
    public String cancelNCR(@RequestParam Integer jobId) {
        return "redirect:/member/job/{jobId}";
    }
}
