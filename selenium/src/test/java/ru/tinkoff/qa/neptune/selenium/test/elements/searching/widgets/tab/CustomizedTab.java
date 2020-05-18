package ru.tinkoff.qa.neptune.selenium.test.elements.searching.widgets.tab;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;
import ru.tinkoff.qa.neptune.selenium.api.widget.Labeled;
import ru.tinkoff.qa.neptune.selenium.api.widget.Name;
import ru.tinkoff.qa.neptune.selenium.api.widget.drafts.Tab;

import java.util.List;

import static java.util.stream.Collectors.toList;
import static ru.tinkoff.qa.neptune.selenium.test.FakeDOMModel.*;
import static ru.tinkoff.qa.neptune.selenium.test.elements.searching.widgets.WidgetNames.CUSTOMIZED_TAB;

@Name(CUSTOMIZED_TAB)
@FindBy(className = TAB_CLASS)
public class CustomizedTab extends Tab implements Labeled {

    @FindAll({@FindBy(xpath = LABEL_XPATH),
            @FindBy(xpath = LABEL_XPATH2)})
    private List<WebElement> labels;

    public CustomizedTab(WebElement wrappedElement) {
        super(wrappedElement);
    }

    @Override
    public boolean isActive() {
        return false;
    }

    @Override
    public List<String> labels() {
        return labels.stream().map(WebElement::getText).collect(toList());
    }

    @Override
    public void click() {
        getWrappedElement().click();
    }
}
