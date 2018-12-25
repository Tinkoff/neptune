package ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.frame;

import org.openqa.selenium.WrapsDriver;
import ru.tinkoff.qa.neptune.selenium.functions.target.locator.frame.Frame;
import ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.TypeSafeDiagnosingMatcher;
import org.hamcrest.Description;
import org.openqa.selenium.NoSuchFrameException;
import org.openqa.selenium.WebDriver;
import java.util.function.Function;

import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.String.format;
import static java.util.Objects.nonNull;

public final class HasFrameMatcher extends TypeSafeDiagnosingMatcher<WrapsDriver> {

    private final Function<WebDriver, Frame> howToGetFrame;

    private HasFrameMatcher(Function<WebDriver, Frame> howToGetFrame) {
        checkArgument(nonNull(howToGetFrame), "");
        this.howToGetFrame = howToGetFrame;
    }

    /**
     * Creates an instance of {@link HasFrameMatcher} which checks presence of some frame.

     * @param howToGetFrame criteria of the frame to get/switch to
     * @return instance of {@link HasFrameMatcher}
     */
    public static HasFrameMatcher hasFrame(Function<WebDriver, Frame> howToGetFrame) {
        return new HasFrameMatcher(howToGetFrame);
    }

    @Override
    protected boolean matchesSafely(WrapsDriver item, Description mismatchDescription) {
        WebDriver driver;
        if ((driver = item.getWrappedDriver()) == null) {
            mismatchDescription.appendText(format("Wrapped webDriver is null. It is not possible to get/switch to the frame %s",
                    howToGetFrame.toString()));
            return false;
        }

        var currentHandle = driver.getWindowHandle();
        boolean result;
        try {
            howToGetFrame.apply(driver);
            result = true;
        }
        catch (NoSuchFrameException e) {
            result = false;
        }
        finally {
            driver.switchTo().window(currentHandle);
        }

        mismatchDescription.appendText(format("There is no frame found by criteria: %s",
                howToGetFrame.toString()));
        return result;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText(toString());
    }

    @Override
    public String toString() {
        return format("has frame. Description: %s", howToGetFrame.toString());
    }
}
