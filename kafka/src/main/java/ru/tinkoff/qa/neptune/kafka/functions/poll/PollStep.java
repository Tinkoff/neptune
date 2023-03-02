package ru.tinkoff.qa.neptune.kafka.functions.poll;

import org.apache.kafka.clients.consumer.ConsumerConfig;
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
     * @throws IllegalArgumentException on attempt to define {@link ConsumerConfig#AUTO_OFFSET_RESET_CONFIG}
     * because it is defined automatically on the starting of the step. So this has no sense.
     * @see <a href="https://kafka.apache.org/documentation/#consumerconfigs">Consumer Configs</a>
     */
    default T setProperty(String property, String value) {
        getPollFunction(this).setProperty(property, value);
        return (T) this;
    }

    /**
     * Defines an action to be performed on the starting of the polling. It is supposed the polled messages
     * is the result of the action. Also, it changes the property {@link ConsumerConfig#AUTO_OFFSET_RESET_CONFIG}
     * of current consumer to 'latest'. Without {@code pollLatestWith(String, Runnable)} the value 'earliest' is used.
     *
     * @param description description of performed action
     * @param action      is the action to be performed
     * @return self-reference
     */
    default T pollLatestWith(String description, Runnable action) {
        getPollFunction(this).pollWith(description, action);
        return (T) this;
    }
}
