package ru.tinkoff.qa.neptune.selenium.test.elements.searching.widgets.table;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import ru.tinkoff.qa.neptune.selenium.api.widget.Labeled;
import ru.tinkoff.qa.neptune.selenium.api.widget.Name;

import java.util.List;

import static java.util.stream.Collectors.toList;
import static ru.tinkoff.qa.neptune.selenium.test.FakeDOMModel.LABEL_TAG;
import static ru.tinkoff.qa.neptune.selenium.test.FakeDOMModel.TABLE;
import static ru.tinkoff.qa.neptune.selenium.test.elements.searching.widgets.WidgetNames.LABELED_TABLE;

@Name(LABELED_TABLE)
@FindBy(tagName = TABLE)
public class LabeledTable extends SimpleTable implements Labeled {

    @FindBy(tagName = LABEL_TAG)
    private List<WebElement> labels;

    public LabeledTable(WebElement wrappedElement) {
        super(wrappedElement);
    }

    @Override
    public List<String> labels() {
        return labels.stream().map(WebElement::getText).collect(toList());
    }
}
