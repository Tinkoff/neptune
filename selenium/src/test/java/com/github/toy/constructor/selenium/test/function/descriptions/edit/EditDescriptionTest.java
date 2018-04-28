package com.github.toy.constructor.selenium.test.function.descriptions.edit;

import com.github.toy.constructor.selenium.api.widget.Editable;
import com.github.toy.constructor.selenium.api.widget.Name;
import com.github.toy.constructor.selenium.api.widget.Widget;
import com.github.toy.constructor.selenium.test.function.descriptions.DescribedWebElement;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;

import java.util.List;

import static com.github.toy.constructor.selenium.functions.edit.EditActionSupplier.valueOfThe;
import static com.github.toy.constructor.selenium.functions.searching.CommonConditions.shouldBeEnabled;
import static com.github.toy.constructor.selenium.functions.searching.CommonConditions.shouldBeVisible;
import static com.github.toy.constructor.selenium.functions.searching.CommonConditions.shouldHaveAttributeContains;
import static com.github.toy.constructor.selenium.functions.searching.SearchSupplier.textField;
import static com.github.toy.constructor.selenium.functions.searching.SearchSupplier.widget;
import static com.github.toy.constructor.selenium.functions.searching.SequentialSearchSupplier.element;
import static java.time.Duration.ofMillis;
import static java.time.Duration.ofSeconds;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.openqa.selenium.Keys.TAB;

public class EditDescriptionTest {

    @Test
    public void editWithSearchingForSomeEditable() {
        assertThat(valueOfThe(element(widget(SomeStubEditableWidget.class, ofSeconds(50),
                shouldBeVisible().and(shouldHaveAttributeContains("id", "someId"))))

                .foundFrom(new DescribedWebElement()), "12345")

                .andValueOfThe(element(textField("Some field", ofMillis(500),
                        shouldBeVisible().and(shouldBeEnabled()))), List.of(TAB, "123")).get().toString(),

                is("Edit. Set new value 12345 on Test web element ->\n" +
                        " A single item from (Elements of type Some editable widget) on condition " +
                        "(Should be visible) AND (Should have attribute 'id' which contains value 'someId'). " +
                        "Time to get valuable result: 0:00:50:000. With parameters: [12345] ->\n" +
                        "  Edit. Set new value [\uE004, 123] on A single item from (Elements of type Text field) on condition " +
                        "(Should have string label(s) [Some field]) AND ((Should be visible) AND (Should be enabled)). " +
                        "Time to get valuable result: 0:00:00:500. With parameters: [[\uE004, 123]]"));
    }

    @Test
    public void editOnSomeEditable() {
        assertThat(valueOfThe(new SomeStubEditableWidget(new DescribedWebElement()), "12345").get().toString(),
                is("Edit. Set new value 12345 on Some editable widget. With parameters: [12345]"));
    }

    @Name("Some editable widget")
    private static class SomeStubEditableWidget extends Widget implements Editable<String> {

        SomeStubEditableWidget(WebElement wrappedElement) {
            super(wrappedElement);
        }

        public String toString() {
            return "Some editable widget";
        }

        @Override
        public void edit(String valueToSet) {
        }
    }
}
