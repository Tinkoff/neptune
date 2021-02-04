package ru.tinkoff.qa.neptune.selenium.functions.searching;

import org.openqa.selenium.SearchContext;

import java.util.ArrayList;
import java.util.Collection;

abstract class LoggableElementList<T extends SearchContext> extends ArrayList<T> {

    LoggableElementList() {
        super();
    }

    LoggableElementList(Collection<T> toAdd) {
        super(toAdd);
    }

    public abstract String toString();
}
