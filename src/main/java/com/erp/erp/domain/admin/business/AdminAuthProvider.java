package com.erp.erp.domain.admin.business;

import com.erp.erp.domain.admin.common.entity.Admin;
import com.erp.erp.domain.admin.common.exception.UnauthorizedAccessException;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminAuthProvider {

  private final AdminReader adminReader;
  private final HttpSessionManager httpSessionManager;

  private final String KEY = "adminId";

  public Admin getAdmin() {
    HttpSession session = httpSessionManager.getSession();
    Long adminId = (Long) httpSessionManager.getValue(session, KEY);
    if (adminId != null) return adminReader.findById(adminId);
    throw new UnauthorizedAccessException();
  }

  public void setAttribute(Admin admin) {
    HttpSession session = httpSessionManager.getSession();
    session.setAttribute(KEY, admin.getId());
  }

}
