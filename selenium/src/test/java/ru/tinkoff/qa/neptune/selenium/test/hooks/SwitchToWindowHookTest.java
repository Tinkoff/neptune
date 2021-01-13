package ru.tinkoff.qa.neptune.selenium.test.hooks;

import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.selenium.content.management.ContentManagementHook;
import ru.tinkoff.qa.neptune.selenium.test.BaseWebDriverTest;
import ru.tinkoff.qa.neptune.selenium.test.MockWebDriver;

import java.lang.reflect.Method;
import java.util.Random;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.testng.AssertJUnit.fail;
import static ru.tinkoff.qa.neptune.selenium.content.management.ContentManagementCommand.getCurrentCommand;
import static ru.tinkoff.qa.neptune.selenium.test.enums.URLs.*;
import static ru.tinkoff.qa.neptune.selenium.test.enums.WindowHandles.*;

public class SwitchToWindowHookTest extends BaseWebDriverTest {

    private static final ContentManagementHook hook = new ContentManagementHook();
    private static final ClassWithSwitchingToWindowTest1 O1 = new ClassWithSwitchingToWindowTest1();
    private static final ClassWithSwitchingToWindowTest2 O2 = new ClassWithSwitchingToWindowTest2();

    private static Method getMethod(Object o, String method) throws NoSuchMethodException {
        return o.getClass().getDeclaredMethod(method);
    }

    @DataProvider
    public static Object[][] data() {
        return new Object[][]{
                {O1, "test1", HANDLE2.getHandle()},
                {O1, "test2", HANDLE3.getHandle()},
                {O2, "test1", HANDLE2.getHandle()},
                {O2, "test2", HANDLE2.getHandle()},
                {O2, "test3", HANDLE3.getHandle()},
        };
    }

    @BeforeMethod
    public void prepare() {
        WebDriver driver = seleniumSteps.getWrappedDriver();
        driver.switchTo().window(HANDLE1.getHandle()).get(GOOGLE.getUrl());
        driver.switchTo().window(HANDLE2.getHandle()).get(FACEBOOK.getUrl());
        driver.switchTo().window(HANDLE3.getHandle()).get(GITHUB.getUrl());
    }

    @Test(dataProvider = "data")
    public void test1(Object o, String method, String expected) throws Exception {
        var m = getMethod(o, method);

        hook.executeMethodHook(m, o, new Random().nextBoolean());
        var command = getCurrentCommand();
        command.get().accept(wrappedWebDriver.getWrappedDriver());
        assertThat(((MockWebDriver) seleniumSteps.getWrappedDriver()).getMockHandle(), equalTo(expected));
    }

    @Test(expectedExceptions = NoSuchWindowException.class)
    public void test2() throws Exception {
        var m = getMethod(O1, "test3");

        hook.executeMethodHook(m, O1, new Random().nextBoolean());
        var command = getCurrentCommand();
        try {
            setStartBenchMark();
            command.get().accept(wrappedWebDriver.getWrappedDriver());
        } catch (Exception e) {
            setEndBenchMark();
            assertThat(getTimeDifference(), greaterThanOrEqualTo(FIVE_SECONDS.toMillis()));
            assertThat(getTimeDifference() - FIVE_SECONDS.toMillis(), lessThan(HALF_SECOND.toMillis()));
            throw e;
        }
        fail("Exception was expected");
    }
}
