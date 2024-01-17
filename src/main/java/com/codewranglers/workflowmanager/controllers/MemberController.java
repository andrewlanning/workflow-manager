package com.codewranglers.workflowmanager.controllers;

import com.codewranglers.workflowmanager.models.Job;
import com.codewranglers.workflowmanager.models.Operation;
import com.codewranglers.workflowmanager.models.data.JobRepository;
import com.codewranglers.workflowmanager.models.data.OperationRepository;
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
    private OperationRepository operationRepository;

    @GetMapping("")
    public String renderMemberPortal(Model model){
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
        return "member/index";
    }

    @GetMapping("/job/search")
    public String searchInProcessJobs (@RequestParam(defaultValue = "") String pName, Model model ) {
        List<Job> jobRepositoryAll = jobRepository.findByProductProductNameStartingWithIgnoreCase(pName);
        List<Job> inProgressJobs = new ArrayList<>();

        if (jobRepositoryAll != null) {
            for (Job j : jobRepositoryAll) {
                if (Boolean.FALSE.equals(j.getIsCompleted())) {
                    inProgressJobs.add(j);
                }
            }
        }
        model.addAttribute("jobs", inProgressJobs);
        model.addAttribute("productName", pName);

        return "/member/search";
    }

    @GetMapping("/job/edit/{jobId}")
    public String displayEditProductForm(Model model, @PathVariable int jobId) {
        Optional<Job> jobById = jobRepository.findById(jobId);
        if (jobById.isPresent()) {
            Job job = jobById.get();
            if (Boolean.TRUE.equals(job.getIsCompleted())) {
                return "redirect:/manager/jobs";
            } else {
                model.addAttribute("job", job);
                model.addAttribute("dueDate", job.getDueDate());
                model.addAttribute("isCompleted", job.getIsCompleted());
                return "/jobs/member/job_edit_member";
            }
        } else {
            return "/redirect:/member/job/edit";
        }
    }

    @PostMapping("/job/edit/{jobId}")
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
            return "redirect:/member";
    }

    @GetMapping("/job/edit_step/job_id/{jobId}")
    public String displayEditJobForm(Model model, @PathVariable int jobId) {
        Optional<Job> jobById = jobRepository.findById(jobId);
        if (jobById.isPresent()) {
            Job job = jobById.get();
            List<Operation> byproductProductId = operationRepository.findByproductProductId(job.getProduct().getProductId());
            model.addAttribute("steps", byproductProductId);
            model.addAttribute("job", job);
            return "/jobs/member/job_edit_step_member";
        } else {
            return "/job/edit_step/job_id/{jobId}";
        }
    }

    @PostMapping("/job/edit_step/job_id/{jobId}")
    public String processEditJobForm(@PathVariable int jobId,
                                     @ModelAttribute Job editedJob,
                                     Model model) {
        Optional<Job> jobById = jobRepository.findById(jobId);
        if (jobById.isPresent()) {
            Job job = jobById.get();
            job.setCurrentStep(editedJob.getCurrentStep());
            jobRepository.save(job);
        }

        return "redirect:/member/job/edit/{jobId}";
    }
}
