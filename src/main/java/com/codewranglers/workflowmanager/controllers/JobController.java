package com.codewranglers.workflowmanager.controllers;

import com.codewranglers.workflowmanager.models.Job;
import com.codewranglers.workflowmanager.models.data.JobRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("jobs")
public class JobController {

    @Autowired
    private JobRepository jobRepository;

    @GetMapping("")
    public String index(Model model) {
        model.addAttribute("title", "All Jobs");
        model.addAttribute("jobs", jobRepository.findAll());
        return "jobs/index";
    }

    @GetMapping("add")
    public String displayAddJobForm(Model model) {
        model.addAttribute(new Job());
        return "jobs/add";
    }

    @PostMapping("add")
    public String processAddJobsForm(@ModelAttribute @Valid Job newJob, Errors errors, Model model) {
        if (errors.hasErrors()) {
            model.addAttribute("title", "Add Job");
            return "jobs/add";
        }
        jobRepository.save(newJob);
        return "redirect:/jobs";
    }

    @GetMapping("view/{jobId}")
    public String displayViewJob(Model model, @PathVariable int jobId) {
        Optional<Job> optJob = jobRepository.findById(jobId);
        if (optJob.isPresent()) {
            Job job = optJob.get();
            model.addAttribute("job", job);
            return "jobs/view";
        } else {
            return "redirect:/jobs";
        }
    }
    @GetMapping("edit/{jobId}")
    public String displayEditJobForm(Model model, @PathVariable int jobId) {
        Optional<Job> optJob = jobRepository.findById(jobId);
        if (optJob.isPresent()) {
            Job job = optJob.get();
            model.addAttribute("title", "Edit Job");
            model.addAttribute("job", job);
            return "jobs/edit";
        } else {
            return "redirect:/jobs";
        }
    }

    @PostMapping("edit/{jobId}")
    public String processEditJobForm(@PathVariable int jobId,
                                       @ModelAttribute @Valid Job editedJob,
                                       Errors errors, Model model) {

        if (errors.hasErrors()) {
            model.addAttribute("title", "Edit Job");
            return "jobs/edit";
        }

        Optional<Job> optJob = jobRepository.findById(jobId);
        if (optJob.isPresent()) {
            Job job = optJob.get();
//            job.setName(editedJob.getName());
            jobRepository.save(job);
        }

        return "redirect:/jobs";
    }

    @GetMapping("delete/{jobId}")
    public String deleteJob(@PathVariable int jobId) {
        jobRepository.deleteById(jobId);
        return "redirect:/jobs";
    }
}