package com.github.toy.constructor.selenium.functions.target.locator.frame;

import org.openqa.selenium.NoSuchFrameException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.internal.WrapsElement;

import java.time.Duration;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.github.toy.constructor.core.api.StoryWriter.toGet;
import static com.github.toy.constructor.core.api.ToGetSingleCheckedObject.getSingle;
import static com.github.toy.constructor.selenium.properties.WaitingProperties.WAITING_FRAME_SWITCHING_DURATION;
import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.String.format;

public final class GetFrameFunction implements Function<WebDriver, Frame> {

    private static final Supplier<NoSuchFrameException> NO_SUCH_FRAME_EXCEPTION_SUPPLIER =
            () -> new NoSuchFrameException("Can't find/switch to the frame");
    private final Object frame;

    private GetFrameFunction(Object frame) {
        checkArgument(frame != null, "Frame object should be defined");
        Class<?> clazz = frame.getClass();
        checkArgument(String.class.isAssignableFrom(clazz) || Integer.class.isAssignableFrom(clazz)
                        || WebElement.class.isAssignableFrom(clazz) || WrapsElement.class.isAssignableFrom(clazz),
                format("Frame to switch to should be an instance of %s, %s, %s or %s", String.class.getName(),
                        Integer.class.getName(), WebElement.class.getName(), WrapsElement.class.getName()));
        this.frame = frame;
    }

    /**
     * Builds a function which performs switching to the frame and returns an instance of {@link Frame}.
     *
     * @param timeOut of the switching to the frame is succeeded.
     * @param index index of the frame to switch to.
     * @return instance of {@link Function}
     */
    public static Function<WebDriver, Frame> index(Duration timeOut, int index) {
        return getSingle(toGet(format("Frame by index %s", index), new GetFrameFunction(index)),
                timeOut, NO_SUCH_FRAME_EXCEPTION_SUPPLIER);
    }

    /**
     * Builds a function which performs switching to the frame and returns an instance of {@link Frame}.
     * About the time of the switching to the frame is succeeded
     * @see com.github.toy.constructor.selenium.properties.WaitingProperties#WAITING_FRAME_SWITCHING_DURATION
     *
     * @param index index of the frame to switch to.
     * @return instance of {@link Function}
     */
    public static Function<WebDriver, Frame> index(int index) {
        return index(WAITING_FRAME_SWITCHING_DURATION.get(), index);
    }

    /**
     * Builds a function which performs switching to the frame and returns an instance of {@link Frame}.
     *
     * @param timeOut of the switching to the frame is succeeded.
     * @param nameOrId name or id of the frame to switch to.
     * @return instance of {@link GetFrameSupplier}
     */
    public static Function<WebDriver, Frame> nameOrId(Duration timeOut, String nameOrId) {
        return getSingle(toGet(format("Frame by name or id %s", nameOrId), new GetFrameFunction(nameOrId)),
                timeOut, NO_SUCH_FRAME_EXCEPTION_SUPPLIER);
    }

    /**
     * Builds a function which performs switching to the frame and returns an instance of {@link Frame}.
     * About the time of the switching to the frame is succeeded
     * @see com.github.toy.constructor.selenium.properties.WaitingProperties#WAITING_FRAME_SWITCHING_DURATION
     *
     * @param nameOrId name or id of the frame to switch to.
     * @return instance of {@link GetFrameSupplier}
     */
    public static Function<WebDriver, Frame> nameOrId(String nameOrId) {
        return nameOrId(WAITING_FRAME_SWITCHING_DURATION.get(), nameOrId);
    }

    /**
     * Builds a function which performs switching to the frame and returns an instance of {@link Frame}.
     *
     * @param timeOut of the switching to the frame is succeeded.
     * @param webElement is the frame element to switch to.
     * @return instance of {@link GetFrameSupplier}
     */
    public static Function<WebDriver, Frame> insideElement(Duration timeOut, WebElement webElement) {
        return getSingle(toGet(format("Frame inside element %s", webElement), new GetFrameFunction(webElement)),
                timeOut, NO_SUCH_FRAME_EXCEPTION_SUPPLIER);
    }

    /**
     * Builds a function which performs switching to the frame and returns an instance of {@link Frame}.
     * About the time of the switching to the frame is succeeded
     * @see com.github.toy.constructor.selenium.properties.WaitingProperties#WAITING_FRAME_SWITCHING_DURATION
     *
     * @param webElement is the frame element to switch to.
     * @return instance of {@link GetFrameSupplier}
     */
    public static Function<WebDriver, Frame> insideElement(WebElement webElement) {
        return insideElement(WAITING_FRAME_SWITCHING_DURATION.get(), webElement);
    }

    /**
     * Builds a function which performs switching to the frame and returns an instance of {@link Frame}.
     *
     * @param timeOut of the switching to the frame is succeeded.
     * @param wrapsElement is the wrapper of a frame element to switch to.
     * @return instance of {@link GetFrameSupplier}
     */
    public static Function<WebDriver, Frame> wrappedBy(Duration timeOut, WrapsElement wrapsElement) {
        return getSingle(toGet(format("Frame inside element wrapped by %s", wrapsElement), new GetFrameFunction(wrapsElement)),
                timeOut, NO_SUCH_FRAME_EXCEPTION_SUPPLIER);
    }

    /**
     * Builds a function which performs switching to the frame and returns an instance of {@link Frame}.
     * About the time of the switching to the frame is succeeded
     * @see com.github.toy.constructor.selenium.properties.WaitingProperties#WAITING_FRAME_SWITCHING_DURATION
     *
     * @param wrapsElement is the wrapper of a frame element to switch to.
     * @return instance of {@link GetFrameSupplier}
     */
    public static Function<WebDriver, Frame> wrappedBy(WrapsElement wrapsElement) {
        return wrappedBy(WAITING_FRAME_SWITCHING_DURATION.get(), wrapsElement);
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
