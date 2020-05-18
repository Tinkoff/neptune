package ru.tinkoff.qa.neptune.selenium.test.elements.searching.widgets.table;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;
import ru.tinkoff.qa.neptune.selenium.api.widget.Labeled;
import ru.tinkoff.qa.neptune.selenium.api.widget.Name;
import ru.tinkoff.qa.neptune.selenium.api.widget.drafts.Table;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;
import static org.openqa.selenium.By.className;
import static ru.tinkoff.qa.neptune.selenium.test.FakeDOMModel.*;
import static ru.tinkoff.qa.neptune.selenium.test.elements.searching.widgets.WidgetNames.SPREADSHEET_TABLE;

@Name(SPREADSHEET_TABLE)
@FindBy(className = SPREAD_SHEET_CLASS)
public class SpreadsheetTable extends Table implements Labeled {

    @FindAll({@FindBy(xpath = LABEL_XPATH),
            @FindBy(xpath = LABEL_XPATH2)})
    private List<WebElement> labels;

    @FindBy(className = HEADLINE_CLASS)
    private WebElement headerElement;

    @FindBy(className = STRING_CLASS)
    private List<WebElement> rowElements;

    public SpreadsheetTable(WebElement wrappedElement) {
        super(wrappedElement);
    }

    @Override
    public List<String> labels() {
        return labels.stream().map(WebElement::getText).collect(toList());
    }

    @Override
    public Map<String, List<String>> getValue() {
        List<String> header =  headerElement.findElements(className(CELL_CLASS))
                .stream().map(WebElement::getText).collect(toList());
        List<List<String>> rows = rowElements.stream().map(webElement -> webElement.findElements(className(CELL_CLASS)))
                .collect(toList())
                .stream().map(elementList -> elementList.stream().map(WebElement::getText).collect(toList()))
                .collect(toList());

        Map<String, List<String>> result = new HashMap<>();
        header.forEach(s -> {
            int index = header.indexOf(s);
            List<String> column = new ArrayList<>();
            rows.forEach(strings -> column.add(strings.get(index)));
            result.put(s, column);
        });
        return result;
    }
}
