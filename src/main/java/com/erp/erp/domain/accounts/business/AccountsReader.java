package com.erp.erp.domain.accounts.business;

import com.erp.erp.domain.accounts.common.entity.Accounts;
import com.erp.erp.domain.accounts.repository.AccountsRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class AccountsReader {
  private final AccountsRepository accountsRepository;


  public Optional<Accounts> findOptionalById(Long id) {
    return accountsRepository.findById(id);
  }
}
