package ru.tinkoff.qa.neptune.data.base.api.test;

import ru.tinkoff.qa.neptune.data.base.api.PersistableObject;

import javax.jdo.annotations.*;

@PersistenceCapable(table = "Publishers")
public class Publisher extends PersistableObject {

    @PrimaryKey
    @Persistent(valueStrategy=IdGeneratorStrategy.INCREMENT)
    @Column(name = "ID")
    private int id;

    @Column(name = "Name")
    private String name;

    public String getName() {
        return name;
    }

    public Publisher setName(String name) {
        this.name = name;
        return this;
    }

    public int getId() {
        return id;
    }
}
