package com.erp.erp.global.config.log;

import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import org.springframework.util.StreamUtils;

public class CachingRequestWrapper extends HttpServletRequestWrapper {

  private final byte[] cachedContent;

  public CachingRequestWrapper(HttpServletRequest request) throws IOException {
    super(request);
    Map<String, String[]> parameterMap = request.getParameterMap();
    this.cachedContent = StreamUtils.copyToByteArray(request.getInputStream());
  }

  @Override
  public ServletInputStream getInputStream() {
    return new ServletInputStream() {
      private final InputStream cachedBodyInputStream = new ByteArrayInputStream(cachedContent);

      @Override
      public boolean isFinished() {
        try {
          return cachedBodyInputStream.available() == 0;
        } catch (IOException e) {
          e.printStackTrace();
        }
        return false;
      }

      @Override
      public boolean isReady() {
        return true;
      }

      @Override
      public void setReadListener(ReadListener readListener) {
        throw new UnsupportedOperationException();
      }

      @Override
      public int read() throws IOException {
        return cachedBodyInputStream.read();
      }
    };
  }
}
