package com.erp.erp.domain.customer.business;

import com.erp.erp.domain.customer.common.entity.Customer;
import com.erp.erp.domain.customer.common.entity.CustomerPhoto;
import com.erp.erp.global.util.ConverterUtil;
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
  private final CustomerPhotoReader customerPhotoReader;
  private final CustomerPhotoCreator customerPhotoCreator;

  public String uploadOrNull(MultipartFile file) {
    try {
      return s3Manager.upload(file);
    } catch (Exception e) {
      return null;
    }
  }

  private CustomerPhoto createCustomerPhoto(Customer customer, byte[] data) {
    return CustomerPhoto.builder()
        .customer(customer)
        .data(data)
        .build();
  }

  public void saveTempImage(Customer customer, MultipartFile file) {
    byte[] data = ConverterUtil.MultipartFileToByte(file);

    CustomerPhoto customerPhoto = customerPhotoReader.findByCustomer(customer)
        .map(photo -> photo.updateData(data))
        .orElseGet(() -> createCustomerPhoto(customer, data));

    customerPhotoCreator.save(customerPhoto);
  }

  public String update(Customer customer, MultipartFile file) {

    // 이전 데이터 삭제
    String oldFile = customer.getPhotoUrl();
    if (oldFile != null) s3Manager.delete(oldFile);

    try {
      return s3Manager.upload(file);
    } catch (Exception e) {
      saveTempImage(customer, file);
      return null;
    }
  }
}
