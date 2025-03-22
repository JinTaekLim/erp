package com.erp.erp.domain.admin.business;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
public class HttpSessionManager {

  public HttpSession getSession() {
    ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
    return attributes.getRequest().getSession();
  }

  public HttpServletResponse getResponse() {
    ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
    return attributes.getResponse();
  }

  public Object getValue(HttpSession session, String key) {
    return session.getAttribute(key);
  }

  private void setAttribute(HttpSession session, String key, Object value) {
    session.setAttribute(key, value);
  }
}
