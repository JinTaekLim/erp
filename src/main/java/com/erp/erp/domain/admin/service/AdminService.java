package com.erp.erp.domain.admin.service;

import com.erp.erp.domain.account.business.AccountCreator;
import com.erp.erp.domain.account.common.entity.Account;
import com.erp.erp.domain.account.common.mapper.AccountMapper;
import com.erp.erp.domain.admin.common.dto.AddAccountDto;
import com.erp.erp.domain.admin.common.dto.AddInstituteDto;
import com.erp.erp.domain.admin.common.dto.AddPlanDto;
import com.erp.erp.domain.institute.business.InstituteCreator;
import com.erp.erp.domain.institute.business.InstituteReader;
import com.erp.erp.domain.institute.common.entity.Institute;
import com.erp.erp.domain.institute.common.mapper.InstituteMapper;
import com.erp.erp.domain.plan.business.PlanCreator;
import com.erp.erp.domain.plan.common.entity.Plan;
import com.erp.erp.domain.plan.common.mapper.PlanMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminService {

  private final AccountCreator accountCreator;
  private final InstituteReader instituteReader;
  private final PlanCreator planCreator;
  private final InstituteCreator instituteCreator;
  private final AccountMapper accountMapper;
  private final PlanMapper planMapper;
  private final InstituteMapper instituteMapper;

  public AddAccountDto.Response addAccount(AddAccountDto.Request req) {
    Institute institute = instituteReader.findById(req.getInstituteId());
    Account account = accountMapper.dtoToEntity(req, institute);
    accountCreator.save(account);
    return accountMapper.entityToDto(account);
  }

  public AddPlanDto.Response addPlans(AddPlanDto.Request req) {
    Plan plan = planMapper.dtoToEntity(req);
    planCreator.save(plan);
    return planMapper.entityToDto(plan);
  }

  public AddInstituteDto.Response addInstitute(AddInstituteDto.Request req) {
    Institute institute = instituteMapper.dtoToEntity(req);
    instituteCreator.save(institute);
    return instituteMapper.entityToAddInstituteResponse(institute);
  }

}
