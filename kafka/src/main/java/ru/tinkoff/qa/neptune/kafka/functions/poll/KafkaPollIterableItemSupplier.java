package ru.tinkoff.qa.neptune.kafka.functions.poll;

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
import ru.tinkoff.qa.neptune.kafka.captors.KafkaObjectResultCaptor;
import ru.tinkoff.qa.neptune.kafka.properties.KafkaDefaultTopicsForPollProperty;

import java.time.Duration;
import java.util.function.Function;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@SequentialGetStepSupplier.DefineGetImperativeParameterName("Get from Kafka:")
@SequentialGetStepSupplier.DefineTimeOutParameterName("Time of the waiting")
@SequentialGetStepSupplier.DefineCriteriaParameterName("Object criteria")
@MaxDepthOfReporting(0)
@CaptureOnSuccess(by = KafkaObjectResultCaptor.class)
public final class KafkaPollIterableItemSupplier<K, V, R>
    extends SequentialGetStepSupplier.GetObjectFromIterableStepSupplier<KafkaStepContext, R, KafkaPollIterableItemSupplier<K, V, R>> {

    public static final String NO_DESC_ERROR_TEXT = "Description should be defined";
    private final GetRecords<K, V> getRecords;

    private KafkaPollIterableItemSupplier(GetRecords<K, V> getFromTopics, Function<ConsumerRecord<K, V>, R> f) {
        super(getFromTopics.andThen(list -> list.stream().map(new KafkaSafeFunction<>(f)).collect(toList())));
        this.getRecords = getFromTopics;
    }

    private KafkaPollIterableItemSupplier(Deserializer<K> keyDeserializer,
                                          Deserializer<V> valueDeserializer,
                                          Function<ConsumerRecord<K, V>, R> f) {
        this(new GetRecords<>()
                .setKeyDeserializer(keyDeserializer)
                .setValueDeserializer(valueDeserializer),
            f);
    }

    /**
     * Creates a step that returns an item from list of values which are calculated by data of read messages.
     *
     * @param description       is description of value to get
     * @param keyDeserializer   deserializer for key
     * @param valueDeserializer deserializer for value
     * @param f                 describes how to get list item from a message
     * @param <K>               type of deserialized key
     * @param <V>               type of deserialized value
     * @param <R>               is a type of list item
     * @return an instance of {@link KafkaPollIterableItemSupplier}
     */
    @Description("{description}")
    public static <K, V, R> KafkaPollIterableItemSupplier<K, V, R> consumedItem(
        @DescriptionFragment(value = "description",
            makeReadableBy = ParameterValueGetter.TranslatedDescriptionParameterValueGetter.class
        ) String description,
        Deserializer<K> keyDeserializer,
        Deserializer<V> valueDeserializer,
        Function<ConsumerRecord<K, V>, R> f) {
        checkArgument(isNotBlank(description), NO_DESC_ERROR_TEXT);
        return new KafkaPollIterableItemSupplier<>(keyDeserializer, valueDeserializer, f);
    }

    /**
     * Creates a step that returns an item from list of values which are calculated by key data of read messages.
     *
     * @param description     is description of value to get
     * @param keyDeserializer deserializer for key
     * @param f               describes how to get list item from message key
     * @param <K>             type of deserialized key
     * @param <R>             is a type of list item
     * @return an instance of {@link KafkaPollIterableItemSupplier}
     */
    public static <K, R> KafkaPollIterableItemSupplier<K, String, R> consumedItemKeyData(
        String description,
        Deserializer<K> keyDeserializer,
        Function<K, R> f) {
        return consumedItem(description, keyDeserializer, new StringDeserializer(), msg -> f.apply(msg.key()));
    }

    /**
     * Creates a step that returns a message key.
     *
     * @param keyDeserializer deserializer for key
     * @param <K>             type of deserialized key
     * @return an instance of {@link KafkaPollIterableItemSupplier}
     */
    @Description("Message key")
    public static <K> KafkaPollIterableItemSupplier<K, String, K> consumedKey(
        Deserializer<K> keyDeserializer) {
        return new KafkaPollIterableItemSupplier<>(keyDeserializer, new StringDeserializer(), ConsumerRecord::key);
    }

    /**
     * Creates a step that returns a message key as text.
     *
     * @return an instance of {@link KafkaPollIterableItemSupplier}
     */
    public static KafkaPollIterableItemSupplier<String, String, String> consumedKey() {
        return consumedKey(new StringDeserializer());
    }

    /**
     * Creates a step that returns an item from list of values which are calculated by value data of read messages.
     *
     * @param description       is description of value to get
     * @param valueDeserializer deserializer for value
     * @param f                 describes how to get list item from a message value
     * @param <V>               type of deserialized value
     * @param <R>               is a type of list item
     * @return an instance of {@link KafkaPollIterableItemSupplier}
     */
    public static <V, R> KafkaPollIterableItemSupplier<String, V, R> consumedItemValueData(
        String description,
        Deserializer<V> valueDeserializer,
        Function<V, R> f) {
        return consumedItem(description, new StringDeserializer(), valueDeserializer, msg -> f.apply(msg.value()));
    }

    /**
     * Creates a step that returns a message value.
     *
     * @param valueDeserializer deserializer for value
     * @param <V>               type of deserialized value
     * @return an instance of {@link KafkaPollIterableItemSupplier}
     */
    @Description("Message value")
    public static <V> KafkaPollIterableItemSupplier<String, V, V> consumedValue(
        Deserializer<V> valueDeserializer) {
        return new KafkaPollIterableItemSupplier<>(new StringDeserializer(), valueDeserializer, ConsumerRecord::value);
    }

    /**
     * Creates a step that returns a message value as text.
     *
     * @return an instance of {@link KafkaPollIterableItemSupplier}
     */
    public static KafkaPollIterableItemSupplier<String, String, String> consumedValue() {
        return consumedValue(new StringDeserializer());
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
    public KafkaPollIterableItemSupplier<K, V, R> fromTopics(String... topics) {
        getRecords.topics(topics);
        return this;
    }

    @Override
    public KafkaPollIterableItemSupplier<K, V, R> timeOut(Duration timeOut) {
        return super.timeOut(timeOut);
    }

    @Override
    protected void onSuccess(R t) {
        getRecords.closeConsumer();
    }

    @Override
    protected void onFailure(KafkaStepContext m, Throwable throwable) {
        getRecords.closeConsumer();
    }
}
