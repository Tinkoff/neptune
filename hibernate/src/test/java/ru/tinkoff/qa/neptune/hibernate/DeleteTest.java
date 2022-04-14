package ru.tinkoff.qa.neptune.hibernate;

import org.mockito.Mockito;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.hibernate.model.TestEntity;

import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaDelete;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static ru.tinkoff.qa.neptune.hibernate.HibernateContext.hibernate;
import static ru.tinkoff.qa.neptune.hibernate.select.common.CommonSelectStepFactory.byId;
import static ru.tinkoff.qa.neptune.hibernate.select.common.CommonSelectStepFactory.byIds;

@SuppressWarnings("unchecked")
public class DeleteTest extends BaseHibernatePreparations {

    private CriteriaDelete<TestEntity> criteriaDelete;

    @Override
    @BeforeClass
    public void prepareClass() {
        super.prepareClass();

        criteriaDelete = mock(CriteriaDelete.class);

        when(criteriaBuilder.createCriteriaDelete(TestEntity.class)).thenReturn(criteriaDelete);
        when(session.createQuery(criteriaDelete)).thenReturn(query);
    }

    @Test
    public void deleteOneByQueryTest() {
        try (var mockedStatic = Mockito.mockStatic(Persistence.class)) {
            mockPersistence(mockedStatic);

            hibernate().delete("Test entity", byId(TestEntity.class, 1L));
            verify(session, times(1)).delete(TEST_ENTITIES.get(0));
        }
    }

    @Test
    public void deleteAllByQueryTest() {
        try (var mockedStatic = Mockito.mockStatic(Persistence.class)) {
            mockPersistence(mockedStatic);

            hibernate().delete("Test entities", byIds(TestEntity.class, 1L, 2L));
            verify(session, times(1)).delete(TEST_ENTITIES.get(0));
            verify(session, times(1)).delete(TEST_ENTITIES.get(1));
        }
    }

    @Test
    public void deleteOneTest() {
        try (var mockedStatic = Mockito.mockStatic(Persistence.class)) {
            mockPersistence(mockedStatic);

            hibernate().delete("Test entity", TEST_ENTITIES.get(0));
            verify(session, times(1)).delete(TEST_ENTITIES.get(0));
        }
    }

    @Test
    public void deleteVarArgTest() {
        try (var mockedStatic = Mockito.mockStatic(Persistence.class)) {
            mockPersistence(mockedStatic);

            hibernate().delete("Test entities", TEST_ENTITIES.get(0), TEST_ENTITIES.get(1));
            verify(session, times(1)).delete(TEST_ENTITIES.get(0));
            verify(session, times(1)).delete(TEST_ENTITIES.get(1));
        }
    }

    @Test
    public void deleteIterableTest() {
        try (var mockedStatic = Mockito.mockStatic(Persistence.class)) {
            mockPersistence(mockedStatic);

            hibernate().delete("Test entities", TEST_ENTITIES);
            verify(session, times(1)).delete(TEST_ENTITIES.get(0));
            verify(session, times(1)).delete(TEST_ENTITIES.get(1));
        }
    }

    @Test
    public void deleteAllTest() {
        try (var mockedStatic = Mockito.mockStatic(Persistence.class)) {
            mockPersistence(mockedStatic);

            hibernate().deleteAllFrom(TestEntity.class);
            verify(session, times(1)).createQuery(criteriaDelete);
        }
    }

    @AfterMethod(alwaysRun = true)
    public void clearInvocations() {
        Mockito.clearInvocations(session);
    }
}
