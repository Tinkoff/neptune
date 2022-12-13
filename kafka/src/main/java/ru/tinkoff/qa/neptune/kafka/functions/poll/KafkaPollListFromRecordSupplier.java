package ru.tinkoff.qa.neptune.kafka.functions.poll;

import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import ru.tinkoff.qa.neptune.core.api.data.format.DataTransformer;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.MaxDepthOfReporting;
import ru.tinkoff.qa.neptune.core.api.steps.parameters.ParameterValueGetter;
import ru.tinkoff.qa.neptune.kafka.KafkaStepContext;

import java.util.List;
import java.util.function.Function;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@SequentialGetStepSupplier.DefineGetImperativeParameterName
@SequentialGetStepSupplier.DefineCriteriaParameterName("Object criteria")
@MaxDepthOfReporting(0)
@SuppressWarnings({"rawtypes", "unchecked"})
public class KafkaPollListFromRecordSupplier<K, V, R, S extends KafkaPollListFromRecordSupplier<K, V, R, S>>
    extends SequentialGetStepSupplier.GetListChainedStepSupplier<KafkaStepContext,
    List<R>,
    List<ConsumerRecord<K, V>>,
    R,
    S> {
    public static final String NO_DESC_ERROR_TEXT = "Description should be defined";

    private KafkaPollListFromRecordSupplier(Function<ConsumerRecord<K, V>, R> f) {
        super(list -> list.stream().map(new KafkaSafeFunction<>(f)).collect(toList()));
    }

    /**
     * Creates a step that returns an iteration of values calculated from read messages.
     *
     * @param description is description of value to get
     * @param f           describes how to get desired value
     * @param <R>         is a type of item of iterable
     * @return KafkaPollDeserializedFromSupplier
     */
    @Description("{description}")
    static <K, V, R> KafkaPollListFromRecordSupplier<K, V, R, ?> listFromRecords(
        @DescriptionFragment(value = "description",
            makeReadableBy = ParameterValueGetter.TranslatedDescriptionParameterValueGetter.class
        ) String description,
        Function<ConsumerRecord<K, V>, R> f) {
        checkArgument(isNotBlank(description), NO_DESC_ERROR_TEXT);
        return new KafkaPollListFromRecordSupplier(f);
    }

    /**
     * Creates a step that returns iterable of values which are calculated by data of read messages.
     *
     * @param description is description of value to get
     * @param cls         is a class of a value to deserialize a message
     * @param conversion  describes how to get desired value
     * @param <R>         is a type of item of iterable
     * @param <M>         is a type of deserialized message
     * @return KafkaPollDeserializedFromSupplier
     */
    @Deprecated(forRemoval = true)
    @Description("{description}")
    static <K, V, M, R> KafkaPollDeserializedFromSupplier<K, V, M, R> listFromRecords(
        @DescriptionFragment(value = "description",
            makeReadableBy = ParameterValueGetter.TranslatedDescriptionParameterValueGetter.class
        ) String description,
        Class<M> cls,
        Function<M, R> conversion,
        GetRecordSupplier<K, V> getRecordSupplier) {
        checkArgument(isNotBlank(description), NO_DESC_ERROR_TEXT);
        return new KafkaPollDeserializedFromSupplier<K, V, M, R>(new Conversion<>(conversion, cls, null))
            .from(getRecordSupplier);
    }

    /**
     * Creates a step that returns iterable of values which are calculated by data of read messages.
     *
     * @param description is description of value to get
     * @param typeT       is a reference to type of value to deserialize message
     * @param conversion  describes how to get desired value
     * @param <R>         is a type of item of iterable
     * @param <M>         is a type of deserialized message
     * @return KafkaPollDeserializedFromSupplier
     */
    @Deprecated(forRemoval = true)
    @Description("{description}")
    static <K, V, M, R> KafkaPollDeserializedFromSupplier<K, V, M, R> listFromRecords(
        @DescriptionFragment(value = "description",
            makeReadableBy = ParameterValueGetter.TranslatedDescriptionParameterValueGetter.class
        ) String description,
        TypeReference<M> typeT,
        Function<M, R> conversion,
        GetRecordSupplier<K, V> getRecordSupplier) {
        checkArgument(isNotBlank(description), NO_DESC_ERROR_TEXT);
        return new KafkaPollDeserializedFromSupplier<K, V, M, R>(new Conversion<>(conversion, null, typeT))
            .from(getRecordSupplier);
    }

    S from(GetRecordSupplier<K, V> from) {
        return super.from(from);
    }

    @Deprecated(forRemoval = true)
    public static final class KafkaPollDeserializedFromSupplier<K, V, M, R>
        extends KafkaPollListFromRecordSupplier<K, V, R, KafkaPollDeserializedFromSupplier<K, V, M, R>> {

        private Conversion<K, V, M, R> conversion;

        private KafkaPollDeserializedFromSupplier(Conversion<K, V, M, R> conversion) {
            super(conversion);
            this.conversion = conversion;
        }

        @Deprecated(forRemoval = true)
        public KafkaPollDeserializedFromSupplier<K, V, M, R> withDataTransformer(DataTransformer transformer) {
            conversion.setTransformer(transformer);
            return this;
        }
    }
}
