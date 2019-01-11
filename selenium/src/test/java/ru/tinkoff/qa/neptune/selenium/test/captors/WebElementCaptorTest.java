package ru.tinkoff.qa.neptune.selenium.test.captors;

import org.openqa.selenium.NoSuchElementException;
import org.testng.annotations.Test;

import java.awt.image.BufferedImage;

import static java.time.Duration.ofSeconds;
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

    @Test
    public void negativeTestOfScreenshotFromElement() {
        try {
            seleniumSteps.find(webElement(tagName("fakeTag"), ofSeconds(1)));
        }
        catch (NoSuchElementException ignored) {
        }
        assertThat(INJECTED.size(), is(1));
        assertThat(INJECTED, hasEntry(equalTo("Browser screenshot"),
                isA(BufferedImage.class)));
    }

    @Test
    public void negativeTestOfScreenshotFromElements() {
        seleniumSteps.find(webElements(tagName("fakeTag"), ofSeconds(1)));
        assertThat(INJECTED.size(), is(0));
    }

    @Test
    public void negativeTestOfScreenshotFromWidget() {
        try {
            seleniumSteps.find(link("fake link", ofSeconds(1)));
        }
        catch (NoSuchElementException ignored) {
        }

        assertThat(INJECTED.size(), is(1));
        assertThat(INJECTED, hasEntry(equalTo("Browser screenshot"),
                isA(BufferedImage.class)));
    }

    @Test
    public void negativeTestOfScreenshotFromWidgets() {
        seleniumSteps.find(links("fake link", ofSeconds(1)));
        assertThat(INJECTED.size(), is(0));
    }

    @Test
    public void negativeTestOfScreenshotFromActiveElement() {
        seleniumSteps.get(activeElement());
        INJECTED.clear();

        try {
            seleniumSteps.get(activeElement());
        }
        catch (NoSuchElementException ignored) {
        }

        assertThat(INJECTED.size(), is(1));
        assertThat(INJECTED, hasEntry(equalTo("Browser screenshot"),
                isA(BufferedImage.class)));
    }
}
