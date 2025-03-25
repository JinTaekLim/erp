package com.erp.erp.global.util.config.fixtureMonkey;

import com.navercorp.fixturemonkey.api.jqwik.JavaTimeTypeArbitraryGenerator;
import java.time.LocalDateTime;
import net.jqwik.time.api.DateTimes;
import net.jqwik.time.api.arbitraries.LocalDateTimeArbitrary;

public class LocalDateTimeJqwikPlugin implements
    JavaTimeTypeArbitraryGenerator {

  @Override
  public LocalDateTimeArbitrary localDateTimes() {
    LocalDateTime now = LocalDateTime.now().withNano(0);
    return DateTimes.dateTimes().between(now.minusDays(365L), now.plusDays(365L));
  }
}
