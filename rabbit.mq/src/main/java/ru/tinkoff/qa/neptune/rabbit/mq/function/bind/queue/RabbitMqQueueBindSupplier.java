package ru.tinkoff.qa.neptune.rabbit.mq.function.bind.queue;

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
public class RabbitMqQueueBindSupplier extends SequentialGetStepSupplier.GetObjectStepSupplier<RabbitMqStepContext, AMQP.Queue.BindOk, RabbitMqQueueBindSupplier> {
    private AdditionalArguments arguments;
    private final ArgSupplier supplier;

    protected RabbitMqQueueBindSupplier(String queue, String exchange, String routingKey, ArgSupplier supplier) {
        super(input->{
            var args = supplier.get();
            try {
                if (args == null) {
                    return input.getChannel().queueBind(queue, exchange, routingKey);
                }
                return input.getChannel().queueBind(queue, exchange, routingKey, args.getHashMap());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        this.supplier = supplier;
    }

    public RabbitMqQueueBindSupplier setArguments(AdditionalArguments arguments) {
        this.arguments = arguments;
        supplier.setArgs(arguments);
        return this;
    }

    @Description("a queue to an exchange, with no extra arguments.\r\n" +
            "Params:\r\n" +
            "queue – {queue}\r\n" +
            "exchange – {exchange}\r\n" +
            "routingKey – {routingKey}")
    public static RabbitMqQueueBindSupplier queueBind(@DescriptionFragment("queue") String queue,
                                                      @DescriptionFragment("exchange") String exchange,
                                                      @DescriptionFragment("routingKey") String routingKey){
        checkArgument(isNotBlank(queue));
        checkArgument(isNotBlank(exchange));

        return new RabbitMqQueueBindSupplier(queue, exchange, routingKey, new ArgSupplier());
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
