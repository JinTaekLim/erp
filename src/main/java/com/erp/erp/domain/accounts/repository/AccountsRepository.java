package com.erp.erp.domain.accounts.repository;

import com.erp.erp.domain.accounts.common.entity.Accounts;
import com.erp.erp.domain.institutes.common.entity.Institutes;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AccountsRepository extends JpaRepository<Accounts, Long> {

  Optional<Accounts> findByAccountAndPassword(String account, String password);

  @Query("SELECT a.institutes FROM Accounts a WHERE a.id = :accountId")
  Optional<Institutes> findInstitutesByAccountId(@Param("accountId") Long accountId);


}
