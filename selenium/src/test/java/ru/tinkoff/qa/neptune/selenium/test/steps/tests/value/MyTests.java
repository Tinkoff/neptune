package ru.tinkoff.qa.neptune.selenium.test.steps.tests.value;

import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.selenium.test.BaseWebDriverTest;

import static org.openqa.selenium.By.*;
import static ru.tinkoff.qa.neptune.selenium.functions.intreraction.InteractiveAction.contextClick;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.webElement;


public class MyTests extends BaseWebDriverTest {

    @Test
    public void myTest() {
        seleniumSteps.perform(contextClick(webElement(tagName("a"))));
    }
}
