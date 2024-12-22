package com.erp.erp.domain.auth.business;

import com.erp.erp.domain.account.business.AccountReader;
import com.erp.erp.domain.auth.common.exception.AuthenticationNameNullException;
import com.erp.erp.domain.auth.common.exception.AuthenticationNullException;
import com.erp.erp.domain.auth.common.exception.NotParsedValueException;
import com.erp.erp.domain.auth.common.exception.UnAuthenticationAccountException;
import com.erp.erp.domain.auth.common.exception.type.TokenErrorType;
import com.erp.erp.domain.institutes.common.entity.Institutes;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@RequiredArgsConstructor
public class AuthProvider {

  private final AccountReader accountReader;

  private final String GUEST = "anonymousUser";

  private Long getCurrentAccountId() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (Objects.isNull(authentication)) {
      throw new AuthenticationNullException();
    }
    if (authentication.getPrincipal().equals(GUEST)) {
      throw new UnAuthenticationAccountException();
    }
    String name = authentication.getName();
    if (!StringUtils.hasText(name)) {
      throw new AuthenticationNameNullException();
    }
    try {
      return Long.valueOf(name);
    } catch (NumberFormatException e) {
      throw new NotParsedValueException(
          TokenErrorType.NOT_PARSED_VALUE_ERROR.getMessage() + " : " + name);
    }
  }

  public Institutes getCurrentInstitute() {
    Long accountId = getCurrentAccountId();
    return accountReader.findInstitutesByAccountId(accountId);
  }

}
