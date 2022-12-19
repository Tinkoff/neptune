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
import ru.tinkoff.qa.neptune.kafka.captors.KafkaConsumerRecordsCaptor;
import ru.tinkoff.qa.neptune.kafka.properties.KafkaDefaultTopicsForPollProperty;

import java.time.Duration;
import java.util.List;
import java.util.function.Function;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static ru.tinkoff.qa.neptune.kafka.functions.poll.KafkaPollItemFromRecordSupplier.itemFromRecords;
import static ru.tinkoff.qa.neptune.kafka.functions.poll.KafkaPollListFromRecordSupplier.listFromRecords;

@SequentialGetStepSupplier.DefineGetImperativeParameterName("Get from Kafka:")
@SequentialGetStepSupplier.DefineTimeOutParameterName("Time of the waiting")
@SequentialGetStepSupplier.DefineCriteriaParameterName("ConsumerRecord criteria")
@MaxDepthOfReporting(0)
@CaptureOnSuccess(by = KafkaConsumerRecordsCaptor.class)
public class GetRecordSupplier<K, V> extends SequentialGetStepSupplier.GetListStepSupplier<KafkaStepContext, List<ConsumerRecord<K, V>>, ConsumerRecord<K, V>, GetRecordSupplier<K, V>> {

    private final GetRecords<K, V> function;

    protected GetRecordSupplier(GetRecords<K, V> originalFunction) {
        super(originalFunction);
        this.function = originalFunction;
    }

    @Deprecated(forRemoval = true)
    @Description("{description}")
    public static GetRecordSupplier<String, String> consumerRecords(
        @DescriptionFragment(value = "description",
            makeReadableBy = ParameterValueGetter.TranslatedDescriptionParameterValueGetter.class
        ) String description,
        String... topics) {
        checkArgument(isNotBlank(description), "Description should be defined");
        var result = new GetRecordSupplier<>(new GetRecords<>()
            .setKeyDeserializer(new StringDeserializer())
            .setValueDeserializer(new StringDeserializer()));

        if (nonNull(topics) && topics.length > 0) {
            result.fromTopics(topics);
        }
        return result;
    }

    @Deprecated(forRemoval = true)
    @Description("Kafka messages")
    public static GetRecordSupplier<String, String> records(String... topics) {
        var result = consumerRecords();

        if (nonNull(topics) && topics.length > 0) {
            result.fromTopics(topics);
        }
        return result;
    }

    @Description("Kafka consumer records")
    public static <K, V> GetRecordSupplier<K, V> consumerRecords(Deserializer<K> keyDeserializer,
                                                                 Deserializer<V> valueDeserializer) {
        return new GetRecordSupplier<>(new GetRecords<>()
            .setKeyDeserializer(keyDeserializer)
            .setValueDeserializer(valueDeserializer));
    }

    public static GetRecordSupplier<String, String> consumerRecords() {
        return consumerRecords(new StringDeserializer(),
            new StringDeserializer());
    }

    /**
     * Defines topics to subscribe
     * <p></p>
     * If there is no topic defined then value of the property
     * {@link KafkaDefaultTopicsForPollProperty#DEFAULT_TOPICS_FOR_POLL} is used.
     *
     * @param topics topics to subscribe
     * @return self-reference
     */
    public GetRecordSupplier<K, V> fromTopics(String... topics) {
        function.topics(topics);
        return this;
    }

    /**
     * @param description     is description of value to get
     * @param getItemFunction
     * @param <R>             is a type of item of iterable
     * @return KafkaPollListFromRecordSupplier
     */
    public <R> KafkaPollListFromRecordSupplier<K, V, R, ?> thenGetList(String description, Function<ConsumerRecord<K, V>, R> getItemFunction) {
        return listFromRecords(description, getItemFunction).from(this);
    }

    /**
     * @param description is description of value to get
     * @param cls         is a class of a value to deserialize a message from topics
     * @param conversion  describes how to get desired value
     * @param <R>         is a type of item of iterable
     * @param <M>         is a type of deserialized message
     * @return KafkaPollListFromRecordSupplier.KafkaPollDeserializedFromSupplier
     */
    @Deprecated(forRemoval = true)
    public <M, R> KafkaPollListFromRecordSupplier.KafkaPollDeserializedFromSupplier<K, V, M, R> thenGetList(String description, Class<M> cls, Function<M, R> conversion) {
        return listFromRecords(description, cls, conversion, this);
    }

    /**
     * @param description is description of value to get
     * @param typeT       is a reference to type of value to deserialize message
     * @param conversion  describes how to get desired value
     * @param <R>         is a type of item of iterable
     * @param <M>         is a type of deserialized message
     * @return KafkaPollListFromRecordSupplier.KafkaPollDeserializedFromSupplier
     */
    @Deprecated(forRemoval = true)
    public <R, M> KafkaPollListFromRecordSupplier.KafkaPollDeserializedFromSupplier<K, V, M, R> thenGetList(String description, TypeReference<M> typeT, Function<M, R> conversion) {
        return listFromRecords(description, typeT, conversion, this);
    }

    /**
     * @param description is description of value to get
     * @param cls         is a class of a value to deserialize a message from topics
     * @param <R>         is a type of deserialized message
     * @return KafkaPollListFromRecordSupplier.KafkaPollDeserializedFromSupplier
     */
    @Deprecated(forRemoval = true)
    public <R> KafkaPollListFromRecordSupplier.KafkaPollDeserializedFromSupplier<K, V, R, R> thenGetList(String description, Class<R> cls) {
        return thenGetList(description, cls, f -> f);
    }

    /**
     * @param description is description of value to get
     * @param typeT       is a reference to type of value to deserialize message
     * @param <R>         is a type of deserialized message
     * @return KafkaPollListFromRecordSupplier.KafkaPollDeserializedFromSupplier
     */
    @Deprecated(forRemoval = true)
    public <R> KafkaPollListFromRecordSupplier.KafkaPollDeserializedFromSupplier<K, V, R, R> thenGetList(String description, TypeReference<R> typeT) {
        return thenGetList(description, typeT, f -> f);
    }

    /**
     * @param description is description of value to get
     * @param function    describes how to get desired value
     * @param <R>         is a type of item
     * @return KafkaPollItemFromRecordSupplier
     */
    public <R> KafkaPollItemFromRecordSupplier<K, V, R, ?> thenGetItem(String description, Function<ConsumerRecord<K, V>, R> function) {
        return itemFromRecords(description, function).from(this);
    }

    /**
     * @param description is description of value to get
     * @param cls         is a class of a value to deserialize a message from topics
     * @param function    describes how to get desired value
     * @param <R>         is a type of resulted value
     * @param <M>         is a type of deserialized message
     * @return KafkaPollItemFromRecordSupplier.KafkaPollDeserializedItemFromRecordSupplier
     */
    @Deprecated(forRemoval = true)
    public <R, M> KafkaPollItemFromRecordSupplier.KafkaPollDeserializedItemFromRecordSupplier<K, V, M, R> thenGetItem(String description, Class<M> cls, Function<M, R> function) {
        return itemFromRecords(description, cls, function, this);
    }

    /**
     * @param description is description of value to get
     * @param typeT       is a reference to type of value to deserialize message
     * @param function    describes how to get desired value
     * @param <R>         is a type of resulted value
     * @param <M>         is a type of deserialized message
     * @return KafkaPollItemFromRecordSupplier.KafkaPollDeserializedItemFromRecordSupplier
     */
    @Deprecated(forRemoval = true)
    public <R, M> KafkaPollItemFromRecordSupplier.KafkaPollDeserializedItemFromRecordSupplier<K, V, M, R> thenGetItem(String description, TypeReference<M> typeT, Function<M, R> function) {
        return itemFromRecords(description, typeT, function, this);
    }

    /**
     * @param description is description of value to get
     * @param typeT       is a reference to type of value to deserialize message
     * @param <R>         is a type of deserialized message
     * @return KafkaPollItemFromRecordSupplier.KafkaPollDeserializedItemFromRecordSupplier
     */
    @Deprecated(forRemoval = true)
    public <R> KafkaPollItemFromRecordSupplier.KafkaPollDeserializedItemFromRecordSupplier<K, V, R, R> thenGetItem(String description, TypeReference<R> typeT) {
        return thenGetItem(description, typeT, f -> f);
    }

    /**
     * @param description is description of value to get
     * @param cls         is a class of a value to deserialize a message from topics
     * @param <R>         is a type of deserialized message
     * @return KafkaPollItemFromRecordSupplier.KafkaPollDeserializedItemFromRecordSupplier
     */
    @Deprecated(forRemoval = true)
    public <R> KafkaPollItemFromRecordSupplier.KafkaPollDeserializedItemFromRecordSupplier<K, V, R, R> thenGetItem(String description, Class<R> cls) {
        return thenGetItem(description, cls, f -> f);
    }

    @Override
    protected void onSuccess(List<ConsumerRecord<K, V>> records) {
        function.closeConsumer();
    }

    @Override
    protected void onFailure(KafkaStepContext context, Throwable throwable) {
        function.closeConsumer();
    }

    @Override
    public GetRecordSupplier<K, V> timeOut(Duration timeOut) {
        return super.timeOut(timeOut);
    }
}
