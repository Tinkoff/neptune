package ru.tinkoff.qa.neptune.selenium.test.captors;

import org.testng.annotations.Test;

import java.awt.image.BufferedImage;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.openqa.selenium.By.tagName;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.MultipleSearchSupplier.links;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.MultipleSearchSupplier.webElements;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.link;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.webElement;
import static ru.tinkoff.qa.neptune.selenium.functions.target.locator.active.element.GetActiveElementSupplier.activeElement;
import static ru.tinkoff.qa.neptune.selenium.test.FakeDOMModel.BUTTON_TAG;
import static ru.tinkoff.qa.neptune.selenium.test.captors.TestImageInjector.INJECTED;

public class WebElementCaptorTest extends BaseCaptorTest {

    @Test
    public void positiveTestOfScreenshotFromElement() {
        seleniumSteps.find(webElement(tagName(BUTTON_TAG)));
        assertThat(INJECTED.size(), is(1));
        assertThat(INJECTED, hasEntry(equalTo("Screenshot taken from the element"),
                isA(BufferedImage.class)));
    }

    @Test
    public void positiveTestOfScreenshotFromElements() {
        seleniumSteps.find(webElements(tagName(BUTTON_TAG)));
        assertThat(INJECTED.size(), is(1));
        assertThat(INJECTED, hasEntry(equalTo("Screenshot taken from the list of elements"),
                isA(BufferedImage.class)));
    }

    @Test
    public void positiveTestOfScreenshotFromWidget() {
        seleniumSteps.find(link());
        assertThat(INJECTED.size(), is(1));
        assertThat(INJECTED, hasEntry(equalTo("Screenshot taken from the element"),
                isA(BufferedImage.class)));
    }

    @Test
    public void positiveTestOfScreenshotFromWidgets() {
        seleniumSteps.find(links());
        assertThat(INJECTED.size(), is(1));
        assertThat(INJECTED, hasEntry(equalTo("Screenshot taken from the list of elements"),
                isA(BufferedImage.class)));
    }

    @Test
    public void positiveTestOfScreenshotFromActiveElement() {
        seleniumSteps.get(activeElement());
        assertThat(INJECTED.size(), is(1));
        assertThat(INJECTED, hasEntry(equalTo("Screenshot taken from the element"),
                isA(BufferedImage.class)));
    }
}
