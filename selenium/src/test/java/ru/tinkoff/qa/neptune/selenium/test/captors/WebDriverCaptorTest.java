package ru.tinkoff.qa.neptune.selenium.test.captors;

import org.openqa.selenium.NoSuchElementException;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.awt.image.BufferedImage;

import static java.time.Duration.ofSeconds;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.openqa.selenium.By.tagName;
import static ru.tinkoff.qa.neptune.core.api.properties.general.events.ToLimitReportDepth.TO_LIMIT_REPORT_DEPTH_PROPERTY;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.*;
import static ru.tinkoff.qa.neptune.selenium.test.FakeDOMModel.BUTTON_TAG;
import static ru.tinkoff.qa.neptune.selenium.test.captors.TestImageInjector.INJECTED;

public class WebDriverCaptorTest extends BaseCaptorTest {

    @BeforeClass
    public void prepareClass() {
        TO_LIMIT_REPORT_DEPTH_PROPERTY.accept(false);
    }

    @AfterClass
    public void finishClass() {
        TO_LIMIT_REPORT_DEPTH_PROPERTY.accept(true);
    }

    @Test
    public void clickOnElementPositiveTest() {
        seleniumSteps.click(webElement(tagName(BUTTON_TAG)));
        assertThat(INJECTED.size(), is(2));
        assertThat(INJECTED, hasEntry(isA(BufferedImage.class),
                equalTo("Screenshot taken from the element")));
        assertThat(INJECTED, hasEntry(isA(BufferedImage.class),
                equalTo("Screenshot taken after action on web element/widget")));
    }

    @Test
    public void clickOnElementNegativeTest() {
        try {
            seleniumSteps.click(webElement(tagName("fakeTag"))
                    .timeOut(ofSeconds(1)));
        } catch (NoSuchElementException ignored) {

        }
        assertThat(INJECTED.size(), is(1));
        assertThat(INJECTED.keySet(), contains(isA(BufferedImage.class)));
        assertThat(INJECTED.values(), contains("Browser screenshot"));
    }

    @Test
    public void clickOnWidgetPositiveTest() {
        seleniumSteps.click(link());
        assertThat(INJECTED.size(), is(2));
        assertThat(INJECTED, hasEntry(isA(BufferedImage.class),
                equalTo("Screenshot taken from the element")));
        assertThat(INJECTED, hasEntry(isA(BufferedImage.class),
                equalTo("Screenshot taken after action on web element/widget")));
    }

    @Test
    public void clickOnOWidgetNegativeTest() {
        try {
            seleniumSteps.click(link("Fake link")
                    .timeOut(ofSeconds(1)));
        } catch (NoSuchElementException ignored) {

        }
        assertThat(INJECTED.size(), is(1));
        assertThat(INJECTED.keySet(), contains(isA(BufferedImage.class)));
        assertThat(INJECTED.values(), contains("Browser screenshot"));
    }

    @Test
    public void editWidgetPositiveTest() {
        seleniumSteps.edit(flag(), true);
        assertThat(INJECTED.size(), is(2));
        assertThat(INJECTED, hasEntry(isA(BufferedImage.class),
                equalTo("Screenshot taken from the element")));
        assertThat(INJECTED, hasEntry(isA(BufferedImage.class),
                equalTo("Screenshot taken after action on web element/widget")));
    }

    @Test
    public void editOnOWidgetNegativeTest() {
        try {
            seleniumSteps.edit(flag("Fake flag").timeOut(ofSeconds(1)), true);
        } catch (NoSuchElementException ignored) {

        }
        assertThat(INJECTED.size(), is(1));
        assertThat(INJECTED.keySet(), contains(isA(BufferedImage.class)));
        assertThat(INJECTED.values(), contains("Browser screenshot"));
    }
}
