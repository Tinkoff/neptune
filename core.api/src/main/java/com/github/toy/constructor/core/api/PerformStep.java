package com.github.toy.constructor.core.api;

import java.util.LinkedList;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static com.github.toy.constructor.core.api.StoryWriter.action;
import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.lang.String.format;

@SuppressWarnings("unchecked")
public interface PerformStep<THIS extends PerformStep<THIS>> {

    @ToBeReported(constantMessagePart = "Perform:")
    default THIS perform(Consumer<THIS> actionConsumer) {
        checkArgument(actionConsumer != null, "Action is not defined");
        checkArgument(DescribedConsumer.class.isAssignableFrom(actionConsumer.getClass()),
                "Action should be described by the StoryWriter.action method.");

        DescribedConsumer<THIS> describedConsumer = DescribedConsumer.class.cast(actionConsumer);
        LinkedList<Consumer<THIS>> sequence = describedConsumer.getSequence();

        if (sequence.size() == 0) {
            actionConsumer.accept((THIS) this);
            return (THIS) this;
        }

        Consumer<THIS> first = sequence.get(0);
        sequence.removeFirst();

        perform(first);
        sequence.forEach(thisConsumer -> perform(action(format("and then %s", thisConsumer), thisConsumer)));

        return (THIS) this;
    }

    default THIS perform(Supplier<Consumer<THIS>> actionSupplier) {
        checkNotNull(actionSupplier, "Supplier of the action was not defined");
        return perform(actionSupplier.get());
    }
}
