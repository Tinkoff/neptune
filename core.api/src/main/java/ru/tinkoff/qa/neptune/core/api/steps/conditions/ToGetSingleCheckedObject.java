package ru.tinkoff.qa.neptune.core.api.steps.conditions;

import javax.annotation.Nullable;
import java.time.Duration;
import java.util.Collection;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static java.util.Arrays.asList;
import static java.util.Optional.ofNullable;
import static ru.tinkoff.qa.neptune.core.api.steps.conditions.ToGetConditionalHelper.*;

@SuppressWarnings("unchecked")
public final class ToGetSingleCheckedObject {
    private ToGetSingleCheckedObject() {
        super();
    }

    private static <T, R> Function<T, R> checkedSingle(Function<T, R> function,
                                                       Predicate<? super R> condition,
                                                       @Nullable Duration waitingTime,
                                                       @Nullable Duration sleepingTime,
                                                       @Nullable Supplier<? extends RuntimeException> exceptionSupplier,
                                                       Collection<Class<? extends Throwable>> toIgnore) {
        return fluentWaitFunction(t ->
                        ofNullable(function.apply(t)).map(r -> {
                            try {
                                if (notNullAnd(condition).test(r)) {
                                    return r;
                                }
                            } catch (Throwable t1) {
                                printErrorAndFalse(t1);
                            }
                            return null;
                        }).orElse(null), waitingTime,
                sleepingTime, Objects::nonNull, exceptionSupplier, toIgnore);
    }

    /**
     * This method returns a function. The result function returns a single value which
     * suits criteria.
     *
     * @param function          function which should return some object
     * @param condition         predicate which is used to find some target value
     * @param waitingTime       is a duration of the waiting for valuable result
     * @param sleepingTime      is a duration of the sleeping between attempts to get
     *                          expected valuable result
     * @param toIgnore          classes of exception to be ignored during execution
     * @param exceptionSupplier is a supplier which returns the exception to be thrown on the waiting time
     *                          expiration
     * @param <T>               is a type of input value
     * @param <R>               is a type of the target value
     * @return a function. The result function returns a single value.
     * It returns a value if it suits criteria. Some exception is thrown if value is null or doesn't suit criteria.
     */
    @SafeVarargs
    public static <T, R> Function<T, R> getSingle(Function<T, R> function,
                                                  Predicate<? super R> condition,
                                                  Duration waitingTime,
                                                  Duration sleepingTime,
                                                  Supplier<? extends RuntimeException> exceptionSupplier,
                                                  Class<? extends Throwable>... toIgnore) {
        return checkedSingle(checkFunction(function),
                checkCondition(condition),
                checkWaitingTime(waitingTime),
                checkSleepingTime(sleepingTime),
                checkExceptionSupplier(exceptionSupplier),
                asList(toIgnore));
    }

    /**
     * This method returns a function. The result function returns a single value which differs from null.
     *
     * @param function          function which should return some object
     * @param waitingTime       is a duration of the waiting for valuable result
     * @param sleepingTime      is a duration of the sleeping between attempts to get
     *                          expected valuable result
     * @param toIgnore          classes of exception to be ignored during execution
     * @param exceptionSupplier is a supplier which returns the exception to be thrown on the waiting time
     *                          expiration
     * @param <T>               is a type of input value
     * @param <R>               is a type of the target value
     * @return a function. The result function returns a single value.
     * It returns a value if it differs from null. Some exception is thrown if value is null.
     */
    @SafeVarargs
    public static <T, R> Function<T, R> getSingle(Function<T, R> function,
                                                  Duration waitingTime, Duration sleepingTime,
                                                  Supplier<? extends RuntimeException> exceptionSupplier,
                                                  Class<? extends Throwable>... toIgnore) {
        return checkedSingle(checkFunction(function),
                (Predicate<? super R>) AS_IS,
                checkWaitingTime(waitingTime),
                checkSleepingTime(sleepingTime),
                checkExceptionSupplier(exceptionSupplier),
                asList(toIgnore));
    }

    /**
     * This method returns a function. The result function returns a single value which
     * suits criteria.
     *
     * @param function          function which should return some object
     * @param condition         predicate which is used to find some target value
     * @param waitingTime       is a duration of the waiting for valuable result
     * @param exceptionSupplier is a supplier which returns the exception to be thrown on the waiting time
     *                          expiration
     * @param toIgnore          classes of exception to be ignored during execution
     * @param <T>               is a type of input value
     * @param <R>               is a type of the target value
     * @return a function. The result function returns a single value.
     * It returns a value if it suits criteria. Some exception is thrown if value is null or doesn't suit criteria.
     */
    @SafeVarargs
    public static <T, R> Function<T, R> getSingle(Function<T, R> function,
                                                  Predicate<? super R> condition,
                                                  Duration waitingTime,
                                                  Supplier<? extends RuntimeException> exceptionSupplier,
                                                  Class<? extends Throwable>... toIgnore) {
        return checkedSingle(checkFunction(function),
                checkCondition(condition),
                checkWaitingTime(waitingTime),
                null,
                checkExceptionSupplier(exceptionSupplier),
                asList(toIgnore));
    }

    /**
     * This method returns a function. The result function returns a single value which differs from null.
     *
     * @param function          function which should return some object
     * @param waitingTime       is a duration of the waiting for valuable result
     * @param exceptionSupplier is a supplier which returns the exception to be thrown on the waiting time
     *                          expiration
     * @param toIgnore          classes of exception to be ignored during execution
     * @param <T>               is a type of input value
     * @param <R>               is a type of the target value
     * @return a function. The result function returns a single value.
     * It returns a value if it differs from null. Some exception is thrown if value is null.
     */
    @SafeVarargs
    public static <T, R> Function<T, R> getSingle(Function<T, R> function,
                                                  Duration waitingTime,
                                                  Supplier<? extends RuntimeException> exceptionSupplier,
                                                  Class<? extends Throwable>... toIgnore) {
        return checkedSingle(checkFunction(function),
                (Predicate<? super R>) AS_IS,
                checkWaitingTime(waitingTime),
                null,
                checkExceptionSupplier(exceptionSupplier),
                asList(toIgnore));
    }

    /**
     * This method returns a function. The result function returns a single value which
     * suits criteria.
     *
     * @param function          function which should return some object
     * @param condition         predicate which is used to find some target value
     * @param exceptionSupplier is a supplier which returns the exception to be thrown on the waiting time
     *                          expiration
     * @param toIgnore          classes of exception to be ignored during execution
     * @param <T>               is a type of input value
     * @param <R>               is a type of the target value
     * @return a function. The result function returns a single value.
     * It returns a value if it suits criteria. Some exception is thrown if value is null or doesn't suit criteria.
     */
    @SafeVarargs
    public static <T, R> Function<T, R> getSingle(Function<T, R> function,
                                                  Predicate<? super R> condition,
                                                  Supplier<? extends RuntimeException> exceptionSupplier,
                                                  Class<? extends Throwable>... toIgnore) {
        return checkedSingle(checkFunction(function),
                checkCondition(condition), null,
                null,
                checkExceptionSupplier(exceptionSupplier),
                asList(toIgnore));
    }

    /**
     * This method returns a function. The result function returns a single value which differs from null.
     *
     * @param function          function which should return some object
     * @param exceptionSupplier is a supplier which returns the exception to be thrown on the waiting time
     *                          expiration
     * @param toIgnore          classes of exception to be ignored during execution
     * @param <T>               is a type of input value
     * @param <R>               is a type of the target value
     * @return a function. The result function returns a single value.
     * It returns a value if it differs from null. Some exception is thrown if value is null.
     */
    @SafeVarargs
    public static <T, R> Function<T, R> getSingle(Function<T, R> function,
                                                  Supplier<? extends RuntimeException> exceptionSupplier,
                                                  Class<? extends Throwable>... toIgnore) {
        return checkedSingle(checkFunction(function),
                (Predicate<? super R>) AS_IS, null,
                null,
                checkExceptionSupplier(exceptionSupplier),
                asList(toIgnore));
    }

    /**
     * This method returns a function. The result function returns a single value which
     * suits criteria.
     *
     * @param function     function which should return some object
     * @param condition    predicate which is used to find some target value
     * @param waitingTime  is a duration of the waiting for valuable result
     * @param sleepingTime is a duration of the sleeping between attempts to get
     *                     expected valuable result
     * @param toIgnore     classes of exception to be ignored during execution
     * @param <T>          is a type of input value
     * @param <R>          is a type of the target value
     * @return a function. The result function returns a single value.
     * It returns a value if it suits criteria. {@code null} is returned if value is null or doesn't suit criteria.
     */
    @SafeVarargs
    public static <T, R> Function<T, R> getSingle(Function<T, R> function,
                                                  Predicate<? super R> condition,
                                                  Duration waitingTime,
                                                  Duration sleepingTime,
                                                  Class<? extends Throwable>... toIgnore) {
        return checkedSingle(checkFunction(function),
                checkCondition(condition),
                checkWaitingTime(waitingTime),
                checkSleepingTime(sleepingTime),
                null,
                asList(toIgnore));
    }

    /**
     * This method returns a function. The result function returns a single value which differs from null.
     *
     * @param function     function which should return some object
     * @param waitingTime  is a duration of the waiting for valuable result
     * @param sleepingTime is a duration of the sleeping between attempts to get
     *                     expected valuable result
     * @param toIgnore          classes of exception to be ignored during execution
     * @param <T>          is a type of input value
     * @param <R>          is a type of the target value
     * @return a function. The result function returns a single value.
     * It returns a value if it differs from null. {@code null} is returned if value is null.
     */
    @SafeVarargs
    public static <T, R> Function<T, R> getSingle(Function<T, R> function,
                                                  Duration waitingTime,
                                                  Duration sleepingTime,
                                                  Class<? extends Throwable>... toIgnore) {
        return checkedSingle(checkFunction(function),
                (Predicate<? super R>) AS_IS,
                checkWaitingTime(waitingTime),
                checkSleepingTime(sleepingTime),
                null,
                asList(toIgnore));
    }

    /**
     * This method returns a function. The result function returns a single value which
     * suits criteria.
     *
     * @param function    function which should return some object
     * @param condition   predicate which is used to find some target value
     * @param waitingTime is a duration of the waiting for valuable result
     * @param toIgnore          classes of exception to be ignored during execution
     * @param <T>         is a type of input value
     * @param <R>         is a type of the target value
     * @return a function. The result function returns a single value.
     * It returns a value if it suits criteria. {@code null} is returned if value is null or doesn't suit criteria.
     */
    @SafeVarargs
    public static <T, R> Function<T, R> getSingle(Function<T, R> function,
                                                  Predicate<? super R> condition,
                                                  Duration waitingTime,
                                                  Class<? extends Throwable>... toIgnore) {
        return checkedSingle(checkFunction(function),
                checkCondition(condition),
                checkWaitingTime(waitingTime),
                null,
                null,
                asList(toIgnore));
    }

    /**
     * TThis method returns a function. The result function returns a single value which differs from null.
     *
     * @param function    function which should return some object
     * @param waitingTime is a duration of the waiting for valuable result
     * @param toIgnore          classes of exception to be ignored during execution
     * @param <T>         is a type of input value
     * @param <R>         is a type of the target value
     * @return a function. The result function returns a single value.
     * It returns a value if it differs from null. {@code null} is returned if value is null.
     */
    @SafeVarargs
    public static <T, R> Function<T, R> getSingle(Function<T, R> function,
                                                  Duration waitingTime,
                                                  Class<? extends Throwable>... toIgnore) {
        return checkedSingle(checkFunction(function),
                (Predicate<? super R>) AS_IS,
                checkWaitingTime(waitingTime),
                null,
                null,
                asList(toIgnore));
    }

    /**
     * This method returns a function. The result function returns a single value which
     * suits criteria.
     *
     * @param function  which should return a value to check.
     * @param condition which is used to check the target value.
     * @param toIgnore          classes of exception to be ignored during execution
     * @param <T>       is a type of input value
     * @param <R>       is a type of the target value
     * @return a function. The result function returns a single value.
     * It returns a value if it suits criteria. {@code null} is returned if value is null or doesn't suit criteria.
     */
    @SafeVarargs
    public static <T, R> Function<T, R> getSingle(Function<T, R> function,
                                                  Predicate<? super R> condition,
                                                  Class<? extends Throwable>... toIgnore) {
        return checkedSingle(checkFunction(function),
                checkCondition(condition),
                null, null, null,
                asList(toIgnore));
    }

    /**
     * This method returns a function. The result function returns a single value which differs from {@code null}
     *
     * @param function which should return a value to check.
     * @param toIgnore classes of exception to be ignored during execution
     * @param <T>      is a type of input value
     * @param <R>      is a type of the target value
     * @return a function. The result function returns a single value.
     * It returns a value if it suits criteria. {@code null} is returned if value is null or doesn't suit criteria.
     */
    @SafeVarargs
    public static <T, R> Function<T, R> getSingle(Function<T, R> function,
                                                  Class<? extends Throwable>... toIgnore) {
        return checkedSingle(checkFunction(function),
                (Predicate<? super R>) AS_IS,
                null, null, null,
                asList(toIgnore));
    }
}
