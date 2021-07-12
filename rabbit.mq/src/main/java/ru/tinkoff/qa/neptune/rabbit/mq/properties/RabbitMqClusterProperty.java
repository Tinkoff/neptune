package ru.tinkoff.qa.neptune.rabbit.mq.properties;

import com.rabbitmq.client.Address;
import ru.tinkoff.qa.neptune.core.api.properties.PropertyDescription;
import ru.tinkoff.qa.neptune.core.api.properties.PropertyName;
import ru.tinkoff.qa.neptune.core.api.properties.PropertySupplier;

import static com.rabbitmq.client.Address.parseAddresses;

@PropertyDescription(description = "Defines RabbitMQ cluster",
        section = "RabbitMQ")
@PropertyName("RABBIT_MQ_CLUSTER")
public final class RabbitMqClusterProperty implements PropertySupplier<Address[],Address[]> {
    public static final RabbitMqClusterProperty RABBIT_MQ_CLUSTER_PROPERTY = new RabbitMqClusterProperty();

    private RabbitMqClusterProperty() {
        super();
    }

    @Override
    public Address[] parse(String value) {
        try {
            return parseAddresses(value);
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage(), e);
        }
    }
}
