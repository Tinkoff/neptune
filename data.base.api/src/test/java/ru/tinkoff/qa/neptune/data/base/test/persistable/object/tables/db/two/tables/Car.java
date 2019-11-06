package ru.tinkoff.qa.neptune.data.base.test.persistable.object.tables.db.two.tables;

import ru.tinkoff.qa.neptune.data.base.api.PersistableObject;

import javax.jdo.annotations.*;

@PersistenceCapable(table = "Car")
public class Car extends PersistableObject {

    @PrimaryKey
    @Persistent(valueStrategy= IdGeneratorStrategy.INCREMENT)
    @Column(name = "Id")
    private Integer id;

    @Column(name = "Name")
    private String name;

    @Column(name = "Manufacturer")
    private Manufacturer manufacturer;

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Car setName(String name) {
        this.name = name;
        return this;
    }

    public Manufacturer getManufacturer() {
        return manufacturer;
    }

    public Car setManufacturer(Manufacturer manufacturer) {
        this.manufacturer = manufacturer;
        return this;
    }
}
