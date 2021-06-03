package ru.tinkoff.qa.neptune.rabbit.mq;

import ru.tinkoff.qa.neptune.core.api.steps.context.ParameterProvider;

import static ru.tinkoff.qa.neptune.rabbit.mq.properties.RabbitMqClusterProperty.RABBIT_MQ_CLUSTER_PROPERTY;
import static ru.tinkoff.qa.neptune.rabbit.mq.properties.RabbitMqPasswordProperty.RABBIT_MQ_PASSWORD_PROPERTY;
import static ru.tinkoff.qa.neptune.rabbit.mq.properties.RabbitMqUserNameProperty.RABBIT_MQ_USERNAME_PROPERTY;

public class RabbitMqParameterProvider implements ParameterProvider {
    @Override
    public Object[] provide() {
        return new Object[]{
                RABBIT_MQ_CLUSTER_PROPERTY.get(),
                RABBIT_MQ_USERNAME_PROPERTY.get(),
                RABBIT_MQ_PASSWORD_PROPERTY.get()
        };
    }
}
