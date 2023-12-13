package com.codewranglers.workflowmanager.models.data;

import com.codewranglers.workflowmanager.models.Job;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobRepository extends CrudRepository<Job, Integer> {
}
