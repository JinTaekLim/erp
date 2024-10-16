package com.erp.erp.domain.accounts.repository;

import com.erp.erp.domain.accounts.common.entity.Accounts;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountsRepository extends JpaRepository<Accounts, Long> {

}
