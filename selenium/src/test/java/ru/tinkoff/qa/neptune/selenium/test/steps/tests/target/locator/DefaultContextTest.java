package ru.tinkoff.qa.neptune.selenium.test.steps.tests.target.locator;

import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.selenium.test.BaseWebDriverTest;
import ru.tinkoff.qa.neptune.selenium.test.MockWebDriver;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static ru.tinkoff.qa.neptune.selenium.functions.target.locator.content.DefaultContentSupplier.defaultContent;
import static ru.tinkoff.qa.neptune.selenium.functions.target.locator.frame.parent.ParentFrameSupplier.parentFrame;

public class DefaultContextTest extends BaseWebDriverTest {
    @Test
    public void parentFrameTest() {
        assertThat(((MockWebDriver) seleniumSteps.get(parentFrame())).isSwitchedToParentFrame(),
                is(true));
    }

    @Test
    public void defaultContentTest() {
        assertThat(((MockWebDriver) seleniumSteps.get(defaultContent())).isSwitchedToDefaultContent(),
                is(true));
    }
}
