package ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.window;

import org.hamcrest.Matcher;
import ru.tinkoff.qa.neptune.core.api.hamcrest.NeptuneFeatureMatcher;
import ru.tinkoff.qa.neptune.core.api.hamcrest.PropertyValueMismatch;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;
import ru.tinkoff.qa.neptune.core.api.steps.parameters.ParameterValueGetter;
import ru.tinkoff.qa.neptune.selenium.functions.target.locator.window.Window;
import ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.descriptions.Height;
import ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.descriptions.Width;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.nonNull;
import static org.hamcrest.Matchers.equalTo;
import static ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.window.IsWindowPresentMatcher.windowIsPresent;

@Description("size width {widthMatcher} and height {heightMatcher}")
public final class WindowHasSizeMatcher extends NeptuneFeatureMatcher<Window> {

    @DescriptionFragment(value = "widthMatcher", makeReadableBy = ParameterValueGetter.TranslatedDescriptionParameterValueGetter.class)
    private final Matcher<Integer> widthMatcher;
    @DescriptionFragment(value = "heightMatcher", makeReadableBy = ParameterValueGetter.TranslatedDescriptionParameterValueGetter.class)
    private final Matcher<Integer> heightMatcher;

    private WindowHasSizeMatcher(Matcher<Integer> widthMatcher, Matcher<Integer> heightMatcher) {
        super(true);
        checkArgument(nonNull(widthMatcher), "Criteria to check width should be defined");
        checkArgument(nonNull(heightMatcher), "Criteria to check height should be defined");
        this.widthMatcher = widthMatcher;
        this.heightMatcher = heightMatcher;
    }

    /**
     * Creates an instance of {@link WindowHasSizeMatcher} that checks size of a window.
     *
     * @param width  expected width of the window
     * @param height expected height of the window
     * @return instance of {@link WindowHasSizeMatcher}
     */
    public static WindowHasSizeMatcher windowHasSize(int width, int height) {
        return windowHasSize(equalTo(width), equalTo(height));
    }

    /**
     * Creates an instance of {@link WindowHasSizeMatcher} that checks size of a window.
     *
     * @param width         expected width of the window
     * @param heightMatcher height criteria
     * @return instance of {@link WindowHasSizeMatcher}
     */
    public static WindowHasSizeMatcher windowHasSize(int width, Matcher<Integer> heightMatcher) {
        return windowHasSize(equalTo(width), heightMatcher);
    }

    /**
     * Creates an instance of {@link WindowHasSizeMatcher} that checks size of a window.
     *
     * @param widthMatcher width criteria
     * @param height       expected height of the window
     * @return instance of {@link WindowHasSizeMatcher}
     */
    public static WindowHasSizeMatcher windowHasSize(Matcher<Integer> widthMatcher, int height) {
        return windowHasSize(widthMatcher, equalTo(height));
    }

    /**
     * Creates an instance of {@link WindowHasSizeMatcher} that checks size of a window.
     *
     * @param widthMatcher  width criteria
     * @param heightMatcher height criteria
     * @return instance of {@link WindowHasSizeMatcher}
     */
    public static WindowHasSizeMatcher windowHasSize(Matcher<Integer> widthMatcher,
                                                     Matcher<Integer> heightMatcher) {
        return new WindowHasSizeMatcher(widthMatcher, heightMatcher);
    }

    @Override
    protected boolean featureMatches(Window toMatch) {
        var windowPresent = windowIsPresent();
        if (!windowPresent.matches(toMatch)) {
            appendMismatchDescription(windowPresent, toMatch);
            return false;
        }

        var size = toMatch.getSize();
        var result = true;

        var width = size.getWidth();
        var height = size.getHeight();

        if (!widthMatcher.matches(width)) {
            appendMismatchDescription(new PropertyValueMismatch(new Width(), width, widthMatcher));
            result = false;
        }

        if (!heightMatcher.matches(height)) {
            appendMismatchDescription(new PropertyValueMismatch(new Height(), height, heightMatcher));
            result = false;
        }

        return result;
    }
}
