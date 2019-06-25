package ru.tinkoff.qa.neptune.core.api.steps;

import com.google.common.collect.Iterables;
import ru.tinkoff.qa.neptune.core.api.steps.context.GetStepContext;

import java.lang.reflect.Array;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.String.format;
import static java.util.Objects.nonNull;
import static java.util.Optional.ofNullable;
import static ru.tinkoff.qa.neptune.core.api.utils.IsLoggableUtil.isLoggable;

@SuppressWarnings("unchecked")
public abstract class Presence<T extends GetStepContext<T>, R extends Presence<T, R>> extends SequentialGetStepSupplier.GetObjectChainedStepSupplier<T, Boolean, Object, R> {

    protected Presence(Function<T, ?> toBePresent) {
        super(format("Presence of [%s]", isLoggable(toBePresent) ? toBePresent.toString() : "<not described value>"),
                o -> ofNullable(o)
                .map(o1 -> {
                    Class<?> clazz = o1.getClass();

                    if (Boolean.class.isAssignableFrom(clazz)) {
                        return (Boolean) o1;
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

        StepFunction<T, ?> expectedToBePresent;
        if (StepFunction.class.isAssignableFrom(toBePresent.getClass())) {
            expectedToBePresent = ((StepFunction<T, ?>) toBePresent);
        }
        else {
            expectedToBePresent = new StepFunction<>(isLoggable(toBePresent) ?
                    toBePresent.toString() :
                    "<not described value>",
                    toBePresent);
        }
        from(expectedToBePresent);
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
    public R throwIfNotPresent(Supplier<? extends RuntimeException> exceptionSupplier) {
        return throwOnEmptyResult(exceptionSupplier);
    }

    /**
     * This is the general implementation of {@link Presence}
     * @param <T> is a type of a {@link GetStepContext} subclass
     */
    public static final class CommonPresence<T extends GetStepContext<T>> extends Presence<T, CommonPresence<T>> {

        private CommonPresence(Function<T, ?> toBePresent) {
            super(toBePresent);
        }

        private CommonPresence(SequentialGetStepSupplier<T, ?, ?, ?, ?> toBePresent) {
            super(toBePresent);
        }

        /**
         * Creates an instance of {@link CommonPresence}.
         *
         * @param function that should return something. If the result of {@link Function#apply(Object)} is not {@code null},
         *                 it is not an empty iterable/array or it is {@link Boolean} {@code true} then this is considered present.
         * @param <T>      is a type of a {@link GetStepContext} subclass.
         * @return an instance of {@link CommonPresence}.
         */
        public static <T extends GetStepContext<T>> CommonPresence<T> presenceOf(Function<T, ?> function) {
            checkArgument(nonNull(function), "Function should not be a null-value");
            return new CommonPresence(function);
        }

        /**
         * Creates an instance of {@link CommonPresence}.
         *
         * @param toBePresent as a supplier of a function. If the result of {@link Function#apply(Object)} is not {@code null},
         *                    it is not an empty iterable/array or it is {@link Boolean} {@code true} then this is considered present.
         * @param <T>         is a type of a {@link GetStepContext} subclass.
         * @return an instance of {@link CommonPresence}.
         */
        public static <T extends GetStepContext<T>> CommonPresence<T> presenceOf(SequentialGetStepSupplier<T, ?, ?, ?, ?> toBePresent) {
            checkArgument(nonNull(toBePresent), "Supplier of a function should not be a null-value");
            return new CommonPresence(toBePresent);
        }
    }
}
