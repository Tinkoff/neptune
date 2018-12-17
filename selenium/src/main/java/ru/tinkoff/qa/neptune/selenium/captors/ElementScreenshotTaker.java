package ru.tinkoff.qa.neptune.selenium.captors;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * Takes a picture of a certain element/list of elements on the page by algorithm designed by users.
 */
public interface ElementScreenshotTaker {

    /**
     * Takes custom screenshots of a single web element on a page.
     *
     * @param toTakeAPicture is a web element to take a picture from
     * @param target is a format of screenshot to return
     * @param <X> is a type if returned value
     * @return a taken screenshot
     */
    <X> X getScreenshotAs(WebElement toTakeAPicture, OutputType<X> target);

    /**
     * Takes custom screenshots of a list of elements on a page.
     *
     * @param toTakeAPicture is a list of elements to take a picture from
     * @param target is a format of screenshot to return
     * @param <X> is a type if returned value
     * @return a taken screenshot
     */
    <X> X getScreenshotAs(List<WebElement> toTakeAPicture, OutputType<X> target);
}
