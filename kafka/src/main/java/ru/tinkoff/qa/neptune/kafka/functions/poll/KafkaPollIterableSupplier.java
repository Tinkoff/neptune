package ru.tinkoff.qa.neptune.kafka.functions.poll;

import com.fasterxml.jackson.core.type.TypeReference;
import ru.tinkoff.qa.neptune.core.api.data.format.DataTransformer;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotations.CaptureOnFailure;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotations.CaptureOnSuccess;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotations.MaxDepthOfReporting;
import ru.tinkoff.qa.neptune.core.api.steps.Criteria;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;
import ru.tinkoff.qa.neptune.core.api.steps.parameters.ParameterValueGetter;
import ru.tinkoff.qa.neptune.kafka.KafkaStepContext;
import ru.tinkoff.qa.neptune.kafka.captors.MessageCaptor;
import ru.tinkoff.qa.neptune.kafka.captors.MessagesCaptor;

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
public class KafkaPollIterableSupplier<T> extends SequentialGetStepSupplier
        .GetIterableStepSupplier<KafkaStepContext, List<T>, T, KafkaPollIterableSupplier<T>> {

    final GetFromTopics<?> getFromTopics;

    @CaptureOnSuccess(by = MessageCaptor.class)
    String message;

    @CaptureOnSuccess(by = MessagesCaptor.class)
    @CaptureOnFailure(by = MessagesCaptor.class)
    List<String> messages;

    protected <M> KafkaPollIterableSupplier(GetFromTopics<M> getFromTopics, Function<M, T> function) {
        super(getFromTopics.andThen(list -> list.stream().map(function).collect(toList())));
        this.getFromTopics = getFromTopics;
    }

    @Description("{description}")
    public static <M, T> KafkaPollIterableSupplier<T> kafkaIterable(
            @DescriptionFragment(value = "description",
                    makeReadableBy = ParameterValueGetter.TranslatedDescriptionParameterValueGetter.class
            ) String description,
            List<String> topics,
            Class<M> cls,
            Function<M, T> toGet) {
        checkArgument(isNotBlank(description), "Description should be defined");
        return new KafkaPollIterableSupplier<>(new GetFromTopics<>(topics, cls), toGet);
    }

    @Description("{description}")
    public static <M, T> KafkaPollIterableSupplier<T> kafkaIterable(
            @DescriptionFragment(value = "description",
                    makeReadableBy = ParameterValueGetter.TranslatedDescriptionParameterValueGetter.class
            ) String description,
            List<String> topics,
            TypeReference<M> typeT,
            Function<M, T> toGet) {
        checkArgument(isNotBlank(description), "Description should be defined");
        return new KafkaPollIterableSupplier<>(new GetFromTopics<>(topics, typeT), toGet);
    }

    public static <T> KafkaPollIterableSupplier<T> kafkaIterable(
            String description,
            List<String> topics,
            TypeReference<T> typeT) {
        checkArgument(isNotBlank(description), "Description should be defined");
        return kafkaIterable(description, topics, typeT, ts -> ts);
    }

    public static <T> KafkaPollIterableSupplier<T> kafkaIterable(
            String description,
            List<String> topics,
            Class<T> cls) {
        checkArgument(isNotBlank(description), "Description should be defined");
        return kafkaIterable(description, topics, cls, ts -> ts);
    }

    @Override
    public KafkaPollIterableSupplier<T> timeOut(Duration timeOut) {
        return super.timeOut(timeOut);
    }

    @Override
    public KafkaPollIterableSupplier<T> criteria(String description, Predicate<? super T> predicate) {
        return super.criteria(description, predicate);
    }

    @Override
    public KafkaPollIterableSupplier<T> criteria(Criteria<? super T> criteria) {
        return super.criteria(criteria);
    }

    KafkaPollIterableSupplier<T> setDataTransformer(DataTransformer dataTransformer) {
        checkNotNull(dataTransformer);
        getFromTopics.setTransformer(dataTransformer);
        return this;
    }

    @Override
    protected void onSuccess(List<T> tList) {
        var ms = getFromTopics.getMessages();
        if (tList != null && tList.size() > 0) {
            message = ms.getLast();
        } else {
            messages = ms;
        }
    }

    @Override
    protected void onFailure(KafkaStepContext m, Throwable throwable) {
        messages = getFromTopics.getMessages();
    }
}
