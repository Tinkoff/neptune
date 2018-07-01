package com.github.toy.constructor.selenium.test.function.descriptions.target.locator.window;

import com.github.toy.constructor.selenium.test.function.descriptions.DescribedWindow;
import org.testng.annotations.Test;

import static com.github.toy.constructor.selenium.functions.target.locator.window.GetWindowSupplier.window;
import static com.github.toy.constructor.selenium.functions.target.locator.window.GetWindowTitleSupplier.titleOf;
import static com.github.toy.constructor.selenium.functions.target.locator.window.GetWindowTitleSupplier.windowTitle;
import static com.github.toy.constructor.selenium.functions.target.locator.window.WindowPredicates.hasTitle;
import static java.time.Duration.ofSeconds;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class GetWindowTitleDescription {

    @Test
    public void getPositionDescription() {
        assertThat(windowTitle().toString(),
                is("Title of the window"));
    }

    @Test
    public void getPositionOfWindowSearchingDescription() {
        assertThat(titleOf(window().byIndex(2)
                        .withTimeToGetWindow(ofSeconds(5))
                        .onCondition(hasTitle("Some title"))).toString(),
                is("Title of the window"));
    }

    @Test
    public void getPositionOfWindowDescription() {
        assertThat(titleOf(new DescribedWindow()).toString(),
                is("Title of the window"));
    }
}
