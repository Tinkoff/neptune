package ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.elements;

import org.hamcrest.Matcher;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import ru.tinkoff.qa.neptune.core.api.hamcrest.NeptuneFeatureMatcher;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.selenium.api.widget.IsVisible;
import ru.tinkoff.qa.neptune.selenium.api.widget.Widget;
import ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.descriptions.NotVisibleMismatch;

@Description("visible")
public final class IsElementVisibleMatcher extends NeptuneFeatureMatcher<SearchContext> {

    private IsElementVisibleMatcher() {
        super(true, WebElement.class, Widget.class);
    }

    /**
     * Creates an instance of {@link IsElementVisibleMatcher} that checks visibility of some instance of {@link SearchContext}.
     * It should be {@link WebElement} or some implementor of {@link IsVisible}.
     * Otherwise the matching returns {@code false}
     *
     * @return an instance of {@link IsElementVisibleMatcher}
     */
    public static Matcher<SearchContext> isVisible() {
        return new IsElementVisibleMatcher();
    }

    @Override
    protected boolean featureMatches(SearchContext toMatch) {
        boolean result;
        var clazz = toMatch.getClass();

        if (WebElement.class.isAssignableFrom(clazz)) {
            result = ((WebElement) toMatch).isDisplayed();
        } else {
            result = ((IsVisible) toMatch).isVisible();
        }

        if (!result) {
            appendMismatchDescription(new NotVisibleMismatch());
        }
        return result;
    }
}
