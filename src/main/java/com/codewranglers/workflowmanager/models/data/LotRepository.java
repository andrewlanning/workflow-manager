package com.codewranglers.workflowmanager.models.data;

import com.codewranglers.workflowmanager.models.Lot;
import com.codewranglers.workflowmanager.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LotRepository extends CrudRepository<Lot, Integer> {
    Lot findByproduct(Product product);
}
