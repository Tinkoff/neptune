# Hibernate. Все записи

```java
import ru.tinkoff.qa.neptune.hibernate.model.TestEntity;

import static java.time.Duration.ofMillis;
import static java.time.Duration.ofSeconds;
import static ru.tinkoff.qa.neptune.hibernate.HibernateContext.hibernate;
import static ru.tinkoff.qa.neptune.hibernate.select.common.CommonSelectStepFactory.all;

public class MyTest {

    @Test
    public void test() {
        var entities = hibernate().select(
            //описание того ЧТО выбирается,
            //в свободной форме или бизнес
            //терминологии
            "Test entities",
            all(TestEntity.class)
                //можно указать один-несколько критериев,
                //которым должны соответствовать отобранные записи
                // Так же доступны criteriaOr(criteria...), 
                // criteriaOnlyOne(criteria...)
                // criteriaNot(criteria...)
                .criteria("Name == 'Some name'", testEntity -> testEntity.getName().equals("Some name"))
                .criteria("ID > 10", testEntity -> testEntity.getId() > 10L)
                //можно указать время, за которое подходящие записи должны быть выбраны,
                // если оно отличается от того, что указано в свойствах/переменных окружения
                // HIBERNATE_WAITING_FOR_SELECTION_RESULT_TIME_UNIT 
                // и HIBERNATE_WAITING_FOR_SELECTION_RESULT_TIME_VALUE
                .timeOut(ofSeconds(5))
                //можно указать время интервала между попытками получить
                // необходимый результат в рамках отведенного для этого времени, 
                // если значение этого интервала должно отличаться от того, 
                // что указано в свойствах/переменных окружения
                // HIBERNATE_SLEEPING_TIME_UNIT и HIBERNATE_SLEEPING_TIME_VALUE
                .pollingInterval(ofMillis(200))
        );
    }
}
```

Про свойства / переменные окружения `HIBERNATE_SLEEPING_TIME_UNIT`, `HIBERNATE_SLEEPING_TIME_VALUE`,
`HIBERNATE_SLEEPING_TIME_UNIT` и `HIBERNATE_SLEEPING_TIME_VALUE` можно прочитать [здесь](./../settings/Hibernate_WaitingTime.md).

```{eval-rst}
.. include:: ../../../shared_docs/steps_return_list_optiotal_parameters_async.rst
```

Кроме записей целиком, можно сделать выборку их данных

```java
import ru.tinkoff.qa.neptune.hibernate.model.TestEntity;

import static ru.tinkoff.qa.neptune.hibernate.HibernateContext.hibernate;
import static ru.tinkoff.qa.neptune.hibernate.select.common.CommonSelectStepFactory.all;

public class MyTest {

    @Test
    public void test() {
        var entities = hibernate().select(
            //описание того ЧТО выбирается,
            //в свободной форме или бизнес
            //терминологии
            "Test names",
            all(TestEntity.class)
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

import static ru.tinkoff.qa.neptune.hibernate.HibernateContext.hibernate;
import static ru.tinkoff.qa.neptune.hibernate.select.common.CommonSelectStepFactory.all;

public class MyTest {

    @Test
    public void test() {
        var entities = hibernate().select(
            //описание того ЧТО выбирается,
            //в свободной форме или бизнес
            //терминологии
            "Test name",
            all(TestEntity.class)
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
import ru.tinkoff.qa.neptune.hibernate.model.TestEntity;
import javax.persistence.QueryTimeoutException;

import static ru.tinkoff.qa.neptune.hibernate.HibernateContext.hibernate;
import static ru.tinkoff.qa.neptune.hibernate.select.common.CommonSelectStepFactory.all;

public class MyTest {

    @Test
    public void presenceTest() {

        //Если запись была или появилась через некоторое время - вернется true. 
        // Иначе - false.
        boolean isPresent = hibernate().presenceOf(
            //описание того ЧТО должно 
            // присутствовать, в свободной форме 
            // или бизнес терминологии
            "Test entities",
            all(TestEntity.class)
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
            "Test entities",
            //опционально критерии и время
            all(TestEntity.class),
            //опционально - игнорируемые исключения
            QueryTimeoutException.class);
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
            "Test entities",
            all(TestEntity.class)
                //можно указать один-несколько критериев,
                //которым должны соответствовать записи,
                //отсутствующие на начало выполнения шага 
                //или исчезающие, пока шаг выполняется
                .criteria(
                    "Name == 'Some name'", 
                    testEntity -> testEntity.getName().equals("Some name"))
                .criteria("ID > 10", testEntity -> testEntity.getId() > 10L),
            //Прочие описанные выше 
            //настройки и параметры игнорируются здесь
            //----------------------------------------
            //время, за которое записи должны исчезнуть
            ofSeconds(10));

        //Если записи не было или она перестала определяться 
        // через некоторое время - вернется true 
        // Иначе - выбросится исключение.
        boolean isAbsent2 = hibernate().absenceOfOrThrow(
            "Test entities",
            all(TestEntity.class), 
            //опционально,
            //можно указать критерии
            ofSeconds(10));
    }
}
```