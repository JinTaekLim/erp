package com.erp.erp.domain.batch.common.dto;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UploadCustomerPhotoMessage {

  private LocalDateTime time;

}
