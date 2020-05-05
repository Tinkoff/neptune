package ru.tinkoff.qa.neptune.selenium.test.elements.searching.widgets.tab;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;
import ru.tinkoff.qa.neptune.selenium.api.widget.Name;
import ru.tinkoff.qa.neptune.selenium.api.widget.Priority;
import ru.tinkoff.qa.neptune.selenium.api.widget.drafts.Tab;

import static ru.tinkoff.qa.neptune.selenium.api.widget.Priority.HIGHEST;
import static ru.tinkoff.qa.neptune.selenium.test.FakeDOMModel.*;
import static ru.tinkoff.qa.neptune.selenium.test.elements.searching.widgets.WidgetNames.SIMPLE_TAB;

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
