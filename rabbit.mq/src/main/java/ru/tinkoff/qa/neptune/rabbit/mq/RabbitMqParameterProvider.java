package ru.tinkoff.qa.neptune.rabbit.mq;

import ru.tinkoff.qa.neptune.core.api.steps.context.ParameterProvider;

import static ru.tinkoff.qa.neptune.rabbit.mq.properties.RabbitMqAuthorizationProperties.RABBIT_MQ_PASSWORD;
import static ru.tinkoff.qa.neptune.rabbit.mq.properties.RabbitMqAuthorizationProperties.RABBIT_MQ_USERNAME;
import static ru.tinkoff.qa.neptune.rabbit.mq.properties.RabbitMqClusterProperty.RABBIT_MQ_CLUSTER_PROPERTY;

public class RabbitMqParameterProvider implements ParameterProvider {
    @Override
    public Object[] provide() {
        return new Object[]{
                RABBIT_MQ_CLUSTER_PROPERTY.get(),
                RABBIT_MQ_USERNAME.get(),
                RABBIT_MQ_PASSWORD.get()
        };
    }
}
