package com.github.toy.constructor.selenium.test.function.descriptions.value;

import com.github.toy.constructor.selenium.api.widget.Name;
import com.github.toy.constructor.selenium.api.widget.Widget;
import com.github.toy.constructor.selenium.test.function.descriptions.DescribedWebElement;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;

import static com.github.toy.constructor.selenium.functions.searching.CommonConditions.*;
import static com.github.toy.constructor.selenium.functions.searching.SearchSupplier.*;
import static com.github.toy.constructor.selenium.functions.searching.SequentialSearchSupplier.element;
import static com.github.toy.constructor.selenium.functions.value.SequentialGetAttributeValueSupplier.attributeValue;
import static java.time.Duration.ofSeconds;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class SequentialGetAttrValueDescriptionTest {

    @Test
    public void getAttrValueWithSearchingForSomeWidget() {
        assertThat(attributeValue("some value").of(element(textField("Some text field", ofSeconds(55), shouldBeVisible()
                .and(shouldHaveAttribute("some attr", "some value"))))
                .foundFrom(tab("Tab 1"))).get().toString(),
                is("Value of the attribute 'some value' from (A single item from (Elements of type Text field) " +
                        "with condition (Should have string label(s) [Some text field]) AND ((Should be visible) " +
                        "AND (Should have attribute 'some attr=\"some value\"')). " +
                        "Time to get valuable result: 0:00:55:000)"));
    }

    @Test
    public void getAttrValueWithSearchingForSomeWebElement() {
        assertThat(attributeValue("some value").of(element(webElement(By.xpath(".//some//path"), ofSeconds(55), shouldBeVisible()
                        .and(shouldHaveAttributeContains("some attr", "some value"))))
                        .foundFrom(tab("Tab 1"))).get().toString(),
                is("Value of the attribute 'some value' from (A single item from (Web elements located By.xpath: .//some//path) " +
                        "with condition (Should be visible) AND (Should have attribute 'some attr' which contains value 'some value'). " +
                        "Time to get valuable result: 0:00:55:000)"));
    }

    @Test
    public void getAttrValueOfSomeWidget() {
        assertThat(attributeValue("some value").of(new SomeStubAttrWidget(new DescribedWebElement())).get().toString(),
                is("Value of the attribute 'some value' from (Some widget which has attribute value)"));
    }

    @Test
    public void getAttrValueOfSomeWebElement() {
        assertThat(attributeValue("some value").of(new DescribedWebElement()).get().toString(),
                is("Value of the attribute 'some value' from (Test web element)"));
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
