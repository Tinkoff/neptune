package ru.tinkoff.qa.neptune.kafka.functions.poll;

import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotations.MaxDepthOfReporting;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;
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
public class GetRecordSupplier extends SequentialGetStepSupplier.GetListStepSupplier<KafkaStepContext, List<ConsumerRecord<String, String>>, ConsumerRecord<String, String>, GetRecordSupplier> {

    final GetRecords function;

    protected GetRecordSupplier(GetRecords originalFunction) {
        super(originalFunction);
        this.function = originalFunction;
    }

    @Description("{description}")
    public static GetRecordSupplier consumerRecords(
            @DescriptionFragment(value = "description",
                    makeReadableBy = ParameterValueGetter.TranslatedDescriptionParameterValueGetter.class
            ) String description,
            String... topics) {
        checkArgument(isNotBlank(description), "Description should be defined");
        return new GetRecordSupplier(new GetRecords(topics));
    }

    /**
     * @param description     is description of value to get
     * @param getItemFunction
     * @param <R>             is a type of item of iterable
     * @return
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
     * @return
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
     * @return
     */
    public <R, M> KafkaPollListFromRecordSupplier.KafkaPollDeserializedFromSupplier<R, M> thenGetList(String description, TypeReference<M> typeT, Function<M, R> conversion) {
        return listFromRecords(description, typeT, conversion).from(this);
    }

    /**
     * @param description is description of value to get
     * @param cls         is a class of a value to deserialize a message from topics
     * @param <R>         is a type of deserialized message
     * @return
     */
    public <R> KafkaPollListFromRecordSupplier.KafkaPollDeserializedFromSupplier<R, ?> thenGetList(String description, Class<R> cls) {
        return thenGetList(description, cls, f -> f);
    }

    /**
     * @param description is description of value to get
     * @param typeT       is a reference to type of value to deserialize message
     * @param <R>         is a type of deserialized message
     * @return
     */
    public <R> KafkaPollListFromRecordSupplier.KafkaPollDeserializedFromSupplier<R, ?> thenGetList(String description, TypeReference<R> typeT) {
        return thenGetList(description, typeT, f -> f);
    }

    /**
     * @param description is description of value to get
     * @param function    describes how to get desired value
     * @param <R>         is a type of item
     * @return
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
     * @return
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
     * @return
     */
    public <R, M> KafkaPollItemFromRecordSupplier.KafkaPollDeserializedItemFromRecordSupplier<R, M> thenGetItem(String description, TypeReference<M> typeT, Function<M, R> function) {
        return itemFromRecords(description, typeT, function).from(this);
    }

    /**
     * @param description is description of value to get
     * @param typeT       is a reference to type of value to deserialize message
     * @param <R>         is a type of deserialized message
     * @return
     */
    public <R> KafkaPollItemFromRecordSupplier.KafkaPollDeserializedItemFromRecordSupplier<R, ?> thenGetItem(String description, TypeReference<R> typeT) {
        return thenGetItem(description, typeT, f -> f);
    }

    /**
     * @param description is description of value to get
     * @param cls         is a class of a value to deserialize a message from topics
     * @param <R>         is a type of deserialized message
     * @return
     */
    public <R> KafkaPollItemFromRecordSupplier.KafkaPollDeserializedItemFromRecordSupplier<R, ?> thenGetItem(String description, Class<R> cls) {
        return thenGetItem(description, cls, f -> f);
    }

    @Override
    public GetRecordSupplier timeOut(Duration timeOut) {
        return super.timeOut(timeOut);
    }
}
