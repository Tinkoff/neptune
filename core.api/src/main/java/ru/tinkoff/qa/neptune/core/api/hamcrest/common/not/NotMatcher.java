package ru.tinkoff.qa.neptune.core.api.hamcrest.common.not;

import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;
import ru.tinkoff.qa.neptune.core.api.hamcrest.NeptuneFeatureMatcher;
import ru.tinkoff.qa.neptune.core.api.hamcrest.common.DoesNotMatchAnyCriteria;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.Arrays.stream;
import static ru.tinkoff.qa.neptune.core.api.steps.localization.StepLocalization.translate;

/**
 * This matcher checks an object by every defined and inverted criteria
 *
 * @param <T> is a type of a checked value
 */
@Description("{notExpression}")
public final class NotMatcher<T> extends NeptuneFeatureMatcher<T> {

    @DescriptionFragment(value = "notExpression", makeReadableBy = NotParameterValueGetter.class)
    private final Matcher<? super T>[] toInvert;

    @SafeVarargs
    private NotMatcher(Matcher<? super T>... toInvert) {
        super(false);
        checkNotNull(toInvert);
        checkArgument(toInvert.length > 0, "At least one matcher should be defined");
        this.toInvert = toInvert;
    }

    /**
     * Creates a matcher that checks an object by every defined and inverted criteria
     *
     * @param matchers are criteria which should be inverted and used by the checking
     * @param <T> is a type of a checked value
     * @return an aggregated matcher
     */
    @SafeVarargs
    public static <T> Matcher<T> notOf(Matcher<? super T>... matchers) {
        return new NotMatcher<>(matchers);
    }

    @Override
    protected boolean featureMatches(T toMatch) {

        var count = stream(toInvert).map(m -> {
            var result = !m.matches(toMatch);
            if (!result) {
                appendMismatchDescription(new StringDescription().appendText(translate(m.toString())));
            }
            return result;
        }).filter(b -> !b).count();

        if (count == 0) {
            return true;
        }

        if (toInvert.length == 1) {
            return false;
        }

        if (count == toInvert.length) {
            mismatchDescriptions.clear();
            appendMismatchDescription(new DoesNotMatchAnyCriteria());
            return false;
        }

        return false;
    }
}
