package com.erp.erp.domain.auth.business;

import com.erp.erp.domain.account.common.entity.Account;
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

  public TokenDto createToken(Account account) {
    TokenDto tokenDto = tokenGenerator.getToken(account);
    tokenCreator.saveRefreshToken(tokenDto.getRefreshToken());
    return tokenDto;
  }

  public TokenDto reissueToken(Account account, String refreshToken) {
    Token token = tokenReader.findByRefreshToken(refreshToken);
    tokenDeleter.deleteToken(token);
    return createToken(account);
  }

}
