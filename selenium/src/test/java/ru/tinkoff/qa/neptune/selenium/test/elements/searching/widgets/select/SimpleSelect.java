package ru.tinkoff.qa.neptune.selenium.test.elements.searching.widgets.select;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import ru.tinkoff.qa.neptune.selenium.api.widget.Name;
import ru.tinkoff.qa.neptune.selenium.api.widget.Priority;
import ru.tinkoff.qa.neptune.selenium.api.widget.drafts.Select;

import java.util.List;

import static java.util.stream.Collectors.toList;
import static ru.tinkoff.qa.neptune.selenium.api.widget.Priority.HIGHEST;
import static ru.tinkoff.qa.neptune.selenium.test.FakeDOMModel.OPTION;
import static ru.tinkoff.qa.neptune.selenium.test.FakeDOMModel.SELECT;
import static ru.tinkoff.qa.neptune.selenium.test.elements.searching.widgets.WidgetNames.SIMPLE_SELECT;

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
    public String getValue() {
        return options.stream().filter(WebElement::isSelected)
                .findFirst().map(WebElement::getText).
                        orElse(null);
    }
}
