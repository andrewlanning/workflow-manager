package com.codewranglers.workflowmanager.models.data;

import com.codewranglers.workflowmanager.models.Operation;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OperationRepository extends CrudRepository<Operation, Integer> {
}
