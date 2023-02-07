package ru.tinkoff.qa.neptune.kafka.jackson.desrializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.header.Headers;

import java.io.IOException;
import java.util.function.Function;

import static java.util.Objects.nonNull;
import static java.util.Optional.ofNullable;

@SuppressWarnings("rawtypes")
public class ConsumerRecordSerializer extends JsonSerializer<ConsumerRecord> {

    private void writeSerializedProperty(String property,
                                         JsonGenerator gen,
                                         ConsumerRecord<?, ?> consumerRecord,
                                         Function<ConsumerRecord<?, ?>, Object> getPropertyValue) throws IOException {
        var recordProperty = ofNullable(getPropertyValue.apply(consumerRecord))
            .map(o -> {
                try {
                    return new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(o);
                } catch (Exception e) {
                    return property + " was not successfully deserialized because of: " + e.getClass() + " " + e.getMessage();
                }
            })
            .orElse(null);

        gen.writeStringField(property, recordProperty);
    }

    @Override
    public void serialize(ConsumerRecord value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeStartObject();
        gen.writeStringField("topic", value.topic());
        gen.writeNumberField("partition", value.partition());
        value.leaderEpoch().ifPresent(epoch -> {
            try {
                gen.writeNumberField("leaderEpoch", (Integer) epoch);
            } catch (IOException e) {
                throw new IllegalArgumentException(e);
            }
        });
        gen.writeNumberField("offset", value.offset());
        if (nonNull(value.timestampType())) {
            gen.writeObjectField(value.timestampType().toString(), value.timestamp());
        }
        serializers.findValueSerializer(Headers.class).serialize(value.headers(), gen, serializers);

        writeSerializedProperty("key", gen, value, ConsumerRecord::key);
        writeSerializedProperty("value", gen, value, ConsumerRecord::value);

        gen.writeEndObject();
    }
}
