package com.erp.erp.global.error;

import java.util.Objects;

import com.erp.erp.global.error.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

  @ExceptionHandler(Exception.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ApiResult<?> handleException(Exception ex){
    log.error(ex.getMessage());
    log.error("Exception : "+ ex);
    return ApiResult.fail();
  }

  @ExceptionHandler(RuntimeException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ApiResult<?> handleRuntimeExceptions(RuntimeException ex) {
    return ApiResult.fail(ApiErrorType.BAD_REQUEST, ex.getMessage());
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ApiResult<?> handleValidationExceptions(MethodArgumentNotValidException ex) {
    String errorMessage = ex.getBindingResult()
        .getFieldErrors()
        .stream()
        .map(DefaultMessageSourceResolvable::getDefaultMessage)
        .filter(Objects::nonNull)
        .findFirst()
        .orElse("유효성 검증 오류 발생");

    return ApiResult.fail(ApiErrorType.BAD_REQUEST, errorMessage);
  }


  @ExceptionHandler(HttpMessageNotReadableException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ApiResult<?> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
    log.info(ex.getMessage());
    return ApiResult.fail(ApiErrorType.BAD_REQUEST);
  }

  @ExceptionHandler(BusinessException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ApiResult<?> handler(BusinessException e) {
    return ApiResult.fail(e.getCode(), e.getMessage());
  }

}
