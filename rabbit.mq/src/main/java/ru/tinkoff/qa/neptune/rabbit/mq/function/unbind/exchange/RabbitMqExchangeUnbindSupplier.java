package ru.tinkoff.qa.neptune.rabbit.mq.function.unbind.exchange;

import com.google.common.base.Supplier;
import com.rabbitmq.client.AMQP;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;
import ru.tinkoff.qa.neptune.rabbit.mq.AdditionalArguments;
import ru.tinkoff.qa.neptune.rabbit.mq.RabbitMqStepContext;

import java.io.IOException;


public class RabbitMqExchangeUnbindSupplier extends SequentialGetStepSupplier.GetObjectStepSupplier<RabbitMqStepContext, AMQP.Exchange.UnbindOk, RabbitMqExchangeUnbindSupplier> {
    private AdditionalArguments arguments;
    private final ArgSupplier supplier;

    protected RabbitMqExchangeUnbindSupplier(String destination, String source, String routingKey, ArgSupplier supplier) {
        super(input -> {
            var args = supplier.get();
            try {
                if (args == null) {
                    return input.getChannel().exchangeUnbind(destination, source, routingKey);
                }
                return input.getChannel().exchangeUnbind(destination, source, routingKey, args.getHashMap());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        this.supplier = supplier;
    }

    public RabbitMqExchangeUnbindSupplier setArguments(AdditionalArguments arguments) {
        this.arguments = arguments;
        return this;
    }

    @Description("Unbind an exchange from an exchange, with no extra arguments.\r\n" +
            "Params:\n" +
            "{destination} – the name of the exchange to which messages flow across the binding\r\n" +
            "{source} – the name of the exchange from which messages flow across the binding\r\n" +
            "{routingKey} – the routing key to use for the binding")
    public static RabbitMqExchangeUnbindSupplier exchangeUnbind(@DescriptionFragment("destination") String destination,
                                                                                @DescriptionFragment("source") String source,
                                                                                @DescriptionFragment("routingKey") String routingKey) {
        return new RabbitMqExchangeUnbindSupplier(destination, source, routingKey, new ArgSupplier());
    }

    private static final class ArgSupplier implements Supplier<AdditionalArguments> {

        private AdditionalArguments args;

        @Override
        public AdditionalArguments get() {
            return args;
        }

        ArgSupplier setArgs(AdditionalArguments args) {
            this.args = args;
            return this;
        }
    }
}
