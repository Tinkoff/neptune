package ru.tinkoff.qa.neptune.check;

import org.hamcrest.Matcher;
import ru.tinkoff.qa.neptune.check.parameter.value.getters.OrParameterValueGetter;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.Arrays.stream;

@Description("{orExpression}")
final class OrMatcher<T> extends MatcherWithTime<T> {

    @DescriptionFragment(value = "orExpression", makeReadableBy = OrParameterValueGetter.class)
    private final Matcher<? super T>[] matchers;

    @SafeVarargs
    OrMatcher(Matcher<? super T>... matchers) {
        checkNotNull(matchers);
        checkArgument(matchers.length > 1, "At least two matchers should be defined");
        this.matchers = matchers;
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
