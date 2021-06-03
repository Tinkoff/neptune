package ru.tinkoff.qa.neptune.rabbit.mq.function.unbind.queue;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotations.MaxDepthOfReporting;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;
import ru.tinkoff.qa.neptune.rabbit.mq.AdditionalArguments;
import ru.tinkoff.qa.neptune.rabbit.mq.RabbitMqStepContext;

import java.io.IOException;
import java.util.function.Function;

import static com.google.common.base.Preconditions.checkArgument;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@MaxDepthOfReporting(0)
public class RabbitMqQueueUnbindSupplier extends SequentialGetStepSupplier.GetObjectChainedStepSupplier<RabbitMqStepContext, AMQP.Queue.UnbindOk, Channel, RabbitMqQueueUnbindSupplier>{
    //TODO Сделать как RabbitMqExchangeUnbindSupplier
    protected RabbitMqQueueUnbindSupplier(Function<Channel, AMQP.Queue.UnbindOk> originalFunction) {
        super(originalFunction);
    }

    @Description("Unbinds a queue from an exchange, with no extra arguments.\r\n" +
            "Params:\n" +
            "queue – {queue}\n" +
            "exchange – {exchange}\n" +
            "routingKey – {routingKey}")
    public static RabbitMqQueueUnbindSupplier withParam(@DescriptionFragment("queue") String queue,
                                                        @DescriptionFragment("exchange") String exchange,
                                                        @DescriptionFragment("routingKey") String routingKey){
        checkArgument(isNotBlank(queue));
        checkArgument(isNotBlank(exchange));

        return new RabbitMqQueueUnbindSupplier(channel -> {
            try {
                return channel.queueUnbind(queue, exchange, routingKey, null);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Description("Unbind a queue from an exchange.\n" +
            "Params:\n" +
            "queue – {queue}\n" +
            "exchange – {exchange}\n" +
            "routingKey – {routingKey}\n"+
            "arguments – {arguments}")
    public static RabbitMqQueueUnbindSupplier withParam(@DescriptionFragment("queue") String queue,
                                                        @DescriptionFragment("exchange") String exchange,
                                                        @DescriptionFragment("routingKey") String routingKey,
                                                        @DescriptionFragment("arguments") AdditionalArguments arguments){
        checkArgument(isNotBlank(queue));
        checkArgument(isNotBlank(exchange));

        return new RabbitMqQueueUnbindSupplier(channel -> {
            try {
                return channel.queueUnbind(queue, exchange, routingKey, arguments.getHashMap());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
