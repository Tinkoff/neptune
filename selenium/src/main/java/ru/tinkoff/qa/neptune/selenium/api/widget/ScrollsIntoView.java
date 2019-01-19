package ru.tinkoff.qa.neptune.selenium.api.widget;

/**
 * This interface declares behavior of the {@link Widget}. It means the scrolling
 * a widget into view area.
 */
public interface ScrollsIntoView {

    /**
     * This method should scroll the widget into view.
     * When some subclass of the {@link Widget} implements {@link ScrollsIntoView} then
     * the method is invoked every time before any action is performed on an instance of the
     * class.
     */
    void scrollsIntoView();
}
