package com.erp.erp.domain.auth.service;

import com.erp.erp.domain.auth.common.dto.TokenDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class TokenService {

  public TokenDto getToken() {

    return TokenDto.builder()
        .accessToken("accessToken")
        .refreshToken("refreshToken")
        .build();
  }

}
