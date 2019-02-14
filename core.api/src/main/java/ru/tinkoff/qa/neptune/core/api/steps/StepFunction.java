package ru.tinkoff.qa.neptune.core.api.steps;

import ru.tinkoff.qa.neptune.core.api.event.firing.annotation.CaptorFilterByProducedType;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotation.MakesCapturesOnFinishing;
import ru.tinkoff.qa.neptune.core.api.exception.management.IgnoresThrowable;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

import static java.util.Objects.nonNull;
import static java.util.Optional.ofNullable;
import static ru.tinkoff.qa.neptune.core.api.utils.IsLoggableUtil.isLoggable;
import static ru.tinkoff.qa.neptune.core.api.event.firing.StaticEventFiring.*;
import static ru.tinkoff.qa.neptune.core.api.properties.DoCapturesOf.catchFailureEvent;
import static ru.tinkoff.qa.neptune.core.api.properties.DoCapturesOf.catchSuccessEvent;
import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.lang.String.format;
import static org.apache.commons.lang3.StringUtils.isBlank;

@SuppressWarnings("unchecked")
public class StepFunction<T, R> implements Function<T, R>, IgnoresThrowable<StepFunction<T, R>>,
        MakesCapturesOnFinishing<StepFunction<T, R>>, TurnsRetortingOff<StepFunction<T, R>> {

    String description;
    Function<Object, Object> function;
    private boolean toReport = true;
    private final Set<Class<? extends Throwable>> ignored = new HashSet<>();
    private final Set<CaptorFilterByProducedType> captorFilters = new HashSet<>();

    StepFunction(String description, Function<T, R> function) {
        checkArgument(nonNull(function), "Function should be defined");
        checkArgument(!isBlank(description), "Description should not be empty string or null value");
        this.description = description;
        this.function = (Function<Object, Object>) function;
    }

    StepFunction() {
        super();
    }


    private boolean shouldBeThrowableIgnored(Throwable toBeIgnored) {
        for (var throwableClass: ignored) {
            if (throwableClass.isAssignableFrom(toBeIgnored.getClass())) {
                return true;
            }
        }
        return false;
    }

    private static <R> void fireReturnedValueIfNecessary(R r) {
        if (isLoggable(r)) {
            fireReturnedValue(r);
        }
    }

    @Override
    public R apply(T t) {
        var thisClass = this.getClass();
        var isComplex = (SequentialStepFunction.class.isAssignableFrom(thisClass)
                && ((SequentialStepFunction) this).sequence.size() > 1);
        try {
            if (toReport) {
                fireEventStarting(format("Get %s", description));
            }
            R result = (R) function.apply(t);
            if (toReport) {
                fireReturnedValueIfNecessary(result);
            }
            if (catchSuccessEvent() && toReport && !isComplex &&
                    !StepFunction.class.isAssignableFrom(function.getClass())) {
                catchValue(result, captorFilters);
            }
            return result;
        }
        catch (Throwable thrown) {
            if (!shouldBeThrowableIgnored(thrown)) {
                if (toReport) {
                    fireThrownException(thrown);
                }
                if (catchFailureEvent() && toReport && !isComplex &&
                        !StepFunction.class.isAssignableFrom(function.getClass())) {
                    catchValue(t, captorFilters);
                }
                throw thrown;
            }
            else {
                if (toReport) {
                    fireReturnedValueIfNecessary(null);
                }
                return null;
            }
        }
        finally {
            if (toReport) {
                fireEventFinishing();
            }
        }
    }

    @Override
    public String toString() {
        return description;
    }

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

    public <V> StepFunction<T, V> andThen(Function<? super R, ? extends V> after) {
        return new SequentialStepFunction<>(this, after);
    }

    @Override
    public StepFunction<T, R> addIgnored(Collection<Class<? extends Throwable>> toBeIgnored) {
        ignored.addAll(toBeIgnored);
        return this;
    }

    @Override
    public StepFunction<T, R> addIgnored(Class<? extends Throwable> toBeIgnored) {
        ignored.add(toBeIgnored);
        return this;
    }

    Set<Class<? extends Throwable>> getIgnored() {
        return new HashSet<>(ignored);
    }

    void addCaptorFilters(Collection<CaptorFilterByProducedType> captorFilters) {
        this.captorFilters.addAll(captorFilters);
    }

    /**
     * Marks that it is needed to produce a {@link java.awt.image.BufferedImage} after invocation of
     * {@link java.util.function.Function#apply(Object)} on this object. This image is produced by
     * {@link ru.tinkoff.qa.neptune.core.api.event.firing.Captor#getData(java.lang.Object)}
     *
     * <p>NOTE 1</p>
     * This image is produced if there is any subclass of {@link ru.tinkoff.qa.neptune.core.api.event.firing.captors.ImageCaptor}
     * or {@link ru.tinkoff.qa.neptune.core.api.event.firing.Captor} that may produce a {@link java.awt.image.BufferedImage}.
     *
     * <p>NOTE 2</p>
     * A subclass of {@link ru.tinkoff.qa.neptune.core.api.event.firing.captors.ImageCaptor} or
     * {@link ru.tinkoff.qa.neptune.core.api.event.firing.Captor} should be able to handle resulted values {@code R}
     * on success or input values {@code T} on failure.
     *
     * @return self-reference
     */
    @Override
    public StepFunction<T, R> makeImageCaptureOnFinish() {
        captorFilters.add(new CaptorFilterByProducedType(BufferedImage.class));
        return this;
    }

    /**
     * Marks that it is needed to produce a {@link java.io.File} after invocation of
     * {@link java.util.function.Function#apply(Object)} on this object. This image is produced by
     * {@link ru.tinkoff.qa.neptune.core.api.event.firing.Captor#getData(java.lang.Object)}
     *
     * <p>NOTE 1</p>
     * This file is produced if there is any subclass of {@link ru.tinkoff.qa.neptune.core.api.event.firing.captors.FileCaptor}
     * or {@link ru.tinkoff.qa.neptune.core.api.event.firing.Captor} that may produce a {@link java.io.File}.
     *
     * <p>NOTE 2</p>
     * A subclass of {@link ru.tinkoff.qa.neptune.core.api.event.firing.captors.FileCaptor} or
     * {@link ru.tinkoff.qa.neptune.core.api.event.firing.Captor} should be able to handle resulted values {@code R}
     * on success or input values {@code T} on failure.
     *
     * @return self-reference
     */
    @Override
    public StepFunction<T, R> makeFileCaptureOnFinish() {
        captorFilters.add(new CaptorFilterByProducedType(File.class));
        return this;
    }

    /**
     * Marks that it is needed to produce a {@link java.lang.StringBuilder} after invocation of
     * {@link java.util.function.Function#apply(Object)} on this object. This string builder is produced by
     * {@link ru.tinkoff.qa.neptune.core.api.event.firing.Captor#getData(java.lang.Object)}
     *
     * <p>NOTE 1</p>
     * This string builder is produced if there is any subclass of {@link ru.tinkoff.qa.neptune.core.api.event.firing.captors.StringCaptor}
     * or {@link ru.tinkoff.qa.neptune.core.api.event.firing.Captor} that may produce a {@link java.lang.StringBuilder}.
     *
     * <p>NOTE 2</p>
     * A subclass of {@link ru.tinkoff.qa.neptune.core.api.event.firing.captors.StringCaptor} or
     * {@link ru.tinkoff.qa.neptune.core.api.event.firing.Captor} should be able to handle resulted values {@code R}
     * on success or input values {@code T} on failure.
     *
     * @return self-reference
     */
    @Override
    public StepFunction<T, R> makeStringCaptureOnFinish() {
        captorFilters.add(new CaptorFilterByProducedType(StringBuilder.class));
        return this;
    }

    /**
     * Marks that it is needed to produce some value after invocation of
     * {@link java.util.function.Function#apply(Object)} on this object. This value is produced by
     * {@link ru.tinkoff.qa.neptune.core.api.event.firing.Captor#getData(java.lang.Object)}
     *
     * <p>NOTE 1</p>
     * This value is produced if there is any subclass of {@link ru.tinkoff.qa.neptune.core.api.event.firing.Captor} that
     * may produce a value of type defined by {@param typeOfCapture}.
     *
     * <p>NOTE 2</p>
     * A subclass of {@link ru.tinkoff.qa.neptune.core.api.event.firing.Captor} should be able to handle resulted values {@code R}
     * on success or input values {@code T} on failure.
     *
     * @param typeOfCapture is a type of a value to produce after the invocation of {@link java.util.function.Function#apply(Object)}
     *                      on this object.
     * @return self-reference
     */
    @Override
    public StepFunction<T, R> onFinishMakeCaptureOfType(Class<?> typeOfCapture) {
        captorFilters.add(new CaptorFilterByProducedType(typeOfCapture));
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
            }
            else {
                description = NOT_DESCRIBED;
            }

            function = t -> {
                Object result = t;
                for (Function<Object, Object> f: sequence) {
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
            }
            else {
                sequence.addFirst((Function<Object, Object>) stepBefore);
            }

            if (SequentialStepFunction.class.isAssignableFrom(stepAfter.getClass())) {
                sequence.addAll(((SequentialStepFunction<?, ?>) stepAfter).sequence);
            }
            else {
                sequence.addLast((Function<Object, Object>) stepAfter);
            }
        }

        private static <T, R> StepFunction<T, R> getStepFunction(Function<T, R> function) {
            if (StepFunction.class.isAssignableFrom(function.getClass())) {
                return  (StepFunction<T, R>) function;
            } else if (isLoggable(function)) {
                return new StepFunction<>(function.toString(), function);
            }
            return new StepFunction<>(NOT_DESCRIBED, function);
        }

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
                if (IgnoresThrowable.class.isAssignableFrom(f.getClass())) {
                    ((IgnoresThrowable<?>) f).addIgnored(getIgnored());
                }
            });
        }

        @Override
        public StepFunction<T, R> addIgnored(Collection<Class<? extends Throwable>> toBeIgnored) {
            super.addIgnored(toBeIgnored);
            syncIgnoredExceptions();
            return this;
        }

        @Override
        public StepFunction<T, R> addIgnored(Class<? extends Throwable> toBeIgnored) {
            super.addIgnored(toBeIgnored);
            syncIgnoredExceptions();
            return this;
        }
    }
}
