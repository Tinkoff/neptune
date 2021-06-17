package ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.window;

import org.hamcrest.Matcher;
import ru.tinkoff.qa.neptune.core.api.hamcrest.NeptuneFeatureMatcher;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;
import ru.tinkoff.qa.neptune.selenium.functions.target.locator.window.Window;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.nonNull;
import static org.hamcrest.Matchers.equalTo;
import static ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.window.IsWindowPresentMatcher.windowIsPresent;

@Description("title {titleMatcher}")
public final class WindowHasTitleMatcher extends NeptuneFeatureMatcher<Window> {

    @DescriptionFragment("titleMatcher")
    private final Matcher<String> titleMatcher;

    private WindowHasTitleMatcher(Matcher<String> titleMatcher) {
        super(true);
        checkArgument(nonNull(titleMatcher), "Criteria to match title of a window should be defined");
        this.titleMatcher = titleMatcher;
    }

    /**
     * Creates an instance of {@link WindowHasTitleMatcher} that checks attribute of an instance of {@link Window}.
     *
     * @param titleMatcher criteria to check title of a window
     * @return an instance of a {@link WindowHasTitleMatcher}
     */
    public static WindowHasTitleMatcher windowHasTitle(Matcher<String> titleMatcher) {
        return new WindowHasTitleMatcher(titleMatcher);
    }

    /**
     * Creates an instance of {@link WindowHasTitleMatcher} that checks attribute of an instance of {@link Window}.
     *
     * @param title expected title of a window
     * @return an instance of a {@link WindowHasTitleMatcher}
     */
    public static WindowHasTitleMatcher windowHasTitle(String title) {
        return windowHasTitle(equalTo(title));
    }

    @Override
    protected boolean featureMatches(Window toMatch) {
        var windowPresent = windowIsPresent();
        if (!windowPresent.matches(toMatch)) {
            appendMismatchDescription(windowPresent, toMatch);
            return false;
        }

        var title = toMatch.getTitle();
        if (!titleMatcher.matches(title)) {
            appendMismatchDescription(titleMatcher, title);
            return false;
        }

        return true;
    }
}
