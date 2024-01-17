package com.codewranglers.workflowmanager.models.data;

import com.codewranglers.workflowmanager.models.Product;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends CrudRepository<Product, Integer> {

    List<Product> findByProductNameStartingWithIgnoreCase(String pName);
}
