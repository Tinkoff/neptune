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
public class KafkaPollItemFromRecordSupplier<K, V, R, I extends KafkaPollItemFromRecordSupplier<K, V, R, I>>
    extends SequentialGetStepSupplier.GetObjectFromIterableChainedStepSupplier<KafkaStepContext, R, List<ConsumerRecord<K, V>>, I> {

    public static final String NO_DESC_ERROR_TEXT = "Description should be defined";

    private KafkaPollItemFromRecordSupplier(Function<ConsumerRecord<K, V>, R> f) {
        super(list -> list.stream().map(new KafkaSafeFunction<>(f)).collect(toList()));
    }

    @Description("{description}")
    static <K, V, R> KafkaPollItemFromRecordSupplier<K, V, R, ?> itemFromRecords(
        @DescriptionFragment(value = "description",
            makeReadableBy = ParameterValueGetter.TranslatedDescriptionParameterValueGetter.class
        ) String description,
        Function<ConsumerRecord<K, V>, R> f) {
        checkArgument(isNotBlank(description), NO_DESC_ERROR_TEXT);
        return new KafkaPollItemFromRecordSupplier(f);
    }

    @Deprecated(forRemoval = true)
    @Description("{description}")
    static <K, V, R, M> KafkaPollDeserializedItemFromRecordSupplier<K, V, M, R> itemFromRecords(
        @DescriptionFragment(value = "description",
            makeReadableBy = ParameterValueGetter.TranslatedDescriptionParameterValueGetter.class
        ) String description,
        Class<M> cls,
        Function<M, R> conversion,
        GetRecordSupplier<K, V> getRecordSupplier) {
        checkArgument(isNotBlank(description), NO_DESC_ERROR_TEXT);
        return new KafkaPollDeserializedItemFromRecordSupplier<K, V, M, R>(new Conversion<>(conversion, cls, null))
            .from(getRecordSupplier);
    }

    @Deprecated(forRemoval = true)
    @Description("{description}")
    static <K, V, R, M> KafkaPollDeserializedItemFromRecordSupplier<K, V, M, R> itemFromRecords(
        @DescriptionFragment(value = "description",
            makeReadableBy = ParameterValueGetter.TranslatedDescriptionParameterValueGetter.class
        ) String description,
        TypeReference<M> typeT,
        Function<M, R> conversion,
        GetRecordSupplier<K, V> getRecordSupplier) {
        checkArgument(isNotBlank(description), NO_DESC_ERROR_TEXT);
        return new KafkaPollDeserializedItemFromRecordSupplier<K, V, M, R>(new Conversion<>(conversion, null, typeT))
            .from(getRecordSupplier);
    }

    I from(GetRecordSupplier<K, V> from) {
        return super.from(from);
    }

    @Deprecated(forRemoval = true)
    public static final class KafkaPollDeserializedItemFromRecordSupplier<K, V, M, R>
        extends KafkaPollItemFromRecordSupplier<K, V, R, KafkaPollDeserializedItemFromRecordSupplier<K, V, M, R>> {

        private Conversion<K, V, M, R> conversion;

        private KafkaPollDeserializedItemFromRecordSupplier(Conversion<K, V, M, R> conversion) {
            super(conversion);
            this.conversion = conversion;
        }

        @Deprecated(forRemoval = true)
        public KafkaPollDeserializedItemFromRecordSupplier<K, V, M, R> withDataTransformer(DataTransformer transformer) {
            conversion.setTransformer(transformer);
            return this;
        }
    }
}
