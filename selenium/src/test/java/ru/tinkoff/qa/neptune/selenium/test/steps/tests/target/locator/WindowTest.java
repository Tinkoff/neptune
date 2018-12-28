package ru.tinkoff.qa.neptune.selenium.test.steps.tests.target.locator;

import ru.tinkoff.qa.neptune.selenium.functions.target.locator.window.Window;
import ru.tinkoff.qa.neptune.selenium.test.BaseWebDriverTest;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static ru.tinkoff.qa.neptune.selenium.functions.target.locator.SwitchActionSupplier.to;
import static ru.tinkoff.qa.neptune.selenium.functions.target.locator.window.CloseWindowActionSupplier.closeWindow;
import static ru.tinkoff.qa.neptune.selenium.functions.target.locator.window.GetWindowPositionSupplier.positionOf;
import static ru.tinkoff.qa.neptune.selenium.functions.target.locator.window.GetWindowPositionSupplier.windowPosition;
import static ru.tinkoff.qa.neptune.selenium.functions.target.locator.window.GetWindowSizeSupplier.sizeOf;
import static ru.tinkoff.qa.neptune.selenium.functions.target.locator.window.GetWindowSupplier.window;
import static ru.tinkoff.qa.neptune.selenium.functions.target.locator.window.GetWindowTitleSupplier.titleOf;
import static ru.tinkoff.qa.neptune.selenium.functions.target.locator.window.SetWindowPositionSupplier.setPositionOf;
import static ru.tinkoff.qa.neptune.selenium.functions.target.locator.window.SetWindowSizeSupplier.setSizeOf;
import static ru.tinkoff.qa.neptune.selenium.functions.target.locator.window.WindowPredicates.hasTitle;
import static ru.tinkoff.qa.neptune.selenium.functions.target.locator.window.WindowPredicates.hasUrl;
import static ru.tinkoff.qa.neptune.selenium.properties.WaitingProperties.TimeUnitProperties.WAITING_WINDOW_TIME_UNIT;
import static ru.tinkoff.qa.neptune.selenium.properties.WaitingProperties.TimeValueProperties.WAITING_WINDOW_TIME_VALUE;
import static ru.tinkoff.qa.neptune.selenium.test.enums.InitialPositions.POSITION_1;
import static ru.tinkoff.qa.neptune.selenium.test.enums.InitialPositions.POSITION_2;
import static ru.tinkoff.qa.neptune.selenium.test.enums.InitialPositions.POSITION_3;
import static ru.tinkoff.qa.neptune.selenium.test.enums.InitialSizes.SIZE1;
import static ru.tinkoff.qa.neptune.selenium.test.enums.InitialSizes.SIZE2;
import static ru.tinkoff.qa.neptune.selenium.test.enums.InitialSizes.SIZE3;
import static ru.tinkoff.qa.neptune.selenium.test.enums.URLs.FACEBOOK;
import static ru.tinkoff.qa.neptune.selenium.test.enums.URLs.GITHUB;
import static ru.tinkoff.qa.neptune.selenium.test.enums.URLs.GOOGLE;
import static ru.tinkoff.qa.neptune.selenium.test.enums.WindowHandles.HANDLE1;
import static ru.tinkoff.qa.neptune.selenium.test.enums.WindowHandles.HANDLE2;
import static ru.tinkoff.qa.neptune.selenium.test.enums.WindowHandles.HANDLE3;
import static java.time.Duration.ofSeconds;
import static java.util.regex.Pattern.compile;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.testng.Assert.fail;

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
        assertThat(firstWindow.toString(), is("The first window"));
    }

    @Test
    public void searchingWindowByIndexTest() {
        Window foundWindow = seleniumSteps.get(window().byIndex(1));
        assertThat(foundWindow.getCurrentUrl(), is(FACEBOOK.getUrl()));
        assertThat(foundWindow.getTitle(), is(FACEBOOK.getTitle()));
        assertThat(foundWindow.getPosition(), is(POSITION_2.getPosition()));
        assertThat(foundWindow.getSize(), is(SIZE2.getSize()));
        assertThat(foundWindow.isPresent(), is(true));
        assertThat(foundWindow.toString(), is("Window/tab found by index 1"));
    }

    @Test
    public void searchingWindowByCriteriaTest() {
        Window foundWindow = seleniumSteps.get(window()
                .onCondition(hasTitle(compile("^.*\\b(Github)\\b.*$")).and(hasUrl(compile("^.*\\b(github)\\b.*$")))));
        assertThat(foundWindow.getCurrentUrl(), is(GITHUB.getUrl()));
        assertThat(foundWindow.getTitle(), is(GITHUB.getTitle()));
        assertThat(foundWindow.getPosition(), is(POSITION_3.getPosition()));
        assertThat(foundWindow.getSize(), is(SIZE3.getSize()));
        assertThat(foundWindow.isPresent(), is(true));
        assertThat(foundWindow.toString(), is("Window/tab found by conditions: title meets regExp pattern " +
                "'^.*\\b(Github)\\b.*$', url meets regExp pattern '^.*\\b(github)\\b.*$'"));
    }

    @Test
    public void searchingWindowByIndexAndCriteriaTest() {
        Window foundWindow = seleniumSteps.get(window().byIndex(2)
                .onCondition(hasTitle(compile("^.*\\b(Github)\\b.*$")).and(hasUrl(compile("^.*\\b(github)\\b.*$")))));
        assertThat(foundWindow.getCurrentUrl(), is(GITHUB.getUrl()));
        assertThat(foundWindow.getTitle(), is(GITHUB.getTitle()));
        assertThat(foundWindow.getPosition(), is(POSITION_3.getPosition()));
        assertThat(foundWindow.getSize(), is(SIZE3.getSize()));
        assertThat(foundWindow.isPresent(), is(true));
        assertThat(foundWindow.toString(), is("Window/tab found by index 2 and by conditions: title meets regExp " +
                "pattern '^.*\\b(Github)\\b.*$', url meets regExp pattern '^.*\\b(github)\\b.*$'"));
    }

    @Test(expectedExceptions = NoSuchWindowException.class)
    public void negativeTestWithTimeDefining() {
        try {
            setStartBenchMark();
            seleniumSteps.get(window().byIndex(1).withTimeToGetWindow(FIVE_SECONDS)
                    .onCondition(hasTitle(compile("^.*\\b(Github)\\b.*$")).and(hasUrl(compile("^.*\\b(github)\\b.*$")))));
        }
        catch (Exception e) {
            assertThat(e.getMessage(), containsString("Window/tab was not found by index 1 and by conditions " +
                    "title meets regExp pattern '^.*\\b(Github)\\b.*$', url meets regExp pattern '^.*\\b(github)\\b.*$'"));
            throw e;
        }
        finally {
            setEndBenchMark();
            assertThat(getTimeDifference(), greaterThanOrEqualTo(FIVE_SECONDS.toMillis()));
            assertThat(getTimeDifference() - FIVE_SECONDS.toMillis(), lessThan(HALF_SECOND.toMillis()));
        }
        fail("Exception was expected");
    }

    @Test(expectedExceptions = NoSuchWindowException.class)
    public void negativeTestWithTimeDefinedImplicitly() {
        try {
            setProperty(WAITING_WINDOW_TIME_UNIT.getPropertyName(), "SECONDS");
            setProperty(WAITING_WINDOW_TIME_VALUE.getPropertyName(), "5");
            setStartBenchMark();
            seleniumSteps.get(window().byIndex(1)
                    .onCondition(hasTitle(compile("^.*\\b(Github)\\b.*$")).and(hasUrl(compile("^.*\\b(github)\\b.*$")))));
        }
        catch (Exception e) {
            assertThat(e.getMessage(), containsString("Window/tab was not found by index 1 and by conditions " +
                    "title meets regExp pattern '^.*\\b(Github)\\b.*$', url meets regExp pattern '^.*\\b(github)\\b.*$'"));
            throw e;
        }
        finally {
            setEndBenchMark();
            removeProperty(WAITING_WINDOW_TIME_UNIT.getPropertyName());
            removeProperty(WAITING_WINDOW_TIME_VALUE.getPropertyName());
            assertThat(getTimeDifference(), greaterThanOrEqualTo(FIVE_SECONDS.toMillis()));
            assertThat(getTimeDifference() - FIVE_SECONDS.toMillis(), lessThan(HALF_SECOND.toMillis()));
        }
        fail("Exception was expected");
    }


    @Test(expectedExceptions = NoSuchWindowException.class)
    public void closeWindowBySearchCriteriaTest() {
        seleniumSteps.perform(closeWindow(window().byIndex(2)
                .onCondition(hasTitle(compile("^.*\\b(Github)\\b.*$")).and(hasUrl(compile("^.*\\b(github)\\b.*$"))))));
        assertThat(seleniumSteps.get(window().byIndex(0)).isPresent(), is(true));
        assertThat(seleniumSteps.get(window().byIndex(1)).isPresent(), is(true));
        seleniumSteps.get(window().byIndex(2).withTimeToGetWindow(ofSeconds(1)));
        fail("Failure was expected");
    }

    @Test(expectedExceptions = NoSuchWindowException.class)
    public void closeFoundWindowTest() {
        Window foundWindow = seleniumSteps.get(window().byIndex(2)
                .onCondition(hasTitle(compile("^.*\\b(Github)\\b.*$")).and(hasUrl(compile("^.*\\b(github)\\b.*$")))));
        seleniumSteps.perform(closeWindow(foundWindow));

        assertThat(foundWindow.isPresent(), is(false));
        assertThat(seleniumSteps.get(window().byIndex(0)).isPresent(), is(true));
        assertThat(seleniumSteps.get(window().byIndex(1)).isPresent(), is(true));
        seleniumSteps.get(window().byIndex(2).withTimeToGetWindow(ofSeconds(1)));
        fail("Failure was expected");
    }

    @Test
    public void switchToWindowBySearchCriteriaTest() {
        seleniumSteps.performSwitch(to(window().byIndex(2)
                .onCondition(hasTitle(compile("^.*\\b(Github)\\b.*$")).and(hasUrl(compile("^.*\\b(github)\\b.*$"))))));
        assertThat(seleniumSteps.getWrappedDriver().getWindowHandle(), is(HANDLE3.getHandle()));
        assertThat(seleniumSteps.getWrappedDriver().getCurrentUrl(), is(GITHUB.getUrl()));
        assertThat(seleniumSteps.getWrappedDriver().getTitle(), is(GITHUB.getTitle()));

        seleniumSteps.performSwitch(to(window().byIndex(0)));
        assertThat(seleniumSteps.getWrappedDriver().getWindowHandle(), is(HANDLE1.getHandle()));
        assertThat(seleniumSteps.getWrappedDriver().getCurrentUrl(), is(GOOGLE.getUrl()));
        assertThat(seleniumSteps.getWrappedDriver().getTitle(), is(GOOGLE.getTitle()));

        seleniumSteps.performSwitch(to(window().byIndex(1)));
        assertThat(seleniumSteps.getWrappedDriver().getWindowHandle(), is(HANDLE2.getHandle()));
        assertThat(seleniumSteps.getWrappedDriver().getCurrentUrl(), is(FACEBOOK.getUrl()));
        assertThat(seleniumSteps.getWrappedDriver().getTitle(), is(FACEBOOK.getTitle()));
    }

    @Test(dependsOnMethods = "switchToWindowBySearchCriteriaTest")
    public void switchToWindowBySearchCriteriaChainedTest() {
        seleniumSteps.performSwitch(to(window().byIndex(2)
                .onCondition(hasTitle(compile("^.*\\b(Github)\\b.*$")).and(hasUrl(compile("^.*\\b(github)\\b.*$"))))))
                .performSwitch(to(window().byIndex(1)))
                .performSwitch(to(window().byIndex(0)));

        assertThat(seleniumSteps.getWrappedDriver().getWindowHandle(), is(HANDLE1.getHandle()));
        assertThat(seleniumSteps.getWrappedDriver().getCurrentUrl(), is(GOOGLE.getUrl()));
        assertThat(seleniumSteps.getWrappedDriver().getTitle(), is(GOOGLE.getTitle()));
    }

    @Test
    public void switchToWindowTest() {

        Window window = seleniumSteps.get(window().byIndex(2)
                .onCondition(hasTitle(compile("^.*\\b(Github)\\b.*$")).and(hasUrl(compile("^.*\\b(github)\\b.*$")))));
        Window window2 = seleniumSteps.get(window().byIndex(0));
        Window window3 = seleniumSteps.get(window().byIndex(1));

        seleniumSteps.performSwitch(to(window));
        assertThat(seleniumSteps.getWrappedDriver().getWindowHandle(), is(HANDLE3.getHandle()));
        assertThat(seleniumSteps.getWrappedDriver().getCurrentUrl(), is(GITHUB.getUrl()));
        assertThat(seleniumSteps.getWrappedDriver().getTitle(), is(GITHUB.getTitle()));

        seleniumSteps.performSwitch(to(window2));
        assertThat(seleniumSteps.getWrappedDriver().getWindowHandle(), is(HANDLE1.getHandle()));
        assertThat(seleniumSteps.getWrappedDriver().getCurrentUrl(), is(GOOGLE.getUrl()));
        assertThat(seleniumSteps.getWrappedDriver().getTitle(), is(GOOGLE.getTitle()));

        seleniumSteps.performSwitch(to(window3));
        assertThat(seleniumSteps.getWrappedDriver().getWindowHandle(), is(HANDLE2.getHandle()));
        assertThat(seleniumSteps.getWrappedDriver().getCurrentUrl(), is(FACEBOOK.getUrl()));
        assertThat(seleniumSteps.getWrappedDriver().getTitle(), is(FACEBOOK.getTitle()));
    }

    @Test(dependsOnMethods = "switchToWindowTest")
    public void switchToChainedTest() {
        Window window = seleniumSteps.get(window().byIndex(2)
                .onCondition(hasTitle(compile("^.*\\b(Github)\\b.*$")).and(hasUrl(compile("^.*\\b(github)\\b.*$")))));
        Window window2 = seleniumSteps.get(window().byIndex(0));
        Window window3 = seleniumSteps.get(window().byIndex(1));

        seleniumSteps.performSwitch(to(window))
                .performSwitch(to(window3))
                .performSwitch(to(window2));

        assertThat(seleniumSteps.getWrappedDriver().getWindowHandle(), is(HANDLE1.getHandle()));
        assertThat(seleniumSteps.getWrappedDriver().getCurrentUrl(), is(GOOGLE.getUrl()));
        assertThat(seleniumSteps.getWrappedDriver().getTitle(), is(GOOGLE.getTitle()));
    }

    @Test
    public void sizeOfWindowBySearchingTest() {
        seleniumSteps.perform(setSizeOf(window().byIndex(1), new Dimension(1001, 1002)));
        assertThat(seleniumSteps.get(sizeOf(window().byIndex(1))), equalTo(new Dimension(1001, 1002)));
    }

    @Test
    public void sizeOfWindowTest() {
        Window window = seleniumSteps.get(window().byIndex(2));
        seleniumSteps.perform(setSizeOf(window, new Dimension(1001, 1002)));
        assertThat(window.getSize(), equalTo(new Dimension(1001, 1002)));
        assertThat(seleniumSteps.get(sizeOf(window().byIndex(2))), equalTo(new Dimension(1001, 1002)));
    }

    @Test
    public void sizeOfTheFirstWindowTest() {
        Window window = seleniumSteps.get(window());
        seleniumSteps.perform(setSizeOf(window(), new Dimension(1001, 1002)));
        assertThat(window.getSize(), equalTo(new Dimension(1001, 1002)));
        assertThat(seleniumSteps.get(sizeOf(window())), equalTo(new Dimension(1001, 1002)));
    }

    @Test
    public void positionOfWindowBySearchingTest() {
        seleniumSteps.perform(setPositionOf(window().byIndex(1), new Point(1001, 1002)));
        assertThat(seleniumSteps.get(positionOf(window().byIndex(1))), equalTo(new Point(1001, 1002)));
    }

    @Test
    public void positionOfWindowTest() {
        Window window = seleniumSteps.get(window().byIndex(2));
        seleniumSteps.perform(setPositionOf(window, new Point(1001, 1002)));
        assertThat(window.getPosition(), equalTo(new Point(1001, 1002)));
        assertThat(seleniumSteps.get(positionOf(window().byIndex(2))), equalTo(new Point(1001, 1002)));
    }

    @Test
    public void positionOfTheFirstWindowTest() {
        Window window = seleniumSteps.get(window());
        seleniumSteps.perform(setPositionOf(window(), new Point(1001, 1002)));
        assertThat(window.getPosition(), equalTo(new Point(1001, 1002)));
        assertThat(seleniumSteps.get(windowPosition()), equalTo(new Point(1001, 1002)));
    }

    @Test
    public void getWindowTitleBySearchingTest() {
        assertThat(seleniumSteps.get(titleOf(window())), is(GOOGLE.getTitle()));
        assertThat(seleniumSteps.get(titleOf(window().byIndex(1))), is(FACEBOOK.getTitle()));
        assertThat(seleniumSteps.get(titleOf(window().byIndex(2))), is(GITHUB.getTitle()));
    }

    @Test
    public void getWindowTitleTest() {
        Window window = seleniumSteps.get(window());
        Window window2 = seleniumSteps.get(window().byIndex(1));
        Window window3 = seleniumSteps.get(window().byIndex(2));

        assertThat(seleniumSteps.get(titleOf(window)), is(GOOGLE.getTitle()));
        assertThat(seleniumSteps.get(titleOf(window2)), is(FACEBOOK.getTitle()));
        assertThat(seleniumSteps.get(titleOf(window3)), is(GITHUB.getTitle()));
    }
}
