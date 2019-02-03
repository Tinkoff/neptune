package ru.tinkoff.qa.neptune.core.api.steps;

import ru.tinkoff.qa.neptune.core.api.event.firing.annotation.CaptorFilterByProducedType;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotation.MakesCapturesOnFinishing;
import ru.tinkoff.qa.neptune.core.api.exception.management.IgnoresThrowable;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.*;
import java.util.function.Function;

import static java.util.Objects.nonNull;
import static ru.tinkoff.qa.neptune.core.api.utils.IsLoggableUtil.isLoggable;
import static ru.tinkoff.qa.neptune.core.api.event.firing.StaticEventFiring.*;
import static ru.tinkoff.qa.neptune.core.api.properties.DoCapturesOf.catchFailureEvent;
import static ru.tinkoff.qa.neptune.core.api.properties.DoCapturesOf.catchSuccessEvent;
import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.lang.String.format;
import static org.apache.commons.lang3.StringUtils.isBlank;

public class StepFunction<T, R> implements Function<T, R>, IgnoresThrowable<StepFunction<T, R>>,
        MakesCapturesOnFinishing<StepFunction<T, R>> {

    private final String description;
    final Function<T, R> function;
    private final Set<Class<? extends Throwable>> ignored = new HashSet<>();
    private final Set<CaptorFilterByProducedType> captorFilters = new HashSet<>();

    StepFunction(String description, Function<T, R> function) {
        checkArgument(nonNull(function), "Function should be defined");
        checkArgument(!isBlank(description), "Description should not be empty string or null value");
        this.description = description;
        this.function = function;
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
        try {
            fireEventStarting(format("Get %s", description));
            R result = function.apply(t);
            fireReturnedValueIfNecessary(result);
            if (catchSuccessEvent() && !StepFunction.class.isAssignableFrom(function.getClass())) {
                catchValue(result, captorFilters);
            }
            return result;
        }
        catch (Throwable thrown) {
            if (!shouldBeThrowableIgnored(thrown)) {
                fireThrownException(thrown);
                if (catchFailureEvent() && !StepFunction.class.isAssignableFrom(function.getClass())) {
                    catchValue(t, captorFilters);
                }
                throw thrown;
            }
            else {
                fireReturnedValueIfNecessary(null);
                return null;
            }
        }
        finally {
            fireEventFinishing();
        }
    }

    @Override
    public String toString() {
        return description;
    }

    public <V> Function<V, R> compose(Function<? super V, ? extends T> before) {
        return new SequentialStepFunction<>(before, this);
    }

    public <V> Function<T, V> andThen(Function<? super R, ? extends V> after) {
        return new SequentialStepFunction<>(this, after);
    }

    @Override
    public StepFunction<T, R> addIgnored(List<Class<? extends Throwable>> toBeIgnored) {
        ignored.addAll(toBeIgnored);
        return this;
    }

    Set<Class<? extends Throwable>> getIgnored() {
        return new HashSet<>(ignored);
    }

    void addCaptorFilters(List<CaptorFilterByProducedType> captorFilters) {
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

    static final class SequentialStepFunction<T, R> implements Function<T, R>, IgnoresThrowable<SequentialStepFunction<T, R>> {

        private final LinkedList<Function<Object, Object>> sequence = new LinkedList<>();
        private final Set<Class<? extends Throwable>> ignored = new HashSet<>();

        @SuppressWarnings("unchecked")
        <V> SequentialStepFunction(Function<? super T, ? extends V> before,
                                   Function<? super V, ? extends R> after) {
            checkNotNull(before);
            checkNotNull(after);
            if (SequentialStepFunction.class.isAssignableFrom(before.getClass())) {
                sequence.addAll(((SequentialStepFunction<?, ?>) before).sequence);
            }
            else {
                sequence.addFirst((Function<Object, Object>) before);
            }

            if (SequentialStepFunction.class.isAssignableFrom(after.getClass())) {
                sequence.addAll(((SequentialStepFunction<?, ?>) after).sequence);
            }
            else {
                sequence.addLast((Function<Object, Object>) after);
            }
        }

        @Override
        public SequentialStepFunction<T, R> addIgnored(List<Class<? extends Throwable>> toBeIgnored) {
            ignored.addAll(toBeIgnored);
            return this;
        }

        @Override
        @SuppressWarnings("unchecked")
        public R apply(T t) {
            Object result = t;
            for (Function<Object, Object> f: sequence) {
                try {
                    result = f.apply(result);
                }
                catch (Throwable throwable) {
                    for (Class<? extends Throwable> c: ignored) {
                        if (c.isAssignableFrom(throwable.getClass())) {
                            return null;
                        }
                    }
                    throw throwable;
                }
                if (result == null) {
                    return null;
                }
            }
            return (R) result;
        }

        public <V> Function<V, R> compose(Function<? super V, ? extends T> before) {
            return new SequentialStepFunction<>(before, this);
        }

        public <V> Function<T, V> andThen(Function<? super R, ? extends V> after) {
            return new SequentialStepFunction<>(this, after);
        }

        public String toString() {
            if (sequence.size() == 0) {
                return super.toString();
            }

            return sequence.getLast().toString();
        }
    }
}
