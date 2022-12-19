package ru.tinkoff.qa.neptune.kafka.functions.poll;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotations.CaptureOnSuccess;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.MaxDepthOfReporting;
import ru.tinkoff.qa.neptune.core.api.steps.parameters.ParameterValueGetter;
import ru.tinkoff.qa.neptune.kafka.KafkaStepContext;
import ru.tinkoff.qa.neptune.kafka.captors.ReceivedListCaptor;

import java.util.List;
import java.util.function.Function;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@SequentialGetStepSupplier.DefineGetImperativeParameterName
@SequentialGetStepSupplier.DefineCriteriaParameterName("Object criteria")
@MaxDepthOfReporting(0)
@SuppressWarnings({"rawtypes", "unchecked"})
@CaptureOnSuccess(by = ReceivedListCaptor.class)
public final class KafkaPollListFromRecordSupplier<K, V, R>
    extends SequentialGetStepSupplier.GetListChainedStepSupplier<KafkaStepContext,
    List<R>,
    List<ConsumerRecord<K, V>>,
    R,
    KafkaPollListFromRecordSupplier<K, V, R>> {
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
    static <K, V, R> KafkaPollListFromRecordSupplier<K, V, R> listFromRecords(
        @DescriptionFragment(value = "description",
            makeReadableBy = ParameterValueGetter.TranslatedDescriptionParameterValueGetter.class
        ) String description,
        Function<ConsumerRecord<K, V>, R> f) {
        checkArgument(isNotBlank(description), NO_DESC_ERROR_TEXT);
        return new KafkaPollListFromRecordSupplier(f);
    }

    KafkaPollListFromRecordSupplier<K, V, R> from(GetRecordSupplier<K, V> from) {
        return super.from(from);
    }
}
