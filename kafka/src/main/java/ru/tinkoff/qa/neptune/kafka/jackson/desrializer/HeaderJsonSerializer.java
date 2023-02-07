package ru.tinkoff.qa.neptune.kafka.jackson.desrializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.apache.kafka.common.header.Headers;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Set;

import static java.util.Objects.isNull;

public class HeaderJsonSerializer extends JsonSerializer<Headers> {

    @Override
    public void serialize(Headers value, JsonGenerator gen, SerializerProvider serializers) throws IOException {

        if (isNull(value)) {
            return;
        }

        var headerMap = new LinkedHashMap<String, Set<String>>();
        value.forEach(header -> {
            var set = headerMap.computeIfAbsent(header.key(), s -> new LinkedHashSet<>());
            set.add(new String(header.value()));
        });
        gen.writeObjectField("headers", headerMap);
    }
}
