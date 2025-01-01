package com.erp.erp.domain.institute.common.mapper;

import com.erp.erp.domain.admin.common.dto.AddInstituteDto;
import com.erp.erp.domain.institute.common.dto.GetInstituteInfoDto;
import com.erp.erp.domain.institute.common.dto.UpdateTotalSeatDto;
import com.erp.erp.domain.institute.common.entity.Institute;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface InstituteMapper {

  Institute dtoToEntity(AddInstituteDto.Request req);

  AddInstituteDto.Response entityToAddInstituteResponse(Institute institute);

  UpdateTotalSeatDto.Response entityToUpdateTotalSeatResponse(Institute institute);

  GetInstituteInfoDto.Response entityToGetInstituteInfoDtoResponse(Institute institute);
}
