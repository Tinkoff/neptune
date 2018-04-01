package com.github.toy.constructor.selenium.functions.target.locator.frame;

import com.github.toy.constructor.selenium.SeleniumSteps;
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

public final class GetFrameSupplier extends TargetLocatorSupplier<Frame, GetFrameSupplier> {

    private GetFrameSupplier() {
        super();
    }

    private static Function<SeleniumSteps, Frame> createFrameFunction(Object frame) {
        return seleniumSteps ->  {
            try {
                return new Frame(seleniumSteps.getWrappedDriver(), frame);
            }
            catch (WebDriverException e) {
                return null;
            }
        };
    }

    public static GetFrameSupplier frame(Duration timeOut, int index) {
        return new GetFrameSupplier().set(getSingle(toGet(format("frame by index %s", index),
                createFrameFunction(index)), timeOut,
                () -> new NoSuchFrameException(format("Frame has not been found by index %s", index))));
    }

    public static GetFrameSupplier frame(int index) {
        return frame(WAITING_FRAME_SWITCHING__DURATION.get(), index);
    }

    public static GetFrameSupplier frame(Duration timeOut, String nameOrId) {
        return new GetFrameSupplier().set(getSingle(toGet(format("frame by id or name %s", nameOrId),
                createFrameFunction(nameOrId)), timeOut,
                () -> new NoSuchFrameException(format("Frame has not been found by id or name %s", nameOrId))));
    }

    public static GetFrameSupplier frame(String nameOrId) {
        return frame(WAITING_FRAME_SWITCHING__DURATION.get(), nameOrId);
    }

    public static GetFrameSupplier frame(Duration timeOut, WebElement element) {
        return new GetFrameSupplier().set(getSingle(toGet(format("frame inside %s", element),
                createFrameFunction(element)), timeOut,
                () -> new NoSuchFrameException(format("Frame has not been found inside %s", element))));
    }

    public static GetFrameSupplier frame(WebElement element) {
        return frame(WAITING_FRAME_SWITCHING__DURATION.get(), element);
    }
}
