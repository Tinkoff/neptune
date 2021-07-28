package ru.tinkoff.qa.neptune.kafka.functions.poll;

import ru.tinkoff.qa.neptune.core.api.event.firing.annotations.MaxDepthOfReporting;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.kafka.KafkaStepContext;

import java.util.List;
import java.util.function.Function;

@SequentialGetStepSupplier.DefineGetImperativeParameterName("Poll:")
@SequentialGetStepSupplier.DefineTimeOutParameterName("Time of the waiting for messages")
@SequentialGetStepSupplier.DefineCriteriaParameterName("Message criteria")
@MaxDepthOfReporting(0)
public class KafkaPollStepSupplier<T> extends SequentialGetStepSupplier.GetObjectStepSupplier<KafkaStepContext, List<T>, KafkaPollStepSupplier<T>> {
    protected KafkaPollStepSupplier(Function<KafkaStepContext, List<T>> originalFunction) {
        super(originalFunction);
    }
}
