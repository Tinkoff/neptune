package ru.tinkoff.qa.neptune.core.api.steps;

import com.google.common.collect.Iterables;
import ru.tinkoff.qa.neptune.core.api.event.firing.Captor;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotations.MaxDepthOfReporting;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.IncludeParamsOfInnerGetterStep;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.ThrowWhenNoData;
import ru.tinkoff.qa.neptune.core.api.steps.context.Context;

import java.lang.reflect.Array;
import java.time.Duration;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

import static com.google.common.base.Preconditions.checkArgument;
import static java.time.Duration.ofMillis;
import static java.util.Objects.nonNull;
import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.StringUtils.SPACE;
import static ru.tinkoff.qa.neptune.core.api.event.firing.StaticEventFiring.catchValue;
import static ru.tinkoff.qa.neptune.core.api.steps.conditions.ToGetSingleCheckedObject.getSingle;

@SuppressWarnings("unchecked")
@SequentialGetStepSupplier.DefineTimeOutParameterName("Time of the waiting for absence")
@SequentialGetStepSupplier.DefineResultDescriptionParameterName("Is absent")
@IncludeParamsOfInnerGetterStep
@MaxDepthOfReporting(0)
@SequentialGetStepSupplier.DefineGetImperativeParameterName("Wait for:")
@ThrowWhenNoData(toThrow = StillPresentException.class, startDescription = "Still present:")
public final class Absence<T> extends SequentialGetStepSupplier.GetObjectChainedStepSupplier<T, Boolean, Object, Absence<T>> {

    private final Set<Captor<Object, Object>> successCaptors = new HashSet<>();
    private Object lastCaught;

    private Absence() {
        super(o -> ofNullable(o)
                .map(o1 -> {
                    Class<?> clazz = o1.getClass();
                    if (Boolean.class.isAssignableFrom(clazz)) {
                        return (Boolean) o;
                    }
                    return false;
                })
                .orElse(false));
    }

    private Absence(SequentialGetStepSupplier<?, ?, ?, ?, ?> toBeAbsent) {
        this();
        from(turnReportingOff(toBeAbsent.clone().timeOut(ofMillis(0))
                .pollingInterval(ofMillis(0))
                .addIgnored(Throwable.class)));

        successCaptors.addAll(toBeAbsent.successCaptors);
    }

    /**
     * Creates an instance of {@link Absence}.
     *
     * @param toBeAbsent as a supplier of a function. If the result of {@link Function#apply(Object)} is {@code null},
     *                   it is an empty iterable/array or it is {@link Boolean} {@code false} then this is considered absent.
     * @param <T>        is a type of {@link Context}
     * @return an instance of {@link Absence}.
     */
    @Description("Absence of {toBeAbsent}")
    public static <T> Absence<T> absence(@DescriptionFragment("toBeAbsent") SequentialGetStepSupplier<T, ?, ?, ?, ?> toBeAbsent) {
        checkArgument(nonNull(toBeAbsent), "Supplier of a function should not be a null-value");
        return new Absence<>(toBeAbsent);
    }

    @Override
    String getExceptionMessage(String messageStarting) {
        var stringBuilder = new StringBuilder(messageStarting)
                .append(SPACE)
                .append(((SequentialGetStepSupplier<?, ?, ?, ?, ?>) from).getDescription());
        getParameters().forEach((key, value) -> stringBuilder.append("\r\n")
                .append("- ")
                .append(key)
                .append(":")
                .append(value));
        return stringBuilder.toString();
    }

    protected Function<T, Object> preparePreFunction() {
        var preFunction = super.preparePreFunction();

        var resulted = getSingle((Function<T, Object>) t -> {
            lastCaught = null;

            Object result;
            try {
                result = preFunction.apply(t);
            } catch (Throwable thrown) {
                return true;
            }

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
                if (Iterables.size((Iterable<?>) result) == 0) {
                    return true;
                }
            }

            if (clazz.isArray()) {
                if (Array.getLength(result) == 0) {
                    return true;
                }
            }

            lastCaught = result;
            return null;
        }, timeToGet);
        return ((Function<Object, Object>) o -> ofNullable(o).orElse(false)).compose(resulted);
    }

    @Override
    protected void onSuccess(Boolean result) {
        if (!result) {
            if (lastCaught != null) {
                catchValue(lastCaught, successCaptors);
            }

            ofNullable(exceptionSupplier).ifPresent(supplier -> {
                throw supplier.get();
            });
        }
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
}
