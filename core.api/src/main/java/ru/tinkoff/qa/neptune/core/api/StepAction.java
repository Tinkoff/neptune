package ru.tinkoff.qa.neptune.core.api;

import java.util.function.Consumer;

import static java.util.Objects.nonNull;
import static ru.tinkoff.qa.neptune.core.api.IsLoggableUtil.isLoggable;
import static ru.tinkoff.qa.neptune.core.api.event.firing.StaticEventFiring.*;
import static ru.tinkoff.qa.neptune.core.api.properties.DoCapturesOf.catchFailureEvent;
import static ru.tinkoff.qa.neptune.core.api.properties.DoCapturesOf.catchSuccessEvent;
import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.String.format;
import static org.apache.commons.lang3.StringUtils.isBlank;

class StepAction<T> implements Consumer<T> {

    private final String description;
    private final Consumer<T> consumer;

    StepAction(String description, Consumer<T> consumer) {
        checkArgument(nonNull(consumer), "Consumer should be defined");
        checkArgument(!isBlank(description), "Description should not be empty");
        this.description = description;
        this.consumer = consumer;
    }

    private void fireEventStartingIfNecessary(T t) {
        if (isLoggable(t)) {
            fireEventStarting(format("%s. Target: %s", description, t));
        }
        else {
            fireEventStarting(format("%s", description));
        }
    }

    private void fireThrownExceptionIfNecessary(Throwable thrown) {
        fireThrownException(thrown);
    }

    private void fireEventFinishingIfNecessary() {
        fireEventFinishing();
    }

    @Override
    public void accept(T t) {
        try {
            fireEventStartingIfNecessary(t);
            consumer.accept(t);
            if (catchSuccessEvent() && !StepAction.class.isAssignableFrom(consumer.getClass())) {
                catchValue(t, format("Performing of '%s' succeed", description));
            }
        }
        catch (Throwable thrown) {
            fireThrownExceptionIfNecessary(thrown);
            if (catchFailureEvent() && !StepAction.class.isAssignableFrom(consumer.getClass())) {
                catchValue(t, format("Performing of '%s' failed", description));
            }
            throw thrown;
        }
        finally {
            fireEventFinishingIfNecessary();
        }
    }

    @Override
    public String toString() {
        return description;
    }

    public Consumer<T> andThen(Consumer<? super T> afterAction)  {
        return new ChainedStepAction<>(this, afterAction);
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
            return before.toString() + "\n" + after.toString();
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
