package ru.tinkoff.qa.neptune.kafka.functions.poll;

import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import ru.tinkoff.qa.neptune.core.api.data.format.DataTransformer;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotations.CaptureOnFailure;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotations.CaptureOnSuccess;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotations.MaxDepthOfReporting;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;
import ru.tinkoff.qa.neptune.core.api.steps.parameters.ParameterValueGetter;
import ru.tinkoff.qa.neptune.kafka.KafkaStepContext;
import ru.tinkoff.qa.neptune.kafka.captors.AllMessagesCaptor;
import ru.tinkoff.qa.neptune.kafka.captors.MessagesCaptor;
import ru.tinkoff.qa.neptune.kafka.properties.KafkaDefaultTopicsForPollProperty;

import java.lang.reflect.Array;
import java.lang.reflect.ParameterizedType;
import java.time.Duration;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;
import static java.util.Objects.nonNull;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.ArrayUtils.add;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static ru.tinkoff.qa.neptune.kafka.GetConsumer.getConsumer;
import static ru.tinkoff.qa.neptune.kafka.functions.poll.GetFromTopics.getStringResult;
import static ru.tinkoff.qa.neptune.kafka.properties.DefaultDataTransformers.KAFKA_DEFAULT_DATA_TRANSFORMER;


@SequentialGetStepSupplier.DefineGetImperativeParameterName("Poll:")
@SequentialGetStepSupplier.DefineTimeOutParameterName("Time of the waiting")
@SequentialGetStepSupplier.DefineCriteriaParameterName("Criteria for every item of resulted array")
@MaxDepthOfReporting(0)
@SuppressWarnings({"unchecked", "rawtypes"})
public abstract class KafkaPollArraySupplier<T, S extends KafkaPollArraySupplier<T, S>> extends SequentialGetStepSupplier
    .GetArrayChainedStepSupplier<KafkaStepContext, T, KafkaConsumer<String, String>, S> {

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
            T[] ts = (T[]) Array.newInstance(componentClass, 0);

            for (var t : listT) {
                ts = add(ts, t);
            }

            return ts;
        }));
        this.getFromTopics = getFromTopics;
        from(getConsumer());
    }

    /**
     * Creates a step that returns array of values which are calculated by data of read messages.
     * <p></p>
     * It is not necessary to define {@code topics}. If there is no topic defined then value of the property
     * {@link KafkaDefaultTopicsForPollProperty#DEFAULT_TOPICS_FOR_POLL} is used.
     *
     * @param description    is description of value to get
     * @param classT         is a class of a value to deserialize a message from topics
     * @param componentClass is a class of array item
     * @param toGet          describes how to get desired value
     * @param topics         are topics to get messages from
     * @param <M>            is a type of deserialized message
     * @param <T>            is a type of item of array
     * @return an instance of {@link KafkaPollArraySupplier.Mapped}
     */
    @Description("{description}")
    public static <M, T> Mapped<T> kafkaArray(
            @DescriptionFragment(value = "description",
                    makeReadableBy = ParameterValueGetter.TranslatedDescriptionParameterValueGetter.class
            ) String description,
            Class<M> classT,
            Class<T> componentClass,
            Function<M, T> toGet,
            String... topics) {
        checkArgument(isNotBlank(description), "Description should be defined");
        return new KafkaPollArraySupplier.Mapped<>(new GetFromTopics<>(classT, topics), toGet, componentClass);
    }

    /**
     * Creates a step that returns array of values which are calculated by data of read messages.
     * <p></p>
     * It is not necessary to define {@code topics}. If there is no topic defined then value of the property
     * {@link KafkaDefaultTopicsForPollProperty#DEFAULT_TOPICS_FOR_POLL} is used.
     *
     * @param description    is description of value to get
     * @param typeT          is a reference to type of value to deserialize message
     * @param componentClass is a class of array item
     * @param toGet          describes how to get desired value
     * @param topics         are topics to get messages from
     * @param <M>            is a type of deserialized message
     * @param <T>            is a type of item of array
     * @return an instance of {@link KafkaPollArraySupplier.Mapped}
     */
    @Description("{description}")
    public static <M, T> Mapped<T> kafkaArray(
            @DescriptionFragment(value = "description",
                    makeReadableBy = ParameterValueGetter.TranslatedDescriptionParameterValueGetter.class
            ) String description,
            TypeReference<M> typeT,
            Class<T> componentClass,
            Function<M, T> toGet,
            String... topics) {
        checkArgument(isNotBlank(description), "Description should be defined");
        return new KafkaPollArraySupplier.Mapped<>(new GetFromTopics<>(typeT, topics), toGet, componentClass);
    }

    /**
     * Creates a step that returns array of deserialized messages.
     * <p></p>
     * It is not necessary to define {@code topics}. If there is no topic defined then value of the property
     * {@link KafkaDefaultTopicsForPollProperty#DEFAULT_TOPICS_FOR_POLL} is used.
     *
     * @param description is description of value to get
     * @param classT      is a class of a value to deserialize a message from topics
     * @param topics      are topics to get messages from
     * @param <T>         is a type of deserialized message
     * @return an instance of {@link KafkaPollArraySupplier.Mapped}
     */
    public static <T> Mapped<T> kafkaArray(
            String description,
            Class<T> classT,
            String... topics) {
        return kafkaArray(description, classT, classT, ts -> ts, topics);
    }

    /**
     * Creates a step that returns array of deserialized messages.
     * <p></p>
     * It is not necessary to define {@code topics}. If there is no topic defined then value of the property
     * {@link KafkaDefaultTopicsForPollProperty#DEFAULT_TOPICS_FOR_POLL} is used.
     *
     * @param description is description of value to get
     * @param typeT       is a reference to type of value to deserialize message
     * @param topics      are topics to get messages from
     * @param <T>         is a type of deserialized message
     * @return an instance of {@link KafkaPollArraySupplier.Mapped}
     */
    public static <T> Mapped<T> kafkaArray(
            String description,
            TypeReference<T> typeT,
            String... topics) {
        var clazz = (Class) (typeT.getType() instanceof ParameterizedType ? ((ParameterizedType) typeT.getType()).getRawType() : typeT.getType());
        return kafkaArray(description, typeT, clazz, ts -> ts, topics);
    }

    /**
     * Creates a step that returns array of string contents of messages.
     * <p></p>
     * It is not necessary to define {@code topics}. If there is no topic defined then value of the property
     * {@link KafkaDefaultTopicsForPollProperty#DEFAULT_TOPICS_FOR_POLL} is used.
     *
     * @param topics      are topics to get messages from
     * @return an instance of {@link KafkaPollArraySupplier.StringMessages}
     */
    @Description("String messages")
    public static StringMessages kafkaArrayOfRawMessages(String... topics) {
        return new StringMessages(getStringResult(topics));
    }

    @Override
    public S timeOut(Duration timeOut) {
        return super.timeOut(timeOut);
    }

    @Override
    protected void onStart(KafkaConsumer<String, String> consumer) {
        var transformer = ofNullable(this.transformer)
            .orElseGet(KAFKA_DEFAULT_DATA_TRANSFORMER);
        checkState(nonNull(transformer), "Data transformer is not defined. Please invoke "
            + "the '#withDataTransformer(DataTransformer)' method or define '"
            + KAFKA_DEFAULT_DATA_TRANSFORMER.getName()
            + "' property/env variable");
        getFromTopics.setTransformer(transformer);
    }

    S withDataTransformer(DataTransformer dataTransformer) {
        this.transformer = dataTransformer;
        return (S) this;
    }

    @Override
    protected void onSuccess(T[] t) {
        var mss = getFromTopics.getSuccessMessages();

        if (t != null && t.length > 0) {
            for (T item : t) {
                successMessages.add(mss.get(item).toString());
            }
        }
        messages = getFromTopics.getMessages();
    }

    @Override
    protected void onFailure(KafkaConsumer<String, String> m, Throwable throwable) {
        messages = getFromTopics.getMessages();
    }

    public final static class Mapped<T> extends KafkaPollArraySupplier<T, Mapped<T>> {

        private <M> Mapped(GetFromTopics<M> getFromTopics, Function<M, T> originalFunction, Class<T> componentClass) {
            super(getFromTopics, originalFunction, componentClass);
        }

        public Mapped<T> withDataTransformer(DataTransformer transformer) {
            return super.withDataTransformer(transformer);
        }
    }

    public final static class StringMessages extends KafkaPollArraySupplier<String, StringMessages> {

        private StringMessages(GetFromTopics<String> getFromTopics) {
            super(getFromTopics, s -> s, String.class);
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
