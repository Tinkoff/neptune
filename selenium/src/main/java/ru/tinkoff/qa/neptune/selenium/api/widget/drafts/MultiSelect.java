package ru.tinkoff.qa.neptune.selenium.api.widget.drafts;

import org.openqa.selenium.WebElement;
import ru.tinkoff.qa.neptune.selenium.api.widget.*;

import java.util.List;

@Name("Multiple Selector")
@NameMultiple("Multiple Selectors")
public abstract class MultiSelect extends Widget implements Editable<List<String>>, HasValue<List<String>>, HasOptions {

    public MultiSelect(WebElement wrappedElement) {
        super(wrappedElement);
    }
}
