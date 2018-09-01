package ru.tinkoff.qa.neptune.data.base.test.persistable.object;

class PersistableObjectTwo extends PersistableObjectOne {

    private Long longField;
    private Character charField;

    PersistableObjectTwo setLongField(long longField) {
        this.longField = longField;
        return this;
    }

    PersistableObjectTwo setCharField(char charField) {
        this.charField = charField;
        return this;
    }
}
