package com.erp.erp.domain.institute.business;

import com.erp.erp.domain.institute.common.entity.Institute;
import com.erp.erp.domain.reservation.common.exception.InvalidSeatRangeException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class InstituteValidator {


//  public Customer validateCustomerBelongsToInstitute(Institute institute, Customer customer) {
//    Institute comparativeInstitute = customer.getInstitute();
//    if (institute != comparativeInstitute) throw new InstituteNotFoundInCustomerException();
//    return customer;
//  }
//  public Reservation validateReservationBelongsToInstitute(
//      Institute institute,
//      Reservation reservation
//  ) {
//    Institute comparativeInstitute = reservation.getInstitute();
//    if (institute != comparativeInstitute) throw new InstituteNotFoundInCustomerException();
//    return reservation;
//  }

  public void isValidSeatNumber(Institute institute, int seatNumber) {
    if (institute.getTotalSeat() < seatNumber) throw new InvalidSeatRangeException();
  }
}
