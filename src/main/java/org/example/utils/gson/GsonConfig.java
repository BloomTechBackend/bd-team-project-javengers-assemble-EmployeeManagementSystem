package org.example.utils.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class GsonConfig {
    public static Gson createGson() {
        return new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new CustomSerializers.LocalDateSerializer())
                .registerTypeAdapter(LocalDate.class, new CustomSerializers.LocalDateDeserializer())
                .registerTypeAdapter(LocalDateTime.class, new CustomSerializers.LocalDateTimeSerializer())
                .registerTypeAdapter(LocalDateTime.class, new CustomSerializers.LocalDateTimeDeserializer())
                //.setPrettyPrinting()
                .create();
    }
}
