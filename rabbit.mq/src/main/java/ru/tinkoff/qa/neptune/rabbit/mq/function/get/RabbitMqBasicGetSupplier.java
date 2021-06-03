package ru.tinkoff.qa.neptune.rabbit.mq.function.get;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Supplier;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.GetResponse;
import ru.tinkoff.qa.neptune.core.api.steps.Criteria;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;
import ru.tinkoff.qa.neptune.rabbit.mq.RabbitMqStepContext;

import java.time.Duration;
import java.util.function.Predicate;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.time.Duration.ofSeconds;

public class RabbitMqBasicGetSupplier<T> extends SequentialGetStepSupplier.GetObjectStepSupplier<RabbitMqStepContext,T, RabbitMqBasicGetSupplier<T>> {
    private ObjectMapper objectMapper;
    private final MapperSupplier supplier;

    protected RabbitMqBasicGetSupplier(String queue, boolean autoAck, Class<T> classT, MapperSupplier supplier) {
        super(input ->{
            checkNotNull(queue);
            var objectMapper = supplier.get();
            try {
                Channel channel = input.getChannel();
                GetResponse getResponse = channel.basicGet(queue, autoAck);
                return objectMapper.readValue(new String(getResponse.getBody(), UTF_8), classT);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        });
        this.supplier = supplier;
    }

    public RabbitMqBasicGetSupplier<T> setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        supplier.setMapper(objectMapper);
        return this;
    }

    @Description("Retrieve a message from a queue using AMQP.Basic.Get\r\n" +
            "Params:\r\n" +
            "queue – {queue}\r\n" +
            "autoAck - {autoAck}")
    public static <T> RabbitMqBasicGetSupplier<T> read(@DescriptionFragment("queue") String queue,
                                                       @DescriptionFragment("autoAck") boolean autoAck, Class<T> classT){
        return new RabbitMqBasicGetSupplier<>(queue,autoAck,classT, new MapperSupplier())
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


    private static final class MapperSupplier implements Supplier<ObjectMapper> {

        private ObjectMapper mapper;

        @Override
        public ObjectMapper get() {
            return mapper;
        }

        MapperSupplier setMapper(ObjectMapper mapper) {
            this.mapper = mapper;
            return this;
        }
    }
}
