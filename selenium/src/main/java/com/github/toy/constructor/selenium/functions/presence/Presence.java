package com.github.toy.constructor.selenium.functions.presence;

import com.github.toy.constructor.core.api.GetSupplier;
import com.github.toy.constructor.core.api.SequentialGetSupplier;
import com.github.toy.constructor.selenium.SeleniumSteps;
import com.github.toy.constructor.selenium.functions.searching.MultipleSearchSupplier;
import com.github.toy.constructor.selenium.functions.searching.SearchSupplier;
import com.google.common.collect.Iterables;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static com.github.toy.constructor.core.api.StoryWriter.toGet;
import static com.github.toy.constructor.selenium.CurrentContentFunction.currentContent;
import static java.lang.String.format;
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
     * Creates an instance of {@link Presence}.
     *
     * @param supplier supplier of a search criteria to find a single element.
     * @return an instance of {@link Presence}.
     */
    public static Presence presenceOf(SearchSupplier<?> supplier) {
        return new Presence().from(supplier.get().compose(currentContent()))
                .ignore(NoSuchElementException.class, StaleElementReferenceException.class);
    }

    /**
     * Creates an instance of {@link Presence}.
     *
     * @param supplier supplier of a search criteria to find a list of elements.
     * @return an instance of {@link Presence}.
     */
    public static Presence presenceOf(MultipleSearchSupplier<?> supplier) {
        return new Presence().from(supplier.get().compose(currentContent()))
                .ignore(StaleElementReferenceException.class);
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
    protected Presence from(GetSupplier<SeleniumSteps, ?, ?> supplier) {
        return from(supplier.get());
    }

    private boolean isPresent(Object o) {
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

    @Override
    protected Presence from(Function<SeleniumSteps, ?> function) {
        return super.from(toGet(format("Presence of %s", function), seleniumSteps -> {
            try {
                return isPresent(seleniumSteps.get(function));
            }
            catch (Throwable t) {
                Class<? extends Throwable> errorClass = t.getClass();
                for (Class<? extends Throwable> c: ignored) {
                    if (c.isAssignableFrom(errorClass)) {
                        return false;
                    }
                }
                throw new RuntimeException(t.getMessage(), t);
            }
        }));
    }

    @Override
    protected Function<Object, Boolean> getEndFunction() {
        return toGet("Presence", Boolean.class::cast);
    }
}
