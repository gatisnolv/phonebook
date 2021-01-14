package com.gatis.phonebook.util;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class JsonMapper {
  private final Gson gson;

  public JsonMapper() {
    gson =
        new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
            .create();
  }

  public String toJson(Object input) {
    return gson.toJson(input);
  }

  private static class LocalDateAdapter implements JsonSerializer<LocalDate> {
    public JsonElement serialize(LocalDate date, Type typeOfSrc, JsonSerializationContext context) {
      return new JsonPrimitive(date.format(DateTimeFormatter.ISO_LOCAL_DATE));
    }
  }
}
