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

import java.lang.reflect.Array;
import java.lang.reflect.ParameterizedType;
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
import static ru.tinkoff.qa.neptune.kafka.properties.KafkaDefaultDataTransformer.KAFKA_DEFAULT_DATA_TRANSFORMER;
import static ru.tinkoff.qa.neptune.kafka.properties.KafkaDefaultTopicsForPollSupplier.DEFAULT_TOPICS_FOR_POLL;


@SequentialGetStepSupplier.DefineGetImperativeParameterName("Poll:")
@SequentialGetStepSupplier.DefineTimeOutParameterName("Time of the waiting")
@SequentialGetStepSupplier.DefineCriteriaParameterName("Criteria for every item of resulted array")
@MaxDepthOfReporting(0)
@SuppressWarnings("unchecked")
public class KafkaPollArraySupplier<T> extends SequentialGetStepSupplier
        .GetArrayStepSupplier<KafkaStepContext, T, KafkaPollArraySupplier<T>> {

    final GetFromTopics<?> getFromTopics;

    @CaptureOnSuccess(by = MessagesCaptor.class)
    List<String> successMessages = new LinkedList<>();

    @CaptureOnSuccess(by = AllMessagesCaptor.class)
    @CaptureOnFailure(by = AllMessagesCaptor.class)
    List<String> messages;

    private DataTransformer transformer;

    protected <M> KafkaPollArraySupplier(GetFromTopics<M> getFromTopics, Function<M, T> originalFunction, Class<T> componentClass) {
        super(getFromTopics.andThen(list -> {
            var listT = list.stream().map(originalFunction).collect(toList());
            T[] ts = (T[]) Array.newInstance(componentClass, listT.size());

            for (int i = 0; i < listT.size(); i++) {
                ts[i] = listT.get(i);
            }
            return ts;
        }));
        this.getFromTopics = getFromTopics;
    }

    @Description("{description}")
    public static <M, T> KafkaPollArraySupplier<T> kafkaArray(
            @DescriptionFragment(value = "description",
                    makeReadableBy = ParameterValueGetter.TranslatedDescriptionParameterValueGetter.class
            ) String description,
            Class<M> classT,
            Class<T> componentClass,
            Function<M, T> toGet,
            String... topics) {
        checkArgument(isNotBlank(description), "Description should be defined");
        if (topics.length == 0) {
            return new KafkaPollArraySupplier<>(new GetFromTopics<>(classT, DEFAULT_TOPICS_FOR_POLL.get()), toGet, componentClass);
        } else {
            return new KafkaPollArraySupplier<>(new GetFromTopics<>(classT, topics), toGet, componentClass);
        }
    }

    @Description("{description}")
    public static <M, T> KafkaPollArraySupplier<T> kafkaArray(
            @DescriptionFragment(value = "description",
                    makeReadableBy = ParameterValueGetter.TranslatedDescriptionParameterValueGetter.class
            ) String description,
            TypeReference<M> typeT,
            Class<T> componentClass,
            Function<M, T> toGet,
            String... topics) {
        checkArgument(isNotBlank(description), "Description should be defined");
        if (topics.length == 0) {
            return new KafkaPollArraySupplier<>(new GetFromTopics<>(typeT, DEFAULT_TOPICS_FOR_POLL.get()), toGet, componentClass);
        } else {
            return new KafkaPollArraySupplier<>(new GetFromTopics<>(typeT, topics), toGet, componentClass);
        }
    }

    public static <T> KafkaPollArraySupplier<T> kafkaArray(
            String description,
            Class<T> classT,
            String... topics) {
        checkArgument(isNotBlank(description), "Description should be defined");
        return kafkaArray(description, classT, classT, ts -> ts, topics);
    }

    public static <T> KafkaPollArraySupplier<T> kafkaArray(
            String description,
            TypeReference<T> typeT,
            String... topics) {
        checkArgument(isNotBlank(description), "Description should be defined");
        var clazz = (Class) (typeT.getType() instanceof ParameterizedType ? ((ParameterizedType) typeT.getType()).getRawType() : typeT.getType());
        return kafkaArray(description, typeT, clazz, ts -> ts, topics);
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

    public KafkaPollArraySupplier<T> withDataTransformer(DataTransformer dataTransformer) {
        this.transformer = dataTransformer;
        return this;
    }

    @Override
    protected void onSuccess(T[] t) {
        var mss = getFromTopics.getSuccessMessages();

        if (t != null && t.length > 0) {
            for (T item : t) {
                successMessages.add(mss.get(item));
            }
        } else {
            messages = getFromTopics.getMessages();
        }
    }

    @Override
    protected void onFailure(KafkaStepContext m, Throwable throwable) {
        messages = getFromTopics.getMessages();
    }
}
