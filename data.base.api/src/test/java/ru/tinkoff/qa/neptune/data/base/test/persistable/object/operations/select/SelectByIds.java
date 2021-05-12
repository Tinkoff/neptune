package ru.tinkoff.qa.neptune.data.base.test.persistable.object.operations.select;

import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.data.base.api.NothingIsSelectedException;
import ru.tinkoff.qa.neptune.data.base.test.persistable.object.operations.BaseDbOperationTest;
import ru.tinkoff.qa.neptune.data.base.test.persistable.object.tables.db.one.tables.Author;
import ru.tinkoff.qa.neptune.data.base.test.persistable.object.tables.db.one.tables.Catalog;
import ru.tinkoff.qa.neptune.data.base.test.persistable.object.tables.db.one.tables.Publisher;

import java.time.Duration;

import static java.lang.System.currentTimeMillis;
import static java.time.Duration.ofSeconds;
import static java.time.temporal.ChronoUnit.SECONDS;
import static java.util.stream.Collectors.toList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static ru.tinkoff.qa.neptune.data.base.api.DataBaseStepContext.inDataBase;
import static ru.tinkoff.qa.neptune.data.base.api.properties.WaitingForQueryResultDuration.QueryTimeUnitProperties.WAITING_FOR_SELECTION_RESULT_TIME_UNIT;
import static ru.tinkoff.qa.neptune.data.base.api.properties.WaitingForQueryResultDuration.QueryTimeValueProperties.WAITING_FOR_SELECTION_RESULT_TIME_VALUE;
import static ru.tinkoff.qa.neptune.data.base.api.queries.SelectASingle.oneOf;
import static ru.tinkoff.qa.neptune.data.base.api.queries.SelectList.listOf;
import static ru.tinkoff.qa.neptune.data.base.api.queries.ids.Id.id;
import static ru.tinkoff.qa.neptune.data.base.api.queries.ids.Ids.ids;

public class SelectByIds extends BaseDbOperationTest {

    @Test(groups = "positive tests")
    public void selectListTest() {
        var publisherItems = inDataBase().select(listOf(Publisher.class, ids(-3, 1, 2)));
        assertThat(publisherItems, hasSize(2));
        assertThat(publisherItems.stream().map(Publisher::getName).collect(toList()),
                contains("Bergh Publishing", "Simon & Schuster"));
    }

    @Test(groups = "positive tests")
    public void selectOneTest() {
        var catalogItem = inDataBase().select(oneOf(Publisher.class, id(1)));
        assertThat(catalogItem.getName(), is("Bergh Publishing"));
    }

    @Test(groups = "positive tests")
    public void selectListTestByCondition() {
        var publishers = inDataBase().select(listOf(Publisher.class, ids(1, 2, -1))
                .criteria("Has name Bergh Publishing", publisher -> publisher
                        .getName().equalsIgnoreCase("bergh Publishing")));
        assertThat(publishers, hasSize(1));
        assertThat(publishers.get(0).getName(), equalTo("Bergh Publishing"));
    }

    @Test(groups = "positive tests")
    public void selectOneTestByCondition() {
        var author = inDataBase().select(oneOf(Author.class, id(1))
                .criteria("Name is 'Alexander'", author1 -> "Alexander".equals(author1.getFirstName())));
        assertThat(author.getLastName(), equalTo("Pushkin"));
    }

    @Test(dependsOnGroups = "positive tests")
    public void selectEmptyListTestWithDefaultTime() {
        long start = currentTimeMillis();
        var catalogItems = inDataBase().select(listOf(Catalog.class, ids(-1, -2)));
        long end = currentTimeMillis();

        Duration fiveSeconds = ofSeconds(5);
        assertThat(catalogItems, hasSize(0));
        assertThat(end - start, greaterThanOrEqualTo(fiveSeconds.toMillis()));
        assertThat(end - start - fiveSeconds.toMillis(), lessThanOrEqualTo(700L));
    }

    @Test(dependsOnGroups = "positive tests")
    public void selectNullTestWithDefaultTime() {
        long start = currentTimeMillis();
        var catalogItem = inDataBase().select(oneOf(Catalog.class, id(-2)));
        long end = currentTimeMillis();

        Duration fiveSeconds = ofSeconds(5);
        assertThat(catalogItem, nullValue());
        assertThat(end - start, greaterThanOrEqualTo(fiveSeconds.toMillis()));
        assertThat(end - start - fiveSeconds.toMillis(), lessThanOrEqualTo(700L));
    }

    @Test(dependsOnGroups = "positive tests")
    public void selectEmptyListByIdWithDefinedTime() {
        Duration sixSeconds = ofSeconds(6);
        long start = currentTimeMillis();
        var catalogItems = inDataBase().select(listOf(Catalog.class, ids(-1, -2))
                .timeOut(sixSeconds));
        long end = currentTimeMillis();

        assertThat(catalogItems, hasSize(0));
        assertThat(end - start, greaterThanOrEqualTo(sixSeconds.toMillis()));
        assertThat(end - start - sixSeconds.toMillis(), lessThanOrEqualTo(700L));
    }

    @Test(dependsOnGroups = "positive tests")
    public void selectNullByIdWithDefinedTime() {
        Duration sixSeconds = ofSeconds(6);
        long start = currentTimeMillis();
        var catalogItem = inDataBase().select(oneOf(Catalog.class, id(-2))
                .timeOut(sixSeconds));
        long end = currentTimeMillis();

        assertThat(catalogItem, nullValue());
        assertThat(end - start, greaterThanOrEqualTo(sixSeconds.toMillis()));
        assertThat(end - start - sixSeconds.toMillis(), lessThanOrEqualTo(700L));
    }

    @Test(dependsOnGroups = "positive tests")
    public void selectEmptyListByIdyWithTimeDefinedByProperty() {
        WAITING_FOR_SELECTION_RESULT_TIME_UNIT.accept(SECONDS);
        WAITING_FOR_SELECTION_RESULT_TIME_VALUE.accept(2L);

        Duration twoSeconds = ofSeconds(2);
        long start = currentTimeMillis();
        var catalogItems = inDataBase().select(listOf(Catalog.class, ids(-1, -2)));
        long end = currentTimeMillis();

        try {
            assertThat(catalogItems, hasSize(0));
            assertThat(end - start, greaterThanOrEqualTo(twoSeconds.toMillis()));
            assertThat(end - start - twoSeconds.toMillis(), lessThanOrEqualTo(700L));
        } finally {
            System.getProperties().remove(WAITING_FOR_SELECTION_RESULT_TIME_UNIT.getName());
            System.getProperties().remove(WAITING_FOR_SELECTION_RESULT_TIME_VALUE.getName());
        }
    }

    @Test(dependsOnGroups = "positive tests")
    public void selectNullByIdWithTimeDefinedByProperty() {
        WAITING_FOR_SELECTION_RESULT_TIME_UNIT.accept(SECONDS);
        WAITING_FOR_SELECTION_RESULT_TIME_VALUE.accept(2L);

        Duration twoSeconds = ofSeconds(2);
        long start = currentTimeMillis();
        var catalogItem = inDataBase().select(oneOf(Catalog.class, id(-2)));
        long end = currentTimeMillis();

        try {
            assertThat(catalogItem, nullValue());
            assertThat(end - start, greaterThanOrEqualTo(twoSeconds.toMillis()));
            assertThat(end - start - twoSeconds.toMillis(), lessThanOrEqualTo(700L));
        } finally {
            System.getProperties().remove(WAITING_FOR_SELECTION_RESULT_TIME_UNIT.getName());
            System.getProperties().remove(WAITING_FOR_SELECTION_RESULT_TIME_VALUE.getName());
        }
    }

    @Test(dependsOnGroups = "positive tests")
    public void selectEmptyListByIdAndConditionWithDefaultTime() {
        long start = currentTimeMillis();
        var publishers = inDataBase().select(listOf(Publisher.class, ids(1))
                .criteria("Has name Simon & Schuster", publisher -> publisher
                        .getName().equals("Simon & Schuster")));
        long end = currentTimeMillis();

        Duration fiveSeconds = ofSeconds(5);
        assertThat(publishers, hasSize(0));
        assertThat(end - start, greaterThanOrEqualTo(fiveSeconds.toMillis()));
        assertThat(end - start - fiveSeconds.toMillis(), lessThanOrEqualTo(700L));
    }

    @Test(dependsOnGroups = "positive tests")
    public void selectNullByIdAndConditionWithDefaultTime() {
        long start = currentTimeMillis();
        var publisher = inDataBase().select(oneOf(Publisher.class, id(1))
                .criteria("Has name Simon & Schuster", publisherItem -> publisherItem
                        .getName().equals("Simon & Schuster")));
        long end = currentTimeMillis();

        Duration fiveSeconds = ofSeconds(5);
        assertThat(publisher, nullValue());
        assertThat(end - start, greaterThanOrEqualTo(fiveSeconds.toMillis()));
        assertThat(end - start - fiveSeconds.toMillis(), lessThanOrEqualTo(700L));
    }

    @Test(dependsOnGroups = "positive tests")
    public void selectEmptyListByIdAndConditionWithDefinedTime() {
        Duration sixSeconds = ofSeconds(6);
        long start = currentTimeMillis();
        var publishers = inDataBase().select(listOf(Publisher.class, ids(1))
                .criteria("Has name Simon & Schuster", publisher -> publisher
                        .getName().equals("Simon & Schuster"))
                .timeOut(sixSeconds));
        long end = currentTimeMillis();

        assertThat(publishers, hasSize(0));
        assertThat(end - start, greaterThanOrEqualTo(sixSeconds.toMillis()));
        assertThat(end - start - sixSeconds.toMillis(), lessThanOrEqualTo(700L));
    }

    @Test(dependsOnGroups = "positive tests")
    public void selectNullByIdAndConditionWithDefinedTime() {
        Duration sixSeconds = ofSeconds(6);
        long start = currentTimeMillis();
        var publisher = inDataBase().select(oneOf(Publisher.class, id(1))
                .criteria("Has name Simon & Schuster", publisherItem -> publisherItem
                        .getName().equals("Simon & Schuster"))
                .timeOut(sixSeconds));
        long end = currentTimeMillis();

        assertThat(publisher, nullValue());
        assertThat(end - start, greaterThanOrEqualTo(sixSeconds.toMillis()));
        assertThat(end - start - sixSeconds.toMillis(), lessThanOrEqualTo(700L));
    }

    @Test(dependsOnGroups = "positive tests")
    public void selectEmptyListByIdAndConditionWithTimeDefinedByProperty() {
        WAITING_FOR_SELECTION_RESULT_TIME_UNIT.accept(SECONDS);
        WAITING_FOR_SELECTION_RESULT_TIME_VALUE.accept(2L);

        Duration twoSeconds = ofSeconds(2);
        long start = currentTimeMillis();
        var publishers = inDataBase().select(listOf(Publisher.class, ids(1))
                .criteria("Has name Simon & Schuster", publisher -> publisher
                        .getName().equals("Simon & Schuster")));
        long end = currentTimeMillis();

        try {
            assertThat(publishers, hasSize(0));
            assertThat(end - start, greaterThanOrEqualTo(twoSeconds.toMillis()));
            assertThat(end - start - twoSeconds.toMillis(), lessThanOrEqualTo(500L));
        } finally {
            System.getProperties().remove(WAITING_FOR_SELECTION_RESULT_TIME_UNIT.getName());
            System.getProperties().remove(WAITING_FOR_SELECTION_RESULT_TIME_VALUE.getName());
        }
    }

    @Test(dependsOnGroups = "positive tests")
    public void selectNullByIdAndConditionWithTimeDefinedByProperty() {
        WAITING_FOR_SELECTION_RESULT_TIME_UNIT.accept(SECONDS);
        WAITING_FOR_SELECTION_RESULT_TIME_VALUE.accept(2L);

        Duration twoSeconds = ofSeconds(2);
        long start = currentTimeMillis();
        var publisher = inDataBase().select(oneOf(Publisher.class, id(1))
                .criteria("Has name Simon & Schuster", publisherItem -> publisherItem
                        .getName().equals("Simon & Schuster")));
        long end = currentTimeMillis();

        try {
            assertThat(publisher, nullValue());
            assertThat(end - start, greaterThanOrEqualTo(twoSeconds.toMillis()));
            assertThat(end - start - twoSeconds.toMillis(), lessThanOrEqualTo(500L));
        } finally {
            System.getProperties().remove(WAITING_FOR_SELECTION_RESULT_TIME_UNIT.getName());
            System.getProperties().remove(WAITING_FOR_SELECTION_RESULT_TIME_VALUE.getName());
        }
    }

    @Test(dependsOnGroups = "positive tests", expectedExceptions = NothingIsSelectedException.class)
    public void selectEmptyListByIdWithExceptionThrowing() {
        inDataBase().select(listOf(Catalog.class, ids(-1)).throwOnNoResult());
    }

    @Test(dependsOnGroups = "positive tests", expectedExceptions = NothingIsSelectedException.class)
    public void selectNullByIdWithExceptionThrowing() {
        inDataBase().select(oneOf(Catalog.class, id(-1)).throwOnNoResult());
    }

    @Test(expectedExceptions = NothingIsSelectedException.class)
    public void selectEmptyListByIdAndConditionWithExceptionThrowing() {
        inDataBase().select(listOf(Publisher.class, ids(1))
                .criteria("Has name Simon & Schuster", p -> p.getName().equals("Simon & Schuster"))
                .throwOnNoResult());
    }

    @Test(dependsOnGroups = "positive tests", expectedExceptions = NothingIsSelectedException.class)
    public void selectNullByIdAndConditionWithExceptionThrowing() {
        inDataBase().select(oneOf(Publisher.class, id(1))
                .criteria("Has name Simon & Schuster", p -> p.getName().equals("Simon & Schuster"))
                .throwOnNoResult());
    }
}
