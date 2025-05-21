package com.erp.erp.domain.customer.common.mapper;

import com.erp.erp.domain.customer.common.entity.Customer;
import com.erp.erp.domain.customer.common.entity.CustomerPhoto;
import com.erp.erp.global.util.ConverterUtil;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.web.multipart.MultipartFile;

@Mapper(componentModel = "spring", imports = {ConverterUtil.class})
public interface CustomerPhotoMapper {

  @Mapping(target = "data", expression = "java(ConverterUtil.MultipartFileToByte(file))")
  CustomerPhoto toCustomerPhoto(Customer customer, MultipartFile file);
}
