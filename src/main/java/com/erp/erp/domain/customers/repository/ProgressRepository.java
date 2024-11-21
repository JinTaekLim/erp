package com.erp.erp.domain.customers.repository;

import com.erp.erp.domain.customers.common.entity.Progress;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProgressRepository extends MongoRepository<Progress, Long> {

}
