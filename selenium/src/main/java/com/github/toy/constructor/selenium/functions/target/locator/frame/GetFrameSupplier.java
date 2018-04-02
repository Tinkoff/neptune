package com.github.toy.constructor.selenium.functions.target.locator.frame;

import com.github.toy.constructor.core.api.SequentialGetSupplier;
import com.github.toy.constructor.selenium.SeleniumSteps;
import com.github.toy.constructor.selenium.functions.searching.SequentialSearchSupplier;
import com.github.toy.constructor.selenium.functions.target.locator.TargetLocatorSupplier;
import org.openqa.selenium.NoSuchFrameException;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;

import java.time.Duration;
import java.util.function.Function;

import static com.github.toy.constructor.core.api.StoryWriter.toGet;
import static com.github.toy.constructor.core.api.ToGetSingleCheckedObject.getSingle;
import static com.github.toy.constructor.selenium.properties.WaitingProperties.WAITING_FRAME_SWITCHING__DURATION;
import static java.lang.String.format;

public final class GetFrameSupplier extends SequentialGetSupplier<SeleniumSteps, Frame, FrameParameterPOJO, GetFrameSupplier>
        implements TargetLocatorSupplier<Frame> {

    private static final  String INDEX = "index";
    private static final String ID_OR_NAME = "id or name";
    private static final String WEB_ELEMENT = "web element";
    private final Duration timeToGetFrame;

    private GetFrameSupplier(Duration timeToGetFrame) {
        this.timeToGetFrame = timeToGetFrame;
    }

    /**
     * Builds a function which performs switching to the frame and returns an instance of {@link Frame}.
     *
     * @param timeOut of the switching to the frame is succeeded.
     * @param index index of the frame to switch to.
     * @return instance of {@link GetFrameSupplier}
     */
    public static GetFrameSupplier frame(Duration timeOut, int index) {
        return new GetFrameSupplier(timeOut).from(toGet(format("Frame by %s %s", INDEX, index),
                seleniumSteps -> new FrameParameterPOJO(seleniumSteps.getWrappedDriver(), index)));
    }

    /**
     * Builds a function which performs switching to the frame and returns an instance of {@link Frame}.
     * About the time of the switching to the frame is succeeded
     * @see com.github.toy.constructor.selenium.properties.WaitingProperties#WAITING_FRAME_SWITCHING__DURATION
     *
     * @param index index of the frame to switch to.
     * @return instance of {@link GetFrameSupplier}
     */
    public static GetFrameSupplier frame(int index) {
        return frame(WAITING_FRAME_SWITCHING__DURATION.get(), index);
    }

    /**
     * Builds a function which performs switching to the frame and returns an instance of {@link Frame}.
     *
     * @param timeOut of the switching to the frame is succeeded.
     * @param nameOrId name or id of the frame to switch to.
     * @return instance of {@link GetFrameSupplier}
     */
    public static GetFrameSupplier frame(Duration timeOut, String nameOrId) {
        return new GetFrameSupplier(timeOut).from(toGet(format("Frame by %s %s", ID_OR_NAME, nameOrId),
                seleniumSteps -> new FrameParameterPOJO(seleniumSteps.getWrappedDriver(), nameOrId)));
    }

    /**
     * Builds a function which performs switching to the frame and returns an instance of {@link Frame}.
     * About the time of the switching to the frame is succeeded
     * @see com.github.toy.constructor.selenium.properties.WaitingProperties#WAITING_FRAME_SWITCHING__DURATION
     *
     * @param nameOrId name or id of the frame to switch to.
     * @return instance of {@link GetFrameSupplier}
     */
    public static GetFrameSupplier frame(String nameOrId) {
        return frame(WAITING_FRAME_SWITCHING__DURATION.get(), nameOrId);
    }

    /**
     * Builds a function which performs switching to the frame and returns an instance of {@link Frame}.
     *
     * @param timeOut of the switching to the frame is succeeded.
     * @param element is the frame element to switch to.
     * @return instance of {@link GetFrameSupplier}
     */
    public static GetFrameSupplier frame(Duration timeOut, WebElement element) {
        return new GetFrameSupplier(timeOut).from(toGet(format("Frame inside %s %s", WEB_ELEMENT, element),
                seleniumSteps -> new FrameParameterPOJO(seleniumSteps.getWrappedDriver(), element)));
    }

    /**
     * Builds a function which performs switching to the frame and returns an instance of {@link Frame}.
     * About the time of the switching to the frame is succeeded
     * @see com.github.toy.constructor.selenium.properties.WaitingProperties#WAITING_FRAME_SWITCHING__DURATION
     *
     * @param element is the frame element to switch to.
     * @return instance of {@link GetFrameSupplier}
     */
    public static GetFrameSupplier frame(WebElement element) {
        return frame(WAITING_FRAME_SWITCHING__DURATION.get(), element);
    }

    /**
     * Builds a function which performs switching to the frame and returns an instance of {@link Frame}.
     *
     * @param timeOut of the switching to the frame is succeeded.
     * @param sequentialSearch is how to find the frame element to switch to.
     * @return instance of {@link GetFrameSupplier}
     */
    public static GetFrameSupplier frame(Duration timeOut, SequentialSearchSupplier<WebElement> sequentialSearch) {
        return new GetFrameSupplier(timeOut).from(toGet(format("Frame inside %s %s", WEB_ELEMENT, sequentialSearch),
                seleniumSteps -> new FrameParameterPOJO(seleniumSteps.getWrappedDriver(), seleniumSteps.find(sequentialSearch))));
    }

    /**
     * Builds a function which performs switching to the frame and returns an instance of {@link Frame}.
     * About the time of the switching to the frame is succeeded
     * @see com.github.toy.constructor.selenium.properties.WaitingProperties#WAITING_FRAME_SWITCHING__DURATION
     *
     * @param sequentialSearch is how to find the frame element to switch to.
     * @return instance of {@link GetFrameSupplier}
     */
    public static GetFrameSupplier frame(SequentialSearchSupplier<WebElement> sequentialSearch) {
        return frame(WAITING_FRAME_SWITCHING__DURATION.get(), sequentialSearch);
    }

    @Override
    protected Function<FrameParameterPOJO, Frame> getEndFunction() {
        return getSingle(toGet("Switch to frame", framePOJO -> {
            try {
                return new Frame(framePOJO.getDriver(), framePOJO.getFrame());
            }
            catch (WebDriverException e) {
                return null;
            }
        }), timeToGetFrame, () -> new NoSuchFrameException("Can't find/switch to the frame"));
    }
}
