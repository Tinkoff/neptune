package ru.tinkoff.qa.neptune.rabbit.mq.function.get;

import com.fasterxml.jackson.core.type.TypeReference;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.MaxDepthOfReporting;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;
import ru.tinkoff.qa.neptune.core.api.steps.parameters.ParameterValueGetter;
import ru.tinkoff.qa.neptune.rabbit.mq.properties.RabbitMQRoutingProperties;

import java.nio.charset.Charset;
import java.util.function.Function;

import static com.google.common.base.Preconditions.checkArgument;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static ru.tinkoff.qa.neptune.rabbit.mq.function.get.GetDeserializedData.getStringResult;
import static ru.tinkoff.qa.neptune.rabbit.mq.properties.RabbitMQRoutingProperties.DEFAULT_QUEUE_NAME;

@SequentialGetStepSupplier.DefineGetImperativeParameterName("Retrieve:")
@SequentialGetStepSupplier.DefineTimeOutParameterName("Time of the waiting")
@SequentialGetStepSupplier.DefineCriteriaParameterName("Object criteria")
@MaxDepthOfReporting(0)
@SuppressWarnings({"unused", "unchecked"})
public class RabbitMqBasicGetSupplier {

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
    @Deprecated
    @Description("{description}")
    public static <M, T> RabbitMqBasicGetIterableItemSupplier.Mapped<M, T> rabbitObject(
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
    @Deprecated
    public static <M, T> RabbitMqBasicGetIterableItemSupplier.Mapped<M, T> rabbitObject(String description,
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
    @Deprecated
    @Description("{description}")
    public static <M, T> RabbitMqBasicGetIterableItemSupplier.Mapped<M, T> rabbitObject(
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
    @Deprecated
    public static <M, T> RabbitMqBasicGetIterableItemSupplier.Mapped<M, T> rabbitObject(String description,
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
    public static <T> RabbitMqBasicGetIterableItemSupplier.Mapped<T, T> rabbitBody(String queue,
                                                                                   Class<T> classT) {
        return new RabbitMqBasicGetIterableItemSupplier.Mapped<>(new GetFromQueue(queue).andThen(new GetDeserializedData<>(classT)), t -> t);
    }

    /**
     * Creates a step that gets body of message. It gets required value from default queue.
     *
     * @param classT is a class of a value to deserialize message
     * @param <T>    is a type of deserialized message
     * @return an instance of {@link RabbitMqBasicGetSupplier}
     * @see RabbitMQRoutingProperties#DEFAULT_QUEUE_NAME
     */
    public static <T> RabbitMqBasicGetIterableItemSupplier.Mapped<T, T> rabbitBody(Class<T> classT) {
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
    public static <T> RabbitMqBasicGetIterableItemSupplier.Mapped<T, T> rabbitBody(String queue,
                                                                                   TypeReference<T> typeT) {
        return new RabbitMqBasicGetIterableItemSupplier.Mapped<>(new GetFromQueue(queue).andThen(new GetDeserializedData<>(typeT)), t -> t);
    }

    /**
     * Creates a step that gets body of message. It gets required value from default queue.
     *
     * @param typeT is a reference to type of value to deserialize message
     * @param <T>   is a type of deserialized message
     * @return an instance of {@link RabbitMqBasicGetSupplier}
     * @see RabbitMQRoutingProperties#DEFAULT_QUEUE_NAME
     */
    public static <T> RabbitMqBasicGetIterableItemSupplier.Mapped<T, T> rabbitBody(TypeReference<T> typeT) {
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
    public static RabbitMqBasicGetIterableItemSupplier.Mapped<String, String> rabbitRawMessage(String queue, Charset charset) {
        return new RabbitMqBasicGetIterableItemSupplier.Mapped<>(new GetFromQueue(queue).andThen(getStringResult(charset)), t -> t);
    }

    /**
     * Creates a step that gets string body of message. It gets required value from default queue.
     *
     * @param charset is a required charset
     * @return an instance of {@link RabbitMqBasicGetSupplier}
     * @see RabbitMQRoutingProperties#DEFAULT_QUEUE_NAME
     */
    public static RabbitMqBasicGetIterableItemSupplier.Mapped<String, String> rabbitRawMessage(Charset charset) {
        return rabbitRawMessage(DEFAULT_QUEUE_NAME.get(), charset);
    }

    /**
     * Creates a step that gets string body of message.
     *
     * @param queue is a queue to read
     * @return an instance of {@link RabbitMqBasicGetSupplier}
     */
    public static RabbitMqBasicGetIterableItemSupplier.Mapped<String, String> rabbitRawMessage(String queue) {
        return rabbitRawMessage(queue, UTF_8);
    }

    /**
     * Creates a step that gets string body of message. It gets required value from default queue.
     *
     * @return an instance of {@link RabbitMqBasicGetSupplier}
     * @see RabbitMQRoutingProperties#DEFAULT_QUEUE_NAME
     */
    public static RabbitMqBasicGetIterableItemSupplier.Mapped<String, String> rabbitRawMessage() {
        return rabbitRawMessage(UTF_8);
    }
}
