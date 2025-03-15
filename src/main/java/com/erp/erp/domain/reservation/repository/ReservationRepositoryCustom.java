package com.erp.erp.domain.reservation.repository;

import com.erp.erp.domain.institute.common.entity.Institute;
import com.erp.erp.domain.reservation.common.entity.Reservation;
import java.time.LocalDate;
import java.util.List;

public interface ReservationRepositoryCustom {

  List<Reservation> findByInstituteAndStartDate(Institute institute, LocalDate date);

//  List<Reservation> findByInstituteAndTimeRange(Institute institute, LocalDate day, int startTime, int endTime);

  List<Reservation> findReservationsWithinTimeRange(Institute institute, LocalDate day, int startIndex, int endIndex);


}
