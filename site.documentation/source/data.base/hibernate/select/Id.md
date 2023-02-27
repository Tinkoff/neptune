# Hibernate. По ID

## Выбор записи по ID

```java
import ru.tinkoff.qa.neptune.hibernate.model.TestEntity;

import static java.time.Duration.ofMillis;
import static java.time.Duration.ofSeconds;
import static ru.tinkoff.qa.neptune.hibernate.HibernateContext.hibernate;
import static ru.tinkoff.qa.neptune.hibernate.select.common.CommonSelectStepFactory.byId;

public class MyTest {

    @Test
    public void test() {
        TestEntity entity = hibernate().select(
            //описание того ЧТО выбирается,
            //в свободной форме или бизнес
            //терминологии
            "Test entity",
            byId(TestEntity.class, 1L)
                //можно указать один-несколько критериев,
                //которым должна соответствовать отобранная запись
                // Так же доступны criteriaOr(criteria...), 
                // criteriaOnlyOne(criteria...)
                // criteriaNot(criteria...)
                .criteria("Name == 'Some name'", testEntity -> testEntity.getName().equals("Some name"))
                //можно указать время, 
                // за которое подходящая запись должна быть выбрана,
                // если оно отличается от того,
                // что указано в свойствах/переменных окружения
                // HIBERNATE_WAITING_FOR_SELECTION_RESULT_TIME_UNIT 
                // и HIBERNATE_WAITING_FOR_SELECTION_RESULT_TIME_VALUE
                .timeOut(ofSeconds(5))
                //можно указать время интервала между попытками получить
                // необходимый результат в рамках отведенного для этого времени, 
                // если значение этого интервала
                //должно отличаться от того, 
                // что указано в свойствах/переменных окружения
                // HIBERNATE_SLEEPING_TIME_UNIT и HIBERNATE_SLEEPING_TIME_VALUE
                .pollingInterval(ofMillis(200))
                //Можно указать, что должно быть выброшено исключение, 
                // если записи с требуемым ID нет совсем 
                // или она не соответствует перечисленным критериям. 
                // Иначе - вернется null.
                .throwOnNoResult());
    }
}
```

Про свойства / переменные окружения `HIBERNATE_SLEEPING_TIME_UNIT`, `HIBERNATE_SLEEPING_TIME_VALUE`,
`HIBERNATE_SLEEPING_TIME_UNIT` и `HIBERNATE_SLEEPING_TIME_VALUE` можно прочитать [здесь](./../settings/Hibernate_WaitingTime.md).

## Выбор записей по ID

```java
import ru.tinkoff.qa.neptune.hibernate.model.TestEntity;

import java.util.List;

import static java.time.Duration.ofMillis;
import static java.time.Duration.ofSeconds;
import static ru.tinkoff.qa.neptune.hibernate.HibernateContext.hibernate;
import static ru.tinkoff.qa.neptune.hibernate.select.common.CommonSelectStepFactory.byIds;


public class MyTest {

    @Test
    public void myTest() {
        List<TestEntity> entities = hibernate().select(
            //описание того ЧТО выбирается,
            //в свободной форме или бизнес
            //терминологии
            "Test entities",
            byIds(TestEntity.class, 1L, 2L)
                //можно указать один-несколько критериев,
                //которым должны соответствовать отобранные записи
                // Так же доступны criteriaOr(criteria...), 
                // criteriaOnlyOne(criteria...)
                // criteriaNot(criteria...)
                .criteria(
                    "Name == 'Some name'",
                    testEntity -> testEntity.getName().equals("Some name")
                )
                // можно указать время, 
                // за которое подходящие записи должны быть выбраны,
                // если оно отличается от того, 
                // что указано в свойствах/переменных окружения
                // HIBERNATE_WAITING_FOR_SELECTION_RESULT_TIME_UNIT 
                // и HIBERNATE_WAITING_FOR_SELECTION_RESULT_TIME_VALUE
                .timeOut(ofSeconds(5))
                //можно указать время интервала между попытками получить
                // необходимый результат в рамках отведенного для этого времени, 
                // если значение этого интервала
                //должно отличаться от того, 
                // что указано в свойствах/переменных окружения
                // HIBERNATE_SLEEPING_TIME_UNIT и HIBERNATE_SLEEPING_TIME_VALUE
                .pollingInterval(ofMillis(200))
                //другие опциональные параметры
        );
    }
}
```

Про свойства / переменные окружения `HIBERNATE_SLEEPING_TIME_UNIT`, `HIBERNATE_SLEEPING_TIME_VALUE`,
`HIBERNATE_SLEEPING_TIME_UNIT` и `HIBERNATE_SLEEPING_TIME_VALUE` можно прочитать [здесь](./../settings/Hibernate_WaitingTime.md).

```{eval-rst}
.. include:: ../../../shared_docs/steps_return_list_optiotal_parameters_async.rst
```

## Выбор данных записи по ID

```java
import ru.tinkoff.qa.neptune.hibernate.model.TestEntity;

import static java.time.Duration.ofMillis;
import static java.time.Duration.ofSeconds;
import static ru.tinkoff.qa.neptune.hibernate.HibernateContext.hibernate;
import static ru.tinkoff.qa.neptune.hibernate.select.common.CommonSelectStepFactory.byId;

public class MyTest {

    @Test
    public void myTest() {
        String name = hibernate().select(
            //описание того ЧТО выбирается,
            //в свободной форме или бизнес
            //терминологии
            "Test name",
            byId(TestEntity.class, 1L)
                //---------------------------------------
                //необходимые параметры для выбора записи
                //---------------------------------------
                //Если необходимо вернуть объект, 
                // полученных из данных найденной записи.
                //Указывается, как извлекаются данные из
                //записи
                .thenGetObject(TestEntity::getName)
                // Можно указать один-несколько критериев, 
                // которым должен соответствовать 
                // результирующий объект.
                // Так же доступны criteriaOr(criteria...), 
                // criteriaOnlyOne(criteria...)
                // criteriaNot(criteria...)
                .criteria("contains 'Some'", s -> s.contains("Some"))
                //Можно указать, что должно быть выброшено исключение,
                // если выбранная запись не содержат данные, 
                // которые бы соответствовали перечисленным критериям. 
                // Иначе - вернется null
                .throwOnNoResult()
        );
    }

}
```

```java
import ru.tinkoff.qa.neptune.hibernate.model.TestEntity;

import java.util.List;

import static java.time.Duration.ofMillis;
import static java.time.Duration.ofSeconds;
import static ru.tinkoff.qa.neptune.hibernate.HibernateContext.hibernate;
import static ru.tinkoff.qa.neptune.hibernate.select.common.CommonSelectStepFactory.byId;

public class MyTest {

    @Test
    public void myTest() {
        List<String> listData = hibernate().select(
            "Test list",
            byId(TestEntity.class, 1L)
                //---------------------------------------
                //необходимые параметры для выбора записи
                //---------------------------------------
                //Если необходимо вернуть лист, 
                // составленный из данных найденной записи.
                //Указывается, как извлекаются данные из
                //записи
                .thenGetList(TestEntity::getListData)
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
import ru.tinkoff.qa.neptune.hibernate.model.TestEntity;

import static java.time.Duration.ofMillis;
import static java.time.Duration.ofSeconds;
import static ru.tinkoff.qa.neptune.hibernate.HibernateContext.hibernate;
import static ru.tinkoff.qa.neptune.hibernate.select.common.CommonSelectStepFactory.byId;

public class MyTest {

    @Test
    public void myTest() {        
        String[] arrayData = hibernate().select(
            "Test array",
             byId(TestEntity.class, 1L)
                //---------------------------------------
                //необходимые параметры для выбора записи
                //---------------------------------------
                //Если необходимо вернуть массив, 
                // составленный из данных найденной записи.
                //Указывается, как извлекаются данные из
                //записи
                .thenGetArray(TestEntity::getArrayData)
                // далее можно указать опциональные 
                // уточняющие параметры
        );
    }
}
```

```{eval-rst}
.. include:: ../../../shared_docs/steps_return_array_optiotal_parameters_sync.rst
```

```java
import ru.tinkoff.qa.neptune.hibernate.model.TestEntity;

import static java.time.Duration.ofMillis;
import static java.time.Duration.ofSeconds;
import static ru.tinkoff.qa.neptune.hibernate.HibernateContext.hibernate;
import static ru.tinkoff.qa.neptune.hibernate.select.common.CommonSelectStepFactory.byId;


public class MyTest {

    @Test
    public void myTest() {        
        String listItem = hibernate().select(
            "Test list item",
            byId(TestEntity.class, 1L)
                //---------------------------------------
                //необходимые параметры для выбора записи
                //---------------------------------------
                //Если необходимо вернуть один объект
                //из данных найденной записи, собранных в набор.
                //Указывается, как извлекаются данные из записи
                .thenGetIterableItem(TestEntity::getListData)
                // далее можно указать опциональные 
                // уточняющие параметры
        );
    }
}
```

```{eval-rst}
.. include:: ../../../shared_docs/steps_return_iterable_item_optiotal_parameters_sync.rst
```

```java
import ru.tinkoff.qa.neptune.hibernate.model.TestEntity;

import static java.time.Duration.ofMillis;
import static java.time.Duration.ofSeconds;
import static ru.tinkoff.qa.neptune.hibernate.HibernateContext.hibernate;
import static ru.tinkoff.qa.neptune.hibernate.select.common.CommonSelectStepFactory.byId;

public class MyTest {

    @Test
    public void myTest() {        
        String arrayItem = hibernate().select(
            "Test array item",
            byId(TestEntity.class, 1L)
                //---------------------------------------
                //необходимые параметры для выбора записи
                //---------------------------------------
                //Если необходимо вернуть один объект
                //из данных найденной записи, собранных в массив
                //Указывается, как извлекаются данные из записи
                .thenGetArrayItem(TestEntity::getArrayData)
                // далее можно указать опциональные 
                // уточняющие параметры
        );
    }
}
```

```{eval-rst}
.. include:: ../../../shared_docs/steps_return_array_item_optiotal_parameters_sync.rst
```

## Выбор данных записей по ID

```java
import ru.tinkoff.qa.neptune.hibernate.model.TestEntity;

import java.util.List;

import static java.time.Duration.ofMillis;
import static java.time.Duration.ofSeconds;
import static ru.tinkoff.qa.neptune.hibernate.HibernateContext.hibernate;
import static ru.tinkoff.qa.neptune.hibernate.select.common.CommonSelectStepFactory.byIds;

public class MyTest {

    @Test
    public void myTest() {
        List<String> names = hibernate().select(
            //описание того ЧТО следует получить,
            //в свободной форме или бизнес
            //терминологии
            "Test names",
            byIds(TestEntity.class, 1L, 2L)
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
import ru.tinkoff.qa.neptune.hibernate.model.TestEntity;

import static java.time.Duration.ofMillis;
import static java.time.Duration.ofSeconds;
import static ru.tinkoff.qa.neptune.hibernate.HibernateContext.hibernate;
import static ru.tinkoff.qa.neptune.hibernate.select.common.CommonSelectStepFactory.byIds;

public class MyTest {

    @Test
    public void myTest() {        
        String name = hibernate().select(
            "Test name",
            byIds(TestEntity.class, 1L, 2L)
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

## Наличие или отсутствие записей по ID

```java
import ru.tinkoff.qa.neptune.hibernate.model.TestEntity;
import javax.persistence.QueryTimeoutException;

import static ru.tinkoff.qa.neptune.hibernate.HibernateContext.hibernate;
import static ru.tinkoff.qa.neptune.hibernate.select.common.CommonSelectStepFactory.byIds;

public class MyTest {

    @Test
    public void presenceTest() {

        //Если запись была или появилась через некоторое время - вернется true. 
        // Иначе - false.
        boolean isPresent = hibernate().presenceOf(
            //описание того ЧТО должно 
            // присутствовать, в свободной форме 
            // или бизнес терминологии
            "Test entity",
            byId(TestEntity.class, 1L)
            //или byIds(TestEntity.class, 1L, 2L)    
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
                // HIBERNATE_WAITING_FOR_SELECTION_RESULT_TIME_UNIT 
                // и HIBERNATE_WAITING_FOR_SELECTION_RESULT_TIME_VALUE
                .timeOut(ofSeconds(5))
            //Прочие описанные выше 
            //настройки и параметры игнорируются здесь
            ,
            //Можно перечислить классы исключений, 
            //которые должны быть проигнорированы 
            //в ходе выполнения шага
            QueryTimeoutException.class
        );

        //Если запись была или появилась в течение - вернется true. Иначе - выбросится исключение.
        var isPresent2 = hibernate().presenceOfOrThrow(
            "Test entity",
            //опционально критерии и время
            byId(TestEntity.class, 1L)
            //или byIds(TestEntity.class, 1L, 2L)  
        );
    }

    @Test
    public void absenceTest() {

        //Если записи не было или она перестала определяться 
        // через некоторое время - вернется true 
        // Иначе - false.
        boolean isAbsent = hibernate().absenceOf(
            //описание того ЧТО должно 
            // отсутствовать, в свободной форме 
            // или бизнес терминологии
            "Test entity",
            byId(TestEntity.class, 1L)
            //или byIds(TestEntity.class, 1L, 2L)  
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
        boolean isAbsent2 = hibernate().absenceOfOrThrow(
            "Test entity",
            byId(TestEntity.class, 1L),
            //или byIds(TestEntity.class, 1L, 2L) 
            //опционально,
            //можно указать критерии
            ofSeconds(10));
    }
}
```
