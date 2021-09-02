package ru.tinkoff.qa.neptune.kafka.properties;

import ru.tinkoff.qa.neptune.core.api.properties.PropertyDescription;
import ru.tinkoff.qa.neptune.core.api.properties.PropertyName;
import ru.tinkoff.qa.neptune.core.api.properties.PropertySupplier;

@PropertyDescription(description = "Defines default topics for poll",
        section = "Kafka")
@PropertyName("DEFAULT_TOPICS_FOR_POLL")
public final class KafkaDefaultTopicsForPollProperty implements PropertySupplier<String[], String> {
    public static final KafkaDefaultTopicsForPollProperty DEFAULT_TOPICS_FOR_POLL = new KafkaDefaultTopicsForPollProperty();

    @Override
    public String[] parse(String value) {
        return value
                .trim()
                .replace(" ", "")
                .split(",");
    }
}
