package com.erp.erp.domain.reservations.business;

import com.erp.erp.domain.institutes.common.entity.Institutes;
import com.erp.erp.domain.reservations.common.entity.Reservations;
import com.erp.erp.domain.reservations.common.exception.NoAvailableSpotsException;
import com.erp.erp.domain.reservations.repository.ReservationsRepository;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ReservationValidator {

  private final ReservationsRepository reservationsRepository;

  public void isTimeSlotAvailable(
      Institutes institutes,
      LocalDateTime startTime,
      LocalDateTime endTime
  ) {
    List<Reservations> reservationsList = reservationsRepository.findByInstitutesAndTimeRange(
        institutes,
        startTime,
        endTime
    );
    int totalSpots = institutes.getTotalSpots();
    int reservationCount = reservationsList.size();
    if (totalSpots <= reservationCount) {throw new NoAvailableSpotsException();}
  }

}
