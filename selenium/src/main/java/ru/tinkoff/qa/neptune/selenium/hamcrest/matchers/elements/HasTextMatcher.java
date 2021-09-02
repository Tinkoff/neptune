package ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.elements;

import org.hamcrest.Matcher;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.internal.WrapsElement;
import ru.tinkoff.qa.neptune.core.api.hamcrest.NeptuneFeatureMatcher;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;
import ru.tinkoff.qa.neptune.core.api.steps.parameters.ParameterValueGetter;
import ru.tinkoff.qa.neptune.selenium.api.widget.HasTextContent;
import ru.tinkoff.qa.neptune.selenium.api.widget.Widget;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.nonNull;
import static org.hamcrest.Matchers.*;

@Description("text: {matcher}")
public final class HasTextMatcher extends NeptuneFeatureMatcher<SearchContext> {

    @DescriptionFragment(value = "matcher", makeReadableBy = ParameterValueGetter.TranslatedDescriptionParameterValueGetter.class)
    private final Matcher<String> matcher;

    private HasTextMatcher(Matcher<String> matcher) {
        super(true, WebElement.class, Widget.class);
        checkArgument(nonNull(matcher), "Criteria to match text of the element should be defined");
        this.matcher = matcher;
    }

    /**
     * Creates an instance of {@link HasTextMatcher} that checks text of some {@link SearchContext}.
     * It should be {@link WebElement} or some implementor of {@link WrapsElement}. Otherwise the matching returns {@code false}
     *
     * @param value criteria to check text
     * @return instance of {@link HasTextMatcher}
     */
    public static Matcher<SearchContext> hasText(Matcher<String> value) {
        return new HasTextMatcher(value);
    }

    /**
     * Creates an instance of {@link HasTextMatcher} that checks text of some {@link SearchContext}.
     * It should be {@link WebElement} or some implementor of {@link WrapsElement}.
     * Otherwise the matching returns {@code false}. It is expected that text is equal to the
     * defined value.
     *
     * @param value is expected to be equal to text of the element.
     * @return instance of {@link HasTextMatcher}
     */
    public static Matcher<SearchContext> hasText(String value) {
        return hasText(equalTo(value));
    }

    /**
     * Creates an instance of {@link HasTextMatcher} that checks text of some {@link SearchContext}.
     * It should be {@link WebElement} or some implementor of {@link WrapsElement}.
     * Otherwise the matching returns {@code false}. It is expected that text is not null or empty value.
     *
     * @return instance of {@link HasTextMatcher}
     */
    public static Matcher<SearchContext> hasText() {
        return hasText(not(emptyOrNullString()));
    }

    @Override
    protected boolean featureMatches(SearchContext toMatch) {
        boolean result;
        var clazz = toMatch.getClass();
        String text;

        if (WebElement.class.isAssignableFrom(clazz)) {
            result = matcher.matches(text = ((WebElement) toMatch).getText());
        } else {
            result = matcher.matches(text = ((HasTextContent) toMatch).getText());
        }

        if (!result) {
            appendMismatchDescription(matcher, text);
        }
        return result;
    }
}
