package ru.tinkoff.qa.neptune.kafka.properties;

import org.apache.kafka.clients.producer.Callback;
import ru.tinkoff.qa.neptune.core.api.properties.PropertyDescription;
import ru.tinkoff.qa.neptune.core.api.properties.PropertyName;
import ru.tinkoff.qa.neptune.core.api.properties.object.ObjectPropertySupplier;

import java.util.function.Supplier;

@PropertyDescription(description = "Defines default call back for the sending",
        section = "Kafka")
@PropertyName("KAFKA_CALL_BACK")
public final class KafkaCallbackProperty implements ObjectPropertySupplier<Callback, Supplier<Callback>> {
    public static final KafkaCallbackProperty KAFKA_CALLBACK = new KafkaCallbackProperty();

    private KafkaCallbackProperty() {
        super();
    }
}
