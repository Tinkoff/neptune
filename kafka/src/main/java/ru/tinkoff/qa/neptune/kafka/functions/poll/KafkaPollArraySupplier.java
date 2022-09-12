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

import java.lang.reflect.Array;
import java.lang.reflect.ParameterizedType;
import java.time.Duration;
import java.util.List;
import java.util.function.Function;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.ArrayUtils.add;
import static org.apache.commons.lang3.StringUtils.isNotBlank;


@SequentialGetStepSupplier.DefineGetImperativeParameterName("Poll:")
@SequentialGetStepSupplier.DefineTimeOutParameterName("Time of the waiting")
@SequentialGetStepSupplier.DefineCriteriaParameterName("Criteria for every item of resulted array")
@MaxDepthOfReporting(0)
@SuppressWarnings({"unchecked", "rawtypes"})
public abstract class KafkaPollArraySupplier<M, R, S extends KafkaPollArraySupplier<M, R, S>> extends SequentialGetStepSupplier
        .GetArrayStepSupplier<KafkaStepContext, R, S> {
    public static final String NO_DESC_ERROR_TEXT = "Description should be defined";

    private GetRecords.MergeProperty getFromTopics;

    @CaptureOnSuccess(by = AllMessagesCaptor.class)
    @CaptureOnFailure(by = AllMessagesCaptor.class)
    List<String> messages;

    protected KafkaPollArraySupplier(GetRecords.MergeProperty<List<M>> getFromTopics, Function<M, R> originalFunction, Class<R> componentClass) {
        super(getFromTopics.andThen(list -> {
            var listT = list.stream().map(originalFunction).collect(toList());
            R[] ts = (R[]) Array.newInstance(componentClass, 0);

            for (var t : listT) {
                ts = add(ts, t);
            }

            return ts;
        }));
        this.getFromTopics = getFromTopics;
    }

    /**
     * Creates a step that returns array of values which are calculated by data of read messages.
     * <p></p>
     * It is not necessary to define {@code topics}. If there is no topic defined then value of the property
     * {@link KafkaDefaultTopicsForPollProperty#DEFAULT_TOPICS_FOR_POLL} is used.
     *
     * @param description    is description of value to get
     * @param classT         is a class of a value to deserialize a message from topics
     * @param componentClass is a class of array item
     * @param toGet          describes how to get desired value
     * @param topics         are topics to get messages from
     * @param <M>            is a type of deserialized message
     * @param <T>            is a type of item of array
     * @return an instance of {@link KafkaPollArraySupplier.Mapped}
     */
    @Description("{description}")
    public static <M, T> Mapped<M, T> kafkaArray(
            @DescriptionFragment(value = "description",
                    makeReadableBy = ParameterValueGetter.TranslatedDescriptionParameterValueGetter.class
            ) String description,
            Class<M> classT,
            Class<T> componentClass,
            Function<M, T> toGet,
            String... topics) {
        checkArgument(isNotBlank(description), NO_DESC_ERROR_TEXT);
        return new KafkaPollArraySupplier.Mapped<>(new GetRecords(topics).andThen(new GetDeserializedData<>(classT)),
                toGet,
                componentClass);
    }

    /**
     * Creates a step that returns array of values which are calculated by data of read messages.
     * <p></p>
     * It is not necessary to define {@code topics}. If there is no topic defined then value of the property
     * {@link KafkaDefaultTopicsForPollProperty#DEFAULT_TOPICS_FOR_POLL} is used.
     *
     * @param description    is description of value to get
     * @param typeT          is a reference to type of value to deserialize message
     * @param componentClass is a class of array item
     * @param toGet          describes how to get desired value
     * @param topics         are topics to get messages from
     * @param <M>            is a type of deserialized message
     * @param <T>            is a type of item of array
     * @return an instance of {@link KafkaPollArraySupplier.Mapped}
     */
    @Description("{description}")
    public static <M, T> Mapped<M, T> kafkaArray(
            @DescriptionFragment(value = "description",
                    makeReadableBy = ParameterValueGetter.TranslatedDescriptionParameterValueGetter.class
            ) String description,
            TypeReference<M> typeT,
            Class<T> componentClass,
            Function<M, T> toGet,
            String... topics) {
        checkArgument(isNotBlank(description), NO_DESC_ERROR_TEXT);
        return new KafkaPollArraySupplier.Mapped<>(new GetRecords(topics).andThen(new GetDeserializedData<>(typeT)), toGet, componentClass);
    }

    /**
     * Creates a step that returns array of deserialized messages.
     * <p></p>
     * It is not necessary to define {@code topics}. If there is no topic defined then value of the property
     * {@link KafkaDefaultTopicsForPollProperty#DEFAULT_TOPICS_FOR_POLL} is used.
     *
     * @param description is description of value to get
     * @param classT      is a class of a value to deserialize a message from topics
     * @param topics      are topics to get messages from
     * @param <T>         is a type of deserialized message
     * @return an instance of {@link KafkaPollArraySupplier.Mapped}
     */
    public static <T> Mapped<T, T> kafkaArray(
            String description,
            Class<T> classT,
            String... topics) {
        return kafkaArray(description, classT, classT, ts -> ts, topics);
    }

    /**
     * Creates a step that returns array of deserialized messages.
     * <p></p>
     * It is not necessary to define {@code topics}. If there is no topic defined then value of the property
     * {@link KafkaDefaultTopicsForPollProperty#DEFAULT_TOPICS_FOR_POLL} is used.
     *
     * @param description is description of value to get
     * @param typeT       is a reference to type of value to deserialize message
     * @param topics      are topics to get messages from
     * @param <T>         is a type of deserialized message
     * @return an instance of {@link KafkaPollArraySupplier.Mapped}
     */
    public static <T> Mapped<T, T> kafkaArray(
            String description,
            TypeReference<T> typeT,
            String... topics) {
        var clazz = (Class) (typeT.getType() instanceof ParameterizedType ? ((ParameterizedType) typeT.getType()).getRawType() : typeT.getType());
        return kafkaArray(description, typeT, clazz, ts -> ts, topics);
    }

    /**
     * Creates a step that returns array of string contents of messages.
     * <p></p>
     * It is not necessary to define {@code topics}. If there is no topic defined then value of the property
     * {@link KafkaDefaultTopicsForPollProperty#DEFAULT_TOPICS_FOR_POLL} is used.
     *
     * @param topics      are topics to get messages from
     * @return an instance of {@link KafkaPollArraySupplier.StringMessages}
     */
    @Description("String messages")
    public static StringMessages kafkaArrayOfRawMessages(String... topics) {
        return new StringMessages(new GetRecords(topics).andThen(new GetDeserializedData<>(String.class)));
    }

    @Override
    public S timeOut(Duration timeOut) {
        return super.timeOut(timeOut);
    }

    S withDataTransformer(DataTransformer dataTransformer) {
        ((GetDeserializedData<M>) getFromTopics.getAfter()).setTransformer(dataTransformer);
        return (S) this;
    }

    @Override
    protected void onSuccess(R[] t) {
        if (t == null || t.length == 0) {
            messages = getFromTopics.getBefore().getMessages();
        }
    }

    @Override
    protected void onFailure(KafkaStepContext m, Throwable throwable) {
        messages = getFromTopics.getBefore().getMessages();
    }

    public final static class Mapped<M, T> extends KafkaPollArraySupplier<M, T, Mapped<M, T>> {

        private Mapped(GetRecords.MergeProperty<List<M>> getFromTopics, Function<M, T> originalFunction, Class<T> componentClass) {
            super(getFromTopics, originalFunction, componentClass);
        }

        public Mapped<M, T> withDataTransformer(DataTransformer transformer) {
            return super.withDataTransformer(transformer);
        }
    }

    public final static class StringMessages extends KafkaPollArraySupplier<String, String, StringMessages> {

        private StringMessages(GetRecords.MergeProperty<List<String>> getFromTopics) {
            super(getFromTopics, s -> s, String.class);
            withDataTransformer(new StringDataTransformer());
        }
    }
}