package com.erp.erp.global.util;

import org.springframework.web.multipart.MultipartFile;

public class ConverterUtil {

  public static byte[] MultipartFileToByte(MultipartFile file) {
    try {
      return file.getBytes();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

}
