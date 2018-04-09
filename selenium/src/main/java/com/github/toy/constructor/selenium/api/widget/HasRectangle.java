package com.github.toy.constructor.selenium.api.widget;

import org.openqa.selenium.Rectangle;

public interface HasRectangle {
    /**
     * @return The location and size of the rendered element
     */
    Rectangle getRect();
}
