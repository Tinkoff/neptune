package ru.tinkoff.qa.neptune.selenium.functions.target.locator.window;

import org.openqa.selenium.WindowType;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotations.MaxDepthOfReporting;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;
import ru.tinkoff.qa.neptune.selenium.functions.target.locator.TargetLocatorSupplier;

import static org.openqa.selenium.WindowType.TAB;
import static org.openqa.selenium.WindowType.WINDOW;

@SequentialGetStepSupplier.DefineGetImperativeParameterName("Open:")
@MaxDepthOfReporting(0)
public class GetNewWindowSupplier extends SequentialGetStepSupplier.GetSimpleStepSupplier<SeleniumStepContext, Window, GetNewWindowSupplier>
        implements TargetLocatorSupplier<Window> {

    protected GetNewWindowSupplier(WindowType type) {
        super(seleniumStepContext -> {
            var driver = seleniumStepContext.getWrappedDriver();
            var currentHandle = driver.getWindowHandle();
            try {
                driver = driver.switchTo().newWindow(type);
                var handle = driver.getWindowHandle();
                return new DefaultWindow(handle, driver);
            } finally {
                driver.switchTo().window(currentHandle);
            }
        });
    }

    /**
     * Creates an instance of {@link GetNewWindowSupplier} to get a new browser window
     *
     * @return an instance of {@link GetNewWindowSupplier}
     */
    @Description("New browser window")
    public static GetNewWindowSupplier newWindow() {
        return new GetNewWindowSupplier(WINDOW);
    }

    /**
     * Creates an instance of {@link GetNewWindowSupplier} to get a new browser tab
     *
     * @return an instance of {@link GetNewWindowSupplier}
     */
    @Description("New browser tab")
    public static GetNewWindowSupplier newTab() {
        return new GetNewWindowSupplier(TAB);
    }
}
