package ru.tinkoff.qa.neptune.kafka.functions.poll;

import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import ru.tinkoff.qa.neptune.core.api.data.format.DataTransformer;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotations.CaptureOnFailure;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotations.CaptureOnSuccess;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotations.MaxDepthOfReporting;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;
import ru.tinkoff.qa.neptune.core.api.steps.parameters.ParameterValueGetter;
import ru.tinkoff.qa.neptune.kafka.KafkaStepContext;
import ru.tinkoff.qa.neptune.kafka.captors.AllMessagesCaptor;
import ru.tinkoff.qa.neptune.kafka.properties.KafkaDefaultTopicsForPollProperty;

import java.time.Duration;
import java.util.List;
import java.util.function.Function;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;
import static java.util.Objects.nonNull;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static ru.tinkoff.qa.neptune.kafka.GetConsumer.getConsumer;
import static ru.tinkoff.qa.neptune.kafka.functions.poll.GetFromTopics.getStringResult;
import static ru.tinkoff.qa.neptune.kafka.properties.DefaultDataTransformers.KAFKA_DEFAULT_DATA_TRANSFORMER;

@SequentialGetStepSupplier.DefineGetImperativeParameterName("Poll:")
@SequentialGetStepSupplier.DefineTimeOutParameterName("Time of the waiting")
@SequentialGetStepSupplier.DefineCriteriaParameterName("Object criteria")
@MaxDepthOfReporting(0)
@SuppressWarnings("unchecked")
public abstract class KafkaPollIterableItemSupplier<T, I extends KafkaPollIterableItemSupplier<T, I>>
    extends SequentialGetStepSupplier.GetObjectFromIterableChainedStepSupplier<KafkaStepContext, T, KafkaConsumer<String, String>, I> {

    final GetFromTopics<?> getFromTopics;

    @CaptureOnSuccess(by = AllMessagesCaptor.class)
    @CaptureOnFailure(by = AllMessagesCaptor.class)
    List<String> messages;

    private DataTransformer transformer;

    protected <S> KafkaPollIterableItemSupplier(GetFromTopics<S> getFromTopics, Function<S, T> originalFunction) {
        super(getFromTopics.andThen(list -> list.stream().map(originalFunction).collect(toList())));
        this.getFromTopics = getFromTopics;
        from(getConsumer());
    }

    /**
     * Creates a step that returns value which is calculated by data of a read message.
     * <p></p>
     * It is not necessary to define {@code topics}. If there is no topic defined then value of the property
     * {@link KafkaDefaultTopicsForPollProperty#DEFAULT_TOPICS_FOR_POLL} is used.
     *
     * @param description    is description of value to get
     * @param cls         is a class of a value to deserialize a message from topics
     * @param toGet          describes how to get desired value
     * @param topics         are topics to get messages from
     * @param <M>            is a type of deserialized message
     * @param <T>            is a type of resulted value
     * @return an instance of {@link KafkaPollIterableItemSupplier.Mapped}
     */
    @Description("{description}")
    public static <M, T> Mapped<T> kafkaIterableItem(
            @DescriptionFragment(value = "description",
                    makeReadableBy = ParameterValueGetter.TranslatedDescriptionParameterValueGetter.class
            ) String description,
            Class<M> cls,
            Function<M, T> toGet,
            String... topics) {
        checkArgument(isNotBlank(description), "Description should be defined");
        return new KafkaPollIterableItemSupplier.Mapped<>(new GetFromTopics<>(cls, topics), toGet);
    }

    /**
     * Creates a step that returns value which is calculated by data of a read message.
     * <p></p>
     * It is not necessary to define {@code topics}. If there is no topic defined then value of the property
     * {@link KafkaDefaultTopicsForPollProperty#DEFAULT_TOPICS_FOR_POLL} is used.
     *
     * @param description    is description of value to get
     * @param typeT       is a reference to type of value to deserialize message
     * @param toGet          describes how to get desired value
     * @param topics         are topics to get messages from
     * @param <M>            is a type of deserialized message
     * @param <T>            is a type of resulted value
     * @return an instance of {@link KafkaPollIterableItemSupplier.Mapped}
     */
    @Description("{description}")
    public static <M, T> Mapped<T> kafkaIterableItem(
            @DescriptionFragment(value = "description",
                    makeReadableBy = ParameterValueGetter.TranslatedDescriptionParameterValueGetter.class
            ) String description,
            TypeReference<M> typeT,
            Function<M, T> toGet,
            String... topics) {
        checkArgument(isNotBlank(description), "Description should be defined");
        return new KafkaPollIterableItemSupplier.Mapped<>(new GetFromTopics<>(typeT, topics), toGet);
    }

    /**
     * Creates a step that returns a deserialized message.
     * <p></p>
     * It is not necessary to define {@code topics}. If there is no topic defined then value of the property
     * {@link KafkaDefaultTopicsForPollProperty#DEFAULT_TOPICS_FOR_POLL} is used.
     *
     * @param description is description of value to get
     * @param cls         is a class of a value to deserialize a message from topics
     * @param topics      are topics to get messages from
     * @param <M>         is a type of deserialized message
     * @return an instance of {@link KafkaPollIterableItemSupplier.Mapped}
     */
    public static <M> Mapped<M> kafkaIterableItem(
            String description,
            Class<M> cls,
            String... topics) {
        return kafkaIterableItem(description, cls, t -> t, topics);
    }

    /**
     * Creates a step that returns a deserialized message.
     * <p></p>
     * It is not necessary to define {@code topics}. If there is no topic defined then value of the property
     * {@link KafkaDefaultTopicsForPollProperty#DEFAULT_TOPICS_FOR_POLL} is used.
     *
     * @param description is description of value to get
     * @param typeT       is a reference to type of value to deserialize message
     * @param topics      are topics to get messages from
     * @param <M>         is a type of deserialized message
     * @return an instance of {@link KafkaPollIterableItemSupplier.Mapped}
     */
    public static <M> Mapped<M> kafkaIterableItem(
            String description,
            TypeReference<M> typeT,
            String... topics) {
        return kafkaIterableItem(description, typeT, t -> t, topics);
    }

    /**
     * Creates a step that returns a string content of a message.
     * <p></p>
     * It is not necessary to define {@code topics}. If there is no topic defined then value of the property
     * {@link KafkaDefaultTopicsForPollProperty#DEFAULT_TOPICS_FOR_POLL} is used.
     *
     * @param topics      are topics to get messages from
     * @return an instance of {@link KafkaPollIterableItemSupplier.StringMessage}
     */
    @Description("String message")
    public static StringMessage kafkaRawMessage(String... topics) {
        return new StringMessage(getStringResult(topics));
    }

    @Override
    protected void onStart(KafkaConsumer<String, String> consumer) {
        var transformer = ofNullable(this.transformer)
            .orElseGet(KAFKA_DEFAULT_DATA_TRANSFORMER);
        checkState(nonNull(transformer), "Data transformer is not defined. Please invoke "
            + "the '#withDataTransformer(DataTransformer)' method or define '"
            + KAFKA_DEFAULT_DATA_TRANSFORMER.getName()
            + "' property/env variable");
        getFromTopics.setTransformer(transformer);
    }

    I withDataTransformer(DataTransformer dataTransformer) {
        this.transformer = dataTransformer;
        return (I) this;
    }

    @Override
    public I timeOut(Duration timeOut) {
        return super.timeOut(timeOut);
    }

    @Override
    protected void onSuccess(T t) {
        if (t == null) {
            messages = getFromTopics.getMessages();
        }
    }

    @Override
    protected void onFailure(KafkaConsumer<String, String> m, Throwable throwable) {
        messages = getFromTopics.getMessages();
    }


    public final static class Mapped<T> extends KafkaPollIterableItemSupplier<T, Mapped<T>> {
        private  <S> Mapped(GetFromTopics<S> getFromTopics, Function<S, T> originalFunction) {
            super(getFromTopics, originalFunction);
        }

        public Mapped<T> withDataTransformer(DataTransformer transformer) {
            return super.withDataTransformer(transformer);
        }

    }

    public final static class StringMessage extends KafkaPollIterableItemSupplier<String, StringMessage> {
        private StringMessage(GetFromTopics<String> getFromTopics) {
            super(getFromTopics, s -> s);
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
