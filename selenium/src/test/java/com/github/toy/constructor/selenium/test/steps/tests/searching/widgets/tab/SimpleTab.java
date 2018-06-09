package com.github.toy.constructor.selenium.test.steps.tests.searching.widgets.tab;

import com.github.toy.constructor.selenium.api.widget.Name;
import com.github.toy.constructor.selenium.api.widget.Priority;
import com.github.toy.constructor.selenium.api.widget.drafts.Tab;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;

import static com.github.toy.constructor.selenium.api.widget.Priority.HIGHEST;
import static com.github.toy.constructor.selenium.test.FakeDOMModel.*;
import static com.github.toy.constructor.selenium.test.steps.tests.searching.widgets.WidgetNames.SIMPLE_TAB;

@Name(SIMPLE_TAB)
@FindBys({@FindBy(tagName = DIV), @FindBy(tagName = SPAN), @FindBy(tagName =LI)})
@Priority(HIGHEST)
public class SimpleTab extends Tab {

    public SimpleTab(WebElement wrappedElement) {
        super(wrappedElement);
    }

    @Override
    public boolean isActive() {
        return false;
    }

    @Override
    public void click() {
        getWrappedElement().click();
    }
}
