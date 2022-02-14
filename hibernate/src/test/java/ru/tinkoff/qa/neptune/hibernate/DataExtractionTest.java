package ru.tinkoff.qa.neptune.hibernate;

import org.mockito.Mockito;
import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.core.api.steps.NotPresentException;
import ru.tinkoff.qa.neptune.hibernate.model.TestEntity;

import javax.persistence.Persistence;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.testng.AssertJUnit.fail;
import static ru.tinkoff.qa.neptune.hibernate.HibernateContext.hibernate;
import static ru.tinkoff.qa.neptune.hibernate.select.common.CommonSelectStepFactory.byId;
import static ru.tinkoff.qa.neptune.hibernate.select.common.CommonSelectStepFactory.byIds;

public class DataExtractionTest extends BaseHibernateTest {

    @Test
    public void getObjectFromEntityTest() {
        try (var mockedStatic = Mockito.mockStatic(Persistence.class)) {
            mockPersistence(mockedStatic);

            var data = hibernate().select("Test data", byId(TestEntity.class, 1L)
                    .criteria("Name = 'Test Name 1'", testEntity -> testEntity.getName().equals("Test Name 1"))
                    .thenGetObject(TestEntity::getId)
                    .criteria("Lesser than 2", l -> l < 2));

            assertThat(data, is(1L));
        }
    }

    @Test
    public void getObjectFromEntityTest2() {
        try (var mockedStatic = Mockito.mockStatic(Persistence.class)) {
            mockPersistence(mockedStatic);

            var data = hibernate().select("Test data", byId(TestEntity.class, 1L)
                    .criteria("Name = 'Test Name 1'", testEntity -> testEntity.getName().equals("Test Name 2"))
                    .thenGetObject(TestEntity::getId)
                    .criteria("Lesser than 2", l -> l < 2));

            assertThat(data, nullValue());
        }
    }

    @Test
    public void getObjectFromEntityTest3() {
        try (var mockedStatic = Mockito.mockStatic(Persistence.class)) {
            mockPersistence(mockedStatic);

            var data = hibernate().select("Test data", byId(TestEntity.class, 1L)
                    .criteria("Name = 'Test Name 1'", testEntity -> testEntity.getName().equals("Test Name 1"))
                    .thenGetObject(TestEntity::getId)
                    .criteria("Greater than 2", l -> l > 2));

            assertThat(data, nullValue());
        }
    }

    @Test
    public void getObjectFromEntityTest4() {
        try (var mockedStatic = Mockito.mockStatic(Persistence.class)) {
            mockPersistence(mockedStatic);

            hibernate().select("Test data", byId(TestEntity.class, 1L)
                    .criteria("Name = 'Test Name 1'", testEntity -> testEntity.getName().equals("Test Name 2"))
                    .throwOnNoResult()
                    .thenGetObject(TestEntity::getId)
                    .criteria("Lesser than 2", l -> l < 2));
        } catch (Throwable e) {
            assertThat(e, instanceOf(NotPresentException.class));
            assertThat(e.getMessage(), startsWith("Not present: Required entity"));
            return;
        }

        fail("Exception was expected");
    }

    @Test
    public void getObjectFromEntityTest5() {
        try (var mockedStatic = Mockito.mockStatic(Persistence.class)) {
            mockPersistence(mockedStatic);

            hibernate().select("Test data", byId(TestEntity.class, 1L)
                    .criteria("Name = 'Test Name 1'", testEntity -> testEntity.getName().equals("Test Name 1"))
                    .thenGetObject(TestEntity::getId)
                    .criteria("Greater than 2", l -> l > 2)
                    .throwOnNoResult());
        } catch (Throwable e) {
            assertThat(e, instanceOf(NotPresentException.class));
            assertThat(e.getMessage(), startsWith("Not present: Test data"));
            return;
        }

        fail("Exception was expected");
    }

    @Test
    public void getIterableFromEntityTest() {
        try (var mockedStatic = Mockito.mockStatic(Persistence.class)) {
            mockPersistence(mockedStatic);

            var data = hibernate().select("Test data", byId(TestEntity.class, 1L)
                    .criteria("Name = 'Test Name 1'", testEntity -> testEntity.getName().equals("Test Name 1"))
                    .thenGetList(TestEntity::getListData)
                    .criteria("not E", s -> !s.equalsIgnoreCase("e")));

            assertThat(data, contains("A", "B", "C", "D"));
        }
    }

    @Test
    public void getIterableFromEntityTest2() {
        try (var mockedStatic = Mockito.mockStatic(Persistence.class)) {
            mockPersistence(mockedStatic);

            var data = hibernate().select("Test data", byId(TestEntity.class, 1L)
                    .criteria("Name = 'Test Name 1'", testEntity -> testEntity.getName().equals("Test Name 2"))
                    .thenGetList(TestEntity::getListData)
                    .criteria("not E", s -> !s.equalsIgnoreCase("e")));

            assertThat(data, nullValue());
        }
    }

    @Test
    public void getIterableFromEntityTest3() {
        try (var mockedStatic = Mockito.mockStatic(Persistence.class)) {
            mockPersistence(mockedStatic);

            var data = hibernate().select("Test data", byId(TestEntity.class, 1L)
                    .criteria("Name = 'Test Name 1'", testEntity -> testEntity.getName().equals("Test Name 1"))
                    .thenGetList(TestEntity::getListData)
                    .criteria("not A, B, C or D", s -> !s.equalsIgnoreCase("a")
                            && !s.equalsIgnoreCase("b")
                            && !s.equalsIgnoreCase("c")
                            && !s.equalsIgnoreCase("d")));

            assertThat(data, either(emptyIterable()).or(nullValue()));
        }
    }

    @Test
    public void getIterableFromEntityTest4() {
        try (var mockedStatic = Mockito.mockStatic(Persistence.class)) {
            mockPersistence(mockedStatic);

            hibernate().select("Test data", byId(TestEntity.class, 1L)
                    .criteria("Name = 'Test Name 1'", testEntity -> testEntity.getName().equals("Test Name 2"))
                    .throwOnNoResult()
                    .thenGetList(TestEntity::getListData)
                    .criteria("not E", s -> !s.equalsIgnoreCase("e")));
        } catch (Throwable e) {
            assertThat(e, instanceOf(NotPresentException.class));
            assertThat(e.getMessage(), startsWith("Not present: Required entity"));
            return;
        }

        fail("Exception was expected");
    }

    @Test
    public void getIterableFromEntityTest5() {
        try (var mockedStatic = Mockito.mockStatic(Persistence.class)) {
            mockPersistence(mockedStatic);

            hibernate().select("Test data", byId(TestEntity.class, 1L)
                    .criteria("Name = 'Test Name 1'", testEntity -> testEntity.getName().equals("Test Name 1"))
                    .thenGetList(TestEntity::getListData)
                    .criteria("not A, B, C or D", s -> !s.equalsIgnoreCase("a")
                            && !s.equalsIgnoreCase("b")
                            && !s.equalsIgnoreCase("c")
                            && !s.equalsIgnoreCase("d"))
                    .throwOnNoResult());
        } catch (Throwable e) {
            assertThat(e, instanceOf(NotPresentException.class));
            assertThat(e.getMessage(), startsWith("Not present: Test data"));
            return;
        }

        fail("Exception was expected");
    }

    @Test
    public void getArrayFromEntityTest() {
        try (var mockedStatic = Mockito.mockStatic(Persistence.class)) {
            mockPersistence(mockedStatic);

            var data = hibernate().select("Test data", byId(TestEntity.class, 1L)
                    .criteria("Name = 'Test Name 1'", testEntity -> testEntity.getName().equals("Test Name 1"))
                    .thenGetArray(TestEntity::getArrayData)
                    .criteria("not E", s -> !s.equalsIgnoreCase("e")));

            assertThat(data, arrayContaining("A", "B", "C", "D"));
        }
    }

    @Test
    public void getArrayFromEntityTest2() {
        try (var mockedStatic = Mockito.mockStatic(Persistence.class)) {
            mockPersistence(mockedStatic);

            var data = hibernate().select("Test data", byId(TestEntity.class, 1L)
                    .criteria("Name = 'Test Name 1'", testEntity -> testEntity.getName().equals("Test Name 2"))
                    .thenGetArray(TestEntity::getArrayData)
                    .criteria("not E", s -> !s.equalsIgnoreCase("e")));

            assertThat(data, nullValue());
        }
    }

    @Test
    public void getArrayFromEntityTest3() {
        try (var mockedStatic = Mockito.mockStatic(Persistence.class)) {
            mockPersistence(mockedStatic);

            var data = hibernate().select("Test data", byId(TestEntity.class, 1L)
                    .criteria("Name = 'Test Name 1'", testEntity -> testEntity.getName().equals("Test Name 1"))
                    .thenGetArray(TestEntity::getArrayData)
                    .criteria("not A, B, C or D", s -> !s.equalsIgnoreCase("a")
                            && !s.equalsIgnoreCase("b")
                            && !s.equalsIgnoreCase("c")
                            && !s.equalsIgnoreCase("d")));

            assertThat(data, either(emptyArray()).or(nullValue()));
        }
    }

    @Test
    public void getArrayFromEntityTest4() {
        try (var mockedStatic = Mockito.mockStatic(Persistence.class)) {
            mockPersistence(mockedStatic);

            hibernate().select("Test data", byId(TestEntity.class, 1L)
                    .criteria("Name = 'Test Name 1'", testEntity -> testEntity.getName().equals("Test Name 2"))
                    .throwOnNoResult()
                    .thenGetArray(TestEntity::getArrayData)
                    .criteria("not E", s -> !s.equalsIgnoreCase("e")));
        } catch (Throwable e) {
            assertThat(e, instanceOf(NotPresentException.class));
            assertThat(e.getMessage(), startsWith("Not present: Required entity"));
            return;
        }

        fail("Exception was expected");
    }

    @Test
    public void getArrayFromEntityTest5() {
        try (var mockedStatic = Mockito.mockStatic(Persistence.class)) {
            mockPersistence(mockedStatic);

            hibernate().select("Test data", byId(TestEntity.class, 1L)
                    .criteria("Name = 'Test Name 1'", testEntity -> testEntity.getName().equals("Test Name 1"))
                    .thenGetArray(TestEntity::getArrayData)
                    .criteria("not A, B, C or D", s -> !s.equalsIgnoreCase("a")
                            && !s.equalsIgnoreCase("b")
                            && !s.equalsIgnoreCase("c")
                            && !s.equalsIgnoreCase("d"))
                    .throwOnNoResult());
        } catch (Throwable e) {
            assertThat(e, instanceOf(NotPresentException.class));
            assertThat(e.getMessage(), startsWith("Not present: Test data"));
            return;
        }

        fail("Exception was expected");
    }

    @Test
    public void getIterableItemFromEntityTest() {
        try (var mockedStatic = Mockito.mockStatic(Persistence.class)) {
            mockPersistence(mockedStatic);

            var data = hibernate().select("Test data", byId(TestEntity.class, 1L)
                    .criteria("Name = 'Test Name 1'", testEntity -> testEntity.getName().equals("Test Name 1"))
                    .thenGetIterableItem(TestEntity::getListData)
                    .criteria("not E", s -> !s.equalsIgnoreCase("e")));

            assertThat(data, is("A"));
        }
    }

    @Test
    public void getIterableItemFromEntityTest2() {
        try (var mockedStatic = Mockito.mockStatic(Persistence.class)) {
            mockPersistence(mockedStatic);

            var data = hibernate().select("Test data", byId(TestEntity.class, 1L)
                    .criteria("Name = 'Test Name 1'", testEntity -> testEntity.getName().equals("Test Name 2"))
                    .thenGetIterableItem(TestEntity::getListData)
                    .criteria("not E", s -> !s.equalsIgnoreCase("e")));

            assertThat(data, nullValue());
        }
    }

    @Test
    public void getIterableItemFromEntityTest3() {
        try (var mockedStatic = Mockito.mockStatic(Persistence.class)) {
            mockPersistence(mockedStatic);

            var data = hibernate().select("Test data", byId(TestEntity.class, 1L)
                    .criteria("Name = 'Test Name 1'", testEntity -> testEntity.getName().equals("Test Name 1"))
                    .thenGetIterableItem(TestEntity::getListData)
                    .criteria("not A, B, C or D", s -> !s.equalsIgnoreCase("a")
                            && !s.equalsIgnoreCase("b")
                            && !s.equalsIgnoreCase("c")
                            && !s.equalsIgnoreCase("d")));

            assertThat(data, nullValue());
        }
    }

    @Test
    public void getIterableItemFromEntityTest4() {
        try (var mockedStatic = Mockito.mockStatic(Persistence.class)) {
            mockPersistence(mockedStatic);

            hibernate().select("Test data", byId(TestEntity.class, 1L)
                    .criteria("Name = 'Test Name 1'", testEntity -> testEntity.getName().equals("Test Name 2"))
                    .throwOnNoResult()
                    .thenGetIterableItem(TestEntity::getListData)
                    .criteria("not E", s -> !s.equalsIgnoreCase("e")));
        } catch (Throwable e) {
            assertThat(e, instanceOf(NotPresentException.class));
            assertThat(e.getMessage(), startsWith("Not present: Required entity"));
            return;
        }

        fail("Exception was expected");
    }

    @Test
    public void getIterableItemFromEntityTest5() {
        try (var mockedStatic = Mockito.mockStatic(Persistence.class)) {
            mockPersistence(mockedStatic);

            hibernate().select("Test data", byId(TestEntity.class, 1L)
                    .criteria("Name = 'Test Name 1'", testEntity -> testEntity.getName().equals("Test Name 1"))
                    .thenGetIterableItem(TestEntity::getListData)
                    .criteria("not A, B, C or D", s -> !s.equalsIgnoreCase("a")
                            && !s.equalsIgnoreCase("b")
                            && !s.equalsIgnoreCase("c")
                            && !s.equalsIgnoreCase("d"))
                    .throwOnNoResult());
        } catch (Throwable e) {
            assertThat(e, instanceOf(NotPresentException.class));
            assertThat(e.getMessage(), startsWith("Not present: Test data"));
            return;
        }

        fail("Exception was expected");
    }

    @Test
    public void getArrayItemFromEntityTest() {
        try (var mockedStatic = Mockito.mockStatic(Persistence.class)) {
            mockPersistence(mockedStatic);

            var data = hibernate().select("Test data", byId(TestEntity.class, 1L)
                    .criteria("Name = 'Test Name 1'", testEntity -> testEntity.getName().equals("Test Name 1"))
                    .thenGetArrayItem(TestEntity::getArrayData)
                    .criteria("not E", s -> !s.equalsIgnoreCase("e")));

            assertThat(data, is("A"));
        }
    }

    @Test
    public void getArrayItemFromEntityTest2() {
        try (var mockedStatic = Mockito.mockStatic(Persistence.class)) {
            mockPersistence(mockedStatic);

            var data = hibernate().select("Test data", byId(TestEntity.class, 1L)
                    .criteria("Name = 'Test Name 1'", testEntity -> testEntity.getName().equals("Test Name 2"))
                    .thenGetArrayItem(TestEntity::getArrayData)
                    .criteria("not E", s -> !s.equalsIgnoreCase("e")));

            assertThat(data, nullValue());
        }
    }

    @Test
    public void getArrayItemFromEntityTest3() {
        try (var mockedStatic = Mockito.mockStatic(Persistence.class)) {
            mockPersistence(mockedStatic);

            var data = hibernate().select("Test data", byId(TestEntity.class, 1L)
                    .criteria("Name = 'Test Name 1'", testEntity -> testEntity.getName().equals("Test Name 1"))
                    .thenGetArrayItem(TestEntity::getArrayData)
                    .criteria("not A, B, C or D", s -> !s.equalsIgnoreCase("a")
                            && !s.equalsIgnoreCase("b")
                            && !s.equalsIgnoreCase("c")
                            && !s.equalsIgnoreCase("d")));

            assertThat(data, nullValue());
        }
    }

    @Test
    public void getArrayItemFromEntityTest4() {
        try (var mockedStatic = Mockito.mockStatic(Persistence.class)) {
            mockPersistence(mockedStatic);

            hibernate().select("Test data", byId(TestEntity.class, 1L)
                    .criteria("Name = 'Test Name 1'", testEntity -> testEntity.getName().equals("Test Name 2"))
                    .throwOnNoResult()
                    .thenGetArrayItem(TestEntity::getArrayData)
                    .criteria("not E", s -> !s.equalsIgnoreCase("e")));
        } catch (Throwable e) {
            assertThat(e, instanceOf(NotPresentException.class));
            assertThat(e.getMessage(), startsWith("Not present: Required entity"));
            return;
        }

        fail("Exception was expected");
    }

    @Test
    public void getArrayItemFromEntityTest5() {
        try (var mockedStatic = Mockito.mockStatic(Persistence.class)) {
            mockPersistence(mockedStatic);

            hibernate().select("Test data", byId(TestEntity.class, 1L)
                    .criteria("Name = 'Test Name 1'", testEntity -> testEntity.getName().equals("Test Name 1"))
                    .thenGetArrayItem(TestEntity::getArrayData)
                    .criteria("not A, B, C or D", s -> !s.equalsIgnoreCase("a")
                            && !s.equalsIgnoreCase("b")
                            && !s.equalsIgnoreCase("c")
                            && !s.equalsIgnoreCase("d"))
                    .throwOnNoResult());
        } catch (Throwable e) {
            assertThat(e, instanceOf(NotPresentException.class));
            assertThat(e.getMessage(), startsWith("Not present: Test data"));
            return;
        }

        fail("Exception was expected");
    }


    @Test
    public void getIterableFromEntitiesTest() {
        try (var mockedStatic = Mockito.mockStatic(Persistence.class)) {
            mockPersistence(mockedStatic);

            var data = hibernate().select("Test data", byIds(TestEntity.class, 1L, 2L)
                    .criteria("Name != 'Test Name 3'", testEntity -> !testEntity
                            .getName()
                            .equalsIgnoreCase("Test Name 3"))
                    .thenGetList(TestEntity::getName)
                    .criteria("not \"Test Name 3\"", s -> !s.equalsIgnoreCase("Test Name 3")));

            assertThat(data, contains("Test Name 1", "Test Name 2"));
        }
    }

    @Test
    public void getIterableFromEntitiesTest2() {
        try (var mockedStatic = Mockito.mockStatic(Persistence.class)) {
            mockPersistence(mockedStatic);

            var data = hibernate().select("Test data", byIds(TestEntity.class, 1L, 2L)
                    .criteria("Name = 'Test Name 3'", testEntity -> testEntity
                            .getName()
                            .equalsIgnoreCase("Test Name 3"))
                    .thenGetList(TestEntity::getName)
                    .criteria("not \"Test Name 3\"", s -> !s.equalsIgnoreCase("Test Name 3")));

            assertThat(data, either(emptyIterable()).or(nullValue()));
        }
    }

    @Test
    public void getIterableFromEntitiesTest3() {
        try (var mockedStatic = Mockito.mockStatic(Persistence.class)) {
            mockPersistence(mockedStatic);

            var data = hibernate().select("Test data", byIds(TestEntity.class, 1L, 2L)
                    .criteria("Name != 'Test Name 3'", testEntity -> !testEntity
                            .getName()
                            .equalsIgnoreCase("Test Name 3"))
                    .thenGetList(TestEntity::getName)
                    .criteria("is \"Test Name 3\"", s -> s.equalsIgnoreCase("Test Name 3")));

            assertThat(data, either(emptyIterable()).or(nullValue()));
        }
    }

    @Test
    public void getIterableFromEntitiesTest4() {
        try (var mockedStatic = Mockito.mockStatic(Persistence.class)) {
            mockPersistence(mockedStatic);

            hibernate().select("Test data", byIds(TestEntity.class, 1L, 2L)
                    .criteria("Name = 'Test Name 3'", testEntity -> testEntity
                            .getName()
                            .equalsIgnoreCase("Test Name 3"))
                    .throwOnNoResult()
                    .thenGetList(TestEntity::getName)
                    .criteria("not \"Test Name 3\"", s -> !s.equalsIgnoreCase("Test Name 3")));
        } catch (Throwable e) {
            assertThat(e, instanceOf(NotPresentException.class));
            assertThat(e.getMessage(), startsWith("Not present: Required entities"));
            return;
        }

        fail("Exception was expected");
    }

    @Test
    public void getIterableFromEntitiesTest5() {
        try (var mockedStatic = Mockito.mockStatic(Persistence.class)) {
            mockPersistence(mockedStatic);

            hibernate().select("Test data", byIds(TestEntity.class, 1L, 2L)
                    .criteria("Name != 'Test Name 3'", testEntity -> !testEntity
                            .getName()
                            .equalsIgnoreCase("Test Name 3"))
                    .thenGetList(TestEntity::getName)
                    .criteria("is \"Test Name 3\"", s -> s.equalsIgnoreCase("Test Name 3"))
                    .throwOnNoResult());
        } catch (Throwable e) {
            assertThat(e, instanceOf(NotPresentException.class));
            assertThat(e.getMessage(), startsWith("Not present: Test data"));
            return;
        }

        fail("Exception was expected");
    }


    @Test
    public void getIterableItemFromEntitiesTest() {
        try (var mockedStatic = Mockito.mockStatic(Persistence.class)) {
            mockPersistence(mockedStatic);

            var data = hibernate().select("Test data", byIds(TestEntity.class, 1L, 2L)
                    .criteria("Name != 'Test Name 3'", testEntity -> !testEntity
                            .getName()
                            .equalsIgnoreCase("Test Name 3"))
                    .thenGetIterableItem(TestEntity::getName)
                    .criteria("not \"Test Name 3\"", s -> !s.equalsIgnoreCase("Test Name 3")));

            assertThat(data, is("Test Name 1"));
        }
    }

    @Test
    public void getIterableItemFromEntitiesTest2() {
        try (var mockedStatic = Mockito.mockStatic(Persistence.class)) {
            mockPersistence(mockedStatic);

            var data = hibernate().select("Test data", byIds(TestEntity.class, 1L, 2L)
                    .criteria("Name = 'Test Name 3'", testEntity -> testEntity
                            .getName()
                            .equalsIgnoreCase("Test Name 3"))
                    .thenGetIterableItem(TestEntity::getName)
                    .criteria("not \"Test Name 3\"", s -> !s.equalsIgnoreCase("Test Name 3")));

            assertThat(data, nullValue());
        }
    }

    @Test
    public void getIterableItemFromEntitiesTest3() {
        try (var mockedStatic = Mockito.mockStatic(Persistence.class)) {
            mockPersistence(mockedStatic);

            var data = hibernate().select("Test data", byIds(TestEntity.class, 1L, 2L)
                    .criteria("Name != 'Test Name 3'", testEntity -> !testEntity
                            .getName()
                            .equalsIgnoreCase("Test Name 3"))
                    .thenGetIterableItem(TestEntity::getName)
                    .criteria("is \"Test Name 3\"", s -> s.equalsIgnoreCase("Test Name 3")));

            assertThat(data, nullValue());
        }
    }

    @Test
    public void getIterableItemFromEntitiesTest4() {
        try (var mockedStatic = Mockito.mockStatic(Persistence.class)) {
            mockPersistence(mockedStatic);

            hibernate().select("Test data", byIds(TestEntity.class, 1L, 2L)
                    .criteria("Name = 'Test Name 3'", testEntity -> testEntity
                            .getName()
                            .equalsIgnoreCase("Test Name 3"))
                    .throwOnNoResult()
                    .thenGetIterableItem(TestEntity::getName)
                    .criteria("not \"Test Name 3\"", s -> !s.equalsIgnoreCase("Test Name 3")));
        } catch (Throwable e) {
            assertThat(e, instanceOf(NotPresentException.class));
            assertThat(e.getMessage(), startsWith("Not present: Required entities"));
            return;
        }

        fail("Exception was expected");
    }

    @Test
    public void getIterableItemFromEntitiesTest5() {
        try (var mockedStatic = Mockito.mockStatic(Persistence.class)) {
            mockPersistence(mockedStatic);

            hibernate().select("Test data", byIds(TestEntity.class, 1L, 2L)
                    .criteria("Name != 'Test Name 3'", testEntity -> !testEntity
                            .getName()
                            .equalsIgnoreCase("Test Name 3"))
                    .thenGetIterableItem(TestEntity::getName)
                    .criteria("is \"Test Name 3\"", s -> s.equalsIgnoreCase("Test Name 3"))
                    .throwOnNoResult());
        } catch (Throwable e) {
            assertThat(e, instanceOf(NotPresentException.class));
            assertThat(e.getMessage(), startsWith("Not present: Test data"));
            return;
        }

        fail("Exception was expected");
    }
}
