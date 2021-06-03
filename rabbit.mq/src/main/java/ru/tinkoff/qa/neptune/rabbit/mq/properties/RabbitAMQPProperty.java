package ru.tinkoff.qa.neptune.rabbit.mq.properties;

import com.rabbitmq.client.AMQP;
import ru.tinkoff.qa.neptune.core.api.properties.PropertyDescription;
import ru.tinkoff.qa.neptune.core.api.properties.PropertyName;
import ru.tinkoff.qa.neptune.core.api.properties.object.ObjectPropertySupplier;

import java.util.function.Supplier;

@PropertyDescription(description = "Defines AMQP.BasicProperties.Builder",
        section = "RabbitMQ")
@PropertyName("RABBIT_AMQP_PROPERTY")
public class RabbitAMQPProperty implements ObjectPropertySupplier<AMQP.BasicProperties.Builder,Supplier<AMQP.BasicProperties.Builder>> {
    public static final RabbitAMQPProperty RABBIT_AMQP_PROPERTY= new RabbitAMQPProperty();

    public RabbitAMQPProperty() { super(); }
}
