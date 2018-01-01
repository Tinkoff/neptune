package com.github.toy.constructor.core.api;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.github.toy.constructor.core.api.StoryWriter.action;
import static com.github.toy.constructor.core.api.StoryWriter.toGet;
import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.String.format;
import static java.util.Optional.ofNullable;

/**
 * This class is designed to restrict actions to perform on certain values.
 * Also it restricts chains of actions. It is supposed to supply chains of similar
 * actions.
 *
 * @param <T> is the type of an input value.
 * @param <R> is the type of a value which is supposed to be used by some action.
 * @param <THIS> is self-type. It is necessary for {@link #andThen(String, Function, Object...)} and
 *              {@link #andThen(String, Object, Object...)} methods.
 */
public abstract class SequentalActionSupplier<T, R, THIS extends SequentalActionSupplier<T, R, THIS>> implements Supplier<Consumer<T>>{

    private Consumer<T> wrappedConsumer;

    /**
     * This is the helping method which designed to build chains of similar actions on some objects got from
     * the input value. It is supposed to be overridden or overloaded/used by custom method.
     *
     * @param description of the action
     * @param function which gets a target object from input value.
     * @param additionalArguments that needed to perform the action
     * @return self-reference.
     */
    protected THIS andThen(String description, Function<T, R> function, Object...additionalArguments) {
        checkArgument(DescribedFunction.class.isAssignableFrom(function.getClass()),
                "Function should be described by the StoryWriter.toGet method.");
        Consumer<T> action = action(format("%s on %s", description, function), t -> {
            R r = function.apply(t);
            performActionOn(r, additionalArguments);
        });

        wrappedConsumer = ofNullable(wrappedConsumer).map(tConsumer -> tConsumer.andThen(action)).orElse(action);
        return (THIS) this;
    }

    /**
     * This is the helping method which designed to build chains of similar actions on some objects got from
     * the input value. It is supposed to be overridden or overloaded/used by custom method.
     *
     * @param description of the action
     * @param value which is used to perform the action on.
     * @param additionalArguments that needed to perform the action
     * @return self-reference.
     */
    protected THIS andThen(String description, R value, Object...additionalArguments) {
        return andThen(description, toGet(value.toString(), t -> value), additionalArguments);
    }

    /**
     * This abstract method describes actions that should be performed on some
     * value.
     *
     * @param value is an object for actions.
     */
    abstract void performActionOn(R value, Object...additionalArgument);

    @Override
    public Consumer<T> get() {
        return wrappedConsumer;
    }
}
