package ru.tinkoff.qa.neptune.core.api.steps;

import com.google.common.collect.Iterables;
import ru.tinkoff.qa.neptune.core.api.steps.context.GetStepContext;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.function.Function;

import static java.util.Objects.nonNull;
import static ru.tinkoff.qa.neptune.core.api.steps.StoryWriter.toGet;
import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.String.format;
import static java.util.Optional.ofNullable;
import static ru.tinkoff.qa.neptune.core.api.utils.IsLoggableUtil.isLoggable;

@SuppressWarnings("unchecked")
public class Presence<T extends GetStepContext<T>> extends GetStepSupplier<T, Boolean, Presence<T>> {

    //TODO needs for refactoring
    protected Presence(Function<T, ?> toBePresent) {
        checkArgument(nonNull(toBePresent),
                "The function is not defined");
        checkArgument(isLoggable(toBePresent),
                "The function which returns the goal value should describe the value to get. Use method " +
                        "StoryWriter.toGet method or override toString method");

        set(toGet(format("Presence of %s", toBePresent), t -> {
            if (StepFunction.class.isAssignableFrom(toBePresent.getClass())) {
                var describedToBePresent = (StepFunction) toBePresent;
                var toBeIgnored = new HashSet<>(ignored);
                toBeIgnored.removeAll(describedToBePresent.getIgnored());
                var listOfThrowableClasses = new ArrayList<>(toBeIgnored);
                try {
                    return isPresent(t.get(describedToBePresent.addIgnored(listOfThrowableClasses)));
                }
                finally {
                    describedToBePresent.stopIgnore(listOfThrowableClasses);
                }
            }
            else {
                try {
                    return isPresent(t.get(toBePresent));
                }
                catch (Throwable throwable) {
                    var clazz = throwable.getClass();
                    for (Class<? extends Throwable> c: ignored) {
                        if (c.isAssignableFrom(clazz)) {
                            return false;
                        }
                    }
                    throw throwable;
                }
            }
        }));
    }

    private static boolean isPresent(Object o) {
        return ofNullable(o)
                .map(o1 -> {
                    Class<?> clazz = o1.getClass();

                    if (Boolean.class.isAssignableFrom(clazz)) {
                        return (Boolean) o;
                    }

                    if (Iterable.class.isAssignableFrom(clazz)) {
                        return Iterables.size((Iterable) o1) > 0;
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
     * @param <T> is a type of a {@link GetStepContext} subclass.
     * @return an instance of {@link Presence}.
     */
    public static <T extends GetStepContext<T>> Presence<T> presenceOf(Function<T, ?> function) {
        return new Presence(function);
    }

    /**
     * Creates an instance of {@link Presence}.
     *
     * @param supplier of a function. If the result of {@link Function#apply(Object)} is not
     *                 {@code null} and not the empty iterable/array the this is considered present.
     * @param <T> is a type of a {@link GetStepContext} subclass.
     * @return an instance of {@link Presence}.
     */
    public static <T extends GetStepContext<T>> Presence<T> presenceOf(GetStepSupplier<T, ?, ?> supplier) {
        return presenceOf(supplier.get());
    }
}
