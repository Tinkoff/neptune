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
import ru.tinkoff.qa.neptune.kafka.captors.ReceivedArrayCaptor;
import ru.tinkoff.qa.neptune.kafka.properties.KafkaDefaultTopicsForPollProperty;

import java.lang.reflect.Array;
import java.lang.reflect.ParameterizedType;
import java.time.Duration;
import java.util.function.Function;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.ArrayUtils.add;
import static org.apache.commons.lang3.StringUtils.isNotBlank;


@SequentialGetStepSupplier.DefineGetImperativeParameterName("Get from Kafka:")
@SequentialGetStepSupplier.DefineTimeOutParameterName("Time of the waiting")
@SequentialGetStepSupplier.DefineCriteriaParameterName("Criteria for every item of resulted array")
@MaxDepthOfReporting(0)
@CaptureOnSuccess(by = ReceivedArrayCaptor.class)
@SuppressWarnings({"unchecked", "rawtypes"})
public class KafkaPollArraySupplier<K, V, R, S extends KafkaPollArraySupplier<K, V, R, S>> extends SequentialGetStepSupplier
    .GetArrayStepSupplier<KafkaStepContext, R, S> {
    public static final String NO_DESC_ERROR_TEXT = "Description should be defined";

    private final GetRecords<K, V> getRecords;

    private KafkaPollArraySupplier(GetRecords<K, V> getFromTopics, Class<R> componentClass, Function<ConsumerRecord<K, V>, R> f) {
        super(getFromTopics.andThen(list -> {
            var listT = list.stream().map(new KafkaSafeFunction<>(f)).collect(toList());
            R[] ts = (R[]) Array.newInstance(componentClass, 0);

            for (var t : listT) {
                ts = add(ts, t);
            }

            return ts;
        }));
        this.getRecords = getFromTopics;
    }

    private KafkaPollArraySupplier(Deserializer<K> keyDeserializer,
                                   Deserializer<V> valueDeserializer,
                                   Class<R> componentClass,
                                   Function<ConsumerRecord<K, V>, R> f) {
        this(new GetRecords<>()
                .setKeyDeserializer(keyDeserializer)
                .setValueDeserializer(valueDeserializer),
            componentClass, f);
    }

    /**
     * Creates a step that returns array of values which are calculated by data of read messages.
     *
     * @param description       is description of value to get
     * @param keyDeserializer   deserializer for key
     * @param valueDeserializer deserializer for value
     * @param componentClass    is a class of array item
     * @param f                 describes how to get array item from a message
     * @param <K>               type of deserialized key
     * @param <V>               type of deserialized value
     * @param <R>               is a type of array item
     * @return an instance of {@link KafkaPollArraySupplier}
     */
    @Description("{description}")
    public static <K, V, R> KafkaPollArraySupplier<K, V, R, ?> kafkaArray(
        @DescriptionFragment(value = "description",
            makeReadableBy = ParameterValueGetter.TranslatedDescriptionParameterValueGetter.class
        ) String description,
        Deserializer<K> keyDeserializer,
        Deserializer<V> valueDeserializer,
        Class<R> componentClass,
        Function<ConsumerRecord<K, V>, R> f) {
        checkArgument(isNotBlank(description), NO_DESC_ERROR_TEXT);
        return new KafkaPollArraySupplier<>(keyDeserializer, valueDeserializer, componentClass, f);
    }

    /**
     * Creates a step that returns array of values which are calculated by data of read messages.
     *
     * @param description       is description of value to get
     * @param keyDeserializer   deserializer for key
     * @param valueDeserializer deserializer for value
     * @param typeReference     reference to type of array item
     * @param f                 describes how to get array item from a message
     * @param <K>               type of deserialized key
     * @param <V>               type of deserialized value
     * @param <R>               is a type of array item
     * @return an instance of {@link KafkaPollArraySupplier}
     */
    public static <K, V, R> KafkaPollArraySupplier<K, V, R, ?> kafkaArray(
        String description,
        Deserializer<K> keyDeserializer,
        Deserializer<V> valueDeserializer,
        TypeReference<R> typeReference,
        Function<ConsumerRecord<K, V>, R> f) {
        return kafkaArray(description,
            keyDeserializer,
            valueDeserializer,
            (Class) (typeReference.getType() instanceof ParameterizedType ? ((ParameterizedType) typeReference.getType()).getRawType() : typeReference.getType()),
            f);
    }

    /**
     * Creates a step that returns array of values which are calculated by key data of read messages.
     *
     * @param description     is description of value to get
     * @param keyDeserializer deserializer for key
     * @param componentClass  is a class of array item
     * @param f               describes how to get array item from message key
     * @param <K>             type of deserialized key
     * @param <R>             is a type of array item
     * @return an instance of {@link KafkaPollArraySupplier}
     */
    public static <K, R> KafkaPollArraySupplier<K, ?, R, ?> kafkaArrayKeyData(String description,
                                                                              Deserializer<K> keyDeserializer,
                                                                              Class<R> componentClass,
                                                                              Function<K, R> f) {
        return kafkaArray(description,
            keyDeserializer,
            new StringDeserializer(),
            componentClass,
            msg -> f.apply(msg.key()));
    }

    /**
     * Creates a step that returns array of values which are calculated by key data of read messages.
     *
     * @param description     is description of value to get
     * @param keyDeserializer deserializer for key
     * @param typeReference   reference to type of array item
     * @param f               describes how to get array item from message key
     * @param <K>             type of deserialized key
     * @param <R>             is a type of array item
     * @return an instance of {@link KafkaPollArraySupplier}
     */
    public static <K, R> KafkaPollArraySupplier<K, ?, R, ?> kafkaArrayKeyData(String description,
                                                                              Deserializer<K> keyDeserializer,
                                                                              TypeReference<R> typeReference,
                                                                              Function<K, R> f) {
        return kafkaArray(description,
            keyDeserializer,
            new StringDeserializer(),
            typeReference,
            msg -> f.apply(msg.key()));
    }

    /**
     * Creates a step that returns array of values which are calculated by key data of read messages.
     *
     * @param description     is description of value to get
     * @param componentClass  is a class of array item
     * @param keyDeserializer deserializer for key
     * @param <K>             type of deserialized key
     * @return an instance of {@link KafkaPollArraySupplier}
     */
    public static <K> KafkaPollArraySupplier<K, ?, K, ?> kafkaArrayKeyData(String description,
                                                                           Class<K> componentClass,
                                                                           Deserializer<K> keyDeserializer) {
        return kafkaArrayKeyData(description,
            keyDeserializer,
            componentClass,
            k -> k);
    }

    /**
     * Creates a step that returns array of values which are calculated by key data of read messages.
     *
     * @param description     is description of value to get
     * @param typeReference   reference to type of array item
     * @param keyDeserializer deserializer for key
     * @param <K>             type of deserialized key
     * @return an instance of {@link KafkaPollArraySupplier}
     */
    public static <K> KafkaPollArraySupplier<K, ?, K, ?> kafkaArrayKeyData(String description,
                                                                           TypeReference<K> typeReference,
                                                                           Deserializer<K> keyDeserializer) {
        return kafkaArrayKeyData(description,
            keyDeserializer,
            typeReference,
            k -> k);
    }

    /**
     * Creates a step that returns array of values which are calculated by value data of read messages.
     *
     * @param description       is description of value to get
     * @param valueDeserializer deserializer for value
     * @param componentClass    is a class of array item
     * @param f                 describes how to get array item from a message value
     * @param <V>               type of deserialized value
     * @param <R>               is a type of array item
     * @return an instance of {@link KafkaPollArraySupplier}
     */
    public static <V, R> KafkaPollArraySupplier<?, V, R, ?> kafkaArrayValueData(
        String description,
        Deserializer<V> valueDeserializer,
        Class<R> componentClass,
        Function<V, R> f) {
        return kafkaArray(description,
            new StringDeserializer(),
            valueDeserializer,
            componentClass,
            msg -> f.apply(msg.value()));
    }

    /**
     * Creates a step that returns array of values which are calculated by value data of read messages.
     *
     * @param description       is description of value to get
     * @param valueDeserializer deserializer for value
     * @param typeReference     reference to type of array item
     * @param f                 describes how to get array item from a message value
     * @param <V>               type of deserialized value
     * @param <R>               is a type of array item
     * @return an instance of {@link KafkaPollArraySupplier}
     */
    public static <V, R> KafkaPollArraySupplier<?, V, R, ?> kafkaArrayValueData(
        String description,
        Deserializer<V> valueDeserializer,
        TypeReference<R> typeReference,
        Function<V, R> f) {
        return kafkaArray(description,
            new StringDeserializer(),
            valueDeserializer,
            typeReference,
            msg -> f.apply(msg.value()));
    }

    /**
     * Creates a step that returns array of values which are calculated by value data of read messages.
     *
     * @param description       is description of value to get
     * @param componentClass    is a class of array item
     * @param valueDeserializer deserializer for value
     * @param <V>               type of deserialized value
     * @return an instance of {@link KafkaPollArraySupplier}
     */
    public static <V> KafkaPollArraySupplier<?, V, V, ?> kafkaArrayValueData(
        String description,
        Class<V> componentClass,
        Deserializer<V> valueDeserializer) {
        return kafkaArrayValueData(description,
            valueDeserializer,
            componentClass,
            v -> v);
    }

    /**
     * Creates a step that returns array of values which are calculated by value data of read messages.
     *
     * @param description       is description of value to get
     * @param typeReference     reference to type of array item
     * @param valueDeserializer deserializer for value
     * @param <V>               type of deserialized value
     * @return an instance of {@link KafkaPollArraySupplier}
     */
    public static <V> KafkaPollArraySupplier<?, V, V, ?> kafkaArrayValueData(
        String description,
        TypeReference<V> typeReference,
        Deserializer<V> valueDeserializer) {
        return kafkaArrayValueData(description,
            valueDeserializer,
            typeReference,
            v -> v);
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
    @Deprecated(forRemoval = true)
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
        var result = new KafkaPollArraySupplier.Mapped<>(componentClass,
            new Conversion<>(toGet, classT, null));
        if (nonNull(topics) && topics.length > 0) {
            result.fromTopics(topics);
        }
        return result;
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
    @Deprecated(forRemoval = true)
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
        var result = new KafkaPollArraySupplier.Mapped<>(componentClass,
            new Conversion<>(toGet, null, typeT));

        if (nonNull(topics) && topics.length > 0) {
            result.fromTopics(topics);
        }
        return result;
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
    @Deprecated(forRemoval = true)
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
    @Deprecated(forRemoval = true)
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
     * @param topics are topics to get messages from
     * @return an instance of {@link KafkaPollArraySupplier}
     */
    @Deprecated(forRemoval = true)
    @Description("String messages")
    public static KafkaPollArraySupplier<String, String, String, ?> kafkaArrayOfRawMessages(String... topics) {
        var result = new KafkaPollArraySupplier(new StringDeserializer(),
            new StringDeserializer(),
            String.class,
            (Function<ConsumerRecord<String, String>, String>) ConsumerRecord::value);

        if (nonNull(topics) && topics.length > 0) {
            result.fromTopics(topics);
        }
        return result;
    }

    /**
     * Defines topics to subscribe.
     * <p></p>
     * If there is no topic defined by this method then value of the property
     * {@link KafkaDefaultTopicsForPollProperty#DEFAULT_TOPICS_FOR_POLL} is used.
     *
     * @param topics topics to subscribe
     * @return self-reference
     */
    public KafkaPollArraySupplier<K, V, R, ?> fromTopics(String... topics) {
        getRecords.topics(topics);
        return this;
    }

    @Override
    public S timeOut(Duration timeOut) {
        return super.timeOut(timeOut);
    }

    @Override
    protected void onSuccess(R[] t) {
        getRecords.closeConsumer();
    }

    @Override
    protected void onFailure(KafkaStepContext m, Throwable throwable) {
        getRecords.closeConsumer();
    }

    @Deprecated(forRemoval = true)
    public final static class Mapped<M, T> extends KafkaPollArraySupplier<String, String, T, Mapped<M, T>> {

        private final Conversion<String, String, M, T> conversion;

        private Mapped(Class<T> componentClass, Conversion<String, String, M, T> conversion) {
            super(new StringDeserializer(), new StringDeserializer(), componentClass, conversion);
            this.conversion = conversion;
        }

        @Deprecated(forRemoval = true)
        public Mapped<M, T> withDataTransformer(DataTransformer transformer) {
            conversion.setTransformer(transformer);
            return this;
        }
    }
}