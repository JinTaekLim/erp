package com.erp.erp.domain.reservation.common.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class PushEventUpdateReservationMessage {

  private final Long instituteId;
  private final String id;
  private final String name;
  private final String data;
  private final long reconnectTime;
  private final String comment;


  @Builder
  public PushEventUpdateReservationMessage(Long instituteId, String id, String name, long reconnectTime, String comment) {
    this.instituteId = instituteId;
    this.id = id;
    this.name = name;
    this.data = "UPDATE_RESERVATION";
    this.reconnectTime = reconnectTime;
    this.comment = comment;
  }

}
