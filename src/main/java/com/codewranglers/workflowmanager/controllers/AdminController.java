package com.codewranglers.workflowmanager.controllers;


import com.codewranglers.workflowmanager.models.*;
import com.codewranglers.workflowmanager.models.data.*;
import com.codewranglers.workflowmanager.models.dto.CreateUserDTO;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.codewranglers.workflowmanager.models.dto.UpdatePasswordDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private OperationRepository operationRepository;
    @Autowired
    private LotRepository lotRepository;
    @Autowired
    private JobRepository jobRepository;
    @Autowired
    private PartRepository partRepository;
    @Autowired
    private ImageRepository imageRepository;


    int productId;

    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @GetMapping("")
    public String renderAdminPortal(Model model) {
        List<String> pages = new ArrayList<>();
        pages.add("User Management");
        pages.add("Workflow Management");

        List<String> user = new ArrayList<>();
        user.add("Manufacturing Operator");
        user.add("Product Manager");
        user.add("Administrator");

        List<String> urlStrings = new ArrayList<>();
        urlStrings.add("usermanagement");
        urlStrings.add("workflowmanagement");

        model.addAttribute("pages", pages);
        model.addAttribute("user", user);
        model.addAttribute("url", urlStrings);
        return "/admin/index";
    }

    @GetMapping("/user_management")
    public String renderUserManagementPortal(Model model) {
        Iterable<User> users = userRepository.findAll();

        // Using Map with key as User and Value as Role to show Role name on user index page.
        Map<User, String> userMap = new LinkedHashMap<>();

        for (User user : users) {
            if (user.getRole() == 1) {
                userMap.put(user, "Manager");
            }
            if (user.getRole() == 2) {
                userMap.put(user, "Member");
            }
            if (user.getRole() == 3) {
                userMap.put(user, "Administrator");
            }
        }
        model.addAttribute("users", userMap);
        return "admin/user_management/index";
    }

    @GetMapping("/user_management/search")
    public String searchUser(@RequestParam(defaultValue = "") String fName, Model model){
        List<User> userList = userRepository.findByFirstnameStartingWithIgnoreCase(fName);

        // Using Map with key as User and Value as Role to show Role name on user index page.
        Map<User, String> userMap = new LinkedHashMap<>();

        for (User user : userList) {
            if (user.getRole() == 1) {
                userMap.put(user, "Manager");
            }
            if (user.getRole() == 2) {
                userMap.put(user, "Member");
            }
            if (user.getRole() == 3) {
                userMap.put(user, "Administrator");
            }
        }
        model.addAttribute("FirstName", fName);
        model.addAttribute("users", userMap);
        return "admin/user_management/search_user";
    }

    @GetMapping("/user_management/create_user")
    public String renderUserCreationPortal(Model model) {
        model.addAttribute(new CreateUserDTO());
        return "admin/user_management/create_user";
    }


    @PostMapping("/user_management/create_user")
    public String renderUserCreated(@ModelAttribute @Valid CreateUserDTO createUserDTO, Errors errors, HttpServletRequest request) {

        if (errors.hasErrors()) {
            return "admin/user_management/create_user";
        }
        //check for existing user with username
        User existingUser = userRepository.findByUsername(createUserDTO.getUsername());
        if (existingUser != null) {
            errors.rejectValue("username", "username.alreadyExists", "That username is already in use.");
            return "admin/user_management/create_user";
        }
        //check password and confirmPassword match
        String password = createUserDTO.getPassword();
        String confirmPassword = createUserDTO.getConfirmPassword();
        if (!password.equals(confirmPassword)) {
            errors.rejectValue("password", "password.mismatch", "Passwords do not match.");
            return "admin/user_management/create_user";
        }

        User newUser = new User(createUserDTO.getFirstName(), createUserDTO.getLastName(), createUserDTO.getUsername(), createUserDTO.getEmail(), createUserDTO.getPassword(), createUserDTO.getRole());


        userRepository.save(newUser);
        return "redirect:/admin/user_management";
    }

    @GetMapping("/user_management/edit/{userId}")
    public String displayEditUserForm(Model model, @PathVariable int userId) {
        Optional<User> optUser = userRepository.findById(userId);
        if (optUser.isPresent()) {
            User user = optUser.get();
            model.addAttribute("title", "Edit User");
            model.addAttribute("user", user);
            return "admin/user_management/edit";
        } else {
            return "redirect:admin/user_management/edit";
        }
    }
    @PostMapping("/user_management/edit/{userId}")
    public String processEditUserForm(@PathVariable int userId,
                                      @ModelAttribute @Valid User editedUser,
                                      Errors errors, Model model) {

        Optional<User> optUser = userRepository.findById(userId);
        if (optUser.isPresent()) {
            User user = optUser.get();
            user.setUsername(editedUser.getUsername());
            user.setFirstname(editedUser.getFirstname());
            user.setLastname(editedUser.getLastname());
            user.setRole(editedUser.getRole());
            user.setEmail(editedUser.getEmail());
            userRepository.save(user);
        }
        if (errors.hasErrors()) {
            model.addAttribute("title", "Edit user");
            return "admin/user_management/edit";
        }

        return "redirect:/admin/user_management";
    }

    @GetMapping("/user_management/update_password/{userId}")
    public String displayUpdatePasswordForm(Model model, @PathVariable int userId) {
        model.addAttribute(new UpdatePasswordDTO());
        Optional<User> optUser = userRepository.findById(userId);
        if (optUser.isPresent()) {
            User user = optUser.get();
            model.addAttribute("user", user);
            return "admin/user_management/update_password";
        } else {
            return "redirect:admin/user_management";
        }
    }

    @PostMapping("/user_management/update_password/{userId}")
    public String processUpdatePasswordForm(@PathVariable int userId,
                                            @ModelAttribute @Valid UpdatePasswordDTO updatePasswordDTO,
                                            Errors errors) {
        if (errors.hasErrors()) {
            return ("redirect:/admin/user_management/update_password/" + userId);
        }

        String password = updatePasswordDTO.getPassword();
        String confirmPassword = updatePasswordDTO.getConfirmPassword();
        if (!password.equals(confirmPassword)) {
            errors.rejectValue("password", "password.mismatch", "Passwords do not match.");
            return ("redirect:/admin/user_management/update_password/" + userId);
        }

        Optional<User> optUser = userRepository.findById(userId);
        if (optUser.isPresent()) {
            User user = optUser.get();
            user.setPwhash(encoder.encode(password));
            userRepository.save(user);
        }
        return "redirect:/admin/user_management";
    }

    @GetMapping("/user_management/delete/{userId}")
    public String deleteUser(@PathVariable int userId) {
        Optional<User> optUser = userRepository.findById(userId);
        if (optUser.isPresent()) {
            userRepository.deleteById(userId);
        }
        return "redirect:/admin/user_management";
    }

    @GetMapping("/workflow_management")
    public String renderWorkflowManagementPortal(Model model) {

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

        return "/admin/workflow_management/index";
    }

    @GetMapping("/workflow_management/job/search")
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

        return "/admin/workflow_management/search";
    }

    // Steps Index page
    @GetMapping("/workflow_management/product/operation/product_id/{productId}")
    public String renderOperationPortal(Model model, @PathVariable int productId) {
        String productName = productRepository.findById(productId).get().getProductName();
        model.addAttribute("productName", productName);
        model.addAttribute("operations", operationRepository.findByproductProductId(productId));
        this.productId=productId;
        return "/operation/admin/index";
    }

    // Add Steps Get Endpoint
    @GetMapping("/workflow_management/product/operation/product_id/{productId}/add")
    public String renderOperationCreationPortal(Model model,  @PathVariable int productId) {
        String productName = productRepository.findById(productId).get().getProductName();
        model.addAttribute("productName", productName);
        model.addAttribute("productId", this.productId);
        model.addAttribute("operations", new Operation());
        return "/operation/admin/create_operation";
    }

    // Add Steps Post Endpoint
    @PostMapping("/workflow_management/product/operation/product_id/{productId}/add")
    public String processOperationCreation(@ModelAttribute("operations") Operation operation) {
        operation.setOpNumber(createOPNumber(productId));
        operation.setProduct(new Product(productId));
        operationRepository.save(operation);
        return "redirect:/admin/workflow_management/product/operation/product_id/{productId}";
    }

    //Edit Steps Get Endpoint
    @GetMapping("/workflow_management/product/operation/product_id/{productId}/edit/operation_id/{operationId}")
    public String displayEditOperationForm(Model model, @PathVariable int operationId, @PathVariable int productId) {
        Optional<Operation> operationById = operationRepository.findById(operationId);
        String productName = productRepository.findById(productId).get().getProductName();
        model.addAttribute("productName", productName);
        model.addAttribute("productId", productId);

        if (operationById.isPresent()) {
            Operation operation = operationById.get();
            model.addAttribute("operation", operation);
            model.addAttribute("products", productRepository.findAll());
            return "/operation/admin/edit";
        } else {
            return "redirect:/admin/workflow_management/product/operation/product_id/{productId}";
        }
    }

    //Edit steps Post Endpoint
    @PostMapping("/workflow_management/product/operation/product_id/{productId}/edit/operation_id/{operationId}")
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
        return "redirect:/admin/workflow_management/product/operation/product_id/{productId}";
    }

    @GetMapping("/workflow_management/product/operation/product_id/{productId}/delete/operation_id/{operationId}")
    public String deleteOperation(Model model, @PathVariable int operationId, @PathVariable int productId) {
        Optional<Operation> optOperation = operationRepository.findById(operationId);
        model.addAttribute("productId", productId);
        if (optOperation.isPresent()) {
            operationRepository.deleteById(operationId);
        }
        return "redirect:/admin/workflow_management/product/operation/product_id/{productId}";
    }

    @GetMapping("/workflow_management/product")
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
        return "/product/admin/index";
    }

    @GetMapping("/workflow_management/product/search")
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
        return "product/admin/search";
    }

    @GetMapping("/workflow_management/product/add")
    public String renderProductCreationPortal(Model model) {
        model.addAttribute("product", new Product());
        return "/product/admin/create_product";
    }

    @PostMapping("/workflow_management/product/add")
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
        return "redirect:/admin/workflow_management/product";
    }

    @GetMapping("/workflow_management/product/edit/{productId}")
    public String displayEditProductForm(Model model, @PathVariable int productId) {
        Optional<Product> productById = productRepository.findById(productId);
        if (productById.isPresent()) {
            Product product = productById.get();
            model.addAttribute("title", "Edit Product");
            model.addAttribute("product", product);
            return "/product/admin/edit";
        } else {
            return "redirect:/admin/workflow_management/product/edit";
        }
    }

    @Transactional
    @PostMapping("/workflow_management/product/edit/{productId}")
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
        return "redirect:/admin/workflow_management/product";
    }

    @GetMapping("/workflow_management/product/delete/{productId}")
    public String deleteProduct(@PathVariable int productId) {
        Optional<Product> optProduct = productRepository.findById(productId);
        if (optProduct.isPresent()) {
            List<Operation> byproductProductId = operationRepository.findByproductProductId(productId);

            if (!byproductProductId.isEmpty()) {
                operationRepository.deleteAll(byproductProductId);
            }

            productRepository.deleteById(productId);
        }
        return "redirect:/admin/workflow_management/product";
    }

    @GetMapping("/workflow_management/jobs")
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

    @GetMapping("/workflow_management/jobs/completed_jobs")
    public String completedJobs(Model model) {
        Iterable<Job> allJobs = jobRepository.findAll();
        List<Job> completedJobs = new ArrayList<>();

        for (Job j : allJobs) {
            if (Boolean.TRUE.equals(j.getIsCompleted())) {
                completedJobs.add(j);
            }
        }

        model.addAttribute("completedJobs", completedJobs);
        return "jobs/admin/completed_jobs";
    }

    @GetMapping("/workflow_management/jobs/add")
    public String displayAddJobForm(Model model) {
        model.addAttribute("products", productRepository.findAll());
        model.addAttribute(new Job());
        return "jobs/admin/job_add";
    }

    @PostMapping("/workflow_management/jobs/add")
    public String processAddJobForm(@ModelAttribute("job") Job newJob, Errors errors, Model model) {
        if (errors.hasErrors()) {
            model.addAttribute("title", "Add Job");
            model.addAttribute("products", productRepository.findAll());
            return "/jobs/admin/job_add";
        }

        // Fetch operations for the selected product
        List<Operation> productOperations = operationRepository.findByproductProductId(newJob.getProduct().getProductId());

        // Check if the product has operations
        if (productOperations.isEmpty()) {
            // Product doesn't have any operations, return an error message
            model.addAttribute("title", "Add Job");
            model.addAttribute("error", "Selected product must have operations to create a job");
            model.addAttribute("products", productRepository.findAll());
            return "/jobs/admin/job_add";
        }

        newJob.setWorkOrderNumber(createWONumber());
        Lot lot = createLotNumber(newJob.getProduct().getProductId());
        newJob.setLot(lot);
        newJob.setIsCompleted(Boolean.FALSE);
        newJob.setStartDate(LocalDate.now());
        newJob.setCurrentStep(1);

        Job job = jobRepository.save(newJob);

        createParts(newJob.getProduct().getProductId(), newJob.getQuantity(), newJob.getLot(), job);
        return "redirect:/admin/workflow_management";
    }

    @GetMapping("/workflow_management/jobs/edit_step/job_id/{jobId}")
    public String processEditJobStepsForm(Model model, @PathVariable int jobId) {
        Optional<Job> jobById = jobRepository.findById(jobId);
        if (jobById.isPresent()) {
            Job job = jobById.get();
            List<Operation> byproductProductId = operationRepository.findByproductProductId(job.getProduct().getProductId());
            model.addAttribute("steps", byproductProductId);
            model.addAttribute("job", job);
            return "/jobs/admin/job_edit_step";
        } else {
            return "/jobs/admin/edit_step/job_id/{jobId}";
        }
    }

    @PostMapping("/workflow_management/jobs/edit_step/job_id/{jobId}")
    public String processEditJobStepsForm(@PathVariable int jobId,
                                          @ModelAttribute Job editedJob,
                                          Model model) {
        Optional<Job> jobById = jobRepository.findById(jobId);
        if (jobById.isPresent()) {
            Job job = jobById.get();
            job.setCurrentStep(editedJob.getCurrentStep());
            jobRepository.save(job);
        }

        return "redirect:/admin/workflow_management/jobs/edit/{jobId}";
    }

    @GetMapping("/workflow_management/jobs/edit/{jobId}")
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
                return "/jobs/admin/job_edit";
            }
        } else {
            return "/redirect:/admin/workflow_management/jobs/edit";
        }
    }

    @PostMapping("/workflow_management/jobs/edit/{jobId}")
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
        return "redirect:/admin/workflow_management";
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
