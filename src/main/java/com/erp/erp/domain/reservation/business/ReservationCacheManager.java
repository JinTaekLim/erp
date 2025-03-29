package com.erp.erp.domain.reservation.business;

import com.erp.erp.domain.customer.business.CustomerReader;
import com.erp.erp.domain.customer.common.dto.GetCustomerDto;
import com.erp.erp.domain.customer.common.entity.Customer;
import com.erp.erp.domain.reservation.common.dto.ReservationCache;
import com.erp.erp.domain.reservation.common.entity.Reservation;
import com.erp.erp.domain.reservation.common.mapper.ReservationCacheMapper;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReservationCacheManager {

  private final ReservationCacheCreator reservationCacheCreator;
  private final ReservationCacheReader reservationCacheReader;
  private final ReservationCacheUpdater reservationCacheUpdater;
  private final CustomerReader customerReader;
  private final ReservationCacheMapper reservationCacheMapper;
  private final ReservationCacheCalculator calculator;

  public List<GetCustomerDto.Response> getCustomers(Long instituteId) {
    List<ReservationCache> reservationCaches = findByInstituteId(instituteId);
    return customerReader.findByReservationCache(reservationCaches);
  }

  public List<ReservationCache> findByInstituteId(Long instituteId) {
    return reservationCacheReader.findByInstituteId(instituteId);
  }

  public void save(Customer customer) {
    ReservationCache reservationCache = reservationCacheMapper.customerToEntity(customer);
    reservationCacheCreator.save(reservationCache);
  }

  private ReservationCache findByCustomerId(Reservation reservation) {
    Long instituteId = reservation.getInstitute().getId();
    Long customerId = reservation.getCustomer().getId();
    return reservationCacheReader.findByCustomerId(instituteId, customerId);
  }

  public void updateCustomerReservation(Reservation reservation) {
    ReservationCache reservationCache = findByCustomerId(reservation);

    if (reservationCache == null) return;

    double usedTime = reservationCache.getUsedTime() + calculator.getUsedTime(reservation.getEndIndex(), reservation.getStartIndex());
    int lateCount = reservationCache.getLateCount() + calculator.getLateCount(reservation.getAttendanceStatus());
    int absenceCount = reservationCache.getAbsenceCount() + calculator.getAbsenceCount(reservation.getAttendanceStatus());

    reservationCache.update(reservation.getId(), usedTime, lateCount, absenceCount);

    reservationCacheUpdater.updateCustomerReservation(reservationCache);
  }

//  public void update(Reservation oldReservation, UpdatedReservationDto.Request req) {
//    ReservationCache reservationCache = findByCustomerId(oldReservation);
//    if (reservationCache == null) return;
//    ReservationCache newReservationCache = calculator.getNewReservationCache(reservationCache, oldReservation, req);
//    reservationCacheUpdater.update(newReservationCache);
//  }

  public void updateAllInstituteCache(Long instituteId, List<ReservationCache> reservationCaches) {
    reservationCacheUpdater.updateAllInstituteCache(instituteId, reservationCaches);
  }

  public void updateCustomerReservation(ReservationCache reservationCache) {
    reservationCacheUpdater.updateCustomerReservation(reservationCache);
  }


  public ReservationCache getNewReservationCache(Reservation reservation) {
    ReservationCache reservationCache = findByCustomerId(reservation);

    if (reservationCache == null) return new ReservationCache();

    double usedTime = reservationCache.getUsedTime() + calculator.getUsedTime(reservation.getEndIndex(), reservation.getStartIndex());
    int lateCount = reservationCache.getLateCount() + calculator.getLateCount(reservation.getAttendanceStatus());
    int absenceCount = reservationCache.getAbsenceCount() + calculator.getAbsenceCount(reservation.getAttendanceStatus());

    reservationCache.update(reservation.getId(), usedTime, lateCount, absenceCount);

    return reservationCache;
  }
}
