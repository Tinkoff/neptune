package ru.tinkoff.qa.neptune.core.api.hamcrest.common.only.one;

import org.hamcrest.Matcher;
import ru.tinkoff.qa.neptune.core.api.hamcrest.NeptuneFeatureMatcher;
import ru.tinkoff.qa.neptune.core.api.hamcrest.UnitesCriteria;
import ru.tinkoff.qa.neptune.core.api.hamcrest.common.DoesNotMatchAnyCriteria;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;

import java.util.HashMap;
import java.util.Map;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;

/**
 * This matcher checks an object by only one of defined criteria
 *
 * @param <T> is a type of a checked value
 */
@UnitesCriteria
@Description("{onlyOneExpression}")
public final class OnlyOneMatcher<T> extends NeptuneFeatureMatcher<T> {

    @DescriptionFragment(value = "onlyOneExpression", makeReadableBy = OnlyOneMatcherParameterValueGetter.class)
    private final Matcher<? super T>[] matchers;

    @SafeVarargs
    private OnlyOneMatcher(Matcher<? super T>... matchers) {
        super(false);
        checkNotNull(matchers);
        checkArgument(matchers.length > 1, "At least two matchers should be defined");
        this.matchers = matchers;
    }

    /**
     * Creates a matcher that checks an object by only one of defined criteria
     *
     * @param matchers are criteria which are used by the checking
     * @param <T>      is a type of a checked value
     * @return an aggregated matcher
     */
    @SafeVarargs
    public static <T> Matcher<T> onlyOne(Matcher<? super T>... matchers) {
        return new OnlyOneMatcher<>(matchers);
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
            appendMismatchDescription(new DoesNotMatchAnyCriteria(toMatch));
            return false;
        }

        appendMismatchDescription(new MatchesMoreThanOneCriteria(resultMap.entrySet()
                .stream()
                .filter(Map.Entry::getValue)
                .map(Map.Entry::getKey)
                .collect(toList()), toMatch));
        return false;
    }
}
