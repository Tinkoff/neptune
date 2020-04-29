package ru.tinkoff.qa.neptune.selenium.test.elements.searching.widgets.text.field;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import ru.tinkoff.qa.neptune.selenium.api.widget.Name;
import ru.tinkoff.qa.neptune.selenium.api.widget.Priority;
import ru.tinkoff.qa.neptune.selenium.api.widget.drafts.TextField;

import java.util.List;

import static ru.tinkoff.qa.neptune.selenium.api.widget.Priority.HIGHEST;
import static ru.tinkoff.qa.neptune.selenium.test.FakeDOMModel.TEXT_FIELD_XPATH;
import static ru.tinkoff.qa.neptune.selenium.test.FakeDOMModel.VALUE;
import static ru.tinkoff.qa.neptune.selenium.test.elements.searching.widgets.WidgetNames.SIMPLE_TEXT_FIELD;

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
