package com.kadioglumf.authservice.core.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.json.JsonWriteFeature;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class SimpleJsonDeserializer {

  public static <T extends DomainEvent> T convertJsonDataToObject(String event, Class<T> clazz) {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    objectMapper.registerModule(new Jdk8Module());
    objectMapper.registerModule(new JavaTimeModule());
    objectMapper.setDateFormat(new StdDateFormat().withColonInTimeZone(true));
    objectMapper.getFactory().configure(JsonWriteFeature.ESCAPE_NON_ASCII.mappedFeature(), true);
    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    try {
      return objectMapper.readValue(event, clazz);
    } catch (JsonProcessingException ex) {
      ex.getStackTrace();
      throw new RuntimeException();
    }
  }

  public static <T extends DomainEvent> String convertObjectToJsonData(T value) {
    try {
      ObjectMapper objectMapper = new ObjectMapper();
      objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
      objectMapper.registerModule(new Jdk8Module());
      objectMapper.registerModule(new JavaTimeModule());
      objectMapper.setDateFormat(new StdDateFormat().withColonInTimeZone(true));
      objectMapper.getFactory().configure(JsonWriteFeature.ESCAPE_NON_ASCII.mappedFeature(), true);
      objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
      return objectMapper.writeValueAsString(value);
    } catch (JsonProcessingException ex) {
      ex.getStackTrace();
    }
    return null;
  }
}
