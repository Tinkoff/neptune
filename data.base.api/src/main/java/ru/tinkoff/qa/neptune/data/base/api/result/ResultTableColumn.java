package ru.tinkoff.qa.neptune.data.base.api.result;

import ru.tinkoff.qa.neptune.core.api.steps.LoggableObject;

import java.util.ArrayList;

import static java.lang.String.format;

class ResultTableColumn<T> extends ArrayList<T> implements LoggableObject {

    ResultTableColumn() {
        super();
    }

    @Override
    public String toString() {
        return format("Column of the specific field values: %s", super.toString());
    }
}
