package com.erp.erp.domain.institutes.business;

import com.erp.erp.domain.customers.common.entity.Customers;
import com.erp.erp.domain.institutes.common.entity.Institutes;
import com.erp.erp.domain.institutes.common.exception.InstituteNotFoundInCustomerException;
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

}
