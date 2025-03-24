package com.erp.erp.domain.institute.business;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.erp.erp.global.test.IntegrationTest;
import com.erp.erp.global.util.randomValue.RandomValue;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class InstituteLockTest extends IntegrationTest {

  @Autowired
  private InstituteLock instituteLock;

  @Test
  void getLock() throws InterruptedException {
    // given
    Long id = RandomValue.getRandomLong(9999);

    // when && then
    boolean status = instituteLock.getLock(id);

    // then
    assertThat(status).isEqualTo(true);
  }
}