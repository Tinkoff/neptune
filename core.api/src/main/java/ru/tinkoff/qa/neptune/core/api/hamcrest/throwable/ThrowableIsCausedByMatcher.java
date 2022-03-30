package ru.tinkoff.qa.neptune.core.api.hamcrest.throwable;

import org.hamcrest.Matcher;
import ru.tinkoff.qa.neptune.core.api.hamcrest.NeptuneFeatureMatcher;
import ru.tinkoff.qa.neptune.core.api.hamcrest.NullValueMismatch;
import ru.tinkoff.qa.neptune.core.api.hamcrest.ObjectIsNotPresentMismatch;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;

import java.util.LinkedList;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.common.all.AllCriteriaMatcher.all;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.ofclass.OfClassMatcher.isObjectOfClass;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.throwable.ThrowableMessageMatcher.throwableHasMessage;

public abstract class ThrowableIsCausedByMatcher extends NeptuneFeatureMatcher<Throwable> {

    @DescriptionFragment("causeMatcher")
    final Matcher<? super Throwable> checkCause;

    private ThrowableIsCausedByMatcher(Matcher<? super Throwable> checkCause) {
        super(true);
        checkNotNull(checkCause);
        this.checkCause = checkCause;
    }

    /**
     * Defines criteria to check {@link Throwable} which is taken via {@link Throwable#getCause()}
     * of the verified throwable
     *
     * @param matchers criteria to check throwable
     * @return an instance of {@link ThrowableIsCausedByMatcher} that checks only first cause taken via {@link Throwable#getCause()}
     * of the verified throwable
     */
    @SafeVarargs
    public static ThrowableIsCausedByMatcher hasPrimaryCause(Matcher<? super Throwable>... matchers) {
        return new ThrowableIsCausedByMatcherPrimary(all(matchers));
    }

    /**
     * Defines expected class of {@link Throwable} which is taken via {@link Throwable#getCause()}
     * of the verified throwable
     *
     * @param expectedClass is expected class of throwable-cause
     * @param <T>           is a type of throwable-cause
     * @return an instance of {@link ThrowableIsCausedByMatcher} that checks only first cause taken via {@link Throwable#getCause()}
     * of the verified throwable
     */
    public static <T extends Throwable> ThrowableIsCausedByMatcher hasPrimaryCause(Class<T> expectedClass) {
        return hasPrimaryCause(isObjectOfClass(expectedClass));
    }

    /**
     * Defines expected class and message of {@link Throwable} which is taken via {@link Throwable#getCause()}
     * of the verified throwable
     *
     * @param expectedClass is expected class of throwable-cause
     * @param message       is expected message of throwable-cause
     * @param <T>           is a type of throwable-cause
     * @return an instance of {@link ThrowableIsCausedByMatcher} that checks only first cause taken via {@link Throwable#getCause()}
     * of the verified throwable
     */
    public static <T extends Throwable> ThrowableIsCausedByMatcher hasPrimaryCause(Class<T> expectedClass, String message) {
        return hasPrimaryCause(isObjectOfClass(expectedClass), throwableHasMessage(message));
    }

    /**
     * Defines expected class and message criteria of {@link Throwable} which is taken via {@link Throwable#getCause()}
     * of the verified throwable
     *
     * @param expectedClass   is expected class of throwable-cause
     * @param messageMatchers criteria to check message of a throwable
     * @param <T>             is a type of throwable-cause
     * @return an instance of {@link ThrowableIsCausedByMatcher} that checks only first cause taken via {@link Throwable#getCause()}
     * of the verified throwable
     */
    @SafeVarargs
    public static <T extends Throwable> ThrowableIsCausedByMatcher hasPrimaryCause(Class<T> expectedClass, Matcher<? super String>... messageMatchers) {
        return hasPrimaryCause(isObjectOfClass(expectedClass), throwableHasMessage(messageMatchers));
    }

    /**
     * Defines criteria to find any suitable throwable-cause of the verified throwable
     *
     * @param matchers criteria to check throwable
     * @return an instance of {@link ThrowableIsCausedByMatcher} that checks all throwable-causes of the verified throwable
     * until it finds ane that matches all defined criteria
     */
    @SafeVarargs
    public static ThrowableIsCausedByMatcher hasRootCause(Matcher<? super Throwable>... matchers) {
        return new ThrowableIsCausedByMatcherDeep(all(matchers));
    }

    /**
     * Defines expected class to find any suitable throwable-cause of the verified throwable
     *
     * @param expectedClass is expected class of throwable-cause
     * @param <T>           is a type of throwable-cause
     * @return an instance of {@link ThrowableIsCausedByMatcher} that checks all throwable-causes of the verified throwable
     * until it finds ane that matches all defined criteria
     */
    public static <T extends Throwable> ThrowableIsCausedByMatcher hasRootCause(Class<T> expectedClass) {
        return hasRootCause(isObjectOfClass(expectedClass));
    }

    /**
     * Defines expected class and message to find any suitable throwable-cause of the verified throwable
     *
     * @param expectedClass is expected class of throwable-cause
     * @param message       is expected message of throwable-cause
     * @param <T>           is a type of throwable-cause
     * @return an instance of {@link ThrowableIsCausedByMatcher} that checks all throwable-causes of the verified throwable
     * until it finds ane that matches all defined criteria
     */
    public static <T extends Throwable> ThrowableIsCausedByMatcher hasRootCause(Class<T> expectedClass, String message) {
        return hasRootCause(isObjectOfClass(expectedClass), throwableHasMessage(message));
    }

    /**
     * Defines expected class and message criteria to find any suitable throwable-cause of the verified throwable
     *
     * @param expectedClass   is expected class of throwable-cause
     * @param messageMatchers criteria to check message of a throwable
     * @param <T>             is a type of throwable-cause
     * @return an instance of {@link ThrowableIsCausedByMatcher} that checks all throwable-causes of the verified throwable
     * until it finds ane that matches all defined criteria
     */
    @SafeVarargs
    public static <T extends Throwable> ThrowableIsCausedByMatcher hasRootCause(Class<T> expectedClass, Matcher<? super String>... messageMatchers) {
        return hasRootCause(isObjectOfClass(expectedClass), throwableHasMessage(messageMatchers));
    }

    @Override
    protected boolean featureMatches(Throwable toMatch) {
        if (isNull(toMatch.getCause())) {
            appendMismatchDescription(new NullValueMismatch());
            return false;
        }

        return matchCause(toMatch, toMatch.getCause());
    }

    abstract boolean matchCause(Throwable toMatch, Throwable primary);

    @Description("primary cause matches '{causeMatcher}'")
    private static final class ThrowableIsCausedByMatcherPrimary extends ThrowableIsCausedByMatcher {

        private ThrowableIsCausedByMatcherPrimary(Matcher<Throwable> checkCause) {
            super(checkCause);
        }

        @Override
        boolean matchCause(Throwable toMatch, Throwable primary) {
            var result = checkCause.matches(primary);
            if (!result) {
                appendMismatchDescription(checkCause, primary);
            }

            return result;
        }
    }

    @Description("has root cause that matches '{causeMatcher}'")
    private static final class ThrowableIsCausedByMatcherDeep extends ThrowableIsCausedByMatcher {

        private ThrowableIsCausedByMatcherDeep(Matcher<Throwable> checkCause) {
            super(checkCause);
        }

        @Override
        boolean matchCause(Throwable toMatch, Throwable primary) {
            var causeList = new LinkedList<Throwable>();
            causeList.addFirst(primary);

            var throwable = primary;
            while (nonNull(throwable)) {
                var cause = throwable.getCause();
                if (nonNull(cause)) {
                    causeList.addLast(cause);
                }
                throwable = cause;
            }

            var result = causeList.stream().anyMatch(checkCause::matches);

            if (!result) {
                appendMismatchDescription(new ObjectIsNotPresentMismatch(new ThrowableCause(), checkCause));
            }

            return result;
        }
    }
}
