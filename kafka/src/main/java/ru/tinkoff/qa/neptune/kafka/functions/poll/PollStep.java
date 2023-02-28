package ru.tinkoff.qa.neptune.kafka.functions.poll;

import ru.tinkoff.qa.neptune.kafka.properties.KafkaDefaultTopicsForPollProperty;

import static java.util.Arrays.stream;

@SuppressWarnings("unchecked")
interface PollStep<T extends PollStep<T>> {

    private static PollFunction<?, ?, ?> getPollFunction(PollStep<?> pollStep) {
        var f = stream(pollStep.getClass().getDeclaredFields())
            .filter(field -> field.getType().equals(PollFunction.class))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("No field of type " + PollFunction.class.getName() + " was found"));

        f.setAccessible(true);
        try {
            return (PollFunction<?, ?, ?>) f.get(pollStep);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * Defines topics to subscribe
     * <p></p>
     * If there is no topic defined then value of the property
     * {@link KafkaDefaultTopicsForPollProperty#DEFAULT_TOPICS_FOR_POLL} is used.
     *
     * @param topics topics to subscribe
     * @return self-reference
     */
    default T fromTopics(String... topics) {
        getPollFunction(this).topics(topics);
        return (T) this;
    }

    /**
     * Defines consumer property value
     *
     * @param property a property name
     * @param value    a property value
     * @return self-reference
     * @see <a href="https://kafka.apache.org/documentation/#consumerconfigs">Consumer Configs</a>
     */
    default T setProperty(String property, String value) {
        getPollFunction(this).setProperty(property, value);
        return (T) this;
    }

    /**
     * Defines an action. When it is performed then the polling should be started.
     *
     * @param description description of performed action
     * @param action      is the action to be performed
     * @return self-reference
     */
    default T pollWith(String description, Runnable action) {
        getPollFunction(this).pollWith(description, action);
        return (T) this;
    }
}
