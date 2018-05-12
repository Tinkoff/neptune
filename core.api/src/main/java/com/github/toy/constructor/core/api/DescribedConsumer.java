package com.github.toy.constructor.core.api;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static org.apache.commons.lang3.StringUtils.isBlank;

@SuppressWarnings("unchecked")
class DescribedConsumer<T> implements Consumer<T> {

    private final LinkedList<Consumer<T>> sequence = new LinkedList<>();
    private final String description;
    private final Consumer<T> consumer;

    DescribedConsumer(String description, Consumer<T> consumer) {
        checkArgument(consumer != null, "Consumer should be defined");
        checkArgument(!isBlank(description), "Description should not be empty");
        this.description = description;
        this.consumer = consumer;
    }

    private static <T> DescribedConsumer<T> getSequentialDescribedConsumer(Consumer<? super T> before,
                                                                  Consumer<? super T> after) {
        checkNotNull(before);
        checkNotNull(after);
        checkArgument(DescribedConsumer.class.isAssignableFrom(before.getClass()),
                "It seems given consumer doesn't describe any before-action. Use method " +
                        "StoryWriter.action to describe the after-action.");
        checkArgument(DescribedConsumer.class.isAssignableFrom(after.getClass()),
                "It seems given consumer doesn't describe any after-action. Use method " +
                        "StoryWriter.action to describe the after-action.");

        return new DescribedConsumer<T>(after.toString(), t -> {
            before.accept(t); after.accept(t);
        })
                .addToSequence(DescribedConsumer.class.cast(before))
                .addToSequence(DescribedConsumer.class.cast(after));
    }

    @Override
    public void accept(T t) {
        consumer.accept(t);
    }

    @Override
    public String toString() {
        return description;
    }

    LinkedList<Consumer<T>> getSequence() {
        return sequence;
    }

    private DescribedConsumer<T> addToSequence(DescribedConsumer<T> sequenceChain) {
        List<Consumer<T>> consumers = sequenceChain.getSequence();
        if (consumers.size() == 0) {
            sequence.addLast(sequenceChain);
        }
        else {
            sequence.addAll(consumers);
        }
        return this;
    }

    public Consumer<T> andThen(Consumer<? super T> afterAction)  {
        return getSequentialDescribedConsumer(this, afterAction);
    }
}
