package com.erp.erp.global.gson;

import com.google.gson.*;
import java.lang.reflect.Type;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class LocalTimeAdapter implements JsonSerializer<LocalTime>, JsonDeserializer<LocalTime> {

  private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

  @Override
  public JsonElement serialize(LocalTime src, Type typeOfSrc, JsonSerializationContext context) {
    return new JsonPrimitive(src.format(formatter));
  }

  @Override
  public LocalTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
    return LocalTime.parse(json.getAsString(), formatter);
  }
}
