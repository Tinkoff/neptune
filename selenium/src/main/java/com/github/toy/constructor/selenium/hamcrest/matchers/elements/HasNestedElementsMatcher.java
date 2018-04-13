package com.github.toy.constructor.selenium.hamcrest.matchers.elements;

import com.github.toy.constructor.selenium.functions.searching.MultipleSearchSupplier;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeDiagnosingMatcher;
import org.openqa.selenium.SearchContext;

import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.String.format;
import static java.util.Arrays.stream;

public final class HasNestedElementsMatcher<T extends SearchContext> extends TypeSafeDiagnosingMatcher<T> {

    private final MultipleSearchSupplier<?> search;
    private int expectedCount = -1;
    private boolean toMatchCountStrictly = false;

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
     * Sets count of nested items that is expected. If the method {@link #checkCountStrictly(boolean)} is not invoked
     * then in means that actual count of nested elements should be greater or equal to expected count. Actual count of
     * nested elements should be equal to expected count otherwise.
     * @param count is expected count of nested elements
     * @return self-reference.
     */
    public HasNestedElementsMatcher<T> withCount(int count) {
        checkArgument(count > 0, "Expected count should be greater than zero");
        this.expectedCount = count;
        return this;
    }

    /**
     * Sets how strictly count of found elements should be checked.
     * @param strictly how strictly count of found elements should be checked. If the method {@link #withCount(int)}
     *                 is not invoked then this value is ignored.
     * @return self-reference.
     */
    public HasNestedElementsMatcher<T> checkCountStrictly(boolean strictly) {
        this.toMatchCountStrictly = strictly;
        return this;
    }

    @Override
    protected boolean matchesSafely(T item, Description mismatchDescription) {

        try {
            int foundSize = search.get().apply(item).size();

            if (foundSize == 0) {
                mismatchDescription.appendText("no such elements were found");
                return false;
            }

            if (expectedCount > 0) {
                if (!toMatchCountStrictly) {
                    if (!(foundSize >= expectedCount)) {
                        mismatchDescription.appendText(format("Were expected at least %s items. %s items were found actually",
                                expectedCount, foundSize));
                        return false;
                    }
                    return true;
                }
                else {
                    if (foundSize != expectedCount) {
                        mismatchDescription.appendText(format("Were expected %s items strictly. %s items were found actually",
                                expectedCount, foundSize));
                        return false;
                    }
                    return true;
                }
            }
            return true;
        }
        catch (Throwable e) {
            mismatchDescription.appendText("The attempt to find nested elements was failed. Something went wrong.\n")
                    .appendText(format("Caught throwable: %s\n", e.getClass().getName()))
                    .appendText("Stack trace:\n");

            stream(e.getStackTrace()).forEach(stackTraceElement -> mismatchDescription.appendText(format("%s\n",
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
        if (expectedCount > 0) {
            if (!toMatchCountStrictly) {
                return format("has at least %s nested elements %s", expectedCount,  search.toString());
            }
            return format("has %s nested elements %s", expectedCount,  search.toString());
        }
        return format("has nested elements %s", search.toString());
    }
}
