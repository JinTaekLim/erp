package com.erp.erp.global.util;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class TimeUtil {

  public static LocalDateTime roundToNearestHalfHour(LocalDateTime dateTime) {
    LocalDateTime rounded = dateTime.truncatedTo(ChronoUnit.HOURS);
    int minutes = dateTime.getMinute();

    if (minutes < 15) { return rounded; }
    else if (minutes < 45) { return rounded.plusMinutes(30); }
    else { return rounded.plusHours(1);}
  }

  public static double calculateHalfHours(LocalDateTime startTime, LocalDateTime endTime) {
    return Math.round(Duration.between(startTime, endTime).toMinutes() / 30.0) / 2.0;
  }

  public static int daysBetween(LocalDate date) {
    LocalDate today = LocalDate.now();
    return (int) ChronoUnit.DAYS.between(today, date);
  }
}
