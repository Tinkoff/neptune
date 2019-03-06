package ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.window;

import ru.tinkoff.qa.neptune.selenium.functions.target.locator.window.Window;
import ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.TypeSafeDiagnosingMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;
import org.openqa.selenium.Point;

import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.String.format;
import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.hamcrest.Matchers.equalTo;

public final class WindowHasPositionMatcher extends TypeSafeDiagnosingMatcher<Window> {

    private final Matcher<Integer> xMatcher;
    private final Matcher<Integer> yMatcher;

    private WindowHasPositionMatcher(Matcher<Integer> xMatcher, Matcher<Integer> yMatcher) {
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
     * @param x expected x value
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
     * @param y expected y value
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
    protected boolean matchesSafely(Window item, Description mismatchDescription) {
        Point position = item.getPosition();
        boolean result = (xMatcher.matches(position.getX()) && yMatcher.matches(position.getY()));

        if (!result) {
            Description description = new StringDescription();
            if (!xMatcher.matches(position.getX())) {
                xMatcher.describeMismatch(position.getX(), description.appendText("x: "));
            }

            if (!yMatcher.matches(position.getY())) {
                if (!isBlank(description.toString())) {
                    description.appendText(" ");
                }
                yMatcher.describeMismatch(position.getY(), description.appendText("y: "));
            }
            mismatchDescription.appendText(description.toString());
        }
        return result;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText(toString());
    }

    public String toString() {
        return format("window has position x %s and y %s", xMatcher.toString(),
                yMatcher.toString());
    }
}
