package ru.tinkoff.qa.neptune.rabbit.mq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import ru.tinkoff.qa.neptune.core.api.steps.context.ParameterProvider;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import static ru.tinkoff.qa.neptune.rabbit.mq.properties.RabbitMqAuthorizationProperties.RABBIT_MQ_PASSWORD;
import static ru.tinkoff.qa.neptune.rabbit.mq.properties.RabbitMqAuthorizationProperties.RABBIT_MQ_USERNAME;
import static ru.tinkoff.qa.neptune.rabbit.mq.properties.RabbitMqClusterProperty.RABBIT_MQ_CLUSTER_PROPERTY;

public class RabbitMqParameterProvider implements ParameterProvider {
    @Override
    public Object[] provide() {
        Connection connection = null;
        Channel channel = null;
        ConnectionFactory factory = new ConnectionFactory();
        factory.setUsername(RABBIT_MQ_USERNAME.get());
        factory.setPassword(RABBIT_MQ_PASSWORD.get());

        try {
            connection = factory.newConnection(RABBIT_MQ_CLUSTER_PROPERTY.get());
            channel = connection.createChannel();
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        }

        return new Object[]{channel};
    }
}
