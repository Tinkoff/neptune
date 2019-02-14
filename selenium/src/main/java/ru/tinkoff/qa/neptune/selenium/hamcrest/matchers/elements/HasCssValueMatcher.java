package ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.elements;

import ru.tinkoff.qa.neptune.selenium.api.widget.HasCssValue;
import ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.TypeSafeDiagnosingMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.internal.WrapsElement;

import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.String.format;
import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.hamcrest.Matchers.*;

public final class HasCssValueMatcher<T extends SearchContext> extends TypeSafeDiagnosingMatcher<T> {

    private final String cssProperty;
    private final Matcher<String> matcher;

    private HasCssValueMatcher(String cssProperty, Matcher<String> matcher) {
        checkArgument(!isBlank(cssProperty), "Css property should not be blank");
        checkArgument(nonNull(matcher), "Criteria to match value of the css property should be defined");
        this.cssProperty = cssProperty;
        this.matcher = matcher;
    }

    /**
     * Creates an instance of {@link HasCssValueMatcher} which checks some css property of an instance of {@link SearchContext}.
     * It should be {@link WebElement} or some implementor of {@link HasCssValue} or {@link WrapsElement}.
     * Otherwise the matching returns {@code false}
     *
     * @param cssProperty to be checked
     * @param value criteria to check the css property
     * @param <T> is a type of a value to be matched. It should extend {@link WebElement} or it should extend both
     *           {@link SearchContext} and {@link HasCssValue}.
     * @return instance of {@link HasCssValueMatcher}
     */
    public static <T extends SearchContext> HasCssValueMatcher<T> hasCss(String cssProperty, Matcher<String> value) {
        return new HasCssValueMatcher<>(cssProperty, value);
    }

    /**
     * Creates an instance of {@link HasCssValueMatcher} which checks some css property  of an instance of {@link SearchContext}.
     * It should be {@link WebElement} or some implementor of {@link HasCssValue} or {@link WrapsElement}.
     * Otherwise the matching returns {@code false}. It is expected that value of the css property is equal to the
     * defined value.
     *
     * @param cssProperty to be checked
     * @param value which is expected to be equal to value of the css property.
     * @param <T> is a type of a value to be matched. It should extend {@link WebElement} or it should extend both
     *           {@link SearchContext} and {@link HasCssValue}.
     * @return instance of {@link HasCssValueMatcher}
     */
    public static <T extends SearchContext> HasCssValueMatcher<T> hasCss(String cssProperty, String value) {
        return hasCss(cssProperty, equalTo(value));
    }

    /**
     * Creates an instance of {@link HasCssValueMatcher} which checks some css property  of an instance of {@link SearchContext}.
     * It should be {@link WebElement} or some implementor of {@link HasCssValue} or {@link WrapsElement}.
     * Otherwise the matching returns {@code false}. It is expected that value of the css property is not null or empty
     * value.
     *
     * @param cssProperty to be checked
     * @param <T> is a type of a value to be matched. It should extend {@link WebElement} or it should extend both
     *           {@link SearchContext} and {@link HasCssValue}.
     * @return instance of {@link HasCssValueMatcher}
     */
    public static <T extends SearchContext> HasCssValueMatcher<T> hasCss(String cssProperty) {
        return hasCss(cssProperty, not(emptyOrNullString()));
    }

    @Override
    protected boolean matchesSafely(T item, Description mismatchDescription) {
        boolean result;
        var clazz = item.getClass();
        String cssValue;

        if (WebElement.class.isAssignableFrom(clazz)) {
            result = matcher.matches(cssValue = ((WebElement) item).getCssValue(cssProperty));
        }
        else if (HasCssValue.class.isAssignableFrom(clazz)){
            result = matcher.matches(cssValue = ((HasCssValue) item).getCssValue(cssProperty));
        }
        else if (WrapsElement.class.isAssignableFrom(clazz)) {
            var e = ((WrapsElement) item).getWrappedElement();
            if (e == null) {
                mismatchDescription.appendText(format("Wrapped element is null. It is not possible to get css property %s from an instance of %s.",
                        cssProperty, clazz.getName()));
                return false;
            }
            result = matcher.matches(cssValue = e.getCssValue(cssProperty));
        }
        else {
            mismatchDescription.appendText(format("It is not possible to get css property %s from the instance of %s because " +
                            "it does not implement %s, %s or %s", cssProperty, clazz.getName(), WebElement.class,
                    HasCssValue.class.getName(),
                    WrapsElement.class.getName()));
            return false;
        }

        if (!result) {
            matcher.describeMismatch(cssValue, mismatchDescription);
        }
        return result;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText(toString());
    }

    public String toString() {
        return format("has css property %s %s", cssProperty, matcher.toString());
    }
}
