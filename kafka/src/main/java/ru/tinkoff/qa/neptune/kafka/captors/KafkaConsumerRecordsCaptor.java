package ru.tinkoff.qa.neptune.kafka.captors;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import ru.tinkoff.qa.neptune.core.api.event.firing.captors.StringCaptor;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.kafka.jackson.desrializer.KafkaJacksonModule;

import java.util.Collection;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Description("Received Kafka consumer records")
public final class KafkaConsumerRecordsCaptor extends StringCaptor<List<? extends ConsumerRecord<?, ?>>> {

    @Override
    public List<? extends ConsumerRecord<?, ?>> getCaptured(Object toBeCaptured) {
        if (toBeCaptured instanceof Collection) {
            var result = ((Collection<?>) toBeCaptured)
                .stream().filter(o -> o instanceof ConsumerRecord<?, ?>)
                .map(o -> (ConsumerRecord<?, ?>) o)
                .collect(toList());

            if (!result.isEmpty()) {
                return result;
            }
        }
        return null;
    }

    @Override
    public StringBuilder getData(List<? extends ConsumerRecord<?, ?>> caught) {
        var result = new StringBuilder();
        var mapper = new ObjectMapper();
        mapper.registerModule(new KafkaJacksonModule());
        caught.forEach(r -> {
            String stringToAppend = null;
            try {
                stringToAppend = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(r);
            } catch (Exception e) {
                stringToAppend = "Was not serialized because of:" + e.getClass() + " " + e.getMessage();
            }
            result.append("Consumer Record: ").append(stringToAppend).append("\r\n");
        });
        return result;
    }
}
