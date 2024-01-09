package com.codewranglers.workflowmanager.models;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;

import java.util.Date;
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
@Table(name = "job")
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "job_id")
    private Integer jobId;

    @Column(name = "wo_number")
    private String workOrderNumber; // ie: WO1234

    @Column(name = "current_step")
    private int currentStep; // ie: 001 ultimately appended to workOrderNumber ie WO1234.001

    @Column(name = "quantity")
    private int quantity;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product; // User will select Product from list. Defining product here.

    @OneToOne
    @JoinColumn(name = "lot_id")
    private Lot lot; // Automatically generated upon object creation

    @OneToMany(mappedBy = "job")
    private List<Part> partsList;

    @Column(name = "is_completed")
    private Boolean isComplete; // When process is exhausted: True

    @Column(name = "start_date")
    @CreatedDate
    private Date startDate; // java.util.date : format MM-DD-YYYY

    @Column(name = "due_date")
    private Date dueDate;

    @Column(name = "completion_date")
    @Nullable
    private Date completionDate;

    public Job(Product product, int quantity, Date startDate, Date dueDate, Date completionDate) {
        this.product = product;
        this.quantity = quantity;
        this.startDate = startDate;
        this.dueDate = dueDate;
        this.completionDate = completionDate;
    }

    public Job() {
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

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public Date getCompletionDate() {
        return completionDate;
    }

    public void setCompletionDate(Date completaionDate) {
        this.completionDate = completaionDate;
    }
}

