package ru.tinkoff.qa.neptune.selenium.test.hooks;

import org.hamcrest.Matcher;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;
import ru.tinkoff.qa.neptune.selenium.hooks.PageNavigationHook;
import ru.tinkoff.qa.neptune.selenium.test.BaseWebDriverTest;
import ru.tinkoff.qa.neptune.selenium.test.MockWebDriver;

import java.lang.reflect.Method;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.emptyOrNullString;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.mockStatic;

public class NavigationHookTest extends BaseWebDriverTest {

    private static final ClassWithNavigationOnTest1 O1 = new ClassWithNavigationOnTest1();
    private static final ClassWithNavigationOnTest2 O2 = new ClassWithNavigationOnTest2();
    private static final ClassWithNavigationOnTest3 O3 = new ClassWithNavigationOnTest3();

    private static final PageNavigationHook hook = new PageNavigationHook();

    private static Method getMethod(Object o, String method) throws NoSuchMethodException {
        return o.getClass().getDeclaredMethod(method);
    }

    @DataProvider
    public static Object[][] data() {
        return new Object[][]{
                {O1, emptyOrNullString(), false},
                {O2, equalTo("https://www.google.com/"), false},
                {O3, emptyOrNullString(), false},
                {O1, equalTo("https://www.google.com/"), true},
                {O2, equalTo("https://www.google.com/"), true},
                {O3, equalTo("https://www.google.com/?p=1&p2=ABC &p3=ABC+&p4=1&p5=ABC &p6=ABCD+&p7=ABCDE+"), true},
        };
    }

    @DataProvider
    public static Object[][] data2() {
        return new Object[][]{
                {O1, false},
                {O2, false},
                {O3, false},
                {O1, true},
                {O2, true},
                {O3, true},
        };
    }

    @DataProvider
    public static Object[][] data3() {
        return new Object[][]{
                {O1, equalTo("https://github.com/"), false},
                {O2, equalTo("https://github.com/"), false},
                {O3, equalTo("https://github.com/?p=1&p2=ABC &p3=ABC+&p4=1&p5=ABC &p6=ABCD+&p7=ABCDE+"), false},
                {O1, equalTo("https://github.com/"), true},
                {O2, equalTo("https://github.com/"), true},
                {O3, equalTo("https://github.com/?p=1&p2=ABC &p3=ABC+&p4=1&p5=ABC &p6=ABCD+&p7=ABCDE+"), true},
        };
    }

    @Test(dataProvider = "data")
    public void test1(Object o, Matcher<? super String> matcher, boolean isTest) throws Exception {
        var m = getMethod(o, "test1");

        try (var mock = mockStatic(SeleniumStepContext.class)) {
            mock.when(SeleniumStepContext::inBrowser).thenReturn(seleniumSteps);
            hook.executeMethodHook(m, o, isTest);
            assertThat(((MockWebDriver) seleniumSteps.getWrappedDriver()).lastNavigationURLAsIs, matcher);
        }
    }

    @Test(dataProvider = "data2")
    public void test2(Object o, boolean isTest) throws Exception {
        var m = getMethod(o, "test2");

        try (var mock = mockStatic(SeleniumStepContext.class)) {
            mock.when(SeleniumStepContext::inBrowser).thenReturn(seleniumSteps);
            hook.executeMethodHook(m, o, isTest);
            assertThat(((MockWebDriver) seleniumSteps.getWrappedDriver()).lastNavigationURLAsIs, emptyOrNullString());
        }
    }

    @Test(dataProvider = "data3")
    public void test3(Object o, Matcher<? super String> matcher, boolean isTest) throws Exception {
        var m = getMethod(o, "test3");

        try (var mock = mockStatic(SeleniumStepContext.class)) {
            mock.when(SeleniumStepContext::inBrowser).thenReturn(seleniumSteps);
            hook.executeMethodHook(m, o, isTest);
            assertThat(((MockWebDriver) seleniumSteps.getWrappedDriver()).lastNavigationURLAsIs, matcher);
        }
    }
}
