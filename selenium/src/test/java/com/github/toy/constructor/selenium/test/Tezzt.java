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
import static com.github.toy.constructor.selenium.functions.navigation.NavigationActionSupplier.toUrl;
import static com.github.toy.constructor.selenium.functions.searching.DefaultWidgetConditions.widgetShouldBeEnabled;
import static com.github.toy.constructor.selenium.functions.searching.DefaultWidgetConditions.widgetShouldBeVisible;
import static com.github.toy.constructor.selenium.functions.target.locator.SwitchActionSupplier.to;
import static com.github.toy.constructor.selenium.functions.target.locator.alert.AlertActionSupplier.accept;
import static com.github.toy.constructor.selenium.functions.target.locator.alert.AlertActionSupplier.dismiss;
import static com.github.toy.constructor.selenium.functions.target.locator.alert.GetAlertSupplier.alert;
import static com.github.toy.constructor.selenium.functions.click.ClickActionSupplier.on;
import static com.github.toy.constructor.selenium.functions.edit.EditActionSupplier.valueOf;
import static com.github.toy.constructor.selenium.functions.java.script.GetJavaScriptResultSupplier.javaScript;
import static com.github.toy.constructor.selenium.functions.searching.MultipleSearchSupplier.links;
import static com.github.toy.constructor.selenium.functions.searching.MultipleSearchSupplier.textFields;
import static com.github.toy.constructor.selenium.functions.searching.SearchSupplier.*;
import static com.github.toy.constructor.selenium.functions.searching.SequentialMultipleSearchSupplier.elements;
import static com.github.toy.constructor.selenium.functions.searching.SequentialSearchSupplier.element;
import static com.github.toy.constructor.selenium.functions.target.locator.frame.GetFrameSupplier.frame;
import static com.github.toy.constructor.selenium.functions.target.locator.window.GetWindowSupplier.window;
import static com.github.toy.constructor.selenium.functions.target.locator.window.WindowPredicates.hasTitle;
import static com.github.toy.constructor.selenium.functions.target.locator.window.WindowPredicates.hasUrl;
import static com.github.toy.constructor.selenium.functions.value.SequentialGetAttributeValueSupplier.attributeValue;
import static com.github.toy.constructor.selenium.functions.value.SequentialGetCSSValueSupplier.cssValue;
import static com.github.toy.constructor.selenium.functions.value.SequentialGetValueSupplier.ofThe;
import static com.google.common.collect.ImmutableList.of;
import static java.time.Duration.ofSeconds;
import static java.util.regex.Pattern.compile;
import static org.openqa.selenium.By.xpath;
import static org.openqa.selenium.Keys.HOME;

public class Tezzt {

    public void tezzt() throws Exception {
        SeleniumSteps selenium = getSubstituted(SeleniumSteps.class, params());

        Button button = selenium.find(element(button("B1"))
                .foundFrom(link("L1"))
                .foundFrom(webElement(xpath(""))));

        List<Link> links = selenium.find(elements(links())
                .foundFrom(button)
                .foundFrom(webElement(xpath(""))));

        List<TextField> textFields =
                selenium.click(on(element(button("submit", ofSeconds(50))).foundFrom(webElement(xpath(""))))
                        .andOn(element(link()))
                        .andOn(button))
                .find(elements(textFields()));

        selenium.perform(action("High-level complex step", seleniumSteps -> {
            //everything below will be documented as sub steps
            seleniumSteps.click(on(element(tab("Some tab")))
                    .andOn(element(button("Some button", ofSeconds(50)))
                            .foundFrom(webElement(xpath("some path")))));

            String text = selenium.getValue(ofThe(element(textField("Some text field"))));

            selenium.edit(
                    valueOf(
                            element(textField(ofSeconds(5),
                                    widgetShouldBeEnabled().and(widgetShouldBeVisible()))), of("123", HOME))

                    .andValueOf(element(flag()), true));

            selenium.evaluate(javaScript("Some script"));

            Alert alert = seleniumSteps.get(alert());

            seleniumSteps.alert(dismiss(alert)).alert(accept(alert(ofSeconds(20))));

            seleniumSteps.get(attributeValue("some attr").of(element(webElement(xpath("some path")))
                    .foundFrom(button("Some button"))));

            seleniumSteps.get(cssValue("some attr")
                    .of(
                            element(webElement(xpath("some path")))
                                    .foundFrom(button("S`ome button"))
                    ));

            Window window = selenium.get(window().byIndex(1).onCondition(hasTitle("Some title")
                    .or(hasTitle(compile("Some title pattern")).and(hasUrl("Some url"))))
                    .withTimeToGetWindow(ofSeconds(5)));
            selenium.performSwitch(to(window));

            selenium.performSwitch(to(frame(1)).andThenSwitchTo(window));
        }));

        selenium.navigate(toUrl("www.youtube.com")
                .andThenToUrl(window().byIndex(1), "www.google.com"));
    }
}