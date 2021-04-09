package ru.tinkoff.qa.neptune.selenium.test.hooks;

import org.hamcrest.Matcher;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.selenium.content.management.ContentManagementHook;
import ru.tinkoff.qa.neptune.selenium.test.BaseWebDriverTest;
import ru.tinkoff.qa.neptune.selenium.test.MockWebDriver;

import java.lang.reflect.Method;
import java.net.URL;
import java.util.Random;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static ru.tinkoff.qa.neptune.selenium.content.management.ContentManagementCommand.getCurrentCommand;
import static ru.tinkoff.qa.neptune.selenium.properties.URLProperties.BASE_WEB_DRIVER_URL_PROPERTY;

public class NavigationHookTest extends BaseWebDriverTest {

    private static final ClassWithNavigationOnTest1 O1 = new ClassWithNavigationOnTest1();
    private static final ClassWithNavigationOnTest2 O2 = new ClassWithNavigationOnTest2();
    private static final ClassWithNavigationOnTest3 O3 = new ClassWithNavigationOnTest3();
    private static final ClassWithNavigationOnTest4 O4 = new ClassWithNavigationOnTest4();
    private static final ClassWithNavigationOnTest5 O5 = new ClassWithNavigationOnTest5();
    private static final ClassWithNavigationOnTest6 O6 = new ClassWithNavigationOnTest6();
    private static final ClassWithNavigationOnTest7 O7 = new ClassWithNavigationOnTest7();
    private static final ClassWithNavigationOnTest8 O8 = new ClassWithNavigationOnTest8();
    private static final ClassWithNavigationOnTest9 O9 = new ClassWithNavigationOnTest9();
    private static final ClassWithNavigationOnTest10 O10 = new ClassWithNavigationOnTest10();
    private static final ClassWithNavigationOnTest11 O11 = new ClassWithNavigationOnTest11();
    private static final ClassWithNavigationOnTest12 O12 = new ClassWithNavigationOnTest12();
    private static final ClassWithNavigationOnTest13 O13 = new ClassWithNavigationOnTest13();
    private static final ClassWithNavigationOnTest14 O14 = new ClassWithNavigationOnTest14();
    private static final ClassWithNavigationOnTest15 O15 = new ClassWithNavigationOnTest15();

    private static final ContentManagementHook hook = new ContentManagementHook();

    private static Method getMethod(Object o, String method) throws NoSuchMethodException {
        return o.getClass().getDeclaredMethod(method);
    }

    @DataProvider
    public static Object[][] data() {
        return new Object[][]{
                {O1, "test1", true, equalTo("https://www.google.com")},
                {O1, "test1", false, anything()},
                {O1, "test2", true, equalTo("https://github.com")},
                {O7, "test1", true, emptyOrNullString()},
                {O7, "test2", true, equalTo("https://www.google.com")},
                {O8, "test1", true, equalTo("https://www.google.com")},
                {O8, "test2", false, anything()}
        };
    }

    @DataProvider
    public static Object[][] data2() {
        return new Object[][]{
                {"test1", true, equalTo("https://www.google.com")},
                {"test1", false, equalTo("https://www.google.com")},
                {"test2", true, equalTo("https://github.com")},
                {"test2", false, equalTo("https://github.com")},
        };
    }

    @DataProvider
    public static Object[][] data3() {
        return new Object[][]{
                {"test1", false, false, anything()},
                {"test1", true, true, equalTo("https://www.google.com")},
                {"test2", true, true, equalTo("https://github.com")},
                {"test2", false, true, equalTo("https://github.com")},
        };
    }

    @DataProvider
    public static Object[][] data4() {
        return new Object[][]{
                {O5, "test1", equalTo("https://www.google.com/1/ABC%20/ABC%20?&p4=1&p5=ABC &p6=ABCD+&p7=1")},
                {O5, "test2", equalTo("https://www.google.com/1/ABC%20/ABC%20?&p4=1&p5=ABC &p6=ABCD+&p7=1")},
                {O6, "test1", equalTo("https://www.google.com/#?p4=1&p5=ABC &p6=ABCD &p7=1")},
                {O6, "test2", equalTo("https://www.google.com/1/ABC%20/ABC%20#?p4=1&p5=ABC &p6=ABCD &p7=1")},
                {O9, "test1", equalTo("https://www.google.com/1/ABC%20/ABC%20?&p4=1&p5=ABC &p6=ABCD+&p7=ABCDE+")},
                {O9, "test2", equalTo("https://www.google.com/1/ABC%20/ABC%20?&p4=1&p5=ABC &p6=ABCD+&p7=ABCDE+")},
                {O10, "test1", equalTo("https://www.google.com/1/ABC%20/ABC%20?&p4=1&p5=ABC &p6=ABCD+&p7=ABCDE+")},
                {O10, "test2", equalTo("https://www.google.com/1/ABC%20/ABC%20?&p4=1&p5=ABC &p6=ABCD+&p7=ABCDE+")},
                {O11, "test1", equalTo("https://www.google.com/1/ABC%20/ABC%20?&p4=1&p5=ABC &p6=ABCD+&p7=ABCDE")},
                {O11, "test2", equalTo("https://www.google.com/1/ABC%20/ABC%20?&p4=1&p5=ABC &p6=ABCD+&p7=ABCDE")},
                {O12, "test1", equalTo("https://www.google.com/1/ABC%20/ABC%20?&p4=1&p5=ABC &p6=ABCD+&p7=ABCDE+")},
                {O12, "test2", equalTo("https://www.google.com/1/ABC%20/ABC%20?&p4=1&p5=ABC &p6=ABCD+&p7=ABCDE+")},
                {O13, "test1", equalTo("https://www.google.com/1/ABC%20/ABC%20?&p4=1&p5=ABC &p6=ABCD+&p7=ABCDE+")},
                {O13, "test2", equalTo("https://www.google.com/1/ABC%20/ABC%20?&p4=1&p5=ABC &p6=ABCD+&p7=ABCDE+")},
                {O14, "test1", equalTo("https://www.google.com/1/ABC%20/ABC%20?&p4=1&p5=ABC &p6=ABCD+&p7=ABCDE")},
                {O14, "test2", equalTo("https://www.google.com/1/ABC%20/ABC%20?&p4=1&p5=ABC &p6=ABCD+&p7=ABCDE")},
                {O15, "test1", equalTo("https://www.google.com/1/ABC%20/ABC%20?&p4=1&p5=ABC &p6=ABCD+&p7=Static+")},
                {O15, "test2", equalTo("https://www.google.com/1/ABC%20/ABC%20?&p4=1&p5=ABC &p6=ABCD+&p7=Static+")},
        };
    }

    @Test(dataProvider = "data")
    public void test1(Object o, String method, boolean hasCommand, Matcher<? super String> matcher) throws Exception {
        var m = getMethod(o, method);

        hook.executeMethodHook(m, o, new Random().nextBoolean());
        var command = getCurrentCommand();
        assertThat("Expected presence of a command", command != null, is(hasCommand));

        if (command == null) {
            return;
        }

        command.get().performAction(wrappedWebDriver.getWrappedDriver());
        assertThat(((MockWebDriver) seleniumSteps.getWrappedDriver()).lastNavigationURLAsIs, matcher);
    }

    @Test(dataProvider = "data2")
    public void test2(String method, boolean isTest, Matcher<? super String> matcher) throws Exception {
        var m = getMethod(O1, method);

        hook.executeMethodHook(m, O2, isTest);
        var command = getCurrentCommand();
        assertThat("Expected presence of a command", command != null, is(true));
        command.get().performAction(wrappedWebDriver.getWrappedDriver());
        assertThat(((MockWebDriver) seleniumSteps.getWrappedDriver()).lastNavigationURLAsIs, matcher);
    }

    @Test(dataProvider = "data3")
    public void test3(String method, boolean isTest, boolean hasCommand, Matcher<? super String> matcher) throws Exception {
        var m = getMethod(O3, method);

        hook.executeMethodHook(m, O3, isTest);
        var command = getCurrentCommand();
        assertThat("Expected presence of a command", command != null, is(hasCommand));

        if (command == null) {
            return;
        }

        command.get().performAction(wrappedWebDriver.getWrappedDriver());
        assertThat(((MockWebDriver) seleniumSteps.getWrappedDriver()).lastNavigationURLAsIs, matcher);
    }

    @Test
    public void test4() throws Exception {
        var m1 = getMethod(O4, "test1");
        var m2 = getMethod(O4, "test2");

        hook.executeMethodHook(m1, O4, new Random().nextBoolean());

        var command = getCurrentCommand();
        command.get().performAction(wrappedWebDriver.getWrappedDriver());
        assertThat(((MockWebDriver) seleniumSteps.getWrappedDriver()).lastNavigationURLAsIs,
                equalTo("https://www.google.com/1/ABC%20/ABC%20?&p4=1&p5=ABC &p6=ABCD+&p7=1"));

        hook.executeMethodHook(m2, O4, new Random().nextBoolean());
        command = getCurrentCommand();

        O4.host2 = "github.com.рф";
        O4.port = 100;

        command.get().performAction(wrappedWebDriver.getWrappedDriver());
        assertThat(((MockWebDriver) seleniumSteps.getWrappedDriver()).lastNavigationURLAsIs,
                equalTo("https://github.com.рф:100/1/ABC%20/ABC%20?&p4=1&p5=ABC &p6=ABCD+&p7=1"));
    }

    @Test(dataProvider = "data4")
    public void test5(Object o, String method, Matcher<? super String> matcher) throws Exception {
        BASE_WEB_DRIVER_URL_PROPERTY.accept(new URL("https://www.google.com"));
        var m = getMethod(o, method);

        hook.executeMethodHook(m, o, new Random().nextBoolean());
        var command = getCurrentCommand();
        command.get().performAction(wrappedWebDriver.getWrappedDriver());
        assertThat(((MockWebDriver) seleniumSteps.getWrappedDriver()).lastNavigationURLAsIs, matcher);
    }
}
