package ru.tinkoff.qa.neptune.selenium.api.widget;

import org.openqa.selenium.Point;

public interface HasLocation {
    /**
     * Where on the page is the top left-hand corner of the rendered element?
     *
     * @return A point, containing the location of the top left-hand corner of the element
     */
    Point getLocation();
}
