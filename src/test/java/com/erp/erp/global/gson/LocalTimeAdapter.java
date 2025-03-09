package com.erp.erp.global.gson;

import com.google.gson.*;
import java.lang.reflect.Type;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class LocalTimeAdapter implements JsonSerializer<LocalTime>, JsonDeserializer<LocalTime> {

  private static final DateTimeFormatter formatterWithSeconds = DateTimeFormatter.ofPattern("HH:mm:ss");
  private static final DateTimeFormatter formatterWithoutSeconds = DateTimeFormatter.ofPattern("HH:mm");

  @Override
  public JsonElement serialize(LocalTime src, Type typeOfSrc, JsonSerializationContext context) {
    return new JsonPrimitive(src.format(formatterWithSeconds));
  }

  @Override
  public LocalTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
    String timeString = json.getAsString();
    try {
      return LocalTime.parse(timeString, formatterWithSeconds);
    } catch (DateTimeParseException e) {
      return LocalTime.parse(timeString, formatterWithoutSeconds);
    }
  }
}
