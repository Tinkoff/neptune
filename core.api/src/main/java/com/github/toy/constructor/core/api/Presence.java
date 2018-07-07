package com.github.toy.constructor.core.api;

import com.google.common.collect.Iterables;

import java.lang.reflect.Array;
import java.util.*;
import java.util.function.Function;

import static com.github.toy.constructor.core.api.StoryWriter.toGet;
import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.String.format;
import static java.util.Optional.ofNullable;

@SuppressWarnings("unchecked")
public class Presence<T extends GetStep<T>> extends GetSupplier<T, Boolean, Presence<T>> {

    protected Presence(Function<T, ?> toBePresent) {
        checkArgument(toBePresent != null,
                "The function is not defined");
        checkArgument(DescribedFunction.class.isAssignableFrom(toBePresent.getClass()),
                "The function which returns the goal value should be described " +
                        "by the StoryWriter.toGet method.");

        set(toGet(format("Presence of %s", toBePresent), t -> {
            DescribedFunction<T, ?> describedToBePresent = DescribedFunction.class.cast(toBePresent);
            HashSet<Class<? extends Throwable>> toBeIgnored = new HashSet<>(ignored);
            toBeIgnored.removeAll(describedToBePresent.ignored);
            List<Class<? extends Throwable>> listOfThrowableClasses = new ArrayList<>(toBeIgnored);
            try {
                return isPresent(t.get(describedToBePresent.addIgnored(listOfThrowableClasses)));
            }
            finally {
                describedToBePresent.stopIgnore(listOfThrowableClasses);
            }
        }));
    }

    private static boolean isPresent(Object o) {
        return ofNullable(o)
                .map(o1 -> {
                    Class<?> clazz = o1.getClass();

                    if (Boolean.class.isAssignableFrom(clazz)) {
                        return Boolean.class.cast(o);
                    }

                    if (Iterable.class.isAssignableFrom(clazz)) {
                        return Iterables.size(Iterable.class.cast(o1)) > 0;
                    }

                    if (clazz.isArray()) {
                        return Array.getLength(o1) > 0;
                    }
                    return true;
                })
                .orElse(false);
    }

    /**
     * Creates an instance of {@link Presence}.
     *
     * @param function that should return something. If the result of {@link Function#apply(Object)} is not {@code null}
     *                 and not the empty iterable/array the this is considered present.
     * @return an instance of {@link Presence}.
     */
    public static <T extends GetStep<T>> Presence<T> presenceOf(Function<T, ?> function) {
        return new Presence(function);
    }

    /**
     * Creates an instance of {@link Presence}.
     *
     * @param supplier of a function. If the result of {@link Function#apply(Object)} is not
     *                 {@code null} and not the empty iterable/array the this is considered present.
     * @return an instance of {@link Presence}.
     */
    public static <T extends GetStep<T>> Presence<T> presenceOf(GetSupplier<T, ?, ?> supplier) {
        return presenceOf(supplier.get());
    }
}
