package ru.tinkoff.qa.neptune.rabbit.mq.function.declare;

import com.rabbitmq.client.Channel;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.StepParameter;
import ru.tinkoff.qa.neptune.rabbit.mq.AdditionalArguments;
import ru.tinkoff.qa.neptune.rabbit.mq.properties.RabbitMQRoutingProperties;

import java.io.IOException;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static ru.tinkoff.qa.neptune.rabbit.mq.properties.RabbitMQRoutingProperties.DEFAULT_QUEUE_NAME;

@Description("Queue")
public final class DeclareQueueParameters extends DeclareParameters<DeclareQueueParameters> {

    @StepParameter("queue")
    private final String queue;

    @StepParameter("exclusive")
    private boolean exclusive;

    private DeclareQueueParameters(String queue) {
        checkArgument(isNotBlank(queue), "Name of the queue should be defined");
        this.queue = queue;
    }

    /**
     * Declares a queue
     *
     * @param queue is a name of queue
     * @return a nwe {@link DeclareQueueParameters}
     */
    public static DeclareQueueParameters newQueue(String queue) {
        return new DeclareQueueParameters(queue);
    }

    /**
     * Declares a queue. Value of the {@link RabbitMQRoutingProperties#DEFAULT_QUEUE_NAME} is used
     * as name of the queue
     *
     * @return a nwe {@link DeclareQueueParameters}
     */
    public static DeclareQueueParameters newQueue() {
        return newQueue(DEFAULT_QUEUE_NAME.get());
    }

    /**
     * Makes new queue exclusive
     *
     * @return self-reference
     */
    public DeclareQueueParameters exclusive() {
        this.exclusive = true;
        return this;
    }

    @Override
    void declare(Channel channel) {
        try {
            if (isPassive()) {
                channel.queueDeclarePassive(queue);
                return;
            }
            channel.queueDeclare(queue,
                    isDurable(),
                    exclusive,
                    isAutoDelete(),
                    ofNullable(arguments).map(AdditionalArguments::getHashMap).orElse(null));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
