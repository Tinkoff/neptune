package com.github.toy.constructor.check.hamcrest;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;
import org.hamcrest.TypeSafeDiagnosingMatcher;

import java.util.*;
import java.util.function.Function;

import static com.github.toy.constructor.core.api.StoryWriter.toGet;
import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.lang.String.format;
import static java.util.Arrays.asList;
import static org.apache.commons.lang3.StringUtils.isBlank;

public class FluentMatcher<T> extends TypeSafeDiagnosingMatcher<T> {
    private final Map<Function<T, ?>, List<Matcher<?>>> matchMap = new LinkedHashMap<>();
    private final Map<String, List<String>> mismatches = new LinkedHashMap<>();

    private FluentMatcher() {
        super();
    }

    /**
     * This method creates instance of the {@link FluentMatcher} with startup
     * parameters.
     *
     * @param criteria matchers to set up
     * @param <T> the type of a value to check
     * @return instance of the {@link FluentMatcher} with startup
     * parameters.
     */
    @SafeVarargs
    public static <T> FluentMatcher<T> shouldMatch(Matcher<? super T>... criteria) {
        return new FluentMatcher<T>().and(criteria);
    }

    /**
     * This method creates instance of the {@link FluentMatcher} with startup
     * parameters.
     *
     * @param function that gets some value from an object which is needed to be matched
     * @param criteria matchers to set up
     * @param <T> the type of a value to check
     * @param <R> the type of a value that should be returned by function and matched by criteria.
     * @return instance of the {@link FluentMatcher} with startup
     * parameters.
     */
    @SafeVarargs
    public static <T, R> FluentMatcher<T> shouldMatch(String valueDescription, Function<T, R> function,
                                                      Matcher<? super R>... criteria) {
        return new FluentMatcher<T>().and(valueDescription, function, criteria);
    }

    /**
     * Adds more criteria to check some value.
     *
     * @param function that gets some value from an object which is needed to be matched
     * @param criteria matchers to be added
     * @param <R> the type of a value that should be returned by function and matched by criteria.
     * @return self-reference
     */
    @SafeVarargs
    public final <R> FluentMatcher<T> and(String valueDescription, Function<T, R> function, Matcher<? super R>... criteria) {
        checkArgument(!isBlank(valueDescription), "Description of a value that the function returns" +
                "should not be blank");
        checkNotNull(function);
        checkNotNull(criteria);
        checkArgument(criteria.length > 0, "Should be defined at least one matcher");
        List<Matcher<?>> criteriaList = asList(criteria);
        matchMap.put(toGet(valueDescription,
                function),
                criteriaList);
        return this;
    }

    /**
     * Adds more criteria to check some value.
     *
     * @param criteria matchers to be added
     * @return self-reference
     */
    @SafeVarargs
    public final FluentMatcher<T> and(Matcher<? super T>... criteria) {
        return and("inspected value", t -> t, criteria);
    }

    @Override
    protected boolean matchesSafely(T item, Description mismatchDescription) {
        mismatches.clear();
        matchMap.forEach((key, value) -> {
            List<String> mismatchList = new ArrayList<>();
            Object valueToMatch = key.apply(item);
            value.forEach(matcher -> {
                boolean result = matcher.matches(valueToMatch);
                if (!result) {
                    Description description = new StringDescription();
                    matcher.describeMismatch(valueToMatch, description);
                    mismatchList.add(format("  It was expected that %s suits criteria '%s'. Actual result: %s\n", key,
                            matcher, description));
                }
            });
            if (mismatchList.size() > 0) {
                mismatches.put(key.toString(), mismatchList);
            }
        });

        if (mismatches.size() == 0) {
            return true;
        }

        StringBuilder builder = new StringBuilder()
                .append("Detected mismatches:\n")
                .append("===================================\n");
        mismatches.forEach((key, value) -> {
            builder.append(format("%s:\n", key));
            value.forEach(s -> builder.append(format("           %s", s)));
        });
        mismatchDescription.appendText("\n\n")
                .appendText(format("\n%s", builder.toString()).trim());
        return false;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText(format(" %s", this.toString()));
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        matchMap.forEach((key, value) -> {
            builder.append("\n").append(key.toString());
            value.forEach(matcher -> builder.append(format("\n           %s", matcher.toString())));
        });

        return builder.toString();
    }
}
