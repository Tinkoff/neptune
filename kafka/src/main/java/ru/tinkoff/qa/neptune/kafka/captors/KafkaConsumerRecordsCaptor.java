package ru.tinkoff.qa.neptune.kafka.captors;

import com.google.gson.GsonBuilder;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import ru.tinkoff.qa.neptune.core.api.event.firing.captors.StringCaptor;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;

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
        caught.forEach(r -> {
            var stringToAppend = new GsonBuilder().setPrettyPrinting().create().toJson(r);
            result.append("Consumer Record: ").append(stringToAppend).append("\r\n");
        });
        return result;
    }
}
