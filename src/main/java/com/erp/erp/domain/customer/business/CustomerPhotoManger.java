package com.erp.erp.domain.customer.business;

import com.erp.erp.domain.customer.common.entity.Customer;
import com.erp.erp.domain.customer.common.entity.CustomerPhoto;
import com.erp.erp.global.error.exception.ServerException;
import com.erp.erp.global.util.S3Manager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomerPhotoManger {

  private final S3Manager s3Manager;
  private final CustomerPhotoCreator customerPhotoCreator;
  private final CustomerPhotoReader customerPhotoReader;

  public String upload(MultipartFile file) {
    return s3Manager.upload(file);
  }

  public void saveTempImage(Customer customer, MultipartFile file) {
    try {
      CustomerPhoto customerPhoto = customerPhotoReader.findByCustomer(customer);

      if (customerPhoto == null) {
        customerPhoto = CustomerPhoto.builder()
            .customer(customer)
            .data(file.getBytes())
            .build();
      } else {
        customerPhoto.updateData(file.getBytes());
      }

      customerPhotoCreator.save(customerPhoto);

    } catch (Exception e) {
      log.error("File-UploadError : ", e);
      throw new ServerException();
    }
  }

  public String update(Customer customer, MultipartFile file, String oldFile) {
    if (oldFile != null) {
      s3Manager.deleteFromUrl(oldFile);
      return s3Manager.upload(file);
    }
    saveTempImage(customer, file);
    return null;
  }
}
