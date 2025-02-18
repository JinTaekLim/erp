package com.erp.erp.domain.account.business;

import com.erp.erp.domain.account.common.entity.Account;
import com.erp.erp.domain.account.repository.AccountRepository;
import com.erp.erp.domain.admin.common.dto.UpdateAccountDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class AccountUpdater {

  private final AccountRepository accountRepository;

  public Account update(Account account, UpdateAccountDto.Request req, String updatedId) {
    account.updateIdentifierAndPassword(req.getIdentifier(), req.getPassword(), updatedId);
    return accountRepository.save(account);
  }

  public void lockAccount(Account account, String updatedId) {
    account.updateLocked(true, updatedId);
    accountRepository.save(account);
  }
}
