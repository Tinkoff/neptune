package ru.tinkoff.qa.neptune.selenium.api.widget.drafts;

import org.openqa.selenium.WebElement;
import ru.tinkoff.qa.neptune.selenium.api.widget.*;

/**
 * For calendar ranges
 */
@Name("Calendar range")
@NameMultiple("Calendar ranges")
public abstract class CalendarRangeWidget extends Widget implements Editable<CalendarRange>,
        HasValue<CalendarRange> {

    public CalendarRangeWidget(WebElement wrappedElement) {
        super(wrappedElement);
    }
}
