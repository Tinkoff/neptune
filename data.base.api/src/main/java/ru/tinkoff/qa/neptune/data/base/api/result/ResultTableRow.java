package ru.tinkoff.qa.neptune.data.base.api.result;

import ru.tinkoff.qa.neptune.core.api.steps.LoggableObject;

import java.util.ArrayList;
import java.util.Collection;

import static java.lang.String.format;

class ResultTableRow extends ArrayList<Object> implements LoggableObject {

    ResultTableRow(Collection<Object> from) {
        super(from);
    }

    @Override
    public String toString() {
        return format("row of field values: %s", super.toString());
    }
}
