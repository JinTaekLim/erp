package com.erp.erp.domain.reservation.business;

import com.erp.erp.domain.reservation.common.dto.ReservationCache;
import com.erp.erp.domain.reservation.common.dto.UpdatedReservationDto;
import com.erp.erp.domain.reservation.common.entity.AttendanceStatus;
import com.erp.erp.domain.reservation.common.entity.Reservation;
import org.springframework.stereotype.Component;

@Component
public class ReservationCacheCalculator {

  public ReservationCache getNewReservationCache(ReservationCache reservationCache, Reservation oldReservation, UpdatedReservationDto.Request req) {
    double newUsedTime = getNewUsedTime(reservationCache.getUsedTime(), oldReservation.getEndIndex(), oldReservation.getStartIndex(), req.getEndIndex(), req.getStartIndex());
    int newLateCount = getNewLateCount(reservationCache.getLateCount(), oldReservation.getAttendanceStatus(), req.getAttendanceStatus());
    int newAbsenceCount = getNewLateAbsenceCount(reservationCache.getAbsenceCount(), oldReservation.getAttendanceStatus(), req.getAttendanceStatus());

    return reservationCache.update(oldReservation.getId(), newUsedTime, newLateCount, newAbsenceCount);
  }

  public double getNewUsedTime(double currentValue, int oldEndIndex, int oldStartIndex, int newEndIndex, int newStartIndex) {
    double usedTime = getUsedTime(newEndIndex, newStartIndex);
    double oldUsedTime = getUsedTime(oldEndIndex, oldStartIndex);
    return Math.max(0, currentValue - oldUsedTime) + usedTime;
  }

  public int getNewLateCount(int currentCount, AttendanceStatus oldAttendanceStatus, AttendanceStatus newAttendanceStatus) {
    int oldCount = getLateCount(oldAttendanceStatus);
    int newCount = getLateCount(newAttendanceStatus);
    return currentCount - oldCount + newCount;
  }

  public int getNewLateAbsenceCount(int currentCount, AttendanceStatus oldAttendanceStatus, AttendanceStatus newAttendanceStatus) {
    int oldCount = getAbsenceCount(oldAttendanceStatus);
    int newCount = getAbsenceCount(newAttendanceStatus);
    return currentCount - oldCount + newCount;
  }

  public double getUsedTime(int endIndex, int startIndex) {
    return (double) (endIndex - startIndex) / 2;
  }

  public int getLateCount(AttendanceStatus status) {
    return (status.equals(AttendanceStatus.LATE)) ? 1 : 0;
  }

  public int getAbsenceCount(AttendanceStatus status) {
    return (status.equals(AttendanceStatus.ABSENT)) ? 1 : 0;
  }
}
