package ru.tinkoff.qa.neptune.rabbit.mq.function.declare.exchange;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;
import ru.tinkoff.qa.neptune.rabbit.mq.RabbitMqStepContext;

import java.io.IOException;

public class RabbitMqExchangeDeclareSupplier extends SequentialGetStepSupplier.GetObjectChainedStepSupplier<RabbitMqStepContext, AMQP.Exchange.DeclareOk, Channel, RabbitMqExchangeDeclareSupplier> {
    private final String exchange;
    private final String type;
    private final ParametersForDeclareExchange params;


    protected RabbitMqExchangeDeclareSupplier(String exchange, String type, ParametersForDeclareExchange params) {
        super(
                channel -> {
                    if (params == null && type == null) {
                        try {
                            return channel.exchangeDeclarePassive(exchange);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    try {
                        return channel.exchangeDeclare(exchange,
                                type,
                                params.isDurable(),
                                params.isAutoDelete(),
                                params.isInternal(),
                                params.getAdditionalArguments().getHashMap());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
        );
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
