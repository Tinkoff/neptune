package ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.window;

import org.hamcrest.Matcher;
import ru.tinkoff.qa.neptune.core.api.hamcrest.NeptuneFeatureMatcher;
import ru.tinkoff.qa.neptune.core.api.hamcrest.PropertyValueMismatch;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;
import ru.tinkoff.qa.neptune.core.api.steps.parameters.ParameterValueGetter;
import ru.tinkoff.qa.neptune.selenium.functions.target.locator.window.Window;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.nonNull;
import static org.hamcrest.Matchers.equalTo;
import static ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.window.IsWindowPresentMatcher.windowIsPresent;

@Description("position x {xMatcher} and y {yMatcher}")
public final class WindowHasPositionMatcher extends NeptuneFeatureMatcher<Window> {

    @DescriptionFragment(value = "xMatcher", makeReadableBy = ParameterValueGetter.TranslatedDescriptionParameterValueGetter.class)
    private final Matcher<Integer> xMatcher;
    @DescriptionFragment(value = "yMatcher", makeReadableBy = ParameterValueGetter.TranslatedDescriptionParameterValueGetter.class)
    private final Matcher<Integer> yMatcher;

    private WindowHasPositionMatcher(Matcher<Integer> xMatcher, Matcher<Integer> yMatcher) {
        super(true);
        checkArgument(nonNull(xMatcher), "Criteria to check x-value should be defined");
        checkArgument(nonNull(yMatcher), "Criteria to check y-value should be defined");
        this.xMatcher = xMatcher;
        this.yMatcher = yMatcher;
    }

    /**
     * Creates an instance of {@link WindowHasPositionMatcher} that checks position of a window.
     *
     * @param x expected x value
     * @param y expected y value
     * @return instance of {@link WindowHasPositionMatcher}
     */
    public static WindowHasPositionMatcher windowHasPosition(int x, int y) {
        return windowHasPosition(equalTo(x), equalTo(y));
    }

    /**
     * Creates an instance of {@link WindowHasPositionMatcher} that checks position of a window.
     *
     * @param x        expected x value
     * @param yMatcher y-value criteria
     * @return instance of {@link WindowHasPositionMatcher}
     */
    public static WindowHasPositionMatcher windowHasPosition(int x, Matcher<Integer> yMatcher) {
        return windowHasPosition(equalTo(x), yMatcher);
    }

    /**
     * Creates an instance of {@link WindowHasPositionMatcher} that checks size of a window.
     *
     * @param xMatcher x-value criteria
     * @param y        expected y value
     * @return instance of {@link WindowHasPositionMatcher}
     */
    public static WindowHasPositionMatcher windowHasPosition(Matcher<Integer> xMatcher, int y) {
        return windowHasPosition(xMatcher, equalTo(y));
    }

    /**
     * Creates an instance of {@link WindowHasPositionMatcher} that checks size of a window.
     *
     * @param xMatcher x-value criteria
     * @param yMatcher y-value criteria
     * @return instance of {@link WindowHasPositionMatcher}
     */
    public static WindowHasPositionMatcher windowHasPosition(Matcher<Integer> xMatcher,
                                                             Matcher<Integer> yMatcher) {
        return new WindowHasPositionMatcher(xMatcher, yMatcher);
    }

    @Override
    protected boolean featureMatches(Window toMatch) {
        var windowPresent = windowIsPresent();
        if (windowPresent.matches(toMatch)) {
            appendMismatchDescription(windowPresent, toMatch);
            return false;
        }

        var position = toMatch.getPosition();
        var result = true;

        var x = position.getX();
        var y = position.getY();

        if (!xMatcher.matches(x)) {
            appendMismatchDescription(new PropertyValueMismatch("x", x, xMatcher));
            result = false;
        }

        if (!yMatcher.matches(y)) {
            appendMismatchDescription(new PropertyValueMismatch("y", y, yMatcher));
            result = false;
        }

        return result;
    }
}
