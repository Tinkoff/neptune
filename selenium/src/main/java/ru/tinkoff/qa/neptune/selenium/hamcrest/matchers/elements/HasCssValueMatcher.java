package ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.elements;

import org.hamcrest.Matcher;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import ru.tinkoff.qa.neptune.core.api.hamcrest.NeptuneFeatureMatcher;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;
import ru.tinkoff.qa.neptune.selenium.api.widget.HasCssValue;
import ru.tinkoff.qa.neptune.selenium.api.widget.Widget;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.hamcrest.Matchers.*;

@Description("css property '{cssProperty}' {matcher}")
public final class HasCssValueMatcher extends NeptuneFeatureMatcher<SearchContext> {

    @DescriptionFragment("cssProperty")
    private final String cssProperty;
    @DescriptionFragment(value = "matcher")
    private final Matcher<String> matcher;

    private HasCssValueMatcher(String cssProperty, Matcher<String> matcher) {
        super(true, WebElement.class, Widget.class);
        checkArgument(!isBlank(cssProperty), "Css property should not be blank");
        checkArgument(nonNull(matcher), "Criteria to match value of the css property should be defined");
        this.cssProperty = cssProperty;
        this.matcher = matcher;
    }

    /**
     * Creates an instance of {@link HasCssValueMatcher} that checks some css property of an instance of {@link SearchContext}.
     * It should be {@link WebElement} or some implementor of {@link HasCssValue}.
     * Otherwise the matching returns {@code false}
     *
     * @param cssProperty to be checked
     * @param value       criteria to check the css property
     * @return instance of {@link HasCssValueMatcher}
     */
    public static Matcher<SearchContext> hasCss(String cssProperty, Matcher<String> value) {
        return new HasCssValueMatcher(cssProperty, value);
    }

    /**
     * Creates an instance of {@link HasCssValueMatcher} that checks some css property  of an instance of {@link SearchContext}.
     * It should be {@link WebElement} or some implementor of {@link HasCssValue}.
     * Otherwise the matching returns {@code false}. It is expected that value of the css property is equal to the
     * defined value.
     *
     * @param cssProperty to be checked
     * @param value       is expected to be equal to value of the css property.
     * @return instance of {@link HasCssValueMatcher}
     */
    public static Matcher<SearchContext> hasCss(String cssProperty, String value) {
        return hasCss(cssProperty, equalTo(value));
    }

    /**
     * Creates an instance of {@link HasCssValueMatcher} that checks some css property  of an instance of {@link SearchContext}.
     * It should be {@link WebElement} or some implementor of {@link HasCssValue}.
     * Otherwise the matching returns {@code false}. It is expected that value of the css property is not null or empty
     * value.
     *
     * @param cssProperty to be checked
     * @return instance of {@link HasCssValueMatcher}
     */
    public static Matcher<SearchContext> hasCss(String cssProperty) {
        return hasCss(cssProperty, not(emptyOrNullString()));
    }

    @Override
    protected boolean featureMatches(SearchContext toMatch) {
        boolean result;
        var clazz = toMatch.getClass();
        String cssValue;

        if (WebElement.class.isAssignableFrom(clazz)) {
            result = matcher.matches(cssValue = ((WebElement) toMatch).getCssValue(cssProperty));
        } else {
            result = matcher.matches(cssValue = ((HasCssValue) toMatch).getCssValue(cssProperty));
        }

        if (!result) {
            appendMismatchDescription(matcher, cssValue);
        }
        return result;
    }
}
