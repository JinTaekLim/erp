package com.erp.erp.global.util.generator;


import com.erp.erp.domain.institute.common.entity.Institute;
import com.erp.erp.global.util.randomValue.RandomValue;
import java.time.LocalTime;

public class InstituteGenerator extends EntityGenerator{

  public static Institute get() {
    LocalTime openTime = getOpenTime();
    return fixtureMonkey.giveMeBuilder(Institute.class)
        .setNull("id")
        .set("openTime", openTime)
        .set("closeTime", getCloseTime())
        .sample();
  }

  public static Institute get(int totalSeat) {
    LocalTime openTime = RandomValue.getRandomLocalTime();
    return fixtureMonkey.giveMeBuilder(Institute.class)
        .setNull("id")
        .set("totalSeat", totalSeat)
        .set("openTime", openTime)
        .set("closeTime", getCloseTime())
        .sample();
  }

  private static LocalTime getOpenTime() {
    int hour = RandomValue.getInt(0,12);
    int minute = 30 * RandomValue.getInt(0,1);
    return LocalTime.of(hour, minute);
  }
  private static LocalTime getCloseTime() {
    int hour = RandomValue.getInt(13,23);
    int minute = 30 * RandomValue.getInt(0,1);
    return LocalTime.of(hour, minute);
  }

}
