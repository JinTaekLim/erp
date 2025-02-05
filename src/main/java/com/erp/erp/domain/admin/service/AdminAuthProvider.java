package com.erp.erp.domain.admin.service;

import com.erp.erp.domain.admin.common.entity.Admin;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
public class AdminAuthProvider {

  private final String KEY = "adminId";

  public HttpSession getSession() {
    ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
    return attributes.getRequest().getSession();
  }

  private Object getValue(HttpSession session, String key) {
    return session.getAttribute(key);
  }

  public Object getAdminId() {
    HttpSession session = getSession();
    return getValue(session, KEY);
  }

  public void setAttribute(Admin admin) {
    HttpSession session = getSession();
    session.setAttribute(KEY, admin.getId());
  }

  private void setAttribute(HttpSession session, String key, Object value) {
    session.setAttribute(key, value);
  }

}
