package ru.tinkoff.qa.neptune.rabbit.mq.function.get;

import com.google.common.base.Supplier;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.GetResponse;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotations.MaxDepthOfReporting;
import ru.tinkoff.qa.neptune.core.api.steps.Criteria;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;
import ru.tinkoff.qa.neptune.rabbit.mq.RabbitMqStepContext;
import ru.tinkoff.qa.neptune.rabbit.mq.properties.RabbitMqMapper;

import java.time.Duration;
import java.util.function.Predicate;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.time.Duration.ofSeconds;

@SequentialGetStepSupplier.DefineGetImperativeParameterName("Retrieve:")
@SequentialGetStepSupplier.DefineTimeOutParameterName("Time of the waiting for messages")
@SequentialGetStepSupplier.DefineCriteriaParameterName("Message criteria")
@MaxDepthOfReporting(0)
public class RabbitMqBasicGetSupplier<T> extends SequentialGetStepSupplier.GetObjectStepSupplier<RabbitMqStepContext,T, RabbitMqBasicGetSupplier<T>> {
    private RabbitMqMapper rabbitMqMapper;
    private final MapperSupplier supplier;

    protected RabbitMqBasicGetSupplier(String queue, boolean autoAck, Class<T> classT, MapperSupplier supplier) {
        super(input ->{
            checkNotNull(queue);
            var rabbitMqMapper = supplier.get();
            try {
                Channel channel = input.getChannel();
                GetResponse getResponse = channel.basicGet(queue, autoAck);

                return rabbitMqMapper.deserialize(new String(getResponse.getBody(), UTF_8), classT);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        });
        this.supplier = supplier;
    }

    public RabbitMqBasicGetSupplier<T> setObjectMapper(RabbitMqMapper rabbitMqMapper) {
        this.rabbitMqMapper = rabbitMqMapper;
        supplier.setMapper(rabbitMqMapper);
        return this;
    }

    @Description("message from a queue using AMQP.Basic.Get\r\n" +
            "Params:\r\n" +
            "queue – {queue}\r\n" +
            "autoAck - {autoAck}")
    public static <T> RabbitMqBasicGetSupplier<T> valueOf(@DescriptionFragment("queue") String queue,
                                                          @DescriptionFragment("autoAck") boolean autoAck, Class<T> classT) {
        return new RabbitMqBasicGetSupplier<>(queue, autoAck, classT, new MapperSupplier())
                .timeOut(ofSeconds(10));
    }

    @Override
    public RabbitMqBasicGetSupplier<T> timeOut(Duration timeOut) {
        return super.timeOut(timeOut);
    }

    @Override
    public RabbitMqBasicGetSupplier<T> criteria(String description, Predicate<? super T> predicate) {
        return super.criteria(description, predicate);
    }

    @Override
    public RabbitMqBasicGetSupplier<T> criteria(Criteria<? super T> criteria) {
        return super.criteria(criteria);
    }


    private static final class MapperSupplier implements Supplier<RabbitMqMapper> {

        private RabbitMqMapper mapper;

        @Override
        public RabbitMqMapper get() {
            return mapper;
        }

        MapperSupplier setMapper(RabbitMqMapper mapper) {
            this.mapper = mapper;
            return this;
        }
    }
}
