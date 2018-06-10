package com.github.toy.constructor.selenium.test.steps.tests.searching.widgets.table;

import com.github.toy.constructor.selenium.api.widget.Name;
import com.github.toy.constructor.selenium.api.widget.Priority;
import com.github.toy.constructor.selenium.api.widget.drafts.Table;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.github.toy.constructor.selenium.api.widget.Priority.HIGHEST;
import static com.github.toy.constructor.selenium.test.FakeDOMModel.*;
import static com.github.toy.constructor.selenium.test.steps.tests.searching.widgets.WidgetNames.SIMPLE_TABLE;
import static java.util.stream.Collectors.toList;
import static org.openqa.selenium.By.tagName;

@Name(SIMPLE_TABLE)
@FindBy(tagName = TABLE)
@Priority(HIGHEST)
public class SimpleTable extends Table {

    @FindBys({@FindBy(tagName = T_HEAD), @FindBy(tagName = TR)})
    private WebElement headerElement;

    @FindBys({@FindBy(tagName = T_BODY), @FindBy(tagName = TR)})
    private List<WebElement> rowElements;

    public SimpleTable(WebElement wrappedElement) {
        super(wrappedElement);
    }

    @Override
    public Map<String, List<String>> getValue() {
        List<String> header =  headerElement.findElements(tagName(TH))
                .stream().map(WebElement::getText).collect(toList());
        List<List<String>> rows = rowElements.stream().map(webElement -> webElement.findElements(tagName(TD)))
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
