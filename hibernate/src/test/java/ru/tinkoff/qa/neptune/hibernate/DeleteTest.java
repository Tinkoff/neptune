package ru.tinkoff.qa.neptune.hibernate;

import org.hibernate.query.Query;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.hibernate.model.TestEntity;

import javax.persistence.criteria.CriteriaDelete;

import static org.mockito.Mockito.*;
import static ru.tinkoff.qa.neptune.hibernate.HibernateContext.hibernate;
import static ru.tinkoff.qa.neptune.hibernate.select.common.CommonSelectStepFactory.byId;
import static ru.tinkoff.qa.neptune.hibernate.select.common.CommonSelectStepFactory.byIds;

public class DeleteTest extends BaseHibernateTest {

    @Mock
    private CriteriaDelete<TestEntity> criteriaDelete;
    @Mock
    private Query<TestEntity> query;

    @BeforeClass
    public void prepareClass() {
        super.prepareClass();

        when(criteriaBuilder.createCriteriaDelete(TestEntity.class)).thenReturn(criteriaDelete);
        when(session.createQuery(criteriaDelete)).thenReturn(query);
    }

    @Test
    public void deleteOneByQueryTest() {
        hibernate().delete("Test entity", byId(TestEntity.class, 1L));
        verify(session, times(1)).delete(TEST_ENTITIES.get(0));
    }

    @Test
    public void deleteAllByQueryTest() {
        hibernate().delete("Test entities", byIds(TestEntity.class, 1L, 2L));
        verify(session, times(1)).delete(TEST_ENTITIES.get(0));
        verify(session, times(1)).delete(TEST_ENTITIES.get(1));
    }

    @Test
    public void deleteOneTest() {
        hibernate().delete("Test entity", TEST_ENTITIES.get(0));
        verify(session, times(1)).delete(TEST_ENTITIES.get(0));
    }

    @Test
    public void deleteVarArgTest() {
        hibernate().delete("Test entities", TEST_ENTITIES.get(0), TEST_ENTITIES.get(1));
        verify(session, times(1)).delete(TEST_ENTITIES.get(0));
        verify(session, times(1)).delete(TEST_ENTITIES.get(1));
    }

    @Test
    public void deleteIterableTest() {
        hibernate().delete("Test entities", TEST_ENTITIES);
        verify(session, times(1)).delete(TEST_ENTITIES.get(0));
        verify(session, times(1)).delete(TEST_ENTITIES.get(1));
    }

    @Test
    public void deleteAllTest() {
        hibernate().deleteAllFrom(TestEntity.class);
        verify(session, times(1)).createQuery(criteriaDelete);
    }

    @AfterMethod(alwaysRun = true)
    public void clearInvocations() {
        Mockito.clearInvocations(session);
    }
}
