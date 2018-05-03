package com.github.toy.constructor.selenium.test.function.descriptions.navigation;

import com.github.toy.constructor.selenium.test.function.descriptions.DescribedWindow;
import org.testng.annotations.Test;

import static com.github.toy.constructor.selenium.functions.navigation.NavigationActionSupplier.toUrl;
import static com.github.toy.constructor.selenium.functions.target.locator.window.GetWindowSupplier.window;
import static com.github.toy.constructor.selenium.functions.target.locator.window.WindowPredicates.hasTitle;
import static com.github.toy.constructor.selenium.functions.target.locator.window.WindowPredicates.hasUrl;
import static java.time.Duration.ofSeconds;
import static java.util.regex.Pattern.compile;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class DescriptionOfNavigationTest {

    @Test
    public void toUrlTest() {
        assertThat(toUrl("https://www.google.ru").andThenToUrl("https://github.com").get().toString(),
                is("Navigate to URL. With parameters: {Current window/tab,https://github.com}"));
    }

    @Test
    public void toUrlWithWindowConditionTest() {
        assertThat(toUrl(window().byIndex(1).onCondition(hasTitle("Some title")
                        .or(hasTitle(compile("Some title pattern")).and(hasUrl("Some url"))))
                        .withTimeToGetWindow(ofSeconds(5)), "https://www.google.ru")

                        .andThenToUrl(window().byIndex(1).onCondition(hasTitle("Some title")
                                        .or(hasTitle(compile("Some title pattern")).and(hasUrl("Some url"))))
                                        .withTimeToGetWindow(ofSeconds(5)), "https://github.com").get().toString(),
                is("Navigate to URL. " +
                        "With parameters: {Window/tab from (Window/tab by index 1) on condition (Has title 'Some title') OR " +
                        "((Has title which matches regExp patter 'Some title pattern') AND (Has loaded url 'Some url')). " +
                        "Time to get valuable result: 0:00:05:000,https://github.com}"));
    }

    @Test
    public void toUrlWithWindowTest() {
        assertThat(toUrl(new DescribedWindow(), "https://www.google.ru")

                        .andThenToUrl(new DescribedWindow(), "https://github.com").get().toString(),
                is("Navigate to URL. With parameters: {Window Test stab window,https://github.com}"));
    }
}
