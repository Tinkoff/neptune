package ru.tinkoff.qa.neptune.selenium.api.widget.drafts;

import org.openqa.selenium.WebElement;
import ru.tinkoff.qa.neptune.selenium.api.widget.Editable;
import ru.tinkoff.qa.neptune.selenium.api.widget.HasValue;
import ru.tinkoff.qa.neptune.selenium.api.widget.Name;
import ru.tinkoff.qa.neptune.selenium.api.widget.Widget;

/**
 * For calendars
 */
@Name("Calendar")
public abstract class CalendarWidget extends Widget implements Editable<CalendarDay>,
        HasValue<CalendarDay> {

    public CalendarWidget(WebElement wrappedElement) {
        super(wrappedElement);
    }
}
