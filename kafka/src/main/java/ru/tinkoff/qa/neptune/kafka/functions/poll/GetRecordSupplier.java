package ru.tinkoff.qa.neptune.kafka.functions.poll;

import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.MaxDepthOfReporting;
import ru.tinkoff.qa.neptune.core.api.steps.parameters.ParameterValueGetter;
import ru.tinkoff.qa.neptune.kafka.KafkaStepContext;

import java.time.Duration;
import java.util.List;
import java.util.function.Function;

import static com.google.common.base.Preconditions.checkArgument;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static ru.tinkoff.qa.neptune.kafka.functions.poll.KafkaPollItemFromRecordSupplier.itemFromRecords;
import static ru.tinkoff.qa.neptune.kafka.functions.poll.KafkaPollListFromRecordSupplier.listFromRecords;

@SequentialGetStepSupplier.DefineGetImperativeParameterName("Poll:")
@SequentialGetStepSupplier.DefineTimeOutParameterName("Time of the waiting")
@SequentialGetStepSupplier.DefineCriteriaParameterName("ConsumerRecord criteria")
@MaxDepthOfReporting(0)
public class GetRecordSupplier<K, V> extends SequentialGetStepSupplier.GetListStepSupplier<KafkaStepContext, List<ConsumerRecord<K, V>>, ConsumerRecord<K, V>, GetRecordSupplier<K, V>> {

    final GetRecords<K, V> function;

    protected GetRecordSupplier(GetRecords<K, V> originalFunction) {
        super(originalFunction);
        this.function = originalFunction;
    }

    @Description("{description}")
    public static GetRecordSupplier<String, String> consumerRecords(
        @DescriptionFragment(value = "description",
            makeReadableBy = ParameterValueGetter.TranslatedDescriptionParameterValueGetter.class
        ) String description,
        String... topics) {
        checkArgument(isNotBlank(description), "Description should be defined");
        return new GetRecordSupplier<>(new GetRecords<>(topics)
            .setKeyDeserializer(new StringDeserializer())
            .setValueDeserializer(new StringDeserializer()));
    }

    @Description("Kafka messages")
    public static GetRecordSupplier<String, String> records(String... topics) {
        return new GetRecordSupplier<>(new GetRecords<>(topics)
            .setKeyDeserializer(new StringDeserializer())
            .setValueDeserializer(new StringDeserializer()));
    }

    /**
     * Defines new deserializer for message keys
     *
     * @param deserializer new deserializer for message keys
     * @param <K2>         new type of message keys
     * @return self-reference
     */
    @SuppressWarnings("unchecked")
    public <K2> GetRecordSupplier<K2, V> withKeyDeserializer(Deserializer<K2> deserializer) {
        var keyFunction = (GetRecords<K2, V>) this.function;
        keyFunction.setKeyDeserializer(deserializer);
        return (GetRecordSupplier<K2, V>) this;
    }

    /**
     * Defines new deserializer for message values
     *
     * @param deserializer new deserializer for message values
     * @param <V2>         new type of message values
     * @return self-reference
     */
    @SuppressWarnings("unchecked")
    public <V2> GetRecordSupplier<K, V2> withValueDeserializer(Deserializer<V2> deserializer) {
        var valueFunction = (GetRecords<K, V2>) this.function;
        valueFunction.setKeyDeserializer(deserializer);
        return (GetRecordSupplier<K, V2>) this;
    }

    /**
     * @param description     is description of value to get
     * @param getItemFunction
     * @param <R>             is a type of item of iterable
     * @return KafkaPollListFromRecordSupplier
     */
    public <R> KafkaPollListFromRecordSupplier<R, R, ?> thenGetList(String description, Function<ConsumerRecord<String, String>, R> getItemFunction) {
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
    public <R, M> KafkaPollListFromRecordSupplier.KafkaPollDeserializedFromSupplier<R, M> thenGetList(String description, Class<M> cls, Function<M, R> conversion) {
        return listFromRecords(description, cls, conversion).from(this);
    }

    /**
     * @param description is description of value to get
     * @param typeT       is a reference to type of value to deserialize message
     * @param conversion  describes how to get desired value
     * @param <R>         is a type of item of iterable
     * @param <M>         is a type of deserialized message
     * @return KafkaPollListFromRecordSupplier.KafkaPollDeserializedFromSupplier
     */
    public <R, M> KafkaPollListFromRecordSupplier.KafkaPollDeserializedFromSupplier<R, M> thenGetList(String description, TypeReference<M> typeT, Function<M, R> conversion) {
        return listFromRecords(description, typeT, conversion).from(this);
    }

    /**
     * @param description is description of value to get
     * @param cls         is a class of a value to deserialize a message from topics
     * @param <R>         is a type of deserialized message
     * @return KafkaPollListFromRecordSupplier.KafkaPollDeserializedFromSupplier
     */
    public <R> KafkaPollListFromRecordSupplier.KafkaPollDeserializedFromSupplier<R, ?> thenGetList(String description, Class<R> cls) {
        return thenGetList(description, cls, f -> f);
    }

    /**
     * @param description is description of value to get
     * @param typeT       is a reference to type of value to deserialize message
     * @param <R>         is a type of deserialized message
     * @return KafkaPollListFromRecordSupplier.KafkaPollDeserializedFromSupplier
     */
    public <R> KafkaPollListFromRecordSupplier.KafkaPollDeserializedFromSupplier<R, ?> thenGetList(String description, TypeReference<R> typeT) {
        return thenGetList(description, typeT, f -> f);
    }

    /**
     * @param description is description of value to get
     * @param function    describes how to get desired value
     * @param <R>         is a type of item
     * @return KafkaPollItemFromRecordSupplier
     */
    public <R> KafkaPollItemFromRecordSupplier<R, R, ?> thenGetItem(String description, Function<ConsumerRecord<String, String>, R> function) {
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
    public <R, M> KafkaPollItemFromRecordSupplier.KafkaPollDeserializedItemFromRecordSupplier<R, M> thenGetItem(String description, Class<M> cls, Function<M, R> function) {
        return itemFromRecords(description, cls, function).from(this);
    }

    /**
     * @param description is description of value to get
     * @param typeT       is a reference to type of value to deserialize message
     * @param function    describes how to get desired value
     * @param <R>         is a type of resulted value
     * @param <M>         is a type of deserialized message
     * @return KafkaPollItemFromRecordSupplier.KafkaPollDeserializedItemFromRecordSupplier
     */
    public <R, M> KafkaPollItemFromRecordSupplier.KafkaPollDeserializedItemFromRecordSupplier<R, M> thenGetItem(String description, TypeReference<M> typeT, Function<M, R> function) {
        return itemFromRecords(description, typeT, function).from(this);
    }

    /**
     * @param description is description of value to get
     * @param typeT       is a reference to type of value to deserialize message
     * @param <R>         is a type of deserialized message
     * @return KafkaPollItemFromRecordSupplier.KafkaPollDeserializedItemFromRecordSupplier
     */
    public <R> KafkaPollItemFromRecordSupplier.KafkaPollDeserializedItemFromRecordSupplier<R, ?> thenGetItem(String description, TypeReference<R> typeT) {
        return thenGetItem(description, typeT, f -> f);
    }

    /**
     * @param description is description of value to get
     * @param cls         is a class of a value to deserialize a message from topics
     * @param <R>         is a type of deserialized message
     * @return KafkaPollItemFromRecordSupplier.KafkaPollDeserializedItemFromRecordSupplier
     */
    public <R> KafkaPollItemFromRecordSupplier.KafkaPollDeserializedItemFromRecordSupplier<R, ?> thenGetItem(String description, Class<R> cls) {
        return thenGetItem(description, cls, f -> f);
    }

    @Override
    public GetRecordSupplier timeOut(Duration timeOut) {
        return super.timeOut(timeOut);
    }
}
