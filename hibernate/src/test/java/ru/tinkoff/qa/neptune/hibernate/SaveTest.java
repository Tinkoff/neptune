package ru.tinkoff.qa.neptune.hibernate;

import org.hamcrest.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.hibernate.model.TestEntity;

import javax.persistence.PersistenceUnitUtil;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;
import static ru.tinkoff.qa.neptune.database.abstractions.UpdateAction.change;
import static ru.tinkoff.qa.neptune.hibernate.HibernateContext.hibernate;
import static ru.tinkoff.qa.neptune.hibernate.select.common.CommonSelectStepFactory.byId;
import static ru.tinkoff.qa.neptune.hibernate.select.common.CommonSelectStepFactory.byIds;

public class SaveTest extends BaseHibernateTest {

    private final TestEntity updatedEntity = new TestEntity().setId(10L)
            .setName("Test Name 1")
            .setArrayData(new String[]{"A1", "B1", "C1", "D1"})
            .setListData(new ArrayList<>(List.of("A1", "B1", "C1", "D1")));

    private final TestEntity NO_ID_ENTITY = new TestEntity()
            .setName("Test Name 3")
            .setArrayData(new String[]{"A3", "B3", "C3", "D3"})
            .setListData(new ArrayList<>(List.of("A3", "B3", "C3", "D3")));

    private final List<TestEntity> updatedEntities = new LinkedList<>(List.of(updatedEntity, updatedEntity));

    private List<TestEntity> original;

    private static List<TestEntity> cloneOriginalEntities() {
        return TEST_ENTITIES.stream().map(TestEntity::clone).collect(toList());
    }

    @Mock
    private PersistenceUnitUtil persistenceUtil;

    @BeforeClass
    public void prepareClass() {
        super.prepareClass();
        original = cloneOriginalEntities();

        when(sessionFactory.getPersistenceUnitUtil()).thenReturn(persistenceUtil);
        when(persistenceUtil.getIdentifier(TEST_ENTITIES.get(0))).thenReturn(1L);
        when(persistenceUtil.getIdentifier(TEST_ENTITIES.get(1))).thenReturn(2L);
        when(persistenceUtil.getIdentifier(updatedEntity)).thenReturn(10L);
        when(session.save(NO_ID_ENTITY)).thenReturn(3L);
        when(session.get(TestEntity.class, 3L)).thenReturn(NO_ID_ENTITY.setId(3L));
    }

    @Test
    public void saveOne() {
        var entity = hibernate().saveOne("Test entity", TEST_ENTITIES.get(0));

        assertThat(entity, Matchers.is(TEST_ENTITIES.get(0)));
        verify(session, times(1)).saveOrUpdate(TEST_ENTITIES.get(0));
    }

    @Test
    public void saveOneWithoutId() {
        var entity = hibernate().saveOne("Test entity", NO_ID_ENTITY);

        assertThat(entity, Matchers.is(NO_ID_ENTITY.setId(3L)));
        verify(session, times(1)).save(NO_ID_ENTITY);
    }

    @Test
    public void saveVarArg() {
        var entity = hibernate().saveAll("Test entities",
                TEST_ENTITIES.get(0), TEST_ENTITIES.get(1));

        assertThat(entity, Matchers.is(TEST_ENTITIES));
        verify(session, times(1)).saveOrUpdate(TEST_ENTITIES.get(0));
        verify(session, times(1)).saveOrUpdate(TEST_ENTITIES.get(1));
    }

    @Test
    public void saveIterable() {
        var entity = hibernate().saveAll("Test entities", TEST_ENTITIES);

        assertThat(entity, Matchers.is(TEST_ENTITIES));
        verify(session, times(1)).saveOrUpdate(TEST_ENTITIES.get(0));
        verify(session, times(1)).saveOrUpdate(TEST_ENTITIES.get(1));
    }

    @Test
    public void updateOne() {
        var entity = hibernate().saveOne("Test entity",
                original.get(0),
                change("Set new ID = 10", e -> e.setId(10L)),
                change("Set new name = \"Test Name 1\"", e -> e.setName("Test Name 1")),
                change("Update ListData", e -> e.setListData(List.of("A1", "B1", "C1", "D1"))),
                change("Update ArrayData", e -> e.setArrayData(new String[]{"A1", "B1", "C1", "D1"})));

        assertThat(entity, Matchers.is(updatedEntity));
        verify(session, times(1)).saveOrUpdate(updatedEntity);
    }

    @Test
    public void updateIterable() {
        var entities = hibernate().saveAll("Test entities",
                original,
                change("Set new ID = 10", e -> e.setId(10L)),
                change("Set new name = \"Test Name 1\"", e -> e.setName("Test Name 1")),
                change("Update ListData", e -> e.setListData(List.of("A1", "B1", "C1", "D1"))),
                change("Update ArrayData", e -> e.setArrayData(new String[]{"A1", "B1", "C1", "D1"})));

        assertThat(entities, Matchers.is(updatedEntities));
        verify(session, times(2)).saveOrUpdate(updatedEntity);
    }

    @Test
    public void updateOneByQuery() {
        var entity = hibernate().saveByQuery("Test entity",
                byId(TestEntity.class, 1L),
                change("Set new ID = 10", e -> e.setId(10L)),
                change("Set new name = \"Test Name 1\"", e -> e.setName("Test Name 1")),
                change("Update ListData", e -> e.setListData(List.of("A1", "B1", "C1", "D1"))),
                change("Update ArrayData", e -> e.setArrayData(new String[]{"A1", "B1", "C1", "D1"})));

        assertThat(entity, Matchers.is(updatedEntity));
        verify(session, times(1)).saveOrUpdate(updatedEntity);
    }

    @Test
    public void updateIterableByQuery() {
        var entities = hibernate().saveAllByQuery("Test entities",
                byIds(TestEntity.class, 1L, 2L),
                change("Set new ID = 10", e -> e.setId(10L)),
                change("Set new name = \"Test Name 1\"", e -> e.setName("Test Name 1")),
                change("Update ListData", e -> e.setListData(List.of("A1", "B1", "C1", "D1"))),
                change("Update ArrayData", e -> e.setArrayData(new String[]{"A1", "B1", "C1", "D1"})));

        assertThat(entities, Matchers.is(updatedEntities));
        verify(session, times(2)).saveOrUpdate(updatedEntity);
    }

    @AfterMethod(alwaysRun = true)
    public void clearInvocations() {
        Mockito.clearInvocations(session);
    }
}
