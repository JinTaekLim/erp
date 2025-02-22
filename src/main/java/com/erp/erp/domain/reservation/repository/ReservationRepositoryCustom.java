package com.erp.erp.domain.reservation.repository;

import com.erp.erp.domain.institute.common.entity.Institute;
import com.erp.erp.domain.reservation.common.entity.Reservation;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface ReservationRepositoryCustom {

  List<Reservation> findByInstituteAndStartDate(Institute institute, LocalDate date);

  List<Reservation> findByInstituteAndTimeRange(Institute institute, LocalDateTime startTime, LocalDateTime endTime);

  List<Reservation> findByInstituteWithOverlappingTimeRange(Institute institute, LocalDateTime startTime, LocalDateTime endTime);

}
