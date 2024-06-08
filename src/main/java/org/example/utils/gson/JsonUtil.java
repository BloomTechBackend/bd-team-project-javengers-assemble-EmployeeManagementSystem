package org.example.utils.gson;

import com.google.gson.Gson;
import org.example.model.results.Result;

public class JsonUtil {
    private static final Gson gson = GsonConfig.createGson();

    private JsonUtil() {}

    public static String createJsonResponse(Result result) {
        return gson.toJson(result);
    }

    public static <T> T fromJson(String json, Class<T> clazz) {
        return gson.fromJson(json, clazz);
    }
}
