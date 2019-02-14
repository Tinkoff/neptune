package ru.tinkoff.qa.neptune.selenium.functions.target.locator.frame;

import org.openqa.selenium.NoSuchFrameException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WrapsElement;

import java.util.function.Function;

import static java.lang.String.format;

public final class GetFrameFunction implements Function<WebDriver, Frame> {

    private final String description;
    private final Object frame;

    private GetFrameFunction(String description, Object frame) {
        this.description = description;
        this.frame = frame;
    }

    /**
     * Builds a function which performs switching to the frame and returns an instance of {@link Frame}.
     *
     * @param index index of the frame to switch to.
     * @return instance of {@link Function}
     */
    public static GetFrameFunction index(int index) {
        return new GetFrameFunction(format("by index %s", index), index);
    }

    /**
     * Builds a function which performs switching to the frame and returns an instance of {@link Frame}.
     *
     * @param nameOrId name or id of the frame to switch to.
     * @return instance of {@link GetFrameSupplier}
     */
    public static GetFrameFunction nameOrId(String nameOrId) {
        return new GetFrameFunction(format("by name or id %s", nameOrId), nameOrId);
    }

    /**
     * Builds a function which performs switching to the frame and returns an instance of {@link Frame}.
     *
     * @param webElement is the frame element to switch to.
     * @return instance of {@link GetFrameSupplier}
     */
    public static GetFrameFunction insideElement(WebElement webElement) {
        return new GetFrameFunction(format("inside element %s", webElement), webElement);
    }

    /**
     * Builds a function which performs switching to the frame and returns an instance of {@link Frame}.
     *
     * @param wrapsElement is the wrapper of a frame element to switch to.
     * @return instance of {@link GetFrameSupplier}
     */
    public static GetFrameFunction wrappedBy(WrapsElement wrapsElement) {
        return new GetFrameFunction(format("inside element wrapped by %s", wrapsElement), wrapsElement);
    }

    String getDescription() {
        return description;
    }

    @Override
    public Frame apply(WebDriver webDriver) {
        try {
            return new Frame(webDriver, frame);
        }
        catch (NoSuchFrameException e) {
            return null;
        }
    }
}
