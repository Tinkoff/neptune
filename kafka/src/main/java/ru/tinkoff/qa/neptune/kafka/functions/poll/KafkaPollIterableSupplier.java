package ru.tinkoff.qa.neptune.kafka.functions.poll;

import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import ru.tinkoff.qa.neptune.core.api.data.format.DataTransformer;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotations.CaptureOnSuccess;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.MaxDepthOfReporting;
import ru.tinkoff.qa.neptune.core.api.steps.parameters.ParameterValueGetter;
import ru.tinkoff.qa.neptune.kafka.KafkaStepContext;
import ru.tinkoff.qa.neptune.kafka.captors.ReceivedListCaptor;
import ru.tinkoff.qa.neptune.kafka.properties.KafkaDefaultTopicsForPollProperty;

import java.time.Duration;
import java.util.List;
import java.util.function.Function;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@SequentialGetStepSupplier.DefineGetImperativeParameterName("Get from Kafka:")
@SequentialGetStepSupplier.DefineTimeOutParameterName("Time of the waiting")
@SequentialGetStepSupplier.DefineCriteriaParameterName("Object criteria")
@MaxDepthOfReporting(0)
@CaptureOnSuccess(by = ReceivedListCaptor.class)
public class KafkaPollIterableSupplier<K, V, R, I extends KafkaPollIterableSupplier<K, V, R, I>>
    extends SequentialGetStepSupplier.GetListStepSupplier<KafkaStepContext, List<R>, R, I> {

    public static final String NO_DESC_ERROR_TEXT = "Description should be defined";

    private final GetRecords<K, V> getRecords;

    protected KafkaPollIterableSupplier(GetRecords<K, V> getFromTopics, Function<ConsumerRecord<K, V>, R> f) {
        super(getFromTopics.andThen(list -> list.stream().map(new KafkaSafeFunction<>(f)).collect(toList())));
        this.getRecords = getFromTopics;
    }

    private KafkaPollIterableSupplier(Deserializer<K> keyDeserializer,
                                      Deserializer<V> valueDeserializer,
                                      Function<ConsumerRecord<K, V>, R> f) {
        this(new GetRecords<>()
                .setKeyDeserializer(keyDeserializer)
                .setValueDeserializer(valueDeserializer),
            f);
    }

    /**
     * Creates a step that returns list of values which are calculated by data of read messages.
     *
     * @param description       is description of value to get
     * @param keyDeserializer   deserializer for key
     * @param valueDeserializer deserializer for value
     * @param f                 describes how to get list item from a message
     * @param <K>               type of deserialized key
     * @param <V>               type of deserialized value
     * @param <R>               is a type of list item
     * @return an instance of {@link KafkaPollIterableSupplier}
     */
    @Description("{description}")
    public static <K, V, R> KafkaPollIterableSupplier<K, V, R, ?> consumedList(
        @DescriptionFragment(value = "description",
            makeReadableBy = ParameterValueGetter.TranslatedDescriptionParameterValueGetter.class
        ) String description,
        Deserializer<K> keyDeserializer,
        Deserializer<V> valueDeserializer,
        Function<ConsumerRecord<K, V>, R> f) {
        checkArgument(isNotBlank(description), NO_DESC_ERROR_TEXT);
        return new KafkaPollIterableSupplier<>(keyDeserializer, valueDeserializer, f);
    }

    /**
     * Creates a step that returns list of values which are calculated by key data of read messages.
     *
     * @param description     is description of value to get
     * @param keyDeserializer deserializer for key
     * @param f               describes how to get list item from message key
     * @param <K>             type of deserialized key
     * @param <R>             is a type of list item
     * @return an instance of {@link KafkaPollIterableSupplier}
     */
    public static <K, R> KafkaPollIterableSupplier<K, String, R, ?> consumedListKeyData(
        String description,
        Deserializer<K> keyDeserializer,
        Function<K, R> f) {
        return consumedList(description, keyDeserializer, new StringDeserializer(), msg -> f.apply(msg.key()));
    }

    /**
     * Creates a step that returns list of message keys.
     *
     * @param keyDeserializer deserializer for key
     * @param <K>             type of deserialized key
     * @return an instance of {@link KafkaPollIterableSupplier}
     */
    @Description("Message keys")
    public static <K> KafkaPollIterableSupplier<K, String, K, ?> consumedKeys(
        Deserializer<K> keyDeserializer) {
        return new KafkaPollIterableSupplier<>(keyDeserializer, new StringDeserializer(), ConsumerRecord::key);
    }

    /**
     * Creates a step that returns list of string keys.
     *
     * @return an instance of {@link KafkaPollIterableSupplier}
     */
    public static KafkaPollIterableSupplier<String, String, String, ?> consumedKeys() {
        return consumedKeys(new StringDeserializer());
    }

    /**
     * Creates a step that returns list of values which are calculated by value data of read messages.
     *
     * @param description       is description of value to get
     * @param valueDeserializer deserializer for value
     * @param f                 describes how to get list item from a message value
     * @param <V>               type of deserialized value
     * @param <R>               is a type of list item
     * @return an instance of {@link KafkaPollIterableSupplier}
     */
    public static <V, R> KafkaPollIterableSupplier<String, V, R, ?> consumedListValueData(
        String description,
        Deserializer<V> valueDeserializer,
        Function<V, R> f) {
        return consumedList(description, new StringDeserializer(), valueDeserializer, msg -> f.apply(msg.value()));
    }

    /**
     * Creates a step that returns list of message values.
     *
     * @param valueDeserializer deserializer for value
     * @param <V>               type of deserialized value
     * @return an instance of {@link KafkaPollIterableSupplier}
     */
    @Description("Message values")
    public static <V> KafkaPollIterableSupplier<String, V, V, ?> consumedValues(
        Deserializer<V> valueDeserializer) {
        return new KafkaPollIterableSupplier<>(new StringDeserializer(), valueDeserializer, ConsumerRecord::value);
    }

    /**
     * Creates a step that returns list of message string values.
     *
     * @return an instance of {@link KafkaPollIterableSupplier}
     */
    public static KafkaPollIterableSupplier<String, String, String, ?> consumedValues() {
        return consumedValues(new StringDeserializer());
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
    @Deprecated(forRemoval = true)
    @Description("{description}")
    public static <M, T> Mapped<M, T> kafkaIterable(
            @DescriptionFragment(value = "description",
                    makeReadableBy = ParameterValueGetter.TranslatedDescriptionParameterValueGetter.class
            ) String description,
            Class<M> cls,
            Function<M, T> toGet,
            String... topics) {
        checkArgument(isNotBlank(description), NO_DESC_ERROR_TEXT);

        var result = new KafkaPollIterableSupplier.Mapped<>(new Conversion<>(toGet, cls, null));

        if (nonNull(topics) && topics.length > 0) {
            result.fromTopics(topics);
        }
        return result;
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
    @Deprecated(forRemoval = true)
    @Description("{description}")
    public static <M, T> Mapped<M, T> kafkaIterable(
            @DescriptionFragment(value = "description",
                    makeReadableBy = ParameterValueGetter.TranslatedDescriptionParameterValueGetter.class
            ) String description,
            TypeReference<M> typeT,
            Function<M, T> toGet,
            String... topics) {
        checkArgument(isNotBlank(description), NO_DESC_ERROR_TEXT);
        var result = new KafkaPollIterableSupplier.Mapped<>(new Conversion<>(toGet, null, typeT));

        if (nonNull(topics) && topics.length > 0) {
            result.fromTopics(topics);
        }
        return result;
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
    @Deprecated(forRemoval = true)
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
    @Deprecated(forRemoval = true)
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
     * @param topics are topics to get messages from
     * @return an instance of {@link KafkaPollIterableSupplier}
     */
    @Deprecated(forRemoval = true)
    public static KafkaPollIterableSupplier<String, String, String, ?> kafkaRawMessages(String... topics) {
        var result = consumedValues();

        if (nonNull(topics) && topics.length > 0) {
            result.fromTopics(topics);
        }
        return result;
    }

    /**
     * Defines topics to subscribe
     * <p></p>
     * If there is no topic defined by this method then value of the property
     * {@link KafkaDefaultTopicsForPollProperty#DEFAULT_TOPICS_FOR_POLL} is used.
     *
     * @param topics topics to subscribe
     * @return self-reference
     */
    public KafkaPollIterableSupplier<K, V, R, ?> fromTopics(String... topics) {
        getRecords.topics(topics);
        return this;
    }

    @Override
    public I timeOut(Duration timeOut) {
        return super.timeOut(timeOut);
    }

    @Override
    protected void onSuccess(List<R> tList) {
        getRecords.closeConsumer();
    }

    @Override
    protected void onFailure(KafkaStepContext m, Throwable throwable) {
        getRecords.closeConsumer();
    }

    public final static class Mapped<M, T> extends KafkaPollIterableSupplier<String, String, T, Mapped<M, T>> {

        private final Conversion<String, String, M, T> conversion;

        private Mapped(Conversion<String, String, M, T> conversion) {
            super(new StringDeserializer(), new StringDeserializer(), conversion);
            this.conversion = conversion;
        }

        @Deprecated(forRemoval = true)
        public Mapped<M, T> withDataTransformer(DataTransformer transformer) {
            conversion.setTransformer(transformer);
            return this;
        }
    }
}
