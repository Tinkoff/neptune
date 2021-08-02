package ru.tinkoff.qa.neptune.rabbit.mq.function.get;

import com.fasterxml.jackson.core.type.TypeReference;
import ru.tinkoff.qa.neptune.core.api.data.format.DataTransformer;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotations.CaptureOnFailure;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotations.CaptureOnSuccess;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotations.MaxDepthOfReporting;
import ru.tinkoff.qa.neptune.core.api.steps.Criteria;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;
import ru.tinkoff.qa.neptune.core.api.steps.parameters.ParameterValueGetter;
import ru.tinkoff.qa.neptune.rabbit.mq.RabbitMqStepContext;
import ru.tinkoff.qa.neptune.rabbit.mq.captors.MessageCaptor;
import ru.tinkoff.qa.neptune.rabbit.mq.captors.MessagesCaptor;

import java.time.Duration;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@SequentialGetStepSupplier.DefineGetImperativeParameterName("Retrieve:")
@SequentialGetStepSupplier.DefineTimeOutParameterName("Time of the waiting")
@SequentialGetStepSupplier.DefineCriteriaParameterName("Object criteria")
@MaxDepthOfReporting(0)
public class RabbitMqBasicGetIterableItemSupplier<T> extends SequentialGetStepSupplier
        .GetObjectFromIterableStepSupplier<RabbitMqStepContext, T, RabbitMqBasicGetIterableItemSupplier<T>> {

    final GetFromQueue<?> getFromQueue;

    @CaptureOnSuccess(by = MessageCaptor.class)
    String message;

    @CaptureOnSuccess(by = MessagesCaptor.class)
    @CaptureOnFailure(by = MessagesCaptor.class)
    List<String> messages;

    protected <M, S extends Iterable<T>> RabbitMqBasicGetIterableItemSupplier(GetFromQueue<M> getFromQueue, Function<M, S> function) {
        super(function.compose(getFromQueue));
        this.getFromQueue = getFromQueue;
    }

    /**
     * Creates a step that gets some value from iterable which is calculated by body of message.
     *
     * @param description is description of value to get
     * @param queue       is a queue to read
     * @param autoAck     true if the server should consider messages
     *                    acknowledged once delivered; false if the server should expect
     *                    explicit acknowledgements
     * @param classT      is a class of a value to deserialize body
     * @param toGet       describes how to get desired value
     * @param <M>         is a type of deserialized body
     * @param <T>         is a type of an item of iterable
     * @param <S>         is a type of iterable
     * @return an instance of {@link RabbitMqBasicGetIterableItemSupplier}
     */
    @Description("{description}")
    public static <M, T, S extends Iterable<T>> RabbitMqBasicGetIterableItemSupplier<T> rabbitIterableItem(
            @DescriptionFragment(value = "description",
                    makeReadableBy = ParameterValueGetter.TranslatedDescriptionParameterValueGetter.class
            ) String description,
            String queue,
            boolean autoAck,
            Class<M> classT,
            Function<M, S> toGet) {
        checkArgument(isNotBlank(description), "Description should be defined");
        return new RabbitMqBasicGetIterableItemSupplier<>(new GetFromQueue<>(queue, autoAck, classT), toGet);
    }

    /**
     * Creates a step that gets some value from iterable which is calculated by body of message.
     *
     * @param description is description of value to get
     * @param queue       is a queue to read
     * @param autoAck     true if the server should consider messages
     *                    acknowledged once delivered; false if the server should expect
     *                    explicit acknowledgements
     * @param typeT       is a reference to type of a value to deserialize body
     * @param toGet       describes how to get desired value
     * @param <M>         is a type of deserialized body
     * @param <T>         is a type of an item of iterable
     * @param <S>         is a type of iterable
     * @return an instance of {@link RabbitMqBasicGetIterableItemSupplier}
     */
    @Description("{description}")
    public static <M, T, S extends Iterable<T>> RabbitMqBasicGetIterableItemSupplier<T> rabbitIterableItem(
            @DescriptionFragment(value = "description",
                    makeReadableBy = ParameterValueGetter.TranslatedDescriptionParameterValueGetter.class
            ) String description,
            String queue,
            boolean autoAck,
            TypeReference<M> typeT,
            Function<M, S> toGet) {
        checkArgument(isNotBlank(description), "Description should be defined");
        return new RabbitMqBasicGetIterableItemSupplier<>(new GetFromQueue<>(queue, autoAck, typeT), toGet);
    }

    /**
     * Creates a step that gets some value from iterable body of message.
     *
     * @param description is description of value to get
     * @param queue       is a queue to read
     * @param autoAck     true if the server should consider messages
     *                    acknowledged once delivered; false if the server should expect
     *                    explicit acknowledgements
     * @param classT      is a class of a value to deserialize body
     * @param <T>         is a type of an item of iterable
     * @param <S>         is a type of iterable
     * @return an instance of {@link RabbitMqBasicGetIterableItemSupplier}
     */
    public static <T, S extends Iterable<T>> RabbitMqBasicGetIterableItemSupplier<T> rabbitIterableItem(
            String description,
            String queue,
            boolean autoAck,
            Class<S> classT) {
        checkArgument(isNotBlank(description), "Description should be defined");
        return rabbitIterableItem(description, queue, autoAck, classT, ts -> ts);
    }

    /**
     * Creates a step that gets some value from iterable body of message.
     *
     * @param description is description of value to get
     * @param queue       is a queue to read
     * @param autoAck     true if the server should consider messages
     *                    acknowledged once delivered; false if the server should expect
     *                    explicit acknowledgements
     * @param typeT       is a reference to type of a value to deserialize body
     * @param <T>         is a type of an item of iterable
     * @param <S>         is a type of iterable
     * @return an instance of {@link RabbitMqBasicGetIterableItemSupplier}
     */
    public static <T, S extends Iterable<T>> RabbitMqBasicGetIterableItemSupplier<T> rabbitIterableItem(
            String description,
            String queue,
            boolean autoAck,
            TypeReference<S> typeT) {
        return rabbitIterableItem(description, queue, autoAck, typeT, ts -> ts);
    }

    @Override
    public RabbitMqBasicGetIterableItemSupplier<T> timeOut(Duration timeOut) {
        return super.timeOut(timeOut);
    }

    @Override
    public RabbitMqBasicGetIterableItemSupplier<T> criteria(String description, Predicate<? super T> predicate) {
        return super.criteria(description, predicate);
    }

    @Override
    public RabbitMqBasicGetIterableItemSupplier<T> criteria(Criteria<? super T> criteria) {
        return super.criteria(criteria);
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

    RabbitMqBasicGetIterableItemSupplier<T> setDataTransformer(DataTransformer dataTransformer) {
        checkNotNull(dataTransformer);
        getFromQueue.setTransformer(dataTransformer);
        return this;
    }
}
