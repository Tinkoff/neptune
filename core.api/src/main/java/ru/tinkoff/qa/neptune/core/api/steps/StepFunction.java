package ru.tinkoff.qa.neptune.core.api.steps;

import ru.tinkoff.qa.neptune.core.api.event.firing.annotation.CaptorFilterByProducedType;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotation.MakesCapturesOnFinishing;
import ru.tinkoff.qa.neptune.core.api.exception.management.IgnoresThrowable;
import ru.tinkoff.qa.neptune.core.api.exception.management.StopsIgnoreThrowable;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

import static java.util.Objects.nonNull;
import static ru.tinkoff.qa.neptune.core.api.utils.IsLoggableUtil.isLoggable;
import static ru.tinkoff.qa.neptune.core.api.event.firing.StaticEventFiring.*;
import static ru.tinkoff.qa.neptune.core.api.properties.DoCapturesOf.catchFailureEvent;
import static ru.tinkoff.qa.neptune.core.api.properties.DoCapturesOf.catchSuccessEvent;
import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.lang.String.format;
import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.StringUtils.isBlank;

public class StepFunction<T, R> implements Function<T, R>, IgnoresThrowable<StepFunction<T, R>>,
        StopsIgnoreThrowable<StepFunction<T, R>>, MakesCapturesOnFinishing<StepFunction<T, R>> {

    private final String description;
    private final Function<T, R> function;
    final Set<Class<? extends Throwable>> ignored = new HashSet<>();
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

    private static <R> void fireReturnedValueIfNecessary(R r, boolean isComplex) {
        if (!isComplex) {
            if (isLoggable(r)) {
                fireReturnedValue(r);
            }
        }
    }

    private void fireEventStartingIfNecessary(T t, boolean isComplex) {
        if (isComplex) {
            return;
        }

        if (isLoggable(t)) {
            fireEventStarting(format("From %s get %s", t, description));
        }
        else {
            fireEventStarting(format("Get %s", description));
        }
    }

    private static void fireThrownExceptionIfNecessary(Throwable thrown, boolean isComplex) {
        if (!isComplex) {
            fireThrownException(thrown);
        }
    }

    private void fireEventFinishingIfNecessary(boolean isComplex) {
        if (!isComplex) {
            fireEventFinishing();
        }
    }

    @Override
    public R apply(T t) {
        var isComplex = SequentialStepFunction.class.equals(this.getClass());
        try {
            fireEventStartingIfNecessary(t, isComplex);
            R result = function.apply(t);
            fireReturnedValueIfNecessary(result, isComplex);
            if (catchSuccessEvent() && !isComplex && !StepFunction.class.isAssignableFrom(function.getClass())) {
                catchValue(result, captorFilters);
            }
            return result;
        }
        catch (Throwable thrown) {
            if (!shouldBeThrowableIgnored(thrown)) {
                fireThrownExceptionIfNecessary(thrown, isComplex);
                if (catchFailureEvent() && !isComplex && !StepFunction.class.isAssignableFrom(function.getClass())) {
                    catchValue(t, captorFilters);
                }
                throw thrown;
            }
            else {
                fireReturnedValueIfNecessary(null, isComplex);
                return null;
            }
        }
        finally {
            fireEventFinishingIfNecessary(isComplex);
        }
    }

    @Override
    public String toString() {
        return description;
    }

    public <V> StepFunction<V, R> compose(Function<? super V, ? extends T> before) {
        return new SequentialStepFunction<>(before, this);
    }

    public <V> StepFunction<T, V> andThen(Function<? super R, ? extends V> after) {
        return new SequentialStepFunction<>(this, after);
    }

    @Override
    public StepFunction<T, R> addIgnored(List<Class<? extends Throwable>> toBeIgnored) {
        ignored.addAll(toBeIgnored);
        return this;
    }

    @Override
    public StepFunction<T, R> stopIgnore(List<Class<? extends Throwable>> toStopIgnore) {
        ignored.removeAll(toStopIgnore);
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

    static class SequentialStepFunction<T, R> extends StepFunction<T, R> {

        final Function<? super T, ?> before;
        final Function<?, ? extends R> after;

        private <V> SequentialStepFunction(Function<? super T, ? extends V> before,
                                           Function<? super V, ? extends R> after) {
            super(after.toString(), getChainOfFunctions(before, after));
            this.before = before;
            this.after = after;
        }

        private static <T, V, R> Function<T, R> getChainOfFunctions(Function<? super T, ? extends V> before,
                                                                    Function<? super V, ? extends R> after) {
            checkNotNull(before);
            checkNotNull(after);
            checkArgument(StepFunction.class.isAssignableFrom(after.getClass()),
                    "It seems given after-function doesn't describe any value to get. Use method " +
                            "StoryWriter.toGet to describe the value to get");
            return t -> {
                V result = before.apply(t);
                return ofNullable(result).map(after).orElse(null);
            };
        }

        @Override
        public StepFunction<T, R> addIgnored(List<Class<? extends Throwable>> toBeIgnored) {
            super.addIgnored(toBeIgnored);
            var ignored = new ArrayList<>(this.ignored);
            if (IgnoresThrowable.class.isAssignableFrom(before.getClass())) {
                ((IgnoresThrowable) before).addIgnored(ignored);
            }

            ((IgnoresThrowable) after).addIgnored(ignored);
            return this;
        }

        @Override
        public StepFunction<T, R> stopIgnore(List<Class<? extends Throwable>> toStopIgnore) {
            super.stopIgnore(toStopIgnore);
            var notIgnored = new ArrayList<>(this.ignored);
            if (StopsIgnoreThrowable.class.isAssignableFrom(before.getClass())) {
                ((StopsIgnoreThrowable) before).stopIgnore(notIgnored);
            }

            ((StopsIgnoreThrowable) after).stopIgnore(notIgnored);
            return this;
        }

        public <V> StepFunction<V, R> compose(Function<? super V, ? extends T> before) {
            return new SequentialStepFunction<>(before, this);
        }

        public <V> StepFunction<T, V> andThen(Function<? super R, ? extends V> after) {
            return new SequentialStepFunction<>(this, after);
        }
    }
}
