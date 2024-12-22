package com.erp.erp.domain.accounts.repository;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.erp.erp.domain.accounts.common.entity.Accounts;
import com.erp.erp.domain.institutes.common.entity.Institutes;
import com.erp.erp.domain.institutes.repository.InstitutesRepository;
import com.erp.erp.global.util.test.JpaTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class AccountsRepositoryTest extends JpaTest {

  @Autowired
  private InstitutesRepository institutesRepository;
  @Autowired
  private AccountsRepository accountsRepository;

  private Institutes getInstitutes() {
    return fixtureMonkey.giveMeBuilder(Institutes.class)
        .setNull("id")
        .sample();
  }
  private Institutes createInstitutes() {
    return institutesRepository.save(getInstitutes());
  }

  private Accounts getAccount(Institutes institutes) {
    return fixtureMonkey.giveMeBuilder(Accounts.class)
        .set("institutes", institutes)
        .sample();
  }

  private Accounts createAccount(Institutes institutes) {
    return accountsRepository.save(getAccount(institutes));
  }



  @Test
  void findInstitutesByAccountId() {
    // given
    Institutes institutes = createInstitutes();
    Accounts accounts = createAccount(institutes);

    // when
    Institutes getInstitutes = accountsRepository.findInstitutesByAccountId(accounts.getId())
        .orElseThrow(AssertionError::new);

    // then
    assertThat(institutes).usingRecursiveComparison().isEqualTo(getInstitutes);
  }
}