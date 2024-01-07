package com.codewranglers.workflowmanager.controllers;

import com.codewranglers.workflowmanager.models.Product;
import com.codewranglers.workflowmanager.models.data.ProductRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/product")
public class ProductController {

    @Autowired
    ProductRepository productRepository;

    @GetMapping("")
    public String renderProductPortal(Model model) {
        model.addAttribute("products", productRepository.findAll());
        return "/product/index";
    }

    @GetMapping("/add")
    public String renderProductCreationPortal(Model model) {
        model.addAttribute("product", new Product());
        return "/product/create_product";
    }

    @PostMapping("/add")
    public String processProductCreation(@ModelAttribute("product") Product product) {
        productRepository.save(product);
        return "redirect:/product";
    }

    @GetMapping("/edit/{productId}")
    public String displayEditProductForm(Model model, @PathVariable int productId) {
        Optional<Product> productById = productRepository.findById(productId);
        if (productById.isPresent()) {
            Product product = productById.get();
            model.addAttribute("title", "Edit Product");
            model.addAttribute("product", product);
            return "/product/edit";
        } else {
            return "redirect:/product/edit";
        }
    }

    @PostMapping("/edit/{productId}")
    public String processEditProductForm(@PathVariable int productId,
                                         @ModelAttribute @Valid Product editedProduct,
                                         Errors errors, Model model) {

        if (errors.hasErrors()) {
            model.addAttribute("title", "Edit Product");
            return "/product/edit";
        }

        Optional<Product> productById = productRepository.findById(productId);
        if (productById.isPresent()) {
            Product product = productById.get();
            product.setProductName(editedProduct.getProductName());
            product.setProductDescription(editedProduct.getProductDescription());
            productRepository.save(product);
        }
        return "redirect:/product";
    }

    @GetMapping("/delete/{productId}")
    public String deleteProduct(@PathVariable int productId) {
        Optional<Product> optProduct = productRepository.findById(productId);
        if (optProduct.isPresent()) {
            productRepository.deleteById(productId);
        }
        return "redirect:/product";
    }
}
