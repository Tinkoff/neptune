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
import static com.google.common.base.Preconditions.checkNotNull;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static ru.tinkoff.qa.neptune.rabbit.mq.properties.RabbitMQRoutingProperties.DEFAULT_QUEUE_NAME;

@SequentialGetStepSupplier.DefineGetImperativeParameterName("Retrieve:")
@SequentialGetStepSupplier.DefineTimeOutParameterName("Time of the waiting")
@SequentialGetStepSupplier.DefineCriteriaParameterName("Object criteria")
@MaxDepthOfReporting(0)
public class RabbitMqBasicGetSupplier<T> extends SequentialGetStepSupplier.GetObjectStepSupplier<RabbitMqStepContext, T, RabbitMqBasicGetSupplier<T>> {

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

    /**
     * Creates a step that gets some value which is calculated by body of message.
     *
     * @param description is description of value to get
     * @param queue       is a queue to read
     * @param autoAck     true if the server should consider messages
     *                    acknowledged once delivered; false if the server should expect
     *                    explicit acknowledgements
     * @param classT      is a class of a value to deserialize message
     * @param toGet       describes how to get desired value
     * @param <M>         is a type of deserialized message
     * @param <T>         is a type of a target value
     * @return an instance of {@link RabbitMqBasicGetSupplier}
     */
    @Description("{description}")
    public static <M, T> RabbitMqBasicGetSupplier<T> rabbitObject(
            @DescriptionFragment(value = "description",
                    makeReadableBy = ParameterValueGetter.TranslatedDescriptionParameterValueGetter.class
            ) String description,
            String queue,
            boolean autoAck,
            Class<M> classT,
            Function<M, T> toGet) {
        checkArgument(isNotBlank(description), "Description should be defined");
        return new RabbitMqBasicGetSupplier<>(new GetFromQueue<>(queue, autoAck, classT), toGet);
    }

    /**
     * Creates a step that gets some value which is calculated by body of message. It gets required value from
     * default queue.
     *
     * @param description is description of value to get
     * @param autoAck     true if the server should consider messages
     *                    acknowledged once delivered; false if the server should expect
     *                    explicit acknowledgements
     * @param classT      is a class of a value to deserialize message
     * @param toGet       describes how to get desired value
     * @param <M>         is a type of deserialized message
     * @param <T>         is a type of a target value
     * @return an instance of {@link RabbitMqBasicGetSupplier}
     * @see RabbitMQRoutingProperties#DEFAULT_QUEUE_NAME
     */
    public static <M, T> RabbitMqBasicGetSupplier<T> rabbitObject(String description,
                                                                  boolean autoAck,
                                                                  Class<M> classT,
                                                                  Function<M, T> toGet) {
        return rabbitObject(description, DEFAULT_QUEUE_NAME.get(), autoAck, classT, toGet);
    }

    /**
     * Creates a step that gets some value which is calculated by body of message.
     *
     * @param description is description of value to get
     * @param queue       is a queue to read
     * @param autoAck     true if the server should consider messages
     *                    acknowledged once delivered; false if the server should expect
     *                    explicit acknowledgements
     * @param typeT       is a reference to type of a value to deserialize message
     * @param toGet       describes how to get desired value
     * @param <M>         is a type of deserialized message
     * @param <T>         is a type of a target value
     * @return an instance of {@link RabbitMqBasicGetSupplier}
     */
    @Description("{description}")
    public static <M, T> RabbitMqBasicGetSupplier<T> rabbitObject(
            @DescriptionFragment(value = "description",
                    makeReadableBy = ParameterValueGetter.TranslatedDescriptionParameterValueGetter.class
            ) String description,
            String queue,
            boolean autoAck,
            TypeReference<M> typeT,
            Function<M, T> toGet) {
        checkArgument(isNotBlank(description), "Description should be defined");
        return new RabbitMqBasicGetSupplier<>(new GetFromQueue<>(queue, autoAck, typeT), toGet);
    }

    /**
     * Creates a step that gets some value which is calculated by body of message. It gets required value from
     * default queue.
     *
     * @param description is description of value to get
     * @param autoAck     true if the server should consider messages
     *                    acknowledged once delivered; false if the server should expect
     *                    explicit acknowledgements
     * @param typeT       is a reference to type of a value to deserialize message
     * @param toGet       describes how to get desired value
     * @param <M>         is a type of deserialized message
     * @param <T>         is a type of a target value
     * @return an instance of {@link RabbitMqBasicGetSupplier}
     * @see RabbitMQRoutingProperties#DEFAULT_QUEUE_NAME
     */
    public static <M, T> RabbitMqBasicGetSupplier<T> rabbitObject(String description,
                                                                  boolean autoAck,
                                                                  TypeReference<M> typeT,
                                                                  Function<M, T> toGet) {
        return rabbitObject(description, DEFAULT_QUEUE_NAME.get(), autoAck, typeT, toGet);
    }

    /**
     * Creates a step that gets body of message.
     *
     * @param queue   is a queue to read
     * @param autoAck true if the server should consider messages
     *                acknowledged once delivered; false if the server should expect
     *                explicit acknowledgements
     * @param classT  is a class of a value to deserialize message
     * @param <T>     is a type of deserialized message
     * @return an instance of {@link RabbitMqBasicGetSupplier}
     */
    @Description("Message body")
    public static <T> RabbitMqBasicGetSupplier<T> rabbitBody(String queue,
                                                             boolean autoAck,
                                                             Class<T> classT) {
        return new RabbitMqBasicGetSupplier<>(new GetFromQueue<>(queue, autoAck, classT), t -> t);
    }

    /**
     * Creates a step that gets body of message. It gets required value from default queue.
     *
     * @param autoAck true if the server should consider messages
     *                acknowledged once delivered; false if the server should expect
     *                explicit acknowledgements
     * @param classT  is a class of a value to deserialize message
     * @param <T>     is a type of deserialized message
     * @return an instance of {@link RabbitMqBasicGetSupplier}
     * @see RabbitMQRoutingProperties#DEFAULT_QUEUE_NAME
     */
    public static <T> RabbitMqBasicGetSupplier<T> rabbitBody(boolean autoAck,
                                                             Class<T> classT) {
        return rabbitBody(DEFAULT_QUEUE_NAME.get(), autoAck, classT);
    }

    /**
     * Creates a step that gets body of message.
     *
     * @param queue   is a queue to read
     * @param autoAck true if the server should consider messages
     *                acknowledged once delivered; false if the server should expect
     *                explicit acknowledgements
     * @param typeT   is a reference to type of a value to deserialize message
     * @param <T>     is a type of deserialized message
     * @return an instance of {@link RabbitMqBasicGetSupplier}
     */
    @Description("Message body")
    public static <T> RabbitMqBasicGetSupplier<T> rabbitBody(String queue,
                                                             boolean autoAck,
                                                             TypeReference<T> typeT) {
        return new RabbitMqBasicGetSupplier<>(new GetFromQueue<>(queue, autoAck, typeT), t -> t);
    }

    /**
     * Creates a step that gets body of message. It gets required value from default queue.
     * *
     *
     * @param autoAck true if the server should consider messages
     *                acknowledged once delivered; false if the server should expect
     *                explicit acknowledgements
     * @param typeT   is a reference to type of a value to deserialize message
     * @param <T>     is a type of deserialized message
     * @return an instance of {@link RabbitMqBasicGetSupplier}
     * @see RabbitMQRoutingProperties#DEFAULT_QUEUE_NAME
     */
    public static <T> RabbitMqBasicGetSupplier<T> rabbitBody(boolean autoAck,
                                                             TypeReference<T> typeT) {
        return rabbitBody(DEFAULT_QUEUE_NAME.get(), autoAck, typeT);
    }

    @Override
    public RabbitMqBasicGetSupplier<T> timeOut(Duration timeOut) {
        return super.timeOut(timeOut);
    }

    @Override
    protected void onSuccess(T t) {
        var ms = getFromQueue.getMessages();
        if (t != null) {
            message = ms.getLast();
        }
        else {
            messages = ms;
        }
    }

    @Override
    protected void onFailure(RabbitMqStepContext m, Throwable throwable) {
        messages = getFromQueue.getMessages();
    }

    RabbitMqBasicGetSupplier<T> setDataTransformer(DataTransformer dataTransformer) {
        checkNotNull(dataTransformer);
        getFromQueue.setTransformer(dataTransformer);
        return this;
    }
}
