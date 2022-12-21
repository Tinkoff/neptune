package ru.tinkoff.qa.neptune.kafka.functions.poll;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotations.CaptureOnSuccess;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.MaxDepthOfReporting;
import ru.tinkoff.qa.neptune.core.api.steps.parameters.ParameterValueGetter;
import ru.tinkoff.qa.neptune.kafka.KafkaStepContext;
import ru.tinkoff.qa.neptune.kafka.captors.KafkaObjectResultCaptor;

import java.util.List;
import java.util.function.Function;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@SequentialGetStepSupplier.DefineGetImperativeParameterName
@SequentialGetStepSupplier.DefineCriteriaParameterName("Object criteria")
@MaxDepthOfReporting(0)
@SuppressWarnings({"rawtypes", "unchecked"})
@CaptureOnSuccess(by = KafkaObjectResultCaptor.class)
public final class KafkaPollItemFromRecordSupplier<K, V, R>
    extends SequentialGetStepSupplier.GetObjectFromIterableChainedStepSupplier<KafkaStepContext, R, List<ConsumerRecord<K, V>>, KafkaPollItemFromRecordSupplier<K, V, R>> {

    public static final String NO_DESC_ERROR_TEXT = "Description should be defined";

    private KafkaPollItemFromRecordSupplier(Function<ConsumerRecord<K, V>, R> f) {
        super(list -> list.stream().map(new KafkaSafeFunction<>(f)).collect(toList()));
    }

    @Description("{description}")
    static <K, V, R> KafkaPollItemFromRecordSupplier<K, V, R> itemFromRecords(
        @DescriptionFragment(value = "description",
            makeReadableBy = ParameterValueGetter.TranslatedDescriptionParameterValueGetter.class
        ) String description,
        Function<ConsumerRecord<K, V>, R> f) {
        checkArgument(isNotBlank(description), NO_DESC_ERROR_TEXT);
        return new KafkaPollItemFromRecordSupplier(f);
    }

    KafkaPollItemFromRecordSupplier<K, V, R> from(GetRecordSupplier<K, V> from) {
        return super.from(from);
    }
}
