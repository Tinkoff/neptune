package com.github.toy.constructor.selenium.test.steps.tests.searching.widgets.select;

import com.github.toy.constructor.selenium.api.widget.Name;
import com.github.toy.constructor.selenium.api.widget.Priority;
import com.github.toy.constructor.selenium.api.widget.drafts.Select;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

import static com.github.toy.constructor.selenium.api.widget.Priority.HIGHEST;
import static com.github.toy.constructor.selenium.test.FakeDOMModel.OPTION;
import static com.github.toy.constructor.selenium.test.FakeDOMModel.SELECT;
import static com.github.toy.constructor.selenium.test.steps.tests.searching.widgets.WidgetNames.SIMPLE_SELECT;

@Name(SIMPLE_SELECT)
@FindBy(tagName = SELECT)
@Priority(HIGHEST)
public class SimpleSelect extends Select {

    @FindBy(tagName = OPTION)
    private List<WebElement> options;

    public SimpleSelect(WebElement wrappedElement) {
        super(wrappedElement);
    }

    @Override
    public void edit(String valueToSet) {
        WebElement optionToSelect =
                options.stream().filter(option -> valueToSet.equals(option.getText()))
                .findFirst().orElseThrow(() -> new NoSuchElementException("There is no option " + valueToSet));
        optionToSelect.click();
    }

    @Override
    public List<String> getValue() {
        return options.stream().filter(WebElement::isSelected)
                .findFirst().map(webElement -> List.of(webElement.getText())).
                orElse(List.of());
    }
}
