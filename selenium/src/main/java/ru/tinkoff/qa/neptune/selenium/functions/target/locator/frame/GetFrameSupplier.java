package ru.tinkoff.qa.neptune.selenium.functions.target.locator.frame;

import org.openqa.selenium.NoSuchFrameException;
import org.openqa.selenium.WebDriver;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotation.MakeFileCapturesOnFinishing;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotation.MakeImageCapturesOnFinishing;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;
import ru.tinkoff.qa.neptune.selenium.functions.target.locator.TargetLocatorSupplier;

import java.time.Duration;

import static java.lang.String.format;
import static ru.tinkoff.qa.neptune.selenium.CurrentContentFunction.currentContent;
import static ru.tinkoff.qa.neptune.selenium.properties.WaitingProperties.WAITING_FRAME_SWITCHING_DURATION;

@MakeImageCapturesOnFinishing
@MakeFileCapturesOnFinishing
public final class GetFrameSupplier extends SequentialGetStepSupplier.GetObjectChainedStepSupplier<SeleniumStepContext, Frame, WebDriver, GetFrameSupplier>
        implements TargetLocatorSupplier<Frame> {


    private GetFrameSupplier(GetFrameFunction getFrame) {
        super(format("Frame %s", getFrame.getDescription()), getFrame);
        timeOut(WAITING_FRAME_SWITCHING_DURATION.get());
        throwOnEmptyResult(() -> new NoSuchFrameException(format("Can't find/switch to the frame %s",
                getFrame.getDescription())));
    }

    /**
     * Creates instance of {@link GetFrameSupplier} which wraps a function. This function switches to some frame by
     * defined parameters and returns an instance of {@link Frame}
     *
     * @param howToGetFrame how to get the frame to switch to.
     * @return instance of {@link GetFrameSupplier}
     */
    public static GetFrameSupplier frame(GetFrameFunction howToGetFrame) {
        return new GetFrameSupplier(howToGetFrame).from(currentContent());
    }

    public GetFrameSupplier timeOut(Duration timeOut) {
        return super.timeOut(timeOut);
    }
}
