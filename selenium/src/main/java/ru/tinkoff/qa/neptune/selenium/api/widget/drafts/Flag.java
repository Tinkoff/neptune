package ru.tinkoff.qa.neptune.selenium.api.widget.drafts;

import org.openqa.selenium.WebElement;
import ru.tinkoff.qa.neptune.selenium.api.widget.Editable;
import ru.tinkoff.qa.neptune.selenium.api.widget.HasValue;
import ru.tinkoff.qa.neptune.selenium.api.widget.Name;
import ru.tinkoff.qa.neptune.selenium.api.widget.Widget;

/**
 * Checkboxes, radio buttons etc.
 */
@Name("Flag")
public abstract class Flag extends Widget implements Editable<Boolean>,
        HasValue<Boolean> {

    public Flag(WebElement wrappedElement) {
        super(wrappedElement);
    }

    @Name("Check box")
    public static abstract class CheckBox extends Flag {
        public CheckBox(WebElement wrappedElement) {
            super(wrappedElement);
        }
    }

    @Name("Radio button")
    public static abstract class RadioButton extends Flag {
        public RadioButton(WebElement wrappedElement) {
            super(wrappedElement);
        }
    }

    @Name("Toggle")
    public static abstract class Toggle extends Flag {
        public Toggle(WebElement wrappedElement) {
            super(wrappedElement);
        }
    }
}
