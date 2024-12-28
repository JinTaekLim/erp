package com.erp.erp.domain.auth.interceptor;

import com.erp.erp.domain.account.business.AccountCreator;
import com.erp.erp.domain.account.business.AccountReader;
import com.erp.erp.domain.account.common.entity.Account;
import com.erp.erp.domain.auth.business.TokenExtractor;
import com.erp.erp.domain.auth.business.TokenManager;
import com.erp.erp.domain.auth.common.dto.TokenDto;
import com.erp.erp.domain.institute.common.entity.Institute;
import com.erp.erp.domain.institute.repository.InstituteRepository;
import com.erp.erp.global.annotation.authentication.PermitAll;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

@Component
@RequiredArgsConstructor
public class AuthInterceptor implements HandlerInterceptor {

  private final TokenExtractor tokenExtractor;

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
      Object handler) {

    if (isViewRequest(handler)) { return true; }
    if (hasAnnotation(handler, PermitAll.class)) { return true; }

    String accessToken = tokenExtractor.getTokenFromAuthorizationHeader(request);

    // 개발 환경에서 토큰 없이 테스트하는 경우 임시 계정을 생성하여 반환하는 임시 코드 / 이후 삭제 예정
    if (accessToken == null) {accessToken = getTemporaryAccountInfo();}

    validateTokenPresence(accessToken);
    setAuthentication(accessToken);
    return true;
  }

  private boolean hasAnnotation(Object handler, Class<? extends Annotation> clazz) {
    HandlerMethod handlerMethod = (HandlerMethod) handler;
    return handlerMethod.getMethod().isAnnotationPresent(clazz);
  }

  private void validateTokenPresence(String token) {
    if(!StringUtils.hasText(token)) throw new RuntimeException();
  }

  private void setAuthentication(String token) {
    Authentication authentication = tokenExtractor.getAuthenticationByAccessToken(token);
    SecurityContextHolder.getContext().setAuthentication(authentication);
  }

  // Swagger 같은 js/html 관련 파일들은 통과한다.(view 관련 요청 = ResourceHttpRequestHandler)
  private boolean isViewRequest(Object handler) {
    return handler instanceof ResourceHttpRequestHandler;
  }





  /*
  개발 환경에서 토큰 없이 테스트하는 경우 임시 계정을 생성하여 반환하는 임시 코드
  이후 삭제 예정
  */

  private final AccountCreator accountCreator;
  private final AccountReader accountReader;
  private final InstituteRepository instituteRepository;
  private final TokenManager tokenManager;

  public String getTemporaryAccountInfo() {
    Institute institute = instituteRepository.findById(1L)
        .orElseGet(()-> {
          Institute newInstitute = Institute.builder()
              .name("test").totalSeat(4).build();
          return instituteRepository.save(newInstitute);
        });
    Account account = accountReader.findOptionalById(1L)
        .orElseGet(() -> {
          Account newAccount = Account.builder()
              .accountId("test").password("test").institute(institute).build();
          return accountCreator.save(newAccount);
        });
    TokenDto tokenDto = tokenManager.createToken(account);
    return tokenDto.getAccessToken();
  }


}
