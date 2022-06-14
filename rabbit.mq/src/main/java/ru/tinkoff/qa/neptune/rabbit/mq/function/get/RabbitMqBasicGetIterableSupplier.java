package ru.tinkoff.qa.neptune.rabbit.mq.function.get;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.Iterables;
import com.rabbitmq.client.Channel;
import ru.tinkoff.qa.neptune.core.api.data.format.DataTransformer;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotations.CaptureOnFailure;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotations.CaptureOnSuccess;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotations.MaxDepthOfReporting;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;
import ru.tinkoff.qa.neptune.core.api.steps.parameters.ParameterValueGetter;
import ru.tinkoff.qa.neptune.rabbit.mq.RabbitMqStepContext;
import ru.tinkoff.qa.neptune.rabbit.mq.captors.MessageCaptor;
import ru.tinkoff.qa.neptune.rabbit.mq.captors.MessagesCaptor;
import ru.tinkoff.qa.neptune.rabbit.mq.properties.RabbitMQRoutingProperties;

import java.time.Duration;
import java.util.List;
import java.util.function.Function;

import static com.google.common.base.Preconditions.checkArgument;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static ru.tinkoff.qa.neptune.rabbit.mq.GetChannel.getChannel;
import static ru.tinkoff.qa.neptune.rabbit.mq.properties.RabbitMQRoutingProperties.DEFAULT_QUEUE_NAME;

@SequentialGetStepSupplier.DefineGetImperativeParameterName("Retrieve:")
@SequentialGetStepSupplier.DefineTimeOutParameterName("Time of the waiting")
@SequentialGetStepSupplier.DefineCriteriaParameterName("Criteria for every item of resulted iterable")
@MaxDepthOfReporting(0)
public class RabbitMqBasicGetIterableSupplier<T, S extends Iterable<T>> extends
    SequentialGetStepSupplier.GetListChainedStepSupplier<RabbitMqStepContext, S, Channel, T, RabbitMqBasicGetIterableSupplier<T, S>> {

    final GetFromQueue<?> getFromQueue;

    @CaptureOnSuccess(by = MessageCaptor.class)
    String message;

    @CaptureOnSuccess(by = MessagesCaptor.class)
    @CaptureOnFailure(by = MessagesCaptor.class)
    List<String> messages;

    protected <M> RabbitMqBasicGetIterableSupplier(GetFromQueue<M> getFromQueue, Function<M, S> function) {
        super(function.compose(getFromQueue));
        this.getFromQueue = getFromQueue;
        from(getChannel());
    }

    /**
     * Creates a step that gets some iterable value which is calculated by body of message.
     *
     * @param description is description of value to get
     * @param queue       is a queue to read
     * @param classT      is a class of a value to deserialize message
     * @param toGet       describes how to get desired value
     * @param <M>         is a type of deserialized message
     * @param <T>         is a type of item of iterable
     * @param <S>         is a type of iterable
     * @return an instance of {@link RabbitMqBasicGetIterableSupplier}
     */
    @Description("{description}")
    public static <M, T, S extends Iterable<T>> RabbitMqBasicGetIterableSupplier<T, S> rabbitIterable(
            @DescriptionFragment(value = "description",
                    makeReadableBy = ParameterValueGetter.TranslatedDescriptionParameterValueGetter.class
            ) String description,
            String queue,
            Class<M> classT,
            Function<M, S> toGet) {
        checkArgument(isNotBlank(description), "Description should be defined");
        return new RabbitMqBasicGetIterableSupplier<>(new GetFromQueue<>(queue, classT), toGet);
    }

    /**
     * Creates a step that gets some iterable value which is calculated by body of message.
     * It gets required value from default queue.
     *
     * @param description is description of value to get
     * @param classT      is a class of a value to deserialize message
     * @param toGet       describes how to get desired value
     * @param <M>         is a type of deserialized message
     * @param <T>         is a type of item of iterable
     * @param <S>         is a type of iterable
     * @return an instance of {@link RabbitMqBasicGetIterableSupplier}
     * @see RabbitMQRoutingProperties#DEFAULT_QUEUE_NAME
     */
    public static <M, T, S extends Iterable<T>> RabbitMqBasicGetIterableSupplier<T, S> rabbitIterable(
            String description,
            Class<M> classT,
            Function<M, S> toGet) {
        return rabbitIterable(description, DEFAULT_QUEUE_NAME.get(), classT, toGet);
    }

    /**
     * Creates a step that gets some iterable value which is calculated by body of message.
     *
     * @param description is description of value to get
     * @param queue       is a queue to read
     * @param typeT       is a reference to type of value to deserialize message
     * @param toGet       describes how to get desired value
     * @param <M>         is a type of deserialized message
     * @param <T>         is a type of item of iterable
     * @param <S>         is a type of iterable
     * @return an instance of {@link RabbitMqBasicGetIterableSupplier}
     */
    @Description("{description}")
    public static <M, T, S extends Iterable<T>> RabbitMqBasicGetIterableSupplier<T, S> rabbitIterable(
            @DescriptionFragment(value = "description",
                    makeReadableBy = ParameterValueGetter.TranslatedDescriptionParameterValueGetter.class
            ) String description,
            String queue,
            TypeReference<M> typeT,
            Function<M, S> toGet) {
        checkArgument(isNotBlank(description), "Description should be defined");
        return new RabbitMqBasicGetIterableSupplier<>(new GetFromQueue<>(queue, typeT), toGet);
    }

    /**
     * Creates a step that gets some iterable value which is calculated by body of message.
     * It gets required value from default queue.
     *
     * @param description is description of value to get
     * @param typeT       is a reference to type of value to deserialize message
     * @param toGet       describes how to get desired value
     * @param <M>         is a type of deserialized message
     * @param <T>         is a type of item of iterable
     * @param <S>         is a type of iterable
     * @return an instance of {@link RabbitMqBasicGetIterableSupplier}
     * @see RabbitMQRoutingProperties#DEFAULT_QUEUE_NAME
     */
    public static <M, T, S extends Iterable<T>> RabbitMqBasicGetIterableSupplier<T, S> rabbitIterable(
            String description,
            TypeReference<M> typeT,
            Function<M, S> toGet) {
        return rabbitIterable(description, DEFAULT_QUEUE_NAME.get(), typeT, toGet);
    }

    /**
     * Creates a step that gets some (sub)iterable from iterable body of message.
     *
     * @param description is description of value to get
     * @param queue       is a queue to read
     * @param classT      is a class of a value to deserialize message
     * @param <T>         is a type of item of iterable
     * @param <S>         is a type of iterable
     * @return an instance of {@link RabbitMqBasicGetIterableSupplier}
     */
    public static <T, S extends Iterable<T>> RabbitMqBasicGetIterableSupplier<T, S> rabbitIterable(
            String description,
            String queue,
            Class<S> classT) {
        return rabbitIterable(description, queue, classT, ts -> ts);
    }

    /**
     * Creates a step that gets some (sub)iterable from iterable body of message.
     * It gets required value from default queue.
     *
     * @param description is description of value to get
     * @param classT      is a class of a value to deserialize message
     * @param <T>         is a type of item of iterable
     * @param <S>         is a type of iterable
     * @return an instance of {@link RabbitMqBasicGetIterableSupplier}
     * @see RabbitMQRoutingProperties#DEFAULT_QUEUE_NAME
     */
    public static <T, S extends Iterable<T>> RabbitMqBasicGetIterableSupplier<T, S> rabbitIterable(
            String description,
            Class<S> classT) {
        return rabbitIterable(description, DEFAULT_QUEUE_NAME.get(), classT);
    }

    /**
     * Creates a step that gets some (sub)iterable from iterable body of message.
     *
     * @param description is description of value to get
     * @param queue       is a queue to read
     * @param typeT       is a reference to type of value to deserialize message
     * @param <T>         is a type of item of iterable
     * @param <S>         is a type of iterable
     * @return an instance of {@link RabbitMqBasicGetIterableSupplier}
     */
    public static <T, S extends Iterable<T>> RabbitMqBasicGetIterableSupplier<T, S> rabbitIterable(
            String description,
            String queue,
            TypeReference<S> typeT) {
        return rabbitIterable(description, queue, typeT, ts -> ts);
    }

    /**
     * Creates a step that gets some (sub)iterable from iterable body of message.
     * It gets required value from default queue.
     *
     * @param description is description of value to get
     * @param typeT       is a reference to type of value to deserialize message
     * @param <T>         is a type of item of iterable
     * @param <S>         is a type of iterable
     * @return an instance of {@link RabbitMqBasicGetIterableSupplier}
     * @see RabbitMQRoutingProperties#DEFAULT_QUEUE_NAME
     */
    public static <T, S extends Iterable<T>> RabbitMqBasicGetIterableSupplier<T, S> rabbitIterable(
            String description,
            TypeReference<S> typeT) {
        return rabbitIterable(description, DEFAULT_QUEUE_NAME.get(), typeT);
    }

    @Override
    public RabbitMqBasicGetIterableSupplier<T, S> timeOut(Duration timeOut) {
        return super.timeOut(timeOut);
    }

    @Override
    protected void onSuccess(List<T> s) {
        var ms = getFromQueue.getMessages();
        if (s != null && Iterables.size(s) > 0) {
            message = ms.getLast();
        } else {
            messages = ms;
        }
    }

    @Override
    protected void onFailure(Channel m, Throwable throwable) {
        messages = getFromQueue.getMessages();
    }

    public RabbitMqBasicGetIterableSupplier<T, S> withDataTransformer(DataTransformer transformer) {
        getFromQueue.setTransformer(transformer);
        return this;
    }

    /**
     * It means that server should consider messages acknowledged once delivered.
     *
     * @return self-reference
     */
    public RabbitMqBasicGetIterableSupplier<T, S> autoAck() {
        this.getFromQueue.setAutoAck();
        return this;
    }
}
