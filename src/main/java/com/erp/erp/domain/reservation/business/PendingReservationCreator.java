package com.erp.erp.domain.reservation.business;

import com.erp.erp.domain.reservation.common.dto.PendingReservationDto;
import com.erp.erp.domain.reservation.repository.PendingReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PendingReservationCreator {

  private final PendingReservationRepository pendingReservationRepository;

  public void save(Long instituteId, PendingReservationDto dto) {
    pendingReservationRepository.save(instituteId, dto);
  }
}
