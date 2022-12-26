package ru.tinkoff.qa.neptune.kafka.functions.poll;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotations.CaptureOnSuccess;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.MaxDepthOfReporting;
import ru.tinkoff.qa.neptune.kafka.KafkaStepContext;
import ru.tinkoff.qa.neptune.kafka.captors.KafkaConsumerRecordsCaptor;
import ru.tinkoff.qa.neptune.kafka.properties.KafkaDefaultTopicsForPollProperty;

import java.time.Duration;
import java.util.List;
import java.util.function.Function;

import static ru.tinkoff.qa.neptune.kafka.functions.poll.KafkaPollItemFromRecordSupplier.itemFromRecords;
import static ru.tinkoff.qa.neptune.kafka.functions.poll.KafkaPollListFromRecordSupplier.listFromRecords;

@SequentialGetStepSupplier.DefineGetImperativeParameterName("Get from Kafka:")
@SequentialGetStepSupplier.DefineTimeOutParameterName("Time of the waiting")
@SequentialGetStepSupplier.DefineCriteriaParameterName("ConsumerRecord criteria")
@MaxDepthOfReporting(0)
@CaptureOnSuccess(by = KafkaConsumerRecordsCaptor.class)
public final class GetRecordSupplier<K, V> extends SequentialGetStepSupplier.GetListStepSupplier<KafkaStepContext, List<ConsumerRecord<K, V>>, ConsumerRecord<K, V>, GetRecordSupplier<K, V>> {

    private final GetRecords<K, V> function;

    private GetRecordSupplier(GetRecords<K, V> originalFunction) {
        super(originalFunction);
        this.function = originalFunction;
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
     * Defines consumer property value
     *
     * @see <a href="https://kafka.apache.org/documentation/#consumerconfigs">Consumer Configs</a>
     *
     * @param property a property name
     * @param value a property value
     * @return self-reference
     */
    public GetRecordSupplier<K, V> setProperty(String property, String value) {
        function.setProperty(property, value);
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
