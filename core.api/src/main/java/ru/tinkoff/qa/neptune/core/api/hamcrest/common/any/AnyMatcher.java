package ru.tinkoff.qa.neptune.core.api.hamcrest.common.any;

import org.hamcrest.Matcher;
import ru.tinkoff.qa.neptune.core.api.hamcrest.NeptuneFeatureMatcher;
import ru.tinkoff.qa.neptune.core.api.hamcrest.UnitesCriteria;
import ru.tinkoff.qa.neptune.core.api.hamcrest.common.DoesNotMatchAnyCriteria;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.Arrays.stream;

/**
 * This matcher checks an object by any of defined criteria
 *
 * @param <T> is a type of a checked value
 */
@UnitesCriteria
@Description("{orExpression}")
public final class AnyMatcher<T> extends NeptuneFeatureMatcher<T> {

    @DescriptionFragment(value = "orExpression", makeReadableBy = AnyParameterValueGetter.class)
    private final Matcher<? super T>[] matchers;

    @SafeVarargs
    private AnyMatcher(Matcher<? super T>... matchers) {
        super(false);
        checkNotNull(matchers);
        checkArgument(matchers.length > 1, "At least two matchers should be defined");
        this.matchers = matchers;
    }

    /**
     * Creates a matcher that checks an object by any of defined criteria
     *
     * @param matchers are criteria which are used by the checking
     * @param <T> is a type of a checked value
     * @return an aggregated matcher
     */
    @SafeVarargs
    public static <T> Matcher<? super T> anyOne(Matcher<? super T>... matchers) {
        return new AnyMatcher<>(matchers);
    }

    @Override
    protected boolean featureMatches(T toMatch) {
        var match = stream(matchers)
                .anyMatch(m -> m.matches(toMatch));

        if (!match) {
            appendMismatchDescription(new DoesNotMatchAnyCriteria());
        }

        return match;
    }
}
