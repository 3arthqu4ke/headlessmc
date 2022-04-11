package me.earth.headlessmc.launcher.util;

import com.google.gson.*;
import lombok.experimental.UtilityClass;
import lombok.val;
import lombok.var;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@UtilityClass
public class JsonUtil {
    /**
     * @param file
     * @return
     * @throws IOException
     * @throws JsonIOException
     * @throws JsonSyntaxException
     */
    public static JsonElement fromFile(File file) throws IOException {
        return fromInput(new FileInputStream(file));
    }

    public static JsonElement fromInput(InputStream stream) throws IOException {
        try (InputStreamReader reader = new InputStreamReader(stream)) {
            return JsonParser.parseReader(reader);
        }
    }

    public static String getString(JsonElement jo, String... path) {
        val result = getElement(jo, path);
        // TODO: if JsonArray, iterate and concatenate
        return result == null ? null : result.getAsString();
    }

    public static JsonObject getObject(JsonElement jo, String... path) {
        val result = getElement(jo, path);
        return result == null || !result.isJsonObject()
            ? null
            : result.getAsJsonObject();
    }

    public static JsonArray getArray(JsonElement jo, String... path) {
        val result = getElement(jo, path);
        return result == null || !result.isJsonArray()
            ? null
            : result.getAsJsonArray();
    }

    public static JsonElement getElement(JsonElement jo, String... path) {
        if (jo == null || !jo.isJsonObject()) {
            return null;
        }

        var current = jo.getAsJsonObject();
        for (int i = 0; i < path.length; i++) {
            val element = current.get(path[i]);
            if (element == null) {
                return null;
            } else if (!element.isJsonObject() && i < path.length - 1) {
                return null;
            } else if (i >= path.length - 1) {
                return element;
            }

            current = element.getAsJsonObject();
        }

        return null;
    }

    public static Map<String, String> toStringMap(JsonObject json) {
        return toMap(json, JsonElement::getAsString);
    }

    public static Map<String, Boolean> toBoolMap(JsonObject json) {
        return toMap(json, JsonElement::getAsBoolean);
    }

    public static <T> Map<String, T> toMap(JsonObject json,
                                           Function<JsonElement, T> mapper) {
        return json.entrySet()
                   .stream()
                   .collect(Collectors.toMap(Map.Entry::getKey,
                                             e -> mapper.apply(e.getValue())));
    }

    public static <T> List<T> toList(JsonArray array,
                                     Function<JsonElement, T> mapper) {
        val result = new ArrayList<T>(array.size());
        for (val element : array) {
            result.add(mapper.apply(element));
        }

        return result;
    }

    public static JsonArray toArray(JsonElement element) {
        if (element == null) {
            return new JsonArray();
        }

        if (element.isJsonArray()) {
            return element.getAsJsonArray();
        }

        val result = new JsonArray(1);
        result.add(element);
        return result;
    }

}
