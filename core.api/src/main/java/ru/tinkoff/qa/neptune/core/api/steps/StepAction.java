package ru.tinkoff.qa.neptune.core.api.steps;

import ru.tinkoff.qa.neptune.core.api.event.firing.Captor;
import ru.tinkoff.qa.neptune.core.api.steps.context.Context;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Collections.emptyMap;
import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static ru.tinkoff.qa.neptune.core.api.event.firing.StaticEventFiring.*;
import static ru.tinkoff.qa.neptune.core.api.properties.general.events.DoCapturesOf.catchFailureEvent;
import static ru.tinkoff.qa.neptune.core.api.properties.general.events.DoCapturesOf.catchSuccessEvent;

public class StepAction<T> {

    private final String description;
    private final Consumer<T> consumer;
    private final Set<Captor<Object, Object>> successCaptors = new HashSet<>();
    private final Set<Captor<Object, Object>> failureCaptors = new HashSet<>();
    private Map<String, String> parameters = emptyMap();

    StepAction(String description, Consumer<T> consumer) {
        checkArgument(nonNull(consumer), "Consumer should be defined");
        checkArgument(!isBlank(description), "Description should not be empty");
        this.description = description;
        this.consumer = consumer;
    }

    /**
     * This method creates a consumer with some string description. This consumer is
     * supposed to perform some action.
     *
     * @param description string narration of the action
     * @param consumer    which performs the action
     * @param <T>         type of accepted value
     * @return a new consumer with the given string description. Description is returned
     * by the {@link #toString()} method.
     */
    static <T> StepAction<T> action(String description, Consumer<T> consumer) {
        return new StepAction<>(description, consumer);
    }


    public void accept(T t) {
        try {
            fireEventStarting(description, parameters);
            consumer.accept(t);
            if (catchSuccessEvent() && !Context.class.isAssignableFrom(consumer.getClass())) {
                catchValue(t, successCaptors);
            }
        } catch (Throwable thrown) {
            fireThrownException(thrown);
            if (catchFailureEvent() && !StepAction.class.isAssignableFrom(consumer.getClass())) {
                catchValue(t, failureCaptors);
            }
            throw thrown;
        } finally {
            fireEventFinishing();
        }
    }

    @Override
    public String toString() {
        return description;
    }

    StepAction<T> addSuccessCaptors(List<Captor<Object, Object>> successCaptors) {
        this.successCaptors.addAll(successCaptors);
        return this;
    }

    StepAction<T> addFailureCaptors(List<Captor<Object, Object>> failureCaptors) {
        this.failureCaptors.addAll(failureCaptors);
        return this;
    }

    StepAction<T> setParameters(Map<String, String> parameters) {
        this.parameters = parameters;
        return this;
    }
}
