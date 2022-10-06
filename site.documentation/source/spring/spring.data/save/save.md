# Save-операции

```java
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static ru.tinkoff.qa.neptune.database.abstractions.UpdateAction.change;
import static ru.tinkoff.qa.neptune.spring.data.SpringDataContext.springData;
import static ru.tinkoff.qa.neptune.spring.data.select.common.CommonSelectStepFactory.*;

@SpringBootTest
public class MyTest {

    @Autowired
    private TestRepository testRepository;

    @Test
    public void myTest() {

        //ранее полученная или новая запись
        TestEntity testEntity;
        //ранее полученная или новая запись
        TestEntity testEntity2;

        //прямое сохранение новой/измененной записи
        TestEntity saved = springData().save(
            //описание того ЧТО следует сохранить,
            //в свободной форме или бизнес
            //терминологии
            "Test entity",
            testRepository,
            testEntity);

        //прямое сохранение нескольких новых/измененных записей
        Iterable<TestEntity> savedEntities = springData().save(
            "Test entities",
            testRepository,
            testEntity, testEntity2);

        //прямое сохранение коллекции новых/измененных записей
        Iterable<TestEntity> savedEntities2 = springData().save(
            "Test entities",
            testRepository,
            List.of(testEntity, testEntity2));

        //прямое сохранение новой/измененной записи
        TestEntity saved2 = springData().save("Test entity",
            testRepository,
            testEntity,
            //с документированием сделанных
            change("Set new ID = 10", e -> e.setId(10L)),
            //изменений
            change("Set new name = \"Test Name 1\"", e -> e.setName("Test Name 1")),
            change("Update ListData", e -> e.setListData(List.of("A1", "B1"))),
            change("Update ArrayData", e -> e.setArrayData(new String[]{"A1", "B1"})));

        //сохранение измененной записи, полученной с помощью запроса
        TestEntity saved3 = springData().save("Test entity",
            byId(testRepository, 1L),
            change("Set new ID = 10", e -> e.setId(10L)),
            change("Set new name = \"Test Name 1\"", e -> e.setName("Test Name 1")),
            change("Update ListData", e -> e.setListData(List.of("A1", "B1"))),
            change("Update ArrayData", e -> e.setArrayData(new String[]{"A1", "B1"})));

        //сохранение измененных записей, полученных с помощью запроса
        //аналогично примеру выше
        Iterable<TestEntity> savedEntities3 = springData().save("Test entities",
            byIds(testRepository, 1L, 2L),
            change("Set new ID = 10", e -> e.setId(10L)),
            change("Set new name = \"Test Name 1\"", e -> e.setName("Test Name 1")),
            change("Update ListData", e -> e.setListData(List.of("A1", "B1"))),
            change("Update ArrayData", e -> e.setArrayData(new String[]{"A1", "B1"})));
    }
}
```

