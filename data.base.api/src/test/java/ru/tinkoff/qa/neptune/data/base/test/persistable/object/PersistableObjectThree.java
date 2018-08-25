package ru.tinkoff.qa.neptune.data.base.test.persistable.object;

import ru.tinkoff.qa.neptune.data.base.api.PersistableObject;

import java.util.List;

class PersistableObjectThree extends PersistableObject {

    private Boolean booleanField;
    private Integer intField;
    private List<Object> objectListField;
    private Long longField;
    private Character charField;

    public boolean isBooleanField() {
        return booleanField;
    }

    PersistableObjectThree setBooleanField(boolean booleanField) {
        this.booleanField = booleanField;
        return this;
    }

    PersistableObjectThree setIntField(int intField) {
        this.intField = intField;
        return this;
    }

    PersistableObjectThree setObjectListField(List<Object> objectListField) {
        this.objectListField = objectListField;
        return this;
    }

    PersistableObjectThree setLongField(long longField) {
        this.longField = longField;
        return this;
    }

    PersistableObjectThree setCharField(char charField) {
        this.charField = charField;
        return this;
    }
}
