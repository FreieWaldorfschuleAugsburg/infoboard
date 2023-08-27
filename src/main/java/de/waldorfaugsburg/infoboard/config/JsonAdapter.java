package de.waldorfaugsburg.infoboard.config;

import com.google.gson.*;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class JsonAdapter<T> implements JsonSerializer<T>, JsonDeserializer<T> {

    private final Map<String, Class<?>> classMap = new HashMap<>();

    @Override
    public JsonElement serialize(final T server, final Type type, final JsonSerializationContext context) {
        final JsonObject object = context.serialize(server, server.getClass()).getAsJsonObject();
        object.addProperty("type", server.getClass().getCanonicalName());
        return object;
    }

    @Override
    public T deserialize(final JsonElement json, final Type type, final JsonDeserializationContext context) {
        final JsonObject object = json.getAsJsonObject();
        final String clazz = object.remove("type").getAsString();
        return context.deserialize(object, classMap.computeIfAbsent(clazz, c -> {
            try {
                return Class.forName(c);
            } catch (final ClassNotFoundException e) {
                log.error("Error occurred while looking for class {}", c, e);
            }
            return null;
        }));
    }
}