package com.codewranglers.workflowmanager.models;

import jakarta.persistence.*;

import java.util.List;

/*
 * This class provides the model for each Job.
 *
 * Each Job object will be defined by a particular Product when that Job is created.
 * Each new job will have a unique lot number assigned to it.
 * Each lot within that job will have a number of parts defined by part quantity.
 *
 * Limitations?
 * No date created at the moment.
 *
 * Created by Andrew Lanning 12-06-23
 */

@Entity
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer jobId;

    private String workOrderNumber; // ie: WO1234

    private int currentStep; // ie: 001 ultimately appended to workOrderNumber ie WO1234.001

    private int quantity;

    @ManyToOne
    @JoinColumn(name= "product_id")
    private Product product; // User will select Product from list. Defining product here.

    @OneToOne
    @JoinColumn(name = "lot_id")
    private Lot lot; // Automatically generated upon object creation

    @OneToMany(mappedBy = "job")
    private List<Part> partsList;

    private Boolean isComplete; // When process is exhausted: True

    private String startDate; // java.util.date : format MM-DD-YYYY

    private String dueDate;

    public Job(Product product, int quantity, String startDate, String dueDate) {
        this.product = product;
        this.quantity = quantity;
        this.startDate = startDate;
        this.dueDate = dueDate;
    }

    public int getJobId() {
        return jobId;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Lot getLot() {
        return lot;
    }

    public void setLot(Lot lot) {
        this.lot = lot;
    }

    public List<Part> getPartsList() {
        return partsList;
    }

    public void setPartsList(List<Part> partsList) {
        this.partsList = partsList;
    }

    public Boolean getComplete() {
        return isComplete;
    }

    public void setComplete(Boolean complete) {
        isComplete = complete;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }
}

