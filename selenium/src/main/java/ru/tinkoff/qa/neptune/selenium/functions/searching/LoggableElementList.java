package ru.tinkoff.qa.neptune.selenium.functions.searching;

import org.openqa.selenium.SearchContext;
import ru.tinkoff.qa.neptune.core.api.LoggableObject;

import java.util.ArrayList;
import java.util.Collection;

class LoggableElementList<T extends SearchContext> extends ArrayList<T> implements LoggableObject {

    LoggableElementList() {
        super();
    }

    LoggableElementList(Collection<T> toAdd) {
        super(toAdd);
    }
}
