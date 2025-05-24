package com.erp.erp.domain.reservation.business;

import com.erp.erp.domain.reservation.common.entity.AttendanceStatus;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class ReservationCalculator {

  public double getUsedTime(int startIndex, int endIndex) {
    return (double) (endIndex - startIndex) / 2;
  }

  public int getLateCount(AttendanceStatus attendanceStatus) {
    return attendanceStatus == AttendanceStatus.LATE ? 1 : 0;
  }

  public int getAbsenceCount(AttendanceStatus attendanceStatus) {
    return attendanceStatus == AttendanceStatus.ABSENT ? 1 : 0;
  }

  public int getRemainingPeriod(LocalDate expiredAt, LocalDate registrationAt) {
    LocalDate now = LocalDate.now();
    return (int) ((expiredAt != null) ?
            ChronoUnit.DAYS.between(expiredAt, now) :
            ChronoUnit.DAYS.between(registrationAt, now));
  }

  public double getRemainingTime(int availableTime, double usedTime) {
    return availableTime - usedTime;
  }

}
