package com.codewranglers.workflowmanager.models.data;

import com.codewranglers.workflowmanager.models.Job;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobRepository extends CrudRepository<Job, Integer> {
    List<Job> findByIsCompleteFalse();
    List<Job> findByProductProductNameStartingWithIgnoreCase (String pName);
}
