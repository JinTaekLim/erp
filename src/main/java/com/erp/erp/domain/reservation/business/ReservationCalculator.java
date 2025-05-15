package com.erp.erp.domain.reservation.business;

public class ReservationCalculator {

  public double getUsedTime(int startIndex, int endIndex) {
    return (double) (endIndex - startIndex) / 2;
  }
}
