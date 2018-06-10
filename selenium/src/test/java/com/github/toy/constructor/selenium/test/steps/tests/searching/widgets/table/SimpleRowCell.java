package com.github.toy.constructor.selenium.test.steps.tests.searching.widgets.table;

import com.github.toy.constructor.selenium.api.widget.Name;
import com.github.toy.constructor.selenium.api.widget.Priority;
import com.github.toy.constructor.selenium.api.widget.drafts.TableCell;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import static com.github.toy.constructor.selenium.api.widget.Priority.HIGHEST;
import static com.github.toy.constructor.selenium.test.FakeDOMModel.TD;
import static com.github.toy.constructor.selenium.test.steps.tests.searching.widgets.WidgetNames.COMMON_ROW_CELL;

@Name(COMMON_ROW_CELL)
@Priority(HIGHEST)
@FindBy(tagName = TD)
public class SimpleRowCell extends TableCell {
    public SimpleRowCell(WebElement wrappedElement) {
        super(wrappedElement);
    }

    @Override
    public String getValue() {
        return getWrappedElement().getText();
    }
}
