package ru.tinkoff.qa.neptune.data.base.test.persistable.object.operations.insert.delete.update;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.data.base.test.persistable.object.operations.BaseDbOperationTest;
import ru.tinkoff.qa.neptune.data.base.test.persistable.object.tables.Book;
import ru.tinkoff.qa.neptune.data.base.test.persistable.object.tables.QBook;
import ru.tinkoff.qa.neptune.data.base.test.persistable.object.tables.db.one.tables.*;
import ru.tinkoff.qa.neptune.data.base.test.persistable.object.tables.db.two.tables.*;

import java.util.Date;

import static java.util.Objects.nonNull;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.testng.Assert.*;
import static ru.tinkoff.qa.neptune.data.base.api.queries.SelectASingle.oneOf;
import static ru.tinkoff.qa.neptune.data.base.api.queries.jdoql.JDOQLQueryParameters.byJDOQuery;

public class InsertTest extends BaseDbOperationTest {

    private Manufacturer lada;

    //positive
    private Book theHunchbackOfNotreDame;
    private Publisher signet;

    //negative
    private Book theLegendOfTheAges;
    private Car kalina;

    @BeforeClass
    public void setUpBeforeClass() {
        theHunchbackOfNotreDame = dataBaseSteps.select(oneOf(Book.class, byJDOQuery(QBook.class)
                .where(qBook -> qBook.name.eq("The Hunchback of Notre-Dame"))));

        theLegendOfTheAges = dataBaseSteps.select(oneOf(Book.class, byJDOQuery(QBook.class)
                .where(qBook -> qBook.name.eq("The Legend of the Ages"))));

        signet = dataBaseSteps.select(oneOf(Publisher.class, byJDOQuery(QPublisher.class)
                .where(qPublisher -> qPublisher.name.eq("Signet"))));

        lada = dataBaseSteps.select(oneOf(Manufacturer.class, byJDOQuery(QManufacturer.class)
                .where(qManufacturer -> qManufacturer.name.eq("Lada"))));

        kalina = dataBaseSteps.select(oneOf(Car.class, byJDOQuery(QCar.class)
                .where(qCar -> qCar.name.eq("Kalina"))));
    }

    @Test
    public void insertPositiveTest() {
        var catalogItem = new Catalog().setBook(theHunchbackOfNotreDame)
                .setPublisher(signet)
                .setYearOfPublishing(2010);

        var producedFrom = new Date();
        var swCross = new CarModel().setCar(new Car().setManufacturer(lada)
                .setName("Granta"))
                .setCarModelName("SW Cross")
                .setProducedFrom(producedFrom);

        var inserted = dataBaseSteps.insert(catalogItem, swCross);

        var catalogAdded = dataBaseSteps.select(oneOf(Catalog.class, byJDOQuery(QCatalog.class)
                .where(qCatalog -> qCatalog.book.eq(theHunchbackOfNotreDame)
                        .and(qCatalog.publisher.eq(signet))
                        .and(qCatalog.yearOfPublishing.eq(2010)))));

        var carAdded = dataBaseSteps.select(oneOf(Car.class, byJDOQuery(QCar.class)
                .where(qCar -> qCar.manufacturer.eq(lada)
                        .and(qCar.name.eq("Granta")))));

        var carModelAdded = dataBaseSteps.select(oneOf(CarModel.class, byJDOQuery(QCarModel.class)
                .where(qCarModel -> qCarModel.carModelName.eq("SW Cross")
                        .and(qCarModel.car.eq(carAdded))
                        .and(qCarModel.car.manufacturer.eq(lada))))
                .criteria("Produced from is " + producedFrom,
                        carModel -> carModel.getProducedFrom().equals(producedFrom)));


        assertTrue(nonNull(carAdded));
        assertThat(inserted, containsInAnyOrder(carModelAdded, catalogAdded));
    }

    @Test(dependsOnMethods = "insertPositiveTest")
    public void insertNegativeTest() {
        var catalogItem = new Catalog().setBook(theLegendOfTheAges)
                .setPublisher(null)// the inserting should be failed because of this
                .setYearOfPublishing(2019);

        var kalinaCross = new CarModel().setCar(kalina)
                .setCarModelName("Kalina Cross") //this inserting is supposed to be successful if object above was valid
                //for the inserting
                .setProducedFrom(new Date());

        try {
            dataBaseSteps.insert(kalinaCross, catalogItem);
        }
        catch (Exception e) {
            var catalogAdded = dataBaseSteps.select(oneOf(Catalog.class, byJDOQuery(QCatalog.class)
                    .where(qCatalog -> qCatalog.book.eq(theLegendOfTheAges)
                            .and(qCatalog.yearOfPublishing.eq(2019)))));

            var carModelAdded = dataBaseSteps.select(oneOf(CarModel.class, byJDOQuery(QCarModel.class)
                    .where(qCarModel -> qCarModel.carModelName.eq("Kalina Cross")
                            .and(qCarModel.car.eq(kalina))
                            .and(qCarModel.car.manufacturer.eq(lada)))));

            assertFalse(nonNull(catalogAdded));
            assertFalse(nonNull(carModelAdded));
            return;
        }

        fail("Exception was expected");
    }
}
