package ru.tinkoff.qa.neptune.rabbit.mq.function.declare;

import ru.tinkoff.qa.neptune.core.api.event.firing.annotations.MaxDepthOfReporting;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.rabbit.mq.RabbitMqStepContext;

import java.io.IOException;

@SequentialGetStepSupplier.DefineGetImperativeParameterName("Declare:")
@MaxDepthOfReporting(0)
@Description("Server named queue")
public final class ServerNamedQueueSequentialGetSupplier
        extends SequentialGetStepSupplier.GetSimpleStepSupplier<RabbitMqStepContext, String, ServerNamedQueueSequentialGetSupplier> {

    private ServerNamedQueueSequentialGetSupplier() {
        super(rabbitMqStepContext -> {
            try {
                return rabbitMqStepContext.getChannel().queueDeclare().getQueue();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public static ServerNamedQueueSequentialGetSupplier newQueueServerNamed() {
        return new ServerNamedQueueSequentialGetSupplier();
    }
}
