package ru.tinkoff.qa.neptune.check;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.String.format;
import static java.lang.System.lineSeparator;
import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

class InnerMatcher<T> extends BaseMatcher<T> {

    private static final String LINE_SEPARATOR = lineSeparator();
    private final String description;
    private final Matcher<? super T> criteria;

    InnerMatcher(String description, Matcher<? super T> criteria) {
        checkArgument(isNotBlank(description), "Description of a value to be evaluated and checked should not be blank");
        checkArgument(nonNull(criteria), "Criteria matcher should not be null");
        this.description = description;
        this.criteria = criteria;
    }

    @Override
    public void describeTo(Description description) {
        criteria.describeTo(description);
    }

    @Override
    public void describeMismatch(Object item, Description mismatchDescription) {
        mismatchDescription.appendText(format("%s: %s", description, LINE_SEPARATOR));
        criteria.describeMismatch(item, mismatchDescription);
    }

    @Override
    public boolean matches(Object item) {
        return criteria.matches(item);
    }
}
