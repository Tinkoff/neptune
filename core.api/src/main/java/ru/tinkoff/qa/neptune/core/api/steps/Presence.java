package ru.tinkoff.qa.neptune.core.api.steps;

import com.google.common.collect.Iterables;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotations.MaxDepthOfReporting;
import ru.tinkoff.qa.neptune.core.api.steps.context.Context;
import ru.tinkoff.qa.neptune.core.api.steps.parameters.IncludeParamsOfInnerGetterStep;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.nonNull;
import static java.util.Optional.ofNullable;
import static ru.tinkoff.qa.neptune.core.api.utils.IsLoggableUtil.isLoggable;

@SuppressWarnings("unchecked")
@SequentialGetStepSupplier.DefineResultDescriptionParameterName("Is present")
@IncludeParamsOfInnerGetterStep
@MaxDepthOfReporting(0)
public final class Presence<T extends Context<?>> extends SequentialGetStepSupplier.GetObjectChainedStepSupplier<T, Boolean, Object, Presence<T>> {

    private final Set<Class<? extends Throwable>> ignored2 = new HashSet<>();

    private Presence() {
        super(o -> ofNullable(o)
                .map(o1 -> {
                    Class<?> clazz = o1.getClass();

                    if (Boolean.class.isAssignableFrom(clazz)) {
                        return (Boolean) o1;
                    }

                    if (Iterable.class.isAssignableFrom(clazz)) {
                        return Iterables.size((Iterable<?>) o1) > 0;
                    }

                            if (clazz.isArray()) {
                                return Array.getLength(o1) > 0;
                            }
                    return true;
                })
                .orElse(false));
    }

    private Presence(SequentialGetStepSupplier<T, ?, ?, ?, ?> toBePresent) {
        this();
        from(turnReportingOff(toBePresent.clone()));
    }

    private Presence(Function<T, ?> toBePresent) {
        this();
        StepFunction<T, ?> expectedToBePresent;
        if (StepFunction.class.isAssignableFrom(toBePresent.getClass())) {
            expectedToBePresent = ((StepFunction<T, ?>) toBePresent);
        } else {
            expectedToBePresent = new StepFunction<>(isLoggable(toBePresent) ?
                    toBePresent.toString() :
                    "<not described value>",
                    toBePresent);
        }
        from(expectedToBePresent.turnReportingOff());
    }

    /**
     * Creates an instance of {@link Presence}.
     *
     * @param function that should return something. If the result of {@link Function#apply(Object)} is not {@code null},
     *                 it is not an empty iterable/array or it is {@link Boolean} {@code true} then this is considered present.
     * @param <T>      is a type of {@link Context}
     * @return an instance of {@link Presence}.
     */
    @Description("Presence of {toBePresent}")
    public static <T extends Context<?>> Presence<T> presence(@DescriptionFragment("toBePresent") Function<T, ?> function) {
        checkArgument(nonNull(function), "Function should not be a null-value");
        return new Presence<>(function);
    }

    /**
     * Creates an instance of {@link Presence}.
     *
     * @param toBePresent as a supplier of a function. If the result of {@link Function#apply(Object)} is not {@code null},
     *                    it is not an empty iterable/array or it is {@link Boolean} {@code true} then this is considered present.
     * @param <T>         is a type of {@link Context}
     * @return an instance of {@link Presence}.
     */
    @Description("Presence of {toBePresent}")
    public static <T extends Context<?>> Presence<T> presence(@DescriptionFragment("toBePresent") SequentialGetStepSupplier<T, ?, ?, ?, ?> toBePresent) {
        checkArgument(nonNull(toBePresent), "Supplier of a function should not be a null-value");
        return new Presence<>(toBePresent);
    }

    protected Function<T, Object> preparePreFunction() {
        var preFunction = super.preparePreFunction();
        if (StepFunction.class.isAssignableFrom(preFunction.getClass())) {
            ((StepFunction<?, ?>) preFunction).addIgnored(ignored2);
        }
        return ((Function<Object, Object>) o -> ofNullable(o).orElse(false))
                .compose(preFunction);
    }

    protected Function<Object, Boolean> getEndFunction() {
        return o -> {
            var result = super.getEndFunction().apply(o);
            if (!result) {
                ofNullable(exceptionSupplier)
                        .ifPresent(supplier -> {
                            throw supplier.get();
                        });
                return false;
            }
            return true;
        };
    }

    @Override
    public Presence<T> addIgnored(Collection<Class<? extends Throwable>> toBeIgnored) {
        ignored2.addAll(toBeIgnored);
        return this;
    }

    @Override
    public Presence<T> addIgnored(Class<? extends Throwable> toBeIgnored) {
        ignored2.add(toBeIgnored);
        return this;
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
