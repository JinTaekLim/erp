package com.erp.erp.global.gson;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.JsonDeserializer;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class LocalDateSerializer implements JsonSerializer<LocalDate>, JsonDeserializer<LocalDate> {
  private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

  @Override
  public JsonElement serialize(LocalDate localDate, Type srcType, JsonSerializationContext context) {
    return new JsonPrimitive(formatter.format(localDate)); // LocalDate를 포맷팅하여 JSON 프리미티브로 반환
  }

  @Override
  public LocalDate deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
    return LocalDate.parse(json.getAsString(), formatter); // JSON 문자열을 LocalDate로 변환
  }
}
