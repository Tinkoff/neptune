package ru.tinkoff.qa.neptune.selenium.test.steps.tests.navigation;

import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.selenium.functions.target.locator.window.Window;
import ru.tinkoff.qa.neptune.selenium.test.BaseWebDriverTest;
import ru.tinkoff.qa.neptune.selenium.test.MockWindow;

import static java.lang.System.getProperties;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static ru.tinkoff.qa.neptune.selenium.functions.target.locator.window.GetWindowSupplier.window;
import static ru.tinkoff.qa.neptune.selenium.properties.SessionFlagProperties.ENABLE_ABILITY_TO_NAVIGATE_BY_RELATIVE_URL;
import static ru.tinkoff.qa.neptune.selenium.properties.SessionFlagProperties.GET_BACK_TO_BASE_URL;
import static ru.tinkoff.qa.neptune.selenium.properties.URLProperties.BASE_WEB_DRIVER_URL_PROPERTY;
import static ru.tinkoff.qa.neptune.selenium.test.enums.URLs.*;
import static ru.tinkoff.qa.neptune.selenium.test.enums.WindowHandles.*;

public class NavigationTest extends BaseWebDriverTest {

    @Test
    public void navigationInTheFirstWindow() {
        seleniumSteps.navigateTo(PAY_PAL.getUrl());
        assertThat(seleniumSteps.getCurrentUrl(), is(PAY_PAL.getUrl()));
    }

    @Test
    public void navigationInWindowBySearching() {
        seleniumSteps.navigateTo(DEEZER.getUrl(), window(1));
        assertThat(seleniumSteps.getCurrentUrl(window(1)), is(DEEZER.getUrl()));

        Window window = seleniumSteps.get(window(1));
        assertThat(window.getCurrentUrl(), is(DEEZER.getUrl()));
    }

    @Test
    public void navigationInTheWindow() {
        Window window = seleniumSteps.get(window(2));

        seleniumSteps.navigateTo(YOUTUBE.getUrl(), window);
        assertThat(seleniumSteps.getCurrentUrl(window(2)), is(YOUTUBE.getUrl()));

        assertThat(window.getCurrentUrl(), is(YOUTUBE.getUrl()));
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "It is impossible to navigate by URL /index.html. This value is not a valid URL and the " +
                    "property ENABLE_TO_NAVIGATE_BY_RELATIVE_URL is not defined/its value is false")
    public void invalidNavigationByRelativeUrl() {
        seleniumSteps.navigateTo("/index.html");
        assertThat(seleniumSteps.getCurrentUrl(), endsWith("/index.html"));
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "It is impossible to navigate by URL /index.html. " +
                    "This value is not a valid URL and the property BASE_WEB_DRIVER_URL is not defined")
    public void invalidNavigationByRelativeUrl2() {
        System.setProperty(ENABLE_ABILITY_TO_NAVIGATE_BY_RELATIVE_URL.getName(), "true");
        try {
            seleniumSteps.navigateTo("/index.html");
            assertThat(seleniumSteps.getCurrentUrl(), endsWith("/index.html"));
        }
        finally {
            getProperties().remove(GET_BACK_TO_BASE_URL.getName());
        }
    }

    @Test
    public void validNavigationByRelativeUrl() {
        System.setProperty(BASE_WEB_DRIVER_URL_PROPERTY.getName(), GITHUB.getUrl());
        System.setProperty(ENABLE_ABILITY_TO_NAVIGATE_BY_RELATIVE_URL.getName(), "true");
        try {
            seleniumSteps.navigateTo("/index.html");
            assertThat(seleniumSteps.getCurrentUrl(), containsString(GITHUB.getUrl()));
        } finally {
            getProperties().remove(BASE_WEB_DRIVER_URL_PROPERTY.getName());
            getProperties().remove(GET_BACK_TO_BASE_URL.getName());
        }
    }

    @Test
    public void chainedNavigationTest() {
        Window window = seleniumSteps.get(window(2));

        seleniumSteps.navigateTo(PAY_PAL.getUrl())
                .navigateTo(DEEZER.getUrl(), window(1))
                .navigateTo(YOUTUBE.getUrl(), window);

        assertThat(seleniumSteps.getCurrentUrl(), is(PAY_PAL.getUrl()));
        Window window2 = seleniumSteps.get(window(1));
        assertThat(window2.getCurrentUrl(), is(DEEZER.getUrl()));

        assertThat(window.getCurrentUrl(), is(YOUTUBE.getUrl()));
    }

    @Test(dependsOnMethods = "navigationInTheFirstWindow")
    public void backForwardFirstWindowTest() {
        Window first = seleniumSteps.get(window());
        seleniumSteps.navigateTo(GOOGLE.getUrl())
                .navigateTo(GITHUB.getUrl())
                .navigateTo(FACEBOOK.getUrl())
                .navigateTo(DEEZER.getUrl())
                .navigateTo(PAY_PAL.getUrl())
                .navigateTo(YOUTUBE.getUrl());

        seleniumSteps.navigateBack();
        assertThat(seleniumSteps.getCurrentUrl(), is(PAY_PAL.getUrl()));
        assertThat(first.getCurrentUrl(), is(PAY_PAL.getUrl()));

        seleniumSteps.navigateBack();
        assertThat(seleniumSteps.getCurrentUrl(), is(DEEZER.getUrl()));
        assertThat(first.getCurrentUrl(), is(DEEZER.getUrl()));

        seleniumSteps.navigateBack();
        assertThat(seleniumSteps.getCurrentUrl(), is(FACEBOOK.getUrl()));
        assertThat(first.getCurrentUrl(), is(FACEBOOK.getUrl()));

        seleniumSteps.navigateBack();
        assertThat(seleniumSteps.getCurrentUrl(), is(GITHUB.getUrl()));
        assertThat(first.getCurrentUrl(), is(GITHUB.getUrl()));

        seleniumSteps.navigateBack();
        assertThat(seleniumSteps.getCurrentUrl(), is(GOOGLE.getUrl()));
        assertThat(first.getCurrentUrl(), is(GOOGLE.getUrl()));

        seleniumSteps.navigateBack();
        assertThat(seleniumSteps.getCurrentUrl(), is(BLANK.getUrl()));
        assertThat(first.getCurrentUrl(), is(BLANK.getUrl()));

        seleniumSteps.navigateForward();
        assertThat(seleniumSteps.getCurrentUrl(), is(GOOGLE.getUrl()));
        assertThat(first.getCurrentUrl(), is(GOOGLE.getUrl()));

        seleniumSteps.navigateForward();
        assertThat(seleniumSteps.getCurrentUrl(), is(GITHUB.getUrl()));
        assertThat(first.getCurrentUrl(), is(GITHUB.getUrl()));

        seleniumSteps.navigateForward();
        assertThat(seleniumSteps.getCurrentUrl(), is(FACEBOOK.getUrl()));
        assertThat(first.getCurrentUrl(), is(FACEBOOK.getUrl()));

        seleniumSteps.navigateForward();
        assertThat(seleniumSteps.getCurrentUrl(), is(DEEZER.getUrl()));
        assertThat(first.getCurrentUrl(), is(DEEZER.getUrl()));

        seleniumSteps.navigateForward();
        assertThat(seleniumSteps.getCurrentUrl(), is(PAY_PAL.getUrl()));
        assertThat(first.getCurrentUrl(), is(PAY_PAL.getUrl()));

        seleniumSteps.navigateForward();
        assertThat(seleniumSteps.getCurrentUrl(), is(YOUTUBE.getUrl()));
        assertThat(first.getCurrentUrl(), is(YOUTUBE.getUrl()));
    }

    @Test(dependsOnMethods = "navigationInWindowBySearching")
    public void backForwardInWindowBySearchingTest() {
        Window second = seleniumSteps.get(window(1));
        seleniumSteps.navigateTo(GOOGLE.getUrl(), window(1))
                .navigateTo(GITHUB.getUrl(), window(1))
                .navigateTo(FACEBOOK.getUrl(), window(1))
                .navigateTo(DEEZER.getUrl(), window(1))
                .navigateTo(PAY_PAL.getUrl(), window(1))
                .navigateTo(YOUTUBE.getUrl(), window(1));

        seleniumSteps.navigateBack(window(1));
        assertThat(seleniumSteps.getCurrentUrl(window(1)), is(PAY_PAL.getUrl()));
        assertThat(second.getCurrentUrl(), is(PAY_PAL.getUrl()));

        seleniumSteps.navigateBack(window(1));
        assertThat(seleniumSteps.getCurrentUrl(window(1)), is(DEEZER.getUrl()));
        assertThat(second.getCurrentUrl(), is(DEEZER.getUrl()));

        seleniumSteps.navigateBack(window(1));
        assertThat(seleniumSteps.getCurrentUrl(window(1)), is(FACEBOOK.getUrl()));
        assertThat(second.getCurrentUrl(), is(FACEBOOK.getUrl()));

        seleniumSteps.navigateBack(window(1));
        assertThat(seleniumSteps.getCurrentUrl(window(1)), is(GITHUB.getUrl()));
        assertThat(second.getCurrentUrl(), is(GITHUB.getUrl()));

        seleniumSteps.navigateBack(window(1));
        assertThat(seleniumSteps.getCurrentUrl(window(1)), is(GOOGLE.getUrl()));
        assertThat(second.getCurrentUrl(), is(GOOGLE.getUrl()));

        seleniumSteps.navigateBack(window(1));
        assertThat(seleniumSteps.getCurrentUrl(window(1)), is(BLANK.getUrl()));
        assertThat(second.getCurrentUrl(), is(BLANK.getUrl()));

        seleniumSteps.navigateForward(window(1));
        assertThat(seleniumSteps.getCurrentUrl(window(1)), is(GOOGLE.getUrl()));
        assertThat(second.getCurrentUrl(), is(GOOGLE.getUrl()));

        seleniumSteps.navigateForward(window(1));
        assertThat(seleniumSteps.getCurrentUrl(window(1)), is(GITHUB.getUrl()));
        assertThat(second.getCurrentUrl(), is(GITHUB.getUrl()));

        seleniumSteps.navigateForward(window(1));
        assertThat(seleniumSteps.getCurrentUrl(window(1)), is(FACEBOOK.getUrl()));
        assertThat(second.getCurrentUrl(), is(FACEBOOK.getUrl()));

        seleniumSteps.navigateForward(window(1));
        assertThat(seleniumSteps.getCurrentUrl(window(1)), is(DEEZER.getUrl()));
        assertThat(second.getCurrentUrl(), is(DEEZER.getUrl()));

        seleniumSteps.navigateForward(window(1));
        assertThat(seleniumSteps.getCurrentUrl(window(1)), is(PAY_PAL.getUrl()));
        assertThat(second.getCurrentUrl(), is(PAY_PAL.getUrl()));

        seleniumSteps.navigateForward(window(1));
        assertThat(seleniumSteps.getCurrentUrl(window(1)), is(YOUTUBE.getUrl()));
        assertThat(second.getCurrentUrl(), is(YOUTUBE.getUrl()));
    }

    @Test(dependsOnMethods = "navigationInWindowBySearching")
    public void backForwardInTheWindowTest() {
        Window third = seleniumSteps.get(window(2));
        seleniumSteps.navigateTo(GOOGLE.getUrl(), third)
                .navigateTo(GITHUB.getUrl(), third)
                .navigateTo(FACEBOOK.getUrl(), third)
                .navigateTo(DEEZER.getUrl(), third)
                .navigateTo(PAY_PAL.getUrl(), third)
                .navigateTo(YOUTUBE.getUrl(), third);

        seleniumSteps.navigateBack(third);
        assertThat(seleniumSteps.getCurrentUrl(window(2)), is(PAY_PAL.getUrl()));
        assertThat(third.getCurrentUrl(), is(PAY_PAL.getUrl()));

        seleniumSteps.navigateBack(third);
        assertThat(seleniumSteps.getCurrentUrl(window(2)), is(DEEZER.getUrl()));
        assertThat(third.getCurrentUrl(), is(DEEZER.getUrl()));

        seleniumSteps.navigateBack(third);
        assertThat(seleniumSteps.getCurrentUrl(window(2)), is(FACEBOOK.getUrl()));
        assertThat(third.getCurrentUrl(), is(FACEBOOK.getUrl()));

        seleniumSteps.navigateBack(third);
        assertThat(seleniumSteps.getCurrentUrl(window(2)), is(GITHUB.getUrl()));
        assertThat(third.getCurrentUrl(), is(GITHUB.getUrl()));

        seleniumSteps.navigateBack(third);
        assertThat(seleniumSteps.getCurrentUrl(window(2)), is(GOOGLE.getUrl()));
        assertThat(third.getCurrentUrl(), is(GOOGLE.getUrl()));

        seleniumSteps.navigateBack(third);
        assertThat(seleniumSteps.getCurrentUrl(window(2)), is(BLANK.getUrl()));
        assertThat(third.getCurrentUrl(), is(BLANK.getUrl()));

        seleniumSteps.navigateForward(third);
        assertThat(seleniumSteps.getCurrentUrl(window(2)), is(GOOGLE.getUrl()));
        assertThat(third.getCurrentUrl(), is(GOOGLE.getUrl()));

        seleniumSteps.navigateForward(third);
        assertThat(seleniumSteps.getCurrentUrl(window(2)), is(GITHUB.getUrl()));
        assertThat(third.getCurrentUrl(), is(GITHUB.getUrl()));

        seleniumSteps.navigateForward(third);
        assertThat(seleniumSteps.getCurrentUrl(window(2)), is(FACEBOOK.getUrl()));
        assertThat(third.getCurrentUrl(), is(FACEBOOK.getUrl()));

        seleniumSteps.navigateForward(third);
        assertThat(seleniumSteps.getCurrentUrl(window(2)), is(DEEZER.getUrl()));
        assertThat(third.getCurrentUrl(), is(DEEZER.getUrl()));

        seleniumSteps.navigateForward(third);
        assertThat(seleniumSteps.getCurrentUrl(window(2)), is(PAY_PAL.getUrl()));
        assertThat(third.getCurrentUrl(), is(PAY_PAL.getUrl()));

        seleniumSteps.navigateForward(third);
        assertThat(seleniumSteps.getCurrentUrl(window(2)), is(YOUTUBE.getUrl()));
        assertThat(third.getCurrentUrl(), is(YOUTUBE.getUrl()));
    }

    @Test
    public void chainedBackForwardTest() {
        Window thirdWindow = seleniumSteps.get(window(2));
        seleniumSteps.navigateTo(GOOGLE.getUrl())
                .navigateTo(GITHUB.getUrl())
                .navigateTo(FACEBOOK.getUrl(), window(1))
                .navigateTo(DEEZER.getUrl(), window(1))
                .navigateTo(PAY_PAL.getUrl(), window(2))
                .navigateTo(YOUTUBE.getUrl(), window(2));

        seleniumSteps.navigateBack()
                .navigateBack(window(1))
                .navigateBack(thirdWindow);
        assertThat(seleniumSteps.getCurrentUrl(), is(GOOGLE.getUrl()));
        assertThat(seleniumSteps.getCurrentUrl(window(1)), is(FACEBOOK.getUrl()));
        assertThat(seleniumSteps.getCurrentUrl(window(2)), is(PAY_PAL.getUrl()));

        seleniumSteps.navigateForward()
                .navigateForward(window(1))
                .navigateForward(thirdWindow);
        assertThat(seleniumSteps.getCurrentUrl(), is(GITHUB.getUrl()));
        assertThat(seleniumSteps.getCurrentUrl(window(1)), is(DEEZER.getUrl()));
        assertThat(seleniumSteps.getCurrentUrl(window(2)), is(YOUTUBE.getUrl()));
    }

    @Test
    public void refreshFirstTest() {
        seleniumSteps.refresh();
        assertThat(((MockWindow) seleniumSteps.getWrappedDriver().manage().window()).isRefreshed(),
                is(true));
    }

    @Test
    public void refreshWindowBySearching() {
        seleniumSteps.refresh(window(1));
        seleniumSteps.getWrappedDriver().switchTo().window(HANDLE2.getHandle());

        assertThat(((MockWindow) seleniumSteps.getWrappedDriver().manage().window()).isRefreshed(),
                is(true));
    }

    @Test
    public void refreshTheWindow() {
        Window window = seleniumSteps.get(window(2));
        seleniumSteps.refresh(window);

        seleniumSteps.getWrappedDriver().switchTo().window(HANDLE3.getHandle());

        assertThat(((MockWindow) seleniumSteps.getWrappedDriver().manage().window()).isRefreshed(),
                is(true));
    }

    @Test
    public void chainedRefreshTest() {
        Window window = seleniumSteps.get(window(2));
        seleniumSteps.refresh(window(1)).refresh(window);

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
