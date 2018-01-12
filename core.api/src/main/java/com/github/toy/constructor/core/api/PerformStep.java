package com.github.toy.constructor.core.api;

import java.util.function.Consumer;
import java.util.function.Supplier;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

public interface PerformStep<THIS extends PerformStep<THIS>> {

    @ToBeReported(constantMessagePart = "Perform:")
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
