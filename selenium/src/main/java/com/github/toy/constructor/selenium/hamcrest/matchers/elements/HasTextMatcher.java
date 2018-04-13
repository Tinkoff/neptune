package com.github.toy.constructor.selenium.hamcrest.matchers.elements;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.internal.WrapsElement;

import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.String.format;
import static org.hamcrest.Matchers.*;

public final class HasTextMatcher<T extends SearchContext> extends TypeSafeDiagnosingMatcher<T> {

    private final Matcher<String> matcher;

    private HasTextMatcher(Matcher<String> matcher) {
        checkArgument(matcher != null, "Criteria to match text of the element should be defined");
        this.matcher = matcher;
    }

    /**
     * Creates an instance of {@link HasTextMatcher} which checks text entire some {@link SearchContext}.
     * It should be {@link WebElement} or some implementor of {@link WrapsElement}. Otherwise the matching returns {@code false}
     *
     * @param value criteria to check text
     * @return instance of {@link HasTextMatcher}
     */
    public static <T extends SearchContext> HasTextMatcher<T> hasText(Matcher<String> value) {
        return new HasTextMatcher<>(value);
    }

    /**
     * Creates an instance of {@link HasTextMatcher} which checks text entire some {@link SearchContext}.
     * It should be {@link WebElement} or some implementor of {@link WrapsElement}.
     * Otherwise the matching returns {@code false}. It is expected that text is equal to the
     * defined value.
     *
     * @param value which is expected to be equal to text of the element.
     * @return instance of {@link HasTextMatcher}
     */
    public static <T extends SearchContext> HasTextMatcher<T> hasText(String value) {
        return hasText(equalTo(value));
    }

    /**
     * Creates an instance of {@link HasTextMatcher} which checks text entire some {@link SearchContext}.
     * It should be {@link WebElement} or some implementor of {@link WrapsElement}.
     * Otherwise the matching returns {@code false}. It is expected that text is not null or empty value.
     *
     * @return instance of {@link HasTextMatcher}
     */
    public static <T extends SearchContext> HasTextMatcher<T> hasText() {
        return hasText(not(emptyOrNullString()));
    }

    @Override
    protected boolean matchesSafely(T item, Description mismatchDescription) {
        boolean result;
        Class<? extends SearchContext> clazz = item.getClass();
        String text;

        if (WebElement.class.isAssignableFrom(clazz)) {
            result = matcher.matches(text = WebElement.class.cast(item).getText());
        }
        else if (WrapsElement.class.isAssignableFrom(clazz)) {
            WebElement e = WrapsElement.class.cast(item).getWrappedElement();
            if (e == null) {
                mismatchDescription.appendText(format("Wrapped element is null. It is not possible to get text from the instance of %s.",
                        clazz.getName()));
                return false;
            }
            result = matcher.matches(text = e.getText());
        }
        else {
            mismatchDescription.appendText(format("It is not possible to get text from the instance of %s because " +
                            "it does not implement %s or %s", clazz.getName(), WebElement.class,
                    WrapsElement.class.getName()));
            return false;
        }

        if (!result) {
            matcher.describeMismatch(text, mismatchDescription);
        }
        return result;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText(toString());
    }

    public String toString() {
        return format("has text %s", matcher.toString());
    }
}
