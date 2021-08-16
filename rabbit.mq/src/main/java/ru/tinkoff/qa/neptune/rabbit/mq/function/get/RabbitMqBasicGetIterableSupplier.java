package ru.tinkoff.qa.neptune.rabbit.mq.function.get;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.Iterables;
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
import static com.google.common.base.Preconditions.checkNotNull;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static ru.tinkoff.qa.neptune.rabbit.mq.properties.RabbitMQRoutingProperties.DEFAULT_QUEUE_NAME;

@SequentialGetStepSupplier.DefineGetImperativeParameterName("Retrieve:")
@SequentialGetStepSupplier.DefineTimeOutParameterName("Time of the waiting")
@SequentialGetStepSupplier.DefineCriteriaParameterName("Criteria for every item of resulted iterable")
@MaxDepthOfReporting(0)
public class RabbitMqBasicGetIterableSupplier<T, S extends Iterable<T>> extends SequentialGetStepSupplier
        .GetIterableStepSupplier<RabbitMqStepContext, S, T, RabbitMqBasicGetIterableSupplier<T, S>> {

    final GetFromQueue<?> getFromQueue;

    @CaptureOnSuccess(by = MessageCaptor.class)
    String message;

    @CaptureOnSuccess(by = MessagesCaptor.class)
    @CaptureOnFailure(by = MessagesCaptor.class)
    List<String> messages;

    protected <M> RabbitMqBasicGetIterableSupplier(GetFromQueue<M> getFromQueue, Function<M, S> function) {
        super(function.compose(getFromQueue));
        this.getFromQueue = getFromQueue;
    }

    /**
     * Creates a step that gets some iterable value which is calculated by body of message.
     *
     * @param description is description of value to get
     * @param queue       is a queue to read
     * @param autoAck     true if the server should consider messages
     *                    acknowledged once delivered; false if the server should expect
     *                    explicit acknowledgements
     * @param classT      is a class of a value to deserialize message
     * @param toGet       describes how to get desired value
     * @param <M>         is a type of deserialized message
     * @param <T>         is a type of an item of iterable
     * @param <S>         is a type of iterable
     * @return an instance of {@link RabbitMqBasicGetIterableSupplier}
     */
    @Description("{description}")
    public static <M, T, S extends Iterable<T>> RabbitMqBasicGetIterableSupplier<T, S> rabbitIterable(
            @DescriptionFragment(value = "description",
                    makeReadableBy = ParameterValueGetter.TranslatedDescriptionParameterValueGetter.class
            ) String description,
            String queue,
            boolean autoAck,
            Class<M> classT,
            Function<M, S> toGet) {
        checkArgument(isNotBlank(description), "Description should be defined");
        return new RabbitMqBasicGetIterableSupplier<>(new GetFromQueue<>(queue, autoAck, classT), toGet);
    }

    /**
     * Creates a step that gets some iterable value which is calculated by body of message.
     * It gets required value from default queue.
     *
     * @param description is description of value to get
     * @param autoAck     true if the server should consider messages
     *                    acknowledged once delivered; false if the server should expect
     *                    explicit acknowledgements
     * @param classT      is a class of a value to deserialize message
     * @param toGet       describes how to get desired value
     * @param <M>         is a type of deserialized message
     * @param <T>         is a type of an item of iterable
     * @param <S>         is a type of iterable
     * @return an instance of {@link RabbitMqBasicGetIterableSupplier}
     * @see RabbitMQRoutingProperties#DEFAULT_QUEUE_NAME
     */
    public static <M, T, S extends Iterable<T>> RabbitMqBasicGetIterableSupplier<T, S> rabbitIterable(
            String description,
            boolean autoAck,
            Class<M> classT,
            Function<M, S> toGet) {
        return rabbitIterable(description, DEFAULT_QUEUE_NAME.get(), autoAck, classT, toGet);
    }

    /**
     * Creates a step that gets some iterable value which is calculated by body of message.
     *
     * @param description is description of value to get
     * @param queue       is a queue to read
     * @param autoAck     true if the server should consider messages
     *                    acknowledged once delivered; false if the server should expect
     *                    explicit acknowledgements
     * @param typeT       is a reference to type of a value to deserialize message
     * @param toGet       describes how to get desired value
     * @param <M>         is a type of deserialized message
     * @param <T>         is a type of an item of iterable
     * @param <S>         is a type of iterable
     * @return an instance of {@link RabbitMqBasicGetIterableSupplier}
     */
    @Description("{description}")
    public static <M, T, S extends Iterable<T>> RabbitMqBasicGetIterableSupplier<T, S> rabbitIterable(
            @DescriptionFragment(value = "description",
                    makeReadableBy = ParameterValueGetter.TranslatedDescriptionParameterValueGetter.class
            ) String description,
            String queue,
            boolean autoAck,
            TypeReference<M> typeT,
            Function<M, S> toGet) {
        checkArgument(isNotBlank(description), "Description should be defined");
        return new RabbitMqBasicGetIterableSupplier<>(new GetFromQueue<>(queue, autoAck, typeT), toGet);
    }

    /**
     * Creates a step that gets some iterable value which is calculated by body of message.
     * It gets required value from default queue.
     *
     * @param description is description of value to get
     * @param autoAck     true if the server should consider messages
     *                    acknowledged once delivered; false if the server should expect
     *                    explicit acknowledgements
     * @param typeT       is a reference to type of a value to deserialize message
     * @param toGet       describes how to get desired value
     * @param <M>         is a type of deserialized message
     * @param <T>         is a type of an item of iterable
     * @param <S>         is a type of iterable
     * @return an instance of {@link RabbitMqBasicGetIterableSupplier}
     * @see RabbitMQRoutingProperties#DEFAULT_QUEUE_NAME
     */
    public static <M, T, S extends Iterable<T>> RabbitMqBasicGetIterableSupplier<T, S> rabbitIterable(
            String description,
            boolean autoAck,
            TypeReference<M> typeT,
            Function<M, S> toGet) {
        return rabbitIterable(description, DEFAULT_QUEUE_NAME.get(), autoAck, typeT, toGet);
    }

    /**
     * Creates a step that gets some (sub)iterable from iterable body of message.
     *
     * @param description is description of value to get
     * @param queue       is a queue to read
     * @param autoAck     true if the server should consider messages
     *                    acknowledged once delivered; false if the server should expect
     *                    explicit acknowledgements
     * @param classT      is a class of a value to deserialize message
     * @param <T>         is a type of an item of iterable
     * @param <S>         is a type of iterable
     * @return an instance of {@link RabbitMqBasicGetIterableSupplier}
     */
    public static <T, S extends Iterable<T>> RabbitMqBasicGetIterableSupplier<T, S> rabbitIterable(
            String description,
            String queue,
            boolean autoAck,
            Class<S> classT) {
        return rabbitIterable(description, queue, autoAck, classT, ts -> ts);
    }

    /**
     * Creates a step that gets some (sub)iterable from iterable body of message.
     * It gets required value from default queue.
     *
     * @param description is description of value to get
     * @param autoAck     true if the server should consider messages
     *                    acknowledged once delivered; false if the server should expect
     *                    explicit acknowledgements
     * @param classT      is a class of a value to deserialize message
     * @param <T>         is a type of an item of iterable
     * @param <S>         is a type of iterable
     * @return an instance of {@link RabbitMqBasicGetIterableSupplier}
     * @see RabbitMQRoutingProperties#DEFAULT_QUEUE_NAME
     */
    public static <T, S extends Iterable<T>> RabbitMqBasicGetIterableSupplier<T, S> rabbitIterable(
            String description,
            boolean autoAck,
            Class<S> classT) {
        return rabbitIterable(description, DEFAULT_QUEUE_NAME.get(), autoAck, classT);
    }

    /**
     * Creates a step that gets some (sub)iterable from iterable body of message.
     *
     * @param description is description of value to get
     * @param queue       is a queue to read
     * @param autoAck     true if the server should consider messages
     *                    acknowledged once delivered; false if the server should expect
     *                    explicit acknowledgements
     * @param typeT       is a reference to type of a value to deserialize message
     * @param <T>         is a type of an item of iterable
     * @param <S>         is a type of iterable
     * @return an instance of {@link RabbitMqBasicGetIterableSupplier}
     */
    public static <T, S extends Iterable<T>> RabbitMqBasicGetIterableSupplier<T, S> rabbitIterable(
            String description,
            String queue,
            boolean autoAck,
            TypeReference<S> typeT) {
        return rabbitIterable(description, queue, autoAck, typeT, ts -> ts);
    }

    /**
     * Creates a step that gets some (sub)iterable from iterable body of message.
     * It gets required value from default queue.
     *
     * @param description is description of value to get
     * @param autoAck     true if the server should consider messages
     *                    acknowledged once delivered; false if the server should expect
     *                    explicit acknowledgements
     * @param typeT       is a reference to type of a value to deserialize message
     * @param <T>         is a type of an item of iterable
     * @param <S>         is a type of iterable
     * @return an instance of {@link RabbitMqBasicGetIterableSupplier}
     * @see RabbitMQRoutingProperties#DEFAULT_QUEUE_NAME
     */
    public static <T, S extends Iterable<T>> RabbitMqBasicGetIterableSupplier<T, S> rabbitIterable(
            String description,
            boolean autoAck,
            TypeReference<S> typeT) {
        return rabbitIterable(description, DEFAULT_QUEUE_NAME.get(), autoAck, typeT);
    }

    @Override
    public RabbitMqBasicGetIterableSupplier<T, S> timeOut(Duration timeOut) {
        return super.timeOut(timeOut);
    }

    @Override
    protected void onSuccess(S s) {
        var ms = getFromQueue.getMessages();
        if (s != null && Iterables.size(s) > 0) {
            message = ms.getLast();
        } else {
            messages = ms;
        }
    }

    @Override
    protected void onFailure(RabbitMqStepContext m, Throwable throwable) {
        messages = getFromQueue.getMessages();
    }

    RabbitMqBasicGetIterableSupplier<T, S> setDataTransformer(DataTransformer dataTransformer) {
        checkNotNull(dataTransformer);
        getFromQueue.setTransformer(dataTransformer);
        return this;
    }
}
