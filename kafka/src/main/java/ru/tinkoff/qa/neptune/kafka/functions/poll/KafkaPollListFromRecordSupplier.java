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
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@SequentialGetStepSupplier.DefineGetImperativeParameterName("Poll:")
@SequentialGetStepSupplier.DefineTimeOutParameterName("Time of the waiting")
@SequentialGetStepSupplier.DefineCriteriaParameterName("Object criteria")
@MaxDepthOfReporting(0)
@SuppressWarnings({"rawtypes", "unchecked"})
public abstract class KafkaPollListFromRecordSupplier<R, M, S extends KafkaPollListFromRecordSupplier<R, M, S>>
        extends SequentialGetStepSupplier.GetListChainedStepSupplier<KafkaStepContext,
        List<R>,
        List<ConsumerRecord<String, String>>,
        R,
        S> {
    public static final String NO_DESC_ERROR_TEXT = "Description should be defined";

    private KafkaPollListFromRecordSupplier(Function<ConsumerRecord<String, String>, R> getItemFunction) {
        super(list -> list.stream().map(getItemFunction).collect(toList()));
    }

    private KafkaPollListFromRecordSupplier(Function<List<ConsumerRecord<String, String>>, List<M>> f1, Function<M, R> conversion) {
        super(f1.andThen(ms -> ms.stream().map(conversion).collect(Collectors.toList())));
    }

    @Override
    protected S from(
            SequentialGetStepSupplier<KafkaStepContext, ? extends List<ConsumerRecord<String, String>>, ?, ?, ?> from) {
        return super.from(from);
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
    static <R> KafkaPollListFromRecordSupplier<R, R, ?> listFromRecords(
            @DescriptionFragment(value = "description",
                    makeReadableBy = ParameterValueGetter.TranslatedDescriptionParameterValueGetter.class
            ) String description,
            Function<ConsumerRecord<String, String>, R> f) {
        checkArgument(isNotBlank(description), NO_DESC_ERROR_TEXT);
        return new KafkaPollListFromRecordSupplier(f) {
        };
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
    @Description("{description}")
    static <R, M> KafkaPollDeserializedFromSupplier<R, M> listFromRecords(
            @DescriptionFragment(value = "description",
                    makeReadableBy = ParameterValueGetter.TranslatedDescriptionParameterValueGetter.class
            ) String description,
            Class<M> cls,
            Function<M, R> conversion) {
        checkArgument(isNotBlank(description), NO_DESC_ERROR_TEXT);
        return new KafkaPollDeserializedFromSupplier<>(new GetDeserializedData<>(cls), conversion);
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
    @Description("{description}")
    static <R, M> KafkaPollDeserializedFromSupplier<R, M> listFromRecords(
            @DescriptionFragment(value = "description",
                    makeReadableBy = ParameterValueGetter.TranslatedDescriptionParameterValueGetter.class
            ) String description,
            TypeReference<M> typeT,
            Function<M, R> conversion) {
        checkArgument(isNotBlank(description), NO_DESC_ERROR_TEXT);
        return new KafkaPollDeserializedFromSupplier<>(new GetDeserializedData<>(typeT), conversion);
    }

    public static final class KafkaPollDeserializedFromSupplier<R, M>
            extends KafkaPollListFromRecordSupplier<R, M, KafkaPollDeserializedFromSupplier<R, M>> {

        final GetDeserializedData<M> getDeserializedData;

        private KafkaPollDeserializedFromSupplier(GetDeserializedData<M> getDeserializedData, Function<M, R> conversion) {
            super(getDeserializedData, conversion);
            this.getDeserializedData = getDeserializedData;
        }

        public KafkaPollDeserializedFromSupplier<R, M> withDataTransformer(DataTransformer transformer) {
            getDeserializedData.setTransformer(transformer);
            return this;
        }
    }
}