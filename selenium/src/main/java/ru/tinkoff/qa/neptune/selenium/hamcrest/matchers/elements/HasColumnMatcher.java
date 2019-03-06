package ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.elements;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import ru.tinkoff.qa.neptune.selenium.api.widget.drafts.Table;
import ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.TypeSafeDiagnosingMatcher;

import static java.lang.String.format;

public class HasColumnMatcher<T extends Iterable<?>> extends TypeSafeDiagnosingMatcher<Table> {

    private final Matcher<? super String> columnMatcher;
    private final Matcher<T> matcher;

    private HasColumnMatcher(Matcher<? super String> columnMatcher, Matcher<T> matcher) {
        this.columnMatcher = columnMatcher;
        this.matcher = matcher;
    }

    @Override
    protected boolean matchesSafely(Table item, Description mismatchDescription) {
        return false;
    }

    @Override
    public String toString() {
        return null;
    }
}
