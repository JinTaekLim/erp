package com.erp.erp.global.config;

import com.erp.erp.domain.auth.interceptor.AuthInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class InterceptorConfig implements WebMvcConfigurer {

  private final AuthInterceptor authInterceptor;

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(authInterceptor);
  }
}
