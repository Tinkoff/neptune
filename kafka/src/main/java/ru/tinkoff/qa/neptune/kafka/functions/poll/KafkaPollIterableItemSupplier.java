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
import ru.tinkoff.qa.neptune.kafka.captors.AllMessagesCaptor;
import ru.tinkoff.qa.neptune.kafka.captors.MessageCaptor;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;
import static java.util.Objects.nonNull;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static ru.tinkoff.qa.neptune.kafka.functions.poll.GetFromTopics.getStringResult;
import static ru.tinkoff.qa.neptune.kafka.properties.KafkaDefaultDataTransformer.KAFKA_DEFAULT_DATA_TRANSFORMER;
import static ru.tinkoff.qa.neptune.kafka.properties.KafkaDefaultTopicsForPollSupplier.DEFAULT_TOPICS_FOR_POLL;

@SequentialGetStepSupplier.DefineGetImperativeParameterName("Poll:")
@SequentialGetStepSupplier.DefineTimeOutParameterName("Time of the waiting")
@SequentialGetStepSupplier.DefineCriteriaParameterName("Object criteria")
@MaxDepthOfReporting(0)
public abstract class KafkaPollIterableItemSupplier<T> extends SequentialGetStepSupplier
        .GetObjectFromIterableStepSupplier<KafkaStepContext, T, KafkaPollIterableItemSupplier<T>> {

    final GetFromTopics<?> getFromTopics;

    @CaptureOnSuccess(by = MessageCaptor.class)
    String message;

    @CaptureOnSuccess(by = AllMessagesCaptor.class)
    @CaptureOnFailure(by = AllMessagesCaptor.class)
    List<String> messages;

    private DataTransformer transformer;

    protected <S> KafkaPollIterableItemSupplier(GetFromTopics<S> getFromTopics, Function<S, T> originalFunction) {
        super(getFromTopics.andThen(list -> list.stream().map(originalFunction).collect(toList())));
        this.getFromTopics = getFromTopics;
    }

    @Description("{description}")
    public static <M, T> Mapped<T> kafkaIterableItem(
            @DescriptionFragment(value = "description",
                    makeReadableBy = ParameterValueGetter.TranslatedDescriptionParameterValueGetter.class
            ) String description,
            Class<M> cls,
            Function<M, T> toGet,
            String... topics) {
        checkArgument(isNotBlank(description), "Description should be defined");
        if (topics.length == 0) {
            return new KafkaPollIterableItemSupplier.Mapped<>(new GetFromTopics<>(cls, DEFAULT_TOPICS_FOR_POLL.get()), toGet);
        } else {
            return new KafkaPollIterableItemSupplier.Mapped<>(new GetFromTopics<>(cls, topics), toGet);
        }
    }

    @Description("{description}")
    public static <M, T> Mapped<T> kafkaIterableItem(
            @DescriptionFragment(value = "description",
                    makeReadableBy = ParameterValueGetter.TranslatedDescriptionParameterValueGetter.class
            ) String description,
            TypeReference<M> typeT,
            Function<M, T> toGet,
            String... topics) {
        checkArgument(isNotBlank(description), "Description should be defined");
        if (topics.length == 0) {
            return new KafkaPollIterableItemSupplier.Mapped<>(new GetFromTopics<>(typeT, DEFAULT_TOPICS_FOR_POLL.get()), toGet);
        } else {
            return new KafkaPollIterableItemSupplier.Mapped<>(new GetFromTopics<>(typeT, topics), toGet);
        }
    }

    public static <M> Mapped<M> kafkaIterableItem(
            String description,
            Class<M> cls,
            String... topics) {
        return kafkaIterableItem(description, cls, t -> t, topics);
    }

    public static <M> Mapped<M> kafkaIterableItem(
            String description,
            TypeReference<M> typeT,
            String... topics) {
        return kafkaIterableItem(description, typeT, t -> t, topics);
    }

    @Description("String message")
    public static StringMessage kafkaRawMessageIterableItem(String... topics) {
        return new StringMessage(getStringResult(topics));
    }

    public static StringMessage kafkaRawMessageIterableItem() {
        return kafkaRawMessageIterableItem(DEFAULT_TOPICS_FOR_POLL.get());
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

    @Override
    protected void onStart(KafkaStepContext kafkaStepContext) {
        var transformer = ofNullable(this.transformer)
                .orElseGet(KAFKA_DEFAULT_DATA_TRANSFORMER);
        checkState(nonNull(transformer), "Data transformer is not defined. Please invoke "
                + "the '#withDataTransformer(DataTransformer)' method or define '"
                + KAFKA_DEFAULT_DATA_TRANSFORMER.getName()
                + "' property/env variable");
        getFromTopics.setTransformer(transformer);
    }

    KafkaPollIterableItemSupplier<T> withDataTransformer(DataTransformer dataTransformer) {
        this.transformer = dataTransformer;
        return this;
    }

    @Override
    protected void onSuccess(T t) {
        var ms = getFromTopics.getSuccessMessages();
        if (t != null) {
            message = ms.get(t);
        } else {
            messages = new ArrayList<>(ms.values());
        }
    }

    @Override
    protected void onFailure(KafkaStepContext m, Throwable throwable) {
        messages = getFromTopics.getMessages();
    }


    public static class Mapped<T> extends KafkaPollIterableItemSupplier<T> {
        public <S> Mapped(GetFromTopics<S> getFromTopics, Function<S, T> originalFunction) {
            super(getFromTopics, originalFunction);
        }

        public KafkaPollIterableItemSupplier<T> withDataTransformer(DataTransformer transformer) {
            return super.withDataTransformer(transformer);
        }

    }

    public static class StringMessage extends KafkaPollIterableItemSupplier<String> {
        private StringMessage(GetFromTopics<String> getFromTopics) {
            super(getFromTopics, s -> s);
            withDataTransformer(new DataTransformer() {
                @Override
                public <T> T deserialize(String string, Class<T> cls) {
                    return (T) string;
                }

                @Override
                public <T> T deserialize(String string, TypeReference<T> type) {
                    return (T) string;
                }

                @Override
                public String serialize(Object obj) {
                    return obj.toString();
                }
            });
        }
    }
}
