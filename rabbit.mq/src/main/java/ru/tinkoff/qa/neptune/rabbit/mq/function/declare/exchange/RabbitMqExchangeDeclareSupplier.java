package ru.tinkoff.qa.neptune.rabbit.mq.function.declare.exchange;

import com.rabbitmq.client.AMQP;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotations.MaxDepthOfReporting;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;
import ru.tinkoff.qa.neptune.rabbit.mq.RabbitMqStepContext;

import java.io.IOException;

@MaxDepthOfReporting(0)
public class RabbitMqExchangeDeclareSupplier extends SequentialGetStepSupplier.GetObjectStepSupplier<RabbitMqStepContext, AMQP.Exchange.DeclareOk, RabbitMqExchangeDeclareSupplier> {
    private final String exchange;
    private final String type;
    private final ParametersForDeclareExchange params;

    protected RabbitMqExchangeDeclareSupplier(String exchange, String type, ParametersForDeclareExchange params) {
        super(input -> {
            var channel = input.getChannel();
            try {
                if (params == null && type == null) {
                    return channel.exchangeDeclarePassive(exchange);
                }
                if (params == null) {
                    return channel.exchangeDeclare(exchange,
                            type,
                            false,
                            false,
                            false,
                            null);
                }
                return channel.exchangeDeclare(exchange,
                        type,
                        params.isDurable(),
                        params.isAutoDelete(),
                        params.isInternal(),
                        params.getAdditionalArguments());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        });
        this.params = params;
        this.exchange = exchange;
        this.type = type;
    }

    @Description("Declare an exchange '{exchange}' passively.")
    public static RabbitMqExchangeDeclareSupplier exchangeDeclarePassive(@DescriptionFragment("exchange") String exchange) {
        return new RabbitMqExchangeDeclareSupplier(exchange, null, null);
    }

    @Description("Actively declare a non-autodelete, non-durable exchange '{exchange}' with type '{type}' and no extra arguments.")
    public static RabbitMqExchangeDeclareSupplier exchangeDeclare(@DescriptionFragment("exchange") String exchange, @DescriptionFragment("type") String type) {
        return new RabbitMqExchangeDeclareSupplier(exchange, type, null);
    }

    @Description("Actively declare a non-autodelete, non-durable exchange '{exchange}' with type '{type}' and  arguments.")
    public static RabbitMqExchangeDeclareSupplier exchangeDeclare(@DescriptionFragment("exchange") String exchange, @DescriptionFragment("type") String type, ParametersForDeclareExchange params) {
        return new RabbitMqExchangeDeclareSupplier(exchange, type, params);
    }
}
