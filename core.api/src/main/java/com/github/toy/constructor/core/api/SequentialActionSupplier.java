package com.github.toy.constructor.core.api;

import org.apache.commons.lang3.ArrayUtils;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.github.toy.constructor.core.api.StoryWriter.action;
import static com.github.toy.constructor.core.api.StoryWriter.toGet;
import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.String.format;
import static java.util.Arrays.stream;
import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.ArrayUtils.addAll;
import static org.apache.commons.lang3.StringUtils.isBlank;

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
public abstract class SequentialActionSupplier<T, R, THIS extends SequentialActionSupplier<T, R, THIS>> implements Supplier<Consumer<T>>{

    private Consumer<T> wrappedConsumer;

    /**
     * This is the helping method which designed to build chains of similar actions on some objects got from
     * the input value. It is supposed to be overridden or overloaded/used by custom method.
     *
     * @param description of the action
     * @param function which gets a target object from input value.
     * @param additionalArguments that needed to perform the action. It may be ignored.
     * @return self-reference.
     */
    protected THIS andThen(String description, Function<T, ? extends R> function, Object...additionalArguments) {
        checkArgument(!isBlank(description),
                "Description should not be blank");
        checkArgument(function != null,
                "Function which gets value to perform action is not defined");
        checkArgument(DescribedFunction.class.isAssignableFrom(function.getClass()),
                "Function should be described by the StoryWriter.toGet method");
        checkArgument(additionalArguments != null,
                "Array of additional arguments should not be a null value");
        String fullDescription = description;
        fullDescription = format("%s. With parameters: %s", fullDescription,
                ArrayUtils.toString(addAll(new Object[]{function}, additionalArguments)));
        Consumer<T> action = action(fullDescription, t -> {
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
     * @param supplier which contains function to gets a target object from input value.
     * @param additionalArguments that needed to perform the action. It may be ignored.
     * @return self-reference.
     */
    protected THIS andThen(String description, GetSupplier<T, ? extends R, ?> supplier, Object...additionalArguments) {
        checkArgument(supplier != null, "Supplier of a function which gets value " +
                "to perform action is not defined");
        return andThen(description, supplier.get(), additionalArguments);
    }

    /**
     * This is the helping method which designed to build chains of similar actions on some objects got from
     * the input value. It is supposed to be overridden or overloaded/used by custom method.
     *
     * @param description of the action
     * @param value which is used to perform the action. It is supposed that this object is got from the
     *              input value {@code T} firstly.
     * @param additionalArguments that needed to perform the action. It may be ignored.
     * @return self-reference.
     */
    protected THIS andThen(String description, R value, Object...additionalArguments) {
        checkArgument(value != null,
                "Object to perform action is not defined");
        return andThen(description, toGet(value.toString(), t -> value), additionalArguments);
    }

    /**
     * This abstract method describes actions that should be performed on some
     * value.
     *
     * @param value is an object for actions.
     * @param additionalArgument that needed to perform the action. It may be ignored.
     */
    protected abstract void performActionOn(R value, Object...additionalArgument);

    @Override
    public Consumer<T> get() {
        return wrappedConsumer;
    }
}
