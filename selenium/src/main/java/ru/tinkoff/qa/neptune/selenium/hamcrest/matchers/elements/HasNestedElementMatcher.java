package ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.elements;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.SearchContext;
import ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier;
import ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.TypeSafeDiagnosingMatcher;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.lang.String.format;
import static java.lang.System.lineSeparator;
import static java.util.Arrays.asList;
import static java.util.Arrays.stream;
import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.joining;
import static ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier.turnReportingOff;

public final class HasNestedElementMatcher<T extends SearchContext> extends TypeSafeDiagnosingMatcher<T> {

    private static final String LINE_SEPARATOR = lineSeparator();
    private final SearchSupplier<?> search;
    private final List<Matcher<T>> elementMatchers = new ArrayList<>();

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

    /**
     * Defines additional criteria that nested element should match
     *
     * @param criteria additional criteria that nested element should match
     * @return self-reference
     */
    @SafeVarargs
    public final HasNestedElementMatcher<T> satisfies(Matcher<T>... criteria) {
        checkNotNull(criteria);

        elementMatchers.addAll(asList(criteria));
        return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    protected boolean matchesSafely(T item, Description mismatchDescription) {
        try {
            var f = turnReportingOff(search.clone()).get();
            try {
                f.apply(item);

                var result = true;

                for (var m : elementMatchers) {
                    if (!m.matches(item)) {
                        result = false;
                        m.describeMismatch(item, mismatchDescription);
                    }
                }

                return result;
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
        var result = format("has nested element %s", search.toString());
        if (elementMatchers.size() > 0) {
            result = format("%s: %s", result, elementMatchers.stream().map(Object::toString).collect(joining(",")));
        }
        return result;
    }
}
