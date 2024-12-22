package com.erp.erp.domain.account.service;

import com.erp.erp.domain.account.business.AccountCreator;
import com.erp.erp.domain.account.business.AccountReader;
import com.erp.erp.domain.account.common.dto.AccountLoginDto;
import com.erp.erp.domain.account.common.entity.Account;
import com.erp.erp.domain.admin.common.dto.AddAccountDto;
import com.erp.erp.domain.auth.business.TokenExtractor;
import com.erp.erp.domain.auth.business.TokenManager;
import com.erp.erp.domain.auth.common.dto.TokenDto;
import com.erp.erp.domain.institute.business.InstituteReader;
import com.erp.erp.domain.institute.common.entity.Institute;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AccountService {

  private final AccountCreator accountCreator;
  private final AccountReader accountReader;
  private final InstituteReader instituteReader;
  private final TokenManager tokenManager;
  private final TokenExtractor tokenExtractor;

  public AddAccountDto.Response addAccount(AddAccountDto.Request req) {
    Institute institute = instituteReader.findById(req.getInstituteId());
    Account account = req.toEntityWithInstitute(institute);
    accountCreator.save(account);
    return AddAccountDto.Response.fromEntity(account);
  }

  public TokenDto login(AccountLoginDto.Request req) {
    Account account = accountReader.findByAccountAndPassword(
        req.getAccount(),
        req.getPassword()
    );
    return tokenManager.createToken(account);
  }

  public TokenDto reissueToken(String refreshToken) {
    Long memberId = tokenExtractor.extractMemberIdFromRefreshToken(refreshToken);
    Account account = accountReader.findById(memberId);
    return tokenManager.reissueToken(account, refreshToken);
  }
}
