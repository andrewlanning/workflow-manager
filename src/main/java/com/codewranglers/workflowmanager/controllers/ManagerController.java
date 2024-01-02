package com.codewranglers.workflowmanager.controllers;

import com.codewranglers.workflowmanager.models.Product;
import com.codewranglers.workflowmanager.models.data.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/manager")
public class ManagerController {

    @Autowired
    private ProductRepository productRepository;

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

    @GetMapping("/product")
    public String renderProductPortal(Model model) {
        model.addAttribute("products", productRepository.findAll());
        return "/manager/product/index";
    }

    @GetMapping("/product/add")
    public String renderProductCreationPortal(Model model) {
        model.addAttribute("product", new Product());
        return "/manager/product/create_product";
    }

    @PostMapping("/product/add")
    public String processProductCreation(@ModelAttribute("product") Product product) {
        productRepository.save(product);
        return "redirect:/manager/product";
    }

    @GetMapping("/operation")
    public String renderOperationPortal(Model model) {
        // Logic for displaying Operations page
        return "/manager/operation/index";
    }
}
