package ru.tinkoff.qa.neptune.selenium.api.widget;

import java.util.List;

public interface HasOptions {

    /**
     * This method returns options which available for the selecting
     *
     * @return a list of available options.
     */
    List<String> getOptions();
}
