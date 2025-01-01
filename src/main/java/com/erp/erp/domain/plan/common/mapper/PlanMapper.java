package com.erp.erp.domain.plan.common.mapper;

import com.erp.erp.domain.admin.common.dto.AddPlanDto;
import com.erp.erp.domain.plan.common.dto.GetPlanDto;
import com.erp.erp.domain.plan.common.entity.Plan;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PlanMapper {

  Plan dtoToEntity(AddPlanDto.Request req);
  AddPlanDto.Response entityToAddPlanResponse(Plan plan);

  List<GetPlanDto.Response> entityToGetPlanResponseList(List<Plan> plans);
}
