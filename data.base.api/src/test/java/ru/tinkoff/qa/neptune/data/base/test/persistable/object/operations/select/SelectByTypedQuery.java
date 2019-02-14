package ru.tinkoff.qa.neptune.data.base.test.persistable.object.operations.select;

import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.data.base.api.query.GetSelectedFunction;
import ru.tinkoff.qa.neptune.data.base.api.query.NothingIsSelectedException;
import ru.tinkoff.qa.neptune.data.base.api.test.*;
import ru.tinkoff.qa.neptune.data.base.test.persistable.object.operations.BaseDbOperationTest;
import ru.tinkoff.qa.neptune.data.base.test.persistable.object.operations.ConnectionDataSupplierForTestBase2;

import java.time.Duration;
import java.util.List;
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
import static ru.tinkoff.qa.neptune.data.base.api.query.QueryBuilderFunction.ofType;
import static ru.tinkoff.qa.neptune.data.base.api.query.SelectListGetSupplier.listByQuery;
import static ru.tinkoff.qa.neptune.data.base.api.query.SelectOneGetSupplier.aSingleByQuery;

@SuppressWarnings("ConstantConditions")
public class SelectByTypedQuery extends BaseDbOperationTest {

    private static final Supplier<NothingIsSelectedException> TEST_SUPPLIER = () ->
            new NothingIsSelectedException("Test exception");

    @Test
    public void selectListTestWithoutAnyCondition() {
        assertThat(dataBaseSteps.get(selected(listByQuery(ofType(Author.class)))),
                hasSize(greaterThanOrEqualTo(2)));
    }

    @Test
    public void selectOneTestWithoutCondition() {
        var a = dataBaseSteps.get(selected(aSingleByQuery(ofType(Author.class))));
        assertThat(a.getId(), is(1));
    }

    @Test
    public void selectListTestWithQuery() {
        var c = QCatalog.candidate();
        var catalogItems = dataBaseSteps.get(selected(listByQuery(ofType(Catalog.class)
                .where(c.book.name.eq("Ruslan and Ludmila")
                        .or(c.isbn.eq("0-671-73246-3")) //<Journey to Ixtlan
                        .or(c.book.author.lastName.eq("Pushkin")))
                .orderBy(c.isbn.desc()))));

        assertThat(catalogItems, hasSize(2));
        assertThat(catalogItems.stream().map(Catalog::getIsbn).collect(toList()),
                contains("0-930267-39-7", "0-671-73246-3"));
    }

    @Test
    public void selectOneTestWithQuery() {
        var c = QCatalog.candidate();
        var catalogItem = dataBaseSteps.get(selected(aSingleByQuery(ofType(Catalog.class)
                .where(c.book.name.eq("Ruslan and Ludmila")
                        .or(c.isbn.eq("0-671-73246-3")) //<Journey to Ixtlan
                        .or(c.book.author.lastName.eq("Pushkin")))
                .orderBy(c.isbn.desc()))));

        assertThat(catalogItem.getIsbn(), is("0-930267-39-7"));
    }

    @Test
    public void selectListTestByCondition() {
        var catalogItems = dataBaseSteps.get(selected(listByQuery(ofType(Catalog.class))
                .criteria("A book with title `Ruslan and Ludmila`", catalog -> catalog
                        .getBook().getName().equalsIgnoreCase("ruslan and ludmila"))));
        assertThat(catalogItems, hasSize(1));
        var catalogItem = catalogItems.get(0);
        assertThat(catalogItem.getBook().getName(), equalTo("Ruslan and Ludmila"));
    }

    @Test
    public void selectOneTestByCondition() {
        var author = dataBaseSteps.get(selected(aSingleByQuery(ofType(Author.class))
                .criteria("Wrote `Ruslan and Ludmila`", author1 -> null != author1.getBooks().stream()
                        .filter(book -> "ruslan and ludmila".equalsIgnoreCase(book.getName()))
                        .findFirst()
                        .orElse(null))));
        assertThat(author.getLastName(), equalTo("Pushkin"));
    }

    @Test
    public void selectListTestByQueryAndCondition() {
        var qAuthor = QAuthor.candidate();
        var qBook = QBook.candidate();
        var qCatalog = QCatalog.candidate();

        var journeyToIxtlan = dataBaseSteps.get(selected(aSingleByQuery(ofType(Book.class)
                .where(qBook.name.eq("Journey to Ixtlan")))));

        var carlosCastaneda = dataBaseSteps.get(selected(aSingleByQuery(ofType(Author.class)
                .where(qAuthor.firstName.eq("Carlos")
                        .and(qAuthor.lastName.eq("Castaneda"))))));

        var catalogItems = dataBaseSteps.get(selected(listByQuery(ofType(Catalog.class)
                .where(qCatalog.book.eq(journeyToIxtlan)))

                .criteria("Publisher is 'Simon & Schuster'",
                        catalog -> "Simon & Schuster".equals(catalog.getPublisher().getName()))));

        assertThat(catalogItems, hasSize(1));
        assertThat(catalogItems.get(0).getIsbn(), equalTo("0-671-73246-3"));
        assertThat(catalogItems.get(0).getBook().getAuthor(), equalTo(carlosCastaneda));
    }

    @Test
    public void selectOneTestByQueryAndCondition() {
        var qAuthor = QAuthor.candidate();
        var qBook = QBook.candidate();
        var qCatalog = QCatalog.candidate();

        var journeyToIxtlan = dataBaseSteps.get(selected(aSingleByQuery(ofType(Book.class)
                .where(qBook.name.eq("Journey to Ixtlan")))));

        var carlosCastaneda = dataBaseSteps.get(selected(aSingleByQuery(ofType(Author.class)
                .where(qAuthor.firstName.eq("Carlos").and(qAuthor.lastName.eq("Castaneda"))))));

        var catalogItem = dataBaseSteps.get(selected(aSingleByQuery(ofType(Catalog.class)
                .where(qCatalog.book.eq(journeyToIxtlan)
                        .and(qCatalog.book.author.eq(carlosCastaneda))))
                .criteria("Publisher is 'Simon & Schuster'",
                        catalog -> "Simon & Schuster".equals(catalog.getPublisher().getName()))));

        assertThat(catalogItem.getIsbn(), equalTo("0-671-73246-3"));
        assertThat(catalogItem.getBook().getAuthor(), equalTo(carlosCastaneda));
    }

    @Test
    public void selectEmptyListByQueryWithDefaultTime() {
        var qAuthor = QAuthor.candidate();
        var qBook = QBook.candidate();
        var qCatalog = QCatalog.candidate();

        var ruslanAndLudmila = dataBaseSteps.get(selected(aSingleByQuery(ofType(Book.class)
                .where(qBook.name.eq("Ruslan and Ludmila")))));

        var carlosCastaneda = dataBaseSteps.get(selected(aSingleByQuery(ofType(Author.class)
                .where(qAuthor.firstName.eq("Carlos")
                        .and(qAuthor.lastName.eq("Castaneda"))))));

        long start = currentTimeMillis();
        var catalogItems = dataBaseSteps.get(selected(listByQuery(ofType(Catalog.class)
                .where(qCatalog.book.eq(ruslanAndLudmila).and(qCatalog.book.author.eq(carlosCastaneda))))));
        long end = currentTimeMillis();

        Duration fiveSeconds = ofSeconds(5);
        assertThat(catalogItems, hasSize(0));
        assertThat(end - start, greaterThanOrEqualTo(fiveSeconds.toMillis()));
        assertThat(end - start - fiveSeconds.toMillis(), lessThanOrEqualTo(500L));
    }

    @Test
    public void selectNullByQueryWithDefaultTime() {
        var qAuthor = QAuthor.candidate();
        var qBook = QBook.candidate();
        var qCatalog = QCatalog.candidate();

        var ruslanAndLudmila = dataBaseSteps.get(selected(aSingleByQuery(ofType(Book.class)
                .where(qBook.name.eq("Ruslan and Ludmila")))));

        var carlosCastaneda = dataBaseSteps.get(selected(aSingleByQuery(ofType(Author.class)
                .where(qAuthor.firstName.eq("Carlos")
                        .and(qAuthor.lastName.eq("Castaneda"))))));

        long start = currentTimeMillis();
        var catalogItem = dataBaseSteps.get(selected(aSingleByQuery(ofType(Catalog.class)
                .where(qCatalog.book.eq(ruslanAndLudmila).and(qCatalog.book.author.eq(carlosCastaneda))))));
        long end = currentTimeMillis();

        Duration fiveSeconds = ofSeconds(5);
        assertThat(catalogItem, nullValue());
        assertThat(end - start, greaterThanOrEqualTo(fiveSeconds.toMillis()));
        assertThat(end - start - fiveSeconds.toMillis(), lessThanOrEqualTo(500L));
    }

    @Test
    public void selectEmptyListByQueryWithDefinedTime() {
        var qAuthor = QAuthor.candidate();
        var qBook = QBook.candidate();
        var qCatalog = QCatalog.candidate();

        var ruslanAndLudmila = dataBaseSteps.get(selected(aSingleByQuery(ofType(Book.class)
                .where(qBook.name.eq("Ruslan and Ludmila")))));

        var carlosCastaneda = dataBaseSteps.get(selected(aSingleByQuery(ofType(Author.class)
                .where(qAuthor.firstName.eq("Carlos")
                        .and(qAuthor.lastName.eq("Castaneda"))))));

        Duration sixSeconds = ofSeconds(6);
        long start = currentTimeMillis();
        var catalogItems = dataBaseSteps.get(selected(listByQuery(ofType(Catalog.class)
                .where(qCatalog.book.eq(ruslanAndLudmila).and(qCatalog.book.author.eq(carlosCastaneda))))
                .timeOut(sixSeconds)));
        long end = currentTimeMillis();

        assertThat(catalogItems, hasSize(0));
        assertThat(end - start, greaterThanOrEqualTo(sixSeconds.toMillis()));
        assertThat(end - start - sixSeconds.toMillis(), lessThanOrEqualTo(500L));
    }

    @Test
    public void selectNullByQueryWithDefinedTime() {
        var qAuthor = QAuthor.candidate();
        var qBook = QBook.candidate();
        var qCatalog = QCatalog.candidate();

        var ruslanAndLudmila = dataBaseSteps.get(selected(aSingleByQuery(ofType(Book.class)
                .where(qBook.name.eq("Ruslan and Ludmila")))));

        var carlosCastaneda = dataBaseSteps.get(selected(aSingleByQuery(ofType(Author.class)
                .where(qAuthor.firstName.eq("Carlos").and(qAuthor.lastName.eq("Castaneda"))))));

        Duration sixSeconds = ofSeconds(6);
        long start = currentTimeMillis();
        var catalogItem = dataBaseSteps.get(selected(aSingleByQuery(ofType(Catalog.class)
                .where(qCatalog.book.eq(ruslanAndLudmila).and(qCatalog.book.author.eq(carlosCastaneda))))
                .timeOut(sixSeconds)));
        long end = currentTimeMillis();

        assertThat(catalogItem, nullValue());
        assertThat(end - start, greaterThanOrEqualTo(sixSeconds.toMillis()));
        assertThat(end - start - sixSeconds.toMillis(), lessThanOrEqualTo(500L));
    }

    @Test
    public void selectEmptyListByQueryWithTimeDefinedByProperty() {
        WAITING_FOR_SELECTION_RESULT_TIME_UNIT.accept("SECONDS");
        WAITING_FOR_SELECTION_RESULT_TIME_VALUE.accept("2");

        var qAuthor = QAuthor.candidate();
        var qBook = QBook.candidate();
        var qCatalog = QCatalog.candidate();

        var ruslanAndLudmila = dataBaseSteps.get(selected(aSingleByQuery(ofType(Book.class)
                .where(qBook.name.eq("Ruslan and Ludmila")))));

        var carlosCastaneda = dataBaseSteps.get(selected(aSingleByQuery(ofType(Author.class)
                .where(qAuthor.firstName.eq("Carlos").and(qAuthor.lastName.eq("Castaneda"))))));

        Duration twoSeconds = ofSeconds(2);
        long start = currentTimeMillis();
        var catalogItems = dataBaseSteps.get(selected(listByQuery(ofType(Catalog.class)
                .where(qCatalog.book.eq(ruslanAndLudmila).and(qCatalog.book.author.eq(carlosCastaneda))))));
        long end = currentTimeMillis();

        try {
            assertThat(catalogItems, hasSize(0));
            assertThat(end - start, greaterThanOrEqualTo(twoSeconds.toMillis()));
            assertThat(end - start - twoSeconds.toMillis(), lessThanOrEqualTo(500L));
        } finally {
            System.getProperties().remove(WAITING_FOR_SELECTION_RESULT_TIME_UNIT.getPropertyName());
            System.getProperties().remove(WAITING_FOR_SELECTION_RESULT_TIME_VALUE.getPropertyName());
        }
    }

    @Test
    public void selectNullByQueryWithTimeDefinedByProperty() {
        WAITING_FOR_SELECTION_RESULT_TIME_UNIT.accept("SECONDS");
        WAITING_FOR_SELECTION_RESULT_TIME_VALUE.accept("2");

        var qAuthor = QAuthor.candidate();
        var qBook = QBook.candidate();
        var qCatalog = QCatalog.candidate();

        var ruslanAndLudmila = dataBaseSteps.get(selected(aSingleByQuery(ofType(Book.class)
                .where(qBook.name.eq("Ruslan and Ludmila")))));

        var carlosCastaneda = dataBaseSteps.get(selected(aSingleByQuery(ofType(Author.class)
                .where(qAuthor.firstName.eq("Carlos")
                        .and(qAuthor.lastName.eq("Castaneda"))))));

        Duration twoSeconds = ofSeconds(2);
        long start = currentTimeMillis();
        var catalogItem = dataBaseSteps.get(selected(aSingleByQuery(ofType(Catalog.class)
                .where(qCatalog.book.eq(ruslanAndLudmila)
                        .and(qCatalog.book.author.eq(carlosCastaneda))))));
        long end = currentTimeMillis();

        try {
            assertThat(catalogItem, nullValue());
            assertThat(end - start, greaterThanOrEqualTo(twoSeconds.toMillis()));
            assertThat(end - start - twoSeconds.toMillis(), lessThanOrEqualTo(500L));
        } finally {
            System.getProperties().remove(WAITING_FOR_SELECTION_RESULT_TIME_UNIT.getPropertyName());
            System.getProperties().remove(WAITING_FOR_SELECTION_RESULT_TIME_VALUE.getPropertyName());
        }
    }

    @Test
    public void selectEmptyListByQueryAndConditionWithDefaultTime() {
        var qAuthor = QAuthor.candidate();
        var qBook = QBook.candidate();
        var qCatalog = QCatalog.candidate();

        var ruslanAndLudmila = dataBaseSteps.get(selected(aSingleByQuery(ofType(Book.class)
                .where(qBook.name.eq("Ruslan and Ludmila")))));

        var alexanderPushkin = dataBaseSteps.get(selected(aSingleByQuery(ofType(Author.class)
                .where(qAuthor.firstName.eq("Alexander")
                        .and(qAuthor.lastName.eq("Pushkin"))))));

        long start = currentTimeMillis();
        var catalogItems = dataBaseSteps.get(selected(listByQuery(ofType(Catalog.class)
                .where(qCatalog.book.eq(ruslanAndLudmila)
                        .and(qCatalog.book.author.eq(alexanderPushkin))))
                .criteria("ISBN is 0-671-73246-3",
                        catalog -> catalog.getIsbn().equals("0-671-73246-3"))));
        long end = currentTimeMillis();

        Duration fiveSeconds = ofSeconds(5);
        assertThat(catalogItems, hasSize(0));
        assertThat(end - start, greaterThanOrEqualTo(fiveSeconds.toMillis()));
        assertThat(end - start - fiveSeconds.toMillis(), lessThanOrEqualTo(500L));
    }

    @Test
    public void selectNullByQueryAndConditionWithDefaultTime() {
        var qAuthor = QAuthor.candidate();
        var qBook = QBook.candidate();
        var qCatalog = QCatalog.candidate();

        var ruslanAndLudmila = dataBaseSteps.get(selected(aSingleByQuery(ofType(Book.class)
                .where(qBook.name.eq("Ruslan and Ludmila")))));

        var alexanderPushkin = dataBaseSteps.get(selected(aSingleByQuery(ofType(Author.class)
                .where(qAuthor.firstName.eq("Alexander")
                        .and(qAuthor.lastName.eq("Pushkin"))))));

        long start = currentTimeMillis();
        var catalogItem = dataBaseSteps.get(selected(aSingleByQuery(ofType(Catalog.class)
                .where(qCatalog.book.eq(ruslanAndLudmila)
                        .and(qCatalog.book.author.eq(alexanderPushkin))))
                .criteria("ISBN is 0-671-73246-3",
                        catalog -> catalog.getIsbn().equals("0-671-73246-3"))));
        long end = currentTimeMillis();

        Duration fiveSeconds = ofSeconds(5);
        assertThat(catalogItem, nullValue());
        assertThat(end - start, greaterThanOrEqualTo(fiveSeconds.toMillis()));
        assertThat(end - start - fiveSeconds.toMillis(), lessThanOrEqualTo(500L));
    }

    @Test
    public void selectEmptyListByQueryAndConditionWithDefinedTime() {
        var qAuthor = QAuthor.candidate();
        var qBook = QBook.candidate();
        var qCatalog = QCatalog.candidate();

        var ruslanAndLudmila = dataBaseSteps.get(selected(aSingleByQuery(ofType(Book.class)
                .where(qBook.name.eq("Ruslan and Ludmila")))));

        var alexanderPushkin = dataBaseSteps.get(selected(aSingleByQuery(ofType(Author.class)
                .where(qAuthor.firstName.eq("Alexander")
                        .and(qAuthor.lastName.eq("Pushkin"))))));

        Duration sixSeconds = ofSeconds(6);
        long start = currentTimeMillis();
        var catalogItems = dataBaseSteps.get(selected(listByQuery(ofType(Catalog.class)
                .where(qCatalog.book.eq(ruslanAndLudmila)
                        .and(qCatalog.book.author.eq(alexanderPushkin))))
                .criteria("ISBN is 0-671-73246-3",
                        catalog -> catalog.getIsbn().equals("0-671-73246-3"))
                .timeOut(sixSeconds)));
        long end = currentTimeMillis();

        assertThat(catalogItems, hasSize(0));
        assertThat(end - start, greaterThanOrEqualTo(sixSeconds.toMillis()));
        assertThat(end - start - sixSeconds.toMillis(), lessThanOrEqualTo(500L));
    }

    @Test
    public void selectNullByQueryAndConditionWithDefinedTime() {
        var qAuthor = QAuthor.candidate();
        var qBook = QBook.candidate();
        var qCatalog = QCatalog.candidate();

        var ruslanAndLudmila = dataBaseSteps.get(selected(aSingleByQuery(ofType(Book.class)
                .where(qBook.name.eq("Ruslan and Ludmila")))));

        var alexanderPushkin = dataBaseSteps.get(selected(aSingleByQuery(ofType(Author.class)
                .where(qAuthor.firstName.eq("Alexander")
                        .and(qAuthor.lastName.eq("Pushkin"))))));

        Duration sixSeconds = ofSeconds(6);
        long start = currentTimeMillis();
        var catalogItem = dataBaseSteps.get(selected(aSingleByQuery(ofType(Catalog.class)
                .where(qCatalog.book.eq(ruslanAndLudmila)
                        .and(qCatalog.book.author.eq(alexanderPushkin))))
                .criteria("ISBN is 0-671-73246-3",
                        catalog -> catalog.getIsbn().equals("0-671-73246-3"))
                .timeOut(sixSeconds)));
        long end = currentTimeMillis();

        assertThat(catalogItem, nullValue());
        assertThat(end - start, greaterThanOrEqualTo(sixSeconds.toMillis()));
        assertThat(end - start - sixSeconds.toMillis(), lessThanOrEqualTo(500L));
    }

    @Test
    public void selectEmptyListByQueryAndConditionWithTimeDefinedByProperty() {
        WAITING_FOR_SELECTION_RESULT_TIME_UNIT.accept("SECONDS");
        WAITING_FOR_SELECTION_RESULT_TIME_VALUE.accept("2");

        var qAuthor = QAuthor.candidate();
        var qBook = QBook.candidate();
        var qCatalog = QCatalog.candidate();

        var ruslanAndLudmila = dataBaseSteps.get(selected(aSingleByQuery(ofType(Book.class)
                .where(qBook.name.eq("Ruslan and Ludmila")))));

        var alexanderPushkin = dataBaseSteps.get(selected(aSingleByQuery(ofType(Author.class)
                .where(qAuthor.firstName.eq("Alexander")
                        .and(qAuthor.lastName.eq("Pushkin"))))));

        Duration twoSeconds = ofSeconds(2);
        long start = currentTimeMillis();
        var catalogItems = dataBaseSteps.get(selected(listByQuery(ofType(Catalog.class)
                .where(qCatalog.book.eq(ruslanAndLudmila)
                        .and(qCatalog.book.author.eq(alexanderPushkin))))
                .criteria("ISBN is 0-671-73246-3",
                        catalog -> catalog.getIsbn().equals("0-671-73246-3"))));
        long end = currentTimeMillis();

        try {
            assertThat(catalogItems, hasSize(0));
            assertThat(end - start, greaterThanOrEqualTo(twoSeconds.toMillis()));
            assertThat(end - start - twoSeconds.toMillis(), lessThanOrEqualTo(500L));
        } finally {
            System.getProperties().remove(WAITING_FOR_SELECTION_RESULT_TIME_UNIT.getPropertyName());
            System.getProperties().remove(WAITING_FOR_SELECTION_RESULT_TIME_VALUE.getPropertyName());
        }
    }

    @Test
    public void selectNullByQueryAndConditionWithTimeDefinedByProperty() {
        WAITING_FOR_SELECTION_RESULT_TIME_UNIT.accept("SECONDS");
        WAITING_FOR_SELECTION_RESULT_TIME_VALUE.accept("2");

        var qAuthor = QAuthor.candidate();
        var qBook = QBook.candidate();
        var qCatalog = QCatalog.candidate();

        var ruslanAndLudmila = dataBaseSteps.get(selected(aSingleByQuery(ofType(Book.class)
                .where(qBook.name.eq("Ruslan and Ludmila")))));

        var alexanderPushkin = dataBaseSteps.get(selected(aSingleByQuery(ofType(Author.class)
                .where(qAuthor.firstName.eq("Alexander")
                        .and(qAuthor.lastName.eq("Pushkin"))))));

        Duration twoSeconds = ofSeconds(2);
        long start = currentTimeMillis();
        var catalogItem = dataBaseSteps.get(selected(aSingleByQuery(ofType(Catalog.class)
                .where(qCatalog.book.eq(ruslanAndLudmila)
                        .and(qCatalog.book.author.eq(alexanderPushkin))))
                .criteria("ISBN is 0-671-73246-3",
                        catalog -> catalog.getIsbn().equals("0-671-73246-3"))));
        long end = currentTimeMillis();

        try {
            assertThat(catalogItem, nullValue());
            assertThat(end - start, greaterThanOrEqualTo(twoSeconds.toMillis()));
            assertThat(end - start - twoSeconds.toMillis(), lessThanOrEqualTo(500L));
        } finally {
            System.getProperties().remove(WAITING_FOR_SELECTION_RESULT_TIME_UNIT.getPropertyName());
            System.getProperties().remove(WAITING_FOR_SELECTION_RESULT_TIME_VALUE.getPropertyName());
        }
    }

    @Test(expectedExceptions = NothingIsSelectedException.class, expectedExceptionsMessageRegExp = "Test exception")
    public void selectEmptyListByQueryWithExceptionThrowing() {
        var qAuthor = QAuthor.candidate();
        var qBook = QBook.candidate();
        var qCatalog = QCatalog.candidate();

        var ruslanAndLudmila = dataBaseSteps.get(selected(aSingleByQuery(ofType(Book.class)
                .where(qBook.name.eq("Ruslan and Ludmila")))));

        var carlosCastaneda = dataBaseSteps.get(selected(aSingleByQuery(ofType(Author.class)
                .where(qAuthor.firstName.eq("Carlos")
                        .and(qAuthor.lastName.eq("Castaneda"))))));

        dataBaseSteps.get(selected(listByQuery(ofType(Catalog.class)
                .where(qCatalog.book.eq(ruslanAndLudmila)
                        .and(qCatalog.book.author.eq(carlosCastaneda))))
                .throwWhenResultEmpty(TEST_SUPPLIER)));
    }

    @Test(expectedExceptions = NothingIsSelectedException.class, expectedExceptionsMessageRegExp = "Test exception")
    public void selectNullByQueryWithExceptionThrowing() {
        var qAuthor = QAuthor.candidate();
        var qBook = QBook.candidate();
        var qCatalog = QCatalog.candidate();

        var ruslanAndLudmila = dataBaseSteps.get(selected(aSingleByQuery(ofType(Book.class)
                .where(qBook.name.eq("Ruslan and Ludmila")))));

        var carlosCastaneda = dataBaseSteps.get(selected(aSingleByQuery(ofType(Author.class)
                .where(qAuthor.firstName.eq("Carlos")
                        .and(qAuthor.lastName.eq("Castaneda"))))));

        dataBaseSteps.get(selected(aSingleByQuery(ofType(Catalog.class)
                .where(qCatalog.book.eq(ruslanAndLudmila)
                        .and(qCatalog.book.author.eq(carlosCastaneda))))
                .throwWhenResultEmpty(TEST_SUPPLIER)));
    }

    @Test(expectedExceptions = NothingIsSelectedException.class, expectedExceptionsMessageRegExp = "Test exception")
    public void selectEmptyListByQueryAndConditionWithExceptionThrowing() {
        var qAuthor = QAuthor.candidate();
        var qBook = QBook.candidate();
        var qCatalog = QCatalog.candidate();

        var ruslanAndLudmila = dataBaseSteps.get(selected(aSingleByQuery(ofType(Book.class)
                .where(qBook.name.eq("Ruslan and Ludmila")))));

        var alexanderPushkin = dataBaseSteps.get(selected(aSingleByQuery(ofType(Author.class)
                .where(qAuthor.firstName.eq("Alexander")
                        .and(qAuthor.lastName.eq("Pushkin"))))));

        dataBaseSteps.get(selected(listByQuery(ofType(Catalog.class)
                .where(qCatalog.book.eq(ruslanAndLudmila)
                        .and(qCatalog.book.author.eq(alexanderPushkin))))
                .criteria("ISBN is 0-671-73246-3",
                        catalog -> catalog.getIsbn().equals("0-671-73246-3"))
                .throwWhenResultEmpty(TEST_SUPPLIER)));
    }

    @Test(expectedExceptions = NothingIsSelectedException.class, expectedExceptionsMessageRegExp = "Test exception")
    public void selectNullByQueryAndConditionWithExceptionThrowing() {
        var qAuthor = QAuthor.candidate();
        var qBook = QBook.candidate();
        var qCatalog = QCatalog.candidate();

        var ruslanAndLudmila = dataBaseSteps.get(selected(aSingleByQuery(ofType(Book.class)
                .where(qBook.name.eq("Ruslan and Ludmila")))));

        var alexanderPushkin = dataBaseSteps.get(selected(aSingleByQuery(ofType(Author.class)
                .where(qAuthor.firstName.eq("Alexander")
                        .and(qAuthor.lastName.eq("Pushkin"))))));

        dataBaseSteps.get(selected(aSingleByQuery(ofType(Catalog.class)
                .where(qCatalog.book.eq(ruslanAndLudmila)
                        .and(qCatalog.book.author.eq(alexanderPushkin))))
                .criteria("ISBN is 0-671-73246-3",
                        catalog -> catalog.getIsbn().equals("0-671-73246-3"))
                .throwWhenResultEmpty(TEST_SUPPLIER)));
    }

    @Test
    public void selectOfListWithConnectionSupplier() {
        var qAuthor = QAuthor.candidate();
        var qBook = QBook.candidate();
        var qCatalog = QCatalog.candidate();

        var ruslanAndLudmila = dataBaseSteps.get(selected(aSingleByQuery(ofType(Book.class)
                .where(qBook.name.eq("Ruslan and Ludmila")))));

        var alexanderPushkin = dataBaseSteps.get(selected(aSingleByQuery(ofType(Author.class)
                .where(qAuthor.firstName.eq("Alexander")
                        .and(qAuthor.lastName.eq("Pushkin"))))));

        GetSelectedFunction<List<Catalog>> query = selected(listByQuery(ofType(Catalog.class)
                .where(qCatalog.book.eq(ruslanAndLudmila)
                        .and(qCatalog.book.author.eq(alexanderPushkin)))))
                .useConnection(ConnectionDataSupplierForTestBase2.class);

        GetSelectedFunction<List<Catalog>> query2 = selected(listByQuery(ofType(Catalog.class)
                .where(qCatalog.book.eq(ruslanAndLudmila)
                        .and(qCatalog.book.author.eq(alexanderPushkin)))));

        assertThat(dataBaseSteps.get(query), hasSize(0));
        assertThat(dataBaseSteps.get(query2), hasSize(1));
    }

    @Test
    public void selectOfOneWithConnectionSupplier() {
        var qAuthor = QAuthor.candidate();
        var qBook = QBook.candidate();
        var qCatalog = QCatalog.candidate();

        var ruslanAndLudmila = dataBaseSteps.get(selected(aSingleByQuery(ofType(Book.class)
                .where(qBook.name.eq("Ruslan and Ludmila")))));

        var alexanderPushkin = dataBaseSteps.get(selected(aSingleByQuery(ofType(Author.class)
                .where(qAuthor.firstName.eq("Alexander")
                        .and(qAuthor.lastName.eq("Pushkin"))))));

        var query = selected(aSingleByQuery(ofType(Catalog.class).where(qCatalog.book.eq(ruslanAndLudmila)
                .and(qCatalog.book.author.eq(alexanderPushkin)))))
                .useConnection(ConnectionDataSupplierForTestBase2.class);

        var query2 = selected(aSingleByQuery(ofType(Catalog.class).where(qCatalog.book.eq(ruslanAndLudmila)
                .and(qCatalog.book.author.eq(alexanderPushkin)))));

        assertThat(dataBaseSteps.get(query), nullValue());
        assertThat(dataBaseSteps.get(query2), not(nullValue()));
    }

    @Test
    public void selectOfListWithConnectionChange() {
        var qAuthor = QAuthor.candidate();
        var qBook = QBook.candidate();
        var qCatalog = QCatalog.candidate();

        var ruslanAndLudmila = dataBaseSteps.get(selected(aSingleByQuery(ofType(Book.class)
                .where(qBook.name.eq("Ruslan and Ludmila")))));

        var alexanderPushkin = dataBaseSteps.get(selected(aSingleByQuery(ofType(Author.class)
                .where(qAuthor.firstName.eq("Alexander")
                        .and(qAuthor.lastName.eq("Pushkin"))))));

        var query = selected(listByQuery(ofType(Catalog.class).where(qCatalog.book.eq(ruslanAndLudmila)
                .and(qCatalog.book.author.eq(alexanderPushkin)))))
                .useConnection(getKnownConnection(ConnectionDataSupplierForTestBase2.class, true));

        var query2 = selected(listByQuery(ofType(Catalog.class).where(qCatalog.book.eq(ruslanAndLudmila)
                .and(qCatalog.book.author.eq(alexanderPushkin)))));

        assertThat(dataBaseSteps.get(query), hasSize(0));
        assertThat(dataBaseSteps.get(query2), hasSize(1));
    }

    @Test
    public void selectOfOneWithConnectionChange() {
        var qAuthor = QAuthor.candidate();
        var qBook = QBook.candidate();
        var qCatalog = QCatalog.candidate();

        var ruslanAndLudmila = dataBaseSteps.get(selected(aSingleByQuery(ofType(Book.class)
                .where(qBook.name.eq("Ruslan and Ludmila")))));

        var alexanderPushkin = dataBaseSteps.get(selected(aSingleByQuery(ofType(Author.class)
                .where(qAuthor.firstName.eq("Alexander")
                        .and(qAuthor.lastName.eq("Pushkin"))))));

        var query = selected(aSingleByQuery(ofType(Catalog.class).where(qCatalog.book.eq(ruslanAndLudmila)
                .and(qCatalog.book.author.eq(alexanderPushkin)))))
                .useConnection(getKnownConnection(ConnectionDataSupplierForTestBase2.class, true));

        var query2 = selected(aSingleByQuery(ofType(Catalog.class).where(qCatalog.book.eq(ruslanAndLudmila)
                .and(qCatalog.book.author.eq(alexanderPushkin)))));

        assertThat(dataBaseSteps.get(query), nullValue());
        assertThat(dataBaseSteps.get(query2), not(nullValue()));
    }

    @Test
    public void equalityTest() {
        var pushkin = dataBaseSteps.get(selected(aSingleByQuery(ofType(Author.class)
                .where(QAuthor.candidate().lastName.eq("Pushkin")))));

        var qBook = QBook.candidate();
        var ruslanAndLudmila = dataBaseSteps.get(selected(aSingleByQuery(ofType(Book.class)
                .where(qBook.author.eq(pushkin)))));
        var ruslanAndLudmila2 = dataBaseSteps.get(selected(aSingleByQuery(ofType(Book.class)
                .where(qBook.name.eq("Ruslan and Ludmila")))));

        assertThat(ruslanAndLudmila, is(ruslanAndLudmila2));
    }
}
