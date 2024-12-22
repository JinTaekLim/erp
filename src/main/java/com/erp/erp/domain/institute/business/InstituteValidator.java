package com.erp.erp.domain.institute.business;

import com.erp.erp.domain.customer.common.entity.Customer;
import com.erp.erp.domain.institute.common.entity.Institute;
import com.erp.erp.domain.institute.common.exception.InstituteNotFoundInCustomerException;
import com.erp.erp.domain.reservations.common.entity.Reservations;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class InstituteValidator {


  public Customer validateCustomerBelongsToInstitute(Institute institute, Customer customer) {
    Institute comparativeInstitute = customer.getInstitute();
    if (institute != comparativeInstitute) throw new InstituteNotFoundInCustomerException();
    return customer;
  }

  public Reservations validateReservationBelongsToInstitute(
      Institute institute,
      Reservations reservations
  ) {
    Institute comparativeInstitute = reservations.getInstitute();
    if (institute != comparativeInstitute) throw new InstituteNotFoundInCustomerException();
    return reservations;
  }
}
