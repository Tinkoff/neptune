# Все записи

Может быть выполнено, если интерфейс-репозиторий расширяет один или несколько из перечисленных:

- [CrudRepository](https://docs.spring.io/spring-data/commons/docs/current/api/org/springframework/data/repository/CrudRepository.html)
- [ReactiveCrudRepository](https://docs.spring.io/spring-data/commons/docs/current/api/org/springframework/data/repository/reactive/ReactiveCrudRepository.html)
- [RxJava2CrudRepository](https://docs.spring.io/spring-data/commons/docs/current/api/org/springframework/data/repository/reactive/RxJava2CrudRepository.html)
- [RxJava3CrudRepository](https://docs.spring.io/spring-data/commons/docs/current/api/org/springframework/data/repository/reactive/RxJava3CrudRepository.html)

```java
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static ru.tinkoff.qa.neptune.spring.data.SpringDataContext.springData;
import static ru.tinkoff.qa.neptune.spring.data.select.common.CommonSelectStepFactory.all;

@SpringBootTest
public class MyTest {

    @Autowired
    private TestRepository testRepository;

    @Test
    public void myTest() {
        List<TestEntity> entities = springData().find(
            //описание того ЧТО выбирается,
            //в свободной форме или бизнес
            //терминологии
            "Test entities",
            all(testRepository)
                //можно указать один-несколько критериев,
                //которым должны соответствовать отобранные записи
                // Так же доступны criteriaOr(criteria...), 
                // criteriaOnlyOne(criteria...)
                // criteriaNot(criteria...)
                .criteria(
                    "Name == 'Some name'", 
                    testEntity -> testEntity.getName().equals("Some name"))
                .criteria("ID > 10", testEntity -> testEntity.getId() > 10L)
                // можно указать время, 
                // за которое подходящие записи должны быть выбраны,
                // если оно отличается от того, 
                // что указано в свойствах/переменных окружения
                // SPRING_DATA_WAITING_FOR_SELECTION_RESULT_TIME_UNIT 
                // и SPRING_DATA_WAITING_FOR_SELECTION_RESULT_TIME_VALUE
                .timeOut(ofSeconds(5))
                //можно указать время интервала между попытками получить
                // необходимый результат в рамках отведенного для этого времени, 
                // если значение этого интервала
                //должно отличаться от того, 
                // что указано в свойствах/переменных окружения
                // SPRING_DATA_SLEEPING_TIME_UNIT и 
                // SPRING_DATA_SLEEPING_TIME_VALUE
                .pollingInterval(ofMillis(200))
                //другие опциональные параметры
        );
    }
}
```

Про свойства / переменные окружения `SPRING_DATA_SLEEPING_TIME_UNIT`, `SPRING_DATA_SLEEPING_TIME_VALUE`,
`SPRING_DATA_SLEEPING_TIME_UNIT` и `SPRING_DATA_SLEEPING_TIME_VALUE` можно прочитать [здесь](./../settings.md).

```{eval-rst}
.. include:: ../../../shared_docs/steps_return_list_optiotal_parameters_async.rst
```

Кроме записей целиком, можно сделать выборку их данных

```java
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static ru.tinkoff.qa.neptune.spring.data.SpringDataContext.springData;
import static ru.tinkoff.qa.neptune.spring.data.select.common.CommonSelectStepFactory.all;

@SpringBootTest
public class MyTest {

    @Autowired
    private TestRepository testRepository;

    @Test
    public void myTest() {
        List<String> names = springData().find(
            //описание того ЧТО следует получить,
            //в свободной форме или бизнес
            //терминологии
            "Test names",
            all(testRepository)
                //
                //Необходимые параметры
                //
                //-------------------------------
                //Если необходимо вернуть лист, 
                // составленный из данных найденных записей.
                //Указывается, как извлекаются данные из
                //каждой записи
                .thenGetList(TestEntity::getName)
                // далее можно указать опциональные 
                // уточняющие параметры
        );
    }
}
```

```{eval-rst}
.. include:: ../../../shared_docs/steps_return_list_optiotal_parameters_sync.rst
```

```java
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static ru.tinkoff.qa.neptune.spring.data.SpringDataContext.springData;
import static ru.tinkoff.qa.neptune.spring.data.select.common.CommonSelectStepFactory.byId;

@SpringBootTest
public class MyTest {

    @Autowired
    private TestRepository testRepository;

    @Test
    public void myTest() {        
        String name = springData().find(
            "Test name",
            all(testRepository)
                //
                //Необходимые параметры
                //
                //-------------------------------
                //Если необходимо вернуть один объект
                //из данных найденных записей, собранных в набор.
                //Указывается, как извлекаются данные из
                //каждой записи
                .thenGetIterableItem(TestEntity::getName)
                // далее можно указать опциональные 
                // уточняющие параметры
        );
    }
}
```

```{eval-rst}
.. include:: ../../../shared_docs/steps_return_iterable_item_optiotal_parameters_sync.rst
```

Так же можно проверить наличие или отсутствие записей

```java
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static ru.tinkoff.qa.neptune.spring.data.SpringDataContext.springData;
import static ru.tinkoff.qa.neptune.spring.data.select.common.CommonSelectStepFactory.all;

@SpringBootTest
public class MyTest {

    @Autowired
    private TestRepository testRepository;

    @Test
    public void presenceTest() {

        //Если запись была или появилась через некоторое время - вернется true. 
        // Иначе - false.
        boolean isPresent = springData().presenceOf(
            //описание того ЧТО должно 
            // присутствовать, в свободной форме 
            // или бизнес терминологии
            "Test entities",
            all(testRepository)
                //можно указать один-несколько критериев,
                //которым должны соответствовать записи,
                //присутствующие на начало выполнения шага 
                //или появляющиеся, пока шаг выполняется
                .criteria(
                    "Name == 'Some name'", 
                    testEntity -> testEntity.getName().equals("Some name")
                )
                .criteria("ID > 10", testEntity -> testEntity.getId() > 10L)
                // можно указать время, 
                // за которое подходящие записи должны появиться,
                // если оно отличается от того, 
                // что указано в свойствах/переменных окружения
                // SPRING_DATA_WAITING_FOR_SELECTION_RESULT_TIME_UNIT 
                // и SPRING_DATA_WAITING_FOR_SELECTION_RESULT_TIME_VALUE
                .timeOut(ofSeconds(5))
            //Прочие описанные настройки выше 
            //настройки и параметры игнорируются здесь
        );
        
        //Если запись была или появилась в течение - вернется true. Иначе - выбросится исключение.
        var isPresent2 = springData().presenceOfOrThrow(
            "Test entities",
            //опционально критерии и время
            all(testRepository));
    }

    @Test
    public void absenceTest() {
        
        //Если записи не было или она перестала определяться 
        // через некоторое время - вернется true 
        // Иначе - false.
        boolean isAbsent = springData().absenceOf(
            //описание того ЧТО должно 
            // отсутствовать, в свободной форме 
            // или бизнес терминологии
            "Test entities",
            all(testRepository)
                //можно указать один-несколько критериев,
                //которым должны соответствовать записи,
                //отсутствующие на начало выполнения шага 
                //или исчезающие, пока шаг выполняется
                .criteria(
                    "Name == 'Some name'", 
                    testEntity -> testEntity.getName().equals("Some name"))
                .criteria("ID > 10", testEntity -> testEntity.getId() > 10L),
            //Прочие описанные настройки выше 
            //настройки и параметры игнорируются здесь
            //----------------------------------------
            //время, за которое записи должны исчезнуть
            ofSeconds(10));

        //Если записи не было или она перестала определяться 
        // через некоторое время - вернется true 
        // Иначе - выбросится исключение.
        boolean isAbsent2 = springData().absenceOfOrThrow(
            "Test entities",
            all(testRepository), 
            //опционально,
            //можно указать критерии
            ofSeconds(10));
    }
}
```