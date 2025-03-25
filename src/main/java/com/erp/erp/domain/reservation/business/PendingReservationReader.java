package com.erp.erp.domain.reservation.business;

import com.erp.erp.domain.reservation.common.dto.PendingReservationDto;
import com.erp.erp.domain.reservation.repository.PendingReservationRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PendingReservationReader {

  private final PendingReservationRepository pendingReservationRepository;

  public List<PendingReservationDto> findByInstituteId(Long instituteId) {
    return pendingReservationRepository.findByInstituteId(instituteId);
  }
}
