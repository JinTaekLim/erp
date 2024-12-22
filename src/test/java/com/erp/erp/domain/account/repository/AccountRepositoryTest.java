package com.erp.erp.domain.account.repository;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.erp.erp.domain.account.common.entity.Account;
import com.erp.erp.domain.institutes.common.entity.Institutes;
import com.erp.erp.domain.institutes.repository.InstitutesRepository;
import com.erp.erp.global.util.test.JpaTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class AccountRepositoryTest extends JpaTest {

  @Autowired
  private InstitutesRepository institutesRepository;
  @Autowired
  private AccountRepository accountRepository;

  private Institutes getInstitutes() {
    return fixtureMonkey.giveMeBuilder(Institutes.class)
        .setNull("id")
        .sample();
  }
  private Institutes createInstitutes() {
    return institutesRepository.save(getInstitutes());
  }

  private Account getAccount(Institutes institutes) {
    return fixtureMonkey.giveMeBuilder(Account.class)
        .set("institutes", institutes)
        .sample();
  }

  private Account createAccount(Institutes institutes) {
    return accountRepository.save(getAccount(institutes));
  }



  @Test
  void findInstitutesByAccountId() {
    // given
    Institutes institutes = createInstitutes();
    Account account = createAccount(institutes);

    // when
    Institutes getInstitutes = accountRepository.findInstitutesByAccountId(account.getId())
        .orElseThrow(AssertionError::new);

    // then
    assertThat(institutes).usingRecursiveComparison().isEqualTo(getInstitutes);
  }
}