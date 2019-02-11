package ru.tinkoff.qa.neptune.data.base.api.operations;

import ru.tinkoff.qa.neptune.data.base.api.DBGetFunction;
import ru.tinkoff.qa.neptune.data.base.api.PersistableObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.String.format;
import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.toList;

abstract class DBGetOperationFunction<T extends PersistableObject, R extends DBGetOperationFunction> extends DBGetFunction<List<T>, R> {

    DBGetOperationFunction(OperationSequentialGetSupplier<T> innerSupplier) {
        super(innerSupplier);
    }

    static <T extends PersistableObject> List<T> getOperationObjects(Collection<T> toBeOperated) {
        checkArgument(nonNull(toBeOperated), "Collection of db objects  should not be null");
        checkArgument(toBeOperated
                .stream()
                .filter(Objects::isNull)
                .collect(toList())
                .size() == 0, format("There are null-values. The collection: %s", toBeOperated));

        return new ArrayList<>(toBeOperated);
    }
}
