package com.erp.erp.global.util;

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
}
