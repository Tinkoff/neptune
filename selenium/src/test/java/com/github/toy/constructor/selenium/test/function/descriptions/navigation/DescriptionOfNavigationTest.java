package com.github.toy.constructor.selenium.test.function.descriptions.navigation;

import com.github.toy.constructor.selenium.test.function.descriptions.DescribedWindow;
import org.testng.annotations.Test;

import static com.github.toy.constructor.selenium.functions.navigation.Back.back;
import static com.github.toy.constructor.selenium.functions.navigation.Forward.forward;
import static com.github.toy.constructor.selenium.functions.navigation.Refresh.refresh;
import static com.github.toy.constructor.selenium.functions.navigation.ToUrl.toUrl;
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
                is("Navigate to URL. With parameters: {The first window/tab,https://github.com}"));
    }

    @Test
    public void toUrlWithWindowConditionTest() {
        assertThat(toUrl(window().byIndex(1).onCondition(hasTitle("Some title")
                        .or(hasTitle(compile("Some title pattern")).and(hasUrl("Some url"))))
                        .withTimeToGetWindow(ofSeconds(5)), "https://www.google.ru")

                        .andThenToUrl(window().byIndex(1).onCondition(hasTitle("Some title")
                                        .or(hasTitle(compile("Some title pattern")).and(hasUrl("Some url"))))
                                        .withTimeToGetWindow(ofSeconds(5)), "https://github.com").get().toString(),
                is("Navigate to URL. With parameters: {Window/tab by index 1 with condition (Has title 'Some title') " +
                        "OR ((Has title which matches regExp pattern 'Some title pattern') " +
                        "AND (Has loaded url 'Some url')). " +
                        "Time to get valuable result: 0:00:05:000,https://github.com}"));
    }

    @Test
    public void toUrlWithWindowTest() {
        assertThat(toUrl(new DescribedWindow(), "https://www.google.ru")

                        .andThenToUrl(new DescribedWindow(), "https://github.com").get().toString(),
                is("Navigate to URL. With parameters: {Test stab window,https://github.com}"));
    }

    @Test
    public void forwardTest() {
        assertThat(forward().get().toString(),
                is("Navigate forward. With parameters: {The first window/tab}"));
    }

    @Test
    public void forwardWindowConditionTest() {
        assertThat(forward(window().byIndex(1).onCondition(hasTitle("Some title")
                        .or(hasTitle(compile("Some title pattern")).and(hasUrl("Some url"))))
                        .withTimeToGetWindow(ofSeconds(5))).get().toString(),
                is("Navigate forward. With parameters: {Window/tab by index 1 with condition " +
                        "(Has title 'Some title') OR ((Has title which matches regExp pattern 'Some title pattern') AND " +
                        "(Has loaded url 'Some url')). Time to get valuable result: 0:00:05:000}"));
    }

    @Test
    public void forwardWithWindowTest() {
        assertThat(forward(new DescribedWindow()).get().toString(),
                is("Navigate forward. With parameters: {Test stab window}"));
    }

    @Test
    public void backTest() {
        assertThat(back().get().toString(),
                is("Navigate back. With parameters: {The first window/tab}"));
    }

    @Test
    public void backWindowConditionTest() {
        assertThat(back(window().byIndex(1).onCondition(hasTitle("Some title")
                        .or(hasTitle(compile("Some title pattern")).and(hasUrl("Some url"))))
                        .withTimeToGetWindow(ofSeconds(5))).get().toString(),
                is("Navigate back. With parameters: {Window/tab by index 1 with condition (Has title 'Some title') " +
                        "OR ((Has title which matches regExp pattern 'Some title pattern') " +
                        "AND (Has loaded url 'Some url')). Time to get valuable result: 0:00:05:000}"));
    }

    @Test
    public void backWithWindowTest() {
        assertThat(back(new DescribedWindow()).get().toString(),
                is("Navigate back. With parameters: {Test stab window}"));
    }

    @Test
    public void refreshTest() {
        assertThat(refresh().get().toString(),
                is("Refresh. With parameters: {The first window/tab}"));
    }

    @Test
    public void refreshWindowConditionTest() {
        assertThat(refresh(window().byIndex(1).onCondition(hasTitle("Some title")
                        .or(hasTitle(compile("Some title pattern")).and(hasUrl("Some url"))))
                        .withTimeToGetWindow(ofSeconds(5))).get().toString(),
                is("Refresh. With parameters: {Window/tab by index 1 with condition (Has title 'Some title') " +
                        "OR ((Has title which matches regExp pattern 'Some title pattern') " +
                        "AND (Has loaded url 'Some url')). Time to get valuable result: 0:00:05:000}"));
    }

    @Test
    public void refreshWindowTest() {
        assertThat(refresh(new DescribedWindow()).get().toString(),
                is("Refresh. With parameters: {Test stab window}"));
    }
}
