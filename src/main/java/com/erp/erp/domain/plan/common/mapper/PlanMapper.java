package com.erp.erp.domain.plan.common.mapper;

import com.erp.erp.domain.admin.common.dto.AddPlanDto;
import com.erp.erp.domain.plan.common.dto.GetPlanDto;
import com.erp.erp.domain.plan.common.entity.Plan;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PlanMapper {

  @Mapping(target = "name", source = "planName")
  Plan dtoToEntity(AddPlanDto.Request req);

  @Mapping(target = "planName", source = "name")
  AddPlanDto.Response entityToAddPlanResponse(Plan plan);

  List<GetPlanDto.Response> entityToGetPlanResponseList(List<Plan> plans);
  @Mapping(target = "planName", source = "name")
  GetPlanDto.Response entityToGetPlanResponse(Plan plan);
}
