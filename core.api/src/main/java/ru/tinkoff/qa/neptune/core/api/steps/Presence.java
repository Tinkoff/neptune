package ru.tinkoff.qa.neptune.core.api.steps;

import com.google.common.collect.Iterables;
import ru.tinkoff.qa.neptune.core.api.steps.context.GetStepContext;

import java.lang.reflect.Array;
import java.util.function.Function;
import java.util.function.Supplier;

import static java.lang.String.format;
import static java.util.Optional.ofNullable;
import static ru.tinkoff.qa.neptune.core.api.utils.IsLoggableUtil.isLoggable;

@SuppressWarnings("unchecked")
public class Presence<T extends GetStepContext<T>> extends SequentialGetStepSupplier.GetObjectChainedStepSupplier<T, Boolean, Object, Presence<T>> {

    protected Presence(Function<T, ?> toBePresent) {
        super(format("Presence of [%s]", isLoggable(toBePresent) ? toBePresent.toString() : "<not described value>"),
                o -> ofNullable(o)
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
        from(toBePresent);
    }

    protected Presence(SequentialGetStepSupplier<T, ?, ?, ?, ?> toBePresent) {
        this(toBePresent.get());
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
    public static <T extends GetStepContext<T>, R> Presence<T> presenceOf(Function<T, R> function) {
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
    public static <T extends GetStepContext<T>, R> Presence<T> presenceOf(SequentialGetStepSupplier<T, R, ?, ?, ?> toBePresent) {
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
