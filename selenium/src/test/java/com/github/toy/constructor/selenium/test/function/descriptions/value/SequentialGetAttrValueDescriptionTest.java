package com.github.toy.constructor.selenium.test.function.descriptions.value;

import com.github.toy.constructor.selenium.api.widget.Name;
import com.github.toy.constructor.selenium.api.widget.Widget;
import com.github.toy.constructor.selenium.test.function.descriptions.DescribedWebElement;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;

import static com.github.toy.constructor.selenium.functions.searching.CommonConditions.*;
import static com.github.toy.constructor.selenium.functions.searching.SearchSupplier.*;
import static com.github.toy.constructor.selenium.functions.value.SequentialGetAttributeValueSupplier.attributeValue;
import static java.time.Duration.ofSeconds;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class SequentialGetAttrValueDescriptionTest {

    @Test
    public void getAttrValueWithSearchingForSomeWidget() {
        assertThat(attributeValue("some value").of(textField("Some text field", ofSeconds(55), shouldBeVisible()
                .and(shouldHaveAttribute("some attr", "some value")))
                .foundFrom(tab("Tab 1"))).get().toString(),
                is("Value of the attribute 'some value'"));
    }

    @Test
    public void getAttrValueWithSearchingForSomeWebElement() {
        assertThat(attributeValue("some value").of(webElement(By.xpath(".//some//path"), ofSeconds(55), shouldBeVisible()
                        .and(shouldHaveAttributeContains("some attr", "some value")))
                        .foundFrom(tab("Tab 1"))).get().toString(),
                is("Value of the attribute 'some value'"));
    }

    @Test
    public void getAttrValueOfSomeWidget() {
        assertThat(attributeValue("some value").of(new SomeStubAttrWidget(new DescribedWebElement())).get().toString(),
                is("Value of the attribute 'some value'"));
    }

    @Test
    public void getAttrValueOfSomeWebElement() {
        assertThat(attributeValue("some value").of(new DescribedWebElement()).get().toString(),
                is("Value of the attribute 'some value'"));
    }


    @Name("Some css widget")
    private static class SomeStubAttrWidget extends Widget {

        SomeStubAttrWidget(WebElement wrappedElement) {
            super(wrappedElement);
        }

        public String toString() {
            return "Some widget which has attribute value";
        }
    }
}
