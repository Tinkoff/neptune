package ru.tinkoff.qa.neptune.spring.data;

import com.querydsl.core.types.Order;
import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.spring.data.model.QTestEntity;
import ru.tinkoff.qa.neptune.spring.data.model.TestEntity;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.ignoreCase;
import static org.springframework.data.domain.ExampleMatcher.matchingAny;
import static org.springframework.data.domain.Sort.Direction.ASC;
import static org.springframework.data.domain.Sort.by;
import static ru.tinkoff.qa.neptune.spring.data.SpringDataContext.springData;
import static ru.tinkoff.qa.neptune.spring.data.select.common.CommonSelectStepFactory.*;
import static ru.tinkoff.qa.neptune.spring.data.select.querydsl.QueryDSLSelectStepFactory.*;

public class SelectTest extends BaseSpringDataPreparing {

    @Test
    public void selectByIdTest() {
        var entity = springData().find("Test entity",
                byId(testRepository, 1L));

        assertThat(entity, is(TEST_ENTITIES.get(0)));
    }

    @Test
    public void selectByIdTest2() {
        var entity = springData().find("Test entity",
                byId(reactiveCrudRepository, 1L));

        assertThat(entity, is(TEST_ENTITIES.get(0)));
    }

    @Test
    public void selectByIdTest4() {
        var entity = springData().find("Test entity",
                byId(testRxJava3SortingRepository, 1L));

        assertThat(entity, is(TEST_ENTITIES.get(0)));
    }

    @Test
    public void selectByIdSTest() {
        var entities = springData().find("Test entities",
                byIds(testRepository, 1L, 2L));

        assertThat(entities, is(TEST_ENTITIES));
    }

    @Test
    public void selectByIdSTest2() {
        var entities = springData().find("Test entities",
                byIds(reactiveCrudRepository, 1L, 2L));

        assertThat(entities, is(TEST_ENTITIES));
    }

    @Test
    public void selectByIdSTest4() {
        var entities = springData().find("Test entities",
                byIds(testRxJava3SortingRepository, 1L, 2L));

        assertThat(entities, is(TEST_ENTITIES));
    }

    @Test
    public void selectBySortingTest() {
        var entities = springData().find("Test entities",
                allBySorting(testRepository, ASC, "id", "name"));

        assertThat(entities, is(TEST_ENTITIES));
    }

    @Test
    public void selectBySortingTest2() {
        var entities = springData().find("Test entities",
                allBySorting(reactiveCrudRepository, ASC, "id", "name"));

        assertThat(entities, is(TEST_ENTITIES));
    }

    @Test
    public void selectBySortingTest4() {
        var entities = springData().find("Test entities",
                allBySorting(testRxJava3SortingRepository, ASC, "id", "name"));

        assertThat(entities, is(TEST_ENTITIES));
    }

    @Test
    public void selectByPageable() {
        var entities = springData().find("Test entities", asAPage(testRepository)
                .number(0)
                .size(5)
                .sort(by("id").descending().and(by("name"))));
        assertThat(entities, is(TEST_ENTITIES));
    }

    @Test
    public void selectOneByExampleTest() {
        var entity = springData().find("Test entity",
                byExample(testRepository, new TestEntity())
                        .matcher(m -> m.withMatcher("firstName", ignoreCase()))
                        .matcher(m -> m.withMatcher("lastName", ignoreCase()))
                        .initialMatcher(matchingAny()));

        assertThat(entity, is(TEST_ENTITIES.get(0)));
    }

    @Test
    public void selectOneByExampleTest2() {
        var entity = springData().find("Test entity",
                byExample(reactiveCrudRepository, new TestEntity())
                        .matcher(m -> m.withMatcher("firstName", ignoreCase()))
                        .matcher(m -> m.withMatcher("lastName", ignoreCase()))
                        .initialMatcher(matchingAny()));

        assertThat(entity, is(TEST_ENTITIES.get(0)));
    }

    @Test
    public void selectAllByExampleTest() {
        var entity = springData().find("Test entities",
                allByExample(testRepository, new TestEntity())
                        .matcher(m -> m.withMatcher("firstName", ignoreCase()))
                        .matcher(m -> m.withMatcher("lastName", ignoreCase()))
                        .initialMatcher(matchingAny()));

        assertThat(entity, is(TEST_ENTITIES));
    }

    @Test
    public void selectAllByExampleTest2() {
        var entity = springData().find("Test entities",
                allByExample(testRepository, new TestEntity())
                        .matcher(m -> m.withMatcher("firstName", ignoreCase()))
                        .matcher(m -> m.withMatcher("lastName", ignoreCase()))
                        .initialMatcher(matchingAny())
                        .sorting(ASC, "id", "name"));

        assertThat(entity, is(TEST_ENTITIES));
    }

    @Test
    public void selectAllByExampleTest3() {
        var entity = springData().find("Test entities",
                allByExample(reactiveCrudRepository, new TestEntity())
                        .matcher(m -> m.withMatcher("firstName", ignoreCase()))
                        .matcher(m -> m.withMatcher("lastName", ignoreCase()))
                        .initialMatcher(matchingAny()));

        assertThat(entity, is(TEST_ENTITIES));
    }

    @Test
    public void selectAllByExampleTest4() {
        var entity = springData().find("Test entities",
                allByExample(reactiveCrudRepository, new TestEntity())
                        .matcher(m -> m.withMatcher("firstName", ignoreCase()))
                        .matcher(m -> m.withMatcher("lastName", ignoreCase()))
                        .initialMatcher(matchingAny())
                        .sorting(ASC, "id", "name"));

        assertThat(entity, is(TEST_ENTITIES));
    }

    @Test
    public void selectOneByInvocationTest() {
        var entity = springData().find("Test entity",
                byInvocation(testRepository, r -> r.findSomething(true, "ABCD", 123)));

        assertThat(entity, is(TEST_ENTITIES.get(0)));
    }

    @Test
    public void selectAllByInvocationTest() {
        var entities = springData().find("Test entities",
                allByInvocation(testRepository, r -> r.findEntities(true, "ABCD", 123)));

        assertThat(entities, is(TEST_ENTITIES));
    }

    @Test
    public void selectByPredicateTest() {
        var q = QTestEntity.qTestEntity;
        var predicate = q.id.eq(1L)
                .and(q.name.eq("Test name"))
                .and(q.listData.contains("A"));

        var entity = springData().find("Test entity",
                byPredicate(querydslRepository, predicate));

        assertThat(entity, is(TEST_ENTITIES.get(0)));
    }

    @Test
    public void selectByPredicateTest2() {
        var q = QTestEntity.qTestEntity;
        var predicate = q.id.eq(1L)
                .and(q.name.eq("Test name"))
                .and(q.listData.contains("A"));

        var entity = springData().find("Test entity",
                byPredicate(reactiveQuerydslRepository, predicate));

        assertThat(entity, is(TEST_ENTITIES.get(0)));
    }

    @Test
    public void selectAllByPredicateTest() {
        var q = QTestEntity.qTestEntity;
        var predicate = q.id.eq(1L)
                .and(q.name.eq("Test name"))
                .and(q.listData.contains("A"));

        var entities = springData().find("Test entities",
                allByPredicate(querydslRepository, predicate));

        assertThat(entities, is(TEST_ENTITIES));
    }

    @Test
    public void selectAllByPredicateTest2() {
        var q = QTestEntity.qTestEntity;
        var predicate = q.id.eq(1L)
                .and(q.name.eq("Test name"))
                .and(q.listData.contains("A"));

        var entities = springData().find("Test entities",
                allByPredicate(reactiveQuerydslRepository, predicate));

        assertThat(entities, is(TEST_ENTITIES));
    }

    @Test
    public void selectAllByPredicateAndSortTest() {
        var q = QTestEntity.qTestEntity;
        var predicate = q.id.eq(1L)
                .and(q.name.eq("Test name"))
                .and(q.listData.contains("A"));

        var entities = springData().find("Test entities",
                allByPredicate(querydslRepository,
                        predicate,
                        ASC, "id", "name"));

        assertThat(entities, is(TEST_ENTITIES));
    }

    @Test
    public void selectAllByPredicateAndSortTest2() {
        var q = QTestEntity.qTestEntity;
        var predicate = q.id.eq(1L)
                .and(q.name.eq("Test name"))
                .and(q.listData.contains("A"));

        var entities = springData().find("Test entities",
                allByPredicate(reactiveQuerydslRepository,
                        predicate,
                        ASC, "id", "name"));

        assertThat(entities, is(TEST_ENTITIES));
    }

    @Test
    public void selectAllOrderedTest() {
        var q = QTestEntity.qTestEntity;

        var entities = springData().find("Test entities",
                allOrdered(querydslRepository, Order.ASC, q.id)
                        .orderSpecifier(Order.DESC, q.name)
                        .orderSpecifier(Order.ASC, q.arrayData));

        assertThat(entities, is(TEST_ENTITIES));
    }

    @Test
    public void selectAllOrderedTest2() {
        var q = QTestEntity.qTestEntity;

        var entities = springData().find("Test entities",
                allOrdered(reactiveQuerydslRepository, Order.ASC, q.id)
                        .orderSpecifier(Order.DESC, q.name)
                        .orderSpecifier(Order.ASC, q.arrayData));

        assertThat(entities, is(TEST_ENTITIES));
    }

    @Test
    public void selectAllOrderedTest3() {
        var q = QTestEntity.qTestEntity;
        var predicate = q.id.eq(1L)
                .and(q.name.eq("Test name"))
                .and(q.listData.contains("A"));

        var entities = springData().find("Test entities",
                allOrdered(querydslRepository, Order.ASC, q.id)
                        .orderSpecifier(Order.DESC, q.name)
                        .orderSpecifier(Order.ASC, q.arrayData)
                        .predicate(predicate));

        assertThat(entities, is(TEST_ENTITIES));
    }

    @Test
    public void selectAllOrderedTest4() {
        var q = QTestEntity.qTestEntity;
        var predicate = q.id.eq(1L)
                .and(q.name.eq("Test name"))
                .and(q.listData.contains("A"));

        var entities = springData().find("Test entities",
                allOrdered(reactiveQuerydslRepository, Order.ASC, q.id)
                        .orderSpecifier(Order.DESC, q.name)
                        .orderSpecifier(Order.ASC, q.arrayData)
                        .predicate(predicate));

        assertThat(entities, is(TEST_ENTITIES));
    }

    @Test
    public void selectAllAsPageByPredicate() {
        var q = QTestEntity.qTestEntity;
        var predicate = q.id.eq(1L)
                .and(q.name.eq("Test name"))
                .and(q.listData.contains("A"));

        var entities = springData().find("Test entities",
                asAPageByPredicate(querydslRepository, predicate)
                        .number(0)
                        .size(5)
                        .orderSpecifier(Order.DESC, q.name)
                        .orderSpecifier(Order.ASC, q.arrayData));

        assertThat(entities, is(TEST_ENTITIES));
    }

    @Test
    public void selectAllTest() {
        var entities = springData().find("Test entities",
                all(testRepository));

        assertThat(entities, is(TEST_ENTITIES));
    }

    @Test
    public void selectAllTest2() {
        var entities = springData().find("Test entities",
                all(reactiveCrudRepository));

        assertThat(entities, is(TEST_ENTITIES));
    }

    @Test
    public void selectAllTest4() {
        var entities = springData().find("Test entities",
                all(testRxJava3SortingRepository));

        assertThat(entities, is(TEST_ENTITIES));
    }
}
