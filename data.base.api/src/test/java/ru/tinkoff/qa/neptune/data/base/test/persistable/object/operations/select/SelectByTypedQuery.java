package ru.tinkoff.qa.neptune.data.base.test.persistable.object.operations.select;

import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.data.base.api.query.NothingIsSelectedException;
import ru.tinkoff.qa.neptune.data.base.api.query.SelectListByQuerySupplier;
import ru.tinkoff.qa.neptune.data.base.api.query.SelectSingleObjectByQuerySupplier;
import ru.tinkoff.qa.neptune.data.base.api.test.*;
import ru.tinkoff.qa.neptune.data.base.test.persistable.object.operations.BaseDbOperationTest;

import javax.jdo.JDOQLTypedQuery;
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
import static ru.tinkoff.qa.neptune.data.base.api.query.QueryBuilderFunction.ofType;
import static ru.tinkoff.qa.neptune.data.base.api.query.SelectListByQuerySupplier.listByQuery;
import static ru.tinkoff.qa.neptune.data.base.api.query.SelectSingleObjectByQuerySupplier.aSingleByQuery;

@SuppressWarnings("ConstantConditions")
public class SelectByTypedQuery extends BaseDbOperationTest {

    private static final Supplier<NothingIsSelectedException> TEST_SUPPLIER = () ->
            new NothingIsSelectedException("Test exception");

    @Test
    public void selectListTestWithoutAnyCondition() {
        assertThat(dataBaseSteps.get(listByQuery(ofType(Author.class))), hasSize(greaterThanOrEqualTo(2)));
    }

    @Test
    public void selectOneTestWithoutCondition() {
        Author a = dataBaseSteps.get(aSingleByQuery(ofType(Author.class)));
        assertThat(a.getId(), is(1));
    }

    @Test
    public void selectListTestWithQuery() {
        QCatalog c = QCatalog.candidate();
        List<Catalog> catalogItems = dataBaseSteps.get(listByQuery(ofType(Catalog.class).where(c.book.name.eq("Ruslan and Ludmila")
                .or(c.isbn.eq("0-671-73246-3")) //<Journey to Ixtlan
                .or(c.book.author.lastName.eq("Pushkin")))
                .orderBy(c.recordId.desc())));

        assertThat(catalogItems, hasSize(2));
        assertThat(catalogItems.stream().map(Catalog::getRecordId).collect(toList()),
                contains(2, 1));
    }

    @Test
    public void selectOneTestWithQuery() {
        QCatalog c = QCatalog.candidate();
        Catalog catalogItem = dataBaseSteps.get(aSingleByQuery(ofType(Catalog.class).where(c.book.name.eq("Ruslan and Ludmila")
                .or(c.isbn.eq("0-671-73246-3")) //<Journey to Ixtlan
                .or(c.book.author.lastName.eq("Pushkin")))
                .orderBy(c.recordId.desc())));

        assertThat(catalogItem.getRecordId(), is(2));
    }

    @Test
    public void selectListTestByCondition() {
        List<Catalog> catalogItems = dataBaseSteps.get(listByQuery(ofType(Catalog.class))
                .withCondition(condition("A book with title `Ruslan and Ludmila`",
                        catalog -> catalog.getBook().getName().equalsIgnoreCase("ruslan and ludmila"))));
        assertThat(catalogItems, hasSize(1));
        Catalog catalogItem = catalogItems.get(0);
        assertThat(catalogItem.getBook().getName(), equalTo("Ruslan and Ludmila"));
    }

    @Test
    public void selectOneTestByCondition() {
        Author author = dataBaseSteps.get(aSingleByQuery(ofType(Author.class))
                .withCondition(condition("Wrote `Ruslan and Ludmila`", author1 ->
                        null != author1.getBooks().stream()
                                .filter(book -> "ruslan and ludmila".equalsIgnoreCase(book.getName()))
                                .findFirst().orElse(null))));
        assertThat(author.getLastName(), equalTo("Pushkin"));
    }

    @Test
    public void selectListTestByQueryAndCondition() {
        QAuthor qAuthor = QAuthor.candidate();
        QBook qBook = QBook.candidate();
        QCatalog qCatalog = QCatalog.candidate();

        Book journeyToIxtlan = dataBaseSteps.get(aSingleByQuery(ofType(Book.class)
                .where(qBook.name.eq("Journey to Ixtlan"))));

        Author carlosCastaneda = dataBaseSteps.get(aSingleByQuery(ofType(Author.class)
                .where(qAuthor.firstName.eq("Carlos")
                        .and(qAuthor.lastName.eq("Castaneda")))));

        List<Catalog> catalogItems = dataBaseSteps.get(listByQuery(ofType(Catalog.class)
                .where(qCatalog.book.eq(journeyToIxtlan)))
                .withCondition(condition("Publisher is 'Simon & Schuster'",
                        catalog -> "Simon & Schuster".equals(catalog.getPublisher().getName()))));

        assertThat(catalogItems, hasSize(1));
        assertThat(catalogItems.get(0).getIsbn(), equalTo("0-671-73246-3"));
        assertThat(catalogItems.get(0).getBook().getAuthor(), equalTo(carlosCastaneda));
    }

    @Test
    public void selectOneTestByQueryAndCondition() {
        QAuthor qAuthor = QAuthor.candidate();
        QBook qBook = QBook.candidate();
        QCatalog qCatalog = QCatalog.candidate();

        Book journeyToIxtlan = dataBaseSteps.get(aSingleByQuery(ofType(Book.class)
                .where(qBook.name.eq("Journey to Ixtlan"))));

        Author carlosCastaneda = dataBaseSteps.get(aSingleByQuery(ofType(Author.class)
                .where(qAuthor.firstName.eq("Carlos")
                        .and(qAuthor.lastName.eq("Castaneda")))));

        Catalog catalogItem = dataBaseSteps.get(aSingleByQuery(ofType(Catalog.class)
                .where(qCatalog.book.eq(journeyToIxtlan)
                        .and(qCatalog.book.author.eq(carlosCastaneda))))
                .withCondition(condition("Publisher is 'Simon & Schuster'",
                        catalog -> "Simon & Schuster".equals(catalog.getPublisher().getName()))));

        assertThat(catalogItem.getIsbn(), equalTo("0-671-73246-3"));
        assertThat(catalogItem.getBook().getAuthor(), equalTo(carlosCastaneda));
    }

    @Test
    public void selectEmptyListByQueryWithDefaultTime() {
        QAuthor qAuthor = QAuthor.candidate();
        QBook qBook = QBook.candidate();
        QCatalog qCatalog = QCatalog.candidate();

        Book ruslanAndLudmila = dataBaseSteps.get(aSingleByQuery(ofType(Book.class)
                .where(qBook.name.eq("Ruslan and Ludmila"))));

        Author carlosCastaneda = dataBaseSteps.get(aSingleByQuery(ofType(Author.class)
                .where(qAuthor.firstName.eq("Carlos")
                        .and(qAuthor.lastName.eq("Castaneda")))));

        long start = currentTimeMillis();
        List<Catalog> catalogItems = dataBaseSteps.get(listByQuery(ofType(Catalog.class)
                .where(qCatalog.book.eq(ruslanAndLudmila)
                        .and(qCatalog.book.author.eq(carlosCastaneda)))));
        long end = currentTimeMillis();

        Duration fiveSeconds = ofSeconds(5);
        assertThat(catalogItems, hasSize(0));
        assertThat(end - start, greaterThanOrEqualTo(fiveSeconds.toMillis()));
        assertThat(end - start - fiveSeconds.toMillis(), lessThanOrEqualTo(500L));
    }

    @Test
    public void selectNullByQueryWithDefaultTime() {
        QAuthor qAuthor = QAuthor.candidate();
        QBook qBook = QBook.candidate();
        QCatalog qCatalog = QCatalog.candidate();

        Book ruslanAndLudmila = dataBaseSteps.get(aSingleByQuery(ofType(Book.class)
                .where(qBook.name.eq("Ruslan and Ludmila"))));

        Author carlosCastaneda = dataBaseSteps.get(aSingleByQuery(ofType(Author.class)
                .where(qAuthor.firstName.eq("Carlos")
                        .and(qAuthor.lastName.eq("Castaneda")))));

        long start = currentTimeMillis();
        Catalog catalogItem = dataBaseSteps.get(aSingleByQuery(ofType(Catalog.class)
                .where(qCatalog.book.eq(ruslanAndLudmila)
                        .and(qCatalog.book.author.eq(carlosCastaneda)))));
        long end = currentTimeMillis();

        Duration fiveSeconds = ofSeconds(5);
        assertThat(catalogItem, nullValue());
        assertThat(end - start, greaterThanOrEqualTo(fiveSeconds.toMillis()));
        assertThat(end - start - fiveSeconds.toMillis(), lessThanOrEqualTo(500L));
    }

    @Test
    public void selectEmptyListByQueryWithDefinedTime() {
        QAuthor qAuthor = QAuthor.candidate();
        QBook qBook = QBook.candidate();
        QCatalog qCatalog = QCatalog.candidate();

        Book ruslanAndLudmila = dataBaseSteps.get(aSingleByQuery(ofType(Book.class)
                .where(qBook.name.eq("Ruslan and Ludmila"))));

        Author carlosCastaneda = dataBaseSteps.get(aSingleByQuery(ofType(Author.class)
                .where(qAuthor.firstName.eq("Carlos")
                        .and(qAuthor.lastName.eq("Castaneda")))));

        Duration sixSeconds = ofSeconds(6);
        long start = currentTimeMillis();
        List<Catalog> catalogItems = dataBaseSteps.get(listByQuery(ofType(Catalog.class)
                .where(qCatalog.book.eq(ruslanAndLudmila)
                        .and(qCatalog.book.author.eq(carlosCastaneda))))
                .withTimeToGetValue(sixSeconds));
        long end = currentTimeMillis();

        assertThat(catalogItems, hasSize(0));
        assertThat(end - start, greaterThanOrEqualTo(sixSeconds.toMillis()));
        assertThat(end - start - sixSeconds.toMillis(), lessThanOrEqualTo(500L));
    }

    @Test
    public void selectNullByQueryWithDefinedTime() {
        QAuthor qAuthor = QAuthor.candidate();
        QBook qBook = QBook.candidate();
        QCatalog qCatalog = QCatalog.candidate();

        Book ruslanAndLudmila = dataBaseSteps.get(aSingleByQuery(ofType(Book.class)
                .where(qBook.name.eq("Ruslan and Ludmila"))));

        Author carlosCastaneda = dataBaseSteps.get(aSingleByQuery(ofType(Author.class)
                .where(qAuthor.firstName.eq("Carlos")
                        .and(qAuthor.lastName.eq("Castaneda")))));

        Duration sixSeconds = ofSeconds(6);
        long start = currentTimeMillis();
        Catalog catalogItem = dataBaseSteps.get(aSingleByQuery(ofType(Catalog.class)
                .where(qCatalog.book.eq(ruslanAndLudmila)
                        .and(qCatalog.book.author.eq(carlosCastaneda))))
                .withTimeToGetValue(sixSeconds));
        long end = currentTimeMillis();

        assertThat(catalogItem, nullValue());
        assertThat(end - start, greaterThanOrEqualTo(sixSeconds.toMillis()));
        assertThat(end - start - sixSeconds.toMillis(), lessThanOrEqualTo(500L));
    }

    @Test
    public void selectEmptyListByQueryWithTimeDefinedByProperty() {
        System.setProperty(DEFAULT_WAITING_FOR_SELECTION_RESULT_TIME_UNIT_PROPERTY.getPropertyName(), "SECONDS");
        System.setProperty(DEFAULT_WAITING_FOR_SELECTION_RESULT_TIME_VALUE_PROPERTY.getPropertyName(), "2");

        QAuthor qAuthor = QAuthor.candidate();
        QBook qBook = QBook.candidate();
        QCatalog qCatalog = QCatalog.candidate();

        Book ruslanAndLudmila = dataBaseSteps.get(aSingleByQuery(ofType(Book.class)
                .where(qBook.name.eq("Ruslan and Ludmila"))));

        Author carlosCastaneda = dataBaseSteps.get(aSingleByQuery(ofType(Author.class)
                .where(qAuthor.firstName.eq("Carlos")
                        .and(qAuthor.lastName.eq("Castaneda")))));

        Duration twoSeconds = ofSeconds(2);
        long start = currentTimeMillis();
        List<Catalog> catalogItems = dataBaseSteps.get(listByQuery(ofType(Catalog.class)
                .where(qCatalog.book.eq(ruslanAndLudmila)
                        .and(qCatalog.book.author.eq(carlosCastaneda)))));
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
    public void selectNullByQueryWithTimeDefinedByProperty() {
        System.setProperty(DEFAULT_WAITING_FOR_SELECTION_RESULT_TIME_UNIT_PROPERTY.getPropertyName(), "SECONDS");
        System.setProperty(DEFAULT_WAITING_FOR_SELECTION_RESULT_TIME_VALUE_PROPERTY.getPropertyName(), "2");

        QAuthor qAuthor = QAuthor.candidate();
        QBook qBook = QBook.candidate();
        QCatalog qCatalog = QCatalog.candidate();

        Book ruslanAndLudmila = dataBaseSteps.get(aSingleByQuery(ofType(Book.class)
                .where(qBook.name.eq("Ruslan and Ludmila"))));

        Author carlosCastaneda = dataBaseSteps.get(aSingleByQuery(ofType(Author.class)
                .where(qAuthor.firstName.eq("Carlos")
                        .and(qAuthor.lastName.eq("Castaneda")))));

        Duration twoSeconds = ofSeconds(2);
        long start = currentTimeMillis();
        Catalog catalogItem = dataBaseSteps.get(aSingleByQuery(ofType(Catalog.class)
                .where(qCatalog.book.eq(ruslanAndLudmila)
                        .and(qCatalog.book.author.eq(carlosCastaneda)))));
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

    @Test
    public void selectEmptyListByQueryAndConditionWithDefaultTime() {
        QAuthor qAuthor = QAuthor.candidate();
        QBook qBook = QBook.candidate();
        QCatalog qCatalog = QCatalog.candidate();

        Book ruslanAndLudmila = dataBaseSteps.get(aSingleByQuery(ofType(Book.class)
                .where(qBook.name.eq("Ruslan and Ludmila"))));

        Author alexanderPushkin = dataBaseSteps.get(aSingleByQuery(ofType(Author.class)
                .where(qAuthor.firstName.eq("Alexander")
                        .and(qAuthor.lastName.eq("Pushkin")))));

        long start = currentTimeMillis();
        List<Catalog> catalogItems = dataBaseSteps.get(listByQuery(ofType(Catalog.class)
                .where(qCatalog.book.eq(ruslanAndLudmila)
                        .and(qCatalog.book.author.eq(alexanderPushkin))))
                .withCondition(condition("ISBN is 0-671-73246-3",
                        catalog -> catalog.getIsbn().equals("0-671-73246-3"))));
        long end = currentTimeMillis();

        Duration fiveSeconds = ofSeconds(5);
        assertThat(catalogItems, hasSize(0));
        assertThat(end - start, greaterThanOrEqualTo(fiveSeconds.toMillis()));
        assertThat(end - start - fiveSeconds.toMillis(), lessThanOrEqualTo(500L));
    }

    @Test
    public void selectNullByQueryAndConditionWithDefaultTime() {
        QAuthor qAuthor = QAuthor.candidate();
        QBook qBook = QBook.candidate();
        QCatalog qCatalog = QCatalog.candidate();

        Book ruslanAndLudmila = dataBaseSteps.get(aSingleByQuery(ofType(Book.class)
                .where(qBook.name.eq("Ruslan and Ludmila"))));

        Author alexanderPushkin = dataBaseSteps.get(aSingleByQuery(ofType(Author.class)
                .where(qAuthor.firstName.eq("Alexander")
                        .and(qAuthor.lastName.eq("Pushkin")))));

        long start = currentTimeMillis();
        Catalog catalogItem = dataBaseSteps.get(aSingleByQuery(ofType(Catalog.class)
                .where(qCatalog.book.eq(ruslanAndLudmila)
                        .and(qCatalog.book.author.eq(alexanderPushkin))))
                .withCondition(condition("ISBN is 0-671-73246-3",
                        catalog -> catalog.getIsbn().equals("0-671-73246-3"))));
        long end = currentTimeMillis();

        Duration fiveSeconds = ofSeconds(5);
        assertThat(catalogItem, nullValue());
        assertThat(end - start, greaterThanOrEqualTo(fiveSeconds.toMillis()));
        assertThat(end - start - fiveSeconds.toMillis(), lessThanOrEqualTo(500L));
    }

    @Test
    public void selectEmptyListByQueryAndConditionWithDefinedTime() {
        QAuthor qAuthor = QAuthor.candidate();
        QBook qBook = QBook.candidate();
        QCatalog qCatalog = QCatalog.candidate();

        Book ruslanAndLudmila = dataBaseSteps.get(aSingleByQuery(ofType(Book.class)
                .where(qBook.name.eq("Ruslan and Ludmila"))));

        Author alexanderPushkin = dataBaseSteps.get(aSingleByQuery(ofType(Author.class)
                .where(qAuthor.firstName.eq("Alexander")
                        .and(qAuthor.lastName.eq("Pushkin")))));

        Duration sixSeconds = ofSeconds(6);
        long start = currentTimeMillis();
        List<Catalog> catalogItems = dataBaseSteps.get(listByQuery(ofType(Catalog.class)
                .where(qCatalog.book.eq(ruslanAndLudmila)
                        .and(qCatalog.book.author.eq(alexanderPushkin))))
                .withCondition(condition("ISBN is 0-671-73246-3",
                        catalog -> catalog.getIsbn().equals("0-671-73246-3")))
                .withTimeToGetValue(sixSeconds));
        long end = currentTimeMillis();

        assertThat(catalogItems, hasSize(0));
        assertThat(end - start, greaterThanOrEqualTo(sixSeconds.toMillis()));
        assertThat(end - start - sixSeconds.toMillis(), lessThanOrEqualTo(500L));
    }

    @Test
    public void selectNullByQueryAndConditionWithDefinedTime() {
        QAuthor qAuthor = QAuthor.candidate();
        QBook qBook = QBook.candidate();
        QCatalog qCatalog = QCatalog.candidate();

        Book ruslanAndLudmila = dataBaseSteps.get(aSingleByQuery(ofType(Book.class)
                .where(qBook.name.eq("Ruslan and Ludmila"))));

        Author alexanderPushkin = dataBaseSteps.get(aSingleByQuery(ofType(Author.class)
                .where(qAuthor.firstName.eq("Alexander")
                        .and(qAuthor.lastName.eq("Pushkin")))));

        Duration sixSeconds = ofSeconds(6);
        long start = currentTimeMillis();
        Catalog catalogItem = dataBaseSteps.get(aSingleByQuery(ofType(Catalog.class)
                .where(qCatalog.book.eq(ruslanAndLudmila)
                        .and(qCatalog.book.author.eq(alexanderPushkin))))
                .withCondition(condition("ISBN is 0-671-73246-3",
                        catalog -> catalog.getIsbn().equals("0-671-73246-3")))
                .withTimeToGetValue(sixSeconds));
        long end = currentTimeMillis();

        assertThat(catalogItem, nullValue());
        assertThat(end - start, greaterThanOrEqualTo(sixSeconds.toMillis()));
        assertThat(end - start - sixSeconds.toMillis(), lessThanOrEqualTo(500L));
    }

    @Test
    public void selectEmptyListByQueryAndConditionWithTimeDefinedByProperty() {
        System.setProperty(DEFAULT_WAITING_FOR_SELECTION_RESULT_TIME_UNIT_PROPERTY.getPropertyName(), "SECONDS");
        System.setProperty(DEFAULT_WAITING_FOR_SELECTION_RESULT_TIME_VALUE_PROPERTY.getPropertyName(), "2");

        QAuthor qAuthor = QAuthor.candidate();
        QBook qBook = QBook.candidate();
        QCatalog qCatalog = QCatalog.candidate();

        Book ruslanAndLudmila = dataBaseSteps.get(aSingleByQuery(ofType(Book.class)
                .where(qBook.name.eq("Ruslan and Ludmila"))));

        Author alexanderPushkin = dataBaseSteps.get(aSingleByQuery(ofType(Author.class)
                .where(qAuthor.firstName.eq("Alexander")
                        .and(qAuthor.lastName.eq("Pushkin")))));

        Duration twoSeconds = ofSeconds(2);
        long start = currentTimeMillis();
        List<Catalog> catalogItems = dataBaseSteps.get(listByQuery(ofType(Catalog.class)
                .where(qCatalog.book.eq(ruslanAndLudmila)
                        .and(qCatalog.book.author.eq(alexanderPushkin))))
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
    public void selectNullByQueryAndConditionWithTimeDefinedByProperty() {
        System.setProperty(DEFAULT_WAITING_FOR_SELECTION_RESULT_TIME_UNIT_PROPERTY.getPropertyName(), "SECONDS");
        System.setProperty(DEFAULT_WAITING_FOR_SELECTION_RESULT_TIME_VALUE_PROPERTY.getPropertyName(), "2");

        QAuthor qAuthor = QAuthor.candidate();
        QBook qBook = QBook.candidate();
        QCatalog qCatalog = QCatalog.candidate();

        Book ruslanAndLudmila = dataBaseSteps.get(aSingleByQuery(ofType(Book.class)
                .where(qBook.name.eq("Ruslan and Ludmila"))));

        Author alexanderPushkin = dataBaseSteps.get(aSingleByQuery(ofType(Author.class)
                .where(qAuthor.firstName.eq("Alexander")
                        .and(qAuthor.lastName.eq("Pushkin")))));

        Duration twoSeconds = ofSeconds(2);
        long start = currentTimeMillis();
        Catalog catalogItem = dataBaseSteps.get(aSingleByQuery(ofType(Catalog.class)
                .where(qCatalog.book.eq(ruslanAndLudmila)
                        .and(qCatalog.book.author.eq(alexanderPushkin))))
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
    public void selectEmptyListByQueryWithExceptionThrowing() {
        QAuthor qAuthor = QAuthor.candidate();
        QBook qBook = QBook.candidate();
        QCatalog qCatalog = QCatalog.candidate();

        Book ruslanAndLudmila = dataBaseSteps.get(aSingleByQuery(ofType(Book.class)
                .where(qBook.name.eq("Ruslan and Ludmila"))));

        Author carlosCastaneda = dataBaseSteps.get(aSingleByQuery(ofType(Author.class)
                .where(qAuthor.firstName.eq("Carlos")
                        .and(qAuthor.lastName.eq("Castaneda")))));

        dataBaseSteps.get(listByQuery(ofType(Catalog.class)
                .where(qCatalog.book.eq(ruslanAndLudmila)
                        .and(qCatalog.book.author.eq(carlosCastaneda))))
                .toThrowOnEmptyResult(TEST_SUPPLIER));
    }

    @Test(expectedExceptions = NothingIsSelectedException.class, expectedExceptionsMessageRegExp = "Test exception")
    public void selectNullByQueryWithExceptionThrowing() {
        QAuthor qAuthor = QAuthor.candidate();
        QBook qBook = QBook.candidate();
        QCatalog qCatalog = QCatalog.candidate();

        Book ruslanAndLudmila = dataBaseSteps.get(aSingleByQuery(ofType(Book.class)
                .where(qBook.name.eq("Ruslan and Ludmila"))));

        Author carlosCastaneda = dataBaseSteps.get(aSingleByQuery(ofType(Author.class)
                .where(qAuthor.firstName.eq("Carlos")
                        .and(qAuthor.lastName.eq("Castaneda")))));

        dataBaseSteps.get(aSingleByQuery(ofType(Catalog.class)
                .where(qCatalog.book.eq(ruslanAndLudmila)
                        .and(qCatalog.book.author.eq(carlosCastaneda))))
                .toThrowOnEmptyResult(TEST_SUPPLIER));
    }

    @Test(expectedExceptions = NothingIsSelectedException.class, expectedExceptionsMessageRegExp = "Test exception")
    public void selectEmptyListByQueryAndConditionWithExceptionThrowing() {
        QAuthor qAuthor = QAuthor.candidate();
        QBook qBook = QBook.candidate();
        QCatalog qCatalog = QCatalog.candidate();

        Book ruslanAndLudmila = dataBaseSteps.get(aSingleByQuery(ofType(Book.class)
                .where(qBook.name.eq("Ruslan and Ludmila"))));

        Author alexanderPushkin = dataBaseSteps.get(aSingleByQuery(ofType(Author.class)
                .where(qAuthor.firstName.eq("Alexander")
                        .and(qAuthor.lastName.eq("Pushkin")))));

        dataBaseSteps.get(listByQuery(ofType(Catalog.class)
                .where(qCatalog.book.eq(ruslanAndLudmila)
                        .and(qCatalog.book.author.eq(alexanderPushkin))))
                .withCondition(condition("ISBN is 0-671-73246-3",
                        catalog -> catalog.getIsbn().equals("0-671-73246-3")))
                .toThrowOnEmptyResult(TEST_SUPPLIER));
    }

    @Test(expectedExceptions = NothingIsSelectedException.class, expectedExceptionsMessageRegExp = "Test exception")
    public void selectNullByQueryAndConditionWithExceptionThrowing() {
        QAuthor qAuthor = QAuthor.candidate();
        QBook qBook = QBook.candidate();
        QCatalog qCatalog = QCatalog.candidate();

        Book ruslanAndLudmila = dataBaseSteps.get(aSingleByQuery(ofType(Book.class)
                .where(qBook.name.eq("Ruslan and Ludmila"))));

        Author alexanderPushkin = dataBaseSteps.get(aSingleByQuery(ofType(Author.class)
                .where(qAuthor.firstName.eq("Alexander")
                        .and(qAuthor.lastName.eq("Pushkin")))));

        dataBaseSteps.get(aSingleByQuery(ofType(Catalog.class)
                .where(qCatalog.book.eq(ruslanAndLudmila)
                        .and(qCatalog.book.author.eq(alexanderPushkin))))
                .withCondition(condition("ISBN is 0-671-73246-3",
                        catalog -> catalog.getIsbn().equals("0-671-73246-3")))
                .toThrowOnEmptyResult(TEST_SUPPLIER));
    }

    @Test
    public void selectOfListWithConnectionChangeByName() {
        QAuthor qAuthor = QAuthor.candidate();
        QBook qBook = QBook.candidate();
        QCatalog qCatalog = QCatalog.candidate();

        Book ruslanAndLudmila = dataBaseSteps.get(aSingleByQuery(ofType(Book.class)
                .where(qBook.name.eq("Ruslan and Ludmila"))));

        Author alexanderPushkin = dataBaseSteps.get(aSingleByQuery(ofType(Author.class)
                .where(qAuthor.firstName.eq("Alexander")
                        .and(qAuthor.lastName.eq("Pushkin")))));

        SelectListByQuerySupplier<Catalog, JDOQLTypedQuery<Catalog>> query = listByQuery(ofType(Catalog.class)
                .where(qCatalog.book.eq(ruslanAndLudmila)
                        .and(qCatalog.book.author.eq(alexanderPushkin))));

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
        QAuthor qAuthor = QAuthor.candidate();
        QBook qBook = QBook.candidate();
        QCatalog qCatalog = QCatalog.candidate();

        Book ruslanAndLudmila = dataBaseSteps.get(aSingleByQuery(ofType(Book.class)
                .where(qBook.name.eq("Ruslan and Ludmila"))));

        Author alexanderPushkin = dataBaseSteps.get(aSingleByQuery(ofType(Author.class)
                .where(qAuthor.firstName.eq("Alexander")
                        .and(qAuthor.lastName.eq("Pushkin")))));

        SelectSingleObjectByQuerySupplier<Catalog, JDOQLTypedQuery<Catalog>> query = aSingleByQuery(ofType(Catalog.class)
                .where(qCatalog.book.eq(ruslanAndLudmila)
                        .and(qCatalog.book.author.eq(alexanderPushkin))));

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
        QAuthor qAuthor = QAuthor.candidate();
        QBook qBook = QBook.candidate();
        QCatalog qCatalog = QCatalog.candidate();

        Book ruslanAndLudmila = dataBaseSteps.get(aSingleByQuery(ofType(Book.class)
                .where(qBook.name.eq("Ruslan and Ludmila"))));

        Author alexanderPushkin = dataBaseSteps.get(aSingleByQuery(ofType(Author.class)
                .where(qAuthor.firstName.eq("Alexander")
                        .and(qAuthor.lastName.eq("Pushkin")))));

        SelectListByQuerySupplier<Catalog, JDOQLTypedQuery<Catalog>> query = listByQuery(ofType(Catalog.class)
                .where(qCatalog.book.eq(ruslanAndLudmila)
                        .and(qCatalog.book.author.eq(alexanderPushkin))));

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
        QAuthor qAuthor = QAuthor.candidate();
        QBook qBook = QBook.candidate();
        QCatalog qCatalog = QCatalog.candidate();

        Book ruslanAndLudmila = dataBaseSteps.get(aSingleByQuery(ofType(Book.class)
                .where(qBook.name.eq("Ruslan and Ludmila"))));

        Author alexanderPushkin = dataBaseSteps.get(aSingleByQuery(ofType(Author.class)
                .where(qAuthor.firstName.eq("Alexander")
                        .and(qAuthor.lastName.eq("Pushkin")))));

        SelectSingleObjectByQuerySupplier<Catalog, JDOQLTypedQuery<Catalog>> query = aSingleByQuery(ofType(Catalog.class)
                .where(qCatalog.book.eq(ruslanAndLudmila)
                        .and(qCatalog.book.author.eq(alexanderPushkin))));

        try {
            assertThat(dataBaseSteps.get(query
                    .usePersistenceUnit(getPersistenceManagerFactory(TEST_BASE2, true))), nullValue());
            assertThat(dataBaseSteps.get(query.useDefaultPersistenceUnit()), not(nullValue()));
        }
        finally {
            dataBaseSteps.switchToDefault();
        }
    }

    @Test
    public void equalityTest() {
        Author pushkin = dataBaseSteps.get(aSingleByQuery(ofType(Author.class)
                .where(QAuthor.candidate().lastName.eq("Pushkin"))));

        QBook qBook = QBook.candidate();
        Book ruslanAndLudmila = dataBaseSteps.get(aSingleByQuery(ofType(Book.class)
                .where(qBook.author.eq(pushkin))));
        Book ruslanAndLudmila2 = dataBaseSteps.get(aSingleByQuery(ofType(Book.class)
                .where(qBook.name.eq("Ruslan and Ludmila"))));

        assertThat(ruslanAndLudmila, is(ruslanAndLudmila2));
    }
}
