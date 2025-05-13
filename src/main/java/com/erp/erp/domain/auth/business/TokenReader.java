package com.erp.erp.domain.auth.business;

import com.erp.erp.domain.auth.common.exception.NotFoundTokenException;
import com.erp.erp.domain.auth.repository.TokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TokenReader {

  private final TokenRepository tokenRepository;

  public String findByRefreshToken(String refreshToken) {
    String token = tokenRepository.findByRefreshToken(refreshToken);
    if (token == null) throw new NotFoundTokenException();
    return token;
  }
}
