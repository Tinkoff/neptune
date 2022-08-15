package ru.tinkoff.qa.neptune.rabbit.mq.function.get;

import com.fasterxml.jackson.core.type.TypeReference;
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
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static ru.tinkoff.qa.neptune.rabbit.mq.properties.RabbitMQRoutingProperties.DEFAULT_QUEUE_NAME;

@SequentialGetStepSupplier.DefineGetImperativeParameterName("Retrieve:")
@SequentialGetStepSupplier.DefineTimeOutParameterName("Time of the waiting")
@SequentialGetStepSupplier.DefineCriteriaParameterName("Object criteria")
@MaxDepthOfReporting(0)
@SuppressWarnings("unchecked")
public abstract class RabbitMqBasicGetIterableItemSupplier<M, T, I extends RabbitMqBasicGetIterableItemSupplier<M, T, I>>
        extends SequentialGetStepSupplier.GetObjectFromIterableStepSupplier<RabbitMqStepContext, T, I> {

    public static final String NO_DESC_ERROR_TEXT = "Description should be defined";
    final GetFromQueue.MergeProperty getFromQueue;

    @CaptureOnSuccess(by = MessageCaptor.class)
    String message;

    @CaptureOnSuccess(by = MessagesCaptor.class)
    @CaptureOnFailure(by = MessagesCaptor.class)
    List<String> messages;

    protected RabbitMqBasicGetIterableItemSupplier(GetFromQueue.MergeProperty<List<M>> getFromQueue, Function<M, T> function) {
        super(getFromQueue.andThen(list -> list.stream().map(function).collect(toList())));
        this.getFromQueue = getFromQueue;
    }

    /**
     * Creates a step that gets some value from iterable which is calculated by body of message.
     *
     * @param description is description of value to get
     * @param queue       is a queue to read
     * @param classT      is a class of a value to deserialize message
     * @param toGet       describes how to get desired value
     * @param <M>         is a type of deserialized message
     * @param <T>         is a type of item of iterable
     * @return an instance of {@link RabbitMqBasicGetIterableItemSupplier}
     */
    @Description("{description}")
    public static <M, T> Mapped<M, T> rabbitIterableItem(
            @DescriptionFragment(value = "description",
                    makeReadableBy = ParameterValueGetter.TranslatedDescriptionParameterValueGetter.class
            ) String description,
            String queue,
            Class<M> classT,
            Function<M, T> toGet) {
        checkArgument(isNotBlank(description), "Description should be defined");
        return new RabbitMqBasicGetIterableItemSupplier.Mapped<>(new GetFromQueue(queue).andThen(new GetDeserializedData<>(classT)), toGet);
    }

    /**
     * Creates a step that gets some value from iterable which is calculated by body of message.
     * It gets required value from default queue.
     *
     * @param description is description of value to get
     * @param classT      is a class of a value to deserialize message
     * @param toGet       describes how to get desired value
     * @param <M>         is a type of deserialized message
     * @param <T>         is a type of item of iterable
     * @return an instance of {@link RabbitMqBasicGetIterableItemSupplier}
     * @see RabbitMQRoutingProperties#DEFAULT_QUEUE_NAME
     */
    public static <M, T> Mapped<M, T> rabbitIterableItem(
            String description,
            Class<M> classT,
            Function<M, T> toGet) {
        return rabbitIterableItem(description, DEFAULT_QUEUE_NAME.get(), classT, toGet);
    }

    /**
     * Creates a step that gets some value from iterable which is calculated by body of message.
     *
     * @param description is description of value to get
     * @param queue       is a queue to read
     * @param typeT       is a reference to type of value to deserialize message
     * @param toGet       describes how to get desired value
     * @param <M>         is a type of deserialized message
     * @param <T>         is a type of item of iterable
     * @return an instance of {@link RabbitMqBasicGetIterableItemSupplier}
     */
    @Description("{description}")
    public static <M, T> Mapped<M, T> rabbitIterableItem(
            @DescriptionFragment(value = "description",
                    makeReadableBy = ParameterValueGetter.TranslatedDescriptionParameterValueGetter.class
            ) String description,
            String queue,
            TypeReference<M> typeT,
            Function<M, T> toGet) {
        checkArgument(isNotBlank(description), "Description should be defined");
        return new RabbitMqBasicGetIterableItemSupplier.Mapped<>(new GetFromQueue(queue).andThen(new GetDeserializedData<>(typeT)), toGet);
    }

    /**
     * Creates a step that gets some value from iterable which is calculated by body of message.
     * It gets required value from default queue.
     *
     * @param description is description of value to get
     * @param typeT       is a reference to type of value to deserialize message
     * @param toGet       describes how to get desired value
     * @param <M>         is a type of deserialized message
     * @param <T>         is a type of item of iterable
     * @param <S>         is a type of iterable
     * @return an instance of {@link RabbitMqBasicGetIterableItemSupplier}
     * @see RabbitMQRoutingProperties#DEFAULT_QUEUE_NAME
     */
    public static <M, T> Mapped<M, T> rabbitIterableItem(
            String description,
            TypeReference<M> typeT,
            Function<M, T> toGet) {
        return rabbitIterableItem(description, DEFAULT_QUEUE_NAME.get(), typeT, toGet);
    }

    /**
     * Creates a step that gets some value from iterable body of message.
     *
     * @param description is description of value to get
     * @param queue       is a queue to read
     * @param classT      is a class of a value to deserialize message
     * @param <T>         is a type of item of iterable
     * @return an instance of {@link RabbitMqBasicGetIterableItemSupplier}
     */
    public static <M> Mapped<M, M> rabbitIterableItem(
            String description,
            String queue,
            Class<M> classT) {
        return rabbitIterableItem(description, queue, classT, ts -> ts);
    }

    /**
     * Creates a step that gets some value from iterable body of message. It gets required value from default queue.
     *
     * @param description is description of value to get
     * @param classT      is a class of a value to deserialize message
     * @param <T>         is a type of item of iterable
     * @param <M>         is a type of iterable
     * @return an instance of {@link RabbitMqBasicGetIterableItemSupplier}
     * @see RabbitMQRoutingProperties#DEFAULT_QUEUE_NAME
     */
    public static <M> Mapped<M, M> rabbitIterableItem(
            String description,
            Class<M> classT) {
        return rabbitIterableItem(description, DEFAULT_QUEUE_NAME.get(), classT);
    }

    /**
     * Creates a step that gets some value from iterable body of message.
     *
     * @param description is description of value to get
     * @param queue       is a queue to read
     * @param typeT       is a reference to type of value to deserialize message
     * @param <M>         is a type of iterable
     * @return an instance of {@link RabbitMqBasicGetIterableItemSupplier}
     */
    public static <M> Mapped<M, M> rabbitIterableItem(
            String description,
            String queue,
            TypeReference<M> typeT) {
        return rabbitIterableItem(description, queue, typeT, ts -> ts);
    }

    /**
     * Creates a step that gets some value from iterable body of message. It gets required value from default queue.
     *
     * @param description is description of value to get
     * @param typeT       is a reference to type of value to deserialize message
     * @param <M>         is a type of iterable
     * @return an instance of {@link RabbitMqBasicGetIterableItemSupplier}
     * @see RabbitMQRoutingProperties#DEFAULT_QUEUE_NAME
     */
    public static <M> Mapped<M, M> rabbitIterableItem(
            String description,
            TypeReference<M> typeT) {
        return rabbitIterableItem(description, DEFAULT_QUEUE_NAME.get(), typeT);
    }

    @Override
    public I timeOut(Duration timeOut) {
        return super.timeOut(timeOut);
    }

    @Override
    protected void onSuccess(T t) {
        var ms = ((GetDeserializedData<T>) getFromQueue.getAfter()).getMessages();
        if (t != null) {
            message = ms.getLast();
        } else {
            messages = ms;
        }
    }

    @Override
    protected void onFailure(RabbitMqStepContext m, Throwable throwable) {
        messages = ((GetDeserializedData<T>) getFromQueue.getAfter()).getMessages();
    }

    I withDataTransformer(DataTransformer transformer) {
        ((GetDeserializedData<M>) getFromQueue.getAfter()).setTransformer(transformer);
        return (I) this;
    }

    /**
     * It means that server should consider messages acknowledged once delivered.
     *
     * @return self-reference
     */
    public I autoAck() {
        this.getFromQueue.getBefore().setAutoAck();
        return (I) this;
    }

    public final static class Mapped<M, T> extends RabbitMqBasicGetIterableItemSupplier<M, T, Mapped<M, T>> {

        protected Mapped(GetFromQueue.MergeProperty<List<M>> getFromQueue, Function<M, T> function) {
            super(getFromQueue, function);
        }

        public Mapped<M, T> withDataTransformer(DataTransformer transformer) {
            return super.withDataTransformer(transformer);
        }
    }

    public final static class StringMessage extends RabbitMqBasicGetIterableItemSupplier<String, String, StringMessage> {

        protected StringMessage(GetFromQueue.MergeProperty<List<String>> getFromQueue) {
            super(getFromQueue, s -> s);
            withDataTransformer(new StringDataTransformer());
        }
    }
}
