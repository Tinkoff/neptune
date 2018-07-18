package ru.tinkoff.qa.neptune.selenium.test.steps.tests.target.locator;

import ru.tinkoff.qa.neptune.selenium.functions.target.locator.frame.Frame;
import ru.tinkoff.qa.neptune.selenium.functions.target.locator.window.Window;
import ru.tinkoff.qa.neptune.selenium.test.*;
import ru.tinkoff.qa.neptune.selenium.test.enums.FrameIndexes;
import ru.tinkoff.qa.neptune.selenium.test.enums.FrameNames;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.internal.WrapsElement;
import org.testng.annotations.Test;

import java.util.Random;

import static ru.tinkoff.qa.neptune.selenium.functions.target.locator.SwitchActionSupplier.to;
import static ru.tinkoff.qa.neptune.selenium.functions.target.locator.active.element.GetActiveElementSupplier.activeElement;
import static ru.tinkoff.qa.neptune.selenium.functions.target.locator.alert.GetAlertSupplier.alert;
import static ru.tinkoff.qa.neptune.selenium.functions.target.locator.content.DefaultContentSupplier.defaultContent;
import static ru.tinkoff.qa.neptune.selenium.functions.target.locator.frame.GetFrameFunction.*;
import static ru.tinkoff.qa.neptune.selenium.functions.target.locator.frame.GetFrameSupplier.frame;
import static ru.tinkoff.qa.neptune.selenium.functions.target.locator.frame.parent.ParentFrameSupplier.parentFrame;
import static ru.tinkoff.qa.neptune.selenium.functions.target.locator.window.GetWindowSupplier.window;
import static ru.tinkoff.qa.neptune.selenium.test.enums.WindowHandles.HANDLE2;
import static ru.tinkoff.qa.neptune.selenium.test.enums.WindowHandles.HANDLE3;
import static org.apache.commons.lang3.ArrayUtils.removeElements;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class SwitchTest extends BaseWebDriverTest {

    private static <T extends Enum> T getRandomEnumItem(T[] elements) {
        return elements[new Random().nextInt(elements.length)];
    }

    @Test
    public void switchToParentFrameTest() {
        seleniumSteps.performSwitch(to(parentFrame()));
        assertThat(((MockWebDriver) seleniumSteps.getWrappedDriver()).isSwitchedToParentFrame(), is(true));
    }

    @Test
    public void switchToActiveElementTest() {
        seleniumSteps.performSwitch(to(activeElement()));
        assertThat(ActiveWebElement.activeWebElement, not(nullValue()));
    }

    @Test
    public void switchToFrameByIndexBySearchingTest() {
        FrameIndexes index = getRandomEnumItem(FrameIndexes.values());
        seleniumSteps.performSwitch(to(frame(index(index.getIndex()))));

        assertThat(((MockWebDriver) seleniumSteps.getWrappedDriver()).getCurrentFrame(), is(index.getIndex()));
    }

    @Test
    public void switchToFrameByIndex() {
        FrameIndexes index1 = getRandomEnumItem(FrameIndexes.values());
        FrameIndexes index2 = getRandomEnumItem(removeElements(FrameIndexes.values(), index1));

        Frame frame = seleniumSteps.get(frame(index(index1.getIndex())));
        seleniumSteps.get(frame(index(index2.getIndex())));

        seleniumSteps.performSwitch(to(frame));
        assertThat(((MockWebDriver) seleniumSteps.getWrappedDriver()).getCurrentFrame(), is(index1.getIndex()));
    }

    @Test
    public void switchToFrameByNameOrIdBySearchingTest() {
        FrameNames name1 = getRandomEnumItem(FrameNames.values());
        seleniumSteps.performSwitch(to(frame(nameOrId(name1.getNameOrId()))));

        assertThat(((MockWebDriver) seleniumSteps.getWrappedDriver()).getCurrentFrame(), is(name1.getNameOrId()));
    }

    @Test
    public void switchToFrameByNameOrId() {
        FrameNames name1 = getRandomEnumItem(FrameNames.values());
        FrameNames name2 = getRandomEnumItem(removeElements(FrameNames.values(), name1));

        Frame frame = seleniumSteps.get(frame(nameOrId(name1.getNameOrId())));
        seleniumSteps.get(frame(nameOrId(name2.getNameOrId())));

        seleniumSteps.performSwitch(to(frame));
        assertThat(((MockWebDriver) seleniumSteps.getWrappedDriver()).getCurrentFrame(), is(name1.getNameOrId()));
    }

    @Test
    public void switchToFrameByWebElementBySearchingTest() {
        WebElement element = new ValidFrameWebElement();
        seleniumSteps.performSwitch(to(frame(insideElement(element))));

        assertThat(((MockWebDriver) seleniumSteps.getWrappedDriver()).getCurrentFrame(), is(element));
    }

    @Test
    public void switchToFrameByWebElement() {
        WebElement element1 = new ValidFrameWebElement();
        WebElement element2 = new ValidFrameWebElement();

        Frame frame = seleniumSteps.get(frame(insideElement(element1)));
        seleniumSteps.get(frame(insideElement(element2)));

        seleniumSteps.performSwitch(to(frame));
        assertThat(((MockWebDriver) seleniumSteps.getWrappedDriver()).getCurrentFrame(), is(element1));
    }

    @Test
    public void switchToFrameByWrappedElementBySearchingTest() {
        WebElement element = new ValidFrameWebElement();
        seleniumSteps.performSwitch(to(frame(wrappedBy(() -> element))));

        assertThat(((MockWebDriver) seleniumSteps.getWrappedDriver()).getCurrentFrame(), is(element));
    }

    @Test
    public void switchToFrameByWrappedElement() {
        WebElement element1 = new ValidFrameWebElement();
        WebElement element2 = new ValidFrameWebElement();

        WrapsElement wrapsElement1 = () -> element1;
        WrapsElement wrapsElement2 = () -> element2;

        Frame frame = seleniumSteps.get(frame(wrappedBy(wrapsElement1)));
        seleniumSteps.get(frame(wrappedBy(wrapsElement2)));

        seleniumSteps.performSwitch(to(frame));
        assertThat(((MockWebDriver) seleniumSteps.getWrappedDriver()).getCurrentFrame(), is(element1));
    }

    @Test
    public void switchToAlertBySearching() {
        seleniumSteps.performSwitch(to(alert()));
        assertThat(((MockTargetLocator) seleniumSteps.getWrappedDriver().switchTo()).getAlert().isSwitchedTo(),
                is(true));
    }

    @Test
    public void switchToWindowBySearching() {
        seleniumSteps.performSwitch(to(window().byIndex(1)));
        assertThat(seleniumSteps.getWrappedDriver().getWindowHandle(), is(HANDLE2.getHandle()));
    }

    @Test
    public void switchToWindow() {
        Window window = seleniumSteps.get(window().byIndex(2));
        seleniumSteps.performSwitch(to(window()));
        seleniumSteps.performSwitch(to(window));
        assertThat(seleniumSteps.getWrappedDriver().getWindowHandle(), is(HANDLE3.getHandle()));
    }

    @Test
    public void switchToDefaultContent() {
        seleniumSteps.performSwitch(to(defaultContent()));
        assertThat(((MockWebDriver) seleniumSteps.getWrappedDriver()).isSwitchedToDefaultContent(), is(true));
    }
}
