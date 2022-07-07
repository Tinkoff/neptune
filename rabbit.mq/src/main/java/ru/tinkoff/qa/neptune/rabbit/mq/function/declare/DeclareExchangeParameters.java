package ru.tinkoff.qa.neptune.rabbit.mq.function.declare;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.StepParameter;
import ru.tinkoff.qa.neptune.rabbit.mq.AdditionalArguments;
import ru.tinkoff.qa.neptune.rabbit.mq.properties.RabbitMQRoutingProperties;

import java.io.IOException;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.nonNull;
import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static ru.tinkoff.qa.neptune.rabbit.mq.properties.RabbitMQRoutingProperties.DEFAULT_EXCHANGE_NAME;

@Description("Exchange")
public final class DeclareExchangeParameters extends DeclareParameters<DeclareExchangeParameters> {

    @StepParameter("exchange")
    private final String exchange;

    @StepParameter("type")
    private String type = "direct";

    @StepParameter("internal")
    private boolean internal;

    private DeclareExchangeParameters(String exchange) {
        checkArgument(isNotBlank(exchange), "Name of the exchange should be defined");
        this.exchange = exchange;
    }

    /**
     * Declares an exchange
     *
     * @param exchange is a name of exchange
     * @return a nwe {@link DeclareExchangeParameters}
     */
    public static DeclareExchangeParameters newExchange(String exchange) {
        return new DeclareExchangeParameters(exchange);
    }

    /**
     * Declares an exchange. Value of the {@link RabbitMQRoutingProperties#DEFAULT_EXCHANGE_NAME} is used
     * as name of the exchange
     *
     * @return a nwe {@link DeclareExchangeParameters}
     */
    public static DeclareExchangeParameters newExchange() {
        return newExchange(DEFAULT_EXCHANGE_NAME.get());
    }

    /**
     * Defines the type of exchange
     *
     * @param type is a type of exchange
     * @return self-reference
     */
    public DeclareExchangeParameters type(String type) {
        checkArgument(isNotBlank(type), "Name of the type of exchange should be defined");
        this.type = type;
        return this;
    }

    /**
     * Defines the type of exchange
     *
     * @param type is a type of exchange
     * @return self-reference
     */
    public DeclareExchangeParameters type(BuiltinExchangeType type) {
        checkArgument(nonNull(type), "Type of exchange should be defined");
        return type(type.getType());
    }

    /**
     * Makes new exchange autoDelete
     *
     * @return self-reference
     */
    public DeclareExchangeParameters internal() {
        this.internal = true;
        return this;
    }

    @Override
    void declare(Channel channel) {
        try {
            if (isPassive()) {
                channel.exchangeDeclarePassive(exchange);
                return;
            }

            channel.exchangeDeclare(exchange,
                    type,
                    isDurable(),
                    isAutoDelete(),
                    internal,
                    ofNullable(arguments).map(AdditionalArguments::getHashMap).orElse(null));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
