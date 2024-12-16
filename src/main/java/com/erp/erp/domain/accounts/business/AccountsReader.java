package com.erp.erp.domain.accounts.business;

import com.erp.erp.domain.accounts.common.entity.Accounts;
import com.erp.erp.domain.accounts.common.exception.InvalidCredentialsException;
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

  public Accounts findById(Long accountId) {
    return accountsRepository.findById(accountId).orElse(null);
  }

  public Accounts findByAccountAndPassword(String account, String password) {
    return accountsRepository.findByAccountAndPassword(account, password)
        .orElseThrow(InvalidCredentialsException::new);
  };
}
