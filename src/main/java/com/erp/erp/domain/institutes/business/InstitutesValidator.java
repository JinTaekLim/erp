package com.erp.erp.domain.institutes.business;

import com.erp.erp.domain.customers.common.entity.Customers;
import com.erp.erp.domain.institutes.common.entity.Institutes;
import com.erp.erp.domain.institutes.common.exception.InstituteNotFoundInCustomerException;
import com.erp.erp.domain.payments.common.entity.Payments;
import com.erp.erp.domain.reservations.common.entity.Reservations;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class InstitutesValidator {


  public Customers validateCustomerBelongsToInstitute(Institutes institutes , Customers customers) {
    Institutes comparativeInstitutes = customers.getInstitutes();
    if (institutes != comparativeInstitutes) throw new InstituteNotFoundInCustomerException();
    return customers;
  }

  public Payments validatePaymentBelongsToInstitute(Institutes institutes , Payments payments) {
    Institutes comparativeInstitutes = payments.getCustomers().getInstitutes();
    if (institutes != comparativeInstitutes) throw new InstituteNotFoundInCustomerException();
    return payments;
  }

  public Reservations validateReservationBelongsToInstitute(
      Institutes institutes,
      Reservations reservations
  ) {
    Institutes comparativeInstitutes = reservations.getInstitutes();
    if (institutes != comparativeInstitutes) throw new InstituteNotFoundInCustomerException();
    return reservations;
  }
}
