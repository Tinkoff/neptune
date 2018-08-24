package ru.tinkoff.qa.neptune.data.base.test.persistable.object.equality;

import java.util.List;

class UnpersistableObject {

    private Boolean booleanField;
    private Integer intField;
    private List<Object> objectListField;
    private Long longField;
    private Character charField;

    public boolean isBooleanField() {
        return booleanField;
    }

    UnpersistableObject setBooleanField(boolean booleanField) {
        this.booleanField = booleanField;
        return this;
    }

    UnpersistableObject setIntField(int intField) {
        this.intField = intField;
        return this;
    }

    UnpersistableObject setObjectListField(List<Object> objectListField) {
        this.objectListField = objectListField;
        return this;
    }

    UnpersistableObject setLongField(long longField) {
        this.longField = longField;
        return this;
    }

    UnpersistableObject setCharField(char charField) {
        this.charField = charField;
        return this;
    }
}
