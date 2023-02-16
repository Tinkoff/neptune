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

@SuppressWarnings({"rawtypes", "unchecked"})
public class ConsumerRecordSerializer extends JsonSerializer<ConsumerRecord> {

    private void writeSerializedProperty(String property,
                                         JsonGenerator gen,
                                         ConsumerRecord<?, ?> consumerRecord,
                                         Function<ConsumerRecord<?, ?>, Object> getPropertyValue) throws IOException {

        var propertyValue = getPropertyValue.apply(consumerRecord);
        if (nonNull(propertyValue)) {
            gen.writeStringField(property, new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(propertyValue));
        } else {
            gen.writeObjectField(property, null);
        }
    }

    @Override
    public void serialize(ConsumerRecord value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeStartObject();
        gen.writeStringField("topic", value.topic());
        gen.writeNumberField("partition", value.partition());
        gen.writeObjectField("leaderEpoch", value.leaderEpoch().orElse(null));
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
