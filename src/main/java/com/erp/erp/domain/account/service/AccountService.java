package com.erp.erp.domain.account.service;

import com.erp.erp.domain.account.business.AccountReader;
import com.erp.erp.domain.account.common.dto.AccountLoginDto;
import com.erp.erp.domain.account.common.entity.Account;
import com.erp.erp.domain.auth.business.TokenExtractor;
import com.erp.erp.domain.auth.business.TokenManager;
import com.erp.erp.domain.auth.common.dto.TokenDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AccountService {

  private final AccountReader accountReader;
  private final TokenManager tokenManager;
  private final TokenExtractor tokenExtractor;

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
