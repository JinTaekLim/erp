package com.erp.erp.domain.auth.business;

import com.erp.erp.domain.accounts.common.entity.Accounts;
import com.erp.erp.domain.auth.common.dto.TokenDto;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class TokenGenerator {

    private final TokenProperties tokenProperties;
    private final AuthenticationProvider authenticationProvider;


    public TokenDto getToken(Accounts accounts) {
        Authentication authentication = authenticationProvider.getAuthentication(accounts);
        return generateToken(authentication);
    }

    private String generateToken(Authentication authentication, SecretKey secret, Long exp) {
        Date now = new Date();
        Date expiredDate = new Date(now.getTime() + exp);
        String authorities = authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining());

        return Jwts.builder()
                .subject(authentication.getName())
                .claim("authorities", authorities)
                .issuedAt(now)
                .expiration(expiredDate)
                .signWith(secret)
                .compact();
    }

    private TokenDto generateToken(Authentication authentication) {
        String accessToken = generateAccessToken(authentication);
        String refreshToken = generateRefreshToken(authentication);
        return new TokenDto(accessToken, refreshToken);
    }

    private String generateAccessToken(Authentication authentication) {
        SecretKey secretKey = tokenProperties.getAccessSecretKey();
        Long exp = tokenProperties.getAccess().getExp();
        return generateToken(authentication, secretKey, exp);
    }

    private String generateRefreshToken(Authentication authentication) {
        SecretKey secretKey = tokenProperties.getRefreshSecretKey();
        Long exp = tokenProperties.getRefresh().getExp();
        return generateToken(authentication, secretKey, exp);
    }

}
