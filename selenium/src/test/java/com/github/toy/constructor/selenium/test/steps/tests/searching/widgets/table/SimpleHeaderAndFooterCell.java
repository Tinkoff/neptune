package com.github.toy.constructor.selenium.test.steps.tests.searching.widgets.table;

import com.github.toy.constructor.selenium.api.widget.Name;
import com.github.toy.constructor.selenium.api.widget.Priority;
import com.github.toy.constructor.selenium.api.widget.drafts.TableCell;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import static com.github.toy.constructor.selenium.api.widget.Priority.HIGHEST;
import static com.github.toy.constructor.selenium.test.FakeDOMModel.TH;
import static com.github.toy.constructor.selenium.test.steps.tests.searching.widgets.WidgetNames.COMMON_BORDER_CELL;

@Name(COMMON_BORDER_CELL)
@Priority(2)
@FindBy(tagName = TH)
public class SimpleHeaderAndFooterCell extends TableCell {
    public SimpleHeaderAndFooterCell(WebElement wrappedElement) {
        super(wrappedElement);
    }

    @Override
    public String getValue() {
        return getWrappedElement().getText();
    }
}
