package com.erp.erp.domain.auth.business;

import com.erp.erp.domain.accounts.common.entity.Accounts;
import com.erp.erp.domain.auth.common.dto.TokenDto;
import com.erp.erp.domain.auth.common.entity.Token;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TokenManager {

  private final TokenCreator tokenCreator;
  private final TokenGenerator tokenGenerator;
  private final TokenReader tokenReader;
  private final TokenDeleter tokenDeleter;

  public TokenDto createToken(Accounts accounts) {
    TokenDto tokenDto = tokenGenerator.getToken(accounts);
    tokenCreator.saveRefreshToken(tokenDto.getRefreshToken());
    return tokenDto;
  }

  public TokenDto reissueToken(Accounts accounts, String refreshToken) {
    Token token = tokenReader.findByRefreshToken(refreshToken);
    tokenDeleter.deleteToken(token);
    return createToken(accounts);
  }

}
