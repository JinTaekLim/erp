package com.erp.erp.domain.reservations.business;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import com.erp.erp.domain.institutes.common.entity.Institutes;
import com.erp.erp.domain.reservations.common.entity.Reservations;
import com.erp.erp.domain.reservations.common.exception.InvalidReservationTimeException;
import com.erp.erp.domain.reservations.common.exception.NoAvailableSpotsException;
import com.erp.erp.domain.reservations.repository.ReservationsRepository;
import com.erp.erp.global.util.randomValue.RandomValue;
import com.erp.erp.global.util.test.ServiceTest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

class ReservationValidatorTest extends ServiceTest {

  @InjectMocks
  private ReservationValidator reservationValidator;

  @Mock
  private ReservationsRepository reservationsRepository;
  @Test
  void isTimeSlotAvailable_성공() {
    // given
    int randomSpots = RandomValue.getInt(1, 8);
    Institutes institutes = Institutes.builder()
        .totalSpots(randomSpots)
        .build();

    int randomInt = RandomValue.getInt(2, 10);
    LocalDateTime startTime = LocalDateTime.now();
    LocalDateTime endTime = startTime.plusMinutes(30L * randomInt);

    List<Reservations> reservationsList = new ArrayList<>();

    for(int i=0; i< randomSpots ; i++) {
      Reservations reservations = Reservations.builder()
          .startTime(startTime)
          .endTime(endTime)
          .build();
      reservationsList.add(reservations);
    }

    // when
    when(reservationsRepository.findByInstitutesAndTimeRange(institutes, startTime, endTime))
        .thenReturn(reservationsList);


    // then
    reservationValidator.isTimeSlotAvailable(institutes,startTime,endTime);
  }

  @Test
  void isTimeSlotAvailable_실패() {
    // given
    int randomSpots = RandomValue.getInt(1, 8);
    Institutes institutes = Institutes.builder()
        .totalSpots(randomSpots)
        .build();

    int randomInt = RandomValue.getInt(2, 10);
    LocalDateTime startTime = LocalDateTime.now();
    LocalDateTime endTime = startTime.plusMinutes(30L * randomInt);

    List<Reservations> reservationsList = new ArrayList<>();

    for(int i=0; i< randomSpots+1 ; i++) {
      Reservations reservations = Reservations.builder()
          .startTime(startTime)
          .endTime(endTime)
          .build();
      reservationsList.add(reservations);
    }


    // when
    when(reservationsRepository.findByInstitutesAndTimeRange(institutes, startTime, endTime))
        .thenReturn(reservationsList);


    // then
    assertThrows(
        NoAvailableSpotsException.class,
        () -> reservationValidator.isTimeSlotAvailable(institutes, startTime, endTime)
    );
  }

  @Test
  void calculate30MinSlots_성공() {
    // given
    int randomInt = RandomValue.getInt(2, 10);
    LocalDateTime startTime = LocalDateTime.now();
    LocalDateTime endTime = startTime.plusMinutes(30L * randomInt);

    // when && then
    assertDoesNotThrow(() -> reservationValidator.calculate30MinSlots(startTime, endTime));
  }

  @Test
  void calculate30MinSlots_실패() {
    // given
    int randomInt = RandomValue.getInt(0, 10);
    LocalDateTime startTime = LocalDateTime.now();
    LocalDateTime endTime = startTime.minusMinutes(30L * randomInt);

    // when && then
    assertThrows(
        InvalidReservationTimeException.class,
        () -> reservationValidator.calculate30MinSlots(startTime, endTime)
    );
  }
  @Test
  void getMinutesBetween_성공() {
    // given
    int randomInt = RandomValue.getInt(1, 10);
    LocalDateTime startTime = LocalDateTime.now();
    LocalDateTime endTime = startTime.plusMinutes(30L * randomInt);

    // when && then
    assertDoesNotThrow(() -> reservationValidator.getMinutesBetween(startTime, endTime));
  }

  @Test
  void getMinutesBetween_실패() {
    // given
    int randomInt = RandomValue.getInt(0, 10);
    LocalDateTime startTime = LocalDateTime.now();
    LocalDateTime endTime = startTime.minusMinutes(30L * randomInt);

    // when && then
    assertThrows(
        InvalidReservationTimeException.class,
        () -> reservationValidator.getMinutesBetween(startTime, endTime)
    );
  }

}