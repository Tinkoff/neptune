package com.github.toy.constructor.selenium.test.steps.tests.searching.widgets.text.field;

import com.github.toy.constructor.selenium.api.widget.Name;
import com.github.toy.constructor.selenium.api.widget.Priority;
import com.github.toy.constructor.selenium.api.widget.drafts.TextField;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

import static com.github.toy.constructor.selenium.api.widget.Priority.HIGHEST;
import static com.github.toy.constructor.selenium.test.FakeDOMModel.*;
import static com.github.toy.constructor.selenium.test.steps.tests.searching.widgets.WidgetNames.SIMPLE_TEXT_FIELD;

@Name(SIMPLE_TEXT_FIELD)
@FindBy(xpath = TEXT_FIELD_XPATH)
@Priority(HIGHEST)
public class SimpleTextField extends TextField {

    public SimpleTextField(WebElement wrappedElement) {
        super(wrappedElement);
    }

    @Override
    public void edit(List<CharSequence> valueToSet) {
       valueToSet.forEach(charSequence -> getWrappedElement().sendKeys(charSequence));
    }

    @Override
    public String getValue() {
        return getWrappedElement().getAttribute(VALUE);
    }
}
