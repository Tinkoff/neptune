package ru.tinkoff.qa.neptune.rabbit.mq.function.declare.exchange;

import com.rabbitmq.client.Channel;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotations.MaxDepthOfReporting;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialActionSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;
import ru.tinkoff.qa.neptune.rabbit.mq.RabbitMqStepContext;

import java.io.IOException;

@SequentialActionSupplier.DefinePerformImperativeParameterName("Declare:")
@MaxDepthOfReporting(0)
public class RabbitMqExchangeDeclareSupplier extends SequentialActionSupplier<RabbitMqStepContext, Channel, RabbitMqExchangeDeclareSupplier> {

    private final String exchange;
    private final String type;
    private final ParametersForDeclareExchange params;

    protected RabbitMqExchangeDeclareSupplier(String exchange, String type, ParametersForDeclareExchange params) {
        this.params = params;
        this.exchange = exchange;
        this.type = type;
        performOn(RabbitMqStepContext::getChannel);
    }

    @Description("exchange '{exchange}' passively")
    public static RabbitMqExchangeDeclareSupplier exchangeDeclarePassive(@DescriptionFragment("exchange") String exchange) {
        return new RabbitMqExchangeDeclareSupplier(exchange, null, null);
    }

    @Description("exchange '{exchange}', type = '{type}'. Non-autodelete, non-durable and no extra arguments")
    public static RabbitMqExchangeDeclareSupplier exchangeDeclare(@DescriptionFragment("exchange") String exchange, @DescriptionFragment("type") String type) {
        return new RabbitMqExchangeDeclareSupplier(exchange, type, null);
    }

    @Description("exchange '{exchange}', type = '{type}'")
    public static RabbitMqExchangeDeclareSupplier exchangeDeclare(@DescriptionFragment("exchange") String exchange, @DescriptionFragment("type") String type, ParametersForDeclareExchange params) {
        return new RabbitMqExchangeDeclareSupplier(exchange, type, params);
    }

    @Override
    protected void howToPerform(Channel value) {
        try {
            if (params == null && type == null) {
                value.exchangeDeclarePassive(exchange);
                return;
            }
            if (params == null) {
                value.exchangeDeclare(exchange,
                        type,
                        false,
                        false,
                        false,
                        null);
                return;
            }
            value.exchangeDeclare(exchange,
                    type,
                    params.isDurable(),
                    params.isAutoDelete(),
                    params.isInternal(),
                    params.getAdditionalArguments());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
