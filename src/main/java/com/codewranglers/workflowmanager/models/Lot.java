package com.codewranglers.workflowmanager.models;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "lot")
public class Lot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lot_id")
    private Integer lotId;

    @Column(name = "lot_number")
    private String lotNumber; // Automatically Generated

    @OneToMany(mappedBy = "lot")
    private List<Part> partsList;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    public Lot(String lotNumber, List<Part> partsList, Product product) {
        this.lotNumber = lotNumber;
        this.partsList = partsList;
        this.product = product;
    }

    public Lot() {

    }

    public Lot(int lotId) {
        this.lotId = lotId;
    }

    public int getLotId() {
        return lotId;
    }

    public String getLotNumber() {
        return lotNumber;
    }

    public void setLotNumber(String lotNumber) {
        this.lotNumber = lotNumber;
    }

    public List<Part> getPartsList() {
        return partsList;
    }

    public void setPartsList(List<Part> partsList) {
        this.partsList = partsList;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}