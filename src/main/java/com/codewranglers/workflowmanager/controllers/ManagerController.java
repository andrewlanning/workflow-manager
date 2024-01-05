package com.codewranglers.workflowmanager.controllers;

import com.codewranglers.workflowmanager.models.Lot;
import com.codewranglers.workflowmanager.models.Operation;
import com.codewranglers.workflowmanager.models.Product;
import com.codewranglers.workflowmanager.models.User;
import com.codewranglers.workflowmanager.models.data.LotRepository;
import com.codewranglers.workflowmanager.models.data.OperationRepository;
import com.codewranglers.workflowmanager.models.data.ProductRepository;
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
        return "/manager/index";
    }

    @GetMapping("/job")
    public String renderJobsPortal(Model model) {
        // Logic for displaying Jobs page
        return "/manager/job/index";
    }



    @GetMapping("/operation")
    public String renderOperationPortal(Model model) {
        model.addAttribute("operations", operationRepository.findAll());
        return "/manager/operation/index";
    }

    @GetMapping("/operation/add")
    public String renderOperationCreationPortal(Model model) {
        model.addAttribute("products", productRepository.findAll());
        model.addAttribute("operations", new Operation());
        return "/manager/operation/create_operation";
    }

    @PostMapping("/operation/add")
    public String processOperationCreation(@ModelAttribute("operations") Operation operation) {
        operationRepository.save(operation);
        return "redirect:/manager/operation";
    }


    @GetMapping("/operation/edit/{operationId}")
    public String displayEditOperationForm(Model model, @PathVariable int operationId) {
        Optional<Operation> operationById = operationRepository.findById(operationId);
        if (operationById.isPresent()) {
            Operation operation = operationById.get();
            model.addAttribute("title", "Edit Operation");
            model.addAttribute("operation", operation);
            model.addAttribute("products", productRepository.findAll());
            return "manager/operation/edit";
        } else {
            return "redirect:manager/operation/edit";
        }
    }

    @PostMapping("/operation/edit/{operationId}")
    public String processEditOperationForm(@PathVariable int operationId,
                                           @ModelAttribute @Valid Operation editedOperation,
                                           Errors errors, Model model) {

        if (errors.hasErrors()) {
            model.addAttribute("title", "Edit Operation");
            return "manager/operation/edit";
        }

        Optional<Operation> operationById = operationRepository.findById(operationId);
        if (operationById.isPresent()) {
            Operation operation = operationById.get();
            operation.setOpName(editedOperation.getOpName());
            operation.setOpNumber(editedOperation.getOpNumber());
            operation.setOpText(editedOperation.getOpText());
            operation.setProduct(editedOperation.getProduct());
            operationRepository.save(operation);
        }
        return "redirect:/manager/operation";
    }

    @GetMapping("/operation/delete/{operationId}")
    public String deleteOperation(@PathVariable int operationId) {
        Optional<Operation> optOperation = operationRepository.findById(operationId);
        if (optOperation.isPresent()) {
            operationRepository.deleteById(operationId);
        }
        return "redirect:/manager/operation";
    }
}
