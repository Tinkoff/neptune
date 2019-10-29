package ru.tinkoff.qa.neptune.data.base.test.persistable.object.operations.insert.delete.update;

import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.data.base.test.persistable.object.operations.BaseDbOperationTest;
import ru.tinkoff.qa.neptune.data.base.test.persistable.object.tables.db.one.tables.Author;
import ru.tinkoff.qa.neptune.data.base.test.persistable.object.tables.db.one.tables.QAuthor;
import ru.tinkoff.qa.neptune.data.base.test.persistable.object.tables.db.two.tables.Manufacturer;
import ru.tinkoff.qa.neptune.data.base.test.persistable.object.tables.db.two.tables.QManufacturer;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.fail;
import static org.testng.AssertJUnit.assertNotNull;
import static ru.tinkoff.qa.neptune.data.base.api.queries.SelectASingle.oneOf;
import static ru.tinkoff.qa.neptune.data.base.api.queries.SelectList.listOf;
import static ru.tinkoff.qa.neptune.data.base.api.queries.jdoql.JDOQLQueryParameters.byJDOQuery;

public class DeleteTest extends BaseDbOperationTest {

    @Test(groups = "positive delete")
    public void positiveDeleteTest1() {
        var jackLondon =  dataBaseSteps.select(oneOf(Author.class,
                byJDOQuery(QAuthor.class).where(qAuthor -> qAuthor
                        .firstName.eq("Jack")
                        .and(qAuthor.lastName.eq("London")))));

        var mazda = dataBaseSteps.select(oneOf(Manufacturer.class,
                byJDOQuery(QManufacturer.class).where(qManufacturer -> qManufacturer.name.eq("Mazda"))));

        var deleted = dataBaseSteps.delete(jackLondon, mazda);

        assertThat(deleted, hasSize(2));
        assertNull(dataBaseSteps.select(oneOf(Author.class,
                byJDOQuery(QAuthor.class).where(qAuthor -> qAuthor
                        .firstName.eq("Jack")
                        .and(qAuthor.lastName.eq("London"))))));

        assertNull(dataBaseSteps.select(oneOf(Manufacturer.class,
                byJDOQuery(QManufacturer.class).where(qManufacturer -> qManufacturer.name.eq("Mazda")))));
    }

    @Test(groups = "positive delete")
    public void positiveDeleteTest2() {
        var deleted = dataBaseSteps.delete(oneOf(Author.class,
                byJDOQuery(QAuthor.class).where(qAuthor -> qAuthor
                        .firstName.eq("Mikhail")
                        .and(qAuthor.lastName.eq("Bulgakov")))));

        var deleted2 = dataBaseSteps.delete(oneOf(Manufacturer.class,
                byJDOQuery(QManufacturer.class).where(qManufacturer -> qManufacturer.name.eq("Fiat"))));

        assertNotNull(deleted);
        assertNotNull(deleted2);

        assertNull(dataBaseSteps.select(oneOf(Author.class,
                byJDOQuery(QAuthor.class).where(qAuthor -> qAuthor
                        .firstName.eq("Mikhail")
                        .and(qAuthor.lastName.eq("Bulgakov"))))));

        assertNull(dataBaseSteps.select(oneOf(Manufacturer.class,
                byJDOQuery(QManufacturer.class).where(qManufacturer -> qManufacturer.name.eq("Fiat")))));
    }

    @Test(groups = "positive delete")
    public void positiveDeleteTest3() {
        var deleted = dataBaseSteps.delete(listOf(Author.class,
                byJDOQuery(QAuthor.class).where(qAuthor -> qAuthor
                        .firstName.eq("Honoré")
                        .and(qAuthor.lastName.eq("de Balzac")))));

        var deleted2 = dataBaseSteps.delete(listOf(Manufacturer.class,
                byJDOQuery(QManufacturer.class).where(qManufacturer -> qManufacturer.name.eq("Skoda"))));

        assertThat(deleted, hasSize(1));
        assertThat(deleted2, hasSize(1));

        assertNull(dataBaseSteps.select(oneOf(Author.class,
                byJDOQuery(QAuthor.class).where(qAuthor -> qAuthor
                        .firstName.eq("Honoré")
                        .and(qAuthor.lastName.eq("de Balzac"))))));

        assertNull(dataBaseSteps.select(oneOf(Manufacturer.class,
                byJDOQuery(QManufacturer.class).where(qManufacturer -> qManufacturer.name.eq("Skoda")))));
    }

    @Test(dependsOnGroups = "positive delete")
    public void negativeTest1() {
        var lermontov =  dataBaseSteps.select(oneOf(Author.class,
                byJDOQuery(QAuthor.class).where(qAuthor -> qAuthor
                        .firstName.eq("Mikhail") //this deleting is supposed to be failed because if
                        .and(qAuthor.lastName.eq("Lermontov"))))); //referential policy violation

        var nissan = dataBaseSteps.select(oneOf(Manufacturer.class, //this deleting is supposed to be successful
                byJDOQuery(QManufacturer.class).where(qManufacturer -> qManufacturer.name.eq("Nissan"))));

        try {
            dataBaseSteps.delete(nissan, lermontov);
        }
        catch (Exception e) {
            assertNotNull(dataBaseSteps.select(oneOf(Author.class,
                    byJDOQuery(QAuthor.class).where(qAuthor -> qAuthor
                            .firstName.eq("Mikhail")
                            .and(qAuthor.lastName.eq("Lermontov"))))));

            assertNotNull(dataBaseSteps.select(oneOf(Manufacturer.class,
                    byJDOQuery(QManufacturer.class).where(qManufacturer -> qManufacturer.name.eq("Nissan")))));

            return;
        }

        fail("Exception was expected");
    }

    @Test(dependsOnGroups = "positive delete")
    public void insertAfterDelete() {
        var ferrari = dataBaseSteps.insert(new Manufacturer().setName("Ferrari"));

        assertNotNull(ferrari);
        assertNotNull(dataBaseSteps.select(oneOf(Manufacturer.class,
                byJDOQuery(QManufacturer.class).where(qManufacturer -> qManufacturer.name.eq("Ferrari")))));

        ferrari = dataBaseSteps.delete(ferrari);

        assertNotNull(ferrari);
        assertNull(dataBaseSteps.select(oneOf(Manufacturer.class,
                byJDOQuery(QManufacturer.class).where(qManufacturer -> qManufacturer.name.eq("Ferrari")))));

        ferrari = dataBaseSteps.insert(ferrari);
        assertNotNull(ferrari);
        assertNotNull(dataBaseSteps.select(oneOf(Manufacturer.class,
                byJDOQuery(QManufacturer.class).where(qManufacturer -> qManufacturer.name.eq("Ferrari")))));
    }
}
