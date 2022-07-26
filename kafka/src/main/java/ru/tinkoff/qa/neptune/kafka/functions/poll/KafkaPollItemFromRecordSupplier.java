package ru.tinkoff.qa.neptune.kafka.functions.poll;

import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import ru.tinkoff.qa.neptune.core.api.data.format.DataTransformer;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotations.MaxDepthOfReporting;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;
import ru.tinkoff.qa.neptune.core.api.steps.parameters.ParameterValueGetter;
import ru.tinkoff.qa.neptune.kafka.KafkaStepContext;

import java.util.List;
import java.util.function.Function;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@SequentialGetStepSupplier.DefineGetImperativeParameterName("Poll:")
@SequentialGetStepSupplier.DefineTimeOutParameterName("Time of the waiting")
@SequentialGetStepSupplier.DefineCriteriaParameterName("Object criteria")
@MaxDepthOfReporting(0)
@SuppressWarnings({"rawtypes", "unchecked"})
public abstract class KafkaPollItemFromRecordSupplier<R, M, I extends KafkaPollItemFromRecordSupplier<R, M, I>>
        extends SequentialGetStepSupplier.GetObjectFromIterableChainedStepSupplier<KafkaStepContext, R, List<ConsumerRecord<String, String>>, I> {

    public static final String NO_DESC_ERROR_TEXT = "Description should be defined";

    private KafkaPollItemFromRecordSupplier(Function<ConsumerRecord<String, String>, R> originalFunction) {
        super(list -> list.stream().map(originalFunction).collect(toList()));
    }

    private KafkaPollItemFromRecordSupplier(GetDeserializedData<M> f1, Function<M, R> f2) {
        super(f1.andThen(ms -> ms.stream().map(f2).collect(toList())));
    }

    @Override
    protected I from(SequentialGetStepSupplier<KafkaStepContext, ? extends List<ConsumerRecord<String, String>>, ?, ?, ?> from) {
        return super.from(from);
    }

    @Description("{description}")
    static <R> KafkaPollItemFromRecordSupplier<R, R, ?> itemFromRecords(
            @DescriptionFragment(value = "description",
                    makeReadableBy = ParameterValueGetter.TranslatedDescriptionParameterValueGetter.class
            ) String description,
            Function<ConsumerRecord<String, String>, R> f) {
        checkArgument(isNotBlank(description), NO_DESC_ERROR_TEXT);
        return new KafkaPollItemFromRecordSupplier(f) {
        };
    }

    @Description("{description}")
    static <R, M> KafkaPollDeserializedItemFromRecordSupplier<R, M> itemFromRecords(
            @DescriptionFragment(value = "description",
                    makeReadableBy = ParameterValueGetter.TranslatedDescriptionParameterValueGetter.class
            ) String description,
            Class<M> cls,
            Function<M, R> conversion) {
        checkArgument(isNotBlank(description), NO_DESC_ERROR_TEXT);
        return new KafkaPollDeserializedItemFromRecordSupplier(new GetDeserializedData<>(cls), conversion);
    }

    @Description("{description}")
    static <R, M> KafkaPollDeserializedItemFromRecordSupplier<R, M> itemFromRecords(
            @DescriptionFragment(value = "description",
                    makeReadableBy = ParameterValueGetter.TranslatedDescriptionParameterValueGetter.class
            ) String description,
            TypeReference<M> typeT,
            Function<M, R> conversion) {
        checkArgument(isNotBlank(description), NO_DESC_ERROR_TEXT);
        return new KafkaPollDeserializedItemFromRecordSupplier(new GetDeserializedData<>(typeT), conversion);
    }


    public static final class KafkaPollDeserializedItemFromRecordSupplier<R, M>
            extends KafkaPollItemFromRecordSupplier<R, M, KafkaPollDeserializedItemFromRecordSupplier<R, M>> {

        final GetDeserializedData<M> getDeserializedData;

        public KafkaPollDeserializedItemFromRecordSupplier(GetDeserializedData<M> getDeserializedData, Function<M, R> convert) {
            super(getDeserializedData, convert);
            this.getDeserializedData = getDeserializedData;
        }

        public KafkaPollDeserializedItemFromRecordSupplier<R, M> withDataTransformer(DataTransformer transformer) {
            getDeserializedData.setTransformer(transformer);
            return this;
        }
    }
}
