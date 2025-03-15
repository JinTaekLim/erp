package com.erp.erp.domain.reservation.repository;

import com.erp.erp.domain.institute.common.entity.Institute;
import com.erp.erp.domain.reservation.common.entity.QReservation;
import com.erp.erp.domain.reservation.common.entity.Reservation;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDate;
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
            reservation.reservationDate.eq(date),
            reservation.institute.eq(institute)
        )
        .fetch();
  }
//
//  @Override
//  public List<Reservation> findByInstituteAndTimeRange(Institute institute, LocalDate day, int startTime, int endTime) {
//    QReservation reservation = QReservation.reservation;
//
//    return queryFactory
//        .selectFrom(reservation)
//        .where(
//            reservation.institute.eq(institute),
//            reservation.reservationDate.eq(day),
//            reservation.startIndex.goe(startTime),
//            reservation.startIndex.loe(endTime)
//        )
//        .fetch();
//  }

  @Override
  public List<Reservation> findReservationsWithinTimeRange(Institute institute, LocalDate day, int startIndex, int endIndex) {
    QReservation reservation = QReservation.reservation;

    return queryFactory
        .selectFrom(reservation)
        .where(
            reservation.institute.eq(institute),
            reservation.reservationDate.eq(day),
            reservation.startIndex.goe(startIndex)
                .and(reservation.endIndex.loe(endIndex))
        )
        .fetch();
  }
}
