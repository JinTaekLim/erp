package com.erp.erp.domain.payments.business;

import com.erp.erp.domain.payments.common.entity.Payments;
import com.erp.erp.domain.payments.common.exception.NotFoundPaymentsException;
import com.erp.erp.domain.payments.repository.PaymentsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentsReader {

  private final PaymentsRepository paymentsRepository;

  public Payments findByCustomersId(Long customersId) {
    return paymentsRepository.findByCustomersId(customersId)
        .orElseThrow(NotFoundPaymentsException::new);
  }
}
