package com.github.toy.constructor.selenium.test.function.descriptions.target.locator;

import com.github.toy.constructor.selenium.functions.target.locator.SwitchesToItself;
import com.github.toy.constructor.selenium.functions.target.locator.TargetLocatorSupplier;
import com.github.toy.constructor.selenium.test.function.descriptions.DescribedWindow;
import org.openqa.selenium.NoAlertPresentException;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static com.github.toy.constructor.core.api.StoryWriter.condition;
import static com.github.toy.constructor.selenium.functions.target.locator.SwitchActionSupplier.to;
import static com.github.toy.constructor.selenium.functions.target.locator.active.element.GetActiveElementSupplier.activeElement;
import static com.github.toy.constructor.selenium.functions.target.locator.alert.GetAlertSupplier.alert;
import static com.github.toy.constructor.selenium.functions.target.locator.content.DefaultContentSupplier.defaultContent;
import static com.github.toy.constructor.selenium.functions.target.locator.frame.GetFrameFunction.index;
import static com.github.toy.constructor.selenium.functions.target.locator.frame.GetFrameFunction.nameOrId;
import static com.github.toy.constructor.selenium.functions.target.locator.frame.GetFrameSupplier.frame;
import static com.github.toy.constructor.selenium.functions.target.locator.frame.parent.ParentFrameSupplier.parentFrame;
import static com.github.toy.constructor.selenium.functions.target.locator.window.GetWindowSupplier.window;
import static com.github.toy.constructor.selenium.functions.target.locator.window.WindowPredicates.hasTitle;
import static java.lang.String.format;
import static java.time.Duration.ofMillis;
import static java.time.Duration.ofMinutes;
import static java.time.Duration.ofSeconds;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class SwitchActionDescription {

    @DataProvider
    public static Object[][] switchParameters() {
        return new Object[][]{
                {alert(condition("Some alert condition", alert -> true),
                        ofSeconds(50),
                        ofMillis(500),
                        () -> new NoAlertPresentException("Some exception"))},

                {window().byIndex(2).withTimeToGetWindow(ofMinutes(5)).onCondition(hasTitle("Some title"))},
                {new DescribedWindow()},
                {activeElement()},
                {activeElement(ofSeconds(700))},
                {defaultContent()},
                {defaultContent(ofSeconds(700))},
                {parentFrame()},
                {parentFrame(ofSeconds(700))},
                {frame(nameOrId(ofSeconds(700), "some frame or id"))},
                {frame(index(ofSeconds(700), 2))},
        };
    }

    @Test(dataProvider = "switchParameters")
    public void switchToDescriptionTest(Object object) {
        if (TargetLocatorSupplier.class.isAssignableFrom(object.getClass())) {
            assertThat(to(TargetLocatorSupplier.class.cast(object)).get().toString(),
                    is(format("Switch to. With parameters: {%s}", object)));
            return;
        }

        if (SwitchesToItself.class.isAssignableFrom(object.getClass())) {
            assertThat(to(SwitchesToItself.class.cast(object)).get().toString(),
                    is(format("Switch to. With parameters: {%s}", object)));
            return;
        }

        throw new IllegalArgumentException(format("Can't check descriptopn of the switching with %s", object));
    }
}
