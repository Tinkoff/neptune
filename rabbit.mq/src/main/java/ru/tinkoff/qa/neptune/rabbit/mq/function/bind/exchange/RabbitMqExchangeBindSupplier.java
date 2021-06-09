package ru.tinkoff.qa.neptune.rabbit.mq.function.bind.exchange;

import com.google.common.base.Supplier;
import com.rabbitmq.client.AMQP;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotations.MaxDepthOfReporting;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;
import ru.tinkoff.qa.neptune.rabbit.mq.AdditionalArguments;
import ru.tinkoff.qa.neptune.rabbit.mq.RabbitMqStepContext;

import java.io.IOException;

import static com.google.common.base.Preconditions.checkArgument;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@SequentialGetStepSupplier.DefineGetImperativeParameterName("Bind:")
@MaxDepthOfReporting(0)
public class RabbitMqExchangeBindSupplier extends SequentialGetStepSupplier.GetObjectStepSupplier<RabbitMqStepContext, AMQP.Exchange.BindOk, RabbitMqExchangeBindSupplier> {

    private AdditionalArguments arguments;
    private final ArgSupplier supplier;

    protected RabbitMqExchangeBindSupplier(String destination, String source, String routingKey, ArgSupplier supplier) {
        super(input -> {
            var args = supplier.get();
            try {
                if (args == null) {
                    return input.getChannel().exchangeBind(destination, source, routingKey);
                }
                return input.getChannel().exchangeBind(destination, source, routingKey, args.getHashMap());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        this.supplier = supplier;
    }

    public RabbitMqExchangeBindSupplier setArguments(AdditionalArguments arguments) {
        this.arguments = arguments;
        supplier.setArgs(arguments);
        return this;
    }

    @Description("an exchange to an exchange, with no extra arguments.\r\n" +
            "Params:\r\n" +
            "{destination} – the name of the exchange to which messages flow across the binding\r\n" +
            "{source} – the name of the exchange from which messages flow across the binding\r\n" +
            "{routingKey} – the routing key to use for the binding")
    public static RabbitMqExchangeBindSupplier exchangeBind(@DescriptionFragment("destination") String destination,
                                                            @DescriptionFragment("source") String source,
                                                            @DescriptionFragment("routingKey") String routingKey) {

        checkArgument(isNotBlank(destination));
        checkArgument(isNotBlank(source));
        checkArgument(isNotBlank(routingKey));

        return new RabbitMqExchangeBindSupplier(destination, source, routingKey, new ArgSupplier());
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
