package ru.tinkoff.qa.neptune.core.api.hamcrest.throwable;

import org.hamcrest.Matcher;
import ru.tinkoff.qa.neptune.core.api.hamcrest.NeptuneFeatureMatcher;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.hamcrest.Matchers.equalTo;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.common.all.AllCriteriaMatcher.all;

@Description("throwable has message '{messageMatcher}'")
public final class ThrowableMessageMatcher extends NeptuneFeatureMatcher<Throwable> {

    @DescriptionFragment("messageMatcher")
    private final Matcher<? super String> messageMatcher;

    private ThrowableMessageMatcher(Matcher<? super String> messageMatcher) {
        super(true);
        checkNotNull(messageMatcher);
        this.messageMatcher = messageMatcher;
    }

    /**
     * Defines criteria to check message of a throwable
     *
     * @param matchers criteria to check message of a throwable
     * @return an instance of {@link ThrowableMessageMatcher}
     */
    @SafeVarargs
    public static ThrowableMessageMatcher throwableHasMessage(Matcher<? super String>... matchers) {
        return new ThrowableMessageMatcher(all(matchers));
    }

    /**
     * Defines expected message of a throwable
     *
     * @param message expected message of a throwable
     * @return an instance of {@link ThrowableMessageMatcher}
     */
    public static ThrowableMessageMatcher throwableHasMessage(String message) {
        return new ThrowableMessageMatcher(equalTo(message));
    }

    @Override
    protected boolean featureMatches(Throwable toMatch) {
        var result = messageMatcher.matches(toMatch.getMessage());
        if (!result) {
            appendMismatchDescription(messageMatcher, toMatch.getMessage());
        }
        return result;
    }
}
