package com.erp.erp.global.config.log;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class LogMapper {

  private final ObjectMapper objectMapper;

  public LogMapper() {
    this.objectMapper = new ObjectMapper();
    this.objectMapper.registerModule(new JavaTimeModule());
    SimpleModule module = new SimpleModule();
    module.addSerializer(byte[].class, new ByteArraySerializer());
    this.objectMapper.registerModule(module);
  }

  public String convertToJson(Object object) {
    try {
      return objectMapper.writeValueAsString(object);
    } catch (Exception e) {
      throw new RuntimeException("Json 변환 실패 : " + e.getMessage());
    }
  }

  public String converToString(Object object) {
    return (object == null) ? null : object.toString();
  }
}
