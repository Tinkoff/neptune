package ru.tinkoff.qa.neptune.core.api;

import com.google.common.collect.Iterables;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.function.Function;

import static java.util.Objects.nonNull;
import static ru.tinkoff.qa.neptune.core.api.StoryWriter.toGet;
import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.String.format;
import static java.util.Optional.ofNullable;

@SuppressWarnings("unchecked")
public class Presence<T extends GetStep<T>> extends GetStepSupplier<T, Boolean, Presence<T>> {

    protected Presence(Function<T, ?> toBePresent) {
        checkArgument(nonNull(toBePresent),
                "The function is not defined");
        checkArgument(StepFunction.class.isAssignableFrom(toBePresent.getClass()),
                "The function which returns the goal value should be described " +
                        "by the StoryWriter.toGet method.");

        set(toGet(format("Presence of %s", toBePresent), t -> {
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
     * @param <T> is a type of a {@link GetStep} subclass.
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
     * @param <T> is a type of a {@link GetStep} subclass.
     * @return an instance of {@link Presence}.
     */
    public static <T extends GetStep<T>> Presence<T> presenceOf(GetStepSupplier<T, ?, ?> supplier) {
        return presenceOf(supplier.get());
    }
}
