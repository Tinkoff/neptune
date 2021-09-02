package ru.tinkoff.qa.neptune.rabbit.mq.function.binding;

import com.rabbitmq.client.Channel;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;
import ru.tinkoff.qa.neptune.rabbit.mq.properties.RabbitMQRoutingProperties;

import java.io.IOException;

import static com.google.common.base.Preconditions.checkArgument;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static ru.tinkoff.qa.neptune.rabbit.mq.function.binding.BindUnbindParameters.ROUTING_KEY;
import static ru.tinkoff.qa.neptune.rabbit.mq.properties.RabbitMQRoutingProperties.DEFAULT_EXCHANGE_NAME;
import static ru.tinkoff.qa.neptune.rabbit.mq.properties.RabbitMQRoutingProperties.DEFAULT_QUEUE_NAME;

@Description("queue = '{queue}', exchange = '{exchange}', routingKey = '{" + ROUTING_KEY + "}'")
public final class QueueBindUnbindParameters extends BindUnbindParameters<QueueBindUnbindParameters> {

    @DescriptionFragment("queue")
    private final String queue;

    @DescriptionFragment("exchange")
    private final String exchange;

    private QueueBindUnbindParameters(String queue, String exchange) {
        checkArgument(isNotBlank(queue), "Name of the queue should be defined");
        checkArgument(isNotBlank(exchange), "Name of the exchange should be defined");
        this.queue = queue;
        this.exchange = exchange;
    }

    /**
     * Defines queue and exchange.
     *
     * @param queue    is a name of a queue
     * @param exchange is a name a exchange
     * @return a new {@link QueueBindUnbindParameters}
     */
    public static QueueBindUnbindParameters queueAndExchange(String queue, String exchange) {
        return new QueueBindUnbindParameters(queue, exchange);
    }

    /**
     * Defines queue. Value of the {@link RabbitMQRoutingProperties#DEFAULT_EXCHANGE_NAME} is used
     * as exchange.
     *
     * @param queue is a name of a queue
     * @return a new {@link QueueBindUnbindParameters}
     */
    public static QueueBindUnbindParameters queueAndDefaultExchange(String queue) {
        return queueAndExchange(queue, DEFAULT_EXCHANGE_NAME.get());
    }

    /**
     * Defines exchange. Value of the {@link RabbitMQRoutingProperties#DEFAULT_QUEUE_NAME} is used
     * as queue.
     *
     * @param exchange is a name a exchange
     * @return a new {@link QueueBindUnbindParameters}
     */
    public static QueueBindUnbindParameters defaultQueueAndExchange(String exchange) {
        return queueAndExchange(DEFAULT_QUEUE_NAME.get(), exchange);
    }

    @Override
    void bind(Channel value) {
        try {
            if (arguments == null) {
                value.queueBind(queue, exchange, routingKey);
                return;
            }
            value.queueBind(queue, exchange, routingKey, arguments.getHashMap());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    void unbind(Channel value) {
        try {
            if (arguments == null) {
                value.queueUnbind(queue, exchange, routingKey);
                return;
            }
            value.queueUnbind(queue, exchange, routingKey, arguments.getHashMap());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
