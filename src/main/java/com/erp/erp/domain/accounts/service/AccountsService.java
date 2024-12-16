package com.erp.erp.domain.accounts.service;

import com.erp.erp.domain.accounts.business.AccountsCreator;
import com.erp.erp.domain.accounts.business.AccountsReader;
import com.erp.erp.domain.accounts.common.dto.AccountsLoginDto;
import com.erp.erp.domain.accounts.common.entity.Accounts;
import com.erp.erp.domain.admins.common.dto.AddAccountDto;
import com.erp.erp.domain.auth.business.TokenExtractor;
import com.erp.erp.domain.auth.business.TokenManager;
import com.erp.erp.domain.auth.common.dto.TokenDto;
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
  private final AccountsReader accountsReader;
  private final InstitutesReader institutesReader;
  private final TokenManager tokenManager;
  private final TokenExtractor tokenExtractor;

  public AddAccountDto.Response addAccount(AddAccountDto.Request req) {
    Institutes institutes = institutesReader.findById(req.getInstituteId());
    Accounts accounts = req.toEntityWithInstitute(institutes);
    accountsCreator.save(accounts);
    return AddAccountDto.Response.fromEntity(accounts);
  }

  public TokenDto login(AccountsLoginDto.Request req) {
    Accounts accounts = accountsReader.findByAccountAndPassword(
        req.getAccount(),
        req.getPassword()
    );
    return tokenManager.createToken(accounts);
  }

  public TokenDto reissueToken(String refreshToken) {
    Long memberId = tokenExtractor.extractMemberIdFromRefreshToken(refreshToken);
    Accounts accounts = accountsReader.findById(memberId);
    return tokenManager.reissueToken(accounts, refreshToken);
  }
}
