package ru.tinkoff.qa.neptune.rabbit.mq.function.binding;

import com.rabbitmq.client.Channel;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.MaxDepthOfReporting;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialActionSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;
import ru.tinkoff.qa.neptune.rabbit.mq.RabbitMqStepContext;

import static ru.tinkoff.qa.neptune.rabbit.mq.GetChannel.getChannel;

@SequentialActionSupplier.DefinePerformImperativeParameterName("Bind:")
@MaxDepthOfReporting(0)
@Description("{parameters}")
public final class RabbitMqBindSupplier extends SequentialActionSupplier<RabbitMqStepContext, Channel, RabbitMqBindSupplier> {

    @DescriptionFragment("parameters")
    private final BindUnbindParameters<?> parameters;

    private RabbitMqBindSupplier(BindUnbindParameters<?> parameters) {
        this.parameters = parameters;
    }

    public static RabbitMqBindSupplier bindAction(BindUnbindParameters<?> parameters) {
        return new RabbitMqBindSupplier(parameters).performOn(getChannel());
    }

    @Override
    protected void howToPerform(Channel value) {
        parameters.bind(value);
    }
}
