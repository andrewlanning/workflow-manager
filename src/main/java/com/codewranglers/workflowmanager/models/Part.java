package com.codewranglers.workflowmanager.models;

import jakarta.persistence.*;

@Entity
@Table(name = "part")
public class Part {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer partId;

    @Column(name = "ser_num")
    private String serNum;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "lot_id")
    private Lot lot;

    @ManyToOne
    @JoinColumn(name = "job_id")
    private Job job;

    public Part(String serNum, Lot lot, Product product, Job job) {
        this.serNum = serNum;
        this.lot = lot;
        this.product = product;
        this.job = job;
    }

    public Part() {

    }

    public int getPartId() {
        return partId;
    }

    public String getSerNum() {
        return serNum;
    }

    public void setSerNum(String serNum) {
        this.serNum = serNum;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Lot getLot() {
        return lot;
    }

    public void setLot(Lot lot) {
        this.lot = lot;
    }

    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }
}
