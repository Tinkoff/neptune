package ru.tinkoff.qa.neptune.kafka.properties;

import ru.tinkoff.qa.neptune.core.api.properties.PropertyDescription;
import ru.tinkoff.qa.neptune.core.api.properties.PropertyName;
import ru.tinkoff.qa.neptune.core.api.properties.string.StringValuePropertySupplier;

@PropertyDescription(description = "Defines default topic for send",
        section = "Kafka")
@PropertyName("DEFAULT_TOPIC_FOR_SEND")
public final class KafkaDefaultTopicForSendSupplier implements StringValuePropertySupplier {
    public static final KafkaDefaultTopicForSendSupplier DEFAULT_TOPIC_FOR_SEND = new KafkaDefaultTopicForSendSupplier();
}
