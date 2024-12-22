package com.erp.erp.domain.customer.repository;

import com.erp.erp.domain.customer.common.entity.Progress;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProgressRepository extends MongoRepository<Progress, Long> {

}
