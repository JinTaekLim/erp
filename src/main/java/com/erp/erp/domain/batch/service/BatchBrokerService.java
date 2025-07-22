package com.erp.erp.domain.batch.service;

import com.erp.erp.domain.batch.common.dto.UploadCustomerPhotoMessage;
import com.erp.erp.domain.batch.common.mapper.BatchBrokerMapper;
import com.erp.erp.domain.batch.sender.BatchSender;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BatchBrokerService {

  private final BatchSender batchSender;
  private final BatchBrokerMapper batchMapper;

  public void sendUploadTemporaryPhoto() {
    UploadCustomerPhotoMessage dto = batchMapper.uploadCustomerPhotoMessage(LocalDateTime.now());
    batchSender.sendUploadTemporaryPhoto(dto);
  }

}
