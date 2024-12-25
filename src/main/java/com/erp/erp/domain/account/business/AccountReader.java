package com.erp.erp.domain.account.business;

import com.erp.erp.domain.account.common.entity.Account;
import com.erp.erp.domain.account.common.exception.AccountInstituteNotFoundException;
import com.erp.erp.domain.account.common.exception.InvalidCredentialsException;
import com.erp.erp.domain.account.repository.AccountRepository;
import com.erp.erp.domain.institute.common.entity.Institute;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class AccountReader {
  private final AccountRepository accountRepository;


  public Optional<Account> findOptionalById(Long id) {
    return accountRepository.findById(id);
  }

  public Account findById(Long accountId) {
    return accountRepository.findById(accountId).orElse(null);
  }

  public Account findByAccountAndPassword(String account, String password) {
    return accountRepository.findByAccountAndPassword(account, password)
        .orElseThrow(InvalidCredentialsException::new);
  };

  public Institute findInstitutesByAccountId(Long accountId) {
    return accountRepository.findInstitutesByAccountId(accountId)
        .orElseThrow(AccountInstituteNotFoundException::new);
  }
}
