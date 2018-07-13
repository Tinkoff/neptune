package com.github.toy.constructor.selenium.hamcrest.matchers.elements;

import com.github.toy.constructor.selenium.api.widget.HasValue;
import com.github.toy.constructor.selenium.hamcrest.matchers.TypeSafeDiagnosingMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.openqa.selenium.SearchContext;

import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.String.format;
import static org.hamcrest.Matchers.equalTo;

public final class HasValueMatcher<Q, T extends SearchContext & HasValue<Q>> extends TypeSafeDiagnosingMatcher<T> {

    private final Matcher<Q> criteria;

    private HasValueMatcher(Matcher<Q> criteria) {
        checkArgument(criteria != null, "Matcher to check value should be defined.");
        this.criteria = criteria;
    }

    /**
     * Creates an instance of {@link HasValueMatcher} which checks value of the element.
     *
     * @param value which is expected to be equal to value of the element.
     * @param <Q> type of a value
     * @param <T> type of an element
     * @return instance of {@link HasValueMatcher}
     */
    public static <Q, T extends SearchContext & HasValue<Q>> HasValueMatcher<Q, T> hasValue(Q value) {
        return new HasValueMatcher<>(equalTo(value));
    }

    /**
     * Creates an instance of {@link HasValueMatcher} which checks value of the element.
     *
     * @param value matcher which is supposed to be used for the value verification
     * @param <Q> type of a value
     * @param <T> type of an element
     * @return instance of {@link HasValueMatcher}
     */
    public static <Q, T extends SearchContext & HasValue<Q>> HasValueMatcher<Q, T> hasValue(Matcher<Q> value) {
        return new HasValueMatcher<>(value);
    }

    @Override
    protected boolean matchesSafely(T item, Description mismatchDescription) {
        Q value = item.getValue();
        boolean result = criteria.matches(value);
        if (!result) {
            criteria.describeMismatch(value, mismatchDescription);
        }
        return result;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText(toString());
    }

    @Override
    public String toString() {
        return format("has value %s", criteria.toString());
    }
}
