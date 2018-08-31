package ru.tinkoff.qa.neptune.data.base.test.persistable.object.operations.select;

import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.data.base.api.query.*;
import ru.tinkoff.qa.neptune.data.base.api.test.*;
import ru.tinkoff.qa.neptune.data.base.test.persistable.object.operations.BaseDbOperationTest;

import java.time.Duration;
import java.util.List;
import java.util.function.Supplier;

import static java.lang.System.currentTimeMillis;
import static java.time.Duration.ofSeconds;
import static java.util.stream.Collectors.toList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static ru.tinkoff.qa.neptune.core.api.StoryWriter.condition;
import static ru.tinkoff.qa.neptune.data.base.api.persistence.data.PersistenceManagerFactoryStore.getPersistenceManagerFactory;
import static ru.tinkoff.qa.neptune.data.base.api.properties.WaitingForSelectionResultTimeUnitProperty.DEFAULT_WAITING_FOR_SELECTION_RESULT_TIME_UNIT_PROPERTY;
import static ru.tinkoff.qa.neptune.data.base.api.properties.WaitingForSelectionResultTimeValueProperty.DEFAULT_WAITING_FOR_SELECTION_RESULT_TIME_VALUE_PROPERTY;
import static ru.tinkoff.qa.neptune.data.base.api.query.SelectListByIdsSupplier.listOfTypeByIds;
import static ru.tinkoff.qa.neptune.data.base.api.query.SelectSingleObjectByIdSupplier.aSingleOfTypeById;

@SuppressWarnings("ConstantConditions")
public class SelectByIds extends BaseDbOperationTest {

    private static final Supplier<NothingIsSelectedException> TEST_SUPPLIER = () ->
            new NothingIsSelectedException("Test exception");

    @Test
    public void selectListTest() {
        List<Catalog> catalogItems = dataBaseSteps.get(listOfTypeByIds(Catalog.class, -3, 1, 2));
        assertThat(catalogItems, hasSize(2));
        assertThat(catalogItems.stream().map(Catalog::getRecordId).collect(toList()),
                contains(1, 2));
    }

    @Test
    public void selectOneTest() {
        Catalog catalogItem = dataBaseSteps.get(aSingleOfTypeById(Catalog.class, 2));
        assertThat(catalogItem.getRecordId(), is(2));
        assertThat(catalogItem.getPublisher().getName(), is("Simon & Schuster"));
    }

    @Test
    public void selectListTestByCondition() {
        List<Catalog> catalogItems = dataBaseSteps.get(listOfTypeByIds(Catalog.class, -3, 1, 2)
                .withCondition(condition("A book with title `Ruslan and Ludmila`",
                        catalog -> catalog.getBook().getName().equalsIgnoreCase("ruslan and ludmila"))));
        assertThat(catalogItems, hasSize(1));
        Catalog catalogItem = catalogItems.get(0);
        assertThat(catalogItem.getBook().getName(), equalTo("Ruslan and Ludmila"));
    }

    @Test
    public void selectOneTestByCondition() {
        Author author = dataBaseSteps.get(aSingleOfTypeById(Author.class, 1)
                .withCondition(condition("Wrote `Ruslan and Ludmila`", author1 ->
                        null != author1.getBooks().stream()
                                .filter(book -> "ruslan and ludmila".equalsIgnoreCase(book.getName()))
                                .findFirst().orElse(null))));
        assertThat(author.getLastName(), equalTo("Pushkin"));
    }

    @Test
    public void selectEmptyListTestWithDefaultTime() {
        long start = currentTimeMillis();
        List<Catalog> catalogItems = dataBaseSteps.get(listOfTypeByIds(Catalog.class, -1, -2));
        long end = currentTimeMillis();

        Duration fiveSeconds = ofSeconds(5);
        assertThat(catalogItems, hasSize(0));
        assertThat(end - start, greaterThanOrEqualTo(fiveSeconds.toMillis()));
        assertThat(end - start - fiveSeconds.toMillis(), lessThanOrEqualTo(700L));
    }

    @Test
    public void selectNullTestWithDefaultTime() {
        long start = currentTimeMillis();
        Catalog catalogItem = dataBaseSteps.get(aSingleOfTypeById(Catalog.class, -2));
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
        List<Catalog> catalogItems = dataBaseSteps.get(listOfTypeByIds(Catalog.class, -1, -2)
                .withTimeToGetValue(sixSeconds));
        long end = currentTimeMillis();

        assertThat(catalogItems, hasSize(0));
        assertThat(end - start, greaterThanOrEqualTo(sixSeconds.toMillis()));
        assertThat(end - start - sixSeconds.toMillis(), lessThanOrEqualTo(700L));
    }

    @Test
    public void selectNullByIdWithDefinedTime() {
        Duration sixSeconds = ofSeconds(6);
        long start = currentTimeMillis();
        Catalog catalogItem = dataBaseSteps.get(aSingleOfTypeById(Catalog.class, -2)
                .withTimeToGetValue(sixSeconds));
        long end = currentTimeMillis();

        assertThat(catalogItem, nullValue());
        assertThat(end - start, greaterThanOrEqualTo(sixSeconds.toMillis()));
        assertThat(end - start - sixSeconds.toMillis(), lessThanOrEqualTo(700L));
    }

    @Test
    public void selectEmptyListByIdyWithTimeDefinedByProperty() {
        System.setProperty(DEFAULT_WAITING_FOR_SELECTION_RESULT_TIME_UNIT_PROPERTY.getPropertyName(), "SECONDS");
        System.setProperty(DEFAULT_WAITING_FOR_SELECTION_RESULT_TIME_VALUE_PROPERTY.getPropertyName(), "2");

        Duration twoSeconds = ofSeconds(2);
        long start = currentTimeMillis();
        List<Catalog> catalogItems = dataBaseSteps.get(listOfTypeByIds(Catalog.class, -1, -2));
        long end = currentTimeMillis();

        try {
            assertThat(catalogItems, hasSize(0));
            assertThat(end - start, greaterThanOrEqualTo(twoSeconds.toMillis()));
            assertThat(end - start - twoSeconds.toMillis(), lessThanOrEqualTo(700L));
        }
        finally {
            System.getProperties().remove(DEFAULT_WAITING_FOR_SELECTION_RESULT_TIME_UNIT_PROPERTY.getPropertyName());
            System.getProperties().remove(DEFAULT_WAITING_FOR_SELECTION_RESULT_TIME_VALUE_PROPERTY.getPropertyName());
        }
    }

    @Test
    public void selectNullByIdWithTimeDefinedByProperty() {
        System.setProperty(DEFAULT_WAITING_FOR_SELECTION_RESULT_TIME_UNIT_PROPERTY.getPropertyName(), "SECONDS");
        System.setProperty(DEFAULT_WAITING_FOR_SELECTION_RESULT_TIME_VALUE_PROPERTY.getPropertyName(), "2");

        Duration twoSeconds = ofSeconds(2);
        long start = currentTimeMillis();
        Catalog catalogItem = dataBaseSteps.get(aSingleOfTypeById(Catalog.class, -2));
        long end = currentTimeMillis();

        try {
            assertThat(catalogItem, nullValue());
            assertThat(end - start, greaterThanOrEqualTo(twoSeconds.toMillis()));
            assertThat(end - start - twoSeconds.toMillis(), lessThanOrEqualTo(700L));
        }
        finally {
            System.getProperties().remove(DEFAULT_WAITING_FOR_SELECTION_RESULT_TIME_UNIT_PROPERTY.getPropertyName());
            System.getProperties().remove(DEFAULT_WAITING_FOR_SELECTION_RESULT_TIME_VALUE_PROPERTY.getPropertyName());
        }
    }

    @Test
    public void selectEmptyListByIdAndConditionWithDefaultTime() {
        long start = currentTimeMillis();
        List<Catalog> catalogItems = dataBaseSteps.get(listOfTypeByIds(Catalog.class, 1)
                .withCondition(condition("ISBN is 0-671-73246-3",
                        catalog -> catalog.getIsbn().equals("0-671-73246-3"))));
        long end = currentTimeMillis();

        Duration fiveSeconds = ofSeconds(5);
        assertThat(catalogItems, hasSize(0));
        assertThat(end - start, greaterThanOrEqualTo(fiveSeconds.toMillis()));
        assertThat(end - start - fiveSeconds.toMillis(), lessThanOrEqualTo(700L));
    }

    @Test
    public void selectNullByIdAndConditionWithDefaultTime() {
        long start = currentTimeMillis();
        Catalog catalogItem = dataBaseSteps.get(aSingleOfTypeById(Catalog.class, 1)
                .withCondition(condition("ISBN is 0-671-73246-3",
                        catalog -> catalog.getIsbn().equals("0-671-73246-3"))));
        long end = currentTimeMillis();

        Duration fiveSeconds = ofSeconds(5);
        assertThat(catalogItem, nullValue());
        assertThat(end - start, greaterThanOrEqualTo(fiveSeconds.toMillis()));
        assertThat(end - start - fiveSeconds.toMillis(), lessThanOrEqualTo(700L));
    }

    @Test
    public void selectEmptyListByIdAndConditionWithDefinedTime() {
        Duration sixSeconds = ofSeconds(6);
        long start = currentTimeMillis();
        List<Catalog> catalogItems = dataBaseSteps.get(listOfTypeByIds(Catalog.class, 1)
                .withCondition(condition("ISBN is 0-671-73246-3",
                        catalog -> catalog.getIsbn().equals("0-671-73246-3")))
                .withTimeToGetValue(sixSeconds));
        long end = currentTimeMillis();

        assertThat(catalogItems, hasSize(0));
        assertThat(end - start, greaterThanOrEqualTo(sixSeconds.toMillis()));
        assertThat(end - start - sixSeconds.toMillis(), lessThanOrEqualTo(700L));
    }

    @Test
    public void selectNullByIdAndConditionWithDefinedTime() {
        Duration sixSeconds = ofSeconds(6);
        long start = currentTimeMillis();
        Catalog catalogItem = dataBaseSteps.get(aSingleOfTypeById(Catalog.class, 1)
                .withCondition(condition("ISBN is 0-671-73246-3",
                        catalog -> catalog.getIsbn().equals("0-671-73246-3")))
                .withTimeToGetValue(sixSeconds));
        long end = currentTimeMillis();

        assertThat(catalogItem, nullValue());
        assertThat(end - start, greaterThanOrEqualTo(sixSeconds.toMillis()));
        assertThat(end - start - sixSeconds.toMillis(), lessThanOrEqualTo(700L));
    }

    @Test
    public void selectEmptyListByIdAndConditionWithTimeDefinedByProperty() {
        System.setProperty(DEFAULT_WAITING_FOR_SELECTION_RESULT_TIME_UNIT_PROPERTY.getPropertyName(), "SECONDS");
        System.setProperty(DEFAULT_WAITING_FOR_SELECTION_RESULT_TIME_VALUE_PROPERTY.getPropertyName(), "2");

        Duration twoSeconds = ofSeconds(2);
        long start = currentTimeMillis();
        List<Catalog> catalogItems = dataBaseSteps.get(listOfTypeByIds(Catalog.class, 1)
                .withCondition(condition("ISBN is 0-671-73246-3",
                        catalog -> catalog.getIsbn().equals("0-671-73246-3"))));
        long end = currentTimeMillis();

        try {
            assertThat(catalogItems, hasSize(0));
            assertThat(end - start, greaterThanOrEqualTo(twoSeconds.toMillis()));
            assertThat(end - start - twoSeconds.toMillis(), lessThanOrEqualTo(500L));
        }
        finally {
            System.getProperties().remove(DEFAULT_WAITING_FOR_SELECTION_RESULT_TIME_UNIT_PROPERTY.getPropertyName());
            System.getProperties().remove(DEFAULT_WAITING_FOR_SELECTION_RESULT_TIME_VALUE_PROPERTY.getPropertyName());
        }
    }

    @Test
    public void selectNullByIdAndConditionWithTimeDefinedByProperty() {
        System.setProperty(DEFAULT_WAITING_FOR_SELECTION_RESULT_TIME_UNIT_PROPERTY.getPropertyName(), "SECONDS");
        System.setProperty(DEFAULT_WAITING_FOR_SELECTION_RESULT_TIME_VALUE_PROPERTY.getPropertyName(), "2");

        Duration twoSeconds = ofSeconds(2);
        long start = currentTimeMillis();
        Catalog catalogItem = dataBaseSteps.get(aSingleOfTypeById(Catalog.class, 1)
                .withCondition(condition("ISBN is 0-671-73246-3",
                        catalog -> catalog.getIsbn().equals("0-671-73246-3"))));
        long end = currentTimeMillis();

        try {
            assertThat(catalogItem, nullValue());
            assertThat(end - start, greaterThanOrEqualTo(twoSeconds.toMillis()));
            assertThat(end - start - twoSeconds.toMillis(), lessThanOrEqualTo(500L));
        }
        finally {
            System.getProperties().remove(DEFAULT_WAITING_FOR_SELECTION_RESULT_TIME_UNIT_PROPERTY.getPropertyName());
            System.getProperties().remove(DEFAULT_WAITING_FOR_SELECTION_RESULT_TIME_VALUE_PROPERTY.getPropertyName());
        }
    }

    @Test(expectedExceptions = NothingIsSelectedException.class, expectedExceptionsMessageRegExp = "Test exception")
    public void selectEmptyListByIdWithExceptionThrowing() {
        dataBaseSteps.get(listOfTypeByIds(Catalog.class, -1)
                .toThrowOnEmptyResult(TEST_SUPPLIER));
    }

    @Test(expectedExceptions = NothingIsSelectedException.class, expectedExceptionsMessageRegExp = "Test exception")
    public void selectNullByIdWithExceptionThrowing() {
        dataBaseSteps.get(aSingleOfTypeById(Catalog.class, -1)
                .toThrowOnEmptyResult(TEST_SUPPLIER));
    }

    @Test(expectedExceptions = NothingIsSelectedException.class, expectedExceptionsMessageRegExp = "Test exception")
    public void selectEmptyListByIdAndConditionWithExceptionThrowing() {
        dataBaseSteps.get(listOfTypeByIds(Catalog.class, 1)
                .withCondition(condition("ISBN is 0-671-73246-3",
                        catalog -> catalog.getIsbn().equals("0-671-73246-3")))
                .toThrowOnEmptyResult(TEST_SUPPLIER));
    }

    @Test(expectedExceptions = NothingIsSelectedException.class, expectedExceptionsMessageRegExp = "Test exception")
    public void selectNullByIdAndConditionWithExceptionThrowing() {
        dataBaseSteps.get(aSingleOfTypeById(Catalog.class, 1)
                .withCondition(condition("ISBN is 0-671-73246-3",
                        catalog -> catalog.getIsbn().equals("0-671-73246-3")))
                .toThrowOnEmptyResult(TEST_SUPPLIER));
    }

    @Test
    public void selectOfListWithConnectionChangeByName() {
        SelectListByIdsSupplier<Catalog> query = listOfTypeByIds(Catalog.class, 1);

        try {
            assertThat(dataBaseSteps.get(query.usePersistenceUnit(TEST_BASE2)), hasSize(0));
            assertThat(dataBaseSteps.get(query.useDefaultPersistenceUnit()), hasSize(1));
        }
        finally {
            dataBaseSteps.switchToDefault();
        }
    }

    @Test
    public void selectOfOneWithConnectionChangeByName() {
        SelectSingleObjectByIdSupplier<Catalog> query = aSingleOfTypeById(Catalog.class, 1);

        try {
            assertThat(dataBaseSteps.get(query.usePersistenceUnit(TEST_BASE2)), nullValue());
            assertThat(dataBaseSteps.get(query.useDefaultPersistenceUnit()), not(nullValue()));
        }
        finally {
            dataBaseSteps.switchToDefault();
        }
    }

    @Test
    public void selectOfListWithConnectionChangeByPersistenceManagerFactory() {
        SelectListByIdsSupplier<Catalog> query = listOfTypeByIds(Catalog.class, 1);

        try {
            assertThat(dataBaseSteps.get(query.
                    usePersistenceUnit(getPersistenceManagerFactory(TEST_BASE2, true))), hasSize(0));
            assertThat(dataBaseSteps.get(query.useDefaultPersistenceUnit()), hasSize(1));
        }
        finally {
            dataBaseSteps.switchToDefault();
        }
    }

    @Test
    public void selectOfOneWithConnectionChangeByPersistenceManagerFactory() {
        SelectSingleObjectByIdSupplier<Catalog> query = aSingleOfTypeById(Catalog.class, 1);

        try {
            assertThat(dataBaseSteps.get(query
                    .usePersistenceUnit(getPersistenceManagerFactory(TEST_BASE2, true))), nullValue());
            assertThat(dataBaseSteps.get(query.useDefaultPersistenceUnit()), not(nullValue()));
        }
        finally {
            dataBaseSteps.switchToDefault();
        }
    }
}
