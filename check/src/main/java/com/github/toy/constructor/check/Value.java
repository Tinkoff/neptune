package com.github.toy.constructor.check;

import com.github.toy.constructor.core.api.SequentialActionSupplier;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

import static com.github.toy.constructor.core.api.StaticEventFiring.catchResult;
import static com.github.toy.constructor.core.api.StoryWriter.toGet;
import static com.github.toy.constructor.core.api.properties.DoCapturesOf.catchFailureEvent;
import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.lang.String.format;
import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.hamcrest.MatcherAssert.assertThat;

public final class Value<T> extends SequentialActionSupplier<Check, T, Value<T>> {

    private final T inspected;
    private final FluentMatcher<T> matcher;

    private Value(T inspected) {
        this.inspected = inspected;
        matcher = new FluentMatcher<>();
    }

    /**
     * Creates an instance of {@link Value} and defines value to be verified.
     *
     * @param t value to be verified.
     * @param <T> is a type to be verified.
     * @return an instance of {@link Value} with defined value to be verified.
     */
    public static <T> Value<T> value(T t) {
        return new Value<>(t);
    }

    /**
     * Adds more criteria to check the value.
     *
     * @param function value gets some value from an object which is needed to be matched
     * @param criteria matcher to be added
     * @param <R> the type of a value value should be returned by function and matched by criteria.
     * @return self-reference
     */
    public <R> Value<T> expected(String description,
                                 Function<T, R> function, Matcher<? super R> criteria) {
        matcher.shouldMatch(description, function, criteria);
        return this;
    }

    /**
     * Adds more criteria to check value value.
     *
     * @param criteria matcher to be added
     * @return self-reference
     */
    public Value<T> expected(Matcher<? super T> criteria) {
        matcher.shouldMatch(criteria);
        return this;
    }

    @Override
    public Consumer<Check> get() {
        return ofNullable(super.get())
                .orElseGet(() -> andThen("Assert value ",
                        toGet(format("Inspected value %s", inspected), check -> inspected), matcher)
                        .get());
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void performActionOn(T value, Object... parameters) {
        FluentMatcher<T> fluentMatcher = (FluentMatcher<T>) parameters[0];
        checkArgument(fluentMatcher.matchMap.size() > 0,
                "There should be defined at least one criteria");
        try {
            assertThat(value, matcher);
        }
        catch (Throwable t) {
            if (catchFailureEvent() && AssertionError.class.isAssignableFrom(t.getClass())) {
                catchResult(value, format("Mismatched object %s", value));
            }
            throw t;
        }
    }

    private static class FluentMatcher<T> extends BaseMatcher<T> {
        private final Map<Function<T, ?>, Matcher<?>> matchMap = new LinkedHashMap<>();
        private final Map<String, List<String>> mismatches = new LinkedHashMap<>();

        private FluentMatcher() {
            super();
        }

        /**
         * Adds more criteria to check some value.
         *
         * @param function value gets some value from an object which is needed to be matched
         * @param criteria matcher to be added
         * @param <R> the type of a value value should be returned by function and matched by criteria.
         * @return self-reference
         */
        private <R> FluentMatcher<T> shouldMatch(String valueDescription, Function<T, R> function, Matcher<? super R> criteria) {
            checkArgument(!isBlank(valueDescription), "Description of a value value the function returns" +
                    "should not be blank");
            checkNotNull(function);
            checkNotNull(criteria);
            matchMap.put(toGet(valueDescription,
                    function),
                    criteria);
            return this;
        }

        /**
         * Adds more criteria to check some value.
         *
         * @param criteria matcher to be added
         * @return self-reference
         */
        private FluentMatcher<T> shouldMatch(Matcher<? super T> criteria) {
            return shouldMatch("Inspected value", t -> t, criteria);
        }

        @Override
        public boolean matches(Object item) {
            mismatches.clear();
            matchMap.forEach((key, value) -> {
                List<String> mismatchList = new ArrayList<>();
                Object valueToMatch = key.apply((T) item);
                boolean result = value.matches(valueToMatch);
                if (!result) {
                    Description description = new StringDescription();
                    value.describeMismatch(valueToMatch, description);
                    mismatchList.add(format("  It was expected value %s suits criteria '%s'. Actual result: %s\n", key,
                            value, description));
                }
                if (mismatchList.size() > 0) {
                    mismatches.put(key.toString(), mismatchList);
                }
            });

            return mismatches.size() == 0;

        }

        @Override
        public void describeMismatch(Object item, Description mismatchDescription) {
            if (mismatches.size() == 0) {
                super.describeMismatch(item, mismatchDescription);
                return;
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
                builder.append(format("\n           %s", value.toString()));
            });

            return builder.toString();
        }
    }
}
