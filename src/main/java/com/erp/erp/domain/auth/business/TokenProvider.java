package com.erp.erp.domain.auth.business;

import com.erp.erp.domain.accounts.common.entity.Accounts;
import com.erp.erp.domain.auth.common.dto.TokenDto;
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
    private final SecretKey ACCESS_SECRET;
    private final SecretKey REFRESH_SECRET;

    public TokenProvider(
            TokenProperties tokenProperties,
            TokenCreator tokenCreator,
            TokenExtractor tokenExtractor,
            AuthenticationProvider authenticationProvider,
            TokenGenerator tokenGenerator
    ) {
        this.tokenExtractor = tokenExtractor;
        this.tokenCreator = tokenCreator;
        this.authenticationProvider = authenticationProvider;
        this.tokenGenerator = tokenGenerator;
        this.ACCESS_SECRET = tokenProperties.getAccessSecretKey();
        this.REFRESH_SECRET = tokenProperties.getRefreshSecretKey();
    }


    public String getTokenFromAuthorizationHeader(HttpServletRequest request) {
        return tokenExtractor.getTokenFromAuthorizationHeader(request);
    }

    public Claims validateAccessToken(String accessToken) {
        return tokenExtractor.getClaims(accessToken, ACCESS_SECRET);
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

    private TokenDto getToken(Accounts accounts) {
        Authentication authentication = authenticationProvider.getAuthentication(accounts);
        return tokenGenerator.generateToken(authentication);
    }
}
