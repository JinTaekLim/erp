package com.erp.erp.domain.customer.business;

import com.erp.erp.domain.customer.common.entity.CustomerPhoto;
import com.erp.erp.domain.customer.repository.CustomerPhotoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomerPhotoCreator {

  private final CustomerPhotoRepository customerPhotoRepository;

  public void save(CustomerPhoto customerPhoto) {
    customerPhotoRepository.save(customerPhoto);
  }

}
