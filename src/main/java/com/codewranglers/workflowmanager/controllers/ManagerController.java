package com.codewranglers.workflowmanager.controllers;

import com.codewranglers.workflowmanager.models.*;
import com.codewranglers.workflowmanager.models.data.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
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
import java.time.LocalDate;
import java.util.*;

@Controller
@RequestMapping("/manager")
public class ManagerController {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private OperationRepository operationRepository;
    @Autowired
    private LotRepository lotRepository;
    @Autowired
    private JobRepository jobRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PartRepository partRepository;
    @Autowired
    private ImageRepository imageRepository;

    int productId;

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

        Iterable<Job> jobRepositoryAll = jobRepository.findAll();
        List<Job> inProgressJobs = new ArrayList<>();

        if (jobRepositoryAll != null) {
            for (Job j : jobRepositoryAll) {
                if (Boolean.FALSE.equals(j.getIsCompleted())) {
                    inProgressJobs.add(j);
                }
            }
        }
        model.addAttribute("jobs", inProgressJobs);
        return "/manager/index";
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

        return "/manager/search";
    }

    @GetMapping("/view-workforce")
    public String renderWorkforceTable(Model model) {
        model.addAttribute("users", userRepository.findAll());
        return "/manager/view-workforce";
    }

    @GetMapping("/product/operation/product_id/{productId}")
    public String renderOperationPortal(Model model, @PathVariable int productId) {
        String productName = productRepository.findById(productId).get().getProductName();
        model.addAttribute("productName", productName);
        model.addAttribute("operations", operationRepository.findByproductProductId(productId));
        this.productId=productId;
        return "/operation/manager/index";
    }

    @GetMapping("/product/operation/product_id/{productId}/add")
    public String renderOperationCreationPortal(Model model,  @PathVariable int productId) {
        String productName = productRepository.findById(productId).get().getProductName();
        model.addAttribute("productName", productName);
        model.addAttribute("productId", this.productId);
        model.addAttribute("operations", new Operation());
        return "/operation/manager/create_operation";
    }

    @PostMapping("/product/operation/product_id/{productId}/add")
    public String processOperationCreation(@ModelAttribute("operations") Operation operation) {
        operation.setOpNumber(createOPNumber(productId));
        operation.setProduct(new Product(productId));
        operationRepository.save(operation);
        return "redirect:/manager/product/operation/product_id/{productId}";
    }

    @GetMapping("/product/operation/product_id/{productId}/edit/operation_id/{operationId}")
    public String displayEditOperationForm(Model model, @PathVariable int operationId, @PathVariable int productId) {
        Optional<Operation> operationById = operationRepository.findById(operationId);
        String productName = productRepository.findById(productId).get().getProductName();
        model.addAttribute("productName", productName);
        model.addAttribute("productId", productId);

        if (operationById.isPresent()) {
            Operation operation = operationById.get();
            model.addAttribute("operation", operation);
            model.addAttribute("products", productRepository.findAll());
            return "/operation/manager/edit";
        } else {
            return "redirect:/manager/product/operation/product_id/{productId}";
        }
    }

    @PostMapping("/product/operation/product_id/{productId}/edit/operation_id/{operationId}")
    public String processEditOperationForm(@PathVariable int operationId,
                                           @PathVariable int productId,
                                           @ModelAttribute @Valid Operation editedOperation,
                                           Errors errors, Model model) {

        if (errors.hasErrors()) {
            model.addAttribute("title", "Edit Operation");
            return "/operation/manager/edit";
        }

        Optional<Operation> operationById = operationRepository.findById(operationId);
        model.addAttribute("productId", productId);
        if (operationById.isPresent()) {
            Operation operation = operationById.get();
            operation.setOpName(editedOperation.getOpName());
            operation.setOpText(editedOperation.getOpText());
            operationRepository.save(operation);
        }
        return "redirect:/manager/product/operation/product_id/{productId}";
    }

    @GetMapping("/product/operation/product_id/{productId}/delete/operation_id/{operationId}")
    public String deleteOperation(Model model, @PathVariable int operationId, @PathVariable int productId) {
        Optional<Operation> optOperation = operationRepository.findById(operationId);
        model.addAttribute("productId", productId);
        if (optOperation.isPresent()) {
            operationRepository.deleteById(operationId);
        }
        return "redirect:/manager/product/operation/product_id/{productId}";
    }

    @GetMapping("/product")
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
        return "/product/manager/index";
    }

    @GetMapping("/product/search")
    public String searchProduct(@RequestParam(defaultValue = "") String pName, Model model){
        // Using Map with key Product and Value total steps to show total steps on index page
        Map<Product, Integer> finalMap = new LinkedHashMap<>();

        int counter;

        List<Product> products = productRepository.findByProductNameStartingWithIgnoreCase(pName);

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

        model.addAttribute("productName", pName);
        model.addAttribute("products", finalMap);
        return "product/manager/search";
    }

    @GetMapping("/product/add")
    public String renderProductCreationPortal(Model model) {
        model.addAttribute("product", new Product());
        return "/product/manager/create_product";
    }

    @PostMapping("/product/add")
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
        return "redirect:/manager/product";
    }

    @GetMapping("/product/edit/{productId}")
    public String displayEditProductForm(Model model, @PathVariable int productId) {
        Optional<Product> productById = productRepository.findById(productId);
        if (productById.isPresent()) {
            Product product = productById.get();
            model.addAttribute("title", "Edit Product");
            model.addAttribute("product", product);
            return "/product/manager/edit";
        } else {
            return "redirect:/product/edit";
        }
    }

    @Transactional
    @PostMapping("/product/edit/{productId}")
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
        return "redirect:/manager/product";
    }

    @GetMapping("/product/delete/{productId}")
    public String deleteProduct(@PathVariable int productId) {
        Optional<Product> optProduct = productRepository.findById(productId);
        if (optProduct.isPresent()) {
            List<Operation> byproductProductId = operationRepository.findByproductProductId(productId);

            if (!byproductProductId.isEmpty()) {
                operationRepository.deleteAll(byproductProductId);
            }

            productRepository.deleteById(productId);
        }
        return "redirect:/manager/product";
    }

    @GetMapping("/jobs")
    public String index(Model model) {
        Iterable<Job> allJobs = jobRepository.findAll();
        List<Job> sortedJobs = new ArrayList<>();

        for (Job j : allJobs) {
            sortedJobs.add(j);
        }
        Collections.reverse(sortedJobs);

        model.addAttribute("jobs", sortedJobs);
        return "jobs/manager/index";
    }

    @GetMapping("/jobs/completed_jobs")
    public String completedJobs(Model model) {
        Iterable<Job> allJobs = jobRepository.findAll();
        List<Job> completedJobs = new ArrayList<>();

        for (Job j : allJobs) {
            if (Boolean.TRUE.equals(j.getIsCompleted())) {
                completedJobs.add(j);
            }
        }

        model.addAttribute("completedJobs", completedJobs);
        return "jobs/manager/completed_jobs";
    }

    @GetMapping("/jobs/add")
    public String displayAddJobForm(Model model) {
        model.addAttribute("products", productRepository.findAll());
        model.addAttribute(new Job());
        return "jobs/manager/job_add";
    }

    @PostMapping("/jobs/add")
    public String processAddJobForm(@ModelAttribute("job") Job newJob, Errors errors, Model model) {
        if (errors.hasErrors()) {
            model.addAttribute("title", "Add Job");
            return "/jobs/manager/job_add";
        }
        newJob.setWorkOrderNumber(createWONumber());
        Lot lot = createLotNumber(newJob.getProduct().getProductId());
        newJob.setLot(lot);
        newJob.setIsCompleted(Boolean.FALSE);
        newJob.setStartDate(LocalDate.now());

        Job job = jobRepository.save(newJob);

        createParts(newJob.getProduct().getProductId(), newJob.getQuantity(), newJob.getLot(), job);
        return "redirect:/manager";
    }

    @GetMapping("/jobs/edit_step/job_id/{jobId}")
    public String processEditJobStepsForm(Model model, @PathVariable int jobId) {
        Optional<Job> jobById = jobRepository.findById(jobId);
        if (jobById.isPresent()) {
            Job job = jobById.get();
            List<Operation> byproductProductId = operationRepository.findByproductProductId(job.getProduct().getProductId());
            model.addAttribute("steps", byproductProductId);
            model.addAttribute("job", job);
            return "/jobs/manager/job_edit_step";
        } else {
            return "/jobs/manager/edit_step/job_id/{jobId}";
        }
    }

    @PostMapping("/jobs/edit_step/job_id/{jobId}")
    public String processEditJobStepsForm(@PathVariable int jobId,
                                     @ModelAttribute Job editedJob,
                                     Model model) {
        Optional<Job> jobById = jobRepository.findById(jobId);
        if (jobById.isPresent()) {
            Job job = jobById.get();
            job.setCurrentStep(editedJob.getCurrentStep());
            jobRepository.save(job);
        }

        return "redirect:/manager/jobs/edit/{jobId}";
    }

    @GetMapping("/jobs/edit/{jobId}")
    public String displayEditJobForm(Model model, @PathVariable int jobId) {
        Optional<Job> jobById = jobRepository.findById(jobId);
        if (jobById.isPresent()) {
            Job job = jobById.get();
            if (Boolean.TRUE.equals(job.getIsCompleted())) {
                return "redirect:/manager/jobs";
            } else {
                model.addAttribute("job", job);
                model.addAttribute("dueDate", job.getDueDate());
                model.addAttribute("isCompleted", job.getIsCompleted());
                return "/jobs/manager/job_edit_manager";
            }
        } else {
            return "/redirect:/manager/jobs/edit";
        }
    }

    @PostMapping("/jobs/edit/{jobId}")
    public String processEditJobForm(@PathVariable int jobId,
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
        return "redirect:/manager";
    }

    @GetMapping("/view_parts")
    private String getAllParts(Model model) {
        model.addAttribute("parts", partRepository.findAll());
        return "parts/manager/index";
    }

    @GetMapping("/view_lots")
    private String getAlllots(Model model) {
        model.addAttribute("lots", lotRepository.findAll());
        return "lots/manager/index";
    }

    private String createWONumber() {
        Iterable<Job> jobs = jobRepository.findAll();
        int woNumber = 0;

        for (Job j : jobs) {
            woNumber = Integer.parseInt(j.getWorkOrderNumber().substring(2));
        }
        woNumber++;

        return "WO" + String.format(String.format("%04d", woNumber));
    }

    private int createOPNumber(int productId){
        List<Operation> byproductProductId = operationRepository.findByproductProductId(productId);
        int opNumber = 0;

        if (byproductProductId != null) {
            for (Operation o : byproductProductId) {
                opNumber = o.getOpNumber();
            }
            opNumber++;
        } else {
            opNumber = 1;
        }

        return opNumber;

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

    private Lot createLotNumber(int productId) {
        Optional<Product> byId = productRepository.findById(productId);
        Iterable<Lot> lots = lotRepository.findAll();
        Lot lot = new Lot();
        int lotNumber = 0;

        for (Lot l : lots) {
            lotNumber = Integer.parseInt(l.getLotNumber());
        }

        lotNumber++;

        lot.setLotNumber(String.format("%04d", lotNumber));
        lot.setProduct(byId.orElse(null));

        lotRepository.save(lot);

        return lot;
    }

    private void createParts(int productId, int quantity, Lot lot, Job job) {
        List<Part> byproductProductId = partRepository.findByproductProductId(productId);
        String productName = productRepository.findById(productId).orElse(null).getProductName();

        Part part = new Part();
        List<Part> totalParts = new ArrayList<>();

        part.setProduct(new Product(productId));

        if (byproductProductId.isEmpty()) {
            for (int i = 1; i < quantity + 1; i++) {
                part.setSerNum("SN" + "-" + String.format("%05d", i));
                part.setJob(job);
                totalParts.add(new Part(part.getSerNum(), lot, part.getProduct(), job));
            }
        } else {
            int serNum = 0;
            for (Part p : byproductProductId) {
                serNum = Integer.parseInt(p.getSerNum().substring(3));
            }
            for (int i = 1; i < quantity + 1; i++) {
                serNum++;
                part.setSerNum("SN" + "-" + String.format("%05d", serNum));
                totalParts.add(new Part(part.getSerNum(), lot, part.getProduct(), job));
            }
        }
        partRepository.saveAll(totalParts);
    }
}
