package ru.tinkoff.qa.neptune.rabbit.mq.function.declare.queue;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;
import ru.tinkoff.qa.neptune.rabbit.mq.RabbitMqStepContext;

import java.io.IOException;

public class RabbitMqQueueDeclareSupplier extends SequentialGetStepSupplier.GetObjectChainedStepSupplier<RabbitMqStepContext, AMQP.Queue.DeclareOk, Channel, RabbitMqQueueDeclareSupplier> {
    private final String queue;
    private final ParametersForDeclareQueue params;

    protected RabbitMqQueueDeclareSupplier(String queue, ParametersForDeclareQueue params) {
        super(channel -> {
            if (queue == null) {
                try {
                    return channel.queueDeclare();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else {
                if (params == null) {
                    try {
                        return channel.queueDeclarePassive(queue);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                try {
                    return channel.queueDeclare(queue,
                            params.isDurable(),
                            params.isExclusive(),
                            params.isAutoDelete(),
                            params.getAdditionalArguments().getHashMap());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        this.params = params;
        this.queue = queue;
    }

    @Description("Actively declare a server-named exclusive, autodelete, non-durable queue.")
    public static RabbitMqQueueDeclareSupplier queueDeclare() {
        return new RabbitMqQueueDeclareSupplier(null, null);
    }

    @Description("Declare a queue {queue} passively.")
    public static RabbitMqQueueDeclareSupplier queueDeclarePassive(@DescriptionFragment("queue") String queue) {
        return new RabbitMqQueueDeclareSupplier(queue, null);
    }

    @Description("Declare a queue - {queue}")
    public static RabbitMqQueueDeclareSupplier queueDeclare(@DescriptionFragment("queue") String queue, ParametersForDeclareQueue params) {
        return new RabbitMqQueueDeclareSupplier(queue, params);
    }
}
