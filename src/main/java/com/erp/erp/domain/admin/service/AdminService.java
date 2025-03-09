package com.erp.erp.domain.admin.service;

import com.erp.erp.domain.account.business.AccountCreator;
import com.erp.erp.domain.account.business.AccountReader;
import com.erp.erp.domain.account.business.AccountUpdater;
import com.erp.erp.domain.account.common.entity.Account;
import com.erp.erp.domain.account.common.mapper.AccountMapper;
import com.erp.erp.domain.admin.business.AdminAuthProvider;
import com.erp.erp.domain.admin.business.AdminReader;
import com.erp.erp.domain.admin.common.dto.AddAccountDto;
import com.erp.erp.domain.admin.common.dto.AddInstituteDto;
import com.erp.erp.domain.admin.common.dto.AddPlanDto;
import com.erp.erp.domain.admin.common.dto.GetAccountDto;
import com.erp.erp.domain.admin.common.dto.LoginDto;
import com.erp.erp.domain.admin.common.dto.UpdateAccountDto;
import com.erp.erp.domain.admin.common.entity.Admin;
import com.erp.erp.domain.admin.common.dto.GetInstituteDto;
import com.erp.erp.domain.institute.business.InstituteCreator;
import com.erp.erp.domain.institute.business.InstituteReader;
import com.erp.erp.domain.institute.common.entity.Institute;
import com.erp.erp.domain.institute.common.mapper.InstituteMapper;
import com.erp.erp.domain.plan.business.PlanCreator;
import com.erp.erp.domain.plan.common.entity.Plan;
import com.erp.erp.domain.plan.common.mapper.PlanMapper;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminService {

  private final AccountCreator accountCreator;
  private final AccountReader accountReader;
  private final AccountUpdater accountUpdater;
  private final InstituteReader instituteReader;
  private final PlanCreator planCreator;
  private final InstituteCreator instituteCreator;
  private final AccountMapper accountMapper;
  private final PlanMapper planMapper;
  private final InstituteMapper instituteMapper;
  private final AdminReader adminReader;
  private final AdminAuthProvider adminAuthProvider;

  public AddAccountDto.Response addAccount(AddAccountDto.Request req) {
    Admin admin = adminAuthProvider.getAdmin();
    Institute institute = instituteReader.findById(req.getInstituteId());
    Account account = accountMapper.dtoToEntity(req, institute, admin);
    accountCreator.save(account);
    return accountMapper.entityToDto(account);
  }

  public AddPlanDto.Response addPlans(AddPlanDto.Request req) {
    Admin admin = adminAuthProvider.getAdmin();
    Plan plan = planMapper.dtoToEntity(req, String.valueOf(admin.getId()));
    planCreator.save(plan);
    return planMapper.entityToAddPlanResponse(plan);
  }

  public AddInstituteDto.Response addInstitute(AddInstituteDto.Request req) {
    Admin admin = adminAuthProvider.getAdmin();
    Institute institute = instituteMapper.dtoToEntity(req, String.valueOf(admin.getId()));
    instituteCreator.save(institute);
    return instituteMapper.entityToAddInstituteResponse(institute);
  }

  public List<GetInstituteDto.Response> getInstitutes() {
    List<Institute> institutes = instituteReader.findAll();
    return instituteMapper.entityToGetInstituteDto(institutes);
  }

  public List<GetAccountDto.Response> getAccounts(Long instituteId) {
    Admin admin = adminAuthProvider.getAdmin();
    List<Account> accounts = accountReader.findByInstituteId(instituteId);
    return accountMapper.entityToGetAccountDto(accounts);
  }

  public UpdateAccountDto.Response updateAccount(UpdateAccountDto.Request req) {
    Admin admin = adminAuthProvider.getAdmin();
    Account account = accountReader.findById(req.getAccountId());
    accountUpdater.update(account, req, String.valueOf(admin.getId()));
    return accountMapper.entityToUpdateAccountDto(account);
  }

  public void lockAccount(Long accountId) {
    Admin admin = adminAuthProvider.getAdmin();
    Account account = accountReader.findById(accountId);
    accountUpdater.lockAccount(account, String.valueOf(admin.getId()));
  }

  public void login(LoginDto.Request req) {
    Admin admin = adminReader.findByIdentifierAndPassword(req);
    adminAuthProvider.setAttribute(admin);
  }
}
