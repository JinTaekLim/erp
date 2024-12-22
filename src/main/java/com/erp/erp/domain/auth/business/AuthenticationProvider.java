package com.erp.erp.domain.auth.business;

import com.erp.erp.domain.account.common.entity.Account;
import io.jsonwebtoken.Claims;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;


@Component
public class AuthenticationProvider {

  private final String ROLE = "ACCOUNT";

  public Authentication getAuthentication(Account account) {
    List<SimpleGrantedAuthority> authorities = getAuthorities();
    User principal = new User(String.valueOf(account.getId()), "", authorities);
    return new UsernamePasswordAuthenticationToken(principal, null, authorities);
  }

  public Authentication getAuthentication(Claims claims) {
    List<SimpleGrantedAuthority> authorities = getAuthorities();
    User principal = new User(claims.getSubject(), "", authorities);
    return new UsernamePasswordAuthenticationToken(principal, null, authorities);
  }

  private List<SimpleGrantedAuthority> getAuthorities() {
    return Collections.singletonList(new SimpleGrantedAuthority(ROLE));
  }
}
