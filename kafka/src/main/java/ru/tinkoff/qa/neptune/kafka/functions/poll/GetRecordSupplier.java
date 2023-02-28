package ru.tinkoff.qa.neptune.kafka.functions.poll;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotations.CaptureOnSuccess;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.StepParameter;
import ru.tinkoff.qa.neptune.kafka.KafkaStepContext;
import ru.tinkoff.qa.neptune.kafka.captors.KafkaConsumerRecordsCaptor;

import java.time.Duration;
import java.util.List;
import java.util.function.Function;

import static ru.tinkoff.qa.neptune.kafka.functions.poll.KafkaPollItemFromRecordSupplier.itemFromRecords;
import static ru.tinkoff.qa.neptune.kafka.functions.poll.KafkaPollListFromRecordSupplier.listFromRecords;

@SequentialGetStepSupplier.DefineGetImperativeParameterName("Get from Kafka:")
@SequentialGetStepSupplier.DefineTimeOutParameterName("Time of the waiting")
@SequentialGetStepSupplier.DefineCriteriaParameterName("ConsumerRecord criteria")
@CaptureOnSuccess(by = KafkaConsumerRecordsCaptor.class)
public final class GetRecordSupplier<K, V>
    extends SequentialGetStepSupplier.GetListStepSupplier<KafkaStepContext, List<ConsumerRecord<K, V>>, ConsumerRecord<K, V>, GetRecordSupplier<K, V>>
    implements PollStep<GetRecordSupplier<K, V>> {

    private final PollFunction<K, V, List<ConsumerRecord<K, V>>> function;

    @StepParameter(value = "Exclude consumer records with null-values")
    boolean excludeRecordsWithNullValues;

    @StepParameter(value = "Exclude consumer records with null-keys")
    boolean excludeRecordsWithNullKeys;

    private GetRecordSupplier(GetRecords<K, V> originalFunction,
                              Deserializer<K> keyDeserializer,
                              Deserializer<V> valueDeserializer) {
        super(originalFunction);
        this.function = new PollFunction<>(originalFunction, keyDeserializer, valueDeserializer);
    }

    @Description("Kafka consumer records")
    public static <K, V> GetRecordSupplier<K, V> consumerRecords(Deserializer<K> keyDeserializer,
                                                                 Deserializer<V> valueDeserializer) {
        return new GetRecordSupplier<>(new GetRecords<>(), keyDeserializer, valueDeserializer);
    }

    public static GetRecordSupplier<String, String> consumerRecords() {
        return consumerRecords(new StringDeserializer(),
            new StringDeserializer());
    }

    /**
     * Says to exclude records with null values (values of records are empty or not properly deserialized)
     * from the result.
     * <p></p>
     * All records (with null and non-null values) are included in result by default.
     *
     * @return self-reference
     */
    public GetRecordSupplier<K, V> excludeWithNullValues() {
        excludeRecordsWithNullValues = true;
        function.excludeNullValues();
        return this;
    }

    /**
     * Says to exclude records with null keys (keys of records are empty or not properly deserialized)
     * from the result.
     * <p></p>
     * All records (with null and non-null keys) are included in result by default.
     *
     * @return self-reference
     */
    public GetRecordSupplier<K, V> excludeWithNullKeys() {
        excludeRecordsWithNullKeys = true;
        function.excludeNullKeys();
        return this;
    }

    /**
     * @param description     is description of value to get
     * @param getItemFunction describes how get resulted list item from each {@link ConsumerRecord}
     * @param <R>             is a type of item of iterable
     * @return KafkaPollListFromRecordSupplier
     */
    public <R> KafkaPollListFromRecordSupplier<K, V, R> thenGetList(String description, Function<ConsumerRecord<K, V>, R> getItemFunction) {
        return listFromRecords(description, getItemFunction).from(this);
    }

    /**
     * @param description is description of value to get
     * @param function    describes how to get desired value from each {@link ConsumerRecord}
     * @param <R>         is a type of item
     * @return KafkaPollItemFromRecordSupplier
     */
    public <R> KafkaPollItemFromRecordSupplier<K, V, R> thenGetItem(String description, Function<ConsumerRecord<K, V>, R> function) {
        return itemFromRecords(description, function).from(this);
    }

    @Override
    public GetRecordSupplier<K, V> timeOut(Duration timeOut) {
        return super.timeOut(timeOut);
    }

    public Function<KafkaStepContext, List<ConsumerRecord<K, V>>> get() {
        return function.setDelegateTo(super.get());
    }
}
