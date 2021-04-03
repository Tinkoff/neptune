package ru.tinkoff.qa.neptune.core.api.steps;

import ru.tinkoff.qa.neptune.core.api.event.firing.Captor;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.Collections.emptyMap;
import static java.util.Objects.nonNull;
import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static ru.tinkoff.qa.neptune.core.api.event.firing.StaticEventFiring.*;
import static ru.tinkoff.qa.neptune.core.api.properties.general.events.DoCapturesOf.catchFailureEvent;
import static ru.tinkoff.qa.neptune.core.api.properties.general.events.DoCapturesOf.catchSuccessEvent;
import static ru.tinkoff.qa.neptune.core.api.utils.IsLoggableUtil.isLoggable;

@SuppressWarnings("unchecked")
public class StepFunction<T, R> implements Function<T, R> {

    String description;
    Function<Object, Object> function;
    private boolean toReport = true;
    private final Set<Class<? extends Throwable>> ignored = new HashSet<>();
    private final Set<Captor<Object, Object>> successCaptors = new HashSet<>();
    private final Set<Captor<Object, Object>> failureCaptors = new HashSet<>();
    private Map<String, String> parameters = emptyMap();
    private String resultDescription;

    StepFunction(String description, Function<T, R> function) {
        checkArgument(nonNull(function), "Function should be defined");
        checkArgument(!isBlank(description), "Description should not be empty string or null value");
        this.description = description;
        this.function = (Function<Object, Object>) function;
    }

    StepFunction() {
        super();
    }

    /**
     * This method creates a function with some string description. This function is
     * supposed to get some value.
     *
     * @param description string narration of the getting value
     * @param function    which gets the needed value
     * @param <T>         type of the input value
     * @param <R>         type of the value to get
     * @return a new function with the given string description. Description is returned
     * by the {@link #toString()} method.
     */
    static <T, R> StepFunction<T, R> toGet(String description, Function<T, R> function) {
        return new StepFunction<>(description, function);
    }


    private boolean shouldBeThrowableIgnored(Throwable toBeIgnored) {
        for (var throwableClass : ignored) {
            if (throwableClass.isAssignableFrom(toBeIgnored.getClass())) {
                return true;
            }
        }
        return false;
    }

    private static <R> void fireReturnedValueIfNecessary(String resultDescription, R r) {
        if (isLoggable(r)) {
            fireReturnedValue(resultDescription, r);
        }
    }

    @Override
    public R apply(T t) {
        var thisClass = this.getClass();
        var isComplex = (SequentialStepFunction.class.isAssignableFrom(thisClass)
                && ((SequentialStepFunction<?, ?>) this).sequence.size() > 1);
        try {
            if (toReport) {
                fireEventStarting(description, parameters);
            }
            R result = (R) function.apply(t);
            if (toReport) {
                fireReturnedValueIfNecessary(resultDescription, result);
            }
            if (catchSuccessEvent() && toReport && !isComplex &&
                    !StepFunction.class.isAssignableFrom(function.getClass())) {
                catchValue(result, successCaptors);
            }
            return result;
        } catch (Throwable thrown) {
            if (!shouldBeThrowableIgnored(thrown)) {
                if (toReport) {
                    fireThrownException(thrown);
                }
                if (catchFailureEvent() && toReport && !isComplex &&
                        !StepFunction.class.isAssignableFrom(function.getClass())) {
                    catchValue(t, failureCaptors);
                }
                throw thrown;
            } else {
                if (toReport) {
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
    public <V> StepFunction<V, R> compose(Function<? super V, ? extends T> before) {
        checkNotNull(before);
        if (isLoggable(before)) {
            return new SequentialStepFunction<>(before, this);
        }

        var after = this.function;
        this.function = o -> {
            var result = before.apply((V) o);
            return ofNullable(result).map(after).orElse(null);
        };
        return (StepFunction<V, R>) this;
    }

    @Override
    public <V> StepFunction<T, V> andThen(Function<? super R, ? extends V> after) {
        return new SequentialStepFunction<>(this, after);
    }

    StepFunction<T, R> addIgnored(Collection<Class<? extends Throwable>> toBeIgnored) {
        ignored.addAll(toBeIgnored);
        return this;
    }

    StepFunction<T, R> addIgnored(Class<? extends Throwable> toBeIgnored) {
        ignored.add(toBeIgnored);
        return this;
    }

    Set<Class<? extends Throwable>> getIgnored() {
        return new HashSet<>(ignored);
    }

    StepFunction<T, R> addSuccessCaptors(List<Captor<Object, Object>> successCaptors) {
        this.successCaptors.addAll(successCaptors);
        return this;
    }

    StepFunction<T, R> addFailureCaptors(List<Captor<Object, Object>> failureCaptors) {
        this.failureCaptors.addAll(failureCaptors);
        return this;
    }

    /**
     * Means that the starting/ending/result of the function applying won't be reported
     *
     * @return self-reference
     */
    public StepFunction<T, R> turnReportingOff() {
        toReport = false;
        return this;
    }

    /**
     * Means that the starting/ending/result of the function applying won't be reported
     *
     * @return self-reference
     */
    public StepFunction<T, R> turnReportingOn() {
        toReport = true;
        return this;
    }

    Map<String, String> getParameters() {
        return parameters;
    }

    StepFunction<T, R> setParameters(Map<String, String> parameters) {
        this.parameters = parameters;
        return this;
    }

    StepFunction<T, R> setResultDescription(String resultDescription) {
        this.resultDescription = resultDescription;
        return this;
    }

    static final class SequentialStepFunction<T, R> extends StepFunction<T, R> {

        final LinkedList<Function<Object, Object>> sequence = new LinkedList<>();
        private static final Consumer<StepFunction<?, ?>> REPORT_TURN_ON = StepFunction::turnReportingOn;
        private static final Consumer<StepFunction<?, ?>> REPORT_TURN_OFF = StepFunction::turnReportingOff;
        private static final String NOT_DESCRIBED = "<not described value>";

        <V> SequentialStepFunction(Function<? super T, ? extends V> before,
                                   Function<? super V, ? extends R> after) {
            checkNotNull(before);
            checkNotNull(after);

            if (isLoggable(after)) {
                description = after.toString();
            } else {
                description = NOT_DESCRIBED;
            }

            function = t -> {
                Object result = t;
                for (Function<Object, Object> f : sequence) {
                    result = f.apply(result);
                    if (result == null) {
                        return null;
                    }
                }
                return (R) result;
            };

            StepFunction<? super V, ? extends R> stepAfter = getStepFunction(after);
            StepFunction<? super T, ? extends V> stepBefore = getStepFunction(before);

            if (SequentialStepFunction.class.isAssignableFrom(stepBefore.getClass())) {
                sequence.addAll(((SequentialStepFunction<?, ?>) stepBefore).sequence);
            } else {
                sequence.addFirst((Function<Object, Object>) stepBefore);
            }

            if (SequentialStepFunction.class.isAssignableFrom(stepAfter.getClass())) {
                sequence.addAll(((SequentialStepFunction<?, ?>) stepAfter).sequence);
            } else {
                sequence.addLast((Function<Object, Object>) stepAfter);
            }
        }

        private static <T, R> StepFunction<T, R> getStepFunction(Function<T, R> function) {
            if (StepFunction.class.isAssignableFrom(function.getClass())) {
                return (StepFunction<T, R>) function;
            } else if (isLoggable(function)) {
                return new StepFunction<>(function.toString(), function);
            }
            return new StepFunction<>(NOT_DESCRIBED, function);
        }

        @Override
        public <V> StepFunction<V, R> compose(Function<? super V, ? extends T> before) {
            checkNotNull(before);
            if (isLoggable(before)) {
                return new SequentialStepFunction<>(before, this);
            }
            this.sequence.addFirst((Function<Object, Object>) before);
            return (StepFunction<V, R>) this;
        }

        public <V> StepFunction<T, V> andThen(Function<? super R, ? extends V> after) {
            return new SequentialStepFunction<>(this, after);
        }

        public StepFunction<T, R> turnReportingOff() {
            super.turnReportingOff();
            syncReportTurnOn(REPORT_TURN_OFF);
            return this;
        }

        public StepFunction<T, R> turnReportingOn() {
            super.turnReportingOn();
            syncReportTurnOn(REPORT_TURN_ON);
            return this;
        }

        private void syncReportTurnOn(Consumer<StepFunction<?, ?>> reportTurnOnOff) {
            sequence.forEach(objectObjectFunction -> {
                if (StepFunction.class.isAssignableFrom(objectObjectFunction.getClass())) {
                    reportTurnOnOff.accept((StepFunction<?, ?>) objectObjectFunction);
                }
            });
        }

        private void syncIgnoredExceptions() {
            sequence.forEach(f -> {
                if (StepFunction.class.isAssignableFrom(f.getClass())) {
                    ((StepFunction<?, ?>) f).addIgnored(getIgnored());
                }
            });
        }

        @Override
        StepFunction<T, R> addIgnored(Collection<Class<? extends Throwable>> toBeIgnored) {
            super.addIgnored(toBeIgnored);
            syncIgnoredExceptions();
            return this;
        }

        @Override
        StepFunction<T, R> addIgnored(Class<? extends Throwable> toBeIgnored) {
            super.addIgnored(toBeIgnored);
            syncIgnoredExceptions();
            return this;
        }
    }
}
