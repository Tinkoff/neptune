package ru.tinkoff.qa.neptune.core.api.steps;

import ru.tinkoff.qa.neptune.core.api.event.firing.Captor;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotations.CaptureOnFailure;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotations.CaptureOnSuccess;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.Collections.emptyMap;
import static java.util.Objects.nonNull;
import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static ru.tinkoff.qa.neptune.core.api.event.firing.StaticEventFiring.*;
import static ru.tinkoff.qa.neptune.core.api.event.firing.annotations.MaxDepthOfReporting.MaxDepthOfReportingReader.getCurrentDepth;
import static ru.tinkoff.qa.neptune.core.api.properties.general.events.DoCapturesOf.catchFailureEvent;
import static ru.tinkoff.qa.neptune.core.api.properties.general.events.DoCapturesOf.catchSuccessEvent;
import static ru.tinkoff.qa.neptune.core.api.properties.general.events.ToLimitReportDepth.TO_LIMIT_REPORT_DEPTH_PROPERTY;
import static ru.tinkoff.qa.neptune.core.api.utils.IsLoggableUtil.isLoggable;

@SuppressWarnings("unchecked")
final class Get<T, R> implements Function<T, R> {

    private final Set<Class<? extends Throwable>> ignored = new HashSet<>();

    private final Set<Captor<Object, Object>> successCaptors = new HashSet<>();
    private final Set<Captor<Object, Object>> failureCaptors = new HashSet<>();

    String description;
    Function<T, R> function;
    private boolean toReport = true;
    private Map<String, String> parameters = emptyMap();
    private String resultDescription;
    private int maxDepth;
    private Get<?, ?> previous;
    private Supplier<Map<String, String>> additionalParams;

    private final Set<FieldValueCaptureMaker<CaptureOnSuccess>> onSuccessAdditional = new HashSet<>();
    private final Set<FieldValueCaptureMaker<CaptureOnFailure>> onFailureAdditional = new HashSet<>();

    Get(String description, Function<T, R> function) {
        checkArgument(nonNull(function), "Function should be defined");
        checkArgument(!isBlank(description), "Description should not be empty string or null value");
        this.description = description;
        this.function = function;
    }

    private static <R> void fireReturnedValueIfNecessary(String resultDescription, R r) {
        if (isLoggable(r)) {
            fireReturnedValue(resultDescription, r);
        }
    }

    private boolean shouldBeThrowableIgnored(Throwable toBeIgnored) {
        for (var throwableClass : ignored) {
            if (throwableClass.isAssignableFrom(toBeIgnored.getClass())) {
                return true;
            }
        }
        return false;
    }

    private boolean toReport() {
        return toReport && (getCurrentDepth() <= maxDepth || !TO_LIMIT_REPORT_DEPTH_PROPERTY.get());
    }

    @Override
    public R apply(T t) {
        if (!toReport()) {
            turnReportingOff();
        }

        try {
            if (toReport) {
                fireEventStarting(description, parameters);
            }
            R result = function.apply(t);
            if (toReport) {
                ofNullable(additionalParams).ifPresent(ap -> fireAdditionalParameters(ap.get()));
                fireReturnedValueIfNecessary(resultDescription, result);
            }
            if (catchSuccessEvent() && toReport) {
                catchValue(result, successCaptors);
                onSuccessAdditional.forEach(FieldValueCaptureMaker::makeCaptures);
            }
            return result;
        } catch (Throwable thrown) {
            if (!shouldBeThrowableIgnored(thrown)) {
                if (toReport) {
                    ofNullable(additionalParams).ifPresent(ap -> fireAdditionalParameters(ap.get()));
                    fireThrownException(thrown);
                }
                if (catchFailureEvent() && toReport) {
                    catchValue(t, failureCaptors);
                    onFailureAdditional.forEach(FieldValueCaptureMaker::makeCaptures);
                }
                throw thrown;
            } else {
                if (toReport) {
                    ofNullable(additionalParams).ifPresent(ap -> fireAdditionalParameters(ap.get()));
                    fireReturnedValueIfNecessary(resultDescription, null);
                }
                return null;
            }
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

    @Override
    public <V> Get<V, R> compose(Function<? super V, ? extends T> before) {
        checkNotNull(before);

        Get<V, T> innerGet = null;
        if (before instanceof Get) {
            innerGet = (Get<V, T>) before;
        } else if (isLoggable(before)) {
            innerGet = new Get<>(before.toString(), (Function<V, T>) before);
        }

        var after = this.function;
        var previous = ofNullable(innerGet).map(st -> (Function<V, T>) st).orElse((Function<V, T>) before);

        var newFunction = new Function<V, R>() {
            @Override
            public R apply(V v) {
                var result = previous.apply(v);
                return ofNullable(result).map(after).orElse(null);
            }
        };

        var compositeGetStep = new Get<>(this.description, newFunction)
                .setResultDescription(resultDescription)
                .setPrevious(innerGet)
                .addIgnored(ignored)
                .addSuccessCaptors(successCaptors)
                .addFailureCaptors(failureCaptors)
                .addOnSuccessAdditional(onSuccessAdditional)
                .addOnFailureAdditional(onFailureAdditional)
                .setParameters(parameters);

        if (!toReport) {
            compositeGetStep.turnReportingOff();
        }

        return compositeGetStep.setMaxDepth(maxDepth);
    }

    @Override
    public <V> Function<T, V> andThen(Function<? super R, ? extends V> after) {
        checkNotNull(after);
        if (after instanceof Get) {
            return ((Get<R, V>) after).compose(this);
        }

        if (isLoggable(after)) {
            var newGetStep = new Get<>(after.toString(), (Function<R, V>) after);
            return newGetStep.compose(this);
        }

        return (Function<T, V>) after.compose(this.turnReportingOff());
    }

    Get<T, R> addIgnored(Collection<Class<? extends Throwable>> toBeIgnored) {
        ignored.addAll(toBeIgnored);
        ofNullable(previous).ifPresent(step -> step.addIgnored(toBeIgnored));
        return this;
    }

    Get<T, R> addSuccessCaptors(Collection<Captor<Object, Object>> successCaptors) {
        this.successCaptors.addAll(successCaptors);
        return this;
    }

    Get<T, R> addFailureCaptors(Collection<Captor<Object, Object>> failureCaptors) {
        this.failureCaptors.addAll(failureCaptors);
        return this;
    }

    Get<T, R> addOnSuccessAdditional(Collection<FieldValueCaptureMaker<CaptureOnSuccess>> additional) {
        this.onSuccessAdditional.addAll(additional);
        return this;
    }

    Get<T, R> addOnFailureAdditional(Collection<FieldValueCaptureMaker<CaptureOnFailure>> additional) {
        this.onFailureAdditional.addAll(additional);
        return this;
    }

    /**
     * Means that the starting/ending/result of the function applying won't be reported
     *
     * @return self-reference
     */
    Get<T, R> turnReportingOff() {
        toReport = false;
        ofNullable(previous).ifPresent(Get::turnReportingOff);
        return this;
    }

    Get<T, R> setParameters(Map<String, String> parameters) {
        this.parameters = parameters;
        return this;
    }

    Get<T, R> setResultDescription(String resultDescription) {
        this.resultDescription = resultDescription;
        return this;
    }

    Get<T, R> setMaxDepth(int maxDepth) {
        this.maxDepth = maxDepth;
        return this;
    }

    Get<T, R> setPrevious(Get<?, ?> previous) {
        this.previous = previous;
        return this;
    }

    Get<T, R> setAdditionalParams(Supplier<Map<String, String>> additionalParams) {
        this.additionalParams = additionalParams;
        return this;
    }
}