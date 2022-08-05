package ru.tinkoff.qa.neptune.rabbit.mq.function.delete;

import com.rabbitmq.client.Channel;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.StepParameter;
import ru.tinkoff.qa.neptune.rabbit.mq.properties.RabbitMQRoutingProperties;

import java.io.IOException;

import static com.google.common.base.Preconditions.checkArgument;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static ru.tinkoff.qa.neptune.rabbit.mq.properties.RabbitMQRoutingProperties.DEFAULT_EXCHANGE_NAME;

@Description("Exchange")
public final class ExchangeDeleteParameters extends DeleteParameters<ExchangeDeleteParameters> {

    @StepParameter("exchange")
    private final String exchange;

    private ExchangeDeleteParameters(String exchange) {
        checkArgument(isNotBlank(exchange), "Name of the exchange should be defined");
        this.exchange = exchange;
    }

    /**
     * Defines the exchange to delete.
     *
     * @param exchange name of exchange to delete
     * @return a new instance of {@link ExchangeDeleteParameters}
     */
    public static ExchangeDeleteParameters exchange(String exchange) {
        return new ExchangeDeleteParameters(exchange);
    }

    /**
     * Defines the exchange to delete. Value of the {@link RabbitMQRoutingProperties#DEFAULT_EXCHANGE_NAME} is used
     * as name of the exchange.
     *
     * @return a new instance of {@link ExchangeDeleteParameters}
     */
    public static ExchangeDeleteParameters exchange() {
        return exchange(DEFAULT_EXCHANGE_NAME.get());
    }

    @Override
    void delete(Channel channel) {
        try {
            channel.exchangeDelete(exchange, isIfUnused());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
