package com.codewranglers.workflowmanager.models.data;

import com.codewranglers.workflowmanager.models.Operation;
import com.codewranglers.workflowmanager.models.Product;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OperationRepository extends CrudRepository<Operation, Integer> {
    List<Operation> findByproductProductId(int productId);
}
