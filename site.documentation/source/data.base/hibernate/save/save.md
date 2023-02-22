# Hibernate. INSERT/UPDATE операции

```java
import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.hibernate.model.TestEntity;

import java.util.List;

import static ru.tinkoff.qa.neptune.database.abstractions.UpdateAction.change;
import static ru.tinkoff.qa.neptune.hibernate.HibernateContext.hibernate;
import static ru.tinkoff.qa.neptune.hibernate.select.common.CommonSelectStepFactory.byId;
import static ru.tinkoff.qa.neptune.hibernate.select.common.CommonSelectStepFactory.byIds;

public class MyTest {

    @Test
    public void test() {
        TestEntity testEntity; //ранее полученная или новая запись
        TestEntity testEntity2; //ранее полученная или новая запись

        //прямое сохранение новой/измененной записи
        TestEntity saved = hibernate().saveOne("Test entity", testEntity);

        //прямое сохранение нескольких новых/измененных записей
        Iterable<TestEntity> savedEntities = hibernate().saveAll("Test entities",
            testEntity, testEntity2);

        //прямое сохранение коллекции новых/измененных записей
        Iterable<TestEntity> savedEntities2 = hibernate().saveAll("Test entities",
            List.of(testEntity, testEntity2));

        //прямое сохранение новой/измененной записи
        TestEntity saved2 = hibernate().saveOne("Test entity",
            testEntity,
            change("Set new ID = 10", e -> e.setId(10L)), //с документированием сделанных
            change("Set new name = \"Test Name 1\"", e -> e.setName("Test Name 1")), //изменений
            change("Update ListData", e -> e.setListData(List.of("A1", "B1", "C1", "D1"))),
            change("Update ArrayData", e -> e.setArrayData(new String[]{"A1", "B1", "C1", "D1"})));
        //в случае с прямым сохранением записей необязательно документировать сделанные изменения
        //тоже самое для прямого сохранения нескольких/коллекций записей

        //сохранение измененной записи, полученной с помощью запроса
        TestEntity saved3 = hibernate().saveByQuery("Test entity",
            byId(TestEntity.class, 1L),
            change("Set new ID = 10", e -> e.setId(10L)), //с документированием сделанных
            change("Set new name = \"Test Name 1\"", e -> e.setName("Test Name 1")), //изменений
            change("Update ListData", e -> e.setListData(List.of("A1", "B1", "C1", "D1"))),
            change("Update ArrayData", e -> e.setArrayData(new String[]{"A1", "B1", "C1", "D1"})));
        //в случае с сохранением записей, полученных запросом, обязательно документировать сделанные изменения

        //сохранение измененных записей, полученных с помощью запроса
        //аналогично примеру выше
        Iterable<TestEntity> savedEntities3 = hibernate().saveAllByQuery("Test entities",
            byIds(TestEntity.class, 1L, 2L),
            change("Set new ID = 10", e -> e.setId(10L)),
            change("Set new name = \"Test Name 1\"", e -> e.setName("Test Name 1")),
            change("Update ListData", e -> e.setListData(List.of("A1", "B1", "C1", "D1"))),
            change("Update ArrayData", e -> e.setArrayData(new String[]{"A1", "B1", "C1", "D1"})));
    }
}
```