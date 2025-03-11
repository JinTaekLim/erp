package com.erp.erp.domain.institute.business;

import com.erp.erp.domain.institute.common.entity.Institute;
import com.erp.erp.domain.reservation.common.exception.InvalidSeatRangeException;
import com.erp.erp.domain.reservation.common.exception.OutsideHoursException;
import java.time.Duration;
import java.time.LocalTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class InstituteValidator {

  public void validateOperatingHours(Institute institute, int startIndex, int endIndex) {
    int openIndex = convertToTimeIndex(institute.getOpenTime());
    int closeIndex = convertToTimeIndex(institute.getCloseTime());

    if (startIndex < openIndex || endIndex > closeIndex) {
      throw new OutsideHoursException();
    }
  }

  private int convertToTimeIndex(LocalTime time) {
    long minutesFromMidnight = Duration.between(LocalTime.MIDNIGHT, time).toMinutes();
    return (int) minutesFromMidnight / 30;
  }


  public void isValidSeatNumber(Institute institute, int seatNumber) {
    if (institute.getTotalSeat() < seatNumber || seatNumber == 0) throw new InvalidSeatRangeException();
  }
}
