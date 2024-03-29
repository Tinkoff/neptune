package ru.tinkoff.qa.neptune.selenium.test.steps.tests.target.locator;

import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.selenium.BaseWebDriverPreparations;
import ru.tinkoff.qa.neptune.selenium.functions.target.locator.frame.Frame;
import ru.tinkoff.qa.neptune.selenium.functions.target.locator.window.Window;
import ru.tinkoff.qa.neptune.selenium.test.ActiveWebElement;
import ru.tinkoff.qa.neptune.selenium.test.MockWebDriver;
import ru.tinkoff.qa.neptune.selenium.test.enums.FrameIndexes;
import ru.tinkoff.qa.neptune.selenium.test.enums.FrameNames;

import java.util.Random;

import static org.apache.commons.lang3.ArrayUtils.removeElements;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.openqa.selenium.By.tagName;
import static ru.tinkoff.qa.neptune.selenium.functions.target.locator.active.element.GetActiveElementSupplier.activeElement;
import static ru.tinkoff.qa.neptune.selenium.functions.target.locator.alert.GetAlertSupplier.alert;
import static ru.tinkoff.qa.neptune.selenium.functions.target.locator.content.DefaultContentSupplier.defaultContent;
import static ru.tinkoff.qa.neptune.selenium.functions.target.locator.frame.GetFrameSupplier.frame;
import static ru.tinkoff.qa.neptune.selenium.functions.target.locator.frame.parent.ParentFrameSupplier.parentFrame;
import static ru.tinkoff.qa.neptune.selenium.functions.target.locator.window.GetWindowSupplier.window;
import static ru.tinkoff.qa.neptune.selenium.test.FakeDOMModel.FRAME_ELEMENT_VALID1;
import static ru.tinkoff.qa.neptune.selenium.test.MockAlert.isSwitchedTo;
import static ru.tinkoff.qa.neptune.selenium.test.MockAlert.setSwitchedTo;
import static ru.tinkoff.qa.neptune.selenium.test.enums.WindowHandles.HANDLE2;
import static ru.tinkoff.qa.neptune.selenium.test.enums.WindowHandles.HANDLE3;

public class SwitchTest extends BaseWebDriverPreparations {

    private static <T extends Enum<?>> T getRandomEnumItem(T[] elements) {
        return elements[new Random().nextInt(elements.length)];
    }

    @Test
    public void switchToParentFrameTest() {
        ((MockWebDriver) wrappedWebDriver.getWrappedDriver()).setSwitchedToParentFrame(false);
        seleniumSteps.switchTo(parentFrame());
        assertThat(((MockWebDriver) seleniumSteps.getWrappedDriver()).isSwitchedToParentFrame(), is(true));
    }

    @Test
    public void switchToActiveElementTest() {
        seleniumSteps.switchTo(activeElement());
        assertThat(ActiveWebElement.activeWebElement, not(nullValue()));
    }

    @Test
    public void switchToFrameByIndexBySearchingTest() {
        FrameIndexes index = getRandomEnumItem(FrameIndexes.values());
        seleniumSteps.switchTo(frame(index.getIndex()));

        assertThat(((MockWebDriver) seleniumSteps.getWrappedDriver()).getCurrentFrame(), is(index.getIndex()));
    }

    @Test
    public void switchToFrameByIndex() {
        FrameIndexes index1 = getRandomEnumItem(FrameIndexes.values());
        FrameIndexes index2 = getRandomEnumItem(removeElements(FrameIndexes.values(), index1));

        Frame frame = seleniumSteps.get(frame(index1.getIndex()));
        seleniumSteps.get(frame(index2.getIndex()));

        seleniumSteps.switchTo(frame);
        assertThat(((MockWebDriver) seleniumSteps.getWrappedDriver()).getCurrentFrame(), is(index1.getIndex()));
    }

    @Test
    public void switchToFrameByNameOrIdBySearchingTest() {
        FrameNames name1 = getRandomEnumItem(FrameNames.values());
        seleniumSteps.switchTo(frame(name1.getNameOrId()));

        assertThat(((MockWebDriver) seleniumSteps.getWrappedDriver()).getCurrentFrame(), is(name1.getNameOrId()));
    }

    @Test
    public void switchToFrameByNameOrId() {
        FrameNames name1 = getRandomEnumItem(FrameNames.values());
        FrameNames name2 = getRandomEnumItem(removeElements(FrameNames.values(), name1));

        Frame frame = seleniumSteps.get(frame(name1.getNameOrId()));
        seleniumSteps.get(frame(name2.getNameOrId()));

        seleniumSteps.switchTo(frame);
        assertThat(((MockWebDriver) seleniumSteps.getWrappedDriver()).getCurrentFrame(), is(name1.getNameOrId()));
    }

    @Test
    public void switchToFrameByWebElementBySearchingTest() {
        seleniumSteps.switchTo(frame(tagName("valid_frame1")));
        assertThat(((MockWebDriver) seleniumSteps.getWrappedDriver()).getCurrentFrame(), is(FRAME_ELEMENT_VALID1));
    }

    @Test
    public void switchToFrameByWebElement() {
        Frame frame = seleniumSteps.get(frame(tagName("valid_frame1")));
        seleniumSteps.get(frame(tagName("valid_frame2")));

        seleniumSteps.switchTo(frame);
        assertThat(((MockWebDriver) seleniumSteps.getWrappedDriver()).getCurrentFrame(), is(FRAME_ELEMENT_VALID1));
    }

    @Test
    public void switchToAlertBySearching() {
        setSwitchedTo(false);
        seleniumSteps.switchTo(alert());
        assertThat(isSwitchedTo(), is(true));
    }

    @Test
    public void switchToWindowBySearching() {
        seleniumSteps.switchTo(window(1));
        assertThat(seleniumSteps.getWrappedDriver().getWindowHandle(), is(HANDLE2.getHandle()));
    }

    @Test
    public void switchToWindow() {
        Window window = seleniumSteps.get(window(2));
        seleniumSteps.switchTo(window());
        seleniumSteps.switchTo(window);
        assertThat(seleniumSteps.getWrappedDriver().getWindowHandle(), is(HANDLE3.getHandle()));
    }

    @Test
    public void switchToDefaultContent() {
        seleniumSteps.switchTo(defaultContent());
        assertThat(((MockWebDriver) seleniumSteps.getWrappedDriver()).isSwitchedToDefaultContent(), is(true));
    }
}
