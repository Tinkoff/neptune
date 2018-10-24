package ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.elements;

import ru.tinkoff.qa.neptune.selenium.api.widget.HasAttribute;
import ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.TypeSafeDiagnosingMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.internal.WrapsElement;

import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.String.format;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.hamcrest.Matchers.emptyOrNullString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;

public final class HasAttributeMatcher<T extends SearchContext> extends TypeSafeDiagnosingMatcher<T> {

    private final String attribute;
    private final Matcher<String> matcher;

    private HasAttributeMatcher(String attribute, Matcher<String> matcher) {
        checkArgument(!isBlank(attribute), "Attribute should not be blank");
        checkArgument(matcher != null, "Criteria to match value of the attribute should be defined");
        this.attribute = attribute;
        this.matcher = matcher;
    }

    /**
     * Creates an instance of {@link HasAttributeMatcher} which checks some attribute of an instance of {@link SearchContext}.
     * It should be {@link WebElement} or some implementor of {@link HasAttribute} or {@link WrapsElement}.
     * Otherwise the matching returns {@code false}
     *
     * @param attribute to be checked
     * @param value criteria to check the attribute
     * @param <T> is a type of a value to be matched. It should extend {@link WebElement} or it should extend both
     *           {@link SearchContext} and {@link HasAttribute}.
     * @return instance of {@link HasAttributeMatcher}
     */
    public static <T extends SearchContext> HasAttributeMatcher<T> hasAttribute(String attribute, Matcher<String> value) {
        return new HasAttributeMatcher<>(attribute, value);
    }

    /**
     * Creates an instance of {@link HasAttributeMatcher} which checks some attribute of an instance of {@link SearchContext}.
     * It should be {@link WebElement} or some implementor of {@link HasAttribute} or {@link WrapsElement}.
     * Otherwise the matching returns {@code false}. It is expected that value of the attribute is equal to the
     * defined value.
     *
     * @param attribute to be checked
     * @param value which is expected to be equal to value of the attribute.
     * @param <T> is a type of a value to be matched. It should extend {@link WebElement} or it should extend both
     *           {@link SearchContext} and {@link HasAttribute}.
     * @return instance of {@link HasAttributeMatcher}
     */
    public static <T extends SearchContext> HasAttributeMatcher<T> hasAttribute(String attribute, String value) {
        return hasAttribute(attribute, equalTo(value));
    }

    /**
     * Creates an instance of {@link HasAttributeMatcher} which checks some attribute of an instance of {@link SearchContext}.
     * It should be {@link WebElement} or some implementor of {@link HasAttribute} or {@link WrapsElement}.
     * Otherwise the matching returns {@code false}. It is expected that value of the attribute is not null or empty
     * value.
     *
     * @param attribute to be checked
     * @param <T> is a type of a value to be matched. It should extend {@link WebElement} or it should extend both
     *           {@link SearchContext} and {@link HasAttribute}.
     * @return instance of {@link HasAttributeMatcher}
     */
    public static <T extends SearchContext> HasAttributeMatcher<T> hasAttribute(String attribute) {
        return hasAttribute(attribute, not(emptyOrNullString()));
    }

    @Override
    protected boolean matchesSafely(T item, Description mismatchDescription) {
        boolean result;
        var clazz = item.getClass();
        String attrValue;

        if (WebElement.class.isAssignableFrom(clazz)) {
            result = matcher.matches(attrValue = WebElement.class.cast(item).getAttribute(attribute));
        }
        else if (HasAttribute.class.isAssignableFrom(clazz)){
            result = matcher.matches(attrValue = HasAttribute.class.cast(item).getAttribute(attribute));
        }
        else if (WrapsElement.class.isAssignableFrom(clazz)) {
            var e = WrapsElement.class.cast(item).getWrappedElement();
            if (e == null) {
                mismatchDescription.appendText(format("Wrapped element is null. It is not possible to get attribute %s from an instance of %s.",
                        attribute, clazz.getName()));
                return false;
            }
            result = matcher.matches(attrValue = e.getAttribute(attribute));
        }
        else {
            mismatchDescription.appendText(format("It is not possible to get attribute %s from the instance of %s because " +
                            "it does not implement %s, %s or %s", attribute, clazz.getName(), WebElement.class,
                    HasAttribute.class.getName(),
                    WrapsElement.class.getName()));
            return false;
        }

        if (!result) {
            matcher.describeMismatch(attrValue, mismatchDescription);
        }
        return result;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText(toString());
    }

    public String toString() {
        return format("has attribute %s %s", attribute, matcher.toString());
    }
}
