package ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.elements;

import org.hamcrest.Matcher;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import ru.tinkoff.qa.neptune.core.api.hamcrest.NeptuneFeatureMatcher;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;
import ru.tinkoff.qa.neptune.selenium.api.widget.HasAttribute;
import ru.tinkoff.qa.neptune.selenium.api.widget.Widget;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.hamcrest.Matchers.*;

@Description("html attribute '{attribute}' {matcher}")
public final class HasAttributeMatcher extends NeptuneFeatureMatcher<SearchContext> {

    @DescriptionFragment("attribute")
    private final String attribute;

    @DescriptionFragment(value = "matcher")
    private final Matcher<String> matcher;

    private HasAttributeMatcher(String attribute, Matcher<String> matcher) {
        super(true, WebElement.class, Widget.class);
        checkArgument(!isBlank(attribute), "Attribute should not be blank");
        checkArgument(nonNull(matcher), "Criteria to match value of the attribute should be defined");
        this.attribute = attribute;
        this.matcher = matcher;
    }

    /**
     * Creates an instance of {@link HasAttributeMatcher} that checks some attribute of an instance of {@link SearchContext}.
     * It should be {@link WebElement} or some implementor of {@link HasAttribute}.
     * Otherwise the matching returns {@code false}
     *
     * @param attribute to be checked
     * @param value     criteria to check the attribute
     * @return instance of {@link HasAttributeMatcher}
     */
    public static Matcher<SearchContext> hasAttribute(String attribute, Matcher<String> value) {
        return new HasAttributeMatcher(attribute, value);
    }

    /**
     * Creates an instance of {@link HasAttributeMatcher} that checks some attribute of an instance of {@link SearchContext}.
     * It should be {@link WebElement} or some implementor of {@link HasAttribute}.
     * Otherwise the matching returns {@code false}. It is expected that value of the attribute is equal to the
     * defined value.
     *
     * @param attribute to be checked
     * @param value     is expected to be equal to value of the attribute.
     * @return instance of {@link HasAttributeMatcher}
     */
    public static Matcher<SearchContext> hasAttribute(String attribute, String value) {
        return hasAttribute(attribute, equalTo(value));
    }

    /**
     * Creates an instance of {@link HasAttributeMatcher} that checks some attribute of an instance of {@link SearchContext}.
     * It should be {@link WebElement} or some implementor of {@link HasAttribute}.
     * Otherwise the matching returns {@code false}. It is expected that value of the attribute is not null or empty
     * value.
     *
     * @param attribute to be checked
     * @return instance of {@link HasAttributeMatcher}
     */
    public static Matcher<SearchContext> hasAttribute(String attribute) {
        return hasAttribute(attribute, not(emptyOrNullString()));
    }

    @Override
    protected boolean featureMatches(SearchContext toMatch) {
        boolean result;
        var clazz = toMatch.getClass();
        String attrValue;

        if (WebElement.class.isAssignableFrom(clazz)) {
            result = matcher.matches(attrValue = ((WebElement) toMatch).getAttribute(attribute));
        } else {
            result = matcher.matches(attrValue = ((HasAttribute) toMatch).getAttribute(attribute));
        }

        if (!result) {
            appendMismatchDescription(matcher, attrValue);
        }
        return result;
    }
}
