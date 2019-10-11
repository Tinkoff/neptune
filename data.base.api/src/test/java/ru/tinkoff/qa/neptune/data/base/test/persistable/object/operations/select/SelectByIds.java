package ru.tinkoff.qa.neptune.data.base.test.persistable.object.operations.select;

import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.data.base.api.NothingIsSelectedException;
import ru.tinkoff.qa.neptune.data.base.test.persistable.object.operations.BaseDbOperationTest;
import ru.tinkoff.qa.neptune.data.base.test.persistable.object.operations.ConnectionDataSupplierForTestBase2;
import ru.tinkoff.qa.neptune.data.base.test.persistable.object.tables.Author;
import ru.tinkoff.qa.neptune.data.base.test.persistable.object.tables.Book;
import ru.tinkoff.qa.neptune.data.base.test.persistable.object.tables.Catalog;
import ru.tinkoff.qa.neptune.data.base.test.persistable.object.tables.Publisher;

import java.time.Duration;
import java.util.function.Supplier;

import static java.lang.System.currentTimeMillis;
import static java.time.Duration.ofSeconds;
import static java.util.stream.Collectors.toList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static ru.tinkoff.qa.neptune.data.base.api.connection.data.DBConnectionStore.getKnownConnection;
import static ru.tinkoff.qa.neptune.data.base.api.properties.WaitingForQueryResultDuration.QueryTimeUnitProperties.WAITING_FOR_SELECTION_RESULT_TIME_UNIT;
import static ru.tinkoff.qa.neptune.data.base.api.properties.WaitingForQueryResultDuration.QueryTimeValueProperties.WAITING_FOR_SELECTION_RESULT_TIME_VALUE;
import static ru.tinkoff.qa.neptune.data.base.api.query.GetSelectedFunction.selected;
import static ru.tinkoff.qa.neptune.data.base.api.query.SelectListGetSupplier.listOfTypeByIds;
import static ru.tinkoff.qa.neptune.data.base.api.query.SelectOneGetSupplier.aSingleOfTypeById;

public class SelectByIds extends BaseDbOperationTest {

    private static final Supplier<NothingIsSelectedException> TEST_SUPPLIER = () ->
            new NothingIsSelectedException("Test exception");

    @Test
    public void selectListTest() {
        var publisherItems = dataBaseSteps.get(selected(listOfTypeByIds(Publisher.class, -3, 1, 2)));
        assertThat(publisherItems, hasSize(2));
        assertThat(publisherItems.stream().map(Publisher::getName).collect(toList()),
                contains("Bergh Publishing", "Simon & Schuster"));
    }

    @Test
    public void selectOneTest() {
        var catalogItem = dataBaseSteps.get(selected(aSingleOfTypeById(Publisher.class, 1)));
        assertThat(catalogItem.getName(), is("Bergh Publishing"));
    }

    @Test
    public void selectListTestByCondition() {
        var publishers = dataBaseSteps.get(selected(
                listOfTypeByIds(Publisher.class, 1, 2, -1)
                        .criteria("Has name Bergh Publishing", publisher -> publisher
                                .getName().equalsIgnoreCase("bergh Publishing"))));
        assertThat(publishers, hasSize(1));
        assertThat(publishers.get(0).getName(), equalTo("Bergh Publishing"));
    }

    @Test
    public void selectOneTestByCondition() {
        var author = dataBaseSteps.get(selected(aSingleOfTypeById(Author.class, 1)
                .criteria("Wrote `Ruslan and Ludmila`", author1 -> null != author1.getBooks().stream()
                                .filter(book -> "ruslan and ludmila".equalsIgnoreCase(book.getName()))
                                .findFirst().orElse(null))));
        assertThat(author.getLastName(), equalTo("Pushkin"));
    }

    @Test
    public void selectEmptyListTestWithDefaultTime() {
        long start = currentTimeMillis();
        var catalogItems = dataBaseSteps.get(selected(listOfTypeByIds(Catalog.class, -1, -2)));
        long end = currentTimeMillis();

        Duration fiveSeconds = ofSeconds(5);
        assertThat(catalogItems, hasSize(0));
        assertThat(end - start, greaterThanOrEqualTo(fiveSeconds.toMillis()));
        assertThat(end - start - fiveSeconds.toMillis(), lessThanOrEqualTo(700L));
    }

    @Test
    public void selectNullTestWithDefaultTime() {
        long start = currentTimeMillis();
        var catalogItem = dataBaseSteps.get(selected(aSingleOfTypeById(Catalog.class, -2)));
        long end = currentTimeMillis();

        Duration fiveSeconds = ofSeconds(5);
        assertThat(catalogItem, nullValue());
        assertThat(end - start, greaterThanOrEqualTo(fiveSeconds.toMillis()));
        assertThat(end - start - fiveSeconds.toMillis(), lessThanOrEqualTo(700L));
    }

    @Test
    public void selectEmptyListByIdWithDefinedTime() {
        Duration sixSeconds = ofSeconds(6);
        long start = currentTimeMillis();
        var catalogItems = dataBaseSteps.get(selected(listOfTypeByIds(Catalog.class, -1, -2)
                .timeOut(sixSeconds)));
        long end = currentTimeMillis();

        assertThat(catalogItems, hasSize(0));
        assertThat(end - start, greaterThanOrEqualTo(sixSeconds.toMillis()));
        assertThat(end - start - sixSeconds.toMillis(), lessThanOrEqualTo(700L));
    }

    @Test
    public void selectNullByIdWithDefinedTime() {
        Duration sixSeconds = ofSeconds(6);
        long start = currentTimeMillis();
        var catalogItem = dataBaseSteps.get(selected(aSingleOfTypeById(Catalog.class, -2).timeOut(sixSeconds)));
        long end = currentTimeMillis();

        assertThat(catalogItem, nullValue());
        assertThat(end - start, greaterThanOrEqualTo(sixSeconds.toMillis()));
        assertThat(end - start - sixSeconds.toMillis(), lessThanOrEqualTo(700L));
    }

    @Test
    public void selectEmptyListByIdyWithTimeDefinedByProperty() {
        WAITING_FOR_SELECTION_RESULT_TIME_UNIT.accept("SECONDS");
        WAITING_FOR_SELECTION_RESULT_TIME_VALUE.accept("2");

        Duration twoSeconds = ofSeconds(2);
        long start = currentTimeMillis();
        var catalogItems = dataBaseSteps.get(selected(listOfTypeByIds(Catalog.class, -1, -2)));
        long end = currentTimeMillis();

        try {
            assertThat(catalogItems, hasSize(0));
            assertThat(end - start, greaterThanOrEqualTo(twoSeconds.toMillis()));
            assertThat(end - start - twoSeconds.toMillis(), lessThanOrEqualTo(700L));
        }
        finally {
            System.getProperties().remove(WAITING_FOR_SELECTION_RESULT_TIME_UNIT.getPropertyName());
            System.getProperties().remove(WAITING_FOR_SELECTION_RESULT_TIME_VALUE.getPropertyName());
        }
    }

    @Test
    public void selectNullByIdWithTimeDefinedByProperty() {
        WAITING_FOR_SELECTION_RESULT_TIME_UNIT.accept("SECONDS");
        WAITING_FOR_SELECTION_RESULT_TIME_VALUE.accept("2");

        Duration twoSeconds = ofSeconds(2);
        long start = currentTimeMillis();
        var catalogItem = dataBaseSteps.get(selected(aSingleOfTypeById(Catalog.class, -2)));
        long end = currentTimeMillis();

        try {
            assertThat(catalogItem, nullValue());
            assertThat(end - start, greaterThanOrEqualTo(twoSeconds.toMillis()));
            assertThat(end - start - twoSeconds.toMillis(), lessThanOrEqualTo(700L));
        }
        finally {
            System.getProperties().remove(WAITING_FOR_SELECTION_RESULT_TIME_UNIT.getPropertyName());
            System.getProperties().remove(WAITING_FOR_SELECTION_RESULT_TIME_VALUE.getPropertyName());
        }
    }

    @Test
    public void selectEmptyListByIdAndConditionWithDefaultTime() {
        long start = currentTimeMillis();
        var publishers = dataBaseSteps.get(selected(listOfTypeByIds(Publisher.class, 1)
                .criteria("Has name Simon & Schuster", publisher -> publisher
                        .getName().equals("Simon & Schuster"))));
        long end = currentTimeMillis();

        Duration fiveSeconds = ofSeconds(5);
        assertThat(publishers, hasSize(0));
        assertThat(end - start, greaterThanOrEqualTo(fiveSeconds.toMillis()));
        assertThat(end - start - fiveSeconds.toMillis(), lessThanOrEqualTo(700L));
    }

    @Test
    public void selectNullByIdAndConditionWithDefaultTime() {
        long start = currentTimeMillis();
        var publisher = dataBaseSteps.get(selected(aSingleOfTypeById(Publisher.class, 1)
                .criteria("Has name Simon & Schuster", publisherItem -> publisherItem
                        .getName().equals("Simon & Schuster"))));
        long end = currentTimeMillis();

        Duration fiveSeconds = ofSeconds(5);
        assertThat(publisher, nullValue());
        assertThat(end - start, greaterThanOrEqualTo(fiveSeconds.toMillis()));
        assertThat(end - start - fiveSeconds.toMillis(), lessThanOrEqualTo(700L));
    }

    @Test
    public void selectEmptyListByIdAndConditionWithDefinedTime() {
        Duration sixSeconds = ofSeconds(6);
        long start = currentTimeMillis();
        var publishers = dataBaseSteps.get(selected(listOfTypeByIds(Publisher.class, 1)
                .criteria("Has name Simon & Schuster", publisher -> publisher
                        .getName().equals("Simon & Schuster"))
                .timeOut(sixSeconds)));
        long end = currentTimeMillis();

        assertThat(publishers, hasSize(0));
        assertThat(end - start, greaterThanOrEqualTo(sixSeconds.toMillis()));
        assertThat(end - start - sixSeconds.toMillis(), lessThanOrEqualTo(700L));
    }

    @Test
    public void selectNullByIdAndConditionWithDefinedTime() {
        Duration sixSeconds = ofSeconds(6);
        long start = currentTimeMillis();
        var publisher = dataBaseSteps.get(selected(aSingleOfTypeById(Publisher.class, 1)
                .criteria("Has name Simon & Schuster", publisherItem -> publisherItem
                        .getName().equals("Simon & Schuster"))
                .timeOut(sixSeconds)));
        long end = currentTimeMillis();

        assertThat(publisher, nullValue());
        assertThat(end - start, greaterThanOrEqualTo(sixSeconds.toMillis()));
        assertThat(end - start - sixSeconds.toMillis(), lessThanOrEqualTo(700L));
    }

    @Test
    public void selectEmptyListByIdAndConditionWithTimeDefinedByProperty() {
        WAITING_FOR_SELECTION_RESULT_TIME_UNIT.accept( "SECONDS");
        WAITING_FOR_SELECTION_RESULT_TIME_VALUE.accept("2");

        Duration twoSeconds = ofSeconds(2);
        long start = currentTimeMillis();
        var publishers = dataBaseSteps.get(selected(listOfTypeByIds(Publisher.class, 1)
                .criteria("Has name Simon & Schuster", publisher -> publisher
                        .getName().equals("Simon & Schuster"))));
        long end = currentTimeMillis();

        try {
            assertThat(publishers, hasSize(0));
            assertThat(end - start, greaterThanOrEqualTo(twoSeconds.toMillis()));
            assertThat(end - start - twoSeconds.toMillis(), lessThanOrEqualTo(500L));
        }
        finally {
            System.getProperties().remove(WAITING_FOR_SELECTION_RESULT_TIME_UNIT.getPropertyName());
            System.getProperties().remove(WAITING_FOR_SELECTION_RESULT_TIME_VALUE.getPropertyName());
        }
    }

    @Test
    public void selectNullByIdAndConditionWithTimeDefinedByProperty() {
        WAITING_FOR_SELECTION_RESULT_TIME_UNIT.accept("SECONDS");
        WAITING_FOR_SELECTION_RESULT_TIME_VALUE.accept("2");

        Duration twoSeconds = ofSeconds(2);
        long start = currentTimeMillis();
        var publisher = dataBaseSteps.get(selected(aSingleOfTypeById(Publisher.class, 1)
                .criteria("Has name Simon & Schuster", publisherItem -> publisherItem
                        .getName().equals("Simon & Schuster"))));
        long end = currentTimeMillis();

        try {
            assertThat(publisher, nullValue());
            assertThat(end - start, greaterThanOrEqualTo(twoSeconds.toMillis()));
            assertThat(end - start - twoSeconds.toMillis(), lessThanOrEqualTo(500L));
        }
        finally {
            System.getProperties().remove(WAITING_FOR_SELECTION_RESULT_TIME_UNIT.getPropertyName());
            System.getProperties().remove(WAITING_FOR_SELECTION_RESULT_TIME_VALUE.getPropertyName());
        }
    }

    @Test(expectedExceptions = NothingIsSelectedException.class, expectedExceptionsMessageRegExp = "Test exception")
    public void selectEmptyListByIdWithExceptionThrowing() {
        dataBaseSteps.get(selected(listOfTypeByIds(Catalog.class, -1)
                .throwWhenResultEmpty(TEST_SUPPLIER)));
    }

    @Test(expectedExceptions = NothingIsSelectedException.class, expectedExceptionsMessageRegExp = "Test exception")
    public void selectNullByIdWithExceptionThrowing() {
        dataBaseSteps.get(selected(aSingleOfTypeById(Catalog.class, -1)
                .throwWhenResultEmpty(TEST_SUPPLIER)));
    }

    @Test(expectedExceptions = NothingIsSelectedException.class, expectedExceptionsMessageRegExp = "Test exception")
    public void selectEmptyListByIdAndConditionWithExceptionThrowing() {
        dataBaseSteps.get(selected(listOfTypeByIds(Publisher.class, 1)
                .criteria("Has name Simon & Schuster", publisher -> publisher
                        .getName().equals("Simon & Schuster"))
                .throwWhenResultEmpty(TEST_SUPPLIER)));
    }

    @Test(expectedExceptions = NothingIsSelectedException.class, expectedExceptionsMessageRegExp = "Test exception")
    public void selectNullByIdAndConditionWithExceptionThrowing() {
        dataBaseSteps.get(selected(aSingleOfTypeById(Publisher.class, 1)
                .criteria("Has name Simon & Schuster", publisherItem -> publisherItem
                        .getName().equals("Simon & Schuster"))
                .throwWhenResultEmpty(TEST_SUPPLIER)));
    }

    @Test
    public void selectOfListWithConnectionDataSupplier() {
        var query = selected(listOfTypeByIds(Book.class, 1, 2))
                .useConnection(ConnectionDataSupplierForTestBase2.class);
        var query2 = selected(listOfTypeByIds(Book.class, 1, 2));

        assertThat(dataBaseSteps.get(query), hasSize(0));
        assertThat(dataBaseSteps.get(query2), hasSize(2));
    }

    @Test
    public void selectOfOneWithConnectionDataSupplier() {
        var query = selected(aSingleOfTypeById(Author.class, 1))
                .useConnection(ConnectionDataSupplierForTestBase2.class);
        var query2 = selected(aSingleOfTypeById(Author.class, 1));

        assertThat(dataBaseSteps.get(query), nullValue());
        assertThat(dataBaseSteps.get(query2), not(nullValue()));
    }

    @Test
    public void selectOfListWithConnection() {
        var query = selected(listOfTypeByIds(Author.class, 1))
                .useConnection(getKnownConnection(ConnectionDataSupplierForTestBase2.class, true));
        var query2 = selected(listOfTypeByIds(Author.class, 1));

        assertThat(dataBaseSteps.get(query), hasSize(0));
        assertThat(dataBaseSteps.get(query2), hasSize(1));
    }

    @Test
    public void selectOfOneWithConnectionChangeByPersistenceManagerFactory() {
        var query = selected(aSingleOfTypeById(Author.class, 1))
                .useConnection(getKnownConnection(ConnectionDataSupplierForTestBase2.class,true));
        var query2 = selected(aSingleOfTypeById(Author.class, 1));

        assertThat(dataBaseSteps.get(query), nullValue());
        assertThat(dataBaseSteps.get(query2), not(nullValue()));
    }
}
