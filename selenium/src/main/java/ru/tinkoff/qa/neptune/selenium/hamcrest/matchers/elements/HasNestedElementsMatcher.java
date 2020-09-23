package ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.elements;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.openqa.selenium.SearchContext;
import ru.tinkoff.qa.neptune.core.api.steps.StepFunction;
import ru.tinkoff.qa.neptune.selenium.functions.searching.MultipleSearchSupplier;
import ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.TypeSafeDiagnosingMatcher;

import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.String.format;
import static java.lang.System.lineSeparator;
import static java.util.Arrays.stream;
import static java.util.Objects.nonNull;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;

public final class HasNestedElementsMatcher<T extends SearchContext> extends TypeSafeDiagnosingMatcher<T> {

    private static final String LINE_SEPARATOR = lineSeparator();
    private final MultipleSearchSupplier<?> search;
    private Matcher<Integer> expectedCount = greaterThan(0);

    private HasNestedElementsMatcher(MultipleSearchSupplier<?> search) {
        checkArgument(nonNull(search), "The way to find nested elements should be defined");
        this.search = search;
    }

    /**
     * Creates a new instance of {@link HasNestedElementsMatcher} and defines the way to find expected nested elements.
     *
     * @param search is the way to find desired nested elements.
     * @param <T>    is the type of an instance of {@link SearchContext}.
     * @return created instance of {@link HasNestedElementsMatcher}
     */
    public static <T extends SearchContext> HasNestedElementsMatcher<T> hasNestedElements(MultipleSearchSupplier<?> search) {
        return new HasNestedElementsMatcher<>(search);
    }

    /**
     * Sets expected count of nested elements.
     *
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
     *
     * @param matcher is criteria to check the count of found elements
     * @return self-reference.
     */
    public HasNestedElementsMatcher<T> withCount(Matcher<Integer> matcher) {
        checkArgument(nonNull(matcher), "Criteria to check the count of found elements should be defined");
        this.expectedCount = matcher;
        return this;
    }

    @Override
    protected boolean matchesSafely(T item, Description mismatchDescription) {
        try {
            var f = (StepFunction<SearchContext, ? extends List<?>>) search.get();
            f.turnReportingOff();
            var foundSize = f.apply(item).size();

            if (!expectedCount.matches(foundSize)) {
                mismatchDescription.appendText(format("actual count %s of found items doesn't meet the criteria %s",
                        foundSize, expectedCount));
                return false;
            }
            return true;
        } catch (Throwable e) {
            mismatchDescription.appendText("The attempt to find nested elements has failed. Something went wrong." + LINE_SEPARATOR)
                    .appendText(format("Caught throwable: %s%s", e.getClass().getName(), LINE_SEPARATOR))
                    .appendText("Stack trace:" + LINE_SEPARATOR);

            stream(e.getStackTrace())
                    .forEach(stackTraceElement -> mismatchDescription.appendText(format("%s%s",
                            stackTraceElement.toString(), LINE_SEPARATOR)));
            return false;
        }
    }

    @Override
    public String toString() {
        return format("has nested elements %s. Count should meet the criteria %s", search.toString(),
                expectedCount);
    }
}
