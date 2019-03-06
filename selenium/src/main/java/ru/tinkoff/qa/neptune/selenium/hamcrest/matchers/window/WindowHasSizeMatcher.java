package ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.window;

import ru.tinkoff.qa.neptune.selenium.functions.target.locator.window.Window;
import ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.TypeSafeDiagnosingMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;
import org.openqa.selenium.Dimension;

import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.String.format;
import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.hamcrest.Matchers.equalTo;

public final class WindowHasSizeMatcher extends TypeSafeDiagnosingMatcher<Window> {

    private final Matcher<Integer> widthMatcher;
    private final Matcher<Integer> heightMatcher;

    private WindowHasSizeMatcher(Matcher<Integer> widthMatcher, Matcher<Integer> heightMatcher) {
        checkArgument(nonNull(widthMatcher), "Criteria to check width should be defined");
        checkArgument(nonNull(heightMatcher), "Criteria to check height should be defined");
        this.widthMatcher = widthMatcher;
        this.heightMatcher = heightMatcher;
    }

    /**
     * Creates an instance of {@link WindowHasSizeMatcher} that checks size of a window.
     *
     * @param width expected width of the window
     * @param height expected height of the window
     * @return instance of {@link WindowHasSizeMatcher}
     */
    public static WindowHasSizeMatcher windowHasSize(int width, int height) {
        return windowHasSize(equalTo(width), equalTo(height));
    }

    /**
     * Creates an instance of {@link WindowHasSizeMatcher} that checks size of a window.
     *
     * @param width expected width of the window
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
     * @param height expected height of the window
     * @return instance of {@link WindowHasSizeMatcher}
     */
    public static WindowHasSizeMatcher windowHasSize(Matcher<Integer> widthMatcher, int height) {
        return windowHasSize(widthMatcher, equalTo(height));
    }

    /**
     * Creates an instance of {@link WindowHasSizeMatcher} that checks size of a window.
     *
     * @param widthMatcher width criteria
     * @param heightMatcher height criteria
     * @return instance of {@link WindowHasSizeMatcher}
     */
    public static WindowHasSizeMatcher windowHasSize(Matcher<Integer> widthMatcher,
                                                     Matcher<Integer> heightMatcher) {
        return new WindowHasSizeMatcher(widthMatcher, heightMatcher);
    }

    @Override
    protected boolean matchesSafely(Window item, Description mismatchDescription) {
        Dimension size = item.getSize();
        boolean result = (widthMatcher.matches(size.getWidth()) && heightMatcher.matches(size.getHeight()));

        if (!result) {
            Description description = new StringDescription();
            if (!widthMatcher.matches(size.getWidth())) {
                widthMatcher.describeMismatch(size.getWidth(), description.appendText("width: "));
            }

            if (!heightMatcher.matches(size.getHeight())) {
                if (!isBlank(description.toString())) {
                    description.appendText(" ");
                }
                heightMatcher.describeMismatch(size.getHeight(), description.appendText("height: "));
            }
            mismatchDescription.appendText(description.toString());
        }
        return result;
    }

    public String toString() {
        return format("window has width %s and height %s", widthMatcher.toString(),
                heightMatcher.toString());
    }
}
