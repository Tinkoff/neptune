package ru.tinkoff.qa.neptune.kafka.properties;

import ru.tinkoff.qa.neptune.core.api.properties.PropertyDescription;
import ru.tinkoff.qa.neptune.core.api.properties.PropertyName;
import ru.tinkoff.qa.neptune.core.api.properties.PropertySupplier;

@PropertyDescription(description = "Defines default topics for poll",
        section = "Kafka")
@PropertyName("DEFAULT_TOPICS_FOR_POLL")
public final class KafkaDefaultTopicsForPollSupplier implements PropertySupplier<String[], String> {
    public static final KafkaDefaultTopicsForPollSupplier DEFAULT_TOPICS_FOR_POLL = new KafkaDefaultTopicsForPollSupplier();

    @Override
    public String[] parse(String value) {
        return value
                .trim()
                .replace(" ", "")
                .split(",");
    }
}
