package ru.tinkoff.qa.neptune.core.api;

import ru.tinkoff.qa.neptune.core.api.event.firing.annotation.*;
import ru.tinkoff.qa.neptune.core.api.exception.management.IgnoresThrowable;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;

import static ru.tinkoff.qa.neptune.core.api.StoryWriter.toGet;
import static ru.tinkoff.qa.neptune.core.api.IsLoggableUtil.isLoggable;
import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.StringUtils.EMPTY;

/**
 * This class is designed to typify functions which get required value.
 *
 * @param <T> is a type of an input value.
 * @param <R> is a type of a returned value.
 * @param <THIS> is self-type. It is necessary for the {@link #set(Function)} method.
 */
@SuppressWarnings("unchecked")
public abstract class GetStepSupplier<T, R, THIS extends GetStepSupplier<T, R, THIS>> implements Supplier<Function<T, R>>,
        IgnoresThrowable<THIS>, MakesCapturesOnFinishing<THIS> {

    private StepFunction<T, R> function;
    protected final Set<Class<? extends Throwable>> ignored = new HashSet<>();
    final List<CaptorFilterByProducedType> captorFilters = new ArrayList<>();

    protected GetStepSupplier() {
        MakesCapturesOnFinishing.makeCaptureSettings(this);
    }

    /**
     * Sets a functions and returns self-reference.
     * It is supposed to be overridden or overloaded/used by custom method.
     *
     * @param function which returns a goal value.
     * @return self-reference.
     */
    protected THIS set(Function<T, R> function) {
        checkNotNull(function);
        checkArgument(isLoggable(function),
                "It seems given function doesn't describe any value to get. Use method " +
                        "StoryWriter.toGet to describe it or override the toString method");
        StepFunction stepFunction;
        if (StepFunction.class.isAssignableFrom(function.getClass())) {
            stepFunction = (StepFunction) function;
        }
        else {
            stepFunction = toGet(function.toString(), function);
        }
        this.function = stepFunction
                .addIgnored(new ArrayList<>(ignored));

        if (!StepFunction.SequentialStepFunction.class.isAssignableFrom(stepFunction.getClass())) {
            stepFunction.addCaptorFilters(captorFilters);
        }

        return (THIS) this;
    }

    @Override
    public Function<T, R> get() {
        return function;
    }

    @Override
    public final THIS addIgnored(List<Class<? extends Throwable>> toBeIgnored) {
        ignored.addAll(toBeIgnored);
        ofNullable(function).ifPresent(function1 ->
                ((IgnoresThrowable) function1).addIgnored(toBeIgnored));
        return (THIS) this;
    }

    @Override
    public String toString() {
        return ofNullable(function).map(Object::toString).orElse(EMPTY);
    }

    /**
     * Marks that it is needed to produce a {@link java.awt.image.BufferedImage} after invocation of
     * {@link java.util.function.Function#apply(Object)} on built resulted {@link java.util.function.Function}.
     * This image is produced by {@link ru.tinkoff.qa.neptune.core.api.event.firing.Captor#getData(java.lang.Object)}
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
    public THIS makeImageCaptureOnFinish() {
        captorFilters.add(new CaptorFilterByProducedType(BufferedImage.class));
        return (THIS) this;
    }

    /**
     * Marks that it is needed to produce a {@link java.io.File} after invocation of
     * {@link java.util.function.Function#apply(Object)} on built resulted {@link java.util.function.Function}.
     * This image is produced by {@link ru.tinkoff.qa.neptune.core.api.event.firing.Captor#getData(java.lang.Object)}
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
    public THIS makeFileCaptureOnFinish() {
        captorFilters.add(new CaptorFilterByProducedType(File.class));
        return (THIS) this;
    }

    /**
     * Marks that it is needed to produce a {@link java.lang.StringBuilder} after invocation of
     * {@link java.util.function.Function#apply(Object)} on built resulted {@link java.util.function.Function}.
     * This image is produced by {@link ru.tinkoff.qa.neptune.core.api.event.firing.Captor#getData(java.lang.Object)}
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
    public THIS makeStringCaptureOnFinish() {
        captorFilters.add(new CaptorFilterByProducedType(StringBuilder.class));
        return (THIS) this;
    }

    /**
     * Marks that it is needed to produce some value after invocation of
     * {@link java.util.function.Function#apply(Object)} on built resulted {@link java.util.function.Function}.
     * This image is produced by {@link ru.tinkoff.qa.neptune.core.api.event.firing.Captor#getData(java.lang.Object)}
     *
     * <p>NOTE 1</p>
     * This value is produced if there is any subclass of {@link ru.tinkoff.qa.neptune.core.api.event.firing.Captor} that
     * may produce  a value of type defined by {@param typeOfCapture}.
     *
     * <p>NOTE 2</p>
     * A subclass of {@link ru.tinkoff.qa.neptune.core.api.event.firing.Captor} should be able to handle resulted values {@code R}
     * on success or input values {@code T} on failure.
     *
     * @param typeOfCapture is a type of a value to produce after the invocation of {@link java.util.function.Function#apply(Object)}
     *                      on the built function is finished.
     * @return self-reference
     */
    @Override
    public THIS onFinishMakeCaptureOfType(Class typeOfCapture) {
        captorFilters.add(new CaptorFilterByProducedType(typeOfCapture));
        return (THIS) this;
    }
}
