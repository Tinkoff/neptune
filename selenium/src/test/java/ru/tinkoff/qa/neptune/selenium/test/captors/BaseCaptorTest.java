package ru.tinkoff.qa.neptune.selenium.test.captors;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import ru.tinkoff.qa.neptune.selenium.test.BaseWebDriverTest;

import static java.lang.System.getProperties;
import static ru.tinkoff.qa.neptune.core.api.properties.CapturedEvents.SUCCESS_AND_FAILURE;
import static ru.tinkoff.qa.neptune.core.api.properties.DoCapturesOf.DO_CAPTURES_OF_INSTANCE;
import static ru.tinkoff.qa.neptune.selenium.test.captors.TestImageInjector.INJECTED;

public class BaseCaptorTest extends BaseWebDriverTest {
    @BeforeClass
    public static void beforeClass() {
        DO_CAPTURES_OF_INSTANCE.accept(SUCCESS_AND_FAILURE.name());
    }

    @BeforeMethod
    public void beforeEachTest() {
        INJECTED.clear();
    }

    @AfterClass
    public static void afterClass() {
        getProperties().remove(DO_CAPTURES_OF_INSTANCE.getPropertyName());
    }
}
