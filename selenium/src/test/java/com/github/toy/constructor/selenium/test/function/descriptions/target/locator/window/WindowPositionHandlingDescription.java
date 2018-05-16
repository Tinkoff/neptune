package com.github.toy.constructor.selenium.test.function.descriptions.target.locator.window;

import com.github.toy.constructor.selenium.test.function.descriptions.DescribedWindow;
import org.openqa.selenium.Point;
import org.testng.annotations.Test;

import static com.github.toy.constructor.selenium.functions.target.locator.window.GetWindowPositionSupplier.positionOf;
import static com.github.toy.constructor.selenium.functions.target.locator.window.GetWindowPositionSupplier.windowPosition;
import static com.github.toy.constructor.selenium.functions.target.locator.window.GetWindowSupplier.window;
import static com.github.toy.constructor.selenium.functions.target.locator.window.SetWindowPositionSupplier.setPositionOf;
import static com.github.toy.constructor.selenium.functions.target.locator.window.SetWindowPositionSupplier.setWindowPosition;
import static com.github.toy.constructor.selenium.functions.target.locator.window.WindowPredicates.hasTitle;
import static java.time.Duration.ofSeconds;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class WindowPositionHandlingDescription {

    @Test
    public void getPositionDescription() {
        assertThat(windowPosition().toString(),
                is("Position from (The first window/tab)"));
    }

    @Test
    public void getPositionOfWindowSearchingDescription() {
        assertThat(positionOf(window().byIndex(2)
                        .withTimeToGetWindow(ofSeconds(5))
                        .onCondition(hasTitle("Some title"))).toString(),
                is("Position from (Window/tab by index 2 with condition Has title 'Some title'. " +
                        "Time to get valuable result: 0:00:05:000)"));
    }

    @Test
    public void getPositionOfWindowDescription() {
        assertThat(positionOf(new DescribedWindow()).toString(),
                is("Position from (Test stab window)"));
    }

    @Test
    public void setPositionDescription() {
        assertThat(setWindowPosition(new Point(10, 20)).get().toString(),
                is("Set position of the window. With parameters: {The first window/tab,(10, 20)}"));
    }

    @Test
    public void setPositionOfWindowSearchingDescription() {
        assertThat(setPositionOf(window().byIndex(2)
                        .withTimeToGetWindow(ofSeconds(5))
                        .onCondition(hasTitle("Some title")), new Point(10, 20)).get().toString(),
                is("Set position of the window. With parameters: {Window/tab by index 2 with condition Has title 'Some title'. " +
                        "Time to get valuable result: 0:00:05:000,(10, 20)}"));
    }

    @Test
    public void setPositionOfWindowDescription() {
        assertThat(setPositionOf(new DescribedWindow(), new Point(10, 20)).get().toString(),
                is("Set position of the window. With parameters: {Test stab window,(10, 20)}"));
    }
}
