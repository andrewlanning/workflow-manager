package com.codewranglers.workflowmanager.models.data;

import com.codewranglers.workflowmanager.models.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

}
