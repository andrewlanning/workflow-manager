package com.codewranglers.workflowmanager.models.data;

import com.codewranglers.workflowmanager.models.NCR;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NCRRepository extends CrudRepository<NCR, Integer> {
}
