package com.erp.erp.domain.reservation.repository;

import com.erp.erp.domain.institute.common.entity.Institute;
import com.erp.erp.domain.reservation.common.entity.QReservation;
import com.erp.erp.domain.reservation.common.entity.Reservation;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ReservationRepositoryImpl implements ReservationRepositoryCustom{

  private final JPAQueryFactory queryFactory;

  @Override
  public List<Reservation> findByInstituteAndStartDate(Institute institute, LocalDate date) {
    QReservation reservation = QReservation.reservation;

    return queryFactory
        .selectFrom(reservation)
        .where(
            reservation.startTime.between(date.atStartOfDay(), date.plusDays(1).atStartOfDay()),
            reservation.institute.eq(institute)
        )
        .fetch();
  }

  @Override
  public List<Reservation> findByInstituteAndTimeRange(Institute institute, LocalDateTime startTime, LocalDateTime endTime) {
    QReservation reservation = QReservation.reservation;

    return queryFactory
        .selectFrom(reservation)
        .where(
            reservation.institute.eq(institute),
            reservation.startTime.goe(startTime),
            reservation.startTime.lt(endTime)
        )
        .fetch();
  }

  @Override
  public List<Reservation> findByInstituteWithOverlappingTimeRange(Institute institute, LocalDateTime startTime, LocalDateTime endTime){
    QReservation reservation = QReservation.reservation;

    return queryFactory
        .selectFrom(reservation)
        .where(
            reservation.institute.eq(institute),
            reservation.startTime.lt(endTime),
            reservation.endTime.gt(startTime)
        )
        .fetch();
  };
}
