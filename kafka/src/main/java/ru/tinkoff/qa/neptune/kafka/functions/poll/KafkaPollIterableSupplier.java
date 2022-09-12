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
public abstract class KafkaPollIterableSupplier<M, R, S extends KafkaPollIterableSupplier<M, R, S>>
        extends SequentialGetStepSupplier.GetListStepSupplier<KafkaStepContext, List<R>, R, S> {

    public static final String NO_DESC_ERROR_TEXT = "Description should be defined";
    private GetRecords.MergeProperty getFromTopics;

    @CaptureOnSuccess(by = AllMessagesCaptor.class)
    @CaptureOnFailure(by = AllMessagesCaptor.class)
    List<String> messages;

    protected KafkaPollIterableSupplier(GetRecords.MergeProperty<List<M>> getFromTopics, Function<M, R> function) {
        super(getFromTopics.andThen(list -> list.stream().map(function).collect(toList())));
        this.getFromTopics = getFromTopics;
    }

    /**
     * Creates a step that returns iterable of values which are calculated by data of read messages.
     * <p></p>
     * It is not necessary to define {@code topics}. If there is no topic defined then value of the property
     * {@link KafkaDefaultTopicsForPollProperty#DEFAULT_TOPICS_FOR_POLL} is used.
     *
     * @param description is description of value to get
     * @param cls         is a class of a value to deserialize a message from topics
     * @param toGet       describes how to get desired value
     * @param topics      are topics to get messages from
     * @param <M>         is a type of deserialized message
     * @param <T>         is a type of item of iterable
     * @return an instance of {@link KafkaPollIterableSupplier.Mapped}
     */
    @Description("{description}")
    public static <M, T> Mapped<M, T> kafkaIterable(
            @DescriptionFragment(value = "description",
                    makeReadableBy = ParameterValueGetter.TranslatedDescriptionParameterValueGetter.class
            ) String description,
            Class<M> cls,
            Function<M, T> toGet,
            String... topics) {
        checkArgument(isNotBlank(description), NO_DESC_ERROR_TEXT);
        return new KafkaPollIterableSupplier.Mapped<>(new GetRecords(topics).andThen(new GetDeserializedData<>(cls)), toGet);
    }

    /**
     * Creates a step that returns iterable of values which are calculated by data of read messages.
     * <p></p>
     * It is not necessary to define {@code topics}. If there is no topic defined then value of the property
     * {@link KafkaDefaultTopicsForPollProperty#DEFAULT_TOPICS_FOR_POLL} is used.
     *
     * @param description is description of value to get
     * @param typeT       is a reference to type of value to deserialize message
     * @param toGet       describes how to get desired value
     * @param topics      are topics to get messages from
     * @param <M>         is a type of deserialized message
     * @param <T>         is a type of item of iterable
     * @return an instance of {@link KafkaPollIterableSupplier.Mapped}
     */
    @Description("{description}")
    public static <M, T> Mapped<M, T> kafkaIterable(
            @DescriptionFragment(value = "description",
                    makeReadableBy = ParameterValueGetter.TranslatedDescriptionParameterValueGetter.class
            ) String description,
            TypeReference<M> typeT,
            Function<M, T> toGet,
            String... topics) {
        checkArgument(isNotBlank(description), NO_DESC_ERROR_TEXT);
        return new KafkaPollIterableSupplier.Mapped<>(new GetRecords(topics).andThen(new GetDeserializedData<>(typeT)), toGet);
    }

    /**
     * Creates a step that returns iterable of deserialized messages.
     * <p></p>
     * It is not necessary to define {@code topics}. If there is no topic defined then value of the property
     * {@link KafkaDefaultTopicsForPollProperty#DEFAULT_TOPICS_FOR_POLL} is used.
     *
     * @param description is description of value to get
     * @param typeT       is a reference to type of value to deserialize message
     * @param topics      are topics to get messages from
     * @param <T>         is a type of deserialized message
     * @return an instance of {@link KafkaPollIterableSupplier.Mapped}
     */
    public static <T> Mapped<T, T> kafkaIterable(
            String description,
            TypeReference<T> typeT,
            String... topics) {
        return kafkaIterable(description, typeT, ts -> ts, topics);
    }

    /**
     * Creates a step that returns iterable of deserialized messages.
     * <p></p>
     * It is not necessary to define {@code topics}. If there is no topic defined then value of the property
     * {@link KafkaDefaultTopicsForPollProperty#DEFAULT_TOPICS_FOR_POLL} is used.
     *
     * @param description is description of value to get
     * @param cls         is a class of a value to deserialize a message from topics
     * @param topics      are topics to get messages from
     * @param <T>         is a type of deserialized message
     * @return an instance of {@link KafkaPollIterableSupplier.Mapped}
     */
    public static <T> Mapped<T, T> kafkaIterable(
            String description,
            Class<T> cls,
            String... topics) {
        return kafkaIterable(description, cls, ts -> ts, topics);
    }

    /**
     * Creates a step that returns iterable of string contents of messages.
     * <p></p>
     * It is not necessary to define {@code topics}. If there is no topic defined then value of the property
     * {@link KafkaDefaultTopicsForPollProperty#DEFAULT_TOPICS_FOR_POLL} is used.
     *
     * @param topics      are topics to get messages from
     * @return an instance of {@link KafkaPollIterableSupplier.StringMessages}
     */
    @Description("String messages")
    public static StringMessages kafkaRawMessages(String... topics) {
        return new StringMessages(new GetRecords(topics).andThen(new GetDeserializedData<>(String.class)));
    }

    S withDataTransformer(DataTransformer dataTransformer) {
        ((GetDeserializedData<M>) getFromTopics.getAfter()).setTransformer(dataTransformer);
        return (S) this;
    }

    @Override
    public S timeOut(Duration timeOut) {
        return super.timeOut(timeOut);
    }

    @Override
    protected void onSuccess(List<R> tList) {
        if (tList == null || tList.isEmpty()) {
            messages = getFromTopics.getBefore().getMessages();
        }
    }

    @Override
    protected void onFailure(KafkaStepContext m, Throwable throwable) {
        messages = getFromTopics.getBefore().getMessages();
    }

    public final static class Mapped<M, T> extends KafkaPollIterableSupplier<M, T, Mapped<M, T>> {
        private Mapped(GetRecords.MergeProperty<List<M>> getFromTopics, Function<M, T> function) {
            super(getFromTopics, function);
        }

        @Override
        public Mapped<M, T> withDataTransformer(DataTransformer transformer) {
            return super.withDataTransformer(transformer);
        }
    }

    public final static class StringMessages extends KafkaPollIterableSupplier<String, String, StringMessages> {
        private StringMessages(GetRecords.MergeProperty<List<String>> getFromTopics) {
            super(getFromTopics, s -> s);
            withDataTransformer(new StringDataTransformer());
        }
    }
}
