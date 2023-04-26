# Spring data. Delete-операции

Может быть выполнено, если интерфейс-репозиторий расширяет один или несколько из перечисленных:

- [CrudRepository](https://docs.spring.io/spring-data/commons/docs/current/api/org/springframework/data/repository/CrudRepository.html)
- [ReactiveCrudRepository](https://docs.spring.io/spring-data/commons/docs/current/api/org/springframework/data/repository/reactive/ReactiveCrudRepository.html)
- [RxJava3CrudRepository](https://docs.spring.io/spring-data/commons/docs/current/api/org/springframework/data/repository/reactive/RxJava3CrudRepository.html)

```java
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static ru.tinkoff.qa.neptune.spring.data.SpringDataContext.springData;
import static ru.tinkoff.qa.neptune.spring.data.select.common.CommonSelectStepFactory.*;

@SpringBootTest
public class MyTest {

    @Autowired
    private TestRepository testRepository;

    @Test
    public void myTest() {

        //удаление записи, выбранной с помощью запроса
        springData().delete(
            //описание того ЧТО следует удалить,
            //в свободной форме или бизнес
            //терминологии
            "Test entity",
            //тип запроса зависит от того,
            // какие интерфейсы расширяет интерфейс-репозиторий
            byId(testRepository, 1L)
            //опциональные параметры find-операции
            //
        );

        //удаление записей, выбранных с помощью запроса
        springData().delete(
            "Test entities",
            byIds(testRepository, 1L, 2L)
            //опциональные параметры find-операции
            //
        );

        //ранее полученная запись
        TestEntity testEntity;
        //ранее полученная запись
        TestEntity testEntity2;

        //прямое удаление записи
        springData().delete(
            "Test entity",
            testRepository,
            testEntity);

        //прямое удаление нескольких записей
        springData().delete(
            "Test entities",
            testRepository,
            testEntity, testEntity2);

        //прямое удаление коллекции записей
        springData().delete(
            "Test entities",
            testRepository,
            List.of(testEntity, testEntity2));

        //удаление записей по ID
        springData().deleteByIds(
            "Test entities",
            testRepository,
            1L, 2L);
    }
}
```