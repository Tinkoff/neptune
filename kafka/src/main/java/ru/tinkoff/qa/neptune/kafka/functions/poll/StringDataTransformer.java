package ru.tinkoff.qa.neptune.kafka.functions.poll;

import com.fasterxml.jackson.core.type.TypeReference;
import ru.tinkoff.qa.neptune.core.api.data.format.DataTransformer;

final class StringDataTransformer implements DataTransformer {

    @Override
    public <T> T deserialize(String string, Class<T> cls) {
        return (T) string;
    }

    @Override
    public <T> T deserialize(String string, TypeReference<T> type) {
        return (T) string;
    }

    @Override
    public String serialize(Object obj) {
        return obj.toString();
    }
}
