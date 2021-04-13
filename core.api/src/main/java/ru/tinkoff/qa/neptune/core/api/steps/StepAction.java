package ru.tinkoff.qa.neptune.core.api.steps;

import ru.tinkoff.qa.neptune.core.api.event.firing.Captor;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import static java.util.Collections.emptyMap;
import static ru.tinkoff.qa.neptune.core.api.event.firing.StaticEventFiring.*;
import static ru.tinkoff.qa.neptune.core.api.properties.general.events.DoCapturesOf.catchFailureEvent;
import static ru.tinkoff.qa.neptune.core.api.properties.general.events.DoCapturesOf.catchSuccessEvent;

/**
 * Performs a simple action (a function with no resulted value) which is built and supplied by
 * {@link SequentialGetStepSupplier}.
 *
 * @param <T> is a type of input value
 * @param <R>
 */
public final class StepAction<T, R> {

    private final String description;
    private final Set<Captor<Object, Object>> successCaptors = new HashSet<>();
    private final Set<Captor<Object, Object>> failureCaptors = new HashSet<>();
    private Map<String, String> parameters = emptyMap();
    private final SequentialActionSupplier<T, R, ?> supplier;
    private final Function<T, R> getFrom;
    private int maxDepth;

    StepAction(String description, SequentialActionSupplier<T, R, ?> supplier, Function<T, R> getFrom) {
        this.supplier = supplier;
        this.description = description;
        this.getFrom = getFrom;
    }

    public void performAction(T t) {
        R performOn = null;
        try {
            fireEventStarting(description, parameters);
            supplier.onStart(t);
            performOn = getFrom.apply(t);
            supplier.howToPerform(performOn);
            if (catchSuccessEvent()) {
                catchValue(performOn, successCaptors);
            }
        } catch (Throwable thrown) {
            supplier.onFailure(t, thrown);
            fireThrownException(thrown);
            if (catchFailureEvent()) {
                catchValue(performOn, failureCaptors);
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

    StepAction<T, R> addSuccessCaptors(List<Captor<Object, Object>> successCaptors) {
        this.successCaptors.addAll(successCaptors);
        return this;
    }

    StepAction<T, R> addFailureCaptors(List<Captor<Object, Object>> failureCaptors) {
        this.failureCaptors.addAll(failureCaptors);
        return this;
    }

    StepAction<T, R> setParameters(Map<String, String> parameters) {
        this.parameters = parameters;
        return this;
    }

    StepAction<T, R> setMaxDepth(int maxDepth) {
        this.maxDepth = maxDepth;
        return this;
    }
}
