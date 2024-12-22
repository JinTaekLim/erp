package com.erp.erp.domain.reservations.business;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import com.erp.erp.domain.institute.common.entity.Institute;
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

class ReservationsValidatorTest extends ServiceTest {

  @InjectMocks
  private ReservationsValidator reservationsValidator;

  @Mock
  private ReservationsRepository reservationsRepository;
  @Test
  void isTimeSlotAvailable_성공() {
    // given
    int randomSpots = RandomValue.getInt(1, 8);
    Institute institute = Institute.builder()
        .totalSeat(randomSpots)
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
    when(reservationsRepository.findByInstitutesAndTimeRange(institute, startTime, endTime))
        .thenReturn(reservationsList);


    // then
    reservationsValidator.isTimeSlotAvailable(institute,startTime,endTime);
  }

  @Test
  void isTimeSlotAvailable_실패() {
    // given
    int randomSpots = RandomValue.getInt(1, 8);
    Institute institute = Institute.builder()
        .totalSeat(randomSpots)
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
    when(reservationsRepository.findByInstitutesAndTimeRange(institute, startTime, endTime))
        .thenReturn(reservationsList);


    // then
    assertThrows(
        NoAvailableSpotsException.class,
        () -> reservationsValidator.isTimeSlotAvailable(institute, startTime, endTime)
    );
  }

  @Test
  void calculate30MinSlots_성공() {
    // given
    int randomInt = RandomValue.getInt(2, 10);
    LocalDateTime startTime = LocalDateTime.now();
    LocalDateTime endTime = startTime.plusMinutes(30L * randomInt);

    // when && then
    assertDoesNotThrow(() -> reservationsValidator.calculate30MinSlots(startTime, endTime));
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
        () -> reservationsValidator.calculate30MinSlots(startTime, endTime)
    );
  }
  @Test
  void getMinutesBetween_성공() {
    // given
    int randomInt = RandomValue.getInt(1, 10);
    LocalDateTime startTime = LocalDateTime.now();
    LocalDateTime endTime = startTime.plusMinutes(30L * randomInt);

    // when && then
    assertDoesNotThrow(() -> reservationsValidator.getMinutesBetween(startTime, endTime));
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
        () -> reservationsValidator.getMinutesBetween(startTime, endTime)
    );
  }

}