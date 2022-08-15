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
import ru.tinkoff.qa.neptune.rabbit.mq.captors.MessagesCaptor;
import ru.tinkoff.qa.neptune.rabbit.mq.properties.RabbitMQRoutingProperties;

import java.lang.reflect.Array;
import java.lang.reflect.ParameterizedType;
import java.time.Duration;
import java.util.List;
import java.util.function.Function;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.ArrayUtils.add;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static ru.tinkoff.qa.neptune.rabbit.mq.properties.RabbitMQRoutingProperties.DEFAULT_QUEUE_NAME;

@SequentialGetStepSupplier.DefineGetImperativeParameterName("Retrieve:")
@SequentialGetStepSupplier.DefineTimeOutParameterName("Time of the waiting")
@SequentialGetStepSupplier.DefineCriteriaParameterName("Criteria for every item of resulted array")
@MaxDepthOfReporting(0)
@SuppressWarnings({"unchecked", "rawtypes"})
public abstract class RabbitMqBasicGetArraySupplier<M, R, S extends RabbitMqBasicGetArraySupplier<M, R, S>> extends SequentialGetStepSupplier
        .GetArrayStepSupplier<RabbitMqStepContext, R, S> {

    public static final String NO_DESC_ERROR_TEXT = "Description should be defined";

    final GetFromQueue.MergeProperty getFromQueue;

    @CaptureOnSuccess(by = MessagesCaptor.class)
    @CaptureOnFailure(by = MessagesCaptor.class)
    List<String> messages;

    protected RabbitMqBasicGetArraySupplier(GetFromQueue.MergeProperty<List<M>> getFromQueue, Function<M, R> function, Class<R> componentClass) {
        super(getFromQueue.andThen(list -> {
            var listT = list.stream().map(function).collect(toList());
            R[] ts = (R[]) Array.newInstance(componentClass, 0);

            for (var t : listT) {
                ts = add(ts, t);
            }

            return ts;
        }));
        this.getFromQueue = getFromQueue;
    }

    /**
     * Creates a step that gets some array value which is calculated by body of message.
     *
     * @param description is description of value to get
     * @param queue       is a queue to read
     * @param classT      is a class of a value to deserialize message
     * @param toGet       describes how to get desired value
     * @param <M>         is a type of deserialized message
     * @param <T>         is a type of item of array
     * @return an instance of {@link RabbitMqBasicGetArraySupplier}
     */
    @Description("{description}")
    public static <M, T> Mapped<M, T> rabbitArray(
            @DescriptionFragment(value = "description",
                    makeReadableBy = ParameterValueGetter.TranslatedDescriptionParameterValueGetter.class
            ) String description,
            String queue,
            Class<M> classT,
            Class<T> componentClass,
            Function<M, T> toGet) {
        checkArgument(isNotBlank(description), NO_DESC_ERROR_TEXT);
        return new RabbitMqBasicGetArraySupplier.Mapped<>(new GetFromQueue(queue).andThen(new GetDeserializedData<>(classT)),
                toGet,
                componentClass);
    }

    /**
     * Creates a step that gets some array value which is calculated by body of message.
     * It gets required value from default queue.
     *
     * @param description is description of value to get
     * @param classT      is a class of a value to deserialize message
     * @param toGet       describes how to get desired value
     * @param <M>         is a type of deserialized message
     * @param <T>         is a type of item of array
     * @return an instance of {@link RabbitMqBasicGetArraySupplier}
     * @see RabbitMQRoutingProperties#DEFAULT_QUEUE_NAME
     */
    public static <M, T> Mapped<M, T> rabbitArray(String description,
                                                  Class<M> classT,
                                                  Class<T> componentClass,
                                                  Function<M, T> toGet) {
        return rabbitArray(description, DEFAULT_QUEUE_NAME.get(), classT, componentClass, toGet);
    }

    /**
     * Creates a step that gets some array value which is calculated by body of message.
     *
     * @param description is description of value to get
     * @param queue       is a queue to read
     * @param typeT       is a reference to type of value to deserialize message
     * @param toGet       describes how to get desired value
     * @param <M>         is a type of deserialized message
     * @param <T>         is a type of item of array
     * @return an instance of {@link RabbitMqBasicGetArraySupplier}
     */
    @Description("{description}")
    public static <M, T> Mapped<M, T> rabbitArray(
            @DescriptionFragment(value = "description",
                    makeReadableBy = ParameterValueGetter.TranslatedDescriptionParameterValueGetter.class
            ) String description,
            String queue,
            TypeReference<M> typeT,
            Class<T> componentClass,
            Function<M, T> toGet) {
        checkArgument(isNotBlank(description), NO_DESC_ERROR_TEXT);
        return new RabbitMqBasicGetArraySupplier.Mapped<>(new GetFromQueue(queue).andThen(new GetDeserializedData<>(typeT)),
                toGet,
                componentClass);
    }

    /**
     * Creates a step that gets some array value which is calculated by body of message.
     * It gets required value from default queue.
     *
     * @param description is description of value to get
     * @param typeT       is a reference to type of value to deserialize message
     * @param toGet       describes how to get desired value
     * @param <M>         is a type of deserialized message
     * @param <T>         is a type of item of array
     * @return an instance of {@link RabbitMqBasicGetArraySupplier}
     * @see RabbitMQRoutingProperties#DEFAULT_QUEUE_NAME
     */
    public static <M, T> Mapped<M, T> rabbitArray(String description,
                                                  TypeReference<M> typeT,
                                                  Class<T> componentClass,
                                                  Function<M, T> toGet) {
        return rabbitArray(description, DEFAULT_QUEUE_NAME.get(), typeT, componentClass, toGet);
    }


    /**
     * Creates a step that gets some (sub)array from array body of message.
     *
     * @param description is description of value to get
     * @param queue       is a queue to read
     * @param classT      is a class of a value to deserialize message
     * @param <T>         is a type of item of array
     * @return an instance of {@link RabbitMqBasicGetArraySupplier}
     */
    public static <T> Mapped<T, T> rabbitArray(
            String description,
            String queue,
            Class<T> classT) {
        return rabbitArray(description, queue, classT, classT, ts -> ts);
    }

    /**
     * Creates a step that gets some (sub)array from array body of message.
     * It gets required value from default queue.
     *
     * @param description is description of value to get
     * @param classT      is a class of a value to deserialize message
     * @param <T>         is a type of item of array
     * @return an instance of {@link RabbitMqBasicGetArraySupplier}
     * @see RabbitMQRoutingProperties#DEFAULT_QUEUE_NAME
     */
    public static <T> Mapped<T, T> rabbitArray(
            String description,
            Class<T> classT) {
        return rabbitArray(description, DEFAULT_QUEUE_NAME.get(), classT);
    }

    /**
     * Creates a step that gets some (sub)array from array body of message.
     *
     * @param description is description of value to get
     * @param queue       is a queue to read
     * @param typeT       is a reference to type of value to deserialize message
     * @param <T>         is a type of item of array
     * @return an instance of {@link RabbitMqBasicGetArraySupplier}
     */
    public static <T> Mapped<T, T> rabbitArray(
            String description,
            String queue,
            TypeReference<T> typeT) {
        var clazz = (Class) (typeT.getType() instanceof ParameterizedType ? ((ParameterizedType) typeT.getType()).getRawType() : typeT.getType());
        return rabbitArray(description, queue, typeT, clazz, ts -> ts);
    }

    /**
     * Creates a step that gets some (sub)array from array body of message.
     * It gets required value from default queue.
     *
     * @param description is description of value to get
     * @param typeT       is a reference to type of value to deserialize message
     * @param <T>         is a type of item of array
     * @return an instance of {@link RabbitMqBasicGetArraySupplier}
     * @see RabbitMQRoutingProperties#DEFAULT_QUEUE_NAME
     */
    public static <T> Mapped<T, T> rabbitArray(
            String description,
            TypeReference<T> typeT) {
        return rabbitArray(description, DEFAULT_QUEUE_NAME.get(), typeT);
    }

    /**
     * Creates a step that returns array of string contents of messages.
     *
     * @param queue are queue to get messages from
     * @return an instance of {@link RabbitMqBasicGetArraySupplier.StringMessages}
     */
    @Description("String messages")
    public static StringMessages rabbitArrayOfRawMessages(String queue) {
        return new StringMessages(new GetFromQueue(queue).andThen(new GetDeserializedData<>(String.class)));
    }

    public static StringMessages rabbitArrayOfRawMessages() {
        return rabbitArrayOfRawMessages(DEFAULT_QUEUE_NAME.get());
    }

    @Override
    public S timeOut(Duration timeOut) {
        return super.timeOut(timeOut);
    }

    @Override
    protected void onSuccess(R[] t) {
        messages = getFromQueue.getBefore().getMessages();
    }

    @Override
    protected void onFailure(RabbitMqStepContext m, Throwable throwable) {
        messages = getFromQueue.getBefore().getMessages();
    }

    S withDataTransformer(DataTransformer dataTransformer) {
        ((GetDeserializedData<M>) getFromQueue.getAfter()).setTransformer(dataTransformer);
        return (S) this;
    }

    /**
     * It means that server should consider messages acknowledged once delivered.
     *
     * @return self-reference
     */
    public S autoAck() {
        this.getFromQueue.getBefore().setAutoAck();
        return (S) this;
    }

    public final static class Mapped<M, T> extends RabbitMqBasicGetArraySupplier<M, T, Mapped<M, T>> {

        private Mapped(GetFromQueue.MergeProperty<List<M>> getFromTopics, Function<M, T> originalFunction, Class<T> componentClass) {
            super(getFromTopics, originalFunction, componentClass);
        }

        public Mapped<M, T> withDataTransformer(DataTransformer transformer) {
            return super.withDataTransformer(transformer);
        }
    }

    public final static class StringMessages extends RabbitMqBasicGetArraySupplier<String, String, StringMessages> {

        private StringMessages(GetFromQueue.MergeProperty<List<String>> getFromTopics) {
            super(getFromTopics, s -> s, String.class);
            withDataTransformer(new StringDataTransformer());
        }
    }
}
