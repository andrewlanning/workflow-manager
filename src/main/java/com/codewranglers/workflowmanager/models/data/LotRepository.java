package com.codewranglers.workflowmanager.models.data;

import com.codewranglers.workflowmanager.models.Lot;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LotRepository extends CrudRepository<Lot, Long> {
}
