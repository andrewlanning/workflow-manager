package com.codewranglers.workflowmanager.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

/*
 * This class provides the model for each Product.
 * It is intended to be simplistic by nature for the sake of the project.
 *
 * Each product is intended to be for a specific type of part.
 * So if you were manufacturing a clock. The Product Name would be "Clock"
 *
 * If your company manufactures more than one type of Clock, consider a more descriptive name "Grandfather Clock"
 * Also consider a unique product name "Jefferson Grandfather Clock"
 * This will prevent future naming concerns.
 *
 * Also consider that a "Jefferson Grandfather Clock" will have parts assembled to create it.
 * "Jefferson Grandfather Clock - Big Hand" would also be considered a product. Feel free to use your own convention.
 *
 * Limitations?
 * For the sake of simplicity:
 * Each product will not retain each Lot/Part, but could be a future consideration.
 * Each product will not retain user who generated product.
 * Each product will not have a revision. Simply outside the scope given the current timeframe.
 *
 * Created by Andrew Lanning 12-06-23
 */

@Entity
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Integer productId;
    @NotBlank(message = "Product name is required")
    @Size(max = 255, message = "Product name must be less than 255 characters")
    @Column(name = "product_name")
    private String productName; // Unique name associated to the product (ie: 'Jefferson Grandfather Clock')
    @NotBlank(message = "Product Description is required")
    @Size(max = 255, message = "Product Description must be less than 255 characters")
    @Column(name = "product_description")
    private String productDescription;


    @OneToOne(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.EAGER, optional = true)
    private Image image;  //image associated with the product

    @OneToMany(mappedBy="product")
    private List<Operation> operationList; // A list of operation objects assigned to this product.

    @OneToMany(mappedBy="product")
    private List<Lot> lotList; // All lots that are assigned to this product

    @OneToMany(mappedBy="product")
    private List<Part> partsList; // All parts that are assigned to this product

//     User is only prompted for Name and Description, all other fields are set when the Job is created.
    public Product(String productName, String productDescription) {
        this.productName = productName;
        this.productDescription = productDescription;
    }

    public Product() {

    }

    public int getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public List<Operation> getOperationList() {
        return operationList;
    }

    public void setOperationList(List<Operation> operationList) {
        this.operationList = operationList;
    }

    public List<Lot> getLotList() {
        return lotList;
    }

    public void setLotList(List<Lot> lotList) {
        this.lotList = lotList;
    }

    public List<Part> getPartsList() {
        return partsList;
    }

    public void setPartsList(List<Part> partsList) {
        this.partsList = partsList;
    }


}