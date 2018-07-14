package com.github.toy.constructor.core.api;

import java.util.function.Consumer;

import static com.github.toy.constructor.core.api.event.firing.StaticEventFiring.*;
import static com.github.toy.constructor.core.api.properties.DoCapturesOf.catchFailureEvent;
import static com.github.toy.constructor.core.api.properties.DoCapturesOf.catchSuccessEvent;
import static com.github.toy.constructor.core.api.utils.IsDescribedUtil.isDescribed;
import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.lang.String.format;
import static org.apache.commons.lang3.StringUtils.isBlank;

@SuppressWarnings("unchecked")
class StepAction<T> implements Consumer<T> {

    private final String description;
    private final Consumer<T> consumer;
    private boolean isComplex;

    StepAction(String description, Consumer<T> consumer) {
        checkArgument(consumer != null, "Consumer should be defined");
        checkArgument(!isBlank(description), "Description should not be empty");
        this.description = description;
        this.consumer = consumer;
    }

    private static <T> StepAction<T> getSequentialDescribedConsumer(Consumer<? super T> before,
                                                                    Consumer<? super T> after) {
        checkNotNull(before);
        checkNotNull(after);
        checkArgument(isDescribed(before),
                "It seems given consumer doesn't describe any before-action. Use method " +
                        "StoryWriter.action to describe the before-action or override the toString method");
        checkArgument(isDescribed(after),
                "It seems given consumer doesn't describe any after-action. Use method " +
                        "StoryWriter.action to describe the after-action or override the toString method");

        return new StepAction<T>(format("%s.\n\t  And then %s", before, after), t -> {
            before.accept(t); after.accept(t);
        }).setComplex();
    }

    private void fireEventStartingIfNecessary(T t) {
        if (isComplex) {
            return;
        }

        if (!PerformActionStep.class.isAssignableFrom(t.getClass())) {
            fireEventStarting(format("%s. Target: %s", description, t));
        }
        else {
            fireEventStarting(format("%s", description));
        }
    }

    private void fireThrownExceptionIfNecessary(Throwable thrown) {
        if (!isComplex) {
            fireThrownException(thrown);
        }
    }

    private void fireEventFinishingIfNecessary() {
        if (!isComplex) {
            fireEventFinishing();
        }
    }

    @Override
    public void accept(T t) {
        try {
            fireEventStartingIfNecessary(t);
            consumer.accept(t);
            if (catchSuccessEvent() && !isComplex && !StepAction.class.isAssignableFrom(consumer.getClass())) {
                catchResult(t, format("Performing of '%s' succeed", description));
            }
        }
        catch (Throwable thrown) {
            fireThrownExceptionIfNecessary(thrown);
            if (catchFailureEvent() && !isComplex && !StepAction.class.isAssignableFrom(consumer.getClass())) {
                catchResult(t, format("Performing of '%s' failed", description));
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
        return getSequentialDescribedConsumer(this, afterAction);
    }

    private StepAction<T> setComplex() {
        isComplex = true;
        return this;
    }
}
