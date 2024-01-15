package com.codewranglers.workflowmanager.controllers;

import com.codewranglers.workflowmanager.models.Image;
import com.codewranglers.workflowmanager.models.Operation;
import com.codewranglers.workflowmanager.models.Product;
import com.codewranglers.workflowmanager.models.data.ImageRepository;
import com.codewranglers.workflowmanager.models.data.OperationRepository;
import com.codewranglers.workflowmanager.models.data.ProductRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.hibernate.proxy.HibernateProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.*;

@Controller
@RequestMapping("/manager/product")
public class ProductController {

    @Autowired
    ProductRepository productRepository;
    @Autowired
    OperationRepository operationRepository;

    @Autowired
    ImageRepository imageRepository;

    @GetMapping("")
    public String renderProductPortal(Model model) {
        // Using Map with key Product and Value total steps to show total steps on index page
        Map<Product, Integer> finalMap = new LinkedHashMap<>();

        int counter;
        Iterable<Product> products = productRepository.findAll();

        for (Product p : products) {
            counter = 0;

            List<Operation> operations = operationRepository.findByproductProductId(p.getProductId());

            if (!operations.isEmpty()) {

                for (Operation o : operations) {

                    if (o != null) {
                        counter++;
                    } else {
                        counter = 0;
                    }
                }
            }

            finalMap.put(p, counter);
        }

        model.addAttribute("products", finalMap);
        return "/product/index";
    }

    @GetMapping("/add")
    public String renderProductCreationPortal(Model model) {
        model.addAttribute("product", new Product());
        return "/product/create_product";
    }

    @PostMapping("/add")
    public String processProductCreation(@ModelAttribute("product") Product product,
                                         @RequestParam(value = "productImage", required = false) MultipartFile productImage) throws IOException {
        //handler method return image URL here
        //logic to add image attribute to product and product attribute to image
        if (productImage != null && !productImage.isEmpty()) {
            String imageUrl = uploadImageAndGetUrl(productImage);
            if (imageUrl != null) {
                Image image = new Image(imageUrl);
                image.setProduct(product);
                product.setImage(image);
                //Update image and product tables
                productRepository.save(product);
                imageRepository.save(image);
            }
        }
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

    @Transactional
    @PostMapping("/edit/{productId}")
    public String processEditProductForm(@PathVariable int productId,
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

    @GetMapping("/delete/{productId}")
    public String deleteProduct(@PathVariable int productId) {
        Optional<Product> optProduct = productRepository.findById(productId);
        if (optProduct.isPresent()) {
            List<Operation> byproductProductId = operationRepository.findByproductProductId(productId);

            if (!byproductProductId.isEmpty()) {
                operationRepository.deleteAll(byproductProductId);
            }

            productRepository.deleteById(productId);
        }
        return "redirect:/product";
    }

    private String uploadImageAndGetUrl(MultipartFile imageFile) throws IOException {
        // API URL
        String apiUrl = "https://freeimage.host/api/1/upload";

        // Convert MultipartFile to Base64
        String base64Image = Base64.getEncoder().encodeToString(imageFile.getBytes());

        // Prepare request body
        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("key", "6d207e02198a847aa98d0a2a901485a5");
        requestBody.add("source", base64Image);
        requestBody.add("format", "json");

        // Set headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // Create request entity
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);

        // Create a RestTemplate instance
        RestTemplate restTemplate = new RestTemplate();

        // Send POST request
        String response = restTemplate.postForObject(apiUrl, requestEntity, String.class);

        // Parse the JSON response
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(response);
        // Retrieve the image URL
        String imageUrl = rootNode.path("image").path("url").asText();
        return imageUrl;
    }
}
