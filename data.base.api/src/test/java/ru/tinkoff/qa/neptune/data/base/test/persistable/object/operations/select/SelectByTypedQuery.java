package ru.tinkoff.qa.neptune.data.base.test.persistable.object.operations.select;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.data.base.api.NothingIsSelectedException;
import ru.tinkoff.qa.neptune.data.base.test.persistable.object.operations.BaseDbOperationTest;
import ru.tinkoff.qa.neptune.data.base.test.persistable.object.tables.Book;
import ru.tinkoff.qa.neptune.data.base.test.persistable.object.tables.QBook;
import ru.tinkoff.qa.neptune.data.base.test.persistable.object.tables.db.one.tables.Author;
import ru.tinkoff.qa.neptune.data.base.test.persistable.object.tables.db.one.tables.Catalog;
import ru.tinkoff.qa.neptune.data.base.test.persistable.object.tables.db.one.tables.QAuthor;
import ru.tinkoff.qa.neptune.data.base.test.persistable.object.tables.db.one.tables.QCatalog;

import java.time.Duration;

import static java.lang.System.currentTimeMillis;
import static java.time.Duration.ofSeconds;
import static java.time.temporal.ChronoUnit.SECONDS;
import static java.util.stream.Collectors.toList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.testng.Assert.fail;
import static ru.tinkoff.qa.neptune.data.base.api.DataBaseStepContext.inDataBase;
import static ru.tinkoff.qa.neptune.data.base.api.properties.WaitingForQueryResultDuration.QueryTimeUnitProperties.WAITING_FOR_SELECTION_RESULT_TIME_UNIT;
import static ru.tinkoff.qa.neptune.data.base.api.properties.WaitingForQueryResultDuration.QueryTimeValueProperties.WAITING_FOR_SELECTION_RESULT_TIME_VALUE;
import static ru.tinkoff.qa.neptune.data.base.api.queries.SelectASingle.oneOf;
import static ru.tinkoff.qa.neptune.data.base.api.queries.SelectASingle.row;
import static ru.tinkoff.qa.neptune.data.base.api.queries.SelectList.listOf;
import static ru.tinkoff.qa.neptune.data.base.api.queries.SelectList.rows;
import static ru.tinkoff.qa.neptune.data.base.api.queries.jdoql.JDOQLQueryParameters.byJDOQuery;
import static ru.tinkoff.qa.neptune.data.base.api.queries.jdoql.JDOQLResultQueryParams.byJDOResultQuery;
import static ru.tinkoff.qa.neptune.data.base.api.queries.jdoql.WhereJunction.and;
import static ru.tinkoff.qa.neptune.data.base.api.queries.jdoql.WhereJunction.or;

@SuppressWarnings("ConstantConditions")
public class SelectByTypedQuery extends BaseDbOperationTest {

    private Book journeyToIxtlan;
    private Author carlosCastaneda;
    private Book ruslanAndLudmila;
    private Author alexanderPushkin;

    private Book theLegendOfTheAges;
    private Author hugo;

    private Book aHeroOfOurTimes;
    private Author lermontov;

    @BeforeClass
    public void prepare() {
        carlosCastaneda = inDataBase().select(oneOf(Author.class, byJDOQuery(QAuthor.class)
                .addWhere(qAuthor -> qAuthor.firstName.eq("Carlos"))
                .addWhere(qAuthor -> qAuthor.lastName.eq("Castaneda"))));

        journeyToIxtlan = inDataBase().select(oneOf(Book.class, byJDOQuery(QBook.class)
                .addWhere(qBook -> qBook.name.eq("Journey to Ixtlan"))
                .addWhere(qBook -> qBook.author.eq(carlosCastaneda))));

        alexanderPushkin = inDataBase().select(oneOf(Author.class, byJDOQuery(QAuthor.class)
                .addWhere(qAuthor -> qAuthor.firstName.eq("Alexander"))
                .addWhere(qAuthor -> qAuthor.lastName.eq("Pushkin"))));

        ruslanAndLudmila = inDataBase().select(oneOf(Book.class, byJDOQuery(QBook.class)
                .addWhere(qBook -> qBook.name.eq("Ruslan and Ludmila"))
                .addWhere(qBook -> qBook.author.eq(alexanderPushkin))));

        theLegendOfTheAges = inDataBase().select(oneOf(Book.class, byJDOQuery(QBook.class)
                .addWhere(qBook -> qBook.name.eq("The Legend of the Ages"))));

        hugo = inDataBase().select(oneOf(Author.class, byJDOQuery(QAuthor.class)
                .addWhere(qAuthor -> qAuthor.firstName.eq("Victor"))
                .addWhere(qAuthor -> qAuthor.lastName.eq("Hugo"))));

        aHeroOfOurTimes = inDataBase().select(oneOf(Book.class, byJDOQuery(QBook.class)
                .addWhere(qBook -> qBook.name.eq("A Hero of Our Times"))));

        lermontov = inDataBase().select(oneOf(Author.class, byJDOQuery(QAuthor.class)
                .addWhere(qAuthor -> qAuthor.firstName.eq("Mikhail"))
                .addWhere(qAuthor -> qAuthor.lastName.eq("Lermontov"))));
    }

    @Test(groups = "positive tests")
    public void selectListTestWithoutAnyCondition() {
        assertThat(inDataBase().select(listOf(Author.class)),
                hasSize(greaterThanOrEqualTo(2)));
    }

    @Test(groups = "positive tests")
    public void selectOneTestWithoutCondition() {
        var a = inDataBase().select(oneOf(Author.class));
        assertThat(a.getId(), is(1));
    }

    @Test(groups = "positive tests")
    public void selectListTestWithQuery() {
        var catalogItems = inDataBase().select(listOf(Catalog.class, byJDOQuery(QCatalog.class)
                .addWhere(qCatalog -> or(
                        qCatalog.book.name.eq(ruslanAndLudmila.getName()),
                        qCatalog.book.author.lastName.eq(carlosCastaneda.getLastName()))
                )
                .orderBy(qCatalog -> qCatalog.book.id.desc())));

        assertThat(catalogItems, hasSize(2));
        assertThat(catalogItems.stream().map(catalog -> catalog.getBook().getName()).collect(toList()),
                contains("Ruslan and Ludmila", "Journey to Ixtlan"));
    }

    @Test(groups = "positive tests")
    public void selectOneTestWithQuery() {
        var catalogItem = inDataBase().select(oneOf(Catalog.class, byJDOQuery(QCatalog.class)
                .addWhere(qCatalog -> or(
                        qCatalog.book.name.eq(ruslanAndLudmila.getName()),
                        qCatalog.book.author.lastName.eq(carlosCastaneda.getLastName())
                ))
                .orderBy(qCatalog -> qCatalog.book.id.desc())));

        assertThat(catalogItem.getBook().getName(), is("Ruslan and Ludmila"));
    }

    @Test(groups = "positive tests")
    public void selectOneTestWithWhereJunctionQuery() {
        var catalogItem = inDataBase().select(oneOf(Catalog.class, byJDOQuery(QCatalog.class)
                .addWhere(qCatalog -> or(
                        and(
                                qCatalog.book.name.eq(ruslanAndLudmila.getName()),
                                qCatalog.book.author.lastName.eq(alexanderPushkin.getLastName())
                        ),
                        and(
                                qCatalog.book.name.eq(journeyToIxtlan.getName()),
                                qCatalog.book.author.lastName.eq(carlosCastaneda.getLastName())
                        )

                ))
                .orderBy(qCatalog -> qCatalog.book.id.desc())));

        assertThat(catalogItem.getBook().getName(), is("Ruslan and Ludmila"));
    }

    @Test(groups = "positive tests")
    public void selectListTestWithWithWhereJunctionQuery() {
        var catalogItems = inDataBase().select(listOf(Catalog.class, byJDOQuery(QCatalog.class)
                .addWhere(qCatalog -> or(
                        and(
                                qCatalog.book.name.eq(ruslanAndLudmila.getName()),
                                qCatalog.book.author.lastName.eq(alexanderPushkin.getLastName())
                        ),
                        and(
                                qCatalog.book.name.eq(journeyToIxtlan.getName()),
                                qCatalog.book.author.lastName.eq(carlosCastaneda.getLastName())
                        )
                ))
                .orderBy(qCatalog -> qCatalog.book.id.desc())));

        assertThat(catalogItems, hasSize(2));
        assertThat(catalogItems.stream().map(catalog -> catalog.getBook().getName()).collect(toList()),
                contains("Ruslan and Ludmila", "Journey to Ixtlan"));
    }

    @Test(groups = "positive tests")
    public void selectListTestByCondition() {
        var catalogItems = inDataBase().select(listOf(Catalog.class)
                .criteria("A book with title `Ruslan and Ludmila`", catalog -> catalog
                        .getBook().getName().equalsIgnoreCase(ruslanAndLudmila.getName())));

        assertThat(catalogItems, hasSize(1));
        var catalogItem = catalogItems.get(0);
        assertThat(catalogItem.getBook().getName(), equalTo("Ruslan and Ludmila"));
    }

    @Test(groups = "positive tests")
    public void selectOneTestByCondition() {
        var author = inDataBase().select(oneOf(Author.class)
                .criteria("Wrote `Ruslan and Ludmila`", author1 -> ruslanAndLudmila
                        .getAuthor()
                        .equals(author1)));
        assertThat(author.getLastName(), equalTo("Pushkin"));
    }

    @Test(groups = "positive tests")
    public void selectListTestByQueryAndCondition() {
        var catalogItems = inDataBase().select(listOf(Catalog.class, byJDOQuery(QCatalog.class)
                .addWhere(qCatalog -> qCatalog.book.eq(journeyToIxtlan)))
                .criteria("Publisher is 'Simon & Schuster'",
                        catalog -> "Simon & Schuster".equals(catalog.getPublisher().getName())));

        assertThat(catalogItems, hasSize(1));
        assertThat(catalogItems.get(0).getBook().getAuthor(), equalTo(carlosCastaneda));
    }

    @Test(groups = "positive tests")
    public void selectOneTestByQueryAndCondition() {
        var catalogItem = inDataBase().select(oneOf(Catalog.class, byJDOQuery(QCatalog.class)
                .addWhere(qCatalog -> qCatalog.book.eq(journeyToIxtlan).and(qCatalog.book.author.eq(carlosCastaneda))))
                .criteria("Publisher is 'Simon & Schuster'",
                        catalog -> "Simon & Schuster".equals(catalog.getPublisher().getName())));

        assertThat(catalogItem.getBook().getAuthor(), equalTo(carlosCastaneda));
    }

    @Test(groups = "positive tests")
    public void selectOneByResultQuery() {
        var row = inDataBase().select(row(Book.class, byJDOResultQuery(QBook.class)
                .resultField(qBook -> qBook.author)
                .resultField(qBook -> qBook.name)
                .resultField(qBook -> qBook.yearOfFinishing.max())
                .addWhere(qBook -> qBook.yearOfFinishing.lt(1972))
                .groupBy(qBook -> qBook.author)
                .orderBy(qBook -> qBook.id.asc())
                .having(qBook -> qBook.id.count().gteq(1))
                .distinct(true)));

        assertThat(row, contains(alexanderPushkin,
                ruslanAndLudmila.getName(),
                ruslanAndLudmila.getYearOfFinishing()));
    }

    @Test(groups = "positive tests")
    public void selectListByResultQuery() {
        var rows = inDataBase().select(rows(Book.class, byJDOResultQuery(QBook.class)
                .resultField(qBook -> qBook.author)
                .resultField(qBook -> qBook.name)
                .resultField(qBook -> qBook.yearOfFinishing.max())
                .addWhere(qBook -> qBook.yearOfFinishing.lt(1972))
                .groupBy(qBook -> qBook.author)
                .orderBy(qBook -> qBook.id.asc())
                .having(qBook -> qBook.id.count().gteq(1))
                .distinct(true)));

        assertThat(rows, containsInRelativeOrder(contains(alexanderPushkin,
                ruslanAndLudmila.getName(),
                ruslanAndLudmila.getYearOfFinishing()),

                contains(hugo,
                        theLegendOfTheAges.getName(),
                        theLegendOfTheAges.getYearOfFinishing()),

                contains(lermontov,
                        aHeroOfOurTimes.getName(),
                        aHeroOfOurTimes.getYearOfFinishing())));

        assertThat(rows.getColumn(0), contains(alexanderPushkin, hugo, lermontov));
        assertThat(rows.getColumn(1), contains(ruslanAndLudmila.getName(),
                theLegendOfTheAges.getName(),
                aHeroOfOurTimes.getName()));
        assertThat(rows.getColumn(2), contains(ruslanAndLudmila.getYearOfFinishing(),
                theLegendOfTheAges.getYearOfFinishing(),
                aHeroOfOurTimes.getYearOfFinishing()));
    }

    @Test(dependsOnGroups = "positive tests")
    public void selectEmptyListByQueryWithDefaultTime() {
        long start = currentTimeMillis();
        var catalogItems = inDataBase().select(listOf(Catalog.class, byJDOQuery(QCatalog.class)
                .addWhere(qCatalog -> qCatalog.book.eq(ruslanAndLudmila).and(qCatalog.book.author.eq(carlosCastaneda)))));

        long end = currentTimeMillis();

        Duration fiveSeconds = ofSeconds(5);
        assertThat(catalogItems, hasSize(0));
        assertThat(end - start, greaterThanOrEqualTo(fiveSeconds.toMillis()));
        assertThat(end - start - fiveSeconds.toMillis(), lessThanOrEqualTo(500L));
    }

    @Test(dependsOnGroups = "positive tests")
    public void selectNullByQueryWithDefaultTime() {
        long start = currentTimeMillis();
        var catalogItem = inDataBase().select(oneOf(Catalog.class, byJDOQuery(QCatalog.class)
                .addWhere(qCatalog -> qCatalog.book.eq(ruslanAndLudmila)
                        .and(qCatalog.book.author.eq(carlosCastaneda)))));
        long end = currentTimeMillis();

        Duration fiveSeconds = ofSeconds(5);
        assertThat(catalogItem, nullValue());
        assertThat(end - start, greaterThanOrEqualTo(fiveSeconds.toMillis()));
        assertThat(end - start - fiveSeconds.toMillis(), lessThanOrEqualTo(500L));
    }

    @Test(dependsOnGroups = "positive tests")
    public void selectEmptyListByQueryWithDefinedTime() {
        Duration sixSeconds = ofSeconds(6);
        long start = currentTimeMillis();
        var catalogItems = inDataBase().select(listOf(Catalog.class, byJDOQuery(QCatalog.class)
                .addWhere(qCatalog -> qCatalog.book.eq(ruslanAndLudmila)
                        .and(qCatalog.book.author.eq(carlosCastaneda))))
                .timeOut(sixSeconds));
        long end = currentTimeMillis();

        assertThat(catalogItems, hasSize(0));
        assertThat(end - start, greaterThanOrEqualTo(sixSeconds.toMillis()));
        assertThat(end - start - sixSeconds.toMillis(), lessThanOrEqualTo(500L));
    }

    @Test(dependsOnGroups = "positive tests")
    public void selectNullByQueryWithDefinedTime() {
        Duration sixSeconds = ofSeconds(6);
        long start = currentTimeMillis();
        var catalogItem = inDataBase().select(oneOf(Catalog.class, byJDOQuery(QCatalog.class)
                .addWhere(qCatalog -> qCatalog.book.eq(ruslanAndLudmila)
                        .and(qCatalog.book.author.eq(carlosCastaneda))))
                .timeOut(sixSeconds));
        long end = currentTimeMillis();

        assertThat(catalogItem, nullValue());
        assertThat(end - start, greaterThanOrEqualTo(sixSeconds.toMillis()));
        assertThat(end - start - sixSeconds.toMillis(), lessThanOrEqualTo(500L));
    }

    @Test(dependsOnGroups = "positive tests")
    public void selectEmptyListByQueryWithTimeDefinedByProperty() {
        WAITING_FOR_SELECTION_RESULT_TIME_UNIT.accept(SECONDS);
        WAITING_FOR_SELECTION_RESULT_TIME_VALUE.accept(2L);

        Duration twoSeconds = ofSeconds(2);
        long start = currentTimeMillis();
        var catalogItems = inDataBase().select(listOf(Catalog.class, byJDOQuery(QCatalog.class)
                .addWhere(qCatalog -> qCatalog.book.eq(ruslanAndLudmila)
                        .and(qCatalog.book.author.eq(carlosCastaneda)))));
        long end = currentTimeMillis();

        try {
            assertThat(catalogItems, hasSize(0));
            assertThat(end - start, greaterThanOrEqualTo(twoSeconds.toMillis()));
            assertThat(end - start - twoSeconds.toMillis(), lessThanOrEqualTo(500L));
        } finally {
            System.getProperties().remove(WAITING_FOR_SELECTION_RESULT_TIME_UNIT.getName());
            System.getProperties().remove(WAITING_FOR_SELECTION_RESULT_TIME_VALUE.getName());
        }
    }

    @Test(dependsOnGroups = "positive tests")
    public void selectNullByQueryWithTimeDefinedByProperty() {
        WAITING_FOR_SELECTION_RESULT_TIME_UNIT.accept(SECONDS);
        WAITING_FOR_SELECTION_RESULT_TIME_VALUE.accept(2L);

        Duration twoSeconds = ofSeconds(2);
        long start = currentTimeMillis();
        var catalogItem = inDataBase().select(oneOf(Catalog.class, byJDOQuery(QCatalog.class)
                .addWhere(qCatalog -> qCatalog.book.eq(ruslanAndLudmila)
                        .and(qCatalog.book.author.eq(carlosCastaneda)))));
        long end = currentTimeMillis();

        try {
            assertThat(catalogItem, nullValue());
            assertThat(end - start, greaterThanOrEqualTo(twoSeconds.toMillis()));
            assertThat(end - start - twoSeconds.toMillis(), lessThanOrEqualTo(500L));
        } finally {
            System.getProperties().remove(WAITING_FOR_SELECTION_RESULT_TIME_UNIT.getName());
            System.getProperties().remove(WAITING_FOR_SELECTION_RESULT_TIME_VALUE.getName());
        }
    }

    @Test(dependsOnGroups = "positive tests")
    public void selectEmptyListByQueryAndConditionWithDefaultTime() {
        long start = currentTimeMillis();
        var catalogItems = inDataBase().select(listOf(Catalog.class, byJDOQuery(QCatalog.class)
                .addWhere(qCatalog -> qCatalog.book.eq(ruslanAndLudmila).and(qCatalog.book.author.eq(alexanderPushkin))))
                .criteria("Published in 1995", catalog -> catalog
                        .getYearOfPublishing().equals(1995)));
        long end = currentTimeMillis();

        Duration fiveSeconds = ofSeconds(5);
        assertThat(catalogItems, hasSize(0));
        assertThat(end - start, greaterThanOrEqualTo(fiveSeconds.toMillis()));
        assertThat(end - start - fiveSeconds.toMillis(), lessThanOrEqualTo(500L));
    }

    @Test(dependsOnGroups = "positive tests")
    public void selectNullByQueryAndConditionWithDefaultTime() {
        long start = currentTimeMillis();
        var catalogItem = inDataBase().select(oneOf(Catalog.class, byJDOQuery(QCatalog.class)
                .addWhere(qCatalog -> qCatalog.book.eq(ruslanAndLudmila).and(qCatalog.book.author.eq(alexanderPushkin))))
                .criteria("Published in 1995", catalog -> catalog.getYearOfPublishing().equals(1995)));
        long end = currentTimeMillis();

        Duration fiveSeconds = ofSeconds(5);
        assertThat(catalogItem, nullValue());
        assertThat(end - start, greaterThanOrEqualTo(fiveSeconds.toMillis()));
        assertThat(end - start - fiveSeconds.toMillis(), lessThanOrEqualTo(500L));
    }

    @Test(dependsOnGroups = "positive tests")
    public void selectEmptyListByQueryAndConditionWithDefinedTime() {
        Duration sixSeconds = ofSeconds(6);
        long start = currentTimeMillis();
        var catalogItems = inDataBase().select(listOf(Catalog.class, byJDOQuery(QCatalog.class)
                .addWhere(qCatalog -> qCatalog.book.eq(ruslanAndLudmila).and(qCatalog.book.author.eq(alexanderPushkin))))
                .criteria("Published in 1995", catalog -> catalog.getYearOfPublishing().equals(1995))
                .timeOut(sixSeconds));
        long end = currentTimeMillis();

        assertThat(catalogItems, hasSize(0));
        assertThat(end - start, greaterThanOrEqualTo(sixSeconds.toMillis()));
        assertThat(end - start - sixSeconds.toMillis(), lessThanOrEqualTo(500L));
    }

    @Test(dependsOnGroups = "positive tests")
    public void selectNullByQueryAndConditionWithDefinedTime() {
        Duration sixSeconds = ofSeconds(6);
        long start = currentTimeMillis();
        var catalogItem = inDataBase().select(oneOf(Catalog.class, byJDOQuery(QCatalog.class)
                .addWhere(qCatalog -> qCatalog.book.eq(ruslanAndLudmila).and(qCatalog.book.author.eq(alexanderPushkin))))
                .criteria("Published in 1995", catalog -> catalog.getYearOfPublishing().equals(1995))
                .timeOut(sixSeconds));
        long end = currentTimeMillis();

        assertThat(catalogItem, nullValue());
        assertThat(end - start, greaterThanOrEqualTo(sixSeconds.toMillis()));
        assertThat(end - start - sixSeconds.toMillis(), lessThanOrEqualTo(500L));
    }

    @Test(dependsOnGroups = "positive tests")
    public void selectEmptyListByQueryAndConditionWithTimeDefinedByProperty() {
        WAITING_FOR_SELECTION_RESULT_TIME_UNIT.accept(SECONDS);
        WAITING_FOR_SELECTION_RESULT_TIME_VALUE.accept(2L);

        Duration twoSeconds = ofSeconds(2);
        long start = currentTimeMillis();
        var catalogItems = inDataBase().select(listOf(Catalog.class, byJDOQuery(QCatalog.class)
                .addWhere(qCatalog -> qCatalog.book.eq(ruslanAndLudmila).and(qCatalog.book.author.eq(alexanderPushkin))))
                .criteria("Published in 1995", catalog -> catalog.getYearOfPublishing().equals(1995)));

        long end = currentTimeMillis();
        try {
            assertThat(catalogItems, hasSize(0));
            assertThat(end - start, greaterThanOrEqualTo(twoSeconds.toMillis()));
            assertThat(end - start - twoSeconds.toMillis(), lessThanOrEqualTo(500L));
        } finally {
            System.getProperties().remove(WAITING_FOR_SELECTION_RESULT_TIME_UNIT.getName());
            System.getProperties().remove(WAITING_FOR_SELECTION_RESULT_TIME_VALUE.getName());
        }
    }

    @Test(dependsOnGroups = "positive tests")
    public void selectNullByQueryAndConditionWithTimeDefinedByProperty() {
        WAITING_FOR_SELECTION_RESULT_TIME_UNIT.accept(SECONDS);
        WAITING_FOR_SELECTION_RESULT_TIME_VALUE.accept(2L);

        Duration twoSeconds = ofSeconds(2);
        long start = currentTimeMillis();
        var catalogItem = inDataBase().select(oneOf(Catalog.class, byJDOQuery(QCatalog.class)
                .addWhere(qCatalog -> qCatalog.book.eq(ruslanAndLudmila).and(qCatalog.book.author.eq(alexanderPushkin))))
                .criteria("Published in 1995", catalog -> catalog.getYearOfPublishing().equals(1995)));

        long end = currentTimeMillis();

        try {
            assertThat(catalogItem, nullValue());
            assertThat(end - start, greaterThanOrEqualTo(twoSeconds.toMillis()));
            assertThat(end - start - twoSeconds.toMillis(), lessThanOrEqualTo(500L));
        } finally {
            System.getProperties().remove(WAITING_FOR_SELECTION_RESULT_TIME_UNIT.getName());
            System.getProperties().remove(WAITING_FOR_SELECTION_RESULT_TIME_VALUE.getName());
        }
    }

    @Test(expectedExceptions = NothingIsSelectedException.class, dependsOnGroups = "positive tests")
    public void selectEmptyListByQueryWithExceptionThrowing() {
        inDataBase().select(listOf(Catalog.class, byJDOQuery(QCatalog.class)
                .addWhere(qCatalog -> qCatalog.book.eq(ruslanAndLudmila).and(qCatalog.book.author.eq(carlosCastaneda))))
                .throwOnNoResult());

        fail("Exception was expected");
    }

    @Test(expectedExceptions = NothingIsSelectedException.class, dependsOnGroups = "positive tests")
    public void selectNullByQueryWithExceptionThrowing() {

        inDataBase().select(oneOf(Catalog.class, byJDOQuery(QCatalog.class)
                .addWhere(qCatalog -> qCatalog.book.eq(ruslanAndLudmila).and(qCatalog.book.author.eq(carlosCastaneda))))
                .throwOnNoResult());

        fail("Exception was expected");
    }

    @Test(expectedExceptions = NothingIsSelectedException.class, dependsOnGroups = "positive tests")
    public void selectEmptyListByQueryAndConditionWithExceptionThrowing() {
        inDataBase().select(listOf(Catalog.class, byJDOQuery(QCatalog.class)
                .addWhere(qCatalog -> qCatalog.book.eq(ruslanAndLudmila).and(qCatalog.book.author.eq(alexanderPushkin))))
                .criteria("Published in 1995", catalog -> catalog.getYearOfPublishing().equals(1995))
                .throwOnNoResult());

        fail("Exception was expected");
    }

    @Test(expectedExceptions = NothingIsSelectedException.class, dependsOnGroups = "positive tests")
    public void selectNullByQueryAndConditionWithExceptionThrowing() {
        inDataBase().select(oneOf(Catalog.class, byJDOQuery(QCatalog.class)
                .addWhere(qCatalog -> qCatalog.book.eq(ruslanAndLudmila).and(qCatalog.book.author.eq(alexanderPushkin))))
                .criteria("Published in 1995", catalog -> catalog.getYearOfPublishing().equals(1995))
                .throwOnNoResult());

        fail("Exception was expected");
    }

    @Test(groups = "positive tests")
    public void equalityTest() {
        var ruslanAndLudmila = inDataBase().select(oneOf(Book.class, byJDOQuery(QBook.class)
                .addWhere(qBook -> qBook.author.eq(alexanderPushkin))));
        var ruslanAndLudmila2 = inDataBase().select(oneOf(Book.class, byJDOQuery(QBook.class)
                .addWhere(qBook -> qBook.name.eq("Ruslan and Ludmila"))));

        assertThat(ruslanAndLudmila, is(ruslanAndLudmila2));
    }
}
