package ru.tinkoff.qa.neptune.check;

import org.hamcrest.Matcher;
import ru.tinkoff.qa.neptune.check.parameter.value.getters.OnlyOneParameterValueGetter;
import ru.tinkoff.qa.neptune.core.api.hamcrest.NeptuneFeatureMatcher;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;

import java.util.HashMap;
import java.util.Map;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;

@Description("{onlyOneExpression}")
final class OnlyOneMatcher<T> extends NeptuneFeatureMatcher<T> {

    @DescriptionFragment(value = "onlyOneExpression", makeReadableBy = OnlyOneParameterValueGetter.class)
    private final Matcher<? super T>[] matchers;

    @SafeVarargs
    OnlyOneMatcher(Matcher<? super T>... matchers) {
        checkNotNull(matchers);
        checkArgument(matchers.length > 1, "At least two matchers should be defined");
        this.matchers = matchers;
    }

    @Override
    protected boolean featureMatches(T toMatch) {
        var resultMap = new HashMap<Matcher<?>, Boolean>();

        var matchCount = stream(matchers)
                .map(m -> {
                    var matches = m.matches(toMatch);
                    resultMap.put(m, m.matches(toMatch));
                    return matches;
                })
                .filter(b -> b)
                .count();

        if (matchCount == 1) {
            return true;
        }

        if (matchCount == 0) {
            appendMismatchDescription(new DoesNotMatchAnyCriteria());
        }

        appendMismatchDescription(new MatchesMoreThanOneCriteria(resultMap.entrySet()
                .stream()
                .filter(Map.Entry::getValue)
                .map(Map.Entry::getKey)
                .collect(toList())));
        return false;
    }
}
