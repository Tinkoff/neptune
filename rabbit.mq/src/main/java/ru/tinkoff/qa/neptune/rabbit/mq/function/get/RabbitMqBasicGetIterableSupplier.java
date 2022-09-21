package ru.tinkoff.qa.neptune.rabbit.mq.function.get;

import com.fasterxml.jackson.core.type.TypeReference;
import ru.tinkoff.qa.neptune.core.api.data.format.DataTransformer;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotations.CaptureOnFailure;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotations.CaptureOnSuccess;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.MaxDepthOfReporting;
import ru.tinkoff.qa.neptune.core.api.steps.parameters.ParameterValueGetter;
import ru.tinkoff.qa.neptune.rabbit.mq.RabbitMqStepContext;
import ru.tinkoff.qa.neptune.rabbit.mq.captors.MessagesCaptor;
import ru.tinkoff.qa.neptune.rabbit.mq.properties.RabbitMQRoutingProperties;

import java.nio.charset.Charset;
import java.time.Duration;
import java.util.List;
import java.util.function.Function;

import static com.google.common.base.Preconditions.checkArgument;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static ru.tinkoff.qa.neptune.rabbit.mq.function.get.GetDeserializedData.getStringResult;
import static ru.tinkoff.qa.neptune.rabbit.mq.properties.RabbitMQRoutingProperties.DEFAULT_QUEUE_NAME;

@SequentialGetStepSupplier.DefineGetImperativeParameterName("Retrieve:")
@SequentialGetStepSupplier.DefineTimeOutParameterName("Time of the waiting")
@SequentialGetStepSupplier.DefineCriteriaParameterName("Criteria for every item of resulted iterable")
@MaxDepthOfReporting(0)
@SuppressWarnings("unchecked")
public abstract class RabbitMqBasicGetIterableSupplier<M, R, S extends RabbitMqBasicGetIterableSupplier<M, R, S>> extends
        SequentialGetStepSupplier.GetListStepSupplier<RabbitMqStepContext, List<R>, R, S> {

    public static final String NO_DESC_ERROR_TEXT = "Description should be defined";

    final GetFromQueue.MergeProperty getFromQueue;

    @CaptureOnSuccess(by = MessagesCaptor.class)
    @CaptureOnFailure(by = MessagesCaptor.class)
    List<String> messages;

    protected RabbitMqBasicGetIterableSupplier(GetFromQueue.MergeProperty<List<M>> getFromQueue, Function<M, R> function) {
        super(getFromQueue.andThen(list -> list.stream().map(function).collect(toList())));
        this.getFromQueue = getFromQueue;
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
     * @return an instance of {@link RabbitMqBasicGetIterableSupplier}
     */
    @Description("{description}")
    public static <M, T> Mapped<M, T> rabbitIterable(
            @DescriptionFragment(value = "description",
                    makeReadableBy = ParameterValueGetter.TranslatedDescriptionParameterValueGetter.class
            ) String description,
            String queue,
            Class<M> classT,
            Function<M, T> toGet) {
        checkArgument(isNotBlank(description), NO_DESC_ERROR_TEXT);
        return new RabbitMqBasicGetIterableSupplier.Mapped<>(new GetFromQueue(queue).andThen(new GetDeserializedData<>(classT)), toGet);
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
     * @return an instance of {@link RabbitMqBasicGetIterableSupplier}
     * @see RabbitMQRoutingProperties#DEFAULT_QUEUE_NAME
     */
    public static <M, T> Mapped<M, T> rabbitIterable(
            String description,
            Class<M> classT,
            Function<M, T> toGet) {
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
     * @return an instance of {@link RabbitMqBasicGetIterableSupplier}
     */
    @Description("{description}")
    public static <M, T> Mapped<M, T> rabbitIterable(
            @DescriptionFragment(value = "description",
                    makeReadableBy = ParameterValueGetter.TranslatedDescriptionParameterValueGetter.class
            ) String description,
            String queue,
            TypeReference<M> typeT,
            Function<M, T> toGet) {
        checkArgument(isNotBlank(description), NO_DESC_ERROR_TEXT);
        return new RabbitMqBasicGetIterableSupplier.Mapped<>(new GetFromQueue(queue).andThen(new GetDeserializedData<>(typeT)), toGet);
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
     * @return an instance of {@link RabbitMqBasicGetIterableSupplier}
     * @see RabbitMQRoutingProperties#DEFAULT_QUEUE_NAME
     */
    public static <M, T> Mapped<M, T> rabbitIterable(
            String description,
            TypeReference<M> typeT,
            Function<M, T> toGet) {
        return rabbitIterable(description, DEFAULT_QUEUE_NAME.get(), typeT, toGet);
    }

    /**
     * Creates a step that gets some (sub)iterable from iterable body of message.
     *
     * @param description is description of value to get
     * @param queue       is a queue to read
     * @param classT      is a class of a value to deserialize message
     * @param <T>         is a type of item of iterable
     * @return an instance of {@link RabbitMqBasicGetIterableSupplier}
     */
    public static <T> Mapped<T, T> rabbitIterable(
            String description,
            String queue,
            Class<T> classT) {
        return rabbitIterable(description, queue, classT, ts -> ts);
    }

    /**
     * Creates a step that gets some (sub)iterable from iterable body of message.
     * It gets required value from default queue.
     *
     * @param description is description of value to get
     * @param classT      is a class of a value to deserialize message
     * @param <T>         is a type of item of iterable
     * @return an instance of {@link RabbitMqBasicGetIterableSupplier}
     * @see RabbitMQRoutingProperties#DEFAULT_QUEUE_NAME
     */
    public static <T> Mapped<T, T> rabbitIterable(
            String description,
            Class<T> classT) {
        return rabbitIterable(description, DEFAULT_QUEUE_NAME.get(), classT);
    }

    /**
     * Creates a step that gets some (sub)iterable from iterable body of message.
     *
     * @param description is description of value to get
     * @param queue       is a queue to read
     * @param typeT       is a reference to type of value to deserialize message
     * @param <T>         is a type of item of iterable
     * @return an instance of {@link RabbitMqBasicGetIterableSupplier}
     */
    public static <T> Mapped<T, T> rabbitIterable(
            String description,
            String queue,
            TypeReference<T> typeT) {
        return rabbitIterable(description, queue, typeT, ts -> ts);
    }

    /**
     * Creates a step that gets some (sub)iterable from iterable body of message.
     * It gets required value from default queue.
     *
     * @param description is description of value to get
     * @param typeT       is a reference to type of value to deserialize message
     * @param <T>         is a type of item of iterable
     * @return an instance of {@link RabbitMqBasicGetIterableSupplier}
     * @see RabbitMQRoutingProperties#DEFAULT_QUEUE_NAME
     */
    public static <T> Mapped<T, T> rabbitIterable(
            String description,
            TypeReference<T> typeT) {
        return rabbitIterable(description, DEFAULT_QUEUE_NAME.get(), typeT);
    }

    /**
     * Creates a step that receives a collection of string messages.
     *
     * @param queue   is a queue to read
     * @param charset is a required charset
     */
    @Description("String message")
    public static StringMessages rabbitIterableOfRawMessages(String queue, Charset charset){
        return new StringMessages(new GetFromQueue(queue).andThen(getStringResult(charset)));
    }

    public static StringMessages rabbitIterableOfRawMessages(Charset charset){
        return rabbitIterableOfRawMessages(DEFAULT_QUEUE_NAME.get(), charset);
    }

    public static StringMessages rabbitIterableOfRawMessages(String queue){
        return rabbitIterableOfRawMessages(queue, UTF_8);
    }

    public static StringMessages rabbitIterableOfRawMessages(){
        return rabbitIterableOfRawMessages(UTF_8);
    }

    @Override
    public S timeOut(Duration timeOut) {
        return super.timeOut(timeOut);
    }

    @Override
    protected void onSuccess(List<R> s) {
        if (s == null || s.isEmpty()) {
            messages = getFromQueue.getBefore().getMessages();
        }
    }

    @Override
    protected void onFailure(RabbitMqStepContext m, Throwable throwable) {
        messages = ((GetDeserializedData<R>) getFromQueue.getAfter()).getMessages();
    }

    public S withDataTransformer(DataTransformer transformer) {
        ((GetDeserializedData<M>) getFromQueue.getAfter()).setTransformer(transformer);
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

    public final static class Mapped<M, T> extends RabbitMqBasicGetIterableSupplier<M, T, Mapped<M, T>> {
        private Mapped(GetFromQueue.MergeProperty<List<M>> getFromQueue, Function<M, T> function) {
            super(getFromQueue, function);
        }

        @Override
        public Mapped<M, T> withDataTransformer(DataTransformer transformer) {
            return super.withDataTransformer(transformer);
        }
    }

    public final static class StringMessages extends RabbitMqBasicGetIterableSupplier<String, String, StringMessages> {
        private StringMessages(GetFromQueue.MergeProperty<List<String>> getFromQueue) {
            super(getFromQueue, s -> s);
            withDataTransformer(new StringDataTransformer());
        }
    }
}
