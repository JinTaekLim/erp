package com.erp.erp.domain.auth.common.exception;

import com.erp.erp.domain.auth.common.exception.type.TokenErrorType;
import com.erp.erp.global.error.exception.ServerException;
import lombok.Getter;

@Getter
public class NotParsedValueException extends ServerException {

  private final String code;

  public NotParsedValueException() {
    this(TokenErrorType.NOT_PARSED_VALUE_ERROR.getMessage());
  }

  public NotParsedValueException(String message) {
    super(message);
    this.code = TokenErrorType.NOT_PARSED_VALUE_ERROR.name();
  }
}
