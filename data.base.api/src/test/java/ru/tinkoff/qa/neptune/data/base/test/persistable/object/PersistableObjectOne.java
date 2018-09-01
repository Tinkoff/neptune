package ru.tinkoff.qa.neptune.data.base.test.persistable.object;

import ru.tinkoff.qa.neptune.data.base.api.PersistableObject;

import java.util.List;

class PersistableObjectOne extends PersistableObject {

    private Boolean booleanField;
    private Integer intField;
    private List<Object> objectListField;

    PersistableObjectOne setBooleanField(boolean booleanField) {
        this.booleanField = booleanField;
        return this;
    }

    PersistableObjectOne setIntField(int intField) {
        this.intField = intField;
        return this;
    }

    PersistableObjectOne setObjectListField(List<Object> objectListField) {
        this.objectListField = objectListField;
        return this;
    }
}
