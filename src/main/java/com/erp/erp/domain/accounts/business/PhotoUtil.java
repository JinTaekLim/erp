package com.erp.erp.domain.accounts.business;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class PhotoUtil {


  public String upload(MultipartFile file) {

    return "photoUrl";
  }
}
