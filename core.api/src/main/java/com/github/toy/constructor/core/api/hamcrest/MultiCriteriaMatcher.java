package com.github.toy.constructor.core.api.hamcrest;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static com.github.toy.constructor.core.api.FunctionalUtil.toReturn;
import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.String.format;
import static org.apache.commons.lang3.StringUtils.EMPTY;

public class  MultiCriteriaMatcher<T> extends TypeSafeDiagnosingMatcher<T> {
    private final Map<Function<T, ?>, Matcher<?>> matchMap = new HashMap<>();
    private final Map<String, String> mismathces = new HashMap<>();

    private MultiCriteriaMatcher() {
        super();
    }

    public static <T> MultiCriteriaMatcher<T> shouldMatch(Matcher<T> matcher) {
        return new MultiCriteriaMatcher<T>().and(matcher);
    }

    public static <T, R> MultiCriteriaMatcher<T> shouldMatch(Function<T, R> function, Matcher<R> matcher) {
        return new MultiCriteriaMatcher<T>().and(function, matcher);
    }

    public <R> MultiCriteriaMatcher<T> and(Function<T, R> function, Matcher<R> matcher) {
        matchMap.put(toReturn(format("%s %s", function.toString(), matcher.toString()), function), matcher);
        return this;
    }

    public MultiCriteriaMatcher<T> and(Matcher<T> matcher) {
        return and(toReturn(EMPTY, (t) -> t), matcher);
    }

    @Override
    protected boolean matchesSafely(T item, Description mismatchDescription) {
        checkArgument(matchMap.size() > 0, "At least one criteria should be defined there");
        mismathces.clear();

        matchMap.forEach((key, value) -> {
            boolean result = value.matches(key.apply(item));
            if (!result) {
                Description.NullDescription description = new Description.NullDescription();
                value.describeMismatch(item, description);
                mismathces.put(key.toString(), description.toString());
            }
        });

        if (mismathces.size() == 0) {
            return true;
        }

        StringBuilder builder = new StringBuilder().append("Detected mismatches: \n");
        mismathces.forEach((key, value) -> {
            builder.append(format("- expected %s but was %s", key, value));
        });
        mismatchDescription.appendText(builder.toString());
        return false;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText(format(" %s", this.toString()));
    }

    public String toString() {
        StringBuilder builder = new StringBuilder().append("Expected:\n");
        matchMap.keySet().forEach(function -> {
            builder.append(format("- %s\n", function.toString()));
        });

        return builder.toString();
    }
}
