package com.erp.erp.domain.customer.business;

import com.erp.erp.domain.customer.common.dto.UpdateCustomerDto;
import com.erp.erp.domain.customer.common.dto.UpdateCustomerDto.OtherPaymentResponse;
import com.erp.erp.domain.customer.common.entity.Customer;
import com.erp.erp.domain.customer.common.entity.CustomerStatus;
import com.erp.erp.domain.customer.repository.CustomerRepository;
import com.erp.erp.domain.payment.common.entity.OtherPayment;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class CustomerUpdater {

  private final CustomerRepository customerRepository;


  public void updateStatus(Long customersId, CustomerStatus status, String updatedId) {
    customerRepository.updateStatusById(customersId, status, updatedId);
  }

  public Customer updateCustomer(UpdateCustomerDto.Request req, String photoUrl, Customer customer, String updatedId) {
    customer.update(
        req.getName(),
        req.getGender(),
        req.getPhone(),
        req.getAddress(),
        req.getVisitPath(),
        photoUrl,
        req.getMemo(),
        req.getBirthDate(),
        req.isPlanPaymentStatus(),
        toOtherPaymentsList(req.getOtherPayment()),
        updatedId
    );

    return customerRepository.save(customer);
  }

  private List<OtherPayment> toOtherPaymentsList(List<OtherPaymentResponse> otherPayments) {
    return otherPayments.stream()
        .map(otherPayment -> OtherPayment.builder()
            .paymentsMethod(otherPayment.getPaymentsMethod())
            .otherPaymentMethod(otherPayment.getOtherPaymentMethod())
            .content(otherPayment.getContent())
            .status(otherPayment.isStatus())
            .registrationAt(otherPayment.getRegistrationAt())
            .price(otherPayment.getPrice())
            .build())
        .toList();
  }
}
