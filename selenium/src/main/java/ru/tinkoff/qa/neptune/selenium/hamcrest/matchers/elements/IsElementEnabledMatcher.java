package ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.elements;

import org.hamcrest.Matcher;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import ru.tinkoff.qa.neptune.core.api.hamcrest.NeptuneFeatureMatcher;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.selenium.api.widget.IsEnabled;
import ru.tinkoff.qa.neptune.selenium.api.widget.Widget;
import ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.descriptions.NotEnabledMismatch;

@Description("enabled")
public final class IsElementEnabledMatcher extends NeptuneFeatureMatcher<SearchContext> {

    private IsElementEnabledMatcher() {
        super(true, WebElement.class, Widget.class);
    }

    /**
     * Creates an instance of {@link IsElementEnabledMatcher} that checks is some instance of {@link SearchContext} enabled or not.
     * It should be {@link WebElement} or some implementor of {@link IsEnabled}.
     * Otherwise the matching returns {@code false}
     *
     * @return an instance of {@link IsElementEnabledMatcher}
     */
    public static Matcher<SearchContext> isEnabled() {
        return new IsElementEnabledMatcher();
    }

    @Override
    protected boolean featureMatches(SearchContext toMatch) {
        boolean result;
        var clazz = toMatch.getClass();

        if (WebElement.class.isAssignableFrom(clazz)) {
            result = ((WebElement) toMatch).isEnabled();
        } else {
            result = ((IsEnabled) toMatch).isEnabled();
        }

        if (!result) {
            appendMismatchDescription(new NotEnabledMismatch());
        }
        return result;
    }
}
