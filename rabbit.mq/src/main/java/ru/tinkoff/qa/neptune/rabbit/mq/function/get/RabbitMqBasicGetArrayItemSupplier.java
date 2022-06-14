package ru.tinkoff.qa.neptune.rabbit.mq.function.get;

import com.fasterxml.jackson.core.type.TypeReference;
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
@SequentialGetStepSupplier.DefineCriteriaParameterName("Object criteria")
@MaxDepthOfReporting(0)
public class RabbitMqBasicGetArrayItemSupplier<T> extends SequentialGetStepSupplier
    .GetObjectFromArrayChainedStepSupplier<RabbitMqStepContext, T, Channel, RabbitMqBasicGetArrayItemSupplier<T>> {

    final GetFromQueue<?> getFromQueue;

    @CaptureOnSuccess(by = MessageCaptor.class)
    String message;

    @CaptureOnSuccess(by = MessagesCaptor.class)
    @CaptureOnFailure(by = MessagesCaptor.class)
    List<String> messages;

    protected <M> RabbitMqBasicGetArrayItemSupplier(GetFromQueue<M> getFromQueue, Function<M, T[]> function) {
        super(function.compose(getFromQueue));
        this.getFromQueue = getFromQueue;
        from(getChannel());
    }

    /**
     * Creates a step that gets some value from array which is calculated by body of message.
     *
     * @param description is description of value to get
     * @param queue       is a queue to read
     * @param classT      is a class of a value to deserialize message
     * @param toGet       describes how to get desired value
     * @param <M>         is a type of deserialized message
     * @param <T>         is a type of item of array
     * @return an instance of {@link RabbitMqBasicGetArrayItemSupplier}
     */
    @Description("{description}")
    public static <M, T> RabbitMqBasicGetArrayItemSupplier<T> rabbitArrayItem(
            @DescriptionFragment(value = "description",
                    makeReadableBy = ParameterValueGetter.TranslatedDescriptionParameterValueGetter.class
            ) String description,
            String queue,
            Class<M> classT,
            Function<M, T[]> toGet) {
        checkArgument(isNotBlank(description), "Description should be defined");
        return new RabbitMqBasicGetArrayItemSupplier<>(new GetFromQueue<>(queue, classT), toGet);
    }

    /**
     * Creates a step that gets some value from array which is calculated by body of message.
     * It gets required value from default queue.
     *
     * @param description is description of value to get
     * @param classT      is a class of a value to deserialize message
     * @param toGet       describes how to get desired value
     * @param <M>         is a type of deserialized message
     * @param <T>         is a type of item of array
     * @return an instance of {@link RabbitMqBasicGetArrayItemSupplier}
     * @see RabbitMQRoutingProperties#DEFAULT_QUEUE_NAME
     */
    public static <M, T> RabbitMqBasicGetArrayItemSupplier<T> rabbitArrayItem(
            String description,
            Class<M> classT,
            Function<M, T[]> toGet) {
        return rabbitArrayItem(description, DEFAULT_QUEUE_NAME.get(), classT, toGet);
    }

    /**
     * Creates a step that gets some value from array which is calculated by body of message.
     *
     * @param description is description of value to get
     * @param queue       is a queue to read
     * @param typeT       is a reference to type of value to deserialize message
     * @param toGet       describes how to get desired value
     * @param <M>         is a type of deserialized message
     * @param <T>         is a type of an item of array
     * @return an instance of {@link RabbitMqBasicGetArrayItemSupplier}
     */
    @Description("{description}")
    public static <M, T> RabbitMqBasicGetArrayItemSupplier<T> rabbitArrayItem(
            @DescriptionFragment(value = "description",
                    makeReadableBy = ParameterValueGetter.TranslatedDescriptionParameterValueGetter.class
            ) String description,
            String queue,
            TypeReference<M> typeT,
            Function<M, T[]> toGet) {
        checkArgument(isNotBlank(description), "Description should be defined");
        return new RabbitMqBasicGetArrayItemSupplier<>(new GetFromQueue<>(queue, typeT), toGet);
    }

    /**
     * Creates a step that gets some value from array which is calculated by body of message.
     * It gets required value from default queue.
     *
     * @param description is description of value to get
     * @param typeT       is a reference to type of value to deserialize message
     * @param toGet       describes how to get desired value
     * @param <M>         is a type of deserialized message
     * @param <T>         is a type of item of array
     * @return an instance of {@link RabbitMqBasicGetArrayItemSupplier}
     * @see RabbitMQRoutingProperties#DEFAULT_QUEUE_NAME
     */
    public static <M, T> RabbitMqBasicGetArrayItemSupplier<T> rabbitArrayItem(
            String description,
            TypeReference<M> typeT,
            Function<M, T[]> toGet) {
        return rabbitArrayItem(description, DEFAULT_QUEUE_NAME.get(), typeT, toGet);
    }

    /**
     * Creates a step that gets value from array body of message.
     *
     * @param description is description of value to get
     * @param queue       is a queue to read
     * @param classT      is a class of a value to deserialize message
     * @param <T>         is a type of an item of array
     * @return an instance of {@link RabbitMqBasicGetArrayItemSupplier}
     */
    public static <T> RabbitMqBasicGetArrayItemSupplier<T> rabbitArrayItem(
            String description,
            String queue,
            Class<T[]> classT) {
        return rabbitArrayItem(description, queue, classT, ts -> ts);
    }

    /**
     * Creates a step that gets value from array body of message. It gets required value from default queue.
     *
     * @param description is description of value to get
     * @param classT      is a class of a value to deserialize message
     * @param <T>         is a type of an item of array
     * @return an instance of {@link RabbitMqBasicGetArrayItemSupplier}
     * @see RabbitMQRoutingProperties#DEFAULT_QUEUE_NAME
     */
    public static <T> RabbitMqBasicGetArrayItemSupplier<T> rabbitArrayItem(
            String description,
            Class<T[]> classT) {
        return rabbitArrayItem(description, DEFAULT_QUEUE_NAME.get(), classT);
    }

    /**
     * Creates a step that gets value from array body of message.
     *
     * @param description is description of value to get
     * @param queue       is a queue to read
     * @param typeT       is a reference to type of value to deserialize message
     * @param <T>         is a type of an item of array
     * @return an instance of {@link RabbitMqBasicGetArrayItemSupplier}
     */
    public static <T> RabbitMqBasicGetArrayItemSupplier<T> rabbitArrayItem(
            String description,
            String queue,
            TypeReference<T[]> typeT) {
        return rabbitArrayItem(description, queue, typeT, ts -> ts);
    }

    /**
     * Creates a step that gets value from array body of message. It gets required value from default queue.
     *
     * @param description is description of value to get
     * @param typeT       is a reference to type of value to deserialize message
     * @param <T>         is a type of an item of array
     * @return an instance of {@link RabbitMqBasicGetArrayItemSupplier}
     * @see RabbitMQRoutingProperties#DEFAULT_QUEUE_NAME
     */
    public static <T> RabbitMqBasicGetArrayItemSupplier<T> rabbitArrayItem(
            String description,
            TypeReference<T[]> typeT) {
        return rabbitArrayItem(description, DEFAULT_QUEUE_NAME.get(), typeT);
    }

    @Override
    public RabbitMqBasicGetArrayItemSupplier<T> timeOut(Duration timeOut) {
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
    protected void onFailure(Channel m, Throwable throwable) {
        messages = getFromQueue.getMessages();
    }

    public RabbitMqBasicGetArrayItemSupplier<T> withDataTransformer(DataTransformer transformer) {
        getFromQueue.setTransformer(transformer);
        return this;
    }

    /**
     * It means that server should consider messages acknowledged once delivered.
     *
     * @return self-reference
     */
    public RabbitMqBasicGetArrayItemSupplier<T> autoAck() {
        this.getFromQueue.setAutoAck();
        return this;
    }
}
