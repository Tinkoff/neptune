package ru.tinkoff.qa.neptune.core.api.steps;

import ru.tinkoff.qa.neptune.core.api.event.firing.annotation.CaptorFilterByProducedType;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotation.MakesCapturesOnFinishing;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

import static java.lang.System.lineSeparator;
import static java.util.Objects.nonNull;
import static ru.tinkoff.qa.neptune.core.api.event.firing.StaticEventFiring.*;
import static ru.tinkoff.qa.neptune.core.api.properties.DoCapturesOf.catchFailureEvent;
import static ru.tinkoff.qa.neptune.core.api.properties.DoCapturesOf.catchSuccessEvent;
import static com.google.common.base.Preconditions.checkArgument;
import static org.apache.commons.lang3.StringUtils.isBlank;

public class StepAction<T> implements Consumer<T>, MakesCapturesOnFinishing<StepAction<T>> {

    private final String description;
    private final Consumer<T> consumer;
    private final Set<CaptorFilterByProducedType> captorFilters = new HashSet<>();

    StepAction(String description, Consumer<T> consumer) {
        checkArgument(nonNull(consumer), "Consumer should be defined");
        checkArgument(!isBlank(description), "Description should not be empty");
        this.description = description;
        this.consumer = consumer;
    }

    private void fireThrownExceptionIfNecessary(Throwable thrown) {
        fireThrownException(thrown);
    }

    @Override
    public void accept(T t) {
        try {
            fireEventStarting(description);
            consumer.accept(t);
            if (catchSuccessEvent() && !StepAction.class.isAssignableFrom(consumer.getClass())) {
                catchValue(t, captorFilters);
            }
        }
        catch (Throwable thrown) {
            fireThrownExceptionIfNecessary(thrown);
            if (catchFailureEvent() && !StepAction.class.isAssignableFrom(consumer.getClass())) {
                catchValue(t, captorFilters);
            }
            throw thrown;
        }
        finally {
            fireEventFinishing();
        }
    }

    @Override
    public String toString() {
        return description;
    }

    public Consumer<T> andThen(Consumer<? super T> afterAction)  {
        return new ChainedStepAction<>(this, afterAction);
    }

    void addCaptorFilters(List<CaptorFilterByProducedType> captorFilters) {
        this.captorFilters.addAll(captorFilters);
    }

    /**
     * Marks that it is needed to produce a {@link java.awt.image.BufferedImage} after invocation of
     * {@link java.util.function.Consumer#accept(Object)} on this object. This image is produced by
     * {@link ru.tinkoff.qa.neptune.core.api.event.firing.Captor#getData(java.lang.Object)}
     *
     * <p>NOTE 1</p>
     * This image is produced if there is any subclass of {@link ru.tinkoff.qa.neptune.core.api.event.firing.captors.ImageCaptor}
     * or {@link ru.tinkoff.qa.neptune.core.api.event.firing.Captor} that may produce a {@link java.awt.image.BufferedImage}.
     *
     * <p>NOTE 2</p>
     * A subclass of {@link ru.tinkoff.qa.neptune.core.api.event.firing.captors.ImageCaptor} or
     * {@link ru.tinkoff.qa.neptune.core.api.event.firing.Captor} should be able to handle input values {@code T}
     * on success/on failure.
     *
     * @return self-reference
     */
    @Override
    public StepAction<T> makeImageCaptureOnFinish() {
        captorFilters.add(new CaptorFilterByProducedType(BufferedImage.class));
        return this;
    }

    /**
     * Marks that it is needed to produce a {@link java.io.File} after invocation of
     * {@link java.util.function.Consumer#accept(Object)} on this object. This file is produced by
     * {@link ru.tinkoff.qa.neptune.core.api.event.firing.Captor#getData(java.lang.Object)}
     *
     * <p>NOTE 1</p>
     * This file is produced if there is any subclass of {@link ru.tinkoff.qa.neptune.core.api.event.firing.captors.FileCaptor}
     * or {@link ru.tinkoff.qa.neptune.core.api.event.firing.Captor} that may produce a {@link java.io.File}.
     *
     * <p>NOTE 2</p>
     * A subclass of {@link ru.tinkoff.qa.neptune.core.api.event.firing.captors.FileCaptor} or
     * {@link ru.tinkoff.qa.neptune.core.api.event.firing.Captor} should be able to handle input values {@code T}
     * on success/on failure.
     *
     * @return self-reference
     */
    @Override
    public StepAction<T> makeFileCaptureOnFinish() {
        captorFilters.add(new CaptorFilterByProducedType(File.class));
        return this;
    }

    /**
     * Marks that it is needed to produce a {@link java.lang.StringBuilder} after invocation of
     * {@link java.util.function.Consumer#accept(Object)} on this object. This string builder is produced by
     * {@link ru.tinkoff.qa.neptune.core.api.event.firing.Captor#getData(java.lang.Object)}
     *
     * <p>NOTE 1</p>
     * This string builder is produced if there is any subclass of {@link ru.tinkoff.qa.neptune.core.api.event.firing.captors.StringCaptor}
     * or {@link ru.tinkoff.qa.neptune.core.api.event.firing.Captor} that may produce a {@link java.lang.StringBuilder}.
     *
     * <p>NOTE 2</p>
     * A subclass of {@link ru.tinkoff.qa.neptune.core.api.event.firing.captors.StringCaptor} or
     * {@link ru.tinkoff.qa.neptune.core.api.event.firing.Captor} should be able to handle input values {@code T}
     * on success/on failure.
     *
     * @return self-reference
     */
    @Override
    public StepAction<T> makeStringCaptureOnFinish() {
        captorFilters.add(new CaptorFilterByProducedType(StringBuilder.class));
        return this;
    }

    /**
     * Marks that it is needed to produce some value after invocation of
     * {@link java.util.function.Consumer#accept(Object)} on this object. This value is produced by
     * {@link ru.tinkoff.qa.neptune.core.api.event.firing.Captor#getData(java.lang.Object)}
     *
     * <p>NOTE 1</p>
     * This value is produced if there is any subclass of {@link ru.tinkoff.qa.neptune.core.api.event.firing.Captor}
     * that may produce something of type defined by {@param typeOfCapture}.
     *
     * <p>NOTE 2</p>
     * A subclass of {@link ru.tinkoff.qa.neptune.core.api.event.firing.Captor} should be able to handle input values {@code T}
     * on success/on failure.
     *
     * @param typeOfCapture is a type of a value to produce by {@link ru.tinkoff.qa.neptune.core.api.event.firing.Captor#getData(java.lang.Object)}
     * @return self-reference
     */
    @Override
    public StepAction<T> onFinishMakeCaptureOfType(Class<?> typeOfCapture) {
        captorFilters.add(new CaptorFilterByProducedType(typeOfCapture));
        return this;
    }

    private static class ChainedStepAction<T> implements Consumer<T> {

        private final Consumer<? super T> before;
        private final Consumer<? super T> after;

        private ChainedStepAction(Consumer<? super T> before, Consumer<? super T> after) {
            var clazz1 = before.getClass();
            var clazz2 = after.getClass();
            checkArgument(StepAction.class.isAssignableFrom(clazz1)
                            || ChainedStepAction.class.isAssignableFrom(clazz1),
                    "It seems given consumer doesn't describe any before-action. Use method " +
                            "StoryWriter.action to describe the before-action to perform");
            checkArgument(StepAction.class.isAssignableFrom(clazz2)
                            || ChainedStepAction.class.isAssignableFrom(clazz2),
                    "It seems given consumer doesn't describe any after-action. Use method " +
                            "StoryWriter.action to describe the after-action to perform");
            this.before = before;
            this.after = after;
        }

        @Override
        public String toString() {
            return before.toString() + lineSeparator() + after.toString();
        }

        @Override
        public void accept(T t) {
            before.accept(t);
            after.accept(t);
        }

        @Override
        public Consumer<T> andThen(Consumer<? super T> after) {
            return new ChainedStepAction<>(this, after);
        }
    }
}
