package ru.tinkoff.qa.neptune.selenium.api.widget;

import org.openqa.selenium.Dimension;

public interface HasSize {
    /**
     * It is the width and height of the rendered element
     *
     * @return The size of the element on the page.
     */
    @NeedToScrollIntoView
    Dimension getSize();
}
