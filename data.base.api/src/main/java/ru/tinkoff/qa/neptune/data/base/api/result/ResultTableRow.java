package ru.tinkoff.qa.neptune.data.base.api.result;

import java.util.ArrayList;
import java.util.Collection;

import static java.lang.String.format;

@Deprecated(forRemoval = true)
class ResultTableRow extends ArrayList<Object> {

    ResultTableRow(Collection<Object> from) {
        super(from);
    }

    @Override
    public String toString() {
        return format("row of field values: %s", super.toString());
    }
}
