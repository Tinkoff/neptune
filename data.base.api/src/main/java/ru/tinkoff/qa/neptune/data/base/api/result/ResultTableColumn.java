package ru.tinkoff.qa.neptune.data.base.api.result;

import java.util.ArrayList;

import static java.lang.String.format;

@Deprecated(forRemoval = true)
class ResultTableColumn<T> extends ArrayList<T> {

    ResultTableColumn() {
        super();
    }

    @Override
    public String toString() {
        return format("Column of the specific field values: %s", super.toString());
    }
}
