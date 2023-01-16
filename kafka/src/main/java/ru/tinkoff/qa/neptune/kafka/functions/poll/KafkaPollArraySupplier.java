package ru.tinkoff.qa.neptune.kafka.functions.poll;

import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
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
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.ArrayUtils.add;
import static org.apache.commons.lang3.StringUtils.isNotBlank;


@SequentialGetStepSupplier.DefineGetImperativeParameterName("Get from Kafka:")
@SequentialGetStepSupplier.DefineTimeOutParameterName("Time of the waiting")
@SequentialGetStepSupplier.DefineCriteriaParameterName("Criteria for every item of resulted array")
@MaxDepthOfReporting(0)
@CaptureOnSuccess(by = ReceivedArrayCaptor.class)
@SuppressWarnings({"unchecked", "rawtypes"})
public final class KafkaPollArraySupplier<K, V, R> extends SequentialGetStepSupplier
    .GetArrayStepSupplier<KafkaStepContext, R, KafkaPollArraySupplier<K, V, R>> {
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
    public static <K, V, R> KafkaPollArraySupplier<K, V, R> consumedArray(
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
    public static <K, V, R> KafkaPollArraySupplier<K, V, R> consumedArray(
        String description,
        Deserializer<K> keyDeserializer,
        Deserializer<V> valueDeserializer,
        TypeReference<R> typeReference,
        Function<ConsumerRecord<K, V>, R> f) {
        return consumedArray(description,
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
    public static <K, R> KafkaPollArraySupplier<K, String, R> consumedArrayKeyData(String description,
                                                                                   Deserializer<K> keyDeserializer,
                                                                                   Class<R> componentClass,
                                                                                   Function<K, R> f) {
        return consumedArray(description,
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
    public static <K, R> KafkaPollArraySupplier<K, String, R> consumedArrayKeyData(String description,
                                                                                   Deserializer<K> keyDeserializer,
                                                                                   TypeReference<R> typeReference,
                                                                                   Function<K, R> f) {
        return consumedArray(description,
            keyDeserializer,
            new StringDeserializer(),
            typeReference,
            msg -> f.apply(msg.key()));
    }

    /**
     * Creates a step that returns array of message keys.
     *
     * @param componentClass  is a class of array item
     * @param keyDeserializer deserializer for key
     * @param <K>             type of deserialized key
     * @return an instance of {@link KafkaPollArraySupplier}
     */
    @Description("Keys as array")
    public static <K> KafkaPollArraySupplier<K, String, K> consumedArrayKeys(Class<K> componentClass,
                                                                             Deserializer<K> keyDeserializer) {
        return new KafkaPollArraySupplier<>(
            keyDeserializer,
            new StringDeserializer(),
            componentClass,
            ConsumerRecord::key);
    }

    /**
     * Creates a step that returns array of message keys.
     *
     * @param typeReference   reference to type of array item
     * @param keyDeserializer deserializer for key
     * @param <K>             type of deserialized key
     * @return an instance of {@link KafkaPollArraySupplier}
     */
    public static <K> KafkaPollArraySupplier<K, String, K> consumedArrayKeys(TypeReference<K> typeReference,
                                                                             Deserializer<K> keyDeserializer) {
        return consumedArrayKeys(
            (Class) (typeReference.getType() instanceof ParameterizedType ? ((ParameterizedType) typeReference.getType()).getRawType() : typeReference.getType()),
            keyDeserializer);
    }

    /**
     * Creates a step that returns array of string keys
     *
     * @return an instance of {@link KafkaPollArraySupplier}
     */
    public static KafkaPollArraySupplier<String, String, String> consumedArrayKeys() {
        return consumedArrayKeys(String.class, new StringDeserializer());
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
    public static <V, R> KafkaPollArraySupplier<String, V, R> consumedArrayValueData(
        String description,
        Deserializer<V> valueDeserializer,
        Class<R> componentClass,
        Function<V, R> f) {
        return consumedArray(description,
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
    public static <V, R> KafkaPollArraySupplier<String, V, R> consumedArrayValueData(
        String description,
        Deserializer<V> valueDeserializer,
        TypeReference<R> typeReference,
        Function<V, R> f) {
        return consumedArray(description,
            new StringDeserializer(),
            valueDeserializer,
            typeReference,
            msg -> f.apply(msg.value()));
    }

    /**
     * Creates a step that returns array of message values.
     *
     * @param componentClass    is a class of array item
     * @param valueDeserializer deserializer for value
     * @param <V>               type of deserialized value
     * @return an instance of {@link KafkaPollArraySupplier}
     */
    @Description("Values as array")
    public static <V> KafkaPollArraySupplier<String, V, V> consumedArrayValues(
        Class<V> componentClass,
        Deserializer<V> valueDeserializer) {
        return new KafkaPollArraySupplier<>(new StringDeserializer(),
            valueDeserializer,
            componentClass,
            ConsumerRecord::value);
    }

    /**
     * Creates a step that returns array of message values.
     *
     * @param typeReference     reference to type of array item
     * @param valueDeserializer deserializer for value
     * @param <V>               type of deserialized value
     * @return an instance of {@link KafkaPollArraySupplier}
     */
    public static <V> KafkaPollArraySupplier<String, V, V> consumedArrayValues(
        TypeReference<V> typeReference,
        Deserializer<V> valueDeserializer) {
        return consumedArrayValues(
            (Class) (typeReference.getType() instanceof ParameterizedType ? ((ParameterizedType) typeReference.getType()).getRawType() : typeReference.getType()),
            valueDeserializer);
    }

    /**
     * Creates a step that returns array of string values
     *
     * @return an instance of {@link KafkaPollArraySupplier}
     */
    public static KafkaPollArraySupplier<String, String, String> consumedArrayValues() {
        return consumedArrayValues(String.class, new StringDeserializer());
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
    public KafkaPollArraySupplier<K, V, R> fromTopics(String... topics) {
        getRecords.topics(topics);
        return this;
    }

    @Override
    public KafkaPollArraySupplier<K, V, R> timeOut(Duration timeOut) {
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

    /**
     * Defines consumer property value
     *
     * @see <a href="https://kafka.apache.org/documentation/#consumerconfigs">Consumer Configs</a>
     *
     * @param property a property name
     * @param value a property value
     * @return self-reference
     */
    public KafkaPollArraySupplier<K, V, R> setProperty(String property, String value) {
        getRecords.setProperty(property, value);
        return this;
    }
}