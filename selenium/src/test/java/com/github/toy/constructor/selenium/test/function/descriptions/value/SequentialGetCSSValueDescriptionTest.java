package com.github.toy.constructor.selenium.test.function.descriptions.value;

import com.github.toy.constructor.selenium.api.widget.Name;
import com.github.toy.constructor.selenium.api.widget.Widget;
import com.github.toy.constructor.selenium.test.function.descriptions.DescribedWebElement;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;

import static com.github.toy.constructor.selenium.functions.searching.CommonConditions.*;
import static com.github.toy.constructor.selenium.functions.searching.SearchSupplier.tab;
import static com.github.toy.constructor.selenium.functions.searching.SearchSupplier.textField;
import static com.github.toy.constructor.selenium.functions.searching.SearchSupplier.webElement;
import static com.github.toy.constructor.selenium.functions.value.SequentialGetCSSValueSupplier.cssValue;
import static java.time.Duration.ofSeconds;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class SequentialGetCSSValueDescriptionTest {

    @Test
    public void getCSSValueWithSearchingForSomeWidget() {
        assertThat(cssValue("some value").of(textField("Some text field", ofSeconds(55), shouldBeVisible()
                .and(shouldHaveAttribute("some attr", "some value")))
                .foundFrom(tab("Tab 1"))).get().toString(),
                is("Value of the css property 'some value'"));
    }

    @Test
    public void getCSSValueWithSearchingForSomeWebElement() {
        assertThat(cssValue("some value").of(webElement(By.xpath(".//some//path"), ofSeconds(55), shouldBeVisible()
                        .and(shouldHaveAttributeContains("some attr", "some value")))
                        .foundFrom(tab("Tab 1"))).get().toString(),
                is("Value of the css property 'some value'"));
    }

    @Test
    public void getCSSValueOfSomeWidget() {
        assertThat(cssValue("some value").of(new SomeStubCSSWidget(new DescribedWebElement())).get().toString(),
                is("Value of the css property 'some value'"));
    }

    @Test
    public void getCSSValueOfSomeWebElement() {
        assertThat(cssValue("some value").of(new DescribedWebElement()).get().toString(),
                is("Value of the css property 'some value'"));
    }


    @Name("Some css widget")
    private static class SomeStubCSSWidget extends Widget {

        SomeStubCSSWidget(WebElement wrappedElement) {
            super(wrappedElement);
        }

        public String toString() {
            return "Some widget which has css value";
        }
    }
}
