package com.github.toy.constructor.selenium.test.function.descriptions.value;

import com.github.toy.constructor.selenium.api.widget.*;
import com.github.toy.constructor.selenium.test.function.descriptions.DescribedWebElement;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;

import static com.github.toy.constructor.selenium.functions.searching.CommonConditions.shouldBeVisible;
import static com.github.toy.constructor.selenium.functions.searching.CommonConditions.shouldHaveCssValueContains;
import static com.github.toy.constructor.selenium.functions.searching.SearchSupplier.tab;
import static com.github.toy.constructor.selenium.functions.searching.SearchSupplier.textField;
import static com.github.toy.constructor.selenium.functions.value.SequentialGetValueSupplier.ofThe;
import static java.time.Duration.ofSeconds;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class SequentialGetValueDescriptionTest {

    @Test
    public void getValueWithSearchingForSomeValuable() {
        assertThat(ofThe(textField("Some text field", ofSeconds(55), shouldBeVisible()
                .and(shouldHaveCssValueContains("some css", "some value")))
                        .foundFrom(tab("Tab 1"))).get().toString(),
                is("Value"));
    }

    @Test
    public void getValueOfSomeValuable() {
        assertThat(ofThe(new SomeStubValuableWidget(new DescribedWebElement())).get().toString(),
                is("Value"));
    }

    @Name("Some valuable widget")
    private static class SomeStubValuableWidget extends Widget implements HasValue<String> {

        SomeStubValuableWidget(WebElement wrappedElement) {
            super(wrappedElement);
        }

        public String toString() {
            return "Some widget which has string value";
        }

        @Override
        public String getValue() {
            return "Test string";
        }
    }
}
