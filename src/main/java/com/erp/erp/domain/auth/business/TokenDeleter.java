package com.erp.erp.domain.auth.business;

import com.erp.erp.domain.auth.repository.TokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TokenDeleter {

  private final TokenRepository tokenRepository;

  public void deleteToken(String token) {
    tokenRepository.deleteKey(token);
  }
}
