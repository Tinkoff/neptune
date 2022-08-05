package ru.tinkoff.qa.neptune.rabbit.mq.function.declare;

import com.rabbitmq.client.Channel;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.MaxDepthOfReporting;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialActionSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;
import ru.tinkoff.qa.neptune.rabbit.mq.RabbitMqStepContext;

import static ru.tinkoff.qa.neptune.rabbit.mq.GetChannel.getChannel;

@SequentialActionSupplier.DefinePerformImperativeParameterName("Declare:")
@MaxDepthOfReporting(0)
@Description("{parameters}")
public final class DeclareActionSupplier extends SequentialActionSupplier<RabbitMqStepContext, Channel, DeclareActionSupplier> {

    @DescriptionFragment("parameters")
    private final DeclareParameters<?> parameters;

    private DeclareActionSupplier(DeclareParameters<?> parameters) {
        this.parameters = parameters;
    }

    public static DeclareActionSupplier declareAction(DeclareParameters<?> parameters) {
        return new DeclareActionSupplier(parameters).performOn(getChannel());
    }

    @Override
    protected void howToPerform(Channel value) {
        parameters.declare(value);
    }
}
