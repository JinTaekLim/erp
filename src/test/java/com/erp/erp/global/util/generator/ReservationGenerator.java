package com.erp.erp.global.util.generator;

import com.erp.erp.domain.customer.common.entity.Customer;
import com.erp.erp.domain.institute.common.entity.Institute;
import com.erp.erp.domain.reservation.common.entity.Reservation;
import com.erp.erp.global.util.randomValue.RandomValue;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;

public class ReservationGenerator extends EntityGenerator{

  public static Reservation get(Customer customer, Institute institute,
      LocalDate day) {

    int openIndex = convertToTimeIndex(institute.getOpenTime());
    int closeIndex = convertToTimeIndex(institute.getCloseTime());
    int startIndex = getStartIndex(openIndex, closeIndex);
    int endIndex = getEndIndex(startIndex, closeIndex);
    int seatNumber = RandomValue.getInt(1, institute.getTotalSeat());

    return Reservation.builder()
        .customer(customer)
        .institute(institute)
        .reservationDate(day)
        .startIndex(startIndex)
        .endIndex(endIndex)
        .seatNumber(seatNumber)
        .build();
  }

  public static Reservation get(Customer customer, Institute institute) {
    LocalDate day = RandomValue.getRandomLocalDate();

    int openIndex = convertToTimeIndex(institute.getOpenTime());
    int closeIndex = convertToTimeIndex(institute.getCloseTime());
    int startIndex = getStartIndex(openIndex, closeIndex);
    int endIndex = getEndIndex(startIndex, closeIndex);
    int seatNumber = RandomValue.getInt(1, institute.getTotalSeat());


    return Reservation.builder()
        .customer(customer)
        .institute(institute)
        .reservationDate(day)
        .startIndex(startIndex)
        .endIndex(endIndex)
        .seatNumber(seatNumber)
        .build();
  }

  public static Reservation get(Customer customer, Institute institute,
      LocalDate day, int startIndex, int endIndex) {

    int seatNumber = RandomValue.getInt(1, institute.getTotalSeat());

    return Reservation.builder()
        .customer(customer)
        .institute(institute)
        .reservationDate(day)
        .startIndex(startIndex)
        .endIndex(endIndex)
        .seatNumber(seatNumber)
        .build();
  }

  public static int convertToTimeIndex(LocalTime time) {
    return (int) Duration.between(LocalTime.MIDNIGHT, time).toMinutes() / 30;
  }

  public static int getStartIndex(int openIndex, int closeIndex) {
    if (openIndex+1 == closeIndex) { return openIndex; }
    return RandomValue.getInt(openIndex, closeIndex);
  }

  public static int getEndIndex(int startIndex, int closeIndex) {
    startIndex ++;
    if (startIndex == closeIndex) { return closeIndex; }
    return RandomValue.getInt(startIndex, closeIndex);
  }
}
