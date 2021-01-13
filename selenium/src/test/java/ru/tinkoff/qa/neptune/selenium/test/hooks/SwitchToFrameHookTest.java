package ru.tinkoff.qa.neptune.selenium.test.hooks;

import org.openqa.selenium.NoSuchFrameException;
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
import static ru.tinkoff.qa.neptune.selenium.test.FakeDOMModel.FRAME_ELEMENT_VALID1;

public class SwitchToFrameHookTest extends BaseWebDriverTest {

    private static final ContentManagementHook hook = new ContentManagementHook();
    private static final ClassWithSwitchingToFrameTest1 O1 = new ClassWithSwitchingToFrameTest1();
    private static final ClassWithSwitchingToFrameTest2 O2 = new ClassWithSwitchingToFrameTest2();

    private static Method getMethod(Object o, String method) throws NoSuchMethodException {
        return o.getClass().getDeclaredMethod(method);
    }

    @DataProvider
    public static Object[][] data() {
        return new Object[][]{
                {O1, "test1", 2},
                {O1, "test2", 1},
                {O1, "test3", "name2"},
                {O1, "test4", FRAME_ELEMENT_VALID1},
                {O2, "test1", 1},
                {O2, "test2", 1},
                {O2, "test3", FRAME_ELEMENT_VALID1},
        };
    }

    @Test(dataProvider = "data")
    public void test1(Object o, String method, Object expected) throws Exception {
        var m = getMethod(o, method);

        hook.executeMethodHook(m, o, new Random().nextBoolean());
        var command = getCurrentCommand();
        command.get().accept(wrappedWebDriver.getWrappedDriver());
        assertThat(((MockWebDriver) seleniumSteps.getWrappedDriver()).getCurrentFrame(), equalTo(expected));
    }

    @Test(expectedExceptions = NoSuchFrameException.class)
    public void test2() throws Exception {
        var m = getMethod(O1, "test5");

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
