package com.github.toy.constructor.core.api;

import java.util.function.Function;
import java.util.function.Supplier;

import static com.github.toy.constructor.core.api.StoryWriter.toGet;

/**
 * This class is designed to typify functions which get required value.
 *
 * @param <T> is a type of an input value.
 * @param <R> is a type of a returned value.
 * @param <THIS> is self-type. It is necessary for the {@link #set(String, Function)} method.
 */
public abstract class GetSupplier<T, R, THIS extends GetSupplier<T, R, THIS>> implements Supplier<Function<T, R>> {

    private Function<T, R> function;

    /**
     * Sets a functions and returns self-reference.
     * It is supposed to be overridden or overloaded/used by custom method.
     *
     * @param description of a value to be returned.
     * @param function which returns a goal value.
     * @return self-reference.
     */
    protected THIS set(String description, Function<T, R> function){
        this.function = toGet(description, function);
        return (THIS) this;
    }

    @Override
    public Function<T, R> get() {
        return function;
    }
}
