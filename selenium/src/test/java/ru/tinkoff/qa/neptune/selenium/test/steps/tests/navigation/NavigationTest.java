package ru.tinkoff.qa.neptune.selenium.test.steps.tests.navigation;

import ru.tinkoff.qa.neptune.selenium.functions.target.locator.window.Window;
import ru.tinkoff.qa.neptune.selenium.test.BaseWebDriverTest;
import ru.tinkoff.qa.neptune.selenium.test.MockWindow;
import org.testng.annotations.Test;

import static java.lang.System.currentTimeMillis;
import static java.lang.System.getProperties;
import static java.time.Duration.ofMillis;
import static java.time.Duration.ofSeconds;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.*;
import static ru.tinkoff.qa.neptune.core.api.StoryWriter.condition;
import static ru.tinkoff.qa.neptune.selenium.functions.navigation.Back.back;
import static ru.tinkoff.qa.neptune.selenium.functions.navigation.Forward.forward;
import static ru.tinkoff.qa.neptune.selenium.functions.navigation.GetCurrentUrlSupplier.currentUrl;
import static ru.tinkoff.qa.neptune.selenium.functions.navigation.GetCurrentUrlSupplier.currentUrlOf;
import static ru.tinkoff.qa.neptune.selenium.functions.navigation.Refresh.refresh;
import static ru.tinkoff.qa.neptune.selenium.functions.navigation.ToUrl.toUrl;
import static ru.tinkoff.qa.neptune.selenium.functions.target.locator.window.GetWindowSupplier.window;
import static ru.tinkoff.qa.neptune.selenium.properties.SessionFlagProperties.ENABLE_ABILITY_TO_NAVIGATE_BY_RELATIVE_URL;
import static ru.tinkoff.qa.neptune.selenium.properties.SessionFlagProperties.GET_BACK_TO_BASE_URL;
import static ru.tinkoff.qa.neptune.selenium.properties.URLProperties.BASE_WEB_DRIVER_URL_PROPERTY;
import static ru.tinkoff.qa.neptune.selenium.properties.WaitingProperties.TimeUnitProperties.WAITING_FOR_PAGE_LOADED_TIME_UNIT;
import static ru.tinkoff.qa.neptune.selenium.properties.WaitingProperties.TimeValueProperties.WAITING_FOR_PAGE_LOADED_TIME_VALUE;
import static ru.tinkoff.qa.neptune.selenium.test.enums.URLs.*;
import static ru.tinkoff.qa.neptune.selenium.test.enums.WindowHandles.HANDLE1;
import static ru.tinkoff.qa.neptune.selenium.test.enums.WindowHandles.HANDLE2;
import static ru.tinkoff.qa.neptune.selenium.test.enums.WindowHandles.HANDLE3;
import static org.hamcrest.MatcherAssert.assertThat;

public class NavigationTest extends BaseWebDriverTest {

    @Test
    public void navigationInTheFirstWindow() {
        seleniumSteps.navigate(toUrl(PAY_PAL.getUrl()));
        assertThat(seleniumSteps.get(currentUrl()), is(PAY_PAL.getUrl()));
    }

    @Test
    public void navigationInWindowBySearching() {
        seleniumSteps.navigate(toUrl(window().byIndex(1), DEEZER.getUrl()));
        assertThat(seleniumSteps.get(currentUrlOf(window().byIndex(1))), is(DEEZER.getUrl()));

        Window window = seleniumSteps.get(window().byIndex(1));
        assertThat(window.getCurrentUrl(), is(DEEZER.getUrl()));
    }

    @Test
    public void navigationInTheWindow() {
        Window window = seleniumSteps.get(window().byIndex(2));

        seleniumSteps.navigate(toUrl(window, YOUTUBE.getUrl()));
        assertThat(seleniumSteps.get(currentUrlOf(window().byIndex(2))), is(YOUTUBE.getUrl()));

        assertThat(window.getCurrentUrl(), is(YOUTUBE.getUrl()));
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "It is impossible to navigate by URL /index.html. This value is not a valid URL and the " +
                    "property enable.ability.to.use.relative.url is not defined or its value is false")
    public void invalidNavigationByRelativeUrl() {
        seleniumSteps.navigate(toUrl("/index.html"));
        assertThat(seleniumSteps.get(currentUrl()), containsString("/index.html"));
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "It is impossible to navigate by URL /index.html. " +
                    "This value is not a valid URL and the property base.web.driver.url is not defined")
    public void invalidNavigationByRelativeUrl2() {
        System.setProperty(ENABLE_ABILITY_TO_NAVIGATE_BY_RELATIVE_URL.getPropertyName(), "true");
        try {
            seleniumSteps.navigate(toUrl("/index.html"));
            assertThat(seleniumSteps.get(currentUrl()), containsString("/index.html"));
        }
        finally {
            getProperties().remove(GET_BACK_TO_BASE_URL.getPropertyName());
        }
    }

    @Test
    public void validNavigationByRelativeUrl() {
        System.setProperty(BASE_WEB_DRIVER_URL_PROPERTY.getPropertyName(), GITHUB.getUrl());
        System.setProperty(ENABLE_ABILITY_TO_NAVIGATE_BY_RELATIVE_URL.getPropertyName(), "true");
        try {
            seleniumSteps.navigate(toUrl("/index.html"));
            assertThat(seleniumSteps.get(currentUrl()), containsString(GITHUB.getUrl()));
        }
        finally {
            getProperties().remove(BASE_WEB_DRIVER_URL_PROPERTY.getPropertyName());
            getProperties().remove(GET_BACK_TO_BASE_URL.getPropertyName());
        }
    }

    @Test
    public void getConditionalCurrentUrlPositive() {
        seleniumSteps.navigate(toUrl(DEEZER.getUrl()));

        long start = currentTimeMillis();
        String currentUrl = seleniumSteps.get(currentUrl()
                .onCondition(condition("url contains " + DEEZER.getUrl(),
                        s -> s.contains(DEEZER.getUrl())))
                .waitFor(ofSeconds(3)));
        long end = currentTimeMillis();

        assertThat(currentUrl, equalTo(DEEZER.getUrl()));
        assertThat(end - start, lessThanOrEqualTo(ofMillis(500).toMillis()));
    }

    @Test(expectedExceptions = IllegalStateException.class, expectedExceptionsMessageRegExp = "Page was not loaded. Expected condition url contains https://github.com/. " +
            "Waiting time 00:05.000")
    public void getConditionalCurrentUrlNegativeWithTimeDefinedInProperties() {
        setProperty(WAITING_FOR_PAGE_LOADED_TIME_UNIT.getPropertyName(), "SECONDS");
        setProperty(WAITING_FOR_PAGE_LOADED_TIME_VALUE.getPropertyName(), "5");

        seleniumSteps.navigate(toUrl(DEEZER.getUrl()));
        long start = currentTimeMillis();
        try {
            seleniumSteps.get(currentUrl()
                    .onCondition(condition("url contains " + GITHUB.getUrl(),
                            s -> s.contains(GITHUB.getUrl()))));
        }
        finally {
            try {
                long end = currentTimeMillis();
                assertThat(end - start, greaterThanOrEqualTo(ofSeconds(5).toMillis()));
                assertThat(end - start - ofSeconds(5).toMillis(),
                        lessThanOrEqualTo(ofMillis(500).toMillis()));
            }
            finally {
                getProperties().remove(WAITING_FOR_PAGE_LOADED_TIME_UNIT.getPropertyName());
                getProperties().remove(WAITING_FOR_PAGE_LOADED_TIME_VALUE.getPropertyName());
            }
        }

    }

    @Test(expectedExceptions = IllegalStateException.class, expectedExceptionsMessageRegExp = "Page was not loaded. Expected condition url contains https://github.com/. " +
            "Waiting time 00:02.000")
    public void getConditionalCurrentUrlNegativeWithTimeDefinedExternally() {
        setProperty(WAITING_FOR_PAGE_LOADED_TIME_UNIT.getPropertyName(), "SECONDS");
        setProperty(WAITING_FOR_PAGE_LOADED_TIME_VALUE.getPropertyName(), "5");

        seleniumSteps.navigate(toUrl(DEEZER.getUrl()));
        long start = currentTimeMillis();
        try {
            seleniumSteps.get(currentUrl()
                    .onCondition(condition("url contains " + GITHUB.getUrl(),
                            s -> s.contains(GITHUB.getUrl())))
                    .waitFor(ofSeconds(2)));
        }
        finally {
            try {
                long end = currentTimeMillis();
                assertThat(end - start, greaterThanOrEqualTo(ofSeconds(2).toMillis()));
                assertThat(end - start - ofSeconds(2).toMillis(),
                        lessThanOrEqualTo(ofMillis(500).toMillis()));
            }
            finally {
                getProperties().remove(WAITING_FOR_PAGE_LOADED_TIME_UNIT.getPropertyName());
                getProperties().remove(WAITING_FOR_PAGE_LOADED_TIME_VALUE.getPropertyName());
            }
        }

    }

    @Test
    public void chainedNavigationTest() {
        Window window = seleniumSteps.get(window().byIndex(2));

        seleniumSteps.navigate(toUrl(PAY_PAL.getUrl())
                .andThenToUrl(window().byIndex(1), DEEZER.getUrl())
                .andThenToUrl(window, YOUTUBE.getUrl()));

        assertThat(seleniumSteps.get(currentUrl()), is(PAY_PAL.getUrl()));
        Window window2 = seleniumSteps.get(window().byIndex(1));
        assertThat(window2.getCurrentUrl(), is(DEEZER.getUrl()));

        assertThat(window.getCurrentUrl(), is(YOUTUBE.getUrl()));
    }

    @Test(dependsOnMethods = "navigationInTheFirstWindow")
    public void backForwardFirstWindowTest() {
        Window first = seleniumSteps.get(window());
        seleniumSteps.navigate(toUrl(GOOGLE.getUrl())
                .andThenToUrl(GITHUB.getUrl())
                .andThenToUrl(FACEBOOK.getUrl())
                .andThenToUrl(DEEZER.getUrl())
                .andThenToUrl(PAY_PAL.getUrl())
                .andThenToUrl(YOUTUBE.getUrl()));

        seleniumSteps.navigate(back());
        assertThat(seleniumSteps.get(currentUrl()), is(PAY_PAL.getUrl()));
        assertThat(first.getCurrentUrl(), is(PAY_PAL.getUrl()));

        seleniumSteps.navigate(back());
        assertThat(seleniumSteps.get(currentUrl()), is(DEEZER.getUrl()));
        assertThat(first.getCurrentUrl(), is(DEEZER.getUrl()));

        seleniumSteps.navigate(back());
        assertThat(seleniumSteps.get(currentUrl()), is(FACEBOOK.getUrl()));
        assertThat(first.getCurrentUrl(), is(FACEBOOK.getUrl()));

        seleniumSteps.navigate(back());
        assertThat(seleniumSteps.get(currentUrl()), is(GITHUB.getUrl()));
        assertThat(first.getCurrentUrl(), is(GITHUB.getUrl()));

        seleniumSteps.navigate(back());
        assertThat(seleniumSteps.get(currentUrl()), is(GOOGLE.getUrl()));
        assertThat(first.getCurrentUrl(), is(GOOGLE.getUrl()));

        seleniumSteps.navigate(back());
        assertThat(seleniumSteps.get(currentUrl()), is(BLANK.getUrl()));
        assertThat(first.getCurrentUrl(), is(BLANK.getUrl()));

        seleniumSteps.navigate(forward());
        assertThat(seleniumSteps.get(currentUrl()), is(GOOGLE.getUrl()));
        assertThat(first.getCurrentUrl(), is(GOOGLE.getUrl()));

        seleniumSteps.navigate(forward());
        assertThat(seleniumSteps.get(currentUrl()), is(GITHUB.getUrl()));
        assertThat(first.getCurrentUrl(), is(GITHUB.getUrl()));

        seleniumSteps.navigate(forward());
        assertThat(seleniumSteps.get(currentUrl()), is(FACEBOOK.getUrl()));
        assertThat(first.getCurrentUrl(), is(FACEBOOK.getUrl()));

        seleniumSteps.navigate(forward());
        assertThat(seleniumSteps.get(currentUrl()), is(DEEZER.getUrl()));
        assertThat(first.getCurrentUrl(), is(DEEZER.getUrl()));

        seleniumSteps.navigate(forward());
        assertThat(seleniumSteps.get(currentUrl()), is(PAY_PAL.getUrl()));
        assertThat(first.getCurrentUrl(), is(PAY_PAL.getUrl()));

        seleniumSteps.navigate(forward());
        assertThat(seleniumSteps.get(currentUrl()), is(YOUTUBE.getUrl()));
        assertThat(first.getCurrentUrl(), is(YOUTUBE.getUrl()));
    }

    @Test(dependsOnMethods = "navigationInWindowBySearching")
    public void backForwardInWindowBySearchingTest() {
        Window second = seleniumSteps.get(window().byIndex(1));
        seleniumSteps.navigate(toUrl(window().byIndex(1), GOOGLE.getUrl())
                .andThenToUrl(window().byIndex(1), GITHUB.getUrl())
                .andThenToUrl(window().byIndex(1), FACEBOOK.getUrl())
                .andThenToUrl(window().byIndex(1), DEEZER.getUrl())
                .andThenToUrl(window().byIndex(1), PAY_PAL.getUrl())
                .andThenToUrl(window().byIndex(1), YOUTUBE.getUrl()));

        seleniumSteps.navigate(back(window().byIndex(1)));
        assertThat(seleniumSteps.get(currentUrlOf(window().byIndex(1))), is(PAY_PAL.getUrl()));
        assertThat(second.getCurrentUrl(), is(PAY_PAL.getUrl()));

        seleniumSteps.navigate(back(window().byIndex(1)));
        assertThat(seleniumSteps.get(currentUrlOf(window().byIndex(1))), is(DEEZER.getUrl()));
        assertThat(second.getCurrentUrl(), is(DEEZER.getUrl()));

        seleniumSteps.navigate(back(window().byIndex(1)));
        assertThat(seleniumSteps.get(currentUrlOf(window().byIndex(1))), is(FACEBOOK.getUrl()));
        assertThat(second.getCurrentUrl(), is(FACEBOOK.getUrl()));

        seleniumSteps.navigate(back(window().byIndex(1)));
        assertThat(seleniumSteps.get(currentUrlOf(window().byIndex(1))), is(GITHUB.getUrl()));
        assertThat(second.getCurrentUrl(), is(GITHUB.getUrl()));

        seleniumSteps.navigate(back(window().byIndex(1)));
        assertThat(seleniumSteps.get(currentUrlOf(window().byIndex(1))), is(GOOGLE.getUrl()));
        assertThat(second.getCurrentUrl(), is(GOOGLE.getUrl()));

        seleniumSteps.navigate(back(window().byIndex(1)));
        assertThat(seleniumSteps.get(currentUrlOf(window().byIndex(1))), is(BLANK.getUrl()));
        assertThat(second.getCurrentUrl(), is(BLANK.getUrl()));

        seleniumSteps.navigate(forward(window().byIndex(1)));
        assertThat(seleniumSteps.get(currentUrlOf(window().byIndex(1))), is(GOOGLE.getUrl()));
        assertThat(second.getCurrentUrl(), is(GOOGLE.getUrl()));

        seleniumSteps.navigate(forward(window().byIndex(1)));
        assertThat(seleniumSteps.get(currentUrlOf(window().byIndex(1))), is(GITHUB.getUrl()));
        assertThat(second.getCurrentUrl(), is(GITHUB.getUrl()));

        seleniumSteps.navigate(forward(window().byIndex(1)));
        assertThat(seleniumSteps.get(currentUrlOf(window().byIndex(1))), is(FACEBOOK.getUrl()));
        assertThat(second.getCurrentUrl(), is(FACEBOOK.getUrl()));

        seleniumSteps.navigate(forward(window().byIndex(1)));
        assertThat(seleniumSteps.get(currentUrlOf(window().byIndex(1))), is(DEEZER.getUrl()));
        assertThat(second.getCurrentUrl(), is(DEEZER.getUrl()));

        seleniumSteps.navigate(forward(window().byIndex(1)));
        assertThat(seleniumSteps.get(currentUrlOf(window().byIndex(1))), is(PAY_PAL.getUrl()));
        assertThat(second.getCurrentUrl(), is(PAY_PAL.getUrl()));

        seleniumSteps.navigate(forward(window().byIndex(1)));
        assertThat(seleniumSteps.get(currentUrlOf(window().byIndex(1))), is(YOUTUBE.getUrl()));
        assertThat(second.getCurrentUrl(), is(YOUTUBE.getUrl()));
    }

    @Test(dependsOnMethods = "navigationInWindowBySearching")
    public void backForwardInTheWindowTest() {
        Window third = seleniumSteps.get(window().byIndex(2));
        seleniumSteps.navigate(toUrl(third, GOOGLE.getUrl())
                .andThenToUrl(third, GITHUB.getUrl())
                .andThenToUrl(third, FACEBOOK.getUrl())
                .andThenToUrl(third, DEEZER.getUrl())
                .andThenToUrl(third, PAY_PAL.getUrl())
                .andThenToUrl(third, YOUTUBE.getUrl()));

        seleniumSteps.navigate(back(third));
        assertThat(seleniumSteps.get(currentUrlOf(window().byIndex(2))), is(PAY_PAL.getUrl()));
        assertThat(third.getCurrentUrl(), is(PAY_PAL.getUrl()));

        seleniumSteps.navigate(back(third));
        assertThat(seleniumSteps.get(currentUrlOf(window().byIndex(2))), is(DEEZER.getUrl()));
        assertThat(third.getCurrentUrl(), is(DEEZER.getUrl()));

        seleniumSteps.navigate(back(third));
        assertThat(seleniumSteps.get(currentUrlOf(window().byIndex(2))), is(FACEBOOK.getUrl()));
        assertThat(third.getCurrentUrl(), is(FACEBOOK.getUrl()));

        seleniumSteps.navigate(back(third));
        assertThat(seleniumSteps.get(currentUrlOf(window().byIndex(2))), is(GITHUB.getUrl()));
        assertThat(third.getCurrentUrl(), is(GITHUB.getUrl()));

        seleniumSteps.navigate(back(third));
        assertThat(seleniumSteps.get(currentUrlOf(window().byIndex(2))), is(GOOGLE.getUrl()));
        assertThat(third.getCurrentUrl(), is(GOOGLE.getUrl()));

        seleniumSteps.navigate(back(third));
        assertThat(seleniumSteps.get(currentUrlOf(window().byIndex(2))), is(BLANK.getUrl()));
        assertThat(third.getCurrentUrl(), is(BLANK.getUrl()));

        seleniumSteps.navigate(forward(third));
        assertThat(seleniumSteps.get(currentUrlOf(window().byIndex(2))), is(GOOGLE.getUrl()));
        assertThat(third.getCurrentUrl(), is(GOOGLE.getUrl()));

        seleniumSteps.navigate(forward(third));
        assertThat(seleniumSteps.get(currentUrlOf(window().byIndex(2))), is(GITHUB.getUrl()));
        assertThat(third.getCurrentUrl(), is(GITHUB.getUrl()));

        seleniumSteps.navigate(forward(third));
        assertThat(seleniumSteps.get(currentUrlOf(window().byIndex(2))), is(FACEBOOK.getUrl()));
        assertThat(third.getCurrentUrl(), is(FACEBOOK.getUrl()));

        seleniumSteps.navigate(forward(third));
        assertThat(seleniumSteps.get(currentUrlOf(window().byIndex(2))), is(DEEZER.getUrl()));
        assertThat(third.getCurrentUrl(), is(DEEZER.getUrl()));

        seleniumSteps.navigate(forward(third));
        assertThat(seleniumSteps.get(currentUrlOf(window().byIndex(2))), is(PAY_PAL.getUrl()));
        assertThat(third.getCurrentUrl(), is(PAY_PAL.getUrl()));

        seleniumSteps.navigate(forward(third));
        assertThat(seleniumSteps.get(currentUrlOf(window().byIndex(2))), is(YOUTUBE.getUrl()));
        assertThat(third.getCurrentUrl(), is(YOUTUBE.getUrl()));
    }

    @Test
    public void chainedBackForwardTest() {
        Window thirdWindow = seleniumSteps.get(window().byIndex(2));
        seleniumSteps.navigate(toUrl(GOOGLE.getUrl())
                .andThenToUrl(GITHUB.getUrl())
                .andThenToUrl(window().byIndex(1), FACEBOOK.getUrl())
                .andThenToUrl(window().byIndex(1), DEEZER.getUrl())
                .andThenToUrl(window().byIndex(2), PAY_PAL.getUrl())
                .andThenToUrl(window().byIndex(2), YOUTUBE.getUrl()));

        seleniumSteps.navigate(back().andThenBack(window().byIndex(1)).andThenBack(thirdWindow));
        assertThat(seleniumSteps.get(currentUrl()), is(GOOGLE.getUrl()));
        assertThat(seleniumSteps.get(currentUrlOf(window().byIndex(1))), is(FACEBOOK.getUrl()));
        assertThat(seleniumSteps.get(currentUrlOf(window().byIndex(2))), is(PAY_PAL.getUrl()));

        seleniumSteps.navigate(forward().andThenForward(window().byIndex(1)).andThenForward(thirdWindow));
        assertThat(seleniumSteps.get(currentUrl()), is(GITHUB.getUrl()));
        assertThat(seleniumSteps.get(currentUrlOf(window().byIndex(1))), is(DEEZER.getUrl()));
        assertThat(seleniumSteps.get(currentUrlOf(window().byIndex(2))), is(YOUTUBE.getUrl()));
    }

    @Test
    public void refreshFirstTest() {
        seleniumSteps.navigate(refresh());
        assertThat(((MockWindow) seleniumSteps.getWrappedDriver().manage().window()).isRefreshed(),
                is(true));
    }

    @Test
    public void refreshWindowBySearching() {
        seleniumSteps.navigate(refresh(window().byIndex(1)));
        seleniumSteps.getWrappedDriver().switchTo().window(HANDLE2.getHandle());

        assertThat(((MockWindow) seleniumSteps.getWrappedDriver().manage().window()).isRefreshed(),
                is(true));
    }

    @Test
    public void refreshTheWindow() {
        Window window = seleniumSteps.get(window().byIndex(2));
        seleniumSteps.navigate(refresh(window));

        seleniumSteps.getWrappedDriver().switchTo().window(HANDLE3.getHandle());

        assertThat(((MockWindow) seleniumSteps.getWrappedDriver().manage().window()).isRefreshed(),
                is(true));
    }

    @Test
    public void chainedRefreshTest() {
        Window window = seleniumSteps.get(window().byIndex(2));
        seleniumSteps.navigate(refresh(window().byIndex(1)).andThenRefresh(window));

        seleniumSteps.getWrappedDriver().switchTo().window(HANDLE1.getHandle());
        assertThat(((MockWindow) seleniumSteps.getWrappedDriver().manage().window()).isRefreshed(),
                is(false));

        seleniumSteps.getWrappedDriver().switchTo().window(HANDLE2.getHandle());
        assertThat(((MockWindow) seleniumSteps.getWrappedDriver().manage().window()).isRefreshed(),
                is(true));

        seleniumSteps.getWrappedDriver().switchTo().window(HANDLE3.getHandle());
        assertThat(((MockWindow) seleniumSteps.getWrappedDriver().manage().window()).isRefreshed(),
                is(true));
    }
}
