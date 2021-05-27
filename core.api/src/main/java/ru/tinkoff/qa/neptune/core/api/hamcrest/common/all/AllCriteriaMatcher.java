package ru.tinkoff.qa.neptune.core.api.hamcrest.common.all;

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
 * This matcher checks an object by every defined criteria
 *
 * @param <T> is a type of a checked value
 */
@Description("{allMatchers}")
public final class AllCriteriaMatcher<T> extends NeptuneFeatureMatcher<T> {

    @DescriptionFragment(value = "allMatchers", makeReadableBy = AllParameterValueGetter.class)
    private final Matcher<? super T>[] delegateTo;

    @SafeVarargs
    private AllCriteriaMatcher(Matcher<? super T>... delegateTo) {
        super(false);
        checkNotNull(delegateTo);
        checkArgument(delegateTo.length > 0, "At least one matcher should be defined");
        this.delegateTo = delegateTo;
    }

    /**
     * Creates a matcher that checks an object by every defined criteria
     *
     * @param matchers are criteria which are used by the checking
     * @param <T>      is a type of a checked value
     * @return an aggregated matcher
     */
    @SafeVarargs
    public static <T> Matcher<T> all(Matcher<? super T>... matchers) {
        return new AllCriteriaMatcher<>(matchers);
    }

    @Override
    protected boolean featureMatches(T toMatch) {
        var count = stream(delegateTo).map(m -> {
            var result = m.matches(toMatch);

            if (!result) {
                var description = new StringDescription();
                m.describeMismatch(toMatch, description);
                appendMismatchDescription(new StringDescription().appendText(translate(description.toString())));
            }
            return result;
        }).filter(b -> !b).count();

        if (count == 0) {
            return true;
        }

        if (delegateTo.length == 1) {
            return false;
        }

        if (count == delegateTo.length) {
            mismatchDescriptions.clear();
            appendMismatchDescription(new DoesNotMatchAnyCriteria());
            return false;
        }

        return false;
    }
}
