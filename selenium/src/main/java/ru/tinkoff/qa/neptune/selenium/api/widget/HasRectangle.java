package ru.tinkoff.qa.neptune.selenium.api.widget;

import org.openqa.selenium.Rectangle;

public interface HasRectangle {
    /**
     * @return The location and size of the rendered element
     */
    @NeedToScrollIntoView
    Rectangle getRect();
}
