package com.github.toy.constructor.selenium.test;

import com.github.toy.constructor.selenium.SeleniumSteps;
import com.github.toy.constructor.selenium.api.widget.drafts.Button;
import com.github.toy.constructor.selenium.api.widget.drafts.Link;
import com.github.toy.constructor.selenium.api.widget.drafts.TextField;
import com.github.toy.constructor.selenium.functions.target.locator.window.Window;
import org.openqa.selenium.Alert;

import java.util.List;

import static com.github.toy.constructor.core.api.StoryWriter.action;
import static com.github.toy.constructor.core.api.proxy.ConstructorParameters.params;
import static com.github.toy.constructor.core.api.proxy.Substitution.getSubstituted;
import static com.github.toy.constructor.selenium.functions.edit.EditActionSupplier.valueOfThe;
import static com.github.toy.constructor.selenium.functions.navigation.GetCurrentUrlSupplier.currentUrl;
import static com.github.toy.constructor.selenium.functions.navigation.GetCurrentUrlSupplier.currentUrlIn;
import static com.github.toy.constructor.selenium.functions.navigation.NavigationActionSupplier.toUrl;
import static com.github.toy.constructor.selenium.functions.searching.CommonConditions.shouldBeEnabled;
import static com.github.toy.constructor.selenium.functions.searching.CommonConditions.shouldBeVisible;
import static com.github.toy.constructor.selenium.functions.searching.MultipleSearchSupplier.*;
import static com.github.toy.constructor.selenium.functions.target.locator.SwitchActionSupplier.to;
import static com.github.toy.constructor.selenium.functions.target.locator.alert.AlertActionSupplier.accept;
import static com.github.toy.constructor.selenium.functions.target.locator.alert.AlertActionSupplier.dismiss;
import static com.github.toy.constructor.selenium.functions.target.locator.alert.GetAlertSupplier.alert;
import static com.github.toy.constructor.selenium.functions.click.ClickActionSupplier.on;
import static com.github.toy.constructor.selenium.functions.java.script.GetJavaScriptResultSupplier.javaScript;
import static com.github.toy.constructor.selenium.functions.searching.SearchSupplier.*;
import static com.github.toy.constructor.selenium.functions.target.locator.frame.GetFrameFunction.*;
import static com.github.toy.constructor.selenium.functions.target.locator.frame.GetFrameSupplier.frame;
import static com.github.toy.constructor.selenium.functions.target.locator.window.GetWindowSupplier.window;
import static com.github.toy.constructor.selenium.functions.target.locator.window.WindowPredicates.hasTitle;
import static com.github.toy.constructor.selenium.functions.target.locator.window.WindowPredicates.hasUrl;
import static com.github.toy.constructor.selenium.functions.value.SequentialGetAttributeValueSupplier.attributeValue;
import static com.github.toy.constructor.selenium.functions.value.SequentialGetCSSValueSupplier.cssValue;
import static com.github.toy.constructor.selenium.functions.value.SequentialGetValueSupplier.ofThe;
import static com.github.toy.constructor.selenium.hamcrest.matchers.alert.AlertHasTextMatcher.alertHasText;
import static com.github.toy.constructor.selenium.hamcrest.matchers.elements.HasAttributeMatcher.hasAttribute;
import static com.github.toy.constructor.selenium.hamcrest.matchers.elements.HasCssValueMatcher.hasCss;
import static com.github.toy.constructor.selenium.hamcrest.matchers.elements.HasLocationMatcher.hasLoction;
import static com.github.toy.constructor.selenium.hamcrest.matchers.elements.HasNestedElementMatcher.hasNestedElement;
import static com.github.toy.constructor.selenium.hamcrest.matchers.elements.HasNestedElementsMatcher.hasNestedElements;
import static com.github.toy.constructor.selenium.hamcrest.matchers.elements.HasSizeMatcher.hasDimensionalSize;
import static com.github.toy.constructor.selenium.hamcrest.matchers.elements.HasTextMatcher.hasText;
import static com.github.toy.constructor.selenium.hamcrest.matchers.elements.HasValueMatcher.hasValue;
import static com.github.toy.constructor.selenium.hamcrest.matchers.elements.IsElementEnabledMatcher.isEnabled;
import static com.github.toy.constructor.selenium.hamcrest.matchers.elements.IsElementVisibleMatcher.isVisible;
import static com.github.toy.constructor.selenium.hamcrest.matchers.frame.HasFrameMatcher.hasFrame;
import static com.github.toy.constructor.selenium.hamcrest.matchers.url.AtThePageMatcher.atThePage;
import static com.github.toy.constructor.selenium.hamcrest.matchers.window.IsWindowPresentMatcher.windowIsPresent;
import static com.github.toy.constructor.selenium.hamcrest.matchers.window.WindowHasPositionMatcher.windowHasPosition;
import static com.github.toy.constructor.selenium.hamcrest.matchers.window.WindowHasSizeMatcher.windowHasSize;
import static com.github.toy.constructor.selenium.hamcrest.matchers.window.WindowHasTitleMatcher.windowHasTitle;
import static com.google.common.collect.ImmutableList.of;
import static java.time.Duration.ofSeconds;
import static java.util.regex.Pattern.compile;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.core.StringContains.containsString;
import static org.openqa.selenium.By.xpath;
import static org.openqa.selenium.Keys.HOME;

public class Tezzt {

    public void tezzt() throws Exception {
        SeleniumSteps selenium = getSubstituted(SeleniumSteps.class);

        Button button = selenium.find(button("B1")
                .foundFrom(link("L1"))
                .foundFrom(webElement(xpath(""))));

        List<Link> links = selenium.find(links()
                .foundFrom(button)
                .foundFrom(webElement(xpath(""))));

        List<TextField> textFields =
                selenium.click(on(button("submit", ofSeconds(50)).foundFrom(webElement(xpath(""))))
                        .andOn(link())
                        .andOn(button()))
                .find(textFields());

        selenium.perform(action("High-level complex step", seleniumSteps -> {
            //everything below will be documented as sub steps
            seleniumSteps.click(on(tab("Some tab"))
                    .andOn(button("Some button", ofSeconds(50))
                            .foundFrom(webElement(xpath("some path")))));

            String text = selenium.getValue(ofThe(textField("Some text field")));

            selenium.edit(
                    valueOfThe(textField(ofSeconds(5),
                                    shouldBeEnabled().and(shouldBeVisible())), of("123", HOME))

                    .andValueOfThe(flag(), true));

            selenium.evaluate(javaScript("Some script"));

            Alert alert = seleniumSteps.get(alert());

            seleniumSteps.alert(dismiss(alert)).alert(accept(alert(ofSeconds(20))));

            seleniumSteps.get(attributeValue("some attr").of(webElement(xpath("some path"))
                    .foundFrom(button("Some button"))));

            seleniumSteps.get(cssValue("some attr")
                    .of(webElement(xpath("some path"))
                                    .foundFrom(button("S`ome button"))));

            Window window = selenium.get(window().byIndex(1).onCondition(hasTitle("Some title")
                    .or(hasTitle(compile("Some title pattern")).and(hasUrl("Some url"))))
                    .withTimeToGetWindow(ofSeconds(5)));
            selenium.performSwitch(to(window));

            selenium.performSwitch(to(frame(index(ofSeconds(5), 1))).andThenSwitchTo(window));
            selenium.performSwitch(to(frame(nameOrId(ofSeconds(5), "frame name"))));
            selenium.performSwitch(to(frame(insideElement(ofSeconds(5),
                    selenium.find(webElement(xpath("some path")))))));
        }));

        selenium.navigate(toUrl("www.youtube.com")
                .andThenToUrl(window().byIndex(1), "www.google.com"));

        selenium.get(currentUrl());
        selenium.get(currentUrlIn(window().byIndex(1)));
    }

    public void tezzt2() throws Exception {
        SeleniumSteps selenium = getSubstituted(SeleniumSteps.class, params());

        assertThat("Check element",
                selenium.find(webElement(xpath("some path"))),
                hasNestedElement(button("Submit", ofSeconds(50), shouldBeVisible())));

        assertThat("Check elements",
                selenium.find(webElement(xpath("some path 2"))),
                hasNestedElements(buttons("Submit", ofSeconds(50), shouldBeVisible()))
                        .withCount(2).checkCountStrictly(true));

        assertThat("Check element",
                selenium.find(webElement(xpath("some path 3"))),
                isVisible());

        assertThat("Check element",
                selenium.find(webElement(xpath("some path 4"))),
                isEnabled());

        assertThat("Check element",
                selenium.find(webElement(xpath("some path 5"))),
                hasAttribute("Some attr", containsString("123")));

        assertThat("Check element",
                selenium.find(webElement(xpath("some path 6"))),
                hasAttribute("Some attr2", "123"));

        assertThat("Check element",
                selenium.find(webElement(xpath("some path 7"))),
                hasAttribute("Some attr3"));

        assertThat("Check element",
                selenium.find(webElement(xpath("some path 8"))),
                hasCss("Some css", containsString("123")));

        assertThat("Check element",
                selenium.find(webElement(xpath("some path 9"))),
                hasCss("Some css2", "123"));

        assertThat("Check element",
                selenium.find(webElement(xpath("some path 10"))),
                hasCss("Some css3"));

        assertThat("Check element",
                selenium.find(webElement(xpath("some path 11"))),
                hasText(containsString("123")));

        assertThat("Check element",
                selenium.find(webElement(xpath("some path 12"))),
                hasText("123"));

        assertThat("Check element",
                selenium.find(webElement(xpath("some path 13"))),
                hasText());


        assertThat("Check value",
                selenium.find(textField()),
                hasValue("123"));

        assertThat("Check value",
                selenium.find(textField()),
                hasValue(containsString("123")));

        assertThat("Check value",
                selenium.find(checkbox()),
                hasValue(true));


        assertThat("Check size",
                selenium.find(textField()),
                hasDimensionalSize(1, 2));

        assertThat("Check size",
                selenium.find(textField()),
                hasDimensionalSize(greaterThan(1), greaterThan(2)));

        assertThat("Check size",
                selenium.find(textField()),
                hasDimensionalSize(1, greaterThan(2)));

        assertThat("Check size",
                selenium.find(textField()),
                hasDimensionalSize(greaterThan(1), 2));

        assertThat("Check location",
                selenium.find(textField()),
                hasLoction(1, 2));

        assertThat("Check location",
                selenium.find(textField()),
                hasLoction(greaterThan(1), greaterThan(2)));

        assertThat("Check location",
                selenium.find(textField()),
                hasLoction(1, greaterThan(2)));

        assertThat("Check location",
                selenium.find(textField()),
                hasLoction(greaterThan(1), 2));

        assertThat("Check location",
                selenium.find(textField()),
                hasLoction(greaterThan(1), 2).relativeTo(selenium.find(button())));

        assertThat("Check location",
                selenium.find(textField()),
                hasLoction(greaterThan(1), 2).relativeTo(selenium.find(webElement(xpath("path")))));

        assertThat("check window title",
                selenium.get(window().withTimeToGetWindow(ofSeconds(20)).byIndex(2)),
                windowHasTitle("Title"));

        assertThat("check window title",
                selenium.get(window().withTimeToGetWindow(ofSeconds(20)).byIndex(2)),
                windowHasTitle(containsString("Title")));

        assertThat("Check window size",
                selenium.get(window().withTimeToGetWindow(ofSeconds(20)).byIndex(2)),
                windowHasSize(1, 2));

        assertThat("Check window size",
                selenium.get(window().withTimeToGetWindow(ofSeconds(20)).byIndex(2)),
                windowHasSize(1, greaterThan(2)));

        assertThat("Check window size",
                selenium.get(window().withTimeToGetWindow(ofSeconds(20)).byIndex(2)),
                windowHasSize(greaterThan(1), 2));

        assertThat("Check window size",
                selenium.get(window().withTimeToGetWindow(ofSeconds(20)).byIndex(2)),
                windowHasSize(greaterThan(1), greaterThan(2)));

        assertThat("Check window position",
                selenium.get(window().withTimeToGetWindow(ofSeconds(20)).byIndex(2)),
                windowHasPosition(1, 2));

        assertThat("Check window position",
                selenium.get(window().withTimeToGetWindow(ofSeconds(20)).byIndex(2)),
                windowHasPosition(1, greaterThan(2)));

        assertThat("Check window position",
                selenium.get(window().withTimeToGetWindow(ofSeconds(20)).byIndex(2)),
                windowHasPosition(greaterThan(1), 2));

        assertThat("Check window position",
                selenium.get(window().withTimeToGetWindow(ofSeconds(20)).byIndex(2)),
                windowHasPosition(greaterThan(1), greaterThan(2)));

        Window window = selenium.get(window().withTimeToGetWindow(ofSeconds(20)).byIndex(2));
        assertThat("Check presence of a window",
                window, windowIsPresent());

        assertThat("Url of the window",
                window,
                atThePage("https://www.youtube.com"));

        assertThat("Url of the window",
                window,
                atThePage(containsString("www.youtube.com")));

        assertThat("Url",
                selenium,
                atThePage("https://www.youtube.com"));

        assertThat("Url",
                selenium,
                atThePage(containsString("www.youtube.com")));

        assertThat("Url of frame",
                selenium.get(frame(index(1))),
                atThePage("https://www.youtube.com"));

        assertThat("Url of frame",
                selenium.get(frame(index(1))),
                atThePage(containsString("www.youtube.com")));

        assertThat("Alert text",
                selenium.get(alert()),
                alertHasText("Some text"));

        assertThat("Alert text",
                selenium.get(alert()),
                alertHasText(containsString("Some text")));

        assertThat("Check presence of a frame",
                window, hasFrame(index(1)));

        assertThat("Check presence of a frame",
                selenium,
                hasFrame(index(2)));

        assertThat("Url of the window",
                selenium.get(frame(nameOrId("some name or id2"))),
                hasFrame(index(2)));
    }
}