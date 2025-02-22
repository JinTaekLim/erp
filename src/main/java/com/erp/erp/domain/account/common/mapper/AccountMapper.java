package com.erp.erp.domain.account.common.mapper;

import com.erp.erp.domain.account.common.entity.Account;
import com.erp.erp.domain.admin.common.dto.AddAccountDto;
import com.erp.erp.domain.admin.common.dto.UpdateAccountDto;
import com.erp.erp.domain.admin.common.entity.Admin;
import com.erp.erp.domain.institute.common.entity.Institute;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AccountMapper {

  @Mapping(target = "identifier", source = "req.identifier")
  @Mapping(target = "institute", source = "institute")
  @Mapping(target = "createdId", source = "admin.id")
  @Mapping(target = "password", source = "req.password")
  Account dtoToEntity(AddAccountDto.Request req, Institute institute, Admin admin);

  @Mapping(target = "instituteId", source = "account.institute.id")
  AddAccountDto.Response entityToDto(Account account);

  @Mapping(target = "accountId", source = "id")
  UpdateAccountDto.Response entityToUpdateAccountDto(Account account);
}
