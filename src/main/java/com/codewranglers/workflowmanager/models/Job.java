package com.codewranglers.workflowmanager.models;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDate;
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

    @Column(name = "is_complete")
    private Boolean isCompleted; // When process is exhausted: True

    @CreatedDate
    @Column(name = "start_date")
    private LocalDate startDate; // java.util.date : format MM-DD-YYYY

    @Column(name = "due_date")
    private LocalDate dueDate;

    @Column(name = "completion_date")
    @Nullable
    private LocalDate completionDate;

    public Job(Product product, int quantity, LocalDate startDate, LocalDate dueDate, LocalDate completionDate) {
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

    public Boolean getIsCompleted() {
        return isCompleted;
    }

    public void setIsCompleted(Boolean isCompleted) {
        this.isCompleted = isCompleted;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public LocalDate getCompletionDate() {
        return completionDate;
    }

    public void setCompletionDate(LocalDate completaionDate) {
        this.completionDate = completaionDate;
    }

    public String getWorkOrderNumber() {
        return workOrderNumber;
    }

    public void setWorkOrderNumber(String workOrderNumber) {
        this.workOrderNumber = workOrderNumber;
    }

    public int getCurrentStep() {
        return currentStep;
    }

    public void setCurrentStep(int currentStep) {
        this.currentStep = currentStep;
    }

}

