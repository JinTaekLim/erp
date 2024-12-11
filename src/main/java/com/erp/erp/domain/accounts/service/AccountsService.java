package com.erp.erp.domain.accounts.service;

import com.erp.erp.domain.accounts.business.AccountsCreator;
import com.erp.erp.domain.accounts.common.entity.Accounts;
import com.erp.erp.domain.admins.common.dto.AddAccountDto;
import com.erp.erp.domain.institutes.business.InstitutesReader;
import com.erp.erp.domain.institutes.common.entity.Institutes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AccountsService {

  private final AccountsCreator accountsCreator;
  private final InstitutesReader institutesReader;

  public AddAccountDto.Response addAccount(AddAccountDto.Request req) {
    Institutes institutes = institutesReader.findById(req.getInstituteId());
    Accounts accounts = req.toEntityWithInstitute(institutes);
    accountsCreator.save(accounts);
    return AddAccountDto.Response.fromEntity(accounts);
  }
}
