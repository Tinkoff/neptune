package com.github.toy.constructor.core.api;

import java.util.LinkedList;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static com.github.toy.constructor.core.api.StaticEventFiring.catchResult;
import static com.github.toy.constructor.core.api.StoryWriter.action;
import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.lang.String.format;

@SuppressWarnings("unchecked")
public interface PerformStep<THIS extends PerformStep<THIS>> {

    default THIS perform(Consumer<THIS> actionConsumer) {
        checkArgument(actionConsumer != null, "Action is not defined");
        checkArgument(DescribedConsumer.class.isAssignableFrom(actionConsumer.getClass()),
                "Action should be described by the StoryWriter.action method.");
        actionConsumer.accept((THIS) this);
        return (THIS) this;
    }

    default THIS perform(Supplier<Consumer<THIS>> actionSupplier) {
        checkNotNull(actionSupplier, "Supplier of the action was not defined");
        return perform(actionSupplier.get());
    }
}
