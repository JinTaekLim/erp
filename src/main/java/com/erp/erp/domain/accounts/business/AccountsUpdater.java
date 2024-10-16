package com.erp.erp.domain.accounts.business;

import com.erp.erp.domain.accounts.repository.AccountsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class AccountsUpdater {
  private final AccountsRepository accountsRepository;
}
