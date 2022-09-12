package ru.tinkoff.qa.neptune.selenium.functions.target.locator.frame;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchFrameException;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotations.CaptureOnFailure;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotations.CaptureOnSuccess;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.MaxDepthOfReporting;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.ThrowWhenNoData;
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;
import ru.tinkoff.qa.neptune.selenium.captors.WebDriverImageCaptor;
import ru.tinkoff.qa.neptune.selenium.functions.target.locator.TargetLocatorSupplier;

import java.time.Duration;

import static ru.tinkoff.qa.neptune.selenium.SeleniumStepContext.CurrentContentFunction.currentContent;
import static ru.tinkoff.qa.neptune.selenium.functions.target.locator.frame.GetFrameFunction.*;
import static ru.tinkoff.qa.neptune.selenium.properties.WaitingProperties.WAITING_FRAME_SWITCHING_DURATION;

@CaptureOnFailure(by = WebDriverImageCaptor.class)
@CaptureOnSuccess(by = WebDriverImageCaptor.class)
@SequentialGetStepSupplier.DefineTimeOutParameterName("Time of the waiting for the frame")
@MaxDepthOfReporting(0)
@ThrowWhenNoData(toThrow = NoSuchFrameException.class)
public final class GetFrameSupplier extends SequentialGetStepSupplier.GetSimpleStepSupplier<SeleniumStepContext, Frame, GetFrameSupplier>
        implements TargetLocatorSupplier<Frame> {


    private GetFrameSupplier(GetFrameFunction getFrame) {
        super(currentContent().andThen(getFrame));
        timeOut(WAITING_FRAME_SWITCHING_DURATION.get());
        throwOnNoResult();
    }

    /**
     * Creates instance of {@link GetFrameSupplier} that creates a function. This function switches to some frame by its
     * name or id.
     *
     * @param nameOrId name or id of the frame to switch to.
     * @return instance of {@link GetFrameSupplier}
     */
    @Description("Frame by name or id {nameOrId}")
    public static GetFrameSupplier frame(@DescriptionFragment("nameOrId") String nameOrId) {
        return new GetFrameSupplier(nameOrId(nameOrId));
    }

    /**
     * Creates instance of {@link GetFrameSupplier} that creates a function. This function switches to some frame by its
     * index.
     *
     * @param index index of the frame
     * @return instance of {@link GetFrameSupplier}
     */
    @Description("Frame by index {index}")
    public static GetFrameSupplier frame(@DescriptionFragment("index") int index) {
        return new GetFrameSupplier(index(index));
    }

    /**
     * Creates instance of {@link GetFrameSupplier} that creates a function. This function switches to some frame by defined
     * locator of a frame-element.
     *
     * @param by is a {@link By}-strategy which describes how to find a frame-element to switch to
     * @return instance of {@link GetFrameSupplier}
     */
    @Description("Frame {by}")
    public static GetFrameSupplier frame(@DescriptionFragment("by") By by) {
        return new GetFrameSupplier(by(by));
    }

    public GetFrameSupplier timeOut(Duration timeOut) {
        return super.timeOut(timeOut);
    }
}
