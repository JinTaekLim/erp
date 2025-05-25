package com.erp.erp.domain.reservation.business;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class PushUpdateReservationEvent {

  private Long instituteId;
  private String id;
  private String name;
  private long reconnectTime;
  private String comment;

}
