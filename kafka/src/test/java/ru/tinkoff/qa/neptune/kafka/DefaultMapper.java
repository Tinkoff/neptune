package ru.tinkoff.qa.neptune.kafka;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ru.tinkoff.qa.neptune.core.api.data.format.DataTransformer;

@Deprecated(forRemoval = true)
public class DefaultMapper implements DataTransformer {

    private final Gson gson;

    public DefaultMapper() {
        this.gson = new GsonBuilder()
            .create();
    }

    @Override
    public <T> T deserialize(String message, Class<T> cls) {
        return gson.fromJson(message, cls);
    }

    @Override
    public <T> T deserialize(String string, TypeReference<T> type) {
        return gson.fromJson(string, type.getType());
    }

    @Override
    public String serialize(Object obj) {
        return gson.toJson(obj);
    }
}
