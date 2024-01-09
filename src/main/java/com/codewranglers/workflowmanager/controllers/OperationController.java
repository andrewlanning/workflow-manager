package com.codewranglers.workflowmanager.controllers;

import com.codewranglers.workflowmanager.models.Operation;
import com.codewranglers.workflowmanager.models.data.OperationRepository;
import com.codewranglers.workflowmanager.models.data.ProductRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@Controller
@RequestMapping("/operation")
public class OperationController {

    @Autowired
    private OperationRepository operationRepository;
    @Autowired
    private ProductRepository productRepository;

    @GetMapping("")
    public String renderOperationPortal(Model model) {
        model.addAttribute("operations", operationRepository.findAll());
        return "/operation/index";
    }

    @GetMapping("/add")
    public String renderOperationCreationPortal(Model model) {
        model.addAttribute("products", productRepository.findAll());
        model.addAttribute("operations", new Operation());
        return "/operation/create_operation";
    }

    @PostMapping("/add")
    public String processOperationCreation(@ModelAttribute("operations") Operation operation) {
        operationRepository.save(operation);
        return "redirect:/operation";
    }


    @GetMapping("/edit/{operationId}")
    public String displayEditOperationForm(Model model, @PathVariable int operationId) {
        Optional<Operation> operationById = operationRepository.findById(operationId);
        if (operationById.isPresent()) {
            Operation operation = operationById.get();
            model.addAttribute("title", "Edit Operation");
            model.addAttribute("operation", operation);
            model.addAttribute("products", productRepository.findAll());
            return "/operation/edit";
        } else {
            return "redirect:/operation/edit";
        }
    }

    @PostMapping("/edit/{operationId}")
    public String processEditOperationForm(@PathVariable int operationId,
                                           @ModelAttribute @Valid Operation editedOperation,
                                           Errors errors, Model model) {

        if (errors.hasErrors()) {
            model.addAttribute("title", "Edit Operation");
            return "/operation/edit";
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
        return "redirect:/operation";
    }

    @GetMapping("/delete/{operationId}")
    public String deleteOperation(@PathVariable int operationId) {
        Optional<Operation> optOperation = operationRepository.findById(operationId);
        if (optOperation.isPresent()) {
            operationRepository.deleteById(operationId);
        }
        return "redirect:/operation";
    }
}
