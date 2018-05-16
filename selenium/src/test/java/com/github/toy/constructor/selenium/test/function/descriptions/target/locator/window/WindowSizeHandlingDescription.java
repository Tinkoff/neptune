package com.github.toy.constructor.selenium.test.function.descriptions.target.locator.window;

import com.github.toy.constructor.selenium.test.function.descriptions.DescribedWindow;
import org.openqa.selenium.Dimension;
import org.testng.annotations.Test;

import static com.github.toy.constructor.selenium.functions.target.locator.window.GetWindowSizeSupplier.sizeOf;
import static com.github.toy.constructor.selenium.functions.target.locator.window.GetWindowSizeSupplier.windowSize;
import static com.github.toy.constructor.selenium.functions.target.locator.window.GetWindowSupplier.window;
import static com.github.toy.constructor.selenium.functions.target.locator.window.SetWindowSizeSupplier.setSizeOf;
import static com.github.toy.constructor.selenium.functions.target.locator.window.SetWindowSizeSupplier.setWindowSize;
import static com.github.toy.constructor.selenium.functions.target.locator.window.WindowPredicates.hasTitle;
import static java.time.Duration.ofSeconds;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class WindowSizeHandlingDescription {

    @Test
    public void getSizeDescription() {
        assertThat(windowSize().toString(),
                is("Size from (The first window/tab)"));
    }

    @Test
    public void getSizeOfWindowSearchingDescription() {
        assertThat(sizeOf(window().byIndex(2)
                        .withTimeToGetWindow(ofSeconds(5))
                        .onCondition(hasTitle("Some title"))).toString(),
                is("Size from (Window/tab by index 2 with condition Has title 'Some title'. " +
                        "Time to get valuable result: 0:00:05:000)"));
    }

    @Test
    public void getSizeOfWindowDescription() {
        assertThat(sizeOf(new DescribedWindow()).toString(),
                is("Size from (Test stab window)"));
    }

    @Test
    public void setSizeDescription() {
        assertThat(setWindowSize(new Dimension(10, 20)).get().toString(),
                is("Set size of the window. With parameters: {The first window/tab,(10, 20)}"));
    }

    @Test
    public void setSizeOfWindowSearchingDescription() {
        assertThat(setSizeOf(window().byIndex(2)
                        .withTimeToGetWindow(ofSeconds(5))
                        .onCondition(hasTitle("Some title")), new Dimension(10, 20)).get().toString(),
                is("Set size of the window. With parameters: {Window/tab by index 2 with condition Has title 'Some title'. " +
                        "Time to get valuable result: 0:00:05:000,(10, 20)}"));
    }

    @Test
    public void setSizeOfWindowDescription() {
        assertThat(setSizeOf(new DescribedWindow(), new Dimension(10, 20)).get().toString(),
                is("Set size of the window. With parameters: {Test stab window,(10, 20)}"));
    }
}
