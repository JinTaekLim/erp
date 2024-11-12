package com.erp.erp.domain.payments.service;

import com.erp.erp.domain.auth.business.AuthProvider;
import com.erp.erp.domain.institutes.business.InstitutesValidator;
import com.erp.erp.domain.institutes.common.entity.Institutes;
import com.erp.erp.domain.payments.business.PaymentsReader;
import com.erp.erp.domain.payments.common.entity.Payments;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentsService {

  private final AuthProvider authProvider;
  private final PaymentsReader paymentsReader;
  private final InstitutesValidator institutesValidator;

  public Payments getCustomersPayments(Long customersId) {
    Institutes institutes = authProvider.getCurrentInstitutes();
    Payments payments = paymentsReader.findByCustomersId(customersId);
    return institutesValidator.validatePaymentBelongsToInstitute(institutes, payments);
  }
}
