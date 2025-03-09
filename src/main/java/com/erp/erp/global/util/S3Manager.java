package com.erp.erp.global.util;

import com.erp.erp.global.error.exception.ServerException;
import io.awspring.cloud.s3.ObjectMetadata;
import io.awspring.cloud.s3.S3Resource;
import io.awspring.cloud.s3.S3Template;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Component
@Slf4j
@RequiredArgsConstructor
public class S3Manager {

  private final S3Template s3Template;

  @Value("${spring.cloud.aws.s3.bucket}")
  private String bucketName;

  public String upload(MultipartFile file) throws IOException {
    String fileName = file.getOriginalFilename();
    String extension = StringUtils.getFilenameExtension(fileName);
    String key = UUID.randomUUID() + "." + extension;
    S3Resource s3Resource = s3Template.upload(bucketName, key, file.getInputStream(),
        ObjectMetadata.builder().contentType(file.getContentType()).build());
    return s3Resource.getURL().toString();
  }

  public String upload(InputStream inputStream) throws IOException {
    String contentType = "image/webp";
    String extension = "webp";
    String key = UUID.randomUUID() + "." + extension;
    S3Resource s3Resource = s3Template.upload(bucketName, key, inputStream,
        ObjectMetadata.builder().contentType(contentType).build());
    return s3Resource.getURL().toString();
  }

  public void deleteFromUrl(String profileUrl) {
    String key = extractS3Key(profileUrl);
    delete(key);
  }

  private String extractS3Key(String url) {
    return url.substring(url.lastIndexOf("/") + 1);
  }

  public void delete(String key) {
    try {
      s3Template.deleteObject(bucketName, key);
    } catch (Exception e) {
      log.error("실패 : " + e.getMessage());
    }
  }


  public void bucketExists() {
      if (!s3Template.bucketExists(bucketName)) throw new ServerException();
  }
}
