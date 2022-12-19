package ru.tinkoff.qa.neptune.rabbit.mq.function.declare;

import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.MaxDepthOfReporting;
import ru.tinkoff.qa.neptune.rabbit.mq.RabbitMqStepContext;

import java.io.IOException;

import static ru.tinkoff.qa.neptune.rabbit.mq.GetChannel.getChannel;

@SequentialGetStepSupplier.DefineGetImperativeParameterName("Declare in RabbitMQ:")
@MaxDepthOfReporting(0)
@Description("Server named queue")
public final class ServerNamedQueueSequentialGetSupplier
    extends SequentialGetStepSupplier.GetSimpleStepSupplier<RabbitMqStepContext, String, ServerNamedQueueSequentialGetSupplier> {

    private ServerNamedQueueSequentialGetSupplier() {
        super(getChannel().andThen(channel -> {
            try {
                return channel.queueDeclare().getQueue();
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }));
    }

    public static ServerNamedQueueSequentialGetSupplier newQueueServerNamed() {
        return new ServerNamedQueueSequentialGetSupplier();
    }
}
