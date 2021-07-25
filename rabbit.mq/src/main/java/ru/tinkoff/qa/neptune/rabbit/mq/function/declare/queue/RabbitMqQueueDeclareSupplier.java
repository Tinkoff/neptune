package ru.tinkoff.qa.neptune.rabbit.mq.function.declare.queue;

import com.rabbitmq.client.AMQP;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotations.MaxDepthOfReporting;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;
import ru.tinkoff.qa.neptune.rabbit.mq.RabbitMqStepContext;

import java.io.IOException;

@SequentialGetStepSupplier.DefineGetImperativeParameterName("Declare:")
@MaxDepthOfReporting(0)
public class RabbitMqQueueDeclareSupplier extends SequentialGetStepSupplier.GetObjectStepSupplier<RabbitMqStepContext, AMQP.Queue.DeclareOk, RabbitMqQueueDeclareSupplier> {
    private final String queue;
    private final ParametersForDeclareQueue params;

    protected RabbitMqQueueDeclareSupplier(String queue, ParametersForDeclareQueue params) {
        super(input -> {
            var channel = input.getChannel();
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
                            params.getAdditionalArguments());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        this.params = params;
        this.queue = queue;
    }

    @Description("server-named exclusive, autodelete, non-durable queue.")
    public static RabbitMqQueueDeclareSupplier queueDeclare() {
        return new RabbitMqQueueDeclareSupplier(null, null);
    }

    @Description("queue {queue} passively.")
    public static RabbitMqQueueDeclareSupplier queueDeclarePassive(@DescriptionFragment("queue") String queue) {
        return new RabbitMqQueueDeclareSupplier(queue, null);
    }

    @Description("queue - {queue}")
    public static RabbitMqQueueDeclareSupplier queueDeclare(@DescriptionFragment("queue") String queue, ParametersForDeclareQueue params) {
        return new RabbitMqQueueDeclareSupplier(queue, params);
    }
}
