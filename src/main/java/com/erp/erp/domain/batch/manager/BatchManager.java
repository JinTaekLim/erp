package com.erp.erp.domain.batch.manager;

import com.erp.erp.domain.customer.batch.UploadCustomerPhotoBatch;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BatchManager {

  private final UploadCustomerPhotoBatch uploadCustomerPhotoBatch;

  public Job getUploadCustomerPhotoBatch() {
    return uploadCustomerPhotoBatch.uploadTemporaryPhotoJob();
  }
}
