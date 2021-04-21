package ru.tinkoff.qa.neptune.selenium.api.widget.drafts;

import org.openqa.selenium.WebElement;
import ru.tinkoff.qa.neptune.selenium.api.widget.*;

/**
 * For calendars
 */
@Name("Calendar")
@NameMultiple("Calendars")
public abstract class CalendarWidget extends Widget implements Editable<CalendarDay>,
        HasValue<CalendarDay> {

    public CalendarWidget(WebElement wrappedElement) {
        super(wrappedElement);
    }
}
