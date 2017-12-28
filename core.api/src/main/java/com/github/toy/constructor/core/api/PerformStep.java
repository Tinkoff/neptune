package com.github.toy.constructor.core.api;

import java.util.function.Consumer;
import java.util.function.Supplier;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.Optional.ofNullable;

public interface PerformStep<THIS extends PerformStep<THIS>> {

    private THIS perform(String description, Consumer<THIS> actionConsumer) {
        checkArgument(DescribedPredicate.class.isAssignableFrom(actionConsumer.getClass()),
                "Action should be described by the StoryWriter.action method.");
        actionConsumer.accept((THIS) this);
        return (THIS) this;
    }

    default THIS perform(Consumer<THIS> actionConsumer) {
        return ofNullable(actionConsumer).map(thisConsumer -> perform(thisConsumer.toString(), thisConsumer))
                .orElseThrow(() -> new IllegalArgumentException("Consumer which contains desired action was not defined"));
    }

    default THIS perform(Supplier<Consumer<THIS>> actionSupplier) {
        checkNotNull(actionSupplier, "Supplier of the action was not defined");
        return perform(actionSupplier.get());
    }
}
