package com.erp.erp.global.config.log;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;

public class ByteArraySerializer extends JsonSerializer<byte[]> {

  @Override
  public void serialize(byte[] value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
    boolean hasValue = (value != null && value.length > 0);
    gen.writeBoolean(hasValue);
  }
}
