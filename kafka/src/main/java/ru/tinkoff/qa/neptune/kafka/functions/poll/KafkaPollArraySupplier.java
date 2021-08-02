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
import static org.apache.commons.lang3.StringUtils.isNotBlank;


@SequentialGetStepSupplier.DefineGetImperativeParameterName("Poll:")
@SequentialGetStepSupplier.DefineTimeOutParameterName("Time of the waiting")
@SequentialGetStepSupplier.DefineCriteriaParameterName("Criteria for every item of resulted array")
@MaxDepthOfReporting(0)
@SuppressWarnings("unchecked")
public class KafkaPollArraySupplier<T> extends SequentialGetStepSupplier
        .GetArrayStepSupplier<KafkaStepContext, T, KafkaPollArraySupplier<T>> {

    final GetFromTopics<?> getFromTopics;

    protected <M> KafkaPollArraySupplier(GetFromTopics<M> getFromTopics, Function<M, T> originalFunction) {
        super(getFromTopics.andThen(list -> list.stream().map(originalFunction).toArray(value -> (T[]) new Object[value])));
        this.getFromTopics = getFromTopics;
    }

    @Description("{description}")
    public static <M, T> KafkaPollArraySupplier<T> kafkaArray(
            @DescriptionFragment(value = "description",
                    makeReadableBy = ParameterValueGetter.TranslatedDescriptionParameterValueGetter.class
            ) String description,
            List<String> topics,
            Class<M> classT,
            Function<M, T> toGet) {
        checkArgument(isNotBlank(description), "Description should be defined");
        return new KafkaPollArraySupplier<>(new GetFromTopics<>(topics, classT), toGet);
    }

    @Description("{description}")
    public static <M, T> KafkaPollArraySupplier<T> kafkaArray(
            @DescriptionFragment(value = "description",
                    makeReadableBy = ParameterValueGetter.TranslatedDescriptionParameterValueGetter.class
            ) String description,
            List<String> topics,
            TypeReference<M> typeT,
            Function<M, T> toGet) {
        checkArgument(isNotBlank(description), "Description should be defined");
        return new KafkaPollArraySupplier<>(new GetFromTopics<>(topics, typeT), toGet);
    }

    public static <T> KafkaPollArraySupplier<T> kafkaArray(
            String description,
            List<String> topics,
            Class<T> classT) {
        checkArgument(isNotBlank(description), "Description should be defined");
        return kafkaArray(description, topics, classT, ts -> ts);
    }

    public static <T> KafkaPollArraySupplier<T> kafkaArray(
            String description,
            List<String> topics,
            TypeReference<T> typeT) {
        checkArgument(isNotBlank(description), "Description should be defined");
        return kafkaArray(description, topics, typeT, ts -> ts);
    }

    @Override
    public KafkaPollArraySupplier<T> timeOut(Duration timeOut) {
        return super.timeOut(timeOut);
    }

    @Override
    public KafkaPollArraySupplier<T> criteria(String description, Predicate<? super T> predicate) {
        return super.criteria(description, predicate);
    }

    @Override
    public KafkaPollArraySupplier<T> criteria(Criteria<? super T> criteria) {
        return super.criteria(criteria);
    }

    KafkaPollArraySupplier<T> setDataTransformer(DataTransformer dataTransformer) {
        checkNotNull(dataTransformer);
        getFromTopics.setTransformer(dataTransformer);
        return this;
    }
}
