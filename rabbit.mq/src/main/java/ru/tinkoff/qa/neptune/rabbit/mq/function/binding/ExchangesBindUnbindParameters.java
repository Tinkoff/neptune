package ru.tinkoff.qa.neptune.rabbit.mq.function.binding;

import com.rabbitmq.client.Channel;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;
import ru.tinkoff.qa.neptune.rabbit.mq.properties.RabbitMQRoutingProperties;

import java.io.IOException;

import static com.google.common.base.Preconditions.checkArgument;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static ru.tinkoff.qa.neptune.rabbit.mq.function.binding.BindUnbindParameters.ROUTING_KEY;
import static ru.tinkoff.qa.neptune.rabbit.mq.properties.RabbitMQRoutingProperties.DEFAULT_EXCHANGE_NAME;

@Description("destination exchange = '{destination}', source exchange = '{source}', routingKey = '{" + ROUTING_KEY + "}'")
public final class ExchangesBindUnbindParameters extends BindUnbindParameters<ExchangesBindUnbindParameters> {

    @DescriptionFragment("source")
    private final String source;

    @DescriptionFragment("destination")
    private final String destination;

    private ExchangesBindUnbindParameters(String source, String destination) {
        checkArgument(isNotBlank(source), "Name of the source should be defined");
        checkArgument(isNotBlank(destination), "Name of the destination should be defined");
        this.source = source;
        this.destination = destination;
    }

    /**
     * Defines source and destination exchanges.
     *
     * @param source      is a name of source exchanges
     * @param destination is a name of destination exchanges
     * @return a new {@link ExchangesBindUnbindParameters}
     */
    public static ExchangesBindUnbindParameters exchanges(String source, String destination) {
        return new ExchangesBindUnbindParameters(source, destination);
    }

    /**
     * Defines source exchange. Value of the {@link RabbitMQRoutingProperties#DEFAULT_EXCHANGE_NAME} is used
     * as destination.
     *
     * @param source is a name of source exchanges
     * @return a new {@link ExchangesBindUnbindParameters}
     */
    public static ExchangesBindUnbindParameters sourceExchange(String source) {
        return exchanges(source, DEFAULT_EXCHANGE_NAME.get());
    }

    /**
     * Defines destination exchange. Value of the {@link RabbitMQRoutingProperties#DEFAULT_EXCHANGE_NAME} is used
     * as source.
     *
     * @param destination is a name of destination exchanges
     * @return a new {@link ExchangesBindUnbindParameters}
     */
    public static ExchangesBindUnbindParameters destinationExchange(String destination) {
        return exchanges(DEFAULT_EXCHANGE_NAME.get(), destination);
    }

    @Override
    void bind(Channel value) {
        try {
            if (arguments == null) {
                value.exchangeBind(destination, source, routingKey);
                return;
            }
            value.exchangeBind(destination, source, routingKey, arguments.getHashMap());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    void unbind(Channel value) {
        try {
            if (arguments == null) {
                value.exchangeUnbind(destination, source, routingKey);
                return;
            }
            value.exchangeUnbind(destination, source, routingKey, arguments.getHashMap());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
