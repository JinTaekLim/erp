package com.erp.erp.domain.auth.business;

import com.erp.erp.domain.accounts.business.AccountsCreator;
import com.erp.erp.domain.accounts.business.AccountsReader;
import com.erp.erp.domain.accounts.common.entity.Accounts;
import com.erp.erp.domain.institutes.common.entity.Institutes;
import com.erp.erp.domain.institutes.repository.InstitutesRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthProvider {

  private final AccountsCreator accountsCreator;
  private final AccountsReader accountsReader;
  private final InstitutesRepository institutesRepository;

  @Transactional
  public Accounts getCurrentAccounts() {

    /* 임시 설정 */
    Institutes institutes = institutesRepository.findById(1L)
        .orElseGet(()-> {
          Institutes newInstitutes = Institutes.builder()
              .name("test")
              .totalSpots(4)
              .build();

          return institutesRepository.save(newInstitutes);
        });

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

  public Institutes getCurrentInstitutes() {
    return institutesRepository.findById(1L)
        .orElseGet(()-> {
          Institutes newInstitutes = Institutes.builder()
              .name("test")
              .totalSpots(4)
              .build();

          return institutesRepository.save(newInstitutes);
        });
  }
}
