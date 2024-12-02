package com.erp.erp.domain.customers.business;

import com.erp.erp.domain.customers.common.entity.Customers;
import com.erp.erp.domain.customers.common.entity.Gender;
import com.erp.erp.domain.customers.repository.CustomersRepository;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class CustomersUpdater {

  private final CustomersRepository customersRepository;


  public void updateStatus(Long customersId, boolean status) {
    customersRepository.updateStatusById(customersId, status);
  }

  public Customers updatedCustomers(
      Customers customers,
      String name,
      Gender gender,
      String phone,
      String address,
      String photoUrl,
      String memo,
      LocalDate birthDate
  ) {
    return customers.update(name,gender,phone,address,photoUrl,memo,birthDate);
  }

}
