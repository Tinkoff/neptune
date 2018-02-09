package com.github.toy.constructor.selenium.api.widget.drafts;

import com.github.toy.constructor.selenium.api.widget.Editable;
import com.github.toy.constructor.selenium.api.widget.HasValue;
import com.github.toy.constructor.selenium.api.widget.Name;
import com.github.toy.constructor.selenium.api.widget.Widget;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;

import java.util.List;

@Name("Selector")
public abstract class Select extends Widget implements Editable<String>, HasValue<String> {

    public Select(WebElement wrappedElement) {
        super(wrappedElement);
    }

    /**
     * Select the option at the given index.
     *
     * @param index The option at this index will be selected
     * @throws NoSuchElementException If no matching option elements are found
     */
    public abstract void selectByIndex(int index) throws NoSuchElementException;

    /**
     * @return All options belonging to this select.
     */
    public abstract List<WebElement> getOptions();

    /**
     * @return All selected options belonging to this select.
     */
    public abstract List<WebElement> getAllSelectedOptions();

    /**
     * Deselect all selected options.
     *
     * @throws UnsupportedOperationException when select does not support multiple
     * selection.
     */
    public abstract void deselectAll() throws UnsupportedOperationException;
}
