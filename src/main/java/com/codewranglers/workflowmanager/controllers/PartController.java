package com.codewranglers.workflowmanager.controllers;


import com.codewranglers.workflowmanager.models.Lot;
import com.codewranglers.workflowmanager.models.Part;
import com.codewranglers.workflowmanager.models.data.PartRepository;
import com.codewranglers.workflowmanager.models.data.ProductRepository;
import com.mysql.cj.util.StringUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/part")
public class PartController {

    @Autowired
    private PartRepository partRepository;
    @Autowired
    private ProductRepository productRepository;

    @GetMapping("")
    public String renderPartPortal(Model model) {
        model.addAttribute("parts", partRepository.findAll());
        return "/part/index";
    }

    @GetMapping("/add")
    public String renderPartCreationPortal(Model model) {
        model.addAttribute("products", productRepository.findAll());
        model.addAttribute("part", new Part());
        return "/part/create_part";
    }

    @PostMapping("/add")
    public String processPartCreation(@ModelAttribute("part") Part part) {
        List<Part> bypartName = partRepository.findBypartName(part.getPartName());
        List<Part> totalParts = new ArrayList<>();
        if (bypartName.isEmpty()) {
            for (int i = 1; i < part.getPartQuantity() + 1; i++) {
                part.setLot(String.format("%04d", 1));
                part.setSerNum("SN" + part.getProduct().getProductId() + "-" + part.getPartName().substring(0, 3).toUpperCase() + String.format("%03d", i));
                totalParts.add(new Part(part.getPartName(), part.getSerNum(), part.getLot(), part.getProduct(), i));
            }
        } else {
            int serNum = 0;
            int lotNum = 0;
            for (Part p : bypartName) {
                serNum = Integer.parseInt(p.getSerNum().substring(8));
                lotNum = Integer.parseInt(p.getLot()) + 1;
            }
            for (int i = 1; i < part.getPartQuantity() + 1; i++) {
                serNum++;
                part.setLot(String.format("%04d", lotNum));
                part.setSerNum("SN" + part.getProduct().getProductId() + "-" + part.getPartName().substring(0, 3).toUpperCase() + String.format("%03d", serNum));
                totalParts.add(new Part(part.getPartName(), part.getSerNum(), part.getLot(), part.getProduct(), i));
            }
        }
        partRepository.saveAll(totalParts);
        return "redirect:/part";
    }

    @GetMapping("/edit/{partId}")
    public String displayEditPartForm(Model model, @PathVariable int partId) {
        Optional<Part> partById = partRepository.findById(partId);
        if (partById.isPresent()) {
            Part part = partById.get();
            model.addAttribute("title", "Edit Part");
            model.addAttribute("part", part);
            model.addAttribute("products", productRepository.findAll());
            return "/part/edit";
        } else {
            return "redirect:/part/edit";
        }
    }

    @PostMapping("/edit/{partId}")
    public String processEditPartForm(@PathVariable int partId,
                                      @ModelAttribute @Valid Part editedPart,
                                      Errors errors, Model model) {

        if (errors.hasErrors()) {
            model.addAttribute("title", "Edit Part");
            return "/part/edit";
        }

        Optional<Part> partById = partRepository.findById(partId);
        List<Part> bypartName = partRepository.findBypartName(editedPart.getPartName());
        if (partById.isPresent()) {
            Part part = partById.get();
            part.setProduct(editedPart.getProduct());

            if (!part.getPartName().equals(editedPart.getPartName())) {
                part.setPartName(editedPart.getPartName());

                if (bypartName == null) {
                    part.setSerNum("SN" + part.getProduct().getProductId() + "-" + part.getPartName().substring(0, 3).toUpperCase() + String.format("%03d", 1));
                } else {
                    int serNum = 0;
                    for (Part p : bypartName) {
                        serNum = Integer.parseInt(p.getSerNum().substring(8));
                    }
                    serNum++;
                    part.setSerNum("SN" + part.getProduct().getProductId() + "-" + part.getPartName().substring(0, 3).toUpperCase() + String.format("%03d", serNum));

                }
            }
            partRepository.save(part);
        }
        return "redirect:/part";
    }

    @GetMapping("/edit/lot")
    public String displayEditPartLotForm(Model model) {
        Iterable<Part> all = partRepository.findAll();
        List<String> lotNumList = new ArrayList<>();
        int i  = 1;
        for (Part p : all){
            int lot = Integer.parseInt(p.getLot());
            if (lot == i){
                lotNumList.add(String.format("%04d", lot));
                i++;
            }
        }
        model.addAttribute("title", "Edit Part");
        model.addAttribute("part", new Part());
        model.addAttribute("lotNumbers", lotNumList);
        model.addAttribute("products", productRepository.findAll());
        return "/part/edit_lot";
    }

    @PostMapping("/edit/lot")
    public String processEditPartLotForm(@ModelAttribute @Valid Part editedPart,
                                         Errors errors, Model model) {

//        if (errors.hasErrors()) {
//            model.addAttribute("title", "Edit Part");
//            return "/part/edit";
//        }
//
//        Optional<Part> partById = partRepository.findById(lotNum);
//        List<Part> bypartName = partRepository.findBypartName(editedPart.getPartName());
//        if (partById.isPresent()) {
//            Part part = partById.get();
//            part.setProduct(editedPart.getProduct());
//
//            if (!part.getPartName().equals(editedPart.getPartName())) {
//                part.setPartName(editedPart.getPartName());
//
//                if (bypartName == null) {
//                    part.setSerNum("SN" + part.getProduct().getProductId() + "-" + part.getPartName().substring(0, 3).toUpperCase() + String.format("%03d", 1));
//                } else {
//                    int serNum = 0;
//                    for (Part p : bypartName) {
//                        serNum = Integer.parseInt(p.getSerNum().substring(8));
//                    }
//                    serNum++;
//                    part.setSerNum("SN" + part.getProduct().getProductId() + "-" + part.getPartName().substring(0, 3).toUpperCase() + String.format("%03d", serNum));
//
//                }
//            }
//            partRepository.save(part);
//        }
        return "redirect:/part";
    }

    @GetMapping("/delete/{partId}")
    public String deletePart(@PathVariable int partId) {
        Optional<Part> optPart = partRepository.findById(partId);
        if (optPart.isPresent()) {
            partRepository.deleteById(partId);
        }
        return "redirect:/part";
    }
}
