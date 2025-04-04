package com.erp.erp.domain.customer.common.mapper;

import com.erp.erp.domain.account.common.entity.Account;
import com.erp.erp.domain.customer.common.dto.AddCustomerDto;
import com.erp.erp.domain.customer.common.dto.AddCustomerMessageDto;
import com.erp.erp.domain.plan.common.entity.Plan;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CustomerSenderMapper {

  AddCustomerMessageDto toAddCustomerMessageDto(Account account, Plan plan, AddCustomerDto.Request req, byte[] file);


}
