package ru.tinkoff.qa.neptune.rabbit.mq.function.declare.queue;

import com.rabbitmq.client.Channel;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotations.MaxDepthOfReporting;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialActionSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;
import ru.tinkoff.qa.neptune.rabbit.mq.RabbitMqStepContext;

import java.io.IOException;

@SequentialActionSupplier.DefinePerformImperativeParameterName("Declare:")
@MaxDepthOfReporting(0)
public class RabbitMqQueueDeclareSupplier extends SequentialActionSupplier<RabbitMqStepContext, Channel, RabbitMqQueueDeclareSupplier> {

    private final String queue;
    private final ParametersForDeclareQueue params;

    protected RabbitMqQueueDeclareSupplier(String queue, ParametersForDeclareQueue params) {
        this.params = params;
        this.queue = queue;
        performOn(RabbitMqStepContext::getChannel);
    }

    @Description("server-named queue. autodelete, non-durable")
    public static RabbitMqQueueDeclareSupplier queueDeclare() {
        return new RabbitMqQueueDeclareSupplier(null, null);
    }

    @Description("queue '{queue}' passively")
    public static RabbitMqQueueDeclareSupplier queueDeclarePassive(@DescriptionFragment("queue") String queue) {
        return new RabbitMqQueueDeclareSupplier(queue, null);
    }

    @Description("queue '{queue}'")
    public static RabbitMqQueueDeclareSupplier queueDeclare(@DescriptionFragment("queue") String queue, ParametersForDeclareQueue params) {
        return new RabbitMqQueueDeclareSupplier(queue, params);
    }

    @Override
    protected void howToPerform(Channel value) {
        try {
            if (queue == null) {
                value.queueDeclare();
            } else {
                if (params == null) {
                    value.queueDeclarePassive(queue);
                    return;
                }
                value.queueDeclare(queue,
                        params.isDurable(),
                        params.isExclusive(),
                        params.isAutoDelete(),
                        params.getAdditionalArguments());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
