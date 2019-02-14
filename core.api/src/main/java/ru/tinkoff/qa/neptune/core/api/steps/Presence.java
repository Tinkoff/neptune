package ru.tinkoff.qa.neptune.core.api.steps;

import com.google.common.collect.Iterables;
import ru.tinkoff.qa.neptune.core.api.steps.context.GetStepContext;

import java.lang.reflect.Array;
import java.util.function.Function;
import java.util.function.Supplier;

import static java.util.Objects.nonNull;
import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.String.format;
import static java.util.Optional.ofNullable;
import static ru.tinkoff.qa.neptune.core.api.steps.StoryWriter.toGet;
import static ru.tinkoff.qa.neptune.core.api.utils.IsLoggableUtil.isLoggable;

@SuppressWarnings("unchecked")
public class Presence<T extends GetStepContext<T>> extends SequentialGetStepSupplier.GetObjectChainedStepSupplier<T, Boolean, Object, Presence<T>> {

    protected Presence(Function<T, ?> toBePresent) {
        super(format("Presence of [%s]", toBePresent), o -> ofNullable(o)
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
                .orElse(false));
        from(checkFunction(toBePresent));
    }

    protected Presence(SequentialGetStepSupplier<T, ?, ?, ?, ?> toBePresent) {
        this(toBePresent.get());
    }

    private static <T extends GetStepContext<T>> Function<T, ?> checkFunction(Function<T, ?> toBePresent) {
        checkArgument(nonNull(toBePresent),
                "The function is not defined");
        checkArgument(isLoggable(toBePresent),
                "The function which returns the goal value should describe the value to get. Use method " +
                        "StoryWriter.toGet method or override toString method");

        if (StepFunction.class.isAssignableFrom(toBePresent.getClass())) {
            return toBePresent;
        }
        return toGet(toBePresent.toString(), toBePresent);
    }

    protected Function<T, Object> preparePreFunction() {
        var preFunction = super.preparePreFunction();
        if (StepFunction.class.isAssignableFrom(preFunction.getClass())) {
            ((StepFunction) preFunction).addIgnored(ignored);
        }
        return ((Function<Object, Object>) o -> ofNullable(o).orElse(false))
                .compose(preFunction);
    }

    protected String prepareStepDescription() {
        return toString();
    }

    /**
     * Creates an instance of {@link Presence}.
     *
     * @param function that should return something. If the result of {@link Function#apply(Object)} is not {@code null}
     *                 and not the empty iterable/array the this is considered present.
     * @param <T>      is a type of a {@link GetStepContext} subclass.
     * @return an instance of {@link Presence}.
     */
    public static <T extends GetStepContext<T>> Presence<T> presenceOf(Function<T, Object> function) {
        return new Presence(function);
    }

    /**
     * Creates an instance of {@link Presence}.
     *
     * @param toBePresent as a supplier of a function. If the result of {@link Function#apply(Object)} is not
     *                    {@code null} and not the empty iterable/array the this is considered present.
     * @param <T>         is a type of a {@link GetStepContext} subclass.
     * @return an instance of {@link Presence}.
     */
    public static <T extends GetStepContext<T>> Presence<T> presenceOf(SequentialGetStepSupplier<T, ?, ?, ?, ?> toBePresent) {
        return new Presence(toBePresent);
    }

    protected Function<Object, Boolean> getEndFunction() {
        return o -> {
            var result = super.getEndFunction().apply(o);
            if (!result) {
                return ofNullable(exceptionSupplier)
                        .map((Function<Supplier<? extends RuntimeException>, Boolean>) supplier -> {
                            throw supplier.get();
                        }).orElse(result);
            }
            return true;
        };
    }

    /**
     * This method defines an exception to be thrown when the expected value is not present.
     *
     * @param exceptionSupplier is a supplier of exception to be thrown when the expected value is not present.
     * @return self-reference
     */
    public Presence<T> throwIfNotPresent(Supplier<? extends RuntimeException> exceptionSupplier) {
        return throwOnEmptyResult(exceptionSupplier);
    }
}
