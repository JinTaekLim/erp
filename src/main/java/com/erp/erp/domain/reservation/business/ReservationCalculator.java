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

  public int getRemainingPeriod(int availablePeriod, LocalDate expiredAt, LocalDate registrationAt) {
    // 사용 시작 날짜
    LocalDate startUsePeriod = (expiredAt != null) ? expiredAt : registrationAt;
    // 사용한 기간
    int usedPeriodInDays = (int) ChronoUnit.DAYS.between(startUsePeriod, LocalDate.now());

    // 남은 기간
    return availablePeriod - usedPeriodInDays;
  }

  public double getRemainingTime(int availableTime, double usedTime) {
    return availableTime - usedTime;
  }

}
