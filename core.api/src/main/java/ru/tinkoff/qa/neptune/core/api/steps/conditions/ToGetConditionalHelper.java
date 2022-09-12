package ru.tinkoff.qa.neptune.core.api.steps.conditions;

import com.google.common.collect.Iterables;
import org.apache.commons.lang3.ArrayUtils;
import ru.tinkoff.qa.neptune.core.api.steps.ExceptionSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.InvalidStepResultException;

import javax.annotation.Nullable;
import java.time.Duration;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static com.google.common.collect.Lists.newArrayList;
import static java.lang.System.currentTimeMillis;
import static java.lang.Thread.currentThread;
import static java.lang.Thread.sleep;
import static java.time.Duration.ofMillis;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;

/**
 * This is the util which helps to crate function with given conditions.
 */
@SuppressWarnings("unchecked")
public final class ToGetConditionalHelper {

    public static final Predicate<?> AS_IS = t -> true;
    private static final Predicate<?> NON_NULL = Objects::nonNull;

    private ToGetConditionalHelper() {
        super();
    }

    static <T> Predicate<T> notNullAnd(Predicate<? super T> condition) {
        return ((Predicate<T>) NON_NULL).and(condition);
    }

    static boolean printErrorAndFalse(Throwable t) {
        System.err.println(t.getMessage());
        return false;
    }

    private static boolean toBeIgnored(Throwable throwable, Collection<Class<? extends Throwable>> toIgnore) {
        var cls = throwable.getClass();
        return toIgnore
                .stream()
                .anyMatch(aClass -> aClass.isAssignableFrom(cls));
    }

    private static <T, F> Function<T, F> fluentWaitFunction(Function<T, F> originalFunction,
                                                            @Nullable Duration waitingTime,
                                                            @Nullable Duration sleepingTime,
                                                            Predicate<F> till,
                                                            ResultSelection<?, F> selection,
                                                            @Nullable Supplier<? extends RuntimeException> exceptionOnTimeOut,
                                                            Collection<Class<? extends Throwable>> toIgnore) {
        var timeOut = ofNullable(waitingTime).orElseGet(() -> ofMillis(0));
        var sleeping = ofNullable(sleepingTime).orElseGet(() -> ofMillis(10));
        ofNullable(selection).ifPresent(ResultSelection::setNotValidated);

        return t -> {
            Throwable lastCaught = null;
            var currentMillis = currentTimeMillis();
            var endMillis = currentMillis + timeOut.toMillis();
            F f = null;
            var suitable = false;
            while (currentMillis <= endMillis && !(suitable)) {
                lastCaught = null;
                try {
                    ofNullable(selection).ifPresent(ResultSelection::clear);
                    f = originalFunction.apply(t);
                } catch (Throwable throwable) {
                    if (toBeIgnored(throwable, toIgnore)) {
                        f = null;
                        lastCaught = throwable;
                        throwable.printStackTrace();
                    } else {
                        throw throwable;
                    }
                }

                suitable = till.test(f);
                try {
                    sleep(sleeping.toMillis());
                } catch (InterruptedException e) {
                    currentThread().interrupt();
                    throw new RuntimeException(e);
                }
                currentMillis = currentTimeMillis();
            }

            if (suitable) {
                return f;
            }

            if (!isNull(exceptionOnTimeOut) || !isNull(selection)) {
                if (nonNull(selection) && selection.isChecked()) {
                    var mismatch = selection.mismatchMessage();
                    throw new InvalidStepResultException(mismatch, lastCaught);
                }

                if (nonNull(exceptionOnTimeOut)) {
                    if (exceptionOnTimeOut instanceof ExceptionSupplier) {
                        ((ExceptionSupplier) exceptionOnTimeOut).setCause(lastCaught);
                    }
                    throw exceptionOnTimeOut.get();
                }

            }
            return f;
        };
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
    public static <T, R> Function<T, R> getSingle(Function<T, R> function,
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
                sleepingTime, Objects::nonNull, null, exceptionSupplier, toIgnore);
    }

    /**
     * This method returns a function. The result function returns immutable {@link List} of elements which differ from null
     * and suit the criteria.
     *
     * @param function          function which should return {@link Iterable}
     * @param condition         predicate which is used to find some target value
     * @param resultSelection   rule to return the result
     * @param waitingTime       is a duration of the waiting for valuable result
     * @param sleepingTime      is a duration of the sleeping between attempts to get
     *                          expected valuable result
     * @param exceptionSupplier is a supplier which returns the exception to be thrown on the waiting time
     *                          expiration
     * @param toIgnore          classes of exception to be ignored during execution
     * @param <T>               is a type of input value
     * @param <R>               is a type of target values
     * @param <V>               is a type of {@link Iterable} of {@code R}
     * @return a function. The result function returns immutable {@link List} of elements which differ from null
     * and suit the criteria. It returns not empty iterable when there are such elements. Some exception is thrown if result
     * iterable is null or has no elements which suit the criteria.
     */
    public static <T, R, V extends Iterable<R>> Function<T, List<R>> getList(Function<T, V> function,
                                                                             Predicate<? super R> condition,
                                                                             ResultSelection<List<R>, List<R>> resultSelection,
                                                                             @Nullable Duration waitingTime,
                                                                             @Nullable Duration sleepingTime,
                                                                             @Nullable Supplier<? extends RuntimeException> exceptionSupplier,
                                                                             Collection<Class<? extends Throwable>> toIgnore) {
        return fluentWaitFunction(t ->
                        ofNullable(function.apply(t)).map(v -> {
                            var arrayList = newArrayList(v);
                            var result = arrayList.stream().filter(r -> {
                                try {
                                    return notNullAnd(condition).test(r);
                                } catch (Throwable t1) {
                                    return printErrorAndFalse(t1);
                                }
                            }).collect(toList());

                            if (isNull(resultSelection)) {
                                return new ImmutableArrayList<>(result);
                            }

                            return ofNullable(resultSelection.evaluate(result))
                                    .map(ImmutableArrayList::new)
                                    .orElse(null);
                        }).orElse(null),
                waitingTime,
                sleepingTime,
                v -> nonNull(v) && Iterables.size(v) > 0,
                resultSelection,
                exceptionSupplier,
                toIgnore);

    }

    /**
     * This method returns a function. The result function returns an array of elements which differ from null
     * and suit the criteria.
     *
     * @param function          function which should return an array
     * @param condition         predicate which is used to find some target value
     * @param resultSelection   rule to return the result
     * @param waitingTime       is a duration of the waiting for valuable result
     * @param sleepingTime      is a duration of the sleeping between attempts to get
     *                          expected valuable result
     * @param exceptionSupplier is a supplier which returns the exception to be thrown on the waiting time
     *                          expiration
     * @param toIgnore          classes of exception to be ignored during execution
     * @param <T>               is a type of input value
     * @param <R>               is a type of target values
     * @return a function. The result function returns an array of elements which differ from null
     * and suit the criteria. It returns not empty array when there are such elements. Some exception is thrown if result
     * array is null or has no elements which suit the criteria.
     */
    public static <T, R> Function<T, R[]> getArray(Function<T, R[]> function,
                                                   Predicate<? super R> condition,
                                                   ResultSelection<R[], R[]> resultSelection,
                                                   Duration waitingTime,
                                                   Duration sleepingTime,
                                                   Supplier<? extends RuntimeException> exceptionSupplier,
                                                   Collection<Class<? extends Throwable>> toIgnore) {
        return fluentWaitFunction(t ->
                        ofNullable(function.apply(t)).map(rs -> {
                            var subResult = Arrays.stream(rs).filter(r -> {
                                try {
                                    return notNullAnd(condition).test(r);
                                } catch (Throwable t1) {
                                    return printErrorAndFalse(t1);
                                }
                            }).collect(toList());

                            R[] result = rs;
                            result = ArrayUtils.removeElements(result, rs);
                            for (R r : subResult) {
                                result = ArrayUtils.add(result, r);
                            }

                            if (isNull(resultSelection)) {
                                return result;
                            }

                            return resultSelection.evaluate(result);
                        }).orElse(null),
                waitingTime,
                sleepingTime,
                rs -> nonNull(rs) && rs.length > 0,
                resultSelection,
                exceptionSupplier,
                toIgnore);
    }

    /**
     * This method returns a function. The result function returns a single {@link Iterable} item that
     * suits criteria.
     *
     * @param function          function which should return {@link Iterable}
     * @param condition         predicate which is used to find some target value
     * @param resultSelection   rule to return the result
     * @param waitingTime       is a duration of the waiting for valuable result
     * @param sleepingTime      is a duration of the sleeping between attempts to get
     *                          expected valuable result
     * @param exceptionSupplier is a supplier which returns the exception to be thrown on the waiting time
     *                          expiration
     * @param toIgnore          classes of exception to be ignored during execution
     * @param <T>               is a type of input value
     * @param <R>               is a type of the target value
     * @param <V>               is a type of {@link Iterable} of {@code R}
     * @return a function. The result function returns a single first found value from {@link Iterable}.
     * It returns a value if something that suits criteria is found. Some exception is thrown if
     * result iterable to get value from is null or has zero-size or it has no item which suits criteria.
     */
    public static <T, R, V extends Iterable<R>> Function<T, R> getFromIterable(Function<T, V> function,
                                                                               Predicate<? super R> condition,
                                                                               ResultSelection<Iterable<R>, R> resultSelection,
                                                                               @Nullable Duration waitingTime,
                                                                               @Nullable Duration sleepingTime,
                                                                               @Nullable Supplier<? extends RuntimeException> exceptionSupplier,
                                                                               Collection<Class<? extends Throwable>> toIgnore) {
        return fluentWaitFunction(t ->
                        ofNullable(function.apply(t))
                                .map(rs -> {
                                    var arrayList = newArrayList(rs);
                                    var found = arrayList.stream().filter(r -> {
                                        try {
                                            return notNullAnd(condition).test(r);
                                        } catch (Throwable t1) {
                                            return printErrorAndFalse(t1);
                                        }
                                    }).collect(toList());

                                    if (isNull(resultSelection)) {
                                        if (!found.isEmpty()) {
                                            return found.get(0);
                                        }
                                        return null;
                                    }

                                    return resultSelection.evaluate(found);

                                })
                                .orElse(null),
                waitingTime,
                sleepingTime,
                Objects::nonNull,
                resultSelection,
                exceptionSupplier,
                toIgnore);
    }

    /**
     * This method returns a function. The result function returns a single array item that
     * suits criteria.
     *
     * @param function          function which should return an array
     * @param condition         predicate which is used to find some target value
     * @param resultSelection   rule to return the result
     * @param waitingTime       is a duration of the waiting for valuable result
     * @param sleepingTime      is a duration of the sleeping between attempts to get
     *                          expected valuable result
     * @param exceptionSupplier is a supplier which returns the exception to be thrown on the waiting time
     *                          expiration
     * @param toIgnore          classes of exception to be ignored during execution
     * @param <T>               is a type of input value
     * @param <R>               is a type of the target value
     * @return a function. The result function returns a single first found value from array.
     * It returns a value if something that suits criteria is found. Some exception is thrown if
     * result array to get value from is null or has zero-length or it has no item which suits criteria.
     */
    public static <T, R> Function<T, R> getFromArray(Function<T, R[]> function,
                                                     Predicate<? super R> condition,
                                                     ResultSelection<R[], R> resultSelection,
                                                     Duration waitingTime,
                                                     Duration sleepingTime,
                                                     Supplier<? extends RuntimeException> exceptionSupplier,
                                                     Collection<Class<? extends Throwable>> toIgnore) {
        return fluentWaitFunction(t ->
                        ofNullable(function.apply(t)).map(rs -> {
                            var found = Arrays.stream(rs).filter(r -> {
                                try {
                                    return notNullAnd(condition).test(r);
                                } catch (Throwable t1) {
                                    return printErrorAndFalse(t1);
                                }
                            }).collect(toList());

                            if (isNull(resultSelection)) {
                                if (!found.isEmpty()) {
                                    return found.get(0);
                                }
                                return null;
                            }

                            R[] result = rs;
                            result = ArrayUtils.removeElements(result, rs);
                            for (R r : found) {
                                result = ArrayUtils.add(result, r);
                            }

                            return resultSelection.evaluate(result);
                        }).orElse(null),
                waitingTime,
                sleepingTime,
                Objects::nonNull,
                resultSelection,
                exceptionSupplier,
                toIgnore);
    }
}
