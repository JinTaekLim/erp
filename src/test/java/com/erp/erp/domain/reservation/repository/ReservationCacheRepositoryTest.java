package com.erp.erp.domain.reservation.repository;


import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.erp.erp.domain.reservation.common.dto.ReservationCache;
import com.erp.erp.global.test.IntegrationTest;
import com.erp.erp.global.util.randomValue.RandomValue;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class ReservationCacheRepositoryTest extends IntegrationTest {

  private final int MAX_SIZE = 20;
  @Autowired
  private ReservationCacheRepository reservationCacheRepository;

  private ReservationCache createReservationCache(Long instituteId) {
    ReservationCache reservationCache = fixtureMonkey.giveMeBuilder(ReservationCache.class)
        .set("instituteId", instituteId)
        .sample();
    reservationCacheRepository.save(reservationCache);
    return reservationCache;
  }

  @Test
  void save_and_findByInstituteId() {
    // given
    Long instituteId = RandomValue.getRandomLong(0,9999);
    int size = RandomValue.getInt(0,25);

    List<ReservationCache> reservationCaches = IntStream.range(0, size)
        .mapToObj(i -> createReservationCache(instituteId))
        .collect(Collectors.toCollection(ArrayList::new));

    // when
    List<ReservationCache> response = reservationCacheRepository.findByInstituteId(instituteId);


    // then
    assertThat(reservationCaches.size()).isEqualTo(size);

    Collections.reverse(reservationCaches);
    int resultSize = Math.min(size, MAX_SIZE);
    IntStream.range(0,resultSize).forEach(i-> {
      assertThat(reservationCaches.get(i).getInstituteId()).isEqualTo(response.get(i).getInstituteId());
      assertThat(reservationCaches.get(i).getReservationId()).isEqualTo(response.get(i).getReservationId());
      assertThat(reservationCaches.get(i).getCustomerId()).isEqualTo(response.get(i).getCustomerId());
      assertThat(reservationCaches.get(i).getUsedTime()).isEqualTo(response.get(i).getUsedTime());
      assertThat(reservationCaches.get(i).getLateCount()).isEqualTo(response.get(i).getLateCount());
      assertThat(reservationCaches.get(i).getAbsenceCount()).isEqualTo(response.get(i).getAbsenceCount());
    });
  }
}