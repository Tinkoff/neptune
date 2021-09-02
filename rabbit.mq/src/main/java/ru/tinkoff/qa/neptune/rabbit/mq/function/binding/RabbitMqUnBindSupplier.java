package ru.tinkoff.qa.neptune.rabbit.mq.function.binding;

import com.rabbitmq.client.Channel;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotations.MaxDepthOfReporting;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialActionSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;
import ru.tinkoff.qa.neptune.rabbit.mq.RabbitMqStepContext;

@SequentialActionSupplier.DefinePerformImperativeParameterName("Unbind:")
@MaxDepthOfReporting(0)
@Description("{parameters}")
public final class RabbitMqUnBindSupplier extends SequentialActionSupplier<RabbitMqStepContext, Channel, RabbitMqUnBindSupplier> {

    @DescriptionFragment("parameters")
    private final BindUnbindParameters<?> parameters;

    private RabbitMqUnBindSupplier(BindUnbindParameters<?> parameters) {
        this.parameters = parameters;
    }

    public static RabbitMqUnBindSupplier unBindAction(BindUnbindParameters<?> parameters) {
        return new RabbitMqUnBindSupplier(parameters).performOn(RabbitMqStepContext::getChannel);
    }

    @Override
    protected void howToPerform(Channel value) {
        parameters.unbind(value);
    }
}
