//package com.codewranglers.workflowmanager.controllers;
//
//import com.codewranglers.workflowmanager.models.Operation;
//import com.codewranglers.workflowmanager.models.Product;
//import com.codewranglers.workflowmanager.models.data.OperationRepository;
//import com.codewranglers.workflowmanager.models.data.ProductRepository;
//import jakarta.validation.Valid;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.validation.Errors;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//import java.util.Optional;
//
//
//@Controller
//@RequestMapping("/manager/product/operation")
//public class OperationController {
//
//    @Autowired
//    private OperationRepository operationRepository;
//    @Autowired
//    private ProductRepository productRepository;
//
//    private int productId;
//
//    @GetMapping("/product_id/{productId}")
//    public String renderOperationPortal(Model model, @PathVariable int productId) {
//        String productName = productRepository.findById(productId).get().getProductName();
//        model.addAttribute("productName", productName);
//        model.addAttribute("operations", operationRepository.findByproductProductId(productId));
//        this.productId=productId;
//        return "/operation/index";
//    }
//
//    @GetMapping("/product_id/{productId}/add")
//    public String renderOperationCreationPortal(Model model,  @PathVariable int productId) {
//        String productName = productRepository.findById(productId).get().getProductName();
//        model.addAttribute("productName", productName);
//        model.addAttribute("productId", this.productId);
//        model.addAttribute("operations", new Operation());
//        return "/operation/create_operation";
//    }
//
//    @PostMapping("/product_id/{productId}/add")
//    public String processOperationCreation(@ModelAttribute("operations") Operation operation) {
//        operation.setOpNumber(createOPNumber(productId));
//        operation.setProduct(new Product(productId));
//        operationRepository.save(operation);
//        return "redirect:/manager/product/operation/product_id/{productId}";
//    }
//
//
//    @GetMapping("product_id/{productId}/edit/operation_id/{operationId}")
//    public String displayEditOperationForm(Model model, @PathVariable int operationId, @PathVariable int productId) {
//        Optional<Operation> operationById = operationRepository.findById(operationId);
//        String productName = productRepository.findById(productId).get().getProductName();
//        model.addAttribute("productName", productName);
//        model.addAttribute("productId", productId);
//
//        if (operationById.isPresent()) {
//            Operation operation = operationById.get();
//            model.addAttribute("operation", operation);
//            model.addAttribute("products", productRepository.findAll());
//            return "/operation/edit";
//        } else {
//            return "redirect:/manager/product/operation/product_id/{productId}";
//        }
//    }
//
//    @PostMapping("product_id/{productId}/edit/operation_id/{operationId}")
//    public String processEditOperationForm(@PathVariable int operationId,
//                                           @PathVariable int productId,
//                                           @ModelAttribute @Valid Operation editedOperation,
//                                           Errors errors, Model model) {
//
//        if (errors.hasErrors()) {
//            model.addAttribute("title", "Edit Operation");
//            return "/operation/edit";
//        }
//
//        Optional<Operation> operationById = operationRepository.findById(operationId);
//        model.addAttribute("productId", productId);
//        if (operationById.isPresent()) {
//            Operation operation = operationById.get();
//            operation.setOpName(editedOperation.getOpName());
//            operation.setOpText(editedOperation.getOpText());
//            operationRepository.save(operation);
//        }
//        return "redirect:/manager/product/operation/product_id/{productId}";
//    }
//
//    @GetMapping("product_id/{productId}/delete/operation_id/{operationId}")
//    public String deleteOperation(Model model, @PathVariable int operationId, @PathVariable int productId) {
//        Optional<Operation> optOperation = operationRepository.findById(operationId);
//        model.addAttribute("productId", productId);
//        if (optOperation.isPresent()) {
//            operationRepository.deleteById(operationId);
//        }
//        return "redirect:/manager/product/operation/product_id/{productId}";
//    }
//
//    private int createOPNumber(int productId){
//        List<Operation> byproductProductId = operationRepository.findByproductProductId(productId);
//         int opNumber = 0;
//
//         if (byproductProductId != null) {
//             for (Operation o : byproductProductId) {
//                 opNumber = o.getOpNumber();
//             }
//             opNumber++;
//         } else {
//             opNumber = 1;
//         }
//
//         return opNumber;
//
//    }
//}
