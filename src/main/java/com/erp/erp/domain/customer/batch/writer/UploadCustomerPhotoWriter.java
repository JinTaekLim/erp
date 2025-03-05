package com.erp.erp.domain.customer.batch.writer;

import com.erp.erp.domain.customer.business.CustomerPhotoDeleter;
import com.erp.erp.domain.customer.business.CustomerUpdater;
import com.erp.erp.domain.customer.common.entity.Customer;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UploadCustomerPhotoWriter implements ItemWriter<Customer> {

  private final CustomerUpdater customerUpdater;
  private final CustomerPhotoDeleter customerPhotoDeleter;

  @Override
  public void write(Chunk<? extends Customer> chunk) {
    for (Customer customer : chunk) {
      customerUpdater.updatePhotoUrl(customer);
      customerPhotoDeleter.deleteByCustomer(customer);
    }
  }
}
