package com.erp.erp.domain.auth.business;

import com.erp.erp.domain.auth.common.exception.ExpiredTokenException;
import com.erp.erp.domain.auth.common.exception.InvalidJwtSignatureException;
import com.erp.erp.domain.auth.common.exception.InvalidTokenException;
import com.erp.erp.global.error.exception.UnAuthenticatedException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;

@Component
@RequiredArgsConstructor
public class TokenExtractor {

    private static final String Bearer = "Bearer ";

    private final TokenProperties tokenProperties;
    private final AuthenticationProvider authenticationProvider;


    public String getTokenFromAuthorizationHeader(HttpServletRequest request) {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.hasText(header) && header.startsWith(Bearer)) {
            return header.replace(Bearer, "");
        }
        return null;
    }

    public Authentication getAuthenticationByAccessToken(String accessToken) {
        Claims claims = getClaims(accessToken, tokenProperties.getAccessSecretKey());
        return authenticationProvider.getAuthentication(claims);
    }

    public Long extractMemberIdFromRefreshToken(String refreshToken) {
        Claims claims = getClaims(refreshToken, tokenProperties.getRefreshSecretKey());
        return Long.valueOf(claims.getSubject());
    }

    public Claims getClaims(String token, SecretKey refreshSecret) {
        try {
            return Jwts.parser().verifyWith(refreshSecret).build().parseSignedClaims(token).getPayload();
        } catch (ExpiredJwtException e) {
            throw new ExpiredTokenException();
        } catch (SignatureException e) {
            throw new InvalidJwtSignatureException();
        } catch (JwtException e) {
            throw new InvalidTokenException();
        } catch (Exception e) {
            throw new UnAuthenticatedException();
        }
    }
}
