package com.erp.erp.domain.payments.business;

import com.erp.erp.domain.payments.common.entity.Payments;
import com.erp.erp.domain.payments.repository.PaymentsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentsCreator {

  private final PaymentsRepository paymentsRepository;

  public Payments save(Payments payments) {
    return paymentsRepository.save(payments);
  }
}
