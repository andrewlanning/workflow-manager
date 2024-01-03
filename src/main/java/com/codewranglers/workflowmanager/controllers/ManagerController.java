package com.codewranglers.workflowmanager.controllers;

import com.codewranglers.workflowmanager.models.Operation;
import com.codewranglers.workflowmanager.models.Product;
import com.codewranglers.workflowmanager.models.User;
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

    @GetMapping("/product/edit/{productId}")
    public String displayEditProductForm(Model model, @PathVariable int productId) {
        Optional<Product> productById = productRepository.findById(productId);
        if (productById.isPresent()) {
            Product product = productById.get();
            model.addAttribute("title", "Edit Product");
            model.addAttribute("product", product);
            return "manager/product/edit";
        } else {
            return "redirect:manager/product/edit";
        }
    }

    @PostMapping("/product/edit/{productId}")
    public String processEditProductForm(@PathVariable int productId,
                                      @ModelAttribute @Valid Product editedProduct,
                                      Errors errors, Model model) {

        if (errors.hasErrors()) {
            model.addAttribute("title", "Edit Product");
            return "manager/product/edit";
        }

        Optional<Product> productById = productRepository.findById(productId);
        if (productById.isPresent()) {
            Product product = productById.get();
            product.setProductName(editedProduct.getProductName());
            product.setProductDescription(editedProduct.getProductDescription());
            productRepository.save(product);
        }
        return "redirect:/manager/product";
    }

    @GetMapping("/product/delete/{productId}")
    public String deleteProduct(@PathVariable int productId) {
        Optional<Product> optProduct = productRepository.findById(productId);
        if (optProduct.isPresent()) {
            productRepository.deleteById(productId);
        }
        return "redirect:/manager/product";
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
}
