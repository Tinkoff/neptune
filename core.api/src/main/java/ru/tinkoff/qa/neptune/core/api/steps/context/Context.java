package ru.tinkoff.qa.neptune.core.api.steps.context;

import ru.tinkoff.qa.neptune.core.api.steps.SequentialActionSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;

import java.time.Duration;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.List.of;
import static ru.tinkoff.qa.neptune.core.api.steps.Absence.absence;
import static ru.tinkoff.qa.neptune.core.api.steps.Presence.presence;

/**
 * /**
 * This class describes something that contains resources for the step performing.
 * These steps are described by {@link ru.tinkoff.qa.neptune.core.api.steps.SequentialActionSupplier} and
 * {@link ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier} generally. "Resources" are opened
 * data base connections, opened browsers etc.
 *
 * @param <THIS> is a type of a class that extends {@link Context}
 */
@SuppressWarnings("unchecked")
public abstract class Context<THIS extends Context<THIS>> {

    private static List<Class<? extends Throwable>> ignoredExceptions(Class<? extends Throwable>... toIgnore) {
        List<Class<? extends Throwable>> ignored;
        if (toIgnore != null && toIgnore.length > 0) {
            ignored = asList(toIgnore);
        } else {
            ignored = of();
        }
        return ignored;
    }

    /**
     * Auxiliary method that performs some get-step and then returns a result.
     *
     * @param toGet is a supplier of a get-step to be performed
     * @param <T>   is a type of a resulted value
     * @return a result of the performing of a get-step
     */
    protected final <T> T get(SequentialGetStepSupplier<? super THIS, T, ?, ?, ?> toGet) {
        return toGet.get().apply((THIS) this);
    }

    /**
     * Auxiliary method that performs some action-step.
     *
     * @param toPerform is a supplier of an action to be performed
     * @return self-reference
     */
    protected final THIS perform(SequentialActionSupplier<? super THIS, ?, ?> toPerform) {
        toPerform.get().performAction((THIS) this);
        return (THIS) this;
    }

    /**
     * Checks is some object present. When it is not present then it throws an exception.
     *
     * @param toBePresent is a supplier of a function that retrieves a value
     * @param toIgnore    which exceptions should be ignored during evaluation of {@code toBePresent}
     * @return is desired object present or not
     */
    @SafeVarargs
    protected final boolean presenceOfOrThrow(SequentialGetStepSupplier<? super THIS, ?, ?, ?, ?> toBePresent,
                                              Class<? extends Throwable>... toIgnore) {

        return get(presence(toBePresent)
                .addIgnored(ignoredExceptions(toIgnore))
                .throwOnNoResult());
    }

    /**
     * Checks is some object present or not.
     *
     * @param toBePresent is a supplier of a function that retrieves a value
     * @param toIgnore    which exceptions should be ignored during evaluation of {@code toBePresent}
     * @return is desired object present or not
     */
    @SafeVarargs
    protected final boolean presenceOf(SequentialGetStepSupplier<? super THIS, ?, ?, ?, ?> toBePresent,
                                       Class<? extends Throwable>... toIgnore) {
        return get(presence(toBePresent)
                .addIgnored(ignoredExceptions(toIgnore)));
    }

    /**
     * Checks is some object absent. When it is present then it throws an exception.
     *
     * @param toBeAbsent is a supplier of a function that retrieves a value
     * @param timeOut    is a time to wait for value is absent. WARNING!!! When {@code toBePresent} has a defined time out
     *                   then it is ignored in favour of a time defined by the method.
     * @return is an object absent or not
     */
    protected final boolean absenceOf(SequentialGetStepSupplier<? super THIS, ?, ?, ?, ?> toBeAbsent,
                                      Duration timeOut) {
        return get(absence(toBeAbsent)
                .timeOut(timeOut));
    }

    /**
     * Checks is some object absent or not.
     *
     * @param toBeAbsent is a supplier of a function that retrieves a value
     * @param timeOut    is a time to wait for value is absent. WARNING!!! When {@code toBePresent} has a defined time out
     *                   then it is ignored in favour of a time defined by the method
     * @return is an object absent or not
     */
    protected final boolean absenceOfOrThrow(SequentialGetStepSupplier<? super THIS, ?, ?, ?, ?> toBeAbsent,
                                             Duration timeOut) {
        return get(absence(toBeAbsent)
                .timeOut(timeOut)
                .throwOnNoResult());
    }
}
