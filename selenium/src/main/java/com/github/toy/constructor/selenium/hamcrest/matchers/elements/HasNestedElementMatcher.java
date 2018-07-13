package com.github.toy.constructor.selenium.hamcrest.matchers.elements;

import com.github.toy.constructor.selenium.functions.searching.SearchSupplier;
import com.github.toy.constructor.selenium.hamcrest.matchers.TypeSafeDiagnosingMatcher;
import org.hamcrest.Description;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.SearchContext;

import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.String.format;
import static java.util.Arrays.stream;

public final class HasNestedElementMatcher<T extends SearchContext> extends TypeSafeDiagnosingMatcher<T> {

    private final SearchSupplier<?> search;

    private HasNestedElementMatcher(SearchSupplier<?> search) {
        checkArgument(search != null, "The way to find nested element should be defined");
        this.search = search;
    }

    /**
     * Creates a new instance of {@link HasNestedElementMatcher} and defines the way to find desired nested element.
     * @param search is the way to find desired nested element.
     * @param <T> is the type of an instance of {@link SearchContext} which is expected to contain desired nested element.
     * @return created instance of {@link HasNestedElementMatcher}
     */
    public static <T extends SearchContext> HasNestedElementMatcher<T> hasNestedElement(SearchSupplier<?> search) {
        return new HasNestedElementMatcher<>(search);
    }

    @Override
    protected boolean matchesSafely(T item, Description mismatchDescription) {
        try {
            search.get().apply(item);
            return true;
        }
        catch (NoSuchElementException e) {
            mismatchDescription.appendText("no such element was found");
            return false;
        }
        catch (Throwable e) {
            mismatchDescription.appendText("The attempt to find nested element was failed. Something went wrong.\n")
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

    public String toString() {
        return format("has nested element %s", search.toString());
    }
}
