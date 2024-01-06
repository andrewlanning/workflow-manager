package com.codewranglers.workflowmanager.models;

import jakarta.persistence.*;

@Entity
@Table(name = "part")
public class Part {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer partId;
    @Column(name = "ser_number")
    private String serNum;

    @Column(name = "part_name")
    private String partName;
    @Column(name = "part_lotnum")
    private String lot;
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
    @Transient
    private int partQuantity;

    public Part(String partName, String serNum, String lot, Product product, int partQuantity) {
        this.partName = partName;
        this.serNum = serNum;
        this.lot = lot;
        this.product = product;
        this.partQuantity = partQuantity;
    }

    public Part() {
    }

    public String getPartName() {
        return partName;
    }

    public void setPartName(String partName) {
        this.partName = partName;
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

    public String getLot() {
        return lot;
    }

    public void setLot(String lot) {
        this.lot = lot;
    }

    public int getPartQuantity() {
        return partQuantity;
    }

    public void setPartQuantity(int partQuantity) {
        this.partQuantity = partQuantity;
    }
}
