package com.erp.erp.domain.customer.business;

import com.erp.erp.domain.customer.common.entity.Customer;
import com.erp.erp.domain.customer.common.entity.CustomerPhoto;
import com.erp.erp.domain.customer.repository.CustomerPhotoRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomerPhotoReader {

  private final CustomerPhotoRepository customerPhotoRepository;

  public Optional<CustomerPhoto> findByCustomer(Customer customer) {
    return customerPhotoRepository.findByCustomer(customer);
  }

  public List<CustomerPhoto> findAll() {
    return customerPhotoRepository.findAll();
  }
}
