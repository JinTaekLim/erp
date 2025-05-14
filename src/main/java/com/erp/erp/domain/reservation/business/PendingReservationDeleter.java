package com.erp.erp.domain.reservation.business;

import com.erp.erp.domain.reservation.common.dto.PendingReservationDto;
import com.erp.erp.domain.reservation.repository.PendingReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PendingReservationDeleter {

  private final PendingReservationRepository pendingReservationRepository;

  public void delete(Long instituteId, PendingReservationDto dto) {
    pendingReservationRepository.delete(instituteId, dto);
  }
}
