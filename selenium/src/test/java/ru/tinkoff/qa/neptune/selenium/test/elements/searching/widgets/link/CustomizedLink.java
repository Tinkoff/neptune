package ru.tinkoff.qa.neptune.selenium.test.elements.searching.widgets.link;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;
import ru.tinkoff.qa.neptune.selenium.api.widget.Labeled;
import ru.tinkoff.qa.neptune.selenium.api.widget.Name;
import ru.tinkoff.qa.neptune.selenium.api.widget.drafts.Link;

import java.util.List;
import java.util.stream.Collectors;

import static ru.tinkoff.qa.neptune.selenium.test.FakeDOMModel.*;
import static ru.tinkoff.qa.neptune.selenium.test.elements.searching.widgets.WidgetNames.CUSTOM_LINK;

@FindBy(css = CUSTOM_LINK_CSS)
@Name(CUSTOM_LINK)
public class CustomizedLink extends Link implements Labeled {

    @FindAll({@FindBy(xpath = LABEL_XPATH),
            @FindBy(xpath = LABEL_XPATH2)})
    private List<WebElement> labels;

    public CustomizedLink(WebElement wrappedElement) {
        super(wrappedElement);
    }

    @Override
    public List<String> labels() {
        return labels.stream().map(WebElement::getText).collect(Collectors.toList());
    }

    @Override
    public String getReference() {
        return getWrappedElement().getAttribute(HREF);
    }

    @Override
    public void click() {
        getWrappedElement().click();
    }
}
