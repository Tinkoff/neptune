package com.github.toy.constructor.selenium.test.function.descriptions.navigation;

import com.github.toy.constructor.selenium.test.function.descriptions.DescribedWindow;
import org.testng.annotations.Test;

import static com.github.toy.constructor.selenium.functions.navigation.GetCurrentUrlSupplier.currentUrl;
import static com.github.toy.constructor.selenium.functions.navigation.GetCurrentUrlSupplier.currentUrlIn;
import static com.github.toy.constructor.selenium.functions.target.locator.window.GetWindowSupplier.window;
import static com.github.toy.constructor.selenium.functions.target.locator.window.WindowPredicates.hasTitle;
import static com.github.toy.constructor.selenium.functions.target.locator.window.WindowPredicates.hasUrl;
import static java.time.Duration.ofSeconds;
import static java.util.regex.Pattern.compile;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class GetCurrentUrlTest {

    @Test
    public void currentUrlTest() {
        assertThat(currentUrl().get().toString(),
                is("Current URL"));
    }

    @Test
    public void currentUrlWithWindowConditionTest() {
        assertThat(currentUrlIn(window().byIndex(1).onCondition(hasTitle("Some title")
                        .or(hasTitle(compile("Some title pattern")).and(hasUrl("Some url"))))
                        .withTimeToGetWindow(ofSeconds(5))).get().toString(),
                is("Current URL"));
    }

    @Test
    public void currentUrlWithWindowTest() {
        assertThat(currentUrlIn(new DescribedWindow()).get().toString(),
                is("Current URL"));
    }
}
