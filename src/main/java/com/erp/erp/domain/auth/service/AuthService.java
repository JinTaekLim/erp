package com.erp.erp.domain.auth.service;

import com.erp.erp.domain.accounts.business.AccountsCreator;
import com.erp.erp.domain.accounts.business.AccountsReader;
import com.erp.erp.domain.accounts.common.entity.Accounts;
import com.erp.erp.domain.accounts.repository.AccountsRepository;
import com.erp.erp.domain.institutes.common.entity.Institutes;
import com.erp.erp.domain.institutes.repository.InstitutesRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

  private final AccountsCreator accountsCreator;
  private final AccountsReader accountsReader;
  private final InstitutesRepository institutesRepository;

  @Transactional
  public Accounts getAccountsInfo() {

    /* 임시 설정 */
    Institutes institutes = Institutes.builder()
        .name("test")
        .totalSpots(4)
        .build();

    institutesRepository.save(institutes);

    Accounts accounts = accountsReader.findOptionalById(1L)
        .orElseGet(() -> {
          Accounts newAccount = Accounts.builder()
              .accountId("test")
              .password("test")
              .institutes(institutes)
              .build();

          return accountsCreator.save(newAccount);
        });

    return accounts;
  }
}
