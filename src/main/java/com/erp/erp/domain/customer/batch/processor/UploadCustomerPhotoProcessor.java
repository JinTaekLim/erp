package com.erp.erp.domain.customer.batch.processor;

import com.erp.erp.domain.customer.common.entity.Customer;
import com.erp.erp.domain.customer.common.entity.CustomerPhoto;
import com.erp.erp.global.util.S3Manager;
import java.io.ByteArrayInputStream;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UploadCustomerPhotoProcessor implements ItemProcessor<CustomerPhoto, Customer> {

  private final S3Manager s3Manager;
  private final String UPDATE_ID = "BATCH";

  @Override
  public Customer process(CustomerPhoto item) throws Exception {
    ByteArrayInputStream file = new ByteArrayInputStream(item.getData());
    String url = s3Manager.upload(file);

    Customer customer = item.getCustomer();
    customer.updatePhotoUrl(url, UPDATE_ID);
    return customer;
  }
}
