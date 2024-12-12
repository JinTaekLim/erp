package com.erp.erp.domain.auth.business;

import com.erp.erp.domain.auth.common.entity.Token;
import com.erp.erp.domain.auth.repository.TokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TokenCreator {

  private final TokenRepository tokenRepository;

  public void saveRefreshToken(String refreshToken) {
    Token token = Token.builder().refreshToken(refreshToken).build();
    tokenRepository.save(token);
  }
}
