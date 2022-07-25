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
import static com.google.common.base.Preconditions.checkState;
import static java.util.Objects.nonNull;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static ru.tinkoff.qa.neptune.kafka.properties.DefaultDataTransformers.KAFKA_DEFAULT_DATA_TRANSFORMER;

@SequentialGetStepSupplier.DefineGetImperativeParameterName("Poll:")
@SequentialGetStepSupplier.DefineTimeOutParameterName("Time of the waiting")
@SequentialGetStepSupplier.DefineCriteriaParameterName("Object criteria")
@MaxDepthOfReporting(0)
@SuppressWarnings({"rawtypes", "unchecked"})
public abstract class KafkaPollItemFromRecordSupplier<R, M, I extends KafkaPollItemFromRecordSupplier<R, M, I>>
        extends SequentialGetStepSupplier.GetObjectFromIterableChainedStepSupplier<KafkaStepContext, R, List<ConsumerRecord<String, String>>, I> {

    private KafkaPollItemFromRecordSupplier(Function<ConsumerRecord<String, String>, R> originalFunction) {
        super(list -> list.stream().map(originalFunction).collect(toList()));
    }

    private KafkaPollItemFromRecordSupplier(GetFromTopics<M> f1, Function<M, R> f2) {
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
        checkArgument(isNotBlank(description), "Description should be defined");
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
        checkArgument(isNotBlank(description), "Description should be defined");
        return new KafkaPollDeserializedItemFromRecordSupplier(new GetFromTopics<>(cls), conversion);
    }

    @Description("{description}")
    static <R, M> KafkaPollDeserializedItemFromRecordSupplier<R, M> itemFromRecords(
            @DescriptionFragment(value = "description",
                    makeReadableBy = ParameterValueGetter.TranslatedDescriptionParameterValueGetter.class
            ) String description,
            TypeReference<M> typeT,
            Function<M, R> conversion) {
        checkArgument(isNotBlank(description), "Description should be defined");
        return new KafkaPollDeserializedItemFromRecordSupplier(new GetFromTopics<>(typeT), conversion);
    }


    public static final class KafkaPollDeserializedItemFromRecordSupplier<R, M>
            extends KafkaPollItemFromRecordSupplier<R, M, KafkaPollDeserializedItemFromRecordSupplier<R, M>> {

        private DataTransformer transformer;
        final GetFromTopics<M> getFromTopics;

        public KafkaPollDeserializedItemFromRecordSupplier(GetFromTopics<M> getFromTopics, Function<M, R> convert) {
            super(getFromTopics, convert);
            this.getFromTopics = getFromTopics;
        }

        public KafkaPollDeserializedItemFromRecordSupplier<R, M> withDataTransformer(DataTransformer transformer) {
            this.transformer = transformer;
            return this;
        }

        @Override
        protected void onStart(List<ConsumerRecord<String, String>> records) {
            var transformer = ofNullable(this.transformer)
                    .orElseGet(KAFKA_DEFAULT_DATA_TRANSFORMER);
            checkState(nonNull(transformer), "Data transformer is not defined. Please invoke "
                    + "the '#withDataTransformer(DataTransformer)' method or define '"
                    + KAFKA_DEFAULT_DATA_TRANSFORMER.getName()
                    + "' property/env variable");
            getFromTopics.setTransformer(transformer);
        }
    }
}
