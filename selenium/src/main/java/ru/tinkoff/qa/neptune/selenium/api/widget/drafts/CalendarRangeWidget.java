package ru.tinkoff.qa.neptune.selenium.api.widget.drafts;

import org.openqa.selenium.WebElement;
import ru.tinkoff.qa.neptune.selenium.api.widget.Editable;
import ru.tinkoff.qa.neptune.selenium.api.widget.HasValue;
import ru.tinkoff.qa.neptune.selenium.api.widget.Name;
import ru.tinkoff.qa.neptune.selenium.api.widget.Widget;

/**
 * For calendar ranges
 */
@Name("Calendar range")
public abstract class CalendarRangeWidget extends Widget implements Editable<CalendarRange>,
        HasValue<CalendarRange> {

    public CalendarRangeWidget(WebElement wrappedElement) {
        super(wrappedElement);
    }
}
