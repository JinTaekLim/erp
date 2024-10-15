package com.erp.erp.global.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

  @ExceptionHandler(Exception.class)
  public ApiResult<?> handleException(Exception ex){
    log.error(ex.getMessage());
    log.error("Exception : "+ ex);
    return ApiResult.fail();
  }
}
