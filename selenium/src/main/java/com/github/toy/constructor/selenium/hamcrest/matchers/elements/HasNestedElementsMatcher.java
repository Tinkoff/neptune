package com.github.toy.constructor.selenium.hamcrest.matchers.elements;

import com.github.toy.constructor.selenium.functions.searching.MultipleSearchSupplier;
import com.github.toy.constructor.selenium.hamcrest.matchers.TypeSafeDiagnosingMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.openqa.selenium.SearchContext;

import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.String.format;
import static java.util.Arrays.stream;
import static org.hamcrest.Matchers.*;

public final class HasNestedElementsMatcher<T extends SearchContext> extends TypeSafeDiagnosingMatcher<T> {

    private final MultipleSearchSupplier<?> search;
    private Matcher<Integer> expectedCount = greaterThan(0);

    private HasNestedElementsMatcher(MultipleSearchSupplier<?> search) {
        checkArgument(search != null, "The way to find nested elements should be defined");
        this.search = search;
    }

    /**
     * Creates a new instance of {@link HasNestedElementsMatcher} and defines the way to find desired nested elements.
     * @param search is the way to find desired nested elements.
     * @param <T> is the type of an instance of {@link SearchContext} which is expected to contain desired nested elements.
     * @return created instance of {@link HasNestedElementsMatcher}
     */
    public static <T extends SearchContext> HasNestedElementsMatcher<T> hasNestedElements(MultipleSearchSupplier<?> search) {
        return new HasNestedElementsMatcher<>(search);
    }

    /**
     * Sets expected count of nested elements.
     * @param count is expected count of nested elements
     * @return self-reference.
     */
    public HasNestedElementsMatcher<T> withCount(int count) {
        checkArgument(count >= 0, "Expected count should be greater than zero or equal to zero.");
        this.expectedCount = is(count);
        return this;
    }

    /**
     * Sets criteria to check the count of found elements.
     * @param matcher is criteria to check the count of found elements
     * @return self-reference.
     */
    public HasNestedElementsMatcher<T> withCount(Matcher<Integer> matcher) {
        checkArgument(matcher != null,  "Criteria to check the count of found elements should be defined");
        this.expectedCount = matcher;
        return this;
    }

    @Override
    protected boolean matchesSafely(T item, Description mismatchDescription) {
        try {
            int foundSize = search.get().apply(item).size();

            if (!expectedCount.matches(foundSize)) {
                mismatchDescription.appendText(format("actual count %s of found items doesn't meet the criteria %s",
                        foundSize, expectedCount));
                return false;
            }
            return true;
        }
        catch (Throwable e) {
            mismatchDescription.appendText("The attempt to find nested elements has failed. Something went wrong.\n")
                    .appendText(format("Caught throwable: %s\n", e.getClass().getName()))
                    .appendText("Stack trace:\n");

            stream(e.getStackTrace())
                    .forEach(stackTraceElement -> mismatchDescription.appendText(format("%s\n",
                    stackTraceElement.toString())));
            return false;
        }
    }

    @Override
    public void describeTo(Description description) {
        description.appendText(toString());
    }

    @Override
    public String toString() {
        return format("has nested elements %s. Count should meet the criteria %s", search.toString(),
                expectedCount);
    }
}
