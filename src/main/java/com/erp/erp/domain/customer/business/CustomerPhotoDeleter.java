package com.erp.erp.domain.customer.business;

import com.erp.erp.domain.customer.common.entity.Customer;
import com.erp.erp.domain.customer.repository.CustomerPhotoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomerPhotoDeleter {

  private final CustomerPhotoRepository customerPhotoRepository;

  public void deleteByCustomer(Customer customer) {
    customerPhotoRepository.deleteByCustomer(customer);
  }
}
