package com.erp.erp.domain.account.repository;

import com.erp.erp.domain.account.common.entity.Account;
import com.erp.erp.domain.institute.common.entity.Institute;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AccountRepository extends JpaRepository<Account, Long> {

  Optional<Account> findByAccountAndPassword(String account, String password);

  @Query("SELECT a.institute FROM Account a WHERE a.id = :accountId")
  Optional<Institute> findInstitutesByAccountId(@Param("accountId") Long accountId);


}
