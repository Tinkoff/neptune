package ru.tinkoff.qa.neptune.rabbit.mq.function.purge;

import com.rabbitmq.client.Channel;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotations.MaxDepthOfReporting;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialActionSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;
import ru.tinkoff.qa.neptune.rabbit.mq.RabbitMqStepContext;

import java.io.IOException;

import static com.google.common.base.Preconditions.checkArgument;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@MaxDepthOfReporting(0)
@Description("Purge the contents of the queue - {queue}.")
public class RabbitMqPurgeQueueSupplier extends SequentialActionSupplier<RabbitMqStepContext, Channel, RabbitMqPurgeQueueSupplier> {
    @DescriptionFragment("queue")
    private final String queue;

    public RabbitMqPurgeQueueSupplier(String queue) {
        super();
        checkArgument(isNotBlank(queue));
        this.queue = queue;
        performOn(RabbitMqStepContext::getChannel);
    }

    public static RabbitMqPurgeQueueSupplier purgeQueue(String queue) {
        return new RabbitMqPurgeQueueSupplier(queue);
    }

    @Override
    protected void howToPerform(Channel value) {
        try {
            value.queuePurge(queue);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
