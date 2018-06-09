package com.github.toy.constructor.selenium.test.steps.tests.searching.widgets.table;

import com.github.toy.constructor.selenium.api.widget.Name;
import com.github.toy.constructor.selenium.api.widget.Priority;
import com.github.toy.constructor.selenium.api.widget.drafts.Table;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;
import java.util.Map;

import static com.github.toy.constructor.selenium.api.widget.Priority.HIGHEST;
import static com.github.toy.constructor.selenium.test.FakeDOMModel.*;
import static com.github.toy.constructor.selenium.test.steps.tests.searching.widgets.WidgetNames.SIMPLE_TABLE;

@Name(SIMPLE_TABLE)
@FindBy(tagName = TABLE)
@Priority(HIGHEST)
public class SimpleTable extends Table {

    public SimpleTable(WebElement wrappedElement) {
        super(wrappedElement);
    }

    @Override
    public Map<String, List<String>> getValue() {
        return null;
    }
}
