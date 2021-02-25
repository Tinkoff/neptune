package ru.tinkoff.qa.neptune.selenium.functions.target.locator.frame;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchFrameException;
import org.openqa.selenium.WebDriver;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotation.MakeFileCapturesOnFinishing;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotation.MakeImageCapturesOnFinishing;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;
import ru.tinkoff.qa.neptune.selenium.functions.target.locator.TargetLocatorSupplier;

import java.time.Duration;

import static java.lang.String.format;
import static ru.tinkoff.qa.neptune.selenium.SeleniumStepContext.CurrentContentFunction.currentContent;
import static ru.tinkoff.qa.neptune.selenium.functions.target.locator.frame.GetFrameFunction.*;
import static ru.tinkoff.qa.neptune.selenium.properties.WaitingProperties.WAITING_FRAME_SWITCHING_DURATION;

@MakeImageCapturesOnFinishing
@MakeFileCapturesOnFinishing
@SequentialGetStepSupplier.DefaultParameterNames(
        timeOut = "Time of the waiting for the frame"
)
public final class GetFrameSupplier extends SequentialGetStepSupplier.GetObjectChainedStepSupplier<SeleniumStepContext, Frame, WebDriver, GetFrameSupplier>
        implements TargetLocatorSupplier<Frame> {


    private GetFrameSupplier(GetFrameFunction getFrame) {
        super(format("Frame %s", getFrame.getDescription()), getFrame);
        timeOut(WAITING_FRAME_SWITCHING_DURATION.get());
        throwOnEmptyResult(() -> new NoSuchFrameException(format("Can't find/switch to the frame %s",
                getFrame.getDescription())));
    }

    /**
     * Creates instance of {@link GetFrameSupplier} that creates a function. This function switches to some frame by its
     * name or id.
     *
     * @param nameOrId name or id of the frame to switch to.
     * @return instance of {@link GetFrameSupplier}
     */
    public static GetFrameSupplier frame(String nameOrId) {
        return new GetFrameSupplier(nameOrId(nameOrId)).from(currentContent());
    }

    /**
     * Creates instance of {@link GetFrameSupplier} that creates a function. This function switches to some frame by its
     * index.
     *
     * @param index index of the frame
     * @return instance of {@link GetFrameSupplier}
     */
    public static GetFrameSupplier frame(int index) {
        return new GetFrameSupplier(index(index)).from(currentContent());
    }

    /**
     * Creates instance of {@link GetFrameSupplier} that creates a function. This function switches to some frame by defined
     * locator of a frame-element.
     *
     * @param by is a {@link By}-strategy which describes how to find a frame-element to switch to
     * @return instance of {@link GetFrameSupplier}
     */
    public static GetFrameSupplier frame(By by) {
        return new GetFrameSupplier(by(by)).from(currentContent());
    }

    public GetFrameSupplier timeOut(Duration timeOut) {
        return super.timeOut(timeOut);
    }
}
