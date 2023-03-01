package ru.tinkoff.qa.neptune.kafka.functions.poll;

import org.apache.kafka.common.serialization.Deserializer;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.StepParameter;
import ru.tinkoff.qa.neptune.core.api.steps.parameters.ParameterValueGetter;
import ru.tinkoff.qa.neptune.core.api.steps.parameters.StepParameterPojo;
import ru.tinkoff.qa.neptune.kafka.KafkaStepContext;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.lang.String.join;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static ru.tinkoff.qa.neptune.core.api.steps.Step.$;
import static ru.tinkoff.qa.neptune.kafka.properties.KafkaDefaultTopicsForPollProperty.DEFAULT_TOPICS_FOR_POLL;

final class PollFunction<K, V, T> implements Function<KafkaStepContext, T>, StepParameterPojo {

    private final Map<String, String> additionalProperties = new LinkedHashMap<>();
    private final PollRunnable<K, V> pollRunnable = new PollRunnable<>();
    private final Deserializer<K> keyDeserializer;
    private final Deserializer<V> valueDeserializer;
    @StepParameter(value = "topics", makeReadableBy = TopicValueGetter.class)
    String[] topics = DEFAULT_TOPICS_FOR_POLL.get();
    private boolean excludeNullValues;
    private boolean excludeNullKeys;
    private Function<KafkaStepContext, T> delegateTo;
    private Runnable toPollWith;
    private String pollWithDescription;

    PollFunction(GetRecords<K, V> getRecords,
                 Deserializer<K> keyDeserializer,
                 Deserializer<V> valueDeserializer) {
        this.keyDeserializer = keyDeserializer;
        this.valueDeserializer = valueDeserializer;
        getRecords.setPollRunnable(pollRunnable);
        pollRunnable.setTopics(topics);
    }

    @Override
    public T apply(KafkaStepContext kafkaStepContext) {
        pollRunnable.setKafkaConsumer(kafkaStepContext,
                additionalProperties,
                keyDeserializer,
                valueDeserializer,
                nonNull(toPollWith))
            .setExcludeNullKeys(excludeNullKeys)
            .setExcludeNullValues(excludeNullValues);

        new Thread(pollRunnable).start();

        try {
            while (!pollRunnable.isPolling() && isNull(pollRunnable.getThrown())) {
            }

            if (nonNull(toPollWith)) {
                $(pollWithDescription, toPollWith);
            }
            return delegateTo.apply(kafkaStepContext);
        } finally {
            pollRunnable.stopPolling();
        }
    }

    PollFunction<K, V, T> setDelegateTo(Function<KafkaStepContext, T> delegateTo) {
        this.delegateTo = delegateTo;
        return this;
    }

    void pollWith(String description, Runnable runnable) {
        checkArgument(isNotBlank(description), "Description of the running action that activates " +
            "the polling should not be a blank or null string");
        checkNotNull(runnable);
        this.pollWithDescription = description;
        this.toPollWith = runnable;
    }

    void topics(String... topics) {
        checkNotNull(topics);
        checkArgument(topics.length > 0, "At least one topic should be defined");
        this.topics = topics;
        pollRunnable.setTopics(this.topics);
    }

    void setProperty(String propertyName, String propertyValue) {
        checkArgument(isNotBlank(propertyName), "Property name should not be empty or null");
        checkArgument(isNotBlank(propertyValue), "Property value should not be empty or null");
        additionalProperties.put(propertyName, propertyValue);
    }

    public Map<String, String> getParameters() {
        var result = StepParameterPojo.super.getParameters();
        result.putAll(additionalProperties);
        return result;
    }


    void excludeNullValues() {
        this.excludeNullValues = true;
    }

    void excludeNullKeys() {
        this.excludeNullKeys = true;
    }

    public static class TopicValueGetter implements ParameterValueGetter<String[]> {
        @Override
        public String getParameterValue(String[] fieldValue) {
            return join(",", fieldValue);
        }
    }
}
