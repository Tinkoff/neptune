package ru.tinkoff.qa.neptune.rabbit.mq.function.delete.exchange;

import com.rabbitmq.client.Channel;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotations.MaxDepthOfReporting;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialActionSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;
import ru.tinkoff.qa.neptune.rabbit.mq.RabbitMqStepContext;

import java.io.IOException;

import static com.google.common.base.Preconditions.checkArgument;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@SequentialActionSupplier.DefinePerformImperativeParameterName("Delete:")
@MaxDepthOfReporting(0)
@Description("exchange '{exchange}', ifUnused = '{ifUnused}'")
public class RabbitMqExchangeDeleteSupplier extends SequentialActionSupplier<RabbitMqStepContext, Channel, RabbitMqExchangeDeleteSupplier> {
    @DescriptionFragment("exchange")
    private final String exchange;

    @DescriptionFragment("ifUnused")
    private final boolean ifUnused;

    public RabbitMqExchangeDeleteSupplier(String exchange, boolean ifUnused) {
        super();
        checkArgument(isNotBlank(exchange));
        this.exchange = exchange;
        this.ifUnused = ifUnused;
        performOn(RabbitMqStepContext::getChannel);
    }

    public static RabbitMqExchangeDeleteSupplier exchangeDelete(String exchange, boolean ifUnused) {
        return new RabbitMqExchangeDeleteSupplier(exchange, ifUnused);
    }

    @Override
    protected void howToPerform(Channel value) {
        try {
            value.exchangeDelete(exchange, ifUnused);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
