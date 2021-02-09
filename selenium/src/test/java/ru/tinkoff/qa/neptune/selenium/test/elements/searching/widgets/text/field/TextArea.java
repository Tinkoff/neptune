package ru.tinkoff.qa.neptune.selenium.test.elements.searching.widgets.text.field;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import ru.tinkoff.qa.neptune.selenium.api.widget.Label;
import ru.tinkoff.qa.neptune.selenium.api.widget.Name;
import ru.tinkoff.qa.neptune.selenium.api.widget.ScrollsIntoView;
import ru.tinkoff.qa.neptune.selenium.api.widget.drafts.TextField;

import java.util.List;

import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static ru.tinkoff.qa.neptune.selenium.test.FakeDOMModel.*;
import static ru.tinkoff.qa.neptune.selenium.test.elements.searching.widgets.WidgetNames.TEXT_AREA;

@Name(TEXT_AREA)
@FindBy(tagName = TEXT_AREA_TAG)
public class TextArea extends TextField implements ScrollsIntoView {

    @FindBy(tagName = LABEL_TAG)
    private List<WebElement> label;

    private int scrollCount = 0;

    public TextArea(WebElement wrappedElement) {
        super(wrappedElement);
    }

    @Override
    public void edit(List<CharSequence> valueToSet) {
        valueToSet.forEach(charSequence -> getWrappedElement().sendKeys(charSequence));
    }

    @Override
    public String getValue() {
        return ofNullable(getWrappedElement().getAttribute(VALUE))
                .orElse(EMPTY);
    }

    @Override
    public void scrollIntoView() {
        scrollCount++;
    }

    public int getScrollCount() {
        return scrollCount;
    }

    @Label
    public String label1() {
        return label.get(0).getText();
    }

    @Label
    public String label2() {
        return label.get(1).getText();
    }
}
