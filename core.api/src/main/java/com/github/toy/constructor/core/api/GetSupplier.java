package com.github.toy.constructor.core.api;

import java.util.function.Function;
import java.util.function.Supplier;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.StringUtils.EMPTY;

/**
 * This class is designed to typify functions which get required value.
 *
 * @param <T> is a type of an input value.
 * @param <R> is a type of a returned value.
 * @param <THIS> is self-type. It is necessary for the {@link #set(Function)} method.
 */
public abstract class GetSupplier<T, R, THIS extends GetSupplier<T, R, THIS>> implements Supplier<Function<T, R>> {

    private Function<T, R> function;

    /**
     * Sets a functions and returns self-reference.
     * It is supposed to be overridden or overloaded/used by custom method.
     *
     * @param function which returns a goal value.
     * @return self-reference.
     */
    protected THIS set(Function<T, R> function){
        checkNotNull(function);
        checkArgument(DescribedFunction.class.isAssignableFrom(function.getClass()),
                "It seems given function doesn't describe any value to get. Use method " +
                        "StoryWriter.toGet to describe the value to get previously.");
        this.function = function;
        return (THIS) this;
    }

    @Override
    public Function<T, R> get() {
        return function;
    }

    @Override
    public String toString() {
        return ofNullable(function).map(Object::toString).orElse(EMPTY);
    }
}
