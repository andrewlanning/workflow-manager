package com.codewranglers.workflowmanager.models;

import jakarta.persistence.*;

/*
 * This class provides the model for each Operation.
 *
 * Each Process object will house a number of operations (min. 1)
 * Creates unique step number for each step.
 * Provides description of operation and operation text.
 *
 * Limitiations?
 * Due to scope, no way to house requirements pdfs/documents/images etc.
 *
 * Created by Andrew Lanning 12-06-23
 */

@Entity
public class Operation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int operationId;

    private String opName;
    private int opNumber;
    private String opText;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    public Operation(String opName, String opText) {
        this.opName = opName;
        this.opText = opText;
    }

    public int getOperationId() {
        return operationId;
    }

    public String getOpName() {
        return opName;
    }

    public void setOpName(String opName) {
        this.opName = opName;
    }

    public int getOpNumber() {
        return opNumber;
    }

    public void setOpNumber(int opNumber) {
        this.opNumber = opNumber;
    }

    public String getOpText() {
        return opText;
    }

    public void setOpText(String opText) {
        this.opText = opText;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}