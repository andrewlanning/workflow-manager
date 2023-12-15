package com.codewranglers.workflowmanager.models.data;

import com.codewranglers.workflowmanager.models.Product;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends CrudRepository<Product, Integer> {
}
