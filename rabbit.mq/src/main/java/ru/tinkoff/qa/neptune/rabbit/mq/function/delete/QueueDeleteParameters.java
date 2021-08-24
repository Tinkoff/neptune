package ru.tinkoff.qa.neptune.rabbit.mq.function.delete;

import com.rabbitmq.client.Channel;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.StepParameter;
import ru.tinkoff.qa.neptune.rabbit.mq.properties.RabbitMQRoutingProperties;

import java.io.IOException;

import static com.google.common.base.Preconditions.checkArgument;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static ru.tinkoff.qa.neptune.rabbit.mq.properties.RabbitMQRoutingProperties.DEFAULT_QUEUE_NAME;

@Description("Queue '{queue}'")
public final class QueueDeleteParameters extends DeleteParameters<QueueDeleteParameters> {

    @DescriptionFragment("queue")
    private final String queue;
    @StepParameter("ifEmpty")
    private boolean ifEmpty;

    private QueueDeleteParameters(String queue) {
        checkArgument(isNotBlank(queue), "Name of the queue should be defined");
        this.queue = queue;
    }

    /**
     * Defines the queue to delete.
     *
     * @param queue name of queue to delete
     * @return a new instance of {@link QueueDeleteParameters}
     */
    public static QueueDeleteParameters queue(String queue) {
        return new QueueDeleteParameters(queue);
    }

    /**
     * Defines the queue to delete. Value of the {@link RabbitMQRoutingProperties#DEFAULT_QUEUE_NAME} is used
     * as name of the queue.
     *
     * @return a new instance of {@link QueueDeleteParameters}
     */
    public static QueueDeleteParameters queue() {
        return queue(DEFAULT_QUEUE_NAME.get());
    }

    @Override
    void delete(Channel channel) {
        try {
            if (!ifEmpty && !isIfUnused()) {
                channel.queueDelete(queue);
            } else {
                channel.queueDelete(queue, isIfUnused(), ifEmpty);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Sets a flag which means to delete queue if it is empty.
     *
     * @return self-reference
     */
    public QueueDeleteParameters ifEmpty() {
        this.ifEmpty = true;
        return this;
    }
}
