package com.erp.erp.domain.account.business;

import com.erp.erp.global.util.S3Manager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
@RequiredArgsConstructor
public class PhotoUtil {

  private final S3Manager s3Manager;

  public String upload(MultipartFile file) {
    if (file.isEmpty()) {return null;}
    return s3Manager.upload(file);
  }
}
