package ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.window;

import ru.tinkoff.qa.neptune.core.api.hamcrest.NeptuneFeatureMatcher;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.selenium.functions.target.locator.window.Window;
import ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.descriptions.WindowIsNotPresentMismatch;

@Description("is currently present")
public final class IsWindowPresentMatcher extends NeptuneFeatureMatcher<Window> {

    private IsWindowPresentMatcher() {
        super(true);
    }

    /**
     * Creates an instance of a matcher that checks presence of a window.
     *
     * @return a new instance of {@link IsWindowPresentMatcher}
     */
    public static IsWindowPresentMatcher windowIsPresent() {
        return new IsWindowPresentMatcher();
    }

    @Override
    protected boolean featureMatches(Window toMatch) {
        var result = toMatch.isPresent();
        if (!result) {
            appendMismatchDescription(new WindowIsNotPresentMismatch());
        }
        return result;
    }
}
