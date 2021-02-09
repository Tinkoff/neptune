package ru.tinkoff.qa.neptune.selenium.api.widget;

import org.openqa.selenium.WrapsElement;

public interface HasTextContent extends WrapsElement {

    /**
     * Get the visible text of a widget.
     *
     * @return The visible text of a widget.
     */
    default String getText() {
        return getWrappedElement().getText();
    }
}
