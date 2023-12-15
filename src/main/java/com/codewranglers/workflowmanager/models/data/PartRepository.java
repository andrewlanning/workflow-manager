package com.codewranglers.workflowmanager.models.data;

import com.codewranglers.workflowmanager.models.Part;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PartRepository extends CrudRepository<Part, Integer> {
}
