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

import java.nio.charset.Charset;
import java.time.Duration;
import java.util.List;
import java.util.function.Function;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Objects.nonNull;
import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static ru.tinkoff.qa.neptune.rabbit.mq.function.get.GetFromQueue.getStringResult;
import static ru.tinkoff.qa.neptune.rabbit.mq.properties.RabbitMQRoutingProperties.DEFAULT_QUEUE_NAME;
import static ru.tinkoff.qa.neptune.rabbit.mq.properties.RabbitMqDefaultDataTransformer.RABBIT_MQ_DEFAULT_DATA_TRANSFORMER;

@SequentialGetStepSupplier.DefineGetImperativeParameterName("Retrieve:")
@SequentialGetStepSupplier.DefineTimeOutParameterName("Time of the waiting")
@SequentialGetStepSupplier.DefineCriteriaParameterName("Object criteria")
@MaxDepthOfReporting(0)
@SuppressWarnings("unchecked")
public abstract class RabbitMqBasicGetSupplier<T, R extends RabbitMqBasicGetSupplier<T, R>>
        extends SequentialGetStepSupplier.GetObjectStepSupplier<RabbitMqStepContext, T, R> {

    final GetFromQueue<?> getFromQueue;

    @CaptureOnSuccess(by = MessageCaptor.class)
    String message;

    @CaptureOnSuccess(by = MessagesCaptor.class)
    @CaptureOnFailure(by = MessagesCaptor.class)
    List<String> messages;

    protected <M> RabbitMqBasicGetSupplier(GetFromQueue<M> getFromQueue, Function<M, T> function) {
        super(function.compose(getFromQueue));
        this.getFromQueue = getFromQueue;
    }

    private DataTransformer transformer;

    /**
     * Creates a step that gets some value which is calculated by body of message.
     *
     * @param description is description of value to get
     * @param queue       is a queue to read
     * @param classT      is a class of a value to deserialize message
     * @param toGet       describes how to get desired value
     * @param <M>         is a type of deserialized message
     * @param <T>         is a type of target value
     * @return an instance of {@link RabbitMqBasicGetSupplier}
     */
    @Description("{description}")
    public static <M, T> Mapped<T> rabbitObject(
            @DescriptionFragment(value = "description",
                    makeReadableBy = ParameterValueGetter.TranslatedDescriptionParameterValueGetter.class
            ) String description,
            String queue,
            Class<M> classT,
            Function<M, T> toGet) {
        checkArgument(isNotBlank(description), "Description should be defined");
        return new RabbitMqBasicGetSupplier.Mapped<>(new GetFromQueue<>(queue, classT), toGet);
    }

    /**
     * Creates a step that gets some value which is calculated by body of message. It gets required value from
     * default queue.
     *
     * @param description is description of value to get
     * @param classT      is a class of a value to deserialize message
     * @param toGet       describes how to get desired value
     * @param <M>         is a type of deserialized message
     * @param <T>         is a type of target value
     * @return an instance of {@link RabbitMqBasicGetSupplier}
     * @see RabbitMQRoutingProperties#DEFAULT_QUEUE_NAME
     */
    public static <M, T> Mapped<T> rabbitObject(String description,
                                                Class<M> classT,
                                                Function<M, T> toGet) {
        return rabbitObject(description, DEFAULT_QUEUE_NAME.get(), classT, toGet);
    }

    /**
     * Creates a step that gets some value which is calculated by body of message.
     *
     * @param description is description of value to get
     * @param queue       is a queue to read
     * @param typeT       is a reference to type of value to deserialize message
     * @param toGet       describes how to get desired value
     * @param <M>         is a type of deserialized message
     * @param <T>         is a type of target value
     * @return an instance of {@link RabbitMqBasicGetSupplier}
     */
    @Description("{description}")
    public static <M, T> Mapped<T> rabbitObject(
            @DescriptionFragment(value = "description",
                    makeReadableBy = ParameterValueGetter.TranslatedDescriptionParameterValueGetter.class
            ) String description,
            String queue,
            TypeReference<M> typeT,
            Function<M, T> toGet) {
        checkArgument(isNotBlank(description), "Description should be defined");
        return new RabbitMqBasicGetSupplier.Mapped<>(new GetFromQueue<>(queue, typeT), toGet);
    }

    /**
     * Creates a step that gets some value which is calculated by body of message. It gets required value from
     * default queue.
     *
     * @param description is description of value to get
     * @param typeT       is a reference to type of value to deserialize message
     * @param toGet       describes how to get desired value
     * @param <M>         is a type of deserialized message
     * @param <T>         is a type of target value
     * @return an instance of {@link RabbitMqBasicGetSupplier}
     * @see RabbitMQRoutingProperties#DEFAULT_QUEUE_NAME
     */
    public static <M, T> Mapped<T> rabbitObject(String description,
                                                TypeReference<M> typeT,
                                                Function<M, T> toGet) {
        return rabbitObject(description, DEFAULT_QUEUE_NAME.get(), typeT, toGet);
    }

    /**
     * Creates a step that gets body of message.
     *
     * @param queue  is a queue to read
     * @param classT is a class of a value to deserialize message
     * @param <T>    is a type of deserialized message
     * @return an instance of {@link RabbitMqBasicGetSupplier}
     */
    @Description("Message body")
    public static <T> Mapped<T> rabbitBody(String queue,
                                           Class<T> classT) {
        return new RabbitMqBasicGetSupplier.Mapped<>(new GetFromQueue<>(queue, classT), t -> t);
    }

    /**
     * Creates a step that gets body of message. It gets required value from default queue.
     *
     * @param classT is a class of a value to deserialize message
     * @param <T>    is a type of deserialized message
     * @return an instance of {@link RabbitMqBasicGetSupplier}
     * @see RabbitMQRoutingProperties#DEFAULT_QUEUE_NAME
     */
    public static <T> Mapped<T> rabbitBody(Class<T> classT) {
        return rabbitBody(DEFAULT_QUEUE_NAME.get(), classT);
    }

    /**
     * Creates a step that gets body of message.
     *
     * @param queue is a queue to read
     * @param typeT is a reference to type of value to deserialize message
     * @param <T>   is a type of deserialized message
     * @return an instance of {@link RabbitMqBasicGetSupplier}
     */
    @Description("Message body")
    public static <T> Mapped<T> rabbitBody(String queue,
                                           TypeReference<T> typeT) {
        return new RabbitMqBasicGetSupplier.Mapped<>(new GetFromQueue<>(queue, typeT), t -> t);
    }

    /**
     * Creates a step that gets body of message. It gets required value from default queue.
     *
     * @param typeT is a reference to type of value to deserialize message
     * @param <T>   is a type of deserialized message
     * @return an instance of {@link RabbitMqBasicGetSupplier}
     * @see RabbitMQRoutingProperties#DEFAULT_QUEUE_NAME
     */
    public static <T> Mapped<T> rabbitBody(TypeReference<T> typeT) {
        return rabbitBody(DEFAULT_QUEUE_NAME.get(), typeT);
    }

    /**
     * Creates a step that gets string body of message.
     *
     * @param queue   is a queue to read
     * @param charset is a required charset
     * @return an instance of {@link RabbitMqBasicGetSupplier}
     */
    @Description("String message")
    public static StringMessage rabbitRawMessage(String queue, Charset charset) {
        return new StringMessage(getStringResult(queue, charset));
    }

    /**
     * Creates a step that gets string body of message. It gets required value from default queue.
     *
     * @param charset is a required charset
     * @return an instance of {@link RabbitMqBasicGetSupplier}
     * @see RabbitMQRoutingProperties#DEFAULT_QUEUE_NAME
     */
    public static StringMessage rabbitRawMessage(Charset charset) {
        return rabbitRawMessage(DEFAULT_QUEUE_NAME.get(), charset);
    }

    /**
     * Creates a step that gets string body of message.
     *
     * @param queue is a queue to read
     * @return an instance of {@link RabbitMqBasicGetSupplier}
     */
    public static StringMessage rabbitRawMessage(String queue) {
        return rabbitRawMessage(queue, UTF_8);
    }

    /**
     * Creates a step that gets string body of message. It gets required value from default queue.
     *
     * @return an instance of {@link RabbitMqBasicGetSupplier}
     * @see RabbitMQRoutingProperties#DEFAULT_QUEUE_NAME
     */
    public static StringMessage rabbitRawMessage() {
        return rabbitRawMessage(UTF_8);
    }

    @Override
    public R timeOut(Duration timeOut) {
        return super.timeOut(timeOut);
    }

    @Override
    protected void onSuccess(T t) {
        var ms = getFromQueue.getMessages();
        if (t != null) {
            message = ms.getLast();
        } else {
            messages = ms;
        }
    }

    @Override
    protected void onFailure(RabbitMqStepContext m, Throwable throwable) {
        messages = getFromQueue.getMessages();
    }

    @Override
    protected void onStart(RabbitMqStepContext rabbitMqStepContext) {
        var transformer = ofNullable(this.transformer)
                .orElseGet(RABBIT_MQ_DEFAULT_DATA_TRANSFORMER);
        checkState(nonNull(transformer), "Data transformer is not defined. Please invoke "
                + "the '#withDataTransformer(DataTransformer)' method or define '"
                + RABBIT_MQ_DEFAULT_DATA_TRANSFORMER.getName()
                + "' property/env variable");
        getFromQueue.setTransformer(transformer);
    }

    R withDataTransformer(DataTransformer transformer) {
        this.transformer = transformer;
        return (R) this;
    }

    /**
     * It means that server should consider messages acknowledged once delivered.
     *
     * @return self-reference
     */
    public R autoAck() {
        this.getFromQueue.setAutoAck();
        return (R) this;
    }

    public static final class Mapped<T> extends RabbitMqBasicGetSupplier<T, Mapped<T>> {

        private <M> Mapped(GetFromQueue<M> getFromQueue, Function<M, T> function) {
            super(getFromQueue, function);
        }

        public Mapped<T> withDataTransformer(DataTransformer transformer) {
            return super.withDataTransformer(transformer);
        }
    }

    @SuppressWarnings("unchecked")
    public static final class StringMessage extends RabbitMqBasicGetSupplier<String, StringMessage> {

        private StringMessage(GetFromQueue<String> getFromQueue) {
            super(getFromQueue, s -> s);
            withDataTransformer(new DataTransformer() {
                @Override
                public <T> T deserialize(String string, Class<T> cls) {
                    return (T) string;
                }

                @Override
                public <T> T deserialize(String string, TypeReference<T> type) {
                    return (T) string;
                }

                @Override
                public String serialize(Object obj) {
                    return obj.toString();
                }
            });
        }
    }
}
