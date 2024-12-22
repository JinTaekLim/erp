package com.erp.erp.domain.account.repository;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.erp.erp.domain.account.common.entity.Account;
import com.erp.erp.domain.institute.common.entity.Institute;
import com.erp.erp.domain.institute.repository.InstituteRepository;
import com.erp.erp.global.util.test.JpaTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class AccountRepositoryTest extends JpaTest {

  @Autowired
  private InstituteRepository instituteRepository;
  @Autowired
  private AccountRepository accountRepository;

  private Institute getInstitutes() {
    return fixtureMonkey.giveMeBuilder(Institute.class)
        .setNull("id")
        .sample();
  }
  private Institute createInstitutes() {
    return instituteRepository.save(getInstitutes());
  }

  private Account getAccount(Institute institute) {
    return fixtureMonkey.giveMeBuilder(Account.class)
        .set("institute", institute)
        .sample();
  }

  private Account createAccount(Institute institute) {
    return accountRepository.save(getAccount(institute));
  }



  @Test
  void findInstitutesByAccountId() {
    // given
    Institute institute = createInstitutes();
    Account account = createAccount(institute);

    // when
    Institute getInstitute = accountRepository.findInstitutesByAccountId(account.getId())
        .orElseThrow(AssertionError::new);

    // then
    assertThat(institute).usingRecursiveComparison().isEqualTo(getInstitute);
  }
}