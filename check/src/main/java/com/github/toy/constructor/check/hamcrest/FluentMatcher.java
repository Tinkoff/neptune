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
import static java.lang.String.join;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.StringUtils.EMPTY;

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
    public static <T> FluentMatcher<T> shouldMatch(Matcher<T>... criteria) {
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
    public static <T, R> FluentMatcher<T> shouldMatch(Function<T, R> function, Matcher<R>... criteria) {
        return new FluentMatcher<T>().and(function, criteria);
    }

    /**
     * Adds more criteria to check some value.
     *
     * @param function that gets some value from an object which is needed to be matched
     * @param criteria matchers to be added
     * @param <R> the type of a value that should be returned by function and matched by criteria.
     * @return self-reference
     */
    public <R> FluentMatcher<T> and(Function<T, R> function, Matcher<R>... criteria) {
        checkNotNull(criteria);
        checkArgument(criteria.length > 0, "Should be defined at least one matcher");
        List<Matcher<?>> criteriaList = asList(criteria);
        String criteriaDescription = join("\n             ",
                criteriaList.stream().map(Object::toString).collect(toList()));
        matchMap.put(toGet(format("%s suits criteria:\n             %s", function.toString(), criteriaDescription).trim(),
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
    public FluentMatcher<T> and(Matcher<T>... criteria) {
        return and(new Function<>() {
            @Override
            public T apply(T t) {
                return t;
            }

            @Override
            public String toString() {
                return EMPTY;
            }
        }, criteria);
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
                    Description description1 = new StringDescription();
                    Description description2 = new StringDescription();
                    matcher.describeTo(description1);
                    matcher.describeMismatch(valueToMatch, description2);
                    mismatchList.add(format("expected %s. Actual result is %s", description1, description2));
                }
            });
            if (mismatchList.size() > 0) {
                mismatches.put(key.toString(), mismatchList);
            }
        });

        if (mismatches.size() == 0) {
            return true;
        }

        StringBuilder builder = new StringBuilder().append("Detected mismatches: \n");
        mismatches.forEach((key, value) -> {
            builder.append(format("- expected %s.\nMismatches:", key));
            value.forEach(s -> builder.append(format("         - %s", s)));
        });
        mismatchDescription.appendText(builder.toString());
        return false;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText(format(" %s", this.toString()));
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder().append("Expected:\n");
        matchMap.keySet().forEach(function -> builder.append(format("- %s\n", function.toString())));

        return builder.toString();
    }
}
