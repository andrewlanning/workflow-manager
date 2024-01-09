package com.codewranglers.workflowmanager.models.data;

import com.codewranglers.workflowmanager.models.Image;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends CrudRepository<Image, Long> {
}
