package com.github.toy.constructor.selenium.functions.presence;

import com.github.toy.constructor.core.api.GetSupplier;
import com.github.toy.constructor.core.api.SequentialGetSupplier;
import com.github.toy.constructor.core.api.StoryWriter;
import com.github.toy.constructor.selenium.SeleniumSteps;
import com.google.common.collect.Iterables;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static java.util.Arrays.asList;
import static java.util.Optional.ofNullable;

public final class Presence extends SequentialGetSupplier<SeleniumSteps, Boolean, Object, Presence> {

    private final List<Class<? extends Throwable>> ignored = new ArrayList<>();

    private Presence() {
        super();
    }

    /**
     * Creates an instance of {@link Presence}.
     *
     * @param supplier of a function. If the result of {@link Function#apply(Object)} is not
     *                 {@code null} and not the empty iterable/array the this is considered present.
     * @return an instance of {@link Presence}.
     */
    public static Presence presenceOf(GetSupplier<SeleniumSteps, ?, ?> supplier) {
        return new Presence().from(supplier);
    }

    /**
     * Creates an instance of {@link Presence}.
     *
     * @param function that should return something. If the result of {@link Function#apply(Object)} is not {@code null}
     *                 and not the empty iterable/array the this is considered present.
     * @return an instance of {@link Presence}.
     */
    public static Presence presenceOf(Function<SeleniumSteps, ?> function) {
        return new Presence().from(function);
    }

    /**
     * This method add types of {@link Throwable} to be ignored.
     *
     * @param toBeIgnored types of throwable which should be ignored. When {@link Function#apply(Object)}
     *                    throw some exception which should be ignored then result is considered not present.
     *                    Then the result is {@code false}
     * @return self-reference.
     */
    @SafeVarargs
    public final Presence ignore(Class<? extends Throwable>... toBeIgnored) {
        ignored.addAll(asList(toBeIgnored));
        return this;
    }

    @Override
    protected Function<Object, Boolean> getEndFunction() {
        return StoryWriter.toGet("Presence", o ->
                ofNullable(o)
                        .map(o1 -> {
                            Class<?> clazz = o1.getClass();
                            if (Iterable.class.isAssignableFrom(clazz)) {
                                return Iterables.size(Iterable.class.cast(o1)) > 0;
                            }

                            if (clazz.isArray()) {
                                return Array.getLength(o1) > 0;
                            }
                            return true;
                        })
                        .orElse(false));
    }
}
