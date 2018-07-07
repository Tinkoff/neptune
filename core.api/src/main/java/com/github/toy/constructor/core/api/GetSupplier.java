package com.github.toy.constructor.core.api;

import com.github.toy.constructor.core.api.exception.management.IgnoresThrowable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
@SuppressWarnings("unchecked")
public abstract class GetSupplier<T, R, THIS extends GetSupplier<T, R, THIS>> implements Supplier<Function<T, R>>,
        IgnoresThrowable<THIS> {

    private Function<T, R> function;
    protected final Set<Class<? extends Throwable>> ignored = new HashSet<>();

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
        this.function = DescribedFunction.class.cast(function)
                .addIgnored(new ArrayList<>(ignored));
        return (THIS) this;
    }

    @Override
    public Function<T, R> get() {
        return function;
    }

    @Override
    public final THIS addIgnored(List<Class<? extends Throwable>> toBeIgnored) {
        ignored.addAll(toBeIgnored);
        ofNullable(function).ifPresent(function1 ->
                IgnoresThrowable.class.cast(function1).addIgnored(toBeIgnored));
        return (THIS) this;
    }

    @Override
    public String toString() {
        return ofNullable(function).map(Object::toString).orElse(EMPTY);
    }
}
