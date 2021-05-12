package ru.tinkoff.qa.neptune.selenium.api.widget.drafts;

import org.openqa.selenium.WebElement;
import ru.tinkoff.qa.neptune.selenium.api.widget.*;

/**
 * Checkboxes, radio buttons etc.
 */
@Name("Flag")
@NameMultiple("Flags")
public abstract class Flag extends Widget implements Editable<Boolean>,
        HasValue<Boolean> {

    public Flag(WebElement wrappedElement) {
        super(wrappedElement);
    }

    @Name("Check box")
    @NameMultiple("Check boxes")
    public static abstract class CheckBox extends Flag {
        public CheckBox(WebElement wrappedElement) {
            super(wrappedElement);
        }
    }

    @Name("Radio button")
    @NameMultiple("Radio buttons")
    public static abstract class RadioButton extends Flag {
        public RadioButton(WebElement wrappedElement) {
            super(wrappedElement);
        }
    }

    @Name("Toggle")
    @NameMultiple("Toggles")
    public static abstract class Toggle extends Flag {
        public Toggle(WebElement wrappedElement) {
            super(wrappedElement);
        }
    }
}
