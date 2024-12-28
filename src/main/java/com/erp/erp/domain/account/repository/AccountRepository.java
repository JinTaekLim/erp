package com.erp.erp.domain.account.repository;

import com.erp.erp.domain.account.common.entity.Account;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long>, AccountRepositoryCustom{

  Optional<Account> findByAccountIdAndPassword(String accountId, String password);

}
