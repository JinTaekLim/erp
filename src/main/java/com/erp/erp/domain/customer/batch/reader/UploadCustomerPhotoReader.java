package com.erp.erp.domain.customer.batch.reader;

import com.erp.erp.domain.customer.business.CustomerPhotoReader;
import com.erp.erp.domain.customer.common.entity.CustomerPhoto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemReader;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UploadCustomerPhotoReader implements ItemReader<CustomerPhoto> {

  private final CustomerPhotoReader customerPhotoReader;
  private List<CustomerPhoto> customerPhotoList;
  private int currentIndex = 0;


  @Override
  public CustomerPhoto read() {
    if (customerPhotoList == null || customerPhotoList.isEmpty()) {
      customerPhotoList = customerPhotoReader.findAll();
      currentIndex = 0;
    }

    if (currentIndex >= customerPhotoList.size()) {return null;}

    return customerPhotoList.get(currentIndex++);
  }
}
