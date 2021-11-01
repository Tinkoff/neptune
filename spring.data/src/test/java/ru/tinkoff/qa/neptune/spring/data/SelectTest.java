package ru.tinkoff.qa.neptune.spring.data;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Single;
import org.mockito.Mock;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.querydsl.QPageRequest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.tinkoff.qa.neptune.spring.data.model.*;

import java.util.ArrayList;
import java.util.List;

import static java.util.List.of;
import static java.util.Optional.ofNullable;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;
import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.ignoreCase;
import static org.springframework.data.domain.ExampleMatcher.matchingAny;
import static org.springframework.data.domain.Sort.Direction.ASC;
import static org.springframework.data.domain.Sort.by;
import static ru.tinkoff.qa.neptune.spring.data.SpringDataContext.springData;
import static ru.tinkoff.qa.neptune.spring.data.select.common.CommonSelectStepFactory.*;
import static ru.tinkoff.qa.neptune.spring.data.select.querydsl.QueryDSLSelectStepFactory.*;

@SuppressWarnings("unchecked")
public class SelectTest {

    private static final List<TestEntity> TEST_ENTITIES = new ArrayList<>(of(
            new TestEntity().setId(1L)
                    .setName("Test Name 1")
                    .setArrayData(new String[]{"A", "B", "C", "D"})
                    .setListData(new ArrayList<>(of("A", "B", "C", "D"))),

            new TestEntity().setId(2L)
                    .setName("Test Name 2")
                    .setArrayData(new String[]{"E", "F", "G", "H"})
                    .setListData(new ArrayList<>(of("E", "F", "G", "H")))
    ));
    @Mock
    private TestRepository testRepository;
    @Mock
    private TestReactiveRepository reactiveCrudRepository;
    @Mock
    private Mono<TestEntity> mockMono;
    @Mock
    private Flux<TestEntity> mockFlux;
    @Mock
    private Mono<List<TestEntity>> listMono;
    @Mock
    private TestRxJava2SortingRepository testRxJava2SortingRepository;
    @Mock
    private Maybe<TestEntity> mockMaybe;
    @Mock
    private Flowable<TestEntity> mockFlowable;
    @Mock
    private Single<List<TestEntity>> mockSingle;
    @Mock
    private TestRxJava3SortingRepository testRxJava3SortingRepository;
    @Mock
    private io.reactivex.rxjava3.core.Maybe<TestEntity> mockMaybe2;
    @Mock
    private io.reactivex.rxjava3.core.Flowable<TestEntity> mockFlowable2;
    @Mock
    private io.reactivex.rxjava3.core.Single<List<TestEntity>> mockSingle2;

    @Mock
    private TestQuerydslPredicateExecutorRepository querydslRepository;

    @Mock
    private TestReactiveQuerydslPredicateExecutorRepository reactiveQuerydslRepository;

    @Mock
    private Page<TestEntity> mockPage;

    @BeforeClass
    public void prepareClass() {
        openMocks(this);
        when(testRepository.findById(1L)).thenReturn(ofNullable(TEST_ENTITIES.get(0)));
        when(testRepository.findAllById(of(1L, 2L))).thenReturn(TEST_ENTITIES);

        when(reactiveCrudRepository.findById(1L)).thenReturn(mockMono);
        when(mockMono.block()).thenReturn(TEST_ENTITIES.get(0));
        when(reactiveCrudRepository.findAllById(of(1L, 2L))).thenReturn(mockFlux);
        when(mockFlux.collectList()).thenReturn(listMono);
        when(listMono.block()).thenReturn(TEST_ENTITIES);

        when(testRxJava2SortingRepository.findById(1L)).thenReturn(mockMaybe);
        when(mockMaybe.blockingGet()).thenReturn(TEST_ENTITIES.get(0));
        when(testRxJava2SortingRepository.findAllById(of(1L, 2L))).thenReturn(mockFlowable);
        when(mockFlowable.toList()).thenReturn(mockSingle);
        when(mockSingle.blockingGet()).thenReturn(TEST_ENTITIES);

        when(testRxJava3SortingRepository.findById(1L)).thenReturn(mockMaybe2);
        when(mockMaybe2.blockingGet()).thenReturn(TEST_ENTITIES.get(0));
        when(testRxJava3SortingRepository.findAllById(of(1L, 2L))).thenReturn(mockFlowable2);
        when(mockFlowable2.toList()).thenReturn(mockSingle2);
        when(mockSingle2.blockingGet()).thenReturn(TEST_ENTITIES);

        when(testRepository.findAll(any(Sort.class))).thenReturn(TEST_ENTITIES);
        when(reactiveCrudRepository.findAll(any(Sort.class))).thenReturn(mockFlux);
        when(testRxJava2SortingRepository.findAll(any(Sort.class))).thenReturn(mockFlowable);
        when(testRxJava3SortingRepository.findAll(any(Sort.class))).thenReturn(mockFlowable2);

        when(testRepository.findAll(any(PageRequest.class))).thenReturn(mockPage);
        when(mockPage.getContent()).thenReturn(TEST_ENTITIES);

        when(testRepository.findOne(any(Example.class))).thenReturn(ofNullable(TEST_ENTITIES.get(0)));
        when(reactiveCrudRepository.findOne(any(Example.class))).thenReturn(mockMono);

        when(testRepository.findAll(any(Example.class))).thenReturn(TEST_ENTITIES);
        when(testRepository.findAll(any(Example.class), any(Sort.class))).thenReturn(TEST_ENTITIES);

        when(reactiveCrudRepository.findAll(any(Example.class))).thenReturn(mockFlux);
        when(reactiveCrudRepository.findAll(any(Example.class), any(Sort.class))).thenReturn(mockFlux);

        when(testRepository.findSomething(any(boolean.class), any(String.class), any(int.class))).thenReturn(TEST_ENTITIES.get(0));
        when(testRepository.findEntities(any(boolean.class), any(String.class), any(int.class))).thenReturn(TEST_ENTITIES);

        when(querydslRepository.findOne(any(Predicate.class))).thenReturn(ofNullable(TEST_ENTITIES.get(0)));
        when(querydslRepository.findAll(any(Predicate.class))).thenReturn(TEST_ENTITIES);

        when(reactiveQuerydslRepository.findOne(any(Predicate.class))).thenReturn(mockMono);
        when(reactiveQuerydslRepository.findAll(any(Predicate.class))).thenReturn(mockFlux);

        when(querydslRepository.findAll(any(Predicate.class), any(Sort.class))).thenReturn(TEST_ENTITIES);
        when(reactiveQuerydslRepository.findAll(any(Predicate.class), any(Sort.class))).thenReturn(mockFlux);

        when(querydslRepository.findAll(any(Predicate.class),
                any(OrderSpecifier.class),
                any(OrderSpecifier.class),
                any(OrderSpecifier.class)))
                .thenReturn(TEST_ENTITIES);

        when(querydslRepository.findAll(any(OrderSpecifier.class),
                any(OrderSpecifier.class),
                any(OrderSpecifier.class)))
                .thenReturn(TEST_ENTITIES);

        when(reactiveQuerydslRepository.findAll(any(Predicate.class),
                any(OrderSpecifier.class),
                any(OrderSpecifier.class),
                any(OrderSpecifier.class))).thenReturn(mockFlux);
        when(reactiveQuerydslRepository.findAll(
                any(OrderSpecifier.class),
                any(OrderSpecifier.class),
                any(OrderSpecifier.class))).thenReturn(mockFlux);

        when(querydslRepository.findAll(any(Predicate.class),
                any(QPageRequest.class)))
                .thenReturn(mockPage);

        when(testRepository.findAll()).thenReturn(TEST_ENTITIES);
        when(reactiveCrudRepository.findAll()).thenReturn(mockFlux);
        when(testRxJava2SortingRepository.findAll()).thenReturn(mockFlowable);
        when(testRxJava3SortingRepository.findAll()).thenReturn(mockFlowable2);
    }

    @Test
    public void selectByIdTest() {
        var entity = springData().select("Test entity",
                byId(testRepository, 1L));

        assertThat(entity, is(TEST_ENTITIES.get(0)));
    }

    @Test
    public void selectByIdTest2() {
        var entity = springData().select("Test entity",
                byId(reactiveCrudRepository, 1L));

        assertThat(entity, is(TEST_ENTITIES.get(0)));
    }

    @Test
    public void selectByIdTest3() {
        var entity = springData().select("Test entity",
                byId(testRxJava2SortingRepository, 1L));

        assertThat(entity, is(TEST_ENTITIES.get(0)));
    }

    @Test
    public void selectByIdTest4() {
        var entity = springData().select("Test entity",
                byId(testRxJava3SortingRepository, 1L));

        assertThat(entity, is(TEST_ENTITIES.get(0)));
    }

    @Test
    public void selectByIdSTest() {
        var entities = springData().select("Test entities",
                byIds(testRepository, 1L, 2L));

        assertThat(entities, is(TEST_ENTITIES));
    }

    @Test
    public void selectByIdSTest2() {
        var entities = springData().select("Test entities",
                byIds(reactiveCrudRepository, 1L, 2L));

        assertThat(entities, is(TEST_ENTITIES));
    }

    @Test
    public void selectByIdSTest3() {
        var entities = springData().select("Test entities",
                byIds(testRxJava2SortingRepository, 1L, 2L));

        assertThat(entities, is(TEST_ENTITIES));
    }

    @Test
    public void selectByIdSTest4() {
        var entities = springData().select("Test entities",
                byIds(testRxJava3SortingRepository, 1L, 2L));

        assertThat(entities, is(TEST_ENTITIES));
    }

    @Test
    public void selectBySortingTest() {
        var entities = springData().select("Test entities",
                allBySorting(testRepository, ASC, "id", "name"));

        assertThat(entities, is(TEST_ENTITIES));
    }

    @Test
    public void selectBySortingTest2() {
        var entities = springData().select("Test entities",
                allBySorting(reactiveCrudRepository, ASC, "id", "name"));

        assertThat(entities, is(TEST_ENTITIES));
    }

    @Test
    public void selectBySortingTest3() {
        var entities = springData().select("Test entities",
                allBySorting(testRxJava2SortingRepository, ASC, "id", "name"));

        assertThat(entities, is(TEST_ENTITIES));
    }

    @Test
    public void selectBySortingTest4() {
        var entities = springData().select("Test entities",
                allBySorting(testRxJava3SortingRepository, ASC, "id", "name"));

        assertThat(entities, is(TEST_ENTITIES));
    }

    @Test
    public void selectByPageable() {
        var entities = springData().select("Test entities", asAPage(testRepository)
                .number(0)
                .size(5)
                .sort(by("id").descending().and(by("name"))));
        assertThat(entities, is(TEST_ENTITIES));
    }

    @Test
    public void selectOneByExampleTest() {
        var entity = springData().select("Test entity",
                byExample(testRepository, new TestEntity())
                        .matcher(m -> m.withMatcher("firstName", ignoreCase()))
                        .matcher(m -> m.withMatcher("lastName", ignoreCase()))
                        .initialMatcher(matchingAny()));

        assertThat(entity, is(TEST_ENTITIES.get(0)));
    }

    @Test
    public void selectOneByExampleTest2() {
        var entity = springData().select("Test entity",
                byExample(reactiveCrudRepository, new TestEntity())
                        .matcher(m -> m.withMatcher("firstName", ignoreCase()))
                        .matcher(m -> m.withMatcher("lastName", ignoreCase()))
                        .initialMatcher(matchingAny()));

        assertThat(entity, is(TEST_ENTITIES.get(0)));
    }

    @Test
    public void selectAllByExampleTest() {
        var entity = springData().select("Test entities",
                allByExample(testRepository, new TestEntity())
                        .matcher(m -> m.withMatcher("firstName", ignoreCase()))
                        .matcher(m -> m.withMatcher("lastName", ignoreCase()))
                        .initialMatcher(matchingAny()));

        assertThat(entity, is(TEST_ENTITIES));
    }

    @Test
    public void selectAllByExampleTest2() {
        var entity = springData().select("Test entities",
                allByExample(testRepository, new TestEntity())
                        .matcher(m -> m.withMatcher("firstName", ignoreCase()))
                        .matcher(m -> m.withMatcher("lastName", ignoreCase()))
                        .initialMatcher(matchingAny())
                        .sorting(ASC, "id", "name"));

        assertThat(entity, is(TEST_ENTITIES));
    }

    @Test
    public void selectAllByExampleTest3() {
        var entity = springData().select("Test entities",
                allByExample(reactiveCrudRepository, new TestEntity())
                        .matcher(m -> m.withMatcher("firstName", ignoreCase()))
                        .matcher(m -> m.withMatcher("lastName", ignoreCase()))
                        .initialMatcher(matchingAny()));

        assertThat(entity, is(TEST_ENTITIES));
    }

    @Test
    public void selectAllByExampleTest4() {
        var entity = springData().select("Test entities",
                allByExample(reactiveCrudRepository, new TestEntity())
                        .matcher(m -> m.withMatcher("firstName", ignoreCase()))
                        .matcher(m -> m.withMatcher("lastName", ignoreCase()))
                        .initialMatcher(matchingAny())
                        .sorting(ASC, "id", "name"));

        assertThat(entity, is(TEST_ENTITIES));
    }

    @Test
    public void selectOneByInvocationTest() {
        var entity = springData().select("Test entity",
                byInvocation(testRepository, r -> r.findSomething(true, "ABCD", 123)));

        assertThat(entity, is(TEST_ENTITIES.get(0)));
    }

    @Test
    public void selectAllByInvocationTest() {
        var entities = springData().select("Test entities",
                allByInvocation(testRepository, r -> r.findEntities(true, "ABCD", 123)));

        assertThat(entities, is(TEST_ENTITIES));
    }

    @Test
    public void selectByPredicateTest() {
        var q = QTestEntity.qTestEntity;
        var predicate = q.id.eq(1L)
                .and(q.name.eq("Test name"))
                .and(q.listData.contains("A"));

        var entity = springData().select("Test entity",
                byPredicate(querydslRepository, predicate));

        assertThat(entity, is(TEST_ENTITIES.get(0)));
    }

    @Test
    public void selectByPredicateTest2() {
        var q = QTestEntity.qTestEntity;
        var predicate = q.id.eq(1L)
                .and(q.name.eq("Test name"))
                .and(q.listData.contains("A"));

        var entity = springData().select("Test entity",
                byPredicate(reactiveQuerydslRepository, predicate));

        assertThat(entity, is(TEST_ENTITIES.get(0)));
    }

    @Test
    public void selectAllByPredicateTest() {
        var q = QTestEntity.qTestEntity;
        var predicate = q.id.eq(1L)
                .and(q.name.eq("Test name"))
                .and(q.listData.contains("A"));

        var entities = springData().select("Test entities",
                allByPredicate(querydslRepository, predicate));

        assertThat(entities, is(TEST_ENTITIES));
    }

    @Test
    public void selectAllByPredicateTest2() {
        var q = QTestEntity.qTestEntity;
        var predicate = q.id.eq(1L)
                .and(q.name.eq("Test name"))
                .and(q.listData.contains("A"));

        var entities = springData().select("Test entities",
                allByPredicate(reactiveQuerydslRepository, predicate));

        assertThat(entities, is(TEST_ENTITIES));
    }

    @Test
    public void selectAllByPredicateAndSortTest() {
        var q = QTestEntity.qTestEntity;
        var predicate = q.id.eq(1L)
                .and(q.name.eq("Test name"))
                .and(q.listData.contains("A"));

        var entities = springData().select("Test entities",
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

        var entities = springData().select("Test entities",
                allByPredicate(reactiveQuerydslRepository,
                        predicate,
                        ASC, "id", "name"));

        assertThat(entities, is(TEST_ENTITIES));
    }

    @Test
    public void selectAllOrderedTest() {
        var q = QTestEntity.qTestEntity;

        var entities = springData().select("Test entities",
                allOrdered(querydslRepository, Order.ASC, q.id)
                        .orderSpecifier(Order.DESC, q.name)
                        .orderSpecifier(Order.ASC, q.arrayData));

        assertThat(entities, is(TEST_ENTITIES));
    }

    @Test
    public void selectAllOrderedTest2() {
        var q = QTestEntity.qTestEntity;

        var entities = springData().select("Test entities",
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

        var entities = springData().select("Test entities",
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

        var entities = springData().select("Test entities",
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

        var entities = springData().select("Test entities",
                asAPageByPredicate(querydslRepository, predicate)
                        .number(0)
                        .size(5)
                        .orderSpecifier(Order.DESC, q.name)
                        .orderSpecifier(Order.ASC, q.arrayData));

        assertThat(entities, is(TEST_ENTITIES));
    }

    @Test
    public void selectAllTest() {
        var entities = springData().select("Test entities",
                all(testRepository));

        assertThat(entities, is(TEST_ENTITIES));
    }

    @Test
    public void selectAllTest2() {
        var entities = springData().select("Test entities",
                all(reactiveCrudRepository));

        assertThat(entities, is(TEST_ENTITIES));
    }

    @Test
    public void selectAllTest3() {
        var entities = springData().select("Test entities",
                all(testRxJava2SortingRepository));

        assertThat(entities, is(TEST_ENTITIES));
    }

    @Test
    public void selectAllTest4() {
        var entities = springData().select("Test entities",
                all(testRxJava3SortingRepository));

        assertThat(entities, is(TEST_ENTITIES));
    }
}
