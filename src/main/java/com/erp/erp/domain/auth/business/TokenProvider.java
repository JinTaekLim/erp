package com.erp.erp.domain.auth.business;

import com.erp.erp.domain.accounts.common.entity.Accounts;
import com.erp.erp.domain.auth.common.dto.TokenDto;
import com.erp.erp.domain.auth.common.entity.Token;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;

@Component
public class TokenProvider {

  private final TokenExtractor tokenExtractor;
  private final TokenCreator tokenCreator;
  private final AuthenticationProvider authenticationProvider;
  private final TokenGenerator tokenGenerator;
  private final TokenReader tokenReader;
  private final TokenDeleter tokenDeleter;
  private final SecretKey ACCESS_SECRET;
  private final SecretKey REFRESH_SECRET;

  public TokenProvider(
      TokenProperties tokenProperties,
      TokenCreator tokenCreator,
      TokenExtractor tokenExtractor,
      AuthenticationProvider authenticationProvider,
      TokenGenerator tokenGenerator,
      TokenReader tokenReader,
      TokenDeleter tokenDeleter
  ) {
    this.tokenExtractor = tokenExtractor;
    this.tokenCreator = tokenCreator;
    this.authenticationProvider = authenticationProvider;
    this.tokenGenerator = tokenGenerator;
    this.tokenDeleter = tokenDeleter;
    this.ACCESS_SECRET = tokenProperties.getAccessSecretKey();
    this.REFRESH_SECRET = tokenProperties.getRefreshSecretKey();
    this.tokenReader = tokenReader;
  }


  public String getTokenFromAuthorizationHeader(HttpServletRequest request) {
    return tokenExtractor.getTokenFromAuthorizationHeader(request);
  }

  public Authentication getAuthenticationByAccessToken(String accessToken) {
    Claims claims = validateAccessToken(accessToken);
    return authenticationProvider.getAuthentication(claims);
  }

  public TokenDto createToken(Accounts accounts) {
    TokenDto tokenDto = getToken(accounts);
    tokenCreator.saveRefreshToken(tokenDto.getRefreshToken());
    return tokenDto;
  }

  public TokenDto reissueToken(Accounts accounts, String refreshToken) {
    Token token = tokenReader.findByRefreshToken(refreshToken);
    tokenDeleter.deleteToken(token);
    return createToken(accounts);
  }

  public Long extractMemberIdFromRefreshToken(String refreshToken) {
    Claims claims = validateRefreshToken(refreshToken);
    return Long.valueOf(claims.getSubject());
  }

  private TokenDto getToken(Accounts accounts) {
    Authentication authentication = authenticationProvider.getAuthentication(accounts);
    return tokenGenerator.generateToken(authentication);
  }

  private Claims validateAccessToken(String accessToken) {
    return tokenExtractor.getClaims(accessToken, ACCESS_SECRET);
  }

  private Claims validateRefreshToken(String refreshToken) {
    return tokenExtractor.getClaims(refreshToken, REFRESH_SECRET);
  }
}
