package com.erp.erp.domain.account.business;

import com.erp.erp.domain.account.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class AccountUpdater {
  private final AccountRepository accountRepository;
}
