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
import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.StringUtils.isNotBlank;


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
            List<String> topics,
            Class<M> classT,
            Class<T> componentClass,
            Function<M, T> toGet) {
        checkArgument(isNotBlank(description), "Description should be defined");
        return new KafkaPollArraySupplier<>(new GetFromTopics<>(topics, classT), toGet, componentClass);
    }

    @Description("{description}")
    public static <M, T> KafkaPollArraySupplier<T> kafkaArray(
            @DescriptionFragment(value = "description",
                    makeReadableBy = ParameterValueGetter.TranslatedDescriptionParameterValueGetter.class
            ) String description,
            List<String> topics,
            TypeReference<M> typeT,
            Class<T> componentClass,
            Function<M, T> toGet) {
        checkArgument(isNotBlank(description), "Description should be defined");
        return new KafkaPollArraySupplier<>(new GetFromTopics<>(topics, typeT), toGet, componentClass);
    }

    public static <T> KafkaPollArraySupplier<T> kafkaArray(
            String description,
            List<String> topics,
            Class<T> classT) {
        checkArgument(isNotBlank(description), "Description should be defined");
        return kafkaArray(description, topics, classT, classT, ts -> ts);
    }

    public static <T> KafkaPollArraySupplier<T> kafkaArray(
            String description,
            List<String> topics,
            TypeReference<T> typeT) {
        checkArgument(isNotBlank(description), "Description should be defined");
        var clazz = (Class) (typeT.getType() instanceof ParameterizedType ? ((ParameterizedType) typeT.getType()).getRawType() : typeT.getType());
        return kafkaArray(description, topics, typeT, clazz, ts -> ts);
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
