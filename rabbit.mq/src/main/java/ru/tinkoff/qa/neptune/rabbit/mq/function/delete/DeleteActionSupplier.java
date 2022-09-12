package ru.tinkoff.qa.neptune.rabbit.mq.function.delete;

import com.rabbitmq.client.Channel;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.MaxDepthOfReporting;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialActionSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;
import ru.tinkoff.qa.neptune.rabbit.mq.RabbitMqStepContext;

import static ru.tinkoff.qa.neptune.rabbit.mq.GetChannel.getChannel;

@SequentialActionSupplier.DefinePerformImperativeParameterName("Delete:")
@MaxDepthOfReporting(0)
@Description("{parameters}")
public final class DeleteActionSupplier extends SequentialActionSupplier<RabbitMqStepContext, Channel, DeleteActionSupplier> {

    @DescriptionFragment("parameters")
    private final DeleteParameters<?> parameters;

    private DeleteActionSupplier(DeleteParameters<?> parameters) {
        this.parameters = parameters;
    }

    public static DeleteActionSupplier deleteAction(DeleteParameters<?> parameters) {
        return new DeleteActionSupplier(parameters).performOn(getChannel());
    }

    @Override
    protected void howToPerform(Channel value) {
        parameters.delete(value);
    }
}
