package ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.window;

import ru.tinkoff.qa.neptune.selenium.functions.target.locator.window.Window;
import ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.TypeSafeDiagnosingMatcher;
import org.hamcrest.Description;

public final class IsWindowPresentMatcher extends TypeSafeDiagnosingMatcher<Window> {

    private IsWindowPresentMatcher() {
        super();
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
    protected boolean matchesSafely(Window item, Description mismatchDescription) {
        boolean result = item.isPresent();
        if (!result) {
            mismatchDescription.appendText("window/tab is not present currently");
        }
        return result;
    }

    public String toString() {
        return "window/tab is currently present";
    }
}
