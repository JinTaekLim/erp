package com.erp.erp.global.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ApiResult<T> {

  private final String code;
  private final String message;
  private final T data;

  public ApiResult(String code, String message, T data) {
    this.code = code;
    this.message = message;
    this.data = data;
  }

  public static <T> ApiResult<T> success(T data) {
    return new ApiResult<T>(HttpStatus.OK.name(), HttpStatus.OK.getReasonPhrase(), data);
  }

  public static <T> ApiResult<T> fail(String code, String message, T data) {
    return new ApiResult<T>(code, message, data);
  }

  public static <T> ApiResult<T> fail(){
    ApiErrorType error = ApiErrorType.INTERNAL_SERVER_ERROR;
    return new ApiResult<T>(error.name(), error.getMessage(), null);
  }
}
