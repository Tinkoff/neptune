package ru.tinkoff.qa.neptune.selenium.test.elements.searching.widgets.select;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import ru.tinkoff.qa.neptune.selenium.api.widget.Label;
import ru.tinkoff.qa.neptune.selenium.api.widget.Name;
import ru.tinkoff.qa.neptune.selenium.api.widget.Priority;
import ru.tinkoff.qa.neptune.selenium.api.widget.drafts.Select;

import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.openqa.selenium.By.xpath;
import static ru.tinkoff.qa.neptune.selenium.test.FakeDOMModel.*;
import static ru.tinkoff.qa.neptune.selenium.test.elements.searching.widgets.WidgetNames.MULTI_SELECT;

@Name(MULTI_SELECT)
@FindBy(className = MULTI_SELECT_CLASS)
@Priority(2)
public class MultiSelect extends Select {

    @FindBy(className = ITEM_OPTION_CLASS)
    private List<WebElement> options;

    public MultiSelect(WebElement wrappedElement) {
        super(wrappedElement);
    }

    @Label
    public String label1() {
        return findElements(xpath(LABEL_XPATH)).get(0).getText();
    }

    @Label
    public String label12() {
        return findElements(xpath(LABEL_XPATH2)).get(1).getText();
    }

    @Override
    public List<String> getOptions() {
        return options.stream().map(WebElement::getText).collect(toList());
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
        return options.stream().filter(WebElement::isSelected).map(WebElement::getText).collect(toList());
    }
}
