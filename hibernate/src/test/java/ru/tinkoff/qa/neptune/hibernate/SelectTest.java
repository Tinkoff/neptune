package ru.tinkoff.qa.neptune.hibernate;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.hibernate.HibernateQuery;
import org.hibernate.query.Query;
import org.mockito.Mock;
import org.mockito.MockedConstruction;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.hibernate.model.QTestEntity;
import ru.tinkoff.qa.neptune.hibernate.model.TestEntity;

import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mockConstruction;
import static org.mockito.Mockito.when;
import static ru.tinkoff.qa.neptune.hibernate.HibernateContext.hibernate;
import static ru.tinkoff.qa.neptune.hibernate.select.common.CommonSelectStepFactory.*;
import static ru.tinkoff.qa.neptune.hibernate.select.querydsl.QueryDSLSelectStepFactory.*;

@SuppressWarnings("rawtypes, unchecked")
public class SelectTest extends BaseHibernateTest {

    private static final List<TestEntity> TEST_ENTITIES_COPY = new ArrayList<>(List.copyOf(TEST_ENTITIES));

    @Mock
    private CriteriaQuery<TestEntity> orderedCriteriaQuery;
    @Mock
    private Query<TestEntity> selectAllQuery;
    @Mock
    private Query<TestEntity> selectSingleQuery;
    @Mock
    private Query<TestEntity> selectMultipleQuery;
    @Mock
    private Query<TestEntity> selectOrderedQuery;
    @Mock
    private Root<TestEntity> root;
    @Mock
    private Path<Object> idPath;
    @Mock
    private Path<Object> namePath;
    @Mock
    private Predicate expression;
    @Mock
    private javax.persistence.criteria.Order order;
    @Mock
    private HibernateQuery returnOneQuery;
    @Mock
    private HibernateQuery returnManyQuery;

    @BeforeClass
    public void prepareClass() {
        super.prepareClass();

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
        var entity = hibernate().select("Test entity",
                byId(TestEntity.class, 1L));

        assertThat(entity, is(TEST_ENTITIES.get(0)));
    }

    @Test
    public void selectByIdsTest() {
        var entities = hibernate().select("Test entities",
                byIds(TestEntity.class, 1L, 2L));

        assertThat(entities, is(TEST_ENTITIES));
    }

    @Test
    public void selectByPageableTest() {
        var entities = hibernate().select("Test entities", asAPage(TestEntity.class)
                .limit(0)
                .offset(5));
        assertThat(entities, is(TEST_ENTITIES));
    }

    @Test
    public void selectByPredicateTest() {
        var q = QTestEntity.qTestEntity;
        var predicate = q.id.eq(1L)
                .and(q.name.eq("Test name"))
                .and(q.listData.contains("A"));

        try (MockedConstruction<HibernateQuery> ignored = mockConstruction(HibernateQuery.class, (mock, context) ->
                when(mock.select(predicate)).thenReturn(returnOneQuery))) {
            var entity = hibernate().select("Test entity",
                    byPredicate(TestEntity.class, q, predicate));

            assertThat(entity, is(TEST_ENTITIES.get(0)));
        }
    }

    @Test
    public void selectAllByPredicateTest() {
        var q = QTestEntity.qTestEntity;
        var predicate = q.id.eq(1L)
                .and(q.name.eq("Test name"))
                .and(q.listData.contains("A"));

        try (MockedConstruction<HibernateQuery> ignored = mockConstruction(HibernateQuery.class, (mock, context) ->
                when(mock.select(predicate)).thenReturn(returnManyQuery))) {
            var entities = hibernate().select("Test entities",
                    allByPredicate(TestEntity.class, q, predicate));

            assertThat(entities, is(TEST_ENTITIES));
        }
    }

    @Test
    public void selectAllOrderedTest() {
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

    @Test
    public void selectAllOrderedTest2() {
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

    @Test
    public void selectAllAsPageByPredicateTest() {
        var q = QTestEntity.qTestEntity;
        var predicate = q.id.eq(1L)
                .and(q.name.eq("Test name"))
                .and(q.listData.contains("A"));

        try (MockedConstruction<HibernateQuery> ignored = mockConstruction(HibernateQuery.class, (mock, context) -> {
            when(mock.select(predicate)).thenReturn(returnManyQuery);
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

    @Test
    public void selectAllTest() {
        var entities = hibernate().select("Test entities",
                all(TestEntity.class));

        assertThat(entities, is(TEST_ENTITIES));
    }

    @Test
    public void selectByQueryTest() {
        var entity = hibernate().select("Test entity",
                byQuery(TestEntity.class, "select e from test_entities e where id = 1"));

        assertThat(entity, is(TEST_ENTITIES.get(0)));
    }

    @Test
    public void selectAllByQueryTest() {
        var entities = hibernate().select("Test entities",
                allByQuery(TestEntity.class, "select e from test_entities e"));

        assertThat(entities, is(TEST_ENTITIES));
    }

    @Test
    public void selectByCriteriaTest() {
        var cb = hibernate().getCriteriaBuilder(TestEntity.class);
        var cq = hibernate().getCriteriaQuery(TestEntity.class);
        var root = cq.from(TestEntity.class);

        var entity = hibernate().select("Test entity",
                byCriteria(TestEntity.class, cq.where(
                        cb.ge(root.get("id"), 1L)
                )));

        assertThat(entity, is(TEST_ENTITIES.get(0)));
    }

    @Test
    public void selectAllByCriteriaTest() {
        var cb = hibernate().getCriteriaBuilder(TestEntity.class);
        var cq = hibernate().getCriteriaQuery(TestEntity.class);
        var root = cq.from(TestEntity.class);

        var entities = hibernate().select("Test entities",
                allByCriteria(TestEntity.class, cq.where(
                        cb.ge(root.get("id"), 1L)
                )));

        assertThat(entities, is(TEST_ENTITIES));
    }

    @Test
    public void selectCriteriaOrderedTest() {
        var cb = hibernate().getCriteriaBuilder(TestEntity.class);
        var cq = hibernate().getCriteriaQuery(TestEntity.class);
        var root = cq.from(TestEntity.class);

        var entities = hibernate().select("Test entities",
                allByOrder(TestEntity.class, cb.desc(root.get("name"))));

        assertThat(entities, is(TEST_ENTITIES_COPY));
    }
}
