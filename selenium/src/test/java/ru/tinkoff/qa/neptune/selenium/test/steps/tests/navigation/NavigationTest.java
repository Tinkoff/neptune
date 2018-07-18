package ru.tinkoff.qa.neptune.selenium.test.steps.tests.navigation;

import ru.tinkoff.qa.neptune.selenium.functions.target.locator.window.Window;
import ru.tinkoff.qa.neptune.selenium.test.BaseWebDriverTest;
import ru.tinkoff.qa.neptune.selenium.test.MockWindow;
import org.testng.annotations.Test;

import static ru.tinkoff.qa.neptune.selenium.functions.navigation.Back.back;
import static ru.tinkoff.qa.neptune.selenium.functions.navigation.Forward.forward;
import static ru.tinkoff.qa.neptune.selenium.functions.navigation.GetCurrentUrlSupplier.currentUrl;
import static ru.tinkoff.qa.neptune.selenium.functions.navigation.GetCurrentUrlSupplier.currentUrlIn;
import static ru.tinkoff.qa.neptune.selenium.functions.navigation.Refresh.refresh;
import static ru.tinkoff.qa.neptune.selenium.functions.navigation.ToUrl.toUrl;
import static ru.tinkoff.qa.neptune.selenium.functions.target.locator.window.GetWindowSupplier.window;
import static ru.tinkoff.qa.neptune.selenium.test.enums.URLs.*;
import static ru.tinkoff.qa.neptune.selenium.test.enums.WindowHandles.HANDLE1;
import static ru.tinkoff.qa.neptune.selenium.test.enums.WindowHandles.HANDLE2;
import static ru.tinkoff.qa.neptune.selenium.test.enums.WindowHandles.HANDLE3;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class NavigationTest extends BaseWebDriverTest {

    @Test
    public void navigationInTheFirstWindow() {
        seleniumSteps.navigate(toUrl(PAY_PAL.getUrl()));
        assertThat(seleniumSteps.get(currentUrl()), is(PAY_PAL.getUrl()));
    }

    @Test
    public void navigationInWindowBySearching() {
        seleniumSteps.navigate(toUrl(window().byIndex(1), DEEZER.getUrl()));
        assertThat(seleniumSteps.get(currentUrlIn(window().byIndex(1))), is(DEEZER.getUrl()));

        Window window = seleniumSteps.get(window().byIndex(1));
        assertThat(window.getCurrentUrl(), is(DEEZER.getUrl()));
    }

    @Test
    public void navigationInTheWindow() {
        Window window = seleniumSteps.get(window().byIndex(2));

        seleniumSteps.navigate(toUrl(window, YOUTUBE.getUrl()));
        assertThat(seleniumSteps.get(currentUrlIn(window().byIndex(2))), is(YOUTUBE.getUrl()));

        assertThat(window.getCurrentUrl(), is(YOUTUBE.getUrl()));
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
        assertThat(seleniumSteps.get(currentUrlIn(window().byIndex(1))), is(PAY_PAL.getUrl()));
        assertThat(second.getCurrentUrl(), is(PAY_PAL.getUrl()));

        seleniumSteps.navigate(back(window().byIndex(1)));
        assertThat(seleniumSteps.get(currentUrlIn(window().byIndex(1))), is(DEEZER.getUrl()));
        assertThat(second.getCurrentUrl(), is(DEEZER.getUrl()));

        seleniumSteps.navigate(back(window().byIndex(1)));
        assertThat(seleniumSteps.get(currentUrlIn(window().byIndex(1))), is(FACEBOOK.getUrl()));
        assertThat(second.getCurrentUrl(), is(FACEBOOK.getUrl()));

        seleniumSteps.navigate(back(window().byIndex(1)));
        assertThat(seleniumSteps.get(currentUrlIn(window().byIndex(1))), is(GITHUB.getUrl()));
        assertThat(second.getCurrentUrl(), is(GITHUB.getUrl()));

        seleniumSteps.navigate(back(window().byIndex(1)));
        assertThat(seleniumSteps.get(currentUrlIn(window().byIndex(1))), is(GOOGLE.getUrl()));
        assertThat(second.getCurrentUrl(), is(GOOGLE.getUrl()));

        seleniumSteps.navigate(back(window().byIndex(1)));
        assertThat(seleniumSteps.get(currentUrlIn(window().byIndex(1))), is(BLANK.getUrl()));
        assertThat(second.getCurrentUrl(), is(BLANK.getUrl()));

        seleniumSteps.navigate(forward(window().byIndex(1)));
        assertThat(seleniumSteps.get(currentUrlIn(window().byIndex(1))), is(GOOGLE.getUrl()));
        assertThat(second.getCurrentUrl(), is(GOOGLE.getUrl()));

        seleniumSteps.navigate(forward(window().byIndex(1)));
        assertThat(seleniumSteps.get(currentUrlIn(window().byIndex(1))), is(GITHUB.getUrl()));
        assertThat(second.getCurrentUrl(), is(GITHUB.getUrl()));

        seleniumSteps.navigate(forward(window().byIndex(1)));
        assertThat(seleniumSteps.get(currentUrlIn(window().byIndex(1))), is(FACEBOOK.getUrl()));
        assertThat(second.getCurrentUrl(), is(FACEBOOK.getUrl()));

        seleniumSteps.navigate(forward(window().byIndex(1)));
        assertThat(seleniumSteps.get(currentUrlIn(window().byIndex(1))), is(DEEZER.getUrl()));
        assertThat(second.getCurrentUrl(), is(DEEZER.getUrl()));

        seleniumSteps.navigate(forward(window().byIndex(1)));
        assertThat(seleniumSteps.get(currentUrlIn(window().byIndex(1))), is(PAY_PAL.getUrl()));
        assertThat(second.getCurrentUrl(), is(PAY_PAL.getUrl()));

        seleniumSteps.navigate(forward(window().byIndex(1)));
        assertThat(seleniumSteps.get(currentUrlIn(window().byIndex(1))), is(YOUTUBE.getUrl()));
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
        assertThat(seleniumSteps.get(currentUrlIn(window().byIndex(2))), is(PAY_PAL.getUrl()));
        assertThat(third.getCurrentUrl(), is(PAY_PAL.getUrl()));

        seleniumSteps.navigate(back(third));
        assertThat(seleniumSteps.get(currentUrlIn(window().byIndex(2))), is(DEEZER.getUrl()));
        assertThat(third.getCurrentUrl(), is(DEEZER.getUrl()));

        seleniumSteps.navigate(back(third));
        assertThat(seleniumSteps.get(currentUrlIn(window().byIndex(2))), is(FACEBOOK.getUrl()));
        assertThat(third.getCurrentUrl(), is(FACEBOOK.getUrl()));

        seleniumSteps.navigate(back(third));
        assertThat(seleniumSteps.get(currentUrlIn(window().byIndex(2))), is(GITHUB.getUrl()));
        assertThat(third.getCurrentUrl(), is(GITHUB.getUrl()));

        seleniumSteps.navigate(back(third));
        assertThat(seleniumSteps.get(currentUrlIn(window().byIndex(2))), is(GOOGLE.getUrl()));
        assertThat(third.getCurrentUrl(), is(GOOGLE.getUrl()));

        seleniumSteps.navigate(back(third));
        assertThat(seleniumSteps.get(currentUrlIn(window().byIndex(2))), is(BLANK.getUrl()));
        assertThat(third.getCurrentUrl(), is(BLANK.getUrl()));

        seleniumSteps.navigate(forward(third));
        assertThat(seleniumSteps.get(currentUrlIn(window().byIndex(2))), is(GOOGLE.getUrl()));
        assertThat(third.getCurrentUrl(), is(GOOGLE.getUrl()));

        seleniumSteps.navigate(forward(third));
        assertThat(seleniumSteps.get(currentUrlIn(window().byIndex(2))), is(GITHUB.getUrl()));
        assertThat(third.getCurrentUrl(), is(GITHUB.getUrl()));

        seleniumSteps.navigate(forward(third));
        assertThat(seleniumSteps.get(currentUrlIn(window().byIndex(2))), is(FACEBOOK.getUrl()));
        assertThat(third.getCurrentUrl(), is(FACEBOOK.getUrl()));

        seleniumSteps.navigate(forward(third));
        assertThat(seleniumSteps.get(currentUrlIn(window().byIndex(2))), is(DEEZER.getUrl()));
        assertThat(third.getCurrentUrl(), is(DEEZER.getUrl()));

        seleniumSteps.navigate(forward(third));
        assertThat(seleniumSteps.get(currentUrlIn(window().byIndex(2))), is(PAY_PAL.getUrl()));
        assertThat(third.getCurrentUrl(), is(PAY_PAL.getUrl()));

        seleniumSteps.navigate(forward(third));
        assertThat(seleniumSteps.get(currentUrlIn(window().byIndex(2))), is(YOUTUBE.getUrl()));
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
        assertThat(seleniumSteps.get(currentUrlIn(window().byIndex(1))), is(FACEBOOK.getUrl()));
        assertThat(seleniumSteps.get(currentUrlIn(window().byIndex(2))), is(PAY_PAL.getUrl()));

        seleniumSteps.navigate(forward().andThenForward(window().byIndex(1)).andThenForward(thirdWindow));
        assertThat(seleniumSteps.get(currentUrl()), is(GITHUB.getUrl()));
        assertThat(seleniumSteps.get(currentUrlIn(window().byIndex(1))), is(DEEZER.getUrl()));
        assertThat(seleniumSteps.get(currentUrlIn(window().byIndex(2))), is(YOUTUBE.getUrl()));
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
