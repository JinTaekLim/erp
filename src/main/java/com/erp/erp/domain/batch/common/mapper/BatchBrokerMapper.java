package com.erp.erp.domain.batch.common.mapper;

import com.erp.erp.domain.batch.common.dto.UploadCustomerPhotoMessage;
import java.time.LocalDateTime;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BatchBrokerMapper {

  UploadCustomerPhotoMessage uploadCustomerPhotoMessage(LocalDateTime time);

}
