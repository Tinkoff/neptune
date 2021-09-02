package ru.tinkoff.qa.neptune.core.api.steps;

import ru.tinkoff.qa.neptune.core.api.event.firing.Captor;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotations.CaptureOnFailure;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotations.CaptureOnSuccess;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

import static java.util.Collections.emptyMap;
import static java.util.Optional.ofNullable;
import static ru.tinkoff.qa.neptune.core.api.event.firing.StaticEventFiring.*;
import static ru.tinkoff.qa.neptune.core.api.event.firing.annotations.MaxDepthOfReporting.MaxDepthOfReportingReader.getCurrentDepth;
import static ru.tinkoff.qa.neptune.core.api.properties.general.events.DoCapturesOf.catchFailureEvent;
import static ru.tinkoff.qa.neptune.core.api.properties.general.events.DoCapturesOf.catchSuccessEvent;
import static ru.tinkoff.qa.neptune.core.api.properties.general.events.ToLimitReportDepth.TO_LIMIT_REPORT_DEPTH_PROPERTY;

/**
 * Performs a simple action (a function with no resulted value) which is built and supplied by
 * {@link SequentialGetStepSupplier}.
 *
 * @param <T> is a type of input value
 * @param <R>
 */
final class ActionImpl<T, R> implements Action<T> {

    private final String description;
    private final Set<Captor<Object, Object>> successCaptors = new HashSet<>();
    private final Set<Captor<Object, Object>> failureCaptors = new HashSet<>();
    private Map<String, String> parameters = emptyMap();
    private final SequentialActionSupplier<T, R, ?> supplier;
    private final Function<T, R> getFrom;
    private int maxDepth;
    private Supplier<Map<String, String>> additionalParams;

    private final Set<FieldValueCaptureMaker<CaptureOnSuccess>> onSuccessAdditional = new HashSet<>();
    private final Set<FieldValueCaptureMaker<CaptureOnFailure>> onFailureAdditional = new HashSet<>();

    ActionImpl(String description, SequentialActionSupplier<T, R, ?> supplier, Function<T, R> getFrom) {
        this.supplier = supplier;
        this.description = description;
        this.getFrom = getFrom;
    }

    private boolean toReport() {
        return getCurrentDepth() <= maxDepth || !TO_LIMIT_REPORT_DEPTH_PROPERTY.get();
    }

    public void performAction(T t) {
        R performOn = null;
        var toReport = toReport();
        try {
            if (toReport) {
                fireEventStarting(description, parameters);
            }
            supplier.onStart(t);
            performOn = getFrom.apply(t);
            supplier.howToPerform(performOn);

            if (toReport) {
                ofNullable(additionalParams).ifPresent(ap -> fireAdditionalParameters(ap.get()));
            }

            if (catchSuccessEvent() && toReport) {
                catchValue(performOn, successCaptors);
                onSuccessAdditional.forEach(FieldValueCaptureMaker::makeCaptures);
            }
        } catch (Throwable thrown) {

            if (toReport) {
                ofNullable(additionalParams).ifPresent(ap -> fireAdditionalParameters(ap.get()));
            }

            supplier.onFailure(t, thrown);
            fireThrownException(thrown);
            if (catchFailureEvent() && toReport) {
                ofNullable(performOn).ifPresent(o -> catchValue(o, failureCaptors));
                catchValue(t, failureCaptors);
                onFailureAdditional.forEach(FieldValueCaptureMaker::makeCaptures);
            }
            throw thrown;
        } finally {
            if (toReport) {
                fireEventFinishing();
            }
        }
    }

    @Override
    public String toString() {
        return description;
    }

    ActionImpl<T, R> addSuccessCaptors(List<Captor<Object, Object>> successCaptors) {
        this.successCaptors.addAll(successCaptors);
        return this;
    }

    ActionImpl<T, R> addFailureCaptors(List<Captor<Object, Object>> failureCaptors) {
        this.failureCaptors.addAll(failureCaptors);
        return this;
    }

    ActionImpl<T, R> setParameters(Map<String, String> parameters) {
        this.parameters = parameters;
        return this;
    }

    ActionImpl<T, R> setMaxDepth(int maxDepth) {
        this.maxDepth = maxDepth;
        return this;
    }

    ActionImpl<T, R> setAdditionalParams(Supplier<Map<String, String>> additionalParams) {
        this.additionalParams = additionalParams;
        return this;
    }

    ActionImpl<T, R> addOnSuccessAdditional(Collection<FieldValueCaptureMaker<CaptureOnSuccess>> additional) {
        this.onSuccessAdditional.addAll(additional);
        return this;
    }

    ActionImpl<T, R> addOnFailureAdditional(Collection<FieldValueCaptureMaker<CaptureOnFailure>> additional) {
        this.onFailureAdditional.addAll(additional);
        return this;
    }
}
