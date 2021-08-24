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
import ru.tinkoff.qa.neptune.kafka.captors.MessagesCaptor;

import java.time.Duration;
import java.util.LinkedList;
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
public class KafkaPollIterableSupplier<T> extends SequentialGetStepSupplier
        .GetIterableStepSupplier<KafkaStepContext, List<T>, T, KafkaPollIterableSupplier<T>> {

    final GetFromTopics<?> getFromTopics;

    @CaptureOnSuccess(by = MessagesCaptor.class)
    List<String> successMessages = new LinkedList<>();

    @CaptureOnSuccess(by = AllMessagesCaptor.class)
    @CaptureOnFailure(by = AllMessagesCaptor.class)
    List<String> messages;

    private DataTransformer transformer;

    protected <M> KafkaPollIterableSupplier(GetFromTopics<M> getFromTopics, Function<M, T> function) {
        super(getFromTopics.andThen(list -> list.stream().map(function).collect(toList())));
        this.getFromTopics = getFromTopics;
    }

    @Description("{description}")
    public static <M, T> KafkaPollIterableSupplier<T> kafkaIterable(
            @DescriptionFragment(value = "description",
                    makeReadableBy = ParameterValueGetter.TranslatedDescriptionParameterValueGetter.class
            ) String description,
            Class<M> cls,
            Function<M, T> toGet,
            String... topics) {
        checkArgument(isNotBlank(description), "Description should be defined");
        if (topics.length == 0) {
            return new KafkaPollIterableSupplier<>(new GetFromTopics<>(cls, DEFAULT_TOPICS_FOR_POLL.get()), toGet);
        } else {
            return new KafkaPollIterableSupplier<>(new GetFromTopics<>(cls, topics), toGet);
        }
    }

    @Description("{description}")
    public static <M, T> KafkaPollIterableSupplier<T> kafkaIterable(
            @DescriptionFragment(value = "description",
                    makeReadableBy = ParameterValueGetter.TranslatedDescriptionParameterValueGetter.class
            ) String description,
            TypeReference<M> typeT,
            Function<M, T> toGet,
            String... topics) {
        checkArgument(isNotBlank(description), "Description should be defined");
        if (topics.length == 0) {
            return new KafkaPollIterableSupplier<>(new GetFromTopics<>(typeT, DEFAULT_TOPICS_FOR_POLL.get()), toGet);
        } else {
            return new KafkaPollIterableSupplier<>(new GetFromTopics<>(typeT, topics), toGet);
        }
    }

    public static <T> KafkaPollIterableSupplier<T> kafkaIterable(
            String description,
            TypeReference<T> typeT,
            String... topics) {
        return kafkaIterable(description, typeT, ts -> ts, topics);
    }

    public static <T> KafkaPollIterableSupplier<T> kafkaIterable(
            String description,
            Class<T> cls,
            String... topics) {
        return kafkaIterable(description, cls, ts -> ts, topics);
    }

    @Description("String messages")
    public static StringMessages kafkaRawMessagesIterable(String... topics) {
        return new StringMessages(getStringResult(topics));
    }

    public static StringMessages kafkaRawMessagesIterable() {
        return kafkaRawMessagesIterable(DEFAULT_TOPICS_FOR_POLL.get());
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

    public KafkaPollIterableSupplier<T> withDataTransformer(DataTransformer dataTransformer) {
        this.transformer = dataTransformer;
        return this;
    }

    @Override
    protected void onSuccess(List<T> tList) {
        var mss = getFromTopics.getSuccessMessages();

        if (tList != null && tList.size() > 0) {
            tList.forEach(item -> successMessages.add(mss.get(item)));
        } else {
            messages = getFromTopics.getMessages();
        }
    }

    @Override
    protected void onFailure(KafkaStepContext m, Throwable throwable) {
        messages = getFromTopics.getMessages();
    }

    public static class StringMessages extends KafkaPollIterableSupplier<String> {
        public StringMessages(GetFromTopics<String> getFromTopics) {
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
