package ru.tinkoff.qa.neptune.data.base.api.operations;

import ru.tinkoff.qa.neptune.data.base.api.ListOfStoredObjects;
import ru.tinkoff.qa.neptune.data.base.api.PersistableObject;

import java.util.Collection;

class OperationResultList<T extends PersistableObject> extends ListOfStoredObjects<T> {

    protected OperationResultList(String description, Collection<T> toAdd) {
        super(description, toAdd, INFO_PERSISTABLE_INFO::apply);
    }
}
