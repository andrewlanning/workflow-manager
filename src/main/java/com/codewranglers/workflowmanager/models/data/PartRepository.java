package com.codewranglers.workflowmanager.models.data;

import com.codewranglers.workflowmanager.models.Part;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PartRepository extends CrudRepository<Part, Integer> {

    List<Part> findByproductProductId(int productId);

    List<Part> findByjobJobId(int jobId);
}
