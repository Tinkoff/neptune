package ru.tinkoff.qa.neptune.data.base.test.persistable.object.tables.db.two.tables;

import ru.tinkoff.qa.neptune.data.base.api.PersistableObject;

import javax.jdo.annotations.*;
import java.util.Date;

@PersistenceCapable(table = "CarModels")
public class CarModel extends PersistableObject {

    @PrimaryKey
    @Persistent(valueStrategy= IdGeneratorStrategy.INCREMENT)
    @Column(name = "Id")
    private Integer id;

    @Column(name = "CariD")
    private Car car;

    @Column(name = "CarModelName")
    private String carModelName;

    @Column(name = "ProducedFrom")
    private Date producedFrom;

    @Column(name = "ProducedTo")
    private Date producedTo;

    public Integer getId() {
        return id;
    }


    public Car getCar() {
        return car;
    }

    public CarModel setCar(Car car) {
        this.car = car;
        return this;
    }

    public String getCarModelName() {
        return carModelName;
    }

    public CarModel setCarModelName(String carModelName) {
        this.carModelName = carModelName;
        return this;
    }

    public Date getProducedFrom() {
        return producedFrom;
    }

    public CarModel setProducedFrom(Date producedFrom) {
        this.producedFrom = producedFrom;
        return this;
    }

    public Date getProducedTo() {
        return producedTo;
    }

    public CarModel setProducedTo(Date producedTo) {
        this.producedTo = producedTo;
        return this;
    }
}
