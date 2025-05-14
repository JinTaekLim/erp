package com.erp.erp.domain.reservation.common.dto;

import com.erp.erp.domain.customer.common.entity.Progress;
import com.erp.erp.domain.reservation.common.entity.Reservation;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UpdateReservationMessageDto {
  private Reservation reservation;
  private ReservationCache reservationCache;
  private List<Progress> progress;
  private PendingReservationDto pendingReservation;
}
