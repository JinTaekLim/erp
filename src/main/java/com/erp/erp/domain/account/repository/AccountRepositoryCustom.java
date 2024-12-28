package com.erp.erp.domain.account.repository;

import com.erp.erp.domain.institute.common.entity.Institute;
import java.util.Optional;
import org.springframework.data.repository.query.Param;

public interface AccountRepositoryCustom {

  Optional<Institute> findInstitutesByAccountId(@Param("accountId") Long accountId);

}
