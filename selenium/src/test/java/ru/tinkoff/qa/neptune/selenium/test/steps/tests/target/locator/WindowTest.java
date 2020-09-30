package ru.tinkoff.qa.neptune.selenium.test.steps.tests.target.locator;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.selenium.functions.target.locator.window.Window;
import ru.tinkoff.qa.neptune.selenium.test.BaseWebDriverTest;

import static java.time.Duration.ofSeconds;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.testng.Assert.fail;
import static ru.tinkoff.qa.neptune.selenium.functions.target.locator.window.GetWindowSupplier.window;
import static ru.tinkoff.qa.neptune.selenium.functions.target.locator.window.WindowCriteria.titleMatches;
import static ru.tinkoff.qa.neptune.selenium.functions.target.locator.window.WindowCriteria.urlMatches;
import static ru.tinkoff.qa.neptune.selenium.properties.WaitingProperties.TimeUnitProperties.WAITING_WINDOW_TIME_UNIT;
import static ru.tinkoff.qa.neptune.selenium.properties.WaitingProperties.TimeValueProperties.WAITING_WINDOW_TIME_VALUE;
import static ru.tinkoff.qa.neptune.selenium.test.enums.InitialPositions.*;
import static ru.tinkoff.qa.neptune.selenium.test.enums.InitialSizes.*;
import static ru.tinkoff.qa.neptune.selenium.test.enums.URLs.*;
import static ru.tinkoff.qa.neptune.selenium.test.enums.WindowHandles.*;

public class WindowTest extends BaseWebDriverTest {

    @BeforeMethod
    public void prepare() {
        WebDriver driver = seleniumSteps.getWrappedDriver();
        driver.switchTo().window(HANDLE1.getHandle()).get(GOOGLE.getUrl());
        driver.switchTo().window(HANDLE2.getHandle()).get(FACEBOOK.getUrl());
        driver.switchTo().window(HANDLE3.getHandle()).get(GITHUB.getUrl());
    }

    @Test
    public void getTheFirstWindowTest() {
        Window firstWindow = seleniumSteps.get(window());
        assertThat(firstWindow.getCurrentUrl(), is(GOOGLE.getUrl()));
        assertThat(firstWindow.getTitle(), is(GOOGLE.getTitle()));
        assertThat(firstWindow.getPosition(), is(POSITION_1.getPosition()));
        assertThat(firstWindow.getSize(), is(SIZE1.getSize()));
        assertThat(firstWindow.isPresent(), is(true));
        assertThat(firstWindow.toString(), is("Window[url https://www.google.com/ title Google]"));
    }

    @Test
    public void searchingWindowByIndexTest() {
        Window foundWindow = seleniumSteps.get(window(1));
        assertThat(foundWindow.getCurrentUrl(), is(FACEBOOK.getUrl()));
        assertThat(foundWindow.getTitle(), is(FACEBOOK.getTitle()));
        assertThat(foundWindow.getPosition(), is(POSITION_2.getPosition()));
        assertThat(foundWindow.getSize(), is(SIZE2.getSize()));
        assertThat(foundWindow.isPresent(), is(true));
        assertThat(foundWindow.toString(), is("Window[url https://www.facebook.com/ title Facebook]"));
    }

    @Test
    public void searchingWindowByCriteriaTest() {
        Window foundWindow = seleniumSteps.get(window()
                .criteria(titleMatches("^.*\\b(Github)\\b.*$"))
                .criteria(urlMatches("^.*\\b(github)\\b.*$")));
        assertThat(foundWindow.getCurrentUrl(), is(GITHUB.getUrl()));
        assertThat(foundWindow.getTitle(), is(GITHUB.getTitle()));
        assertThat(foundWindow.getPosition(), is(POSITION_3.getPosition()));
        assertThat(foundWindow.getSize(), is(SIZE3.getSize()));
        assertThat(foundWindow.isPresent(), is(true));
        assertThat(foundWindow.toString(), is("Window[url https://github.com/ title Github Inc]"));
    }

    @Test
    public void searchingWindowByIndexAndCriteriaTest() {
        Window foundWindow = seleniumSteps.get(window(2)
                .criteria(titleMatches("^.*\\b(Github)\\b.*$"))
                .criteria(urlMatches("^.*\\b(github)\\b.*$")));
        assertThat(foundWindow.getCurrentUrl(), is(GITHUB.getUrl()));
        assertThat(foundWindow.getTitle(), is(GITHUB.getTitle()));
        assertThat(foundWindow.getPosition(), is(POSITION_3.getPosition()));
        assertThat(foundWindow.getSize(), is(SIZE3.getSize()));
        assertThat(foundWindow.isPresent(), is(true));
        assertThat(foundWindow.toString(), is("Window[url https://github.com/ title Github Inc]"));
    }

    @Test(expectedExceptions = NoSuchWindowException.class)
    public void negativeTestWithTimeDefining() {
        try {
            setStartBenchMark();
            seleniumSteps.get(window(1)
                    .timeOut(FIVE_SECONDS)
                    .criteria(titleMatches("^.*\\b(Github)\\b.*$"))
                    .criteria(urlMatches("^.*\\b(github)\\b.*$")));
        } finally {
            setEndBenchMark();
            assertThat(getTimeDifference(), greaterThanOrEqualTo(FIVE_SECONDS.toMillis()));
            assertThat(getTimeDifference() - FIVE_SECONDS.toMillis(), lessThan(HALF_SECOND.toMillis()));
        }
        fail("Exception was expected");
    }

    @Test(expectedExceptions = NoSuchWindowException.class)
    public void negativeTestWithTimeDefinedImplicitly() {
        try {
            setProperty(WAITING_WINDOW_TIME_UNIT.getName(), "SECONDS");
            setProperty(WAITING_WINDOW_TIME_VALUE.getName(), "5");
            setStartBenchMark();
            seleniumSteps.get(window(1)
                    .criteria(titleMatches("^.*\\b(Github)\\b.*$"))
                    .criteria(urlMatches("^.*\\b(github)\\b.*$")));
        } finally {
            setEndBenchMark();
            removeProperty(WAITING_WINDOW_TIME_UNIT.getName());
            removeProperty(WAITING_WINDOW_TIME_VALUE.getName());
            assertThat(getTimeDifference(), greaterThanOrEqualTo(FIVE_SECONDS.toMillis()));
            assertThat(getTimeDifference() - FIVE_SECONDS.toMillis(), lessThan(HALF_SECOND.toMillis()));
        }
        fail("Exception was expected");
    }


    @Test(expectedExceptions = NoSuchWindowException.class)
    public void closeWindowBySearchCriteriaTest() {
        seleniumSteps.closeWindow(window(2)
                .criteria(titleMatches("^.*\\b(Github)\\b.*$"))
                .criteria(urlMatches("^.*\\b(github)\\b.*$")));
        assertThat(seleniumSteps.get(window(0)).isPresent(), is(true));
        assertThat(seleniumSteps.get(window(1)).isPresent(), is(true));
        seleniumSteps.get(window(2)
                .timeOut(ofSeconds(1)));
        fail("Failure was expected");
    }

    @Test(expectedExceptions = NoSuchWindowException.class)
    public void closeFoundWindowTest() {
        Window foundWindow = seleniumSteps.get(window(2)
                .criteria(titleMatches("^.*\\b(Github)\\b.*$"))
                .criteria(urlMatches("^.*\\b(github)\\b.*$")));
        seleniumSteps.closeWindow(foundWindow);

        assertThat(foundWindow.isPresent(), is(false));
        assertThat(seleniumSteps.get(window(0)).isPresent(), is(true));
        assertThat(seleniumSteps.get(window(1)).isPresent(), is(true));
        seleniumSteps.get(window(2).timeOut(ofSeconds(1)));
        fail("Failure was expected");
    }

    @Test
    public void switchToWindowBySearchCriteriaTest() {
        seleniumSteps.switchTo(window(2)
                .criteria(titleMatches("^.*\\b(Github)\\b.*$"))
                .criteria(urlMatches("^.*\\b(github)\\b.*$")));
        assertThat(seleniumSteps.getWrappedDriver().getWindowHandle(), is(HANDLE3.getHandle()));
        assertThat(seleniumSteps.getWrappedDriver().getCurrentUrl(), is(GITHUB.getUrl()));
        assertThat(seleniumSteps.getWrappedDriver().getTitle(), is(GITHUB.getTitle()));

        seleniumSteps.switchTo(window(0));
        assertThat(seleniumSteps.getWrappedDriver().getWindowHandle(), is(HANDLE1.getHandle()));
        assertThat(seleniumSteps.getWrappedDriver().getCurrentUrl(), is(GOOGLE.getUrl()));
        assertThat(seleniumSteps.getWrappedDriver().getTitle(), is(GOOGLE.getTitle()));

        seleniumSteps.switchTo(window(1));
        assertThat(seleniumSteps.getWrappedDriver().getWindowHandle(), is(HANDLE2.getHandle()));
        assertThat(seleniumSteps.getWrappedDriver().getCurrentUrl(), is(FACEBOOK.getUrl()));
        assertThat(seleniumSteps.getWrappedDriver().getTitle(), is(FACEBOOK.getTitle()));
    }

    @Test(dependsOnMethods = "switchToWindowBySearchCriteriaTest")
    public void switchToWindowBySearchCriteriaChainedTest() {
        seleniumSteps.switchTo(window(2)
                .criteria(titleMatches("^.*\\b(Github)\\b.*$"))
                .criteria(urlMatches("^.*\\b(github)\\b.*$")))
                .switchTo(window(1))
                .switchTo(window(0));

        assertThat(seleniumSteps.getWrappedDriver().getWindowHandle(), is(HANDLE1.getHandle()));
        assertThat(seleniumSteps.getWrappedDriver().getCurrentUrl(), is(GOOGLE.getUrl()));
        assertThat(seleniumSteps.getWrappedDriver().getTitle(), is(GOOGLE.getTitle()));
    }

    @Test
    public void switchToWindowTest() {

        Window window = seleniumSteps.get(window(2)
                .criteria(titleMatches("^.*\\b(Github)\\b.*$"))
                .criteria(urlMatches("^.*\\b(github)\\b.*$")));
        Window window2 = seleniumSteps.get(window(0));
        Window window3 = seleniumSteps.get(window(1));

        seleniumSteps.switchTo(window);
        assertThat(seleniumSteps.getWrappedDriver().getWindowHandle(), is(HANDLE3.getHandle()));
        assertThat(seleniumSteps.getWrappedDriver().getCurrentUrl(), is(GITHUB.getUrl()));
        assertThat(seleniumSteps.getWrappedDriver().getTitle(), is(GITHUB.getTitle()));

        seleniumSteps.switchTo(window2);
        assertThat(seleniumSteps.getWrappedDriver().getWindowHandle(), is(HANDLE1.getHandle()));
        assertThat(seleniumSteps.getWrappedDriver().getCurrentUrl(), is(GOOGLE.getUrl()));
        assertThat(seleniumSteps.getWrappedDriver().getTitle(), is(GOOGLE.getTitle()));

        seleniumSteps.switchTo(window3);
        assertThat(seleniumSteps.getWrappedDriver().getWindowHandle(), is(HANDLE2.getHandle()));
        assertThat(seleniumSteps.getWrappedDriver().getCurrentUrl(), is(FACEBOOK.getUrl()));
        assertThat(seleniumSteps.getWrappedDriver().getTitle(), is(FACEBOOK.getTitle()));
    }

    @Test(dependsOnMethods = "switchToWindowTest")
    public void switchToChainedTest() {
        Window window = seleniumSteps.get(window(2)
                .criteria(titleMatches("^.*\\b(Github)\\b.*$"))
                .criteria(urlMatches("^.*\\b(github)\\b.*$")));
        Window window2 = seleniumSteps.get(window(0));
        Window window3 = seleniumSteps.get(window(1));

        seleniumSteps.switchTo(window)
                .switchTo(window3)
                .switchTo(window2);

        assertThat(seleniumSteps.getWrappedDriver().getWindowHandle(), is(HANDLE1.getHandle()));
        assertThat(seleniumSteps.getWrappedDriver().getCurrentUrl(), is(GOOGLE.getUrl()));
        assertThat(seleniumSteps.getWrappedDriver().getTitle(), is(GOOGLE.getTitle()));
    }

    @Test
    public void sizeOfWindowBySearchingTest() {
        seleniumSteps.changeWindowSize(window(1), 1001, 1002);
        assertThat(seleniumSteps.sizeOf(window(1)), equalTo(new Dimension(1001, 1002)));
    }

    @Test
    public void sizeOfWindowTest() {
        Window window = seleniumSteps.get(window(2));
        seleniumSteps.changeWindowSize(window, 1001, 1002);
        assertThat(window.getSize(), equalTo(new Dimension(1001, 1002)));
        assertThat(seleniumSteps.sizeOf(window(2)), equalTo(new Dimension(1001, 1002)));
    }

    @Test
    public void sizeOfTheFirstWindowTest() {
        Window window = seleniumSteps.get(window());
        seleniumSteps.changeWindowSize(window(), 1001, 1002);
        assertThat(window.getSize(), equalTo(new Dimension(1001, 1002)));
        assertThat(seleniumSteps.sizeOf(window()), equalTo(new Dimension(1001, 1002)));
    }

    @Test
    public void positionOfWindowBySearchingTest() {
        seleniumSteps.changeWindowPosition(window(1), 1001, 1002);
        assertThat(seleniumSteps.positionOf(window(1)), equalTo(new Point(1001, 1002)));
    }

    @Test
    public void positionOfWindowTest() {
        Window window = seleniumSteps.get(window(2));
        seleniumSteps.changeWindowPosition(window, 1001, 1002);
        assertThat(window.getPosition(), equalTo(new Point(1001, 1002)));
        assertThat(seleniumSteps.positionOf(window(2)), equalTo(new Point(1001, 1002)));
    }

    @Test
    public void positionOfTheFirstWindowTest() {
        Window window = seleniumSteps.get(window());
        seleniumSteps.changeWindowPosition(window(), 1001, 1002);
        assertThat(window.getPosition(), equalTo(new Point(1001, 1002)));
        assertThat(seleniumSteps.windowPosition(), equalTo(new Point(1001, 1002)));
    }

    @Test
    public void getWindowTitleBySearchingTest() {
        assertThat(seleniumSteps.windowTitle(window()), is(GOOGLE.getTitle()));
        assertThat(seleniumSteps.windowTitle(window(1)), is(FACEBOOK.getTitle()));
        assertThat(seleniumSteps.windowTitle(window(2)), is(GITHUB.getTitle()));
    }

    @Test
    public void getWindowTitleTest() {
        Window window = seleniumSteps.get(window());
        Window window2 = seleniumSteps.get(window(1));
        Window window3 = seleniumSteps.get(window(2));

        assertThat(seleniumSteps.windowTitle(window), is(GOOGLE.getTitle()));
        assertThat(seleniumSteps.windowTitle(window2), is(FACEBOOK.getTitle()));
        assertThat(seleniumSteps.windowTitle(window3), is(GITHUB.getTitle()));
    }
}
