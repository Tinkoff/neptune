package ru.tinkoff.qa.neptune.selenium.test.captors;

import org.openqa.selenium.NoSuchElementException;
import org.testng.annotations.Test;

import java.awt.image.BufferedImage;

import static java.time.Duration.ofSeconds;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.isA;
import static org.openqa.selenium.By.tagName;
import static ru.tinkoff.qa.neptune.selenium.functions.click.ClickActionSupplier.on;
import static ru.tinkoff.qa.neptune.selenium.functions.edit.EditActionSupplier.valueOfThe;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.*;
import static ru.tinkoff.qa.neptune.selenium.test.FakeDOMModel.BUTTON_TAG;
import static ru.tinkoff.qa.neptune.selenium.test.captors.TestImageInjector.INJECTED;

public class WebDriverCaptorTest extends BaseCaptorTest {

    @Test
    public void clickOnElementPositiveTest() {
        seleniumSteps.click(on(webElement(tagName(BUTTON_TAG))));
        assertThat(INJECTED.size(), is(2));
        assertThat(INJECTED, hasEntry(equalTo("Screenshot taken from the element"),
                isA(BufferedImage.class)));
        assertThat(INJECTED, hasEntry(equalTo("Browser screenshot"),
                isA(BufferedImage.class)));
    }

    @Test
    public void clickOnElementNegativeTest() {
        try {
            seleniumSteps.click(on(webElement(tagName("fakeTag"), ofSeconds(1))));
        }
        catch (NoSuchElementException ignored) {

        }
        assertThat(INJECTED.size(), is(1));
        assertThat(INJECTED, hasEntry(equalTo("Browser screenshot"),
                isA(BufferedImage.class)));
    }

    @Test
    public void clickOnWidgetPositiveTest() {
        seleniumSteps.click(on(link()));
        assertThat(INJECTED.size(), is(2));
        assertThat(INJECTED, hasEntry(equalTo("Screenshot taken from the element"),
                isA(BufferedImage.class)));
        assertThat(INJECTED, hasEntry(equalTo("Browser screenshot"),
                isA(BufferedImage.class)));
    }

    @Test
    public void clickOnOWidgetNegativeTest() {
        try {
            seleniumSteps.click(on(link("Fake link", ofSeconds(1))));
        }
        catch (NoSuchElementException ignored) {

        }
        assertThat(INJECTED.size(), is(1));
        assertThat(INJECTED, hasEntry(equalTo("Browser screenshot"),
                isA(BufferedImage.class)));
    }

    @Test
    public void editWidgetPositiveTest() {
        seleniumSteps.edit(valueOfThe(flag(), true));
        assertThat(INJECTED.size(), is(2));
        assertThat(INJECTED, hasEntry(equalTo("Screenshot taken from the element"),
                isA(BufferedImage.class)));
        assertThat(INJECTED, hasEntry(equalTo("Browser screenshot"),
                isA(BufferedImage.class)));
    }

    @Test
    public void editOnOWidgetNegativeTest() {
        try {
            seleniumSteps.edit(valueOfThe(flag("Fake flag", ofSeconds(1)), true));
        }
        catch (NoSuchElementException ignored) {

        }
        assertThat(INJECTED.size(), is(1));
        assertThat(INJECTED, hasEntry(equalTo("Browser screenshot"),
                isA(BufferedImage.class)));
    }
}
