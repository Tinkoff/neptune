package com.github.toy.constructor.selenium.test.function.descriptions.target.locator.window;

import com.github.toy.constructor.selenium.test.function.descriptions.DescribedWindow;
import org.openqa.selenium.Point;
import org.testng.annotations.Test;

import static com.github.toy.constructor.selenium.functions.target.locator.window.CloseWindowActionSupplier.closeWindow;
import static com.github.toy.constructor.selenium.functions.target.locator.window.GetWindowPositionSupplier.positionOf;
import static com.github.toy.constructor.selenium.functions.target.locator.window.GetWindowPositionSupplier.windowPosition;
import static com.github.toy.constructor.selenium.functions.target.locator.window.GetWindowSupplier.window;
import static com.github.toy.constructor.selenium.functions.target.locator.window.SetWindowPositionSupplier.setPositionOf;
import static com.github.toy.constructor.selenium.functions.target.locator.window.SetWindowPositionSupplier.setWindowPosition;
import static com.github.toy.constructor.selenium.functions.target.locator.window.WindowPredicates.hasTitle;
import static java.time.Duration.ofSeconds;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class WindowClosingActionDescription {


    @Test
    public void setPositionDescription() {
        assertThat(closeWindow().get().toString(),
                is("Close window/tab. With parameters: {The first window/tab}"));
    }

    @Test
    public void setPositionOfWindowSearchingDescription() {
        assertThat(closeWindow(window().byIndex(2)
                        .withTimeToGetWindow(ofSeconds(5))
                        .onCondition(hasTitle("Some title"))).get().toString(),
                is("Close window/tab. With parameters: {Window/tab by index 2 with condition Has title 'Some title'. " +
                        "Time to get valuable result: 0:00:05:000}"));
    }

    @Test
    public void setPositionOfWindowDescription() {
        assertThat(closeWindow(new DescribedWindow()).get().toString(),
                is("Close window/tab. With parameters: {Test stab window}"));
    }
}
