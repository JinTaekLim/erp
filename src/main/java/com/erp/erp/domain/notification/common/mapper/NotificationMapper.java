package com.erp.erp.domain.notification.common.mapper;

import com.erp.erp.domain.notification.dto.CreateAuthMessage;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface NotificationMapper {

  CreateAuthMessage.Request createAuthMessage(Long instituteId, Long accountId);

}
