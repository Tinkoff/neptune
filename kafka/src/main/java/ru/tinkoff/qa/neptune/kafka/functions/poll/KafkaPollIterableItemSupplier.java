package ru.tinkoff.qa.neptune.kafka.functions.poll;

import com.fasterxml.jackson.core.type.TypeReference;
import ru.tinkoff.qa.neptune.core.api.data.format.DataTransformer;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotations.MaxDepthOfReporting;
import ru.tinkoff.qa.neptune.core.api.steps.Criteria;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;
import ru.tinkoff.qa.neptune.core.api.steps.parameters.ParameterValueGetter;
import ru.tinkoff.qa.neptune.kafka.KafkaStepContext;

import java.time.Duration;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@SequentialGetStepSupplier.DefineGetImperativeParameterName("Poll:")
@SequentialGetStepSupplier.DefineTimeOutParameterName("Time of the waiting")
@SequentialGetStepSupplier.DefineCriteriaParameterName("Object criteria")
@MaxDepthOfReporting(0)
public class KafkaPollIterableItemSupplier<T> extends SequentialGetStepSupplier
        .GetObjectFromIterableStepSupplier<KafkaStepContext, T, KafkaPollIterableItemSupplier<T>> {

    final GetFromTopics<?> getFromTopics;

    protected <S> KafkaPollIterableItemSupplier(GetFromTopics<S> getFromTopics, Function<S, T> originalFunction) {
        super(getFromTopics.andThen(list -> list.stream().map(originalFunction).collect(toList())));
        this.getFromTopics = getFromTopics;
    }

    @Description("{description}")
    public static <M, T> KafkaPollIterableItemSupplier<T> kafkaIterableItem(
            @DescriptionFragment(value = "description",
                    makeReadableBy = ParameterValueGetter.TranslatedDescriptionParameterValueGetter.class
            ) String description,
            List<String> topics,
            Class<M> cls,
            Function<M, T> toGet) {
        checkArgument(isNotBlank(description), "Description should be defined");
        return new KafkaPollIterableItemSupplier<>(new GetFromTopics<>(topics, cls), toGet);
    }

    @Description("{description}")
    public static <M, T> KafkaPollIterableItemSupplier<T> kafkaIterableItem(
            @DescriptionFragment(value = "description",
                    makeReadableBy = ParameterValueGetter.TranslatedDescriptionParameterValueGetter.class
            ) String description,
            List<String> topics,
            TypeReference<M> typeT,
            Function<M, T> toGet) {
        checkArgument(isNotBlank(description), "Description should be defined");
        return new KafkaPollIterableItemSupplier<>(new GetFromTopics<>(topics, typeT), toGet);
    }

    public static <M, T> KafkaPollIterableItemSupplier<T> kafkaIterableItem(
            String description,
            List<String> topics,
            Class<M> cls) {
        checkArgument(isNotBlank(description), "Description should be defined");
        return (KafkaPollIterableItemSupplier<T>) kafkaIterableItem(description, topics, cls, t -> t);
    }

    public static <M, T> KafkaPollIterableItemSupplier<T> kafkaIterableItem(
            String description,
            List<String> topics,
            TypeReference<M> typeT) {
        checkArgument(isNotBlank(description), "Description should be defined");
        return (KafkaPollIterableItemSupplier<T>) kafkaIterableItem(description, topics, typeT, t -> t);
    }

    @Override
    public KafkaPollIterableItemSupplier<T> timeOut(Duration timeOut) {
        return super.timeOut(timeOut);
    }

    @Override
    public KafkaPollIterableItemSupplier<T> criteria(String description, Predicate<? super T> predicate) {
        return super.criteria(description, predicate);
    }

    @Override
    public KafkaPollIterableItemSupplier<T> criteria(Criteria<? super T> criteria) {
        return super.criteria(criteria);
    }

    KafkaPollIterableItemSupplier<T> setDataTransformer(DataTransformer dataTransformer) {
        checkNotNull(dataTransformer);
        getFromTopics.setTransformer(dataTransformer);
        return this;
    }
}
