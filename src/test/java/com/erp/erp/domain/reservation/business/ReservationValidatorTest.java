package com.erp.erp.domain.reservation.business;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import com.erp.erp.domain.institute.common.entity.Institute;
import com.erp.erp.domain.reservation.common.entity.Reservation;
import com.erp.erp.domain.reservation.common.exception.InvalidReservationTimeException;
import com.erp.erp.domain.reservation.common.exception.NoAvailableSeatException;
import com.erp.erp.domain.reservation.repository.ReservationRepository;
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
  private ReservationRepository reservationRepository;
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

    List<Reservation> reservationList = new ArrayList<>();

    for(int i=0; i< randomSpots ; i++) {
      Reservation reservation = Reservation.builder()
          .startTime(startTime)
          .endTime(endTime)
          .build();
      reservationList.add(reservation);
    }

    // when
    when(reservationRepository.findByInstituteAndTimeRange(institute, startTime, endTime))
        .thenReturn(reservationList);


    // then
    reservationValidator.isTimeSlotAvailable(institute,startTime,endTime);
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

    List<Reservation> reservationList = new ArrayList<>();

    for(int i=0; i< randomSpots+1 ; i++) {
      Reservation reservation = Reservation.builder()
          .startTime(startTime)
          .endTime(endTime)
          .build();
      reservationList.add(reservation);
    }


    // when
    when(reservationRepository.findByInstituteAndTimeRange(institute, startTime, endTime))
        .thenReturn(reservationList);


    // then
    assertThrows(
        NoAvailableSeatException.class,
        () -> reservationValidator.isTimeSlotAvailable(institute, startTime, endTime)
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