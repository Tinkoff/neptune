# По ID

Может быть выполнено, если интерфейс-репозиторий расширяет один или несколько из перечисленных:

- [CrudRepository](https://docs.spring.io/spring-data/commons/docs/current/api/org/springframework/data/repository/CrudRepository.html)
- [ReactiveCrudRepository](https://docs.spring.io/spring-data/commons/docs/current/api/org/springframework/data/repository/reactive/ReactiveCrudRepository.html)
- [RxJava2CrudRepository](https://docs.spring.io/spring-data/commons/docs/current/api/org/springframework/data/repository/reactive/RxJava2CrudRepository.html)
- [RxJava3CrudRepository](https://docs.spring.io/spring-data/commons/docs/current/api/org/springframework/data/repository/reactive/RxJava3CrudRepository.html)

## Выбор одной записи по ID

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
        TestEntity entity = springData().find(
            //описание того ЧТО выбирается,
            //в свободной форме или бизнес
            //терминологии
            "Test entity",
            byId(testRepository, 1L)
                //можно указать один-несколько критериев,
                //которым должна соответствовать отобранная запись
                // Так же доступны criteriaOr(criteria...), 
                // criteriaOnlyOne(criteria...)
                // criteriaNot(criteria...)
                .criteria(
                    "Name == 'Some name'",
                    testEntity -> testEntity.getName().equals("Some name")
                )
                // можно указать время, 
                // за которое подходящая запись должна быть выбрана,
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
                //Можно указать, что должно быть выброшено исключение, 
                // если записи с требуемым ID нет совсем 
                // или она не соответствует перечисленным критериям. 
                // Иначе - вернется null.
                .throwOnNoResult());
    }
}
```

Про свойства / переменные окружения `SPRING_DATA_SLEEPING_TIME_UNIT`, `SPRING_DATA_SLEEPING_TIME_VALUE`,
`SPRING_DATA_SLEEPING_TIME_UNIT` и `SPRING_DATA_SLEEPING_TIME_VALUE` можно прочитать [здесь](./../settings.md).

## Выбор нескольких записей по ID

```java
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static ru.tinkoff.qa.neptune.spring.data.SpringDataContext.springData;
import static ru.tinkoff.qa.neptune.spring.data.select.common.CommonSelectStepFactory.byIds;

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
            byIds(testRepository, 1L, 2L)
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
                // Можно указать, что должно быть выброшено исключение, 
                // если записей нет совсем или нет ни одной записи, 
                // которая бы соответствовала перечисленным 
                // критериям. 
                // Иначе - вернется null или пустая коллекция.
                .throwOnNoResult()
                //ТАКЖЕ ЕСТЬ СЛЕДУЮЩИЕ ОПЦИИ:
                //можно указать сколько объектов,
                //которые соответствуют критериям,
                //нужно вернуть
                .returnListOfSize(3)
                //-------------------------------------------
                //можно указать, до элемента с каким индексом
                //нужно собрать результирующие элементы,
                //индекс - индекс объекта в наборе элементов, 
                //которые соответствуют критериям 
                .returnBeforeIndex(7)
                //.returnAfterIndex(8) либо после какого элемента
                //----------------------------------------------
                //Либо можно перечислить индексы элементов, 
                // которые следует вернуть.
                //Индексы - индексы объектов в наборе элементов, 
                //которые соответствуют критериям 
                .returnItemsOfIndexes(0, 3, 5)
                //-----------------------------------------------
                //можно указать, при достижении какого количества 
                //ВСЕХ объектов, которые соответствуют критериям, 
                //должен быть возвращен результат 
                //----------------------------------------------
                .returnIfEntireSize(isEqual(8))
                //можно указать, при достижении каких условий,
                //которым должен соответствовать лист ВСЕХ объектов,
                //соответствующих критериям,
                //можно возвращать результирующий лист/суб-лист 
                .returnOnCondition("Описание условия", list -> {
                    /*предикат, как работает критерий*/
                })
            //так же доступны returnOnConditionOr(criteria...), 
            // returnOnConditionOnlyOne(criteria...)
            // returnOnConditionNot(criteria...)
            //------------------------------------------
            //Если за отведенное время не нашлось столько подходящих объектов, 
            // чтобы вернуть результат, или вся полученная коллекция не соответствует 
            // каким-то критериям,
            //будет выброшено исключение с подробным описанием
        );
    }
}
```

Про свойства / переменные окружения `SPRING_DATA_SLEEPING_TIME_UNIT`, `SPRING_DATA_SLEEPING_TIME_VALUE`,
`SPRING_DATA_SLEEPING_TIME_UNIT` и `SPRING_DATA_SLEEPING_TIME_VALUE` можно прочитать [здесь](./../settings.md).

## Выбор данных записи, полученной по ID

```java
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static ru.tinkoff.qa.neptune.spring.data.SpringDataContext.springData;
import static ru.tinkoff.qa.neptune.spring.data.select.common.CommonSelectStepFactory.byId;

@SpringBootTest
public class MyTest {

    @Autowired
    private TestRepository testRepository;

    @Test
    public void myTest() {
        String name = springData().find(
            //описание того ЧТО выбирается,
            //в свободной форме или бизнес
            //терминологии
            "Test name",
            byId(testRepository, 1L)
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

        List<String> listData = springData().find(
            "Test list",
            byId(testRepository, 1L)
                //---------------------------------------
                //необходимые параметры для выбора записи
                //---------------------------------------
                //Если необходимо вернуть лист, 
                // составленный из данных найденной записи.
                //Указывается, как извлекаются данные из
                //записи
                .thenGetList(TestEntity::getListData)
                // Можно указать один-несколько критериев, 
                // которым должен соответствовать каждый элемент, 
                // попадающий в результирующий лист.
                // Так же доступны criteriaOr(criteria...), 
                // criteriaOnlyOne(criteria...)
                // criteriaNot(criteria...)
                .criteria("Item contains 'Some'", s -> s.contains("Some"))
                // Можно указать, что должно быть выброшено исключение,
                // если выбранная запись не содержат данные,
                // которые бы соответствовали перечисленным
                // критериям. Иначе - вернется null или пустая коллекция.
                .throwOnNoResult()
                //ТАКЖЕ ЕСТЬ СЛЕДУЮЩИЕ ОПЦИИ:
                //можно указать сколько объектов,
                //которые соответствуют критериям,
                //нужно вернуть
                .returnListOfSize(3)
                //-------------------------------------------
                //можно указать, до элемента с каким индексом
                //нужно собрать результирующие элементы,
                //индекс - индекс объекта в наборе элементов, 
                //которые соответствуют критериям 
                .returnBeforeIndex(7)
                //.returnAfterIndex(8) либо после какого элемента
                //----------------------------------------------
                //Либо можно перечислить индексы элементов, 
                // которые следует вернуть.
                //Индексы - индексы объектов в наборе элементов, 
                //которые соответствуют критериям 
                .returnItemsOfIndexes(0, 3, 5)
                //-----------------------------------------------
                //можно указать, при достижении какого количества 
                //ВСЕХ объектов, которые соответствуют критериям, 
                //должен быть возвращен результат 
                //----------------------------------------------
                .returnIfEntireSize(isEqual(8))
                //можно указать, при достижении каких условий,
                //которым должен соответствовать набор ВСЕХ объектов,
                //соответствующих критериям,
                //можно возвращать результирующий лист/суб-лист 
                .returnOnCondition("Описание условия", list -> {
                    /*предикат, как работает критерий*/
                })
            //так же доступны returnOnConditionOr(criteria...), 
            // returnOnConditionOnlyOne(criteria...)
            // returnOnConditionNot(criteria...)
            //------------------------------------------
            //Если не нашлось столько подходящих объектов, 
            // чтобы вернуть результат, или вся полученная коллекция не соответствует 
            // каким-то критериям,
            //будет выброшено исключение с подробным описанием
        );

        String[] arrayData = springData().find(
            "Test array",
            byId(testRepository, 1L)
                //---------------------------------------
                //необходимые параметры для выбора записи
                //---------------------------------------
                //Если необходимо вернуть массив, 
                // составленный из данных найденной записи.
                //Указывается, как извлекаются данные из
                //записи
                .thenGetArray(TestEntity::getArrayData)
                //Можно указать один или несколько критериев, 
                //которым должен соответствовать
                //каждый элемент результирующего массива.
                // Так же доступны criteriaOr(criteria...), 
                // criteriaOnlyOne(criteria...)
                // criteriaNot(criteria...)
                .criteria("Item contains 'Some'", s -> s.contains("Some"))
                // Можно указать, что должно быть выброшено исключение,
                // если выбранная запись не содержат данные, 
                // которые бы соответствовали перечисленным
                // критериям. Иначе - вернется null или пустой массив.
                .throwOnNoResult()
                //ТАКЖЕ ЕСТЬ СЛЕДУЮЩИЕ ОПЦИИ:
                //можно указать сколько объектов,
                //которые соответствуют критериям,
                //нужно вернуть
                .returnArrayOfLength(3)
                //-------------------------------------------
                //можно указать, до элемента с каким индексом
                //нужно собрать результирующие элементы,
                //индекс - индекс объекта в наборе элементов, 
                //которые соответствуют критериям 
                .returnBeforeIndex(7)
                //.returnAfterIndex(8) либо после какого элемента
                //----------------------------------------------
                //Либо можно перечислить индексы элементов, 
                // которые следует вернуть.
                //Индексы - индексы объектов в наборе элементов, 
                //которые соответствуют критериям 
                .returnItemsOfIndexes(0, 3, 5)
                //-----------------------------------------------
                //можно указать, при достижении какого количества
                //ВСЕХ объектов, которые соответствуют критериям, 
                //должен быть возвращен результат 
                //----------------------------------------------
                .returnIfEntireLength(isEqual(8))
                //можно указать, при достижении каких условий,
                //которым должен соответствовать массив ВСЕХ объектов,
                //соответствующих критериям,
                //можно возвращать результирующий массив/суб-массив
                .returnOnCondition("Описание условия", array -> {
                    /*предикат, как работает критерий*/
                })
            //так же доступны returnOnConditionOr(criteria...), 
            // returnOnConditionOnlyOne(criteria...)
            // returnOnConditionNot(criteria...)
            //------------------------------------------
            //Если не нашлось столько подходящих объектов, чтобы вернуть результат,
            //или весь полученный массив не соответствует каким-то критериям
            // будет выброшено исключение с подробным описанием
        );

        String listItem = springData().find(
            "Test list item",
            byId(testRepository, 1L)
                //---------------------------------------
                //необходимые параметры для выбора записи
                //---------------------------------------
                //Если необходимо вернуть один объект
                //из данных найденной записи, собранных в набор.
                //Указывается, как извлекаются данные из записи
                .thenGetIterableItem(TestEntity::getListData)
                //Можно указать один или несколько критериев, 
                //которым должен соответствовать
                //результирующий элемент из набора.
                // Так же доступны criteriaOr(criteria...), 
                // criteriaOnlyOne(criteria...)
                // criteriaNot(criteria...)
                .criteria("Contains 'Some'", s -> s.contains("Some"))
                // Можно указать, что должно быть выброшено исключение,
                // если выбранная запись не содержат данные, 
                // которые бы соответствовали перечисленным
                // критериям. Иначе - вернется null
                .throwOnNoResult()
                //ТАКЖЕ ЕСТЬ СЛЕДУЮЩИЕ ОПЦИИ:
                //Можно указать индекс элемента, который следует вернуть.
                //Индекс - индекс объекта в наборе элементов, 
                //которые соответствуют критериям 
                .returnItemOfIndex(1)
                //-----------------------------------------------
                //можно указать, при достижении какого количества 
                //ВСЕХ объектов, которые соответствуют критериям, 
                //должен быть возвращен результат 
                //----------------------------------------------
                .returnIfEntireSize(isEqual(8))
                //можно указать, при достижении каких условий,
                //которым должен соответствовать набор ВСЕХ объектов,
                //соответствующих критериям,
                //можно возвращать результирующий элемент 
                .returnOnCondition("Описание условия", iterable -> {
                    /*предикат, как работает критерий*/
                })
            //так же доступны returnOnConditionOr(criteria...), 
            // returnOnConditionOnlyOne(criteria...)
            // returnOnConditionNot(criteria...)
            //------------------------------------------
            //Если не нашлось столько подходящих объектов, чтобы вернуть результат,
            //или вся полученная коллекция не соответствует каким-то критериям
            // будет выброшено исключение с подробным описанием

        );

        String arrayItem = springData().find(
            "Test array item",
            byId(testRepository, 1L)
                //---------------------------------------
                //необходимые параметры для выбора записи
                //---------------------------------------
                //Если необходимо вернуть один объект
                //из данных найденной записи, собранных в массив
                //Указывается, как извлекаются данные из записи
                .thenGetArrayItem(TestEntity::getArrayData)
                //Можно указать один или несколько критериев, 
                //которым должен соответствовать
                //результирующий элемент из массива.
                // Так же доступны criteriaOr(criteria...), 
                // criteriaOnlyOne(criteria...)
                // criteriaNot(criteria...)
                .criteria("Item contains 'Some'", s -> s.contains("Some"))
                // Можно указать, что должно быть выброшено исключение,
                // если выбранная запись не содержат данные, 
                // которые бы соответствовали перечисленным
                // критериям. Иначе - вернется null
                .throwOnNoResult()
                //ТАКЖЕ ЕСТЬ СЛЕДУЮЩИЕ ОПЦИИ:
                //Можно указать индекс элемента, который следует вернуть.
                //Индекс - индекс объекта в наборе элементов, 
                //которые соответствуют критериям 
                .returnItemOfIndex(1)
                //-----------------------------------------------
                //можно указать, при достижении какого количества
                //ВСЕХ объектов, которые соответствуют критериям, 
                //должен быть возвращен результат 
                //----------------------------------------------
                .returnIfEntireLength(isEqual(8))
                //можно указать, при достижении каких условий,
                //которым должен соответствовать набор ВСЕХ объектов,
                //соответствующих критериям,
                //можно возвращать результирующий элемент
                .returnOnCondition("Описание условия", array -> {
                    /*предикат, как работает критерий*/
                })
            //так же доступны returnOnConditionOr(criteria...), 
            // returnOnConditionOnlyOne(criteria...)
            // returnOnConditionNot(criteria...)
            //------------------------------------------
            //Если не нашлось столько подходящих объектов, чтобы вернуть результат,
            // или весь полученный массив не соответствует каким-то критериям
            // будет выброшено исключение с подробным описанием
        );
    }
}
```

## Выбор данных записей, полученных по ID

```java
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static ru.tinkoff.qa.neptune.spring.data.SpringDataContext.springData;
import static ru.tinkoff.qa.neptune.spring.data.select.common.CommonSelectStepFactory.byIds;

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
            byIds(testRepository, 1L, 2L)
                //
                //Необходимые параметры
                //
                //-------------------------------
                //Если необходимо вернуть лист, 
                // составленный из данных найденных записей.
                //Указывается, как извлекаются данные из
                //каждой записи
                .thenGetList(TestEntity::getName)
                // Можно указать один-несколько критериев, 
                // которым должен соответствовать каждый элемент, 
                // попадающий в результирующий лист.
                // Так же доступны criteriaOr(criteria...), 
                // criteriaOnlyOne(criteria...)
                // criteriaNot(criteria...)
                .criteria("contains 'Some'", s -> s.contains("Some"))
                // Можно указать, что должно быть выброшено исключение,
                // если данные записей не содержат элементы, которые бы 
                // соответствовали перечисленным критериям. 
                // Иначе - вернется null или пустая коллекция.
                .throwOnNoResult()
                //ТАКЖЕ ЕСТЬ СЛЕДУЮЩИЕ ОПЦИИ:
                //можно указать сколько объектов,
                //которые соответствуют критериям,
                //нужно вернуть
                .returnListOfSize(3)
                //-------------------------------------------
                //можно указать, до элемента с каким индексом
                //нужно собрать результирующие элементы,
                //индекс - индекс объекта в наборе элементов, 
                //которые соответствуют критериям 
                .returnBeforeIndex(7)
                //.returnAfterIndex(8) либо после какого элемента
                //----------------------------------------------
                //Либо можно перечислить индексы элементов, 
                // которые следует вернуть.
                //Индексы - индексы объектов в наборе элементов, 
                //которые соответствуют критериям 
                .returnItemsOfIndexes(0, 3, 5)
                //-----------------------------------------------
                //можно указать, при достижении какого количества 
                //ВСЕХ объектов, которые соответствуют критериям, 
                //должен быть возвращен результат 
                //----------------------------------------------
                .returnIfEntireSize(isEqual(8))
                //можно указать, при достижении каких условий,
                //которым должен соответствовать набор ВСЕХ объектов,
                //соответствующих критериям,
                //можно возвращать результирующий лист/суб-лист 
                .returnOnCondition("Описание условия", list -> {
                    /*предикат, как работает критерий*/
                })
            //так же доступны returnOnConditionOr(criteria...), 
            // returnOnConditionOnlyOne(criteria...)
            // returnOnConditionNot(criteria...)
            //------------------------------------------
            //Если не нашлось столько подходящих объектов, 
            // чтобы вернуть результат, или вся полученная коллекция не соответствует 
            // каким-то критериям,
            //будет выброшено исключение с подробным описанием
        );

        String name = springData().find(
            "Test name",
            byIds(testRepository, 1L, 2L)
                //
                //Необходимые параметры
                //
                //-------------------------------
                //Если необходимо вернуть один объект
                //из данных найденных записей, собранных в набор.
                //Указывается, как извлекаются данные из
                //каждой записи
                .thenGetIterableItem(TestEntity::getName)
                //Можно указать один или несколько критериев, 
                //которым должен соответствовать
                //результирующий элемент из набора.
                // Так же доступны criteriaOr(criteria...), 
                // criteriaOnlyOne(criteria...)
                // criteriaNot(criteria...)
                .criteria("Name contains 'Some'", s -> s.contains("Some"))
                // Можно указать, что должно быть выброшено исключение,
                // если выбранные записи не содержат данные, 
                // которые бы соответствовали перечисленным
                // критериям. Иначе - вернется null
                .throwOnNoResult()
                //ТАКЖЕ ЕСТЬ СЛЕДУЮЩИЕ ОПЦИИ:
                //Можно указать индекс элемента, который следует вернуть.
                //Индекс - индекс объекта в наборе элементов, 
                //которые соответствуют критериям 
                .returnItemOfIndex(1)
                //-----------------------------------------------
                //можно указать, при достижении какого количества 
                //ВСЕХ объектов, которые соответствуют критериям, 
                //должен быть возвращен результат 
                //----------------------------------------------
                .returnIfEntireSize(isEqual(8))
                //можно указать, при достижении каких условий,
                //которым должен соответствовать набор ВСЕХ объектов,
                //соответствующих критериям,
                //можно возвращать результирующий элемент 
                .returnOnCondition("Описание условия", iterable -> {
                    /*предикат, как работает критерий*/
                })
            //так же доступны returnOnConditionOr(criteria...), 
            // returnOnConditionOnlyOne(criteria...)
            // returnOnConditionNot(criteria...)
            //------------------------------------------
            //Если не нашлось столько подходящих объектов, чтобы вернуть результат,
            //или вся полученная коллекция не соответствует каким-то критериям
            // будет выброшено исключение с подробным описанием          
        );
    }
}
```

## Наличие или отсутствие записей, найденных по ID

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
    public void presenceTest() {

        //Если запись была или появилась через некоторое время - вернется true. 
        // Иначе - false.
        boolean isPresent = springData().presenceOf(
            //описание того ЧТО должно 
            // присутствовать, в свободной форме 
            // или бизнес терминологии
            "Test entity",
            byId(testRepository, 1L)
            //или byIds(testRepository, 1L, 2L)    
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
            "Test entity",
            //опционально критерии и время
            byId(testRepository, 1L)
            //или byIds(testRepository, 1L, 2L)  
        );
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
            "Test entity",
            byId(testRepository, 1L)
            //или byIds(testRepository, 1L, 2L)  
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
            "Test entity",
            byId(testRepository, 1L),
            //или byIds(testRepository, 1L, 2L)
            //опционально,
            //можно указать критерии
            ofSeconds(10));
    }
}
```
