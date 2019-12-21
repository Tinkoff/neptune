package ru.tinkoff.qa.neptune.core.api.steps;

import com.google.common.collect.Iterables;
import ru.tinkoff.qa.neptune.core.api.steps.context.Context;

import java.lang.reflect.Array;
import java.time.Duration;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.String.format;
import static java.time.Duration.ofMillis;
import static java.util.Objects.nonNull;
import static java.util.Optional.ofNullable;
import static ru.tinkoff.qa.neptune.core.api.event.firing.StaticEventFiring.fireReturnedValue;
import static ru.tinkoff.qa.neptune.core.api.steps.conditions.ToGetSingleCheckedObject.getSingle;
import static ru.tinkoff.qa.neptune.core.api.utils.IsLoggableUtil.isLoggable;

@SuppressWarnings("unchecked")
public final class Absence<T extends Context> extends SequentialGetStepSupplier.GetObjectChainedStepSupplier<T, Boolean, Object, Absence<T>> {

    private Object received;

    private Absence(Function<T, ?> toBeAbsent) {
        super(format("Absence of [%s]", isLoggable(toBeAbsent) ? toBeAbsent.toString() : "<not described value>"),
                o -> ofNullable(o)
                        .map(o1 -> {
                            Class<?> clazz = o1.getClass();
                            if (Boolean.class.isAssignableFrom(clazz)) {
                                return (Boolean) o;
                            }
                            return false;
                        })
                        .orElse(false));

        StepFunction<T, ?> expectedToBeAbsent;
        if (StepFunction.class.isAssignableFrom(toBeAbsent.getClass())) {
            expectedToBeAbsent = ((StepFunction<T, ?>) toBeAbsent);
        }
        else {
            expectedToBeAbsent = new StepFunction<>(isLoggable(toBeAbsent) ?
                    toBeAbsent.toString() :
                    "<not described value>",
                    toBeAbsent);
        }
        from(expectedToBeAbsent.turnReportingOff()
                .addIgnored(Throwable.class));
    }

    private Absence(SequentialGetStepSupplier<T, ?, ?, ?, ?> toBeAbsent) {
        this(toBeAbsent.clone().timeOut(ofMillis(0))
                .pollingInterval(ofMillis(0))
                .get());
    }

    /**
     * Creates an instance of {@link Absence}.
     *
     * @param function that should return something. If the result of {@link Function#apply(Object)} is {@code null},
     *                 it is an empty iterable/array or it is {@link Boolean} {@code false} then this is considered absent.
     * @param <T>      is a type of {@link Context}
     * @return an instance of {@link Absence}.
     */
    public static <T extends Context> Absence<T> absence(Function<T, ?> function) {
        checkArgument(nonNull(function), "Function should not be a null-value");
        return new Absence<>(function);
    }

    /**
     * Creates an instance of {@link Absence}.
     *
     * @param toBeAbsent as a supplier of a function. If the result of {@link Function#apply(Object)} is {@code null},
     *                    it is an empty iterable/array or it is {@link Boolean} {@code false} then this is considered absent.
     * @param <T>         is a type of {@link Context}
     * @return an instance of {@link Absence}.
     */
    public static <T extends Context> Absence<T> absence(SequentialGetStepSupplier<T, ?, ?, ?, ?> toBeAbsent) {
        checkArgument(nonNull(toBeAbsent), "Supplier of a function should not be a null-value");
        return new Absence<>(toBeAbsent);
    }

    protected Function<T, Object> preparePreFunction() {
        var preFunction = super.preparePreFunction();

        var getAbsence = new Function<T, Object>() {
            @Override
            public Object apply(T t) {
                received = null;
                var result = preFunction.apply(t);
                received = result;

                if (result == null) {
                    return true;
                }

                Class<?> clazz = result.getClass();

                if (Boolean.class.isAssignableFrom(clazz)) {
                    if (result.equals(false)) {
                        return true;
                    }
                }

                if (Iterable.class.isAssignableFrom(clazz)) {
                    if (Iterables.size((Iterable) result) == 0) {
                        return true;
                    }
                }

                if (clazz.isArray()) {
                    if (Array.getLength(result) == 0) {
                        return true;
                    }
                }

                return null;
            }
        };

        var resulted = getSingle(getAbsence, timeToGet);
        return ((Function<Object, Object>) o -> ofNullable(o).orElseGet(() -> {
            fireReturnedValue(received);
            return false;
        })).compose(resulted);
    }

    protected Function<Object, Boolean> getEndFunction() {
        return o -> {
            var result = super.getEndFunction().apply(o);
            if (!result) {
                ofNullable(exceptionSupplier).ifPresent(supplier -> {
                    throw supplier.get();
                });
                return false;
            }
            return true;
        };
    }

    protected String prepareStepDescription() {
        return toString();
    }

    /**
     * This method defines the time to wait for the value is absent.
     * <p>WARNING!</p>
     * When there is a function that is used to find a value to be absent and it was received by
     * invocation of {@link SequentialGetStepSupplier#get()} and there is a timeout defined by
     * {@link SequentialGetStepSupplier#timeOut(Duration)} then this timeout is ignored in favor of the time value
     * defined by this method
     *
     * @param timeOut is a time duration to get the value is absent or not
     * @return self-reference
     */
    @Override
    public Absence<T> timeOut(Duration timeOut) {
        return super.timeOut(timeOut);
    }

    /**
     * This method defines an exception to be thrown when value to be absent is here still.
     *
     * @param exceptionMessage is a message of {@link IllegalStateException} to be thrown when value to be absent is still here.
     * @return self-reference
     */
    public Absence<T> throwIfPresent(String exceptionMessage) {
        return throwOnEmptyResult(() -> new IllegalStateException(exceptionMessage));
    }
}
