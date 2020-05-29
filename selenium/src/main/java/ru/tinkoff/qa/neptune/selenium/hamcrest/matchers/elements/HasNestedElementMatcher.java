package ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.elements;

import org.hamcrest.Description;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.SearchContext;
import ru.tinkoff.qa.neptune.core.api.steps.StepFunction;
import ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier;
import ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.TypeSafeDiagnosingMatcher;

import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.String.format;
import static java.lang.System.lineSeparator;
import static java.util.Arrays.stream;
import static java.util.Objects.nonNull;

public final class HasNestedElementMatcher<T extends SearchContext> extends TypeSafeDiagnosingMatcher<T> {

    private static final String LINE_SEPARATOR = lineSeparator();
    private final SearchSupplier<?> search;

    private HasNestedElementMatcher(SearchSupplier<?> search) {
        checkArgument(nonNull(search), "The way to find nested element should be defined");
        this.search = search;
    }

    /**
     * Creates a new instance of {@link HasNestedElementMatcher} and defines the way to find expected nested element.
     *
     * @param search is the way to find desired nested element.
     * @param <T>    is the type of an instance of {@link SearchContext}.
     * @return created instance of {@link HasNestedElementMatcher}
     */
    public static <T extends SearchContext> HasNestedElementMatcher<T> hasNestedElement(SearchSupplier<?> search) {
        return new HasNestedElementMatcher<>(search);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected boolean matchesSafely(T item, Description mismatchDescription) {
        try {
            var f = (StepFunction<SearchContext, ?>) search.get();
            f.turnReportingOff();
            try {
                f.apply(item);
                return true;
            } catch (NoSuchElementException e) {
                mismatchDescription.appendText("no such element was found");
                return false;
            }

        } catch (Throwable e) {
            mismatchDescription.appendText("The attempt to find nested element was failed. Something went wrong." + LINE_SEPARATOR)
                    .appendText(format("Caught throwable: %s%s", e.getClass().getName(), LINE_SEPARATOR))
                    .appendText("Stack trace:" + LINE_SEPARATOR);

            stream(e.getStackTrace()).forEach(stackTraceElement -> mismatchDescription.appendText(format("%s%s",
                    stackTraceElement.toString(), LINE_SEPARATOR)));
            return false;
        }
    }

    public String toString() {
        return format("has nested element %s", search.toString());
    }
}
