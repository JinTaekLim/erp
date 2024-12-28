package com.erp.erp.domain.account.repository;

import com.erp.erp.domain.institute.common.entity.Institute;
import java.util.Optional;

public interface AccountRepositoryCustom {

  Optional<Institute> findInstitutesByAccountId(Long accountId);

}
