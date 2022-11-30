package ru.tinkoff.qa.neptune.kafka.functions.poll;

import com.fasterxml.jackson.core.type.TypeReference;
import ru.tinkoff.qa.neptune.core.api.data.format.DataTransformer;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotations.CaptureOnFailure;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotations.CaptureOnSuccess;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.MaxDepthOfReporting;
import ru.tinkoff.qa.neptune.core.api.steps.parameters.ParameterValueGetter;
import ru.tinkoff.qa.neptune.kafka.KafkaStepContext;
import ru.tinkoff.qa.neptune.kafka.captors.AllMessagesCaptor;
import ru.tinkoff.qa.neptune.kafka.properties.KafkaDefaultTopicsForPollProperty;

import java.time.Duration;
import java.util.List;
import java.util.function.Function;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@SequentialGetStepSupplier.DefineGetImperativeParameterName("Poll:")
@SequentialGetStepSupplier.DefineTimeOutParameterName("Time of the waiting")
@SequentialGetStepSupplier.DefineCriteriaParameterName("Object criteria")
@MaxDepthOfReporting(0)
@SuppressWarnings("unchecked")
public abstract class KafkaPollIterableItemSupplier<M, T, I extends KafkaPollIterableItemSupplier<M, T, I>>
        extends SequentialGetStepSupplier.GetObjectFromIterableStepSupplier<KafkaStepContext, T, I> {

    public static final String NO_DESC_ERROR_TEXT = "Description should be defined";
    private GetRecords.MergeProperty getFromTopics;

    @CaptureOnSuccess(by = AllMessagesCaptor.class)
    @CaptureOnFailure(by = AllMessagesCaptor.class)
    List<String> messages;

    protected KafkaPollIterableItemSupplier(GetRecords.MergeProperty<List<M>> getFromTopics, Function<M, T> originalFunction) {
        super(getFromTopics.andThen(list -> list.stream().map(originalFunction).collect(toList())));
        this.getFromTopics = getFromTopics;
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
    public static <M, T> Mapped<M, T> kafkaIterableItem(
            @DescriptionFragment(value = "description",
                    makeReadableBy = ParameterValueGetter.TranslatedDescriptionParameterValueGetter.class
            ) String description,
            Class<M> cls,
            Function<M, T> toGet,
            String... topics) {
        checkArgument(isNotBlank(description), NO_DESC_ERROR_TEXT);
        return new KafkaPollIterableItemSupplier.Mapped<>(new GetRecords(topics).andThen(new GetDeserializedData<>(cls)), toGet);
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
    public static <M, T> Mapped<M, T> kafkaIterableItem(
            @DescriptionFragment(value = "description",
                    makeReadableBy = ParameterValueGetter.TranslatedDescriptionParameterValueGetter.class
            ) String description,
            TypeReference<M> typeT,
            Function<M, T> toGet,
            String... topics) {
        checkArgument(isNotBlank(description), NO_DESC_ERROR_TEXT);
        return new KafkaPollIterableItemSupplier.Mapped<>(new GetRecords(topics).andThen(new GetDeserializedData<>(typeT)), toGet);
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
    public static <M> Mapped<M, M> kafkaIterableItem(
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
    public static <M> Mapped<M, M> kafkaIterableItem(
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
        return new StringMessage(new GetRecords(topics).andThen(new GetDeserializedData<>(String.class)));
    }

    I withDataTransformer(DataTransformer dataTransformer) {
        ((GetDeserializedData<M>) getFromTopics.getAfter()).setTransformer(dataTransformer);
        return (I) this;
    }

    @Override
    public I timeOut(Duration timeOut) {
        return super.timeOut(timeOut);
    }

    @Override
    protected void onSuccess(T t) {
        if (t == null) {
            messages = getFromTopics.getBefore().getMessages();
        }
        getFromTopics.getBefore().getKafkaConsumer().close();
    }

    @Override
    protected void onFailure(KafkaStepContext m, Throwable throwable) {
        messages = getFromTopics.getBefore().getMessages();
        getFromTopics.getBefore().getKafkaConsumer().close();
    }


    public final static class Mapped<M, T> extends KafkaPollIterableItemSupplier<M, T, Mapped<M, T>> {
        private Mapped(GetRecords.MergeProperty<List<M>> getFromTopics, Function<M, T> originalFunction) {
            super(getFromTopics, originalFunction);
        }

        public Mapped<M, T> withDataTransformer(DataTransformer transformer) {
            return super.withDataTransformer(transformer);
        }

    }

    public final static class StringMessage extends KafkaPollIterableItemSupplier<String, String, StringMessage> {
        private StringMessage(GetRecords.MergeProperty<List<String>> getFromTopics) {
            super(getFromTopics, s -> s);
            withDataTransformer(new StringDataTransformer());
        }
    }
}
