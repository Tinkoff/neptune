package com.github.toy.constructor.core.api.sequence.action;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.github.toy.constructor.core.api.StoryWriter.action;
import static java.lang.String.format;
import static java.util.Optional.ofNullable;

/**
 * This class is designed to restrict actions to perform on certain values.
 * Also it restricts chains of actions. It is supposed to build chains of similar
 * actions.
 *
 * @param <T> is the type of an input value.
 * @param <R> is the type of a value which is supposed to be used by some action.
 * @param <THIS> is self-type. It is necessary for the {@link #andThen(String, Function)} method.
 */
public abstract class ActionBuilder<T, R, THIS extends ActionBuilder<T, R, THIS>> implements Supplier<Consumer<T>>{

    private Consumer<T> wrappedConsumer;

    /**
     * This is the helping method which designed to build chains of similar actions on some objects got from
     * the input value. It is supposed to be overridden or overloaded by custom method.
     *
     * @param description of the action
     * @param function which gets a target object from input value.
     * @return self-reference.
     */
    protected THIS andThen(String description, Function<T, R> function) {
        Consumer<T> action = action(format("%s on %s", description, function), t -> {
            R r = function.apply(t);
            performActionOn(r);
        });

        wrappedConsumer = ofNullable(wrappedConsumer).map(tConsumer -> tConsumer.andThen(action)).orElse(action);
        return (THIS) this;
    }

    /**
     * This abstract method describes actions that should be performed on some
     * value.
     *
     * @param value is an object for actions.
     */
    abstract void performActionOn(R value);

    @Override
    public Consumer<T> get() {
        return wrappedConsumer;
    }
}
