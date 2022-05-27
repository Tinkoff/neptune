package ru.tinkoff.qa.neptune.hibernate;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.hibernate.HibernateQuery;
import org.hibernate.query.Query;
import org.mockito.MockedConstruction;
import org.mockito.Mockito;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.hibernate.model.QTestEntity;
import ru.tinkoff.qa.neptune.hibernate.model.TestEntity;

import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockConstruction;
import static org.mockito.Mockito.when;
import static ru.tinkoff.qa.neptune.hibernate.HibernateContext.hibernate;
import static ru.tinkoff.qa.neptune.hibernate.select.common.CommonSelectStepFactory.*;
import static ru.tinkoff.qa.neptune.hibernate.select.querydsl.QueryDSLSelectStepFactory.allByPredicate;
import static ru.tinkoff.qa.neptune.hibernate.select.querydsl.QueryDSLSelectStepFactory.allOrdered;
import static ru.tinkoff.qa.neptune.hibernate.select.querydsl.QueryDSLSelectStepFactory.asAPageByPredicate;
import static ru.tinkoff.qa.neptune.hibernate.select.querydsl.QueryDSLSelectStepFactory.byPredicate;

@SuppressWarnings("rawtypes, unchecked")
public class SelectTest extends BaseHibernatePreparations {

    private static final List<TestEntity> TEST_ENTITIES_COPY = new ArrayList<>(List.copyOf(TEST_ENTITIES));

    private CriteriaQuery<TestEntity> orderedCriteriaQuery;
    private Query<TestEntity> selectAllQuery;
    private Query<TestEntity> selectSingleQuery;
    private Query<TestEntity> selectMultipleQuery;
    private Query<TestEntity> selectOrderedQuery;
    private Root<TestEntity> root;
    private Path<Object> idPath;
    private Path<Object> namePath;
    private Predicate expression;
    private javax.persistence.criteria.Order order;
    private HibernateQuery returnOneQuery;
    private HibernateQuery returnManyQuery;

    @Override
    @BeforeClass
    public void prepareClass() {
        super.prepareClass();

        orderedCriteriaQuery = mock(CriteriaQuery.class);
        selectAllQuery = mock(Query.class);
        selectSingleQuery = mock(Query.class);
        selectMultipleQuery = mock(Query.class);
        selectOrderedQuery = mock(Query.class);
        root = mock(Root.class);
        idPath = mock(Path.class);
        namePath = mock(Path.class);
        expression = mock(Predicate.class);
        order = mock(javax.persistence.criteria.Order.class);
        returnOneQuery = mock(HibernateQuery.class);
        returnManyQuery = mock(HibernateQuery.class);

        when(criteriaBuilder.createQuery(TestEntity.class)).thenReturn(criteriaQuery);
        when(criteriaQuery.from(TestEntity.class)).thenReturn(root);
        when(root.get("id")).thenReturn(idPath);
        when(root.get("name")).thenReturn(namePath);
        when(criteriaBuilder.ge((Path) idPath, 1L)).thenReturn(expression);
        when(criteriaBuilder.desc(namePath)).thenReturn(order);
        when(criteriaQuery.where(expression)).thenReturn(criteriaQuery);

        when(session.createQuery(criteriaQuery)).thenReturn(selectAllQuery);
        when(selectAllQuery.getResultList()).thenReturn(TEST_ENTITIES);
        when(selectAllQuery.getSingleResult()).thenReturn(TEST_ENTITIES.get(0));

        when(session.createQuery("select e from test_entities e where id = 1", TestEntity.class)).thenReturn(selectSingleQuery);
        when(selectSingleQuery.getSingleResult()).thenReturn(TEST_ENTITIES.get(0));

        when(session.createQuery("select e from test_entities e", TestEntity.class)).thenReturn(selectMultipleQuery);
        when(selectMultipleQuery.getResultList()).thenReturn(TEST_ENTITIES);

        when(criteriaQuery.orderBy(List.of(order))).thenReturn(orderedCriteriaQuery);
        when(session.createQuery(orderedCriteriaQuery)).thenReturn(selectOrderedQuery);
        when(selectOrderedQuery.getResultList()).thenReturn(TEST_ENTITIES_COPY);

        when(returnOneQuery.from(QTestEntity.qTestEntity)).thenReturn(returnOneQuery);
        when(returnManyQuery.from(QTestEntity.qTestEntity)).thenReturn(returnManyQuery);
        when(returnOneQuery.fetchOne()).thenReturn(TEST_ENTITIES.get(0));
        when(returnManyQuery.fetch()).thenReturn(TEST_ENTITIES);

        Collections.reverse(TEST_ENTITIES_COPY);
    }

    @Test
    public void selectByIdTest() {
        try (var mockedStatic = Mockito.mockStatic(Persistence.class)) {
            mockPersistence(mockedStatic);

            var entity = hibernate().select("Test entity",
                    byId(TestEntity.class, 1L));

            assertThat(entity, is(TEST_ENTITIES.get(0)));
        }
    }

    @Test
    public void selectByIdsTest() {
        try (var mockedStatic = Mockito.mockStatic(Persistence.class)) {
            mockPersistence(mockedStatic);

            var entities = hibernate().select("Test entities",
                    byIds(TestEntity.class, 1L, 2L));

            assertThat(entities, is(TEST_ENTITIES));
        }
    }

    @Test
    public void selectByPageableTest() {
        try (var mockedStatic = Mockito.mockStatic(Persistence.class)) {
            mockPersistence(mockedStatic);

            var entities = hibernate().select("Test entities", asAPage(TestEntity.class)
                    .limit(0)
                    .offset(5));
            assertThat(entities, is(TEST_ENTITIES));
        }
    }

    @Test
    public void selectByPredicateTest() {
        try (var mockedStatic = Mockito.mockStatic(Persistence.class)) {
            mockPersistence(mockedStatic);

            var q = QTestEntity.qTestEntity;
            var predicate = q.id.eq(1L)
                    .and(q.name.eq("Test name"))
                    .and(q.listData.contains("A"));

            try (MockedConstruction<HibernateQuery> ignored = mockConstruction(HibernateQuery.class, (mock, context) -> {
                when(mock.select(q)).thenReturn(returnOneQuery);
                when(returnOneQuery.from(q)).thenReturn(returnOneQuery);
                when(returnOneQuery.where(predicate)).thenReturn(returnOneQuery);
            })) {
                var entity = hibernate().select("Test entity",
                        byPredicate(TestEntity.class, q, predicate));

                assertThat(entity, is(TEST_ENTITIES.get(0)));
            }
        }
    }

    @Test
    public void selectAllByPredicateTest() {
        try (var mockedStatic = Mockito.mockStatic(Persistence.class)) {
            mockPersistence(mockedStatic);

            var q = QTestEntity.qTestEntity;
            var predicate = q.id.eq(1L)
                    .and(q.name.eq("Test name"))
                    .and(q.listData.contains("A"));

            try (MockedConstruction<HibernateQuery> ignored = mockConstruction(HibernateQuery.class, (mock, context) -> {
                    when(mock.select(q)).thenReturn(returnManyQuery);
                    when(returnManyQuery.from(q)).thenReturn(returnManyQuery);
                    when(returnOneQuery.where(predicate)).thenReturn(returnManyQuery);
            })) {
                var entities = hibernate().select("Test entities",
                        allByPredicate(TestEntity.class, q, predicate));

                assertThat(entities, is(TEST_ENTITIES));
            }
        }
    }

    @Test
    public void selectAllOrderedTest() {
        try (var mockedStatic = Mockito.mockStatic(Persistence.class)) {
            mockPersistence(mockedStatic);

            var q = QTestEntity.qTestEntity;

            try (MockedConstruction<HibernateQuery> ignored = mockConstruction(HibernateQuery.class, (mock, context) -> {
                        when(mock.from(q)).thenReturn(returnManyQuery);
                        when(returnManyQuery.orderBy(new OrderSpecifier<>(Order.ASC, q.id),
                                new OrderSpecifier<>(Order.DESC, q.name),
                                new OrderSpecifier<>(Order.ASC, q.arrayData))).thenReturn(returnManyQuery);
                    }
            )) {
                var entities = hibernate().select("Test entities",
                        allOrdered(TestEntity.class, q, Order.ASC, q.id)
                                .orderSpecifier(Order.DESC, q.name)
                                .orderSpecifier(Order.ASC, q.arrayData));

                assertThat(entities, is(TEST_ENTITIES));
            }
        }
    }

    @Test
    public void selectAllOrderedTest2() {
        try (var mockedStatic = Mockito.mockStatic(Persistence.class)) {
            mockPersistence(mockedStatic);

            var q = QTestEntity.qTestEntity;
            var predicate = q.id.eq(1L)
                    .and(q.name.eq("Test name"))
                    .and(q.listData.contains("A"));

            try (MockedConstruction<HibernateQuery> ignored = mockConstruction(HibernateQuery.class, (mock, context) -> {
                when(mock.from(q)).thenReturn(returnManyQuery);
                when(returnManyQuery.select(predicate)).thenReturn(returnManyQuery);
                when(returnManyQuery.orderBy(new OrderSpecifier<>(Order.ASC, q.id),
                        new OrderSpecifier<>(Order.DESC, q.name),
                        new OrderSpecifier<>(Order.ASC, q.arrayData))).thenReturn(returnManyQuery);
            })) {
                var entities = hibernate().select("Test entities",
                        allOrdered(TestEntity.class, q, Order.ASC, q.id)
                                .orderSpecifier(Order.DESC, q.name)
                                .orderSpecifier(Order.ASC, q.arrayData)
                                .predicate(predicate));

                assertThat(entities, is(TEST_ENTITIES));
            }
        }
    }

    @Test
    public void selectAllAsPageByPredicateTest() {
        try (var mockedStatic = Mockito.mockStatic(Persistence.class)) {
            mockPersistence(mockedStatic);

            var q = QTestEntity.qTestEntity;
            var predicate = q.id.eq(1L)
                    .and(q.name.eq("Test name"))
                    .and(q.listData.contains("A"));

            try (MockedConstruction<HibernateQuery> ignored = mockConstruction(HibernateQuery.class, (mock, context) -> {
                when(mock.select(q)).thenReturn(returnManyQuery);
                when(returnManyQuery.from(q)).thenReturn(returnManyQuery);
                when(returnManyQuery.where(predicate)).thenReturn(returnManyQuery);
                when(returnManyQuery.orderBy(new OrderSpecifier<>(Order.DESC, q.name),
                        new OrderSpecifier<>(Order.ASC, q.arrayData))).thenReturn(returnManyQuery);
                when(returnManyQuery.limit(5)).thenReturn(returnManyQuery);
                when(returnManyQuery.offset(0)).thenReturn(returnManyQuery);
            })) {
                var entities = hibernate().select("Test entities",
                        asAPageByPredicate(TestEntity.class, q, predicate)
                                .limit(5)
                                .offset(0)
                                .orderSpecifier(Order.DESC, q.name)
                                .orderSpecifier(Order.ASC, q.arrayData));

                assertThat(entities, is(TEST_ENTITIES));
            }
        }
    }

    @Test
    public void selectAllTest() {
        try (var mockedStatic = Mockito.mockStatic(Persistence.class)) {
            mockPersistence(mockedStatic);

            var entities = hibernate().select("Test entities",
                    all(TestEntity.class));

            assertThat(entities, is(TEST_ENTITIES));
        }
    }

    @Test
    public void selectByQueryTest() {
        try (var mockedStatic = Mockito.mockStatic(Persistence.class)) {
            mockPersistence(mockedStatic);

            var entity = hibernate().select("Test entity",
                    byQuery(TestEntity.class, "select e from test_entities e where id = 1"));

            assertThat(entity, is(TEST_ENTITIES.get(0)));
        }
    }

    @Test
    public void selectAllByQueryTest() {
        try (var mockedStatic = Mockito.mockStatic(Persistence.class)) {
            mockPersistence(mockedStatic);

            var entities = hibernate().select("Test entities",
                    allByQuery(TestEntity.class, "select e from test_entities e"));

            assertThat(entities, is(TEST_ENTITIES));
        }
    }

    @Test
    public void selectByCriteriaTest() {
        try (var mockedStatic = Mockito.mockStatic(Persistence.class)) {
            mockPersistence(mockedStatic);

            var cb = hibernate().getCriteriaBuilder(TestEntity.class);
            var cq = hibernate().getCriteriaQuery(TestEntity.class);
            var root = cq.from(TestEntity.class);

            var entity = hibernate().select("Test entity",
                    byCriteria(TestEntity.class, cq.where(
                            cb.ge(root.get("id"), 1L)
                    )));

            assertThat(entity, is(TEST_ENTITIES.get(0)));
        }
    }

    @Test
    public void selectAllByCriteriaTest() {
        try (var mockedStatic = Mockito.mockStatic(Persistence.class)) {
            mockPersistence(mockedStatic);

            var cb = hibernate().getCriteriaBuilder(TestEntity.class);
            var cq = hibernate().getCriteriaQuery(TestEntity.class);
            var root = cq.from(TestEntity.class);

            var entities = hibernate().select("Test entities",
                    allByCriteria(TestEntity.class, cq.where(
                            cb.ge(root.get("id"), 1L)
                    )));

            assertThat(entities, is(TEST_ENTITIES));
        }
    }

    @Test
    public void selectCriteriaOrderedTest() {
        try (var mockedStatic = Mockito.mockStatic(Persistence.class)) {
            mockPersistence(mockedStatic);

            var cb = hibernate().getCriteriaBuilder(TestEntity.class);
            var cq = hibernate().getCriteriaQuery(TestEntity.class);
            var root = cq.from(TestEntity.class);

            var entities = hibernate().select("Test entities",
                    allByOrder(TestEntity.class, cb.desc(root.get("name"))));

            assertThat(entities, is(TEST_ENTITIES_COPY));
        }
    }
}
