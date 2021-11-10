# Работа со Spring Data.

Расширение [абстракций для работы с базами данных в Neptune](./../../../database.abstractions/README.md) с возможностью 
взаимодействовать со [Spring data](https://spring.io/projects/spring-data).

Данный модуль поддерживает работу с любыми [репозиториями](https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#repositories), которые являются расширениями:

- [CrudRepository](https://docs.spring.io/spring-data/commons/docs/current/api/org/springframework/data/repository/CrudRepository.html)
- [ReactiveCrudRepository](https://docs.spring.io/spring-data/commons/docs/current/api/org/springframework/data/repository/reactive/ReactiveCrudRepository.html)
- [RxJava2CrudRepository](https://docs.spring.io/spring-data/commons/docs/current/api/org/springframework/data/repository/reactive/RxJava2CrudRepository.html)
- [RxJava3CrudRepository](https://docs.spring.io/spring-data/commons/docs/current/api/org/springframework/data/repository/reactive/RxJava3CrudRepository.html)
- [PagingAndSortingRepository](https://docs.spring.io/spring-data/commons/docs/current/api/org/springframework/data/repository/PagingAndSortingRepository.html)
- [ReactiveSortingRepository](https://docs.spring.io/spring-data/commons/docs/current/api/org/springframework/data/repository/reactive/ReactiveSortingRepository.html)
- [RxJava2SortingRepository](https://docs.spring.io/spring-data/commons/docs/current/api/org/springframework/data/repository/reactive/RxJava2SortingRepository.html)
- [RxJava3SortingRepository](https://docs.spring.io/spring-data/commons/docs/current/api/org/springframework/data/repository/reactive/RxJava3SortingRepository.html)
- [QueryByExampleExecutor](https://docs.spring.io/spring-data/commons/docs/current/api/org/springframework/data/repository/query/QueryByExampleExecutor.html)
- [ReactiveQueryByExampleExecutor](https://docs.spring.io/spring-data/commons/docs/current/api/org/springframework/data/repository/query/ReactiveQueryByExampleExecutor.html)
- [QuerydslPredicateExecutor](https://docs.spring.io/spring-data/commons/docs/current/api/org/springframework/data/querydsl/QuerydslPredicateExecutor.html)
- [ReactiveQuerydslPredicateExecutor](https://docs.spring.io/spring-data/commons/docs/current/api/org/springframework/data/querydsl/ReactiveQuerydslPredicateExecutor.html)

## Оглавление

## Настройки

[Подробнее о механизме настроек](./../../../core.api/doc/rus/SETTINGS.MD).

Описанный ниже набор настроек/свойств дополняет [базовый набор настроек/свойств](./../../../core.api/doc/rus/SETTINGS.MD#Основной-набор-настроексвойств),
которые могут быть включены в `neptune.global.properties` или в `neptune.properties`, или использованы в качестве
свойств/переменных окружения в той среде, в которой запускаются тесты:

### Ожидание выполнения запросов. Таймаут

Свойства `SPRING_DATA_WAITING_FOR_SELECTION_RESULT_TIME_UNIT` и `SPRING_DATA_WAITING_FOR_SELECTION_RESULT_TIME_VALUE` 
определяют время ожидания выполнения запроса в базу данных и возврата требуемого результата. Для свойства 
`SPRING_DATA_WAITING_FOR_SELECTION_RESULT_TIME_UNIT` следует указать значение, соответствующее одному из элементов 
перечисления `java.time.temporal.ChronoUnit`, для свойства `SPRING_DATA_WAITING_FOR_SELECTION_RESULT_TIME_VALUE` 
следует указать значение, которое может быть прочитано как положительное число типа `java.lang.Long`.

```properties
#Укажем тайм аут
#2 секунды
SPRING_DATA_WAITING_FOR_SELECTION_RESULT_TIME_UNIT=SECONDS
SPRING_DATA_WAITING_FOR_SELECTION_RESULT_TIME_VALUE=2
```

```java
import java.time.Duration;

import static ru.tinkoff.qa.neptune.spring.data.properties
        .SpringDataWaitingSelectedResultDuration.SPRING_DATA_WAITING_FOR_SELECTION_RESULT_TIME;

public class SomeClass {

    public void someVoid() {
        //пример доступа до величины тайм аута
        Duration timeOut = SPRING_DATA_WAITING_FOR_SELECTION_RESULT_TIME.get();
    }
}
```

Время ожидания по умолчанию = 1 секунда.

[Подробнее о механизме работы подобных свойств](./../../../core.api/doc/rus/SETTINGS.MD#Свойства-для-возврата-величины-продолжительности)

[к оглавлению документа](#Оглавление)

### Ожидание выполнения запросов. Интервал

Свойства `SPRING_DATA_SLEEPING_TIME_UNIT` и `SPRING_DATA_SLEEPING_TIME_VALUE` определяют время интервала между попытками 
получить необходимый результат в рамках [времени ожидания выполнения запроса в базу данных](#Ожидание-выполнения-запросов-Таймаут). 
Для свойства `SPRING_DATA_SLEEPING_TIME_UNIT` следует указать значение, соответствующее одному из элементов
перечисления `java.time.temporal.ChronoUnit`, для свойства `SPRING_DATA_SLEEPING_TIME_VALUE` следует указать значение, 
которое может быть прочитано как положительное число типа `java.lang.Long`.

```properties
#200 миллисекунд
SPRING_DATA_SLEEPING_TIME_UNIT=MILLIS
SPRING_DATA_SLEEPING_TIME_VALUE=200
```

```java
import java.time.Duration;

import static ru.tinkoff.qa.neptune.spring.data.properties
        .SpringDataWaitingSelectedResultDuration.SPRING_DATA_SLEEPING_TIME;

public class SomeClass {

    public void someVoid() {
        Duration sleepingTime = SPRING_DATA_SLEEPING_TIME.get();
    }
}
```

[Подробнее о механизме работы подобных свойств](./../../../core.api/doc/rus/SETTINGS.MD#Свойства-для-возврата-величины-продолжительности)

[к оглавлению документа](#Оглавление)

## Шаги

- О контекстах можно прочитать [здесь](./../../../core.api/doc/rus/STEPS.MD#Контекст). 
  Объект [контекста для работы со Spring Data](https://tinkoffcreditsystems.github.io/neptune/spring.data/ru/tinkoff/qa/neptune/spring/data/SpringDataContext.html)
  в примерах ниже вызывается статическим методом `springData`

- О принципах работы шагов, которые возвращают результат, можно прочитать [здесь](./../../../core.api/doc/rus/STEPS.MD#Шаги-которые-возвращают-результат).

В приведенных ниже примерах все операции описаны с использованием интерфейса-репозитория

```java
package org.mypack;

import org.springframework.data.repository.Repository;

public interface TestRepository extends Repository<TestEntity, Long> {

  TestEntity findSomething(boolean p1, String p2, int p3);

  Iterable<TestEntity> findEntities(boolean p1, String p2, int p3);
}
```

который может расширять один или несколько из приведенных ниже интерфейсов:

- [CrudRepository](https://docs.spring.io/spring-data/commons/docs/current/api/org/springframework/data/repository/CrudRepository.html)
- [ReactiveCrudRepository](https://docs.spring.io/spring-data/commons/docs/current/api/org/springframework/data/repository/reactive/ReactiveCrudRepository.html)
- [RxJava2CrudRepository](https://docs.spring.io/spring-data/commons/docs/current/api/org/springframework/data/repository/reactive/RxJava2CrudRepository.html)
- [RxJava3CrudRepository](https://docs.spring.io/spring-data/commons/docs/current/api/org/springframework/data/repository/reactive/RxJava3CrudRepository.html)
- [PagingAndSortingRepository](https://docs.spring.io/spring-data/commons/docs/current/api/org/springframework/data/repository/PagingAndSortingRepository.html)
- [ReactiveSortingRepository](https://docs.spring.io/spring-data/commons/docs/current/api/org/springframework/data/repository/reactive/ReactiveSortingRepository.html)
- [RxJava2SortingRepository](https://docs.spring.io/spring-data/commons/docs/current/api/org/springframework/data/repository/reactive/RxJava2SortingRepository.html)
- [RxJava3SortingRepository](https://docs.spring.io/spring-data/commons/docs/current/api/org/springframework/data/repository/reactive/RxJava3SortingRepository.html)
- [QueryByExampleExecutor](https://docs.spring.io/spring-data/commons/docs/current/api/org/springframework/data/repository/query/QueryByExampleExecutor.html)
- [ReactiveQueryByExampleExecutor](https://docs.spring.io/spring-data/commons/docs/current/api/org/springframework/data/repository/query/ReactiveQueryByExampleExecutor.html)
- [QuerydslPredicateExecutor](https://docs.spring.io/spring-data/commons/docs/current/api/org/springframework/data/querydsl/QuerydslPredicateExecutor.html)
- [ReactiveQuerydslPredicateExecutor](https://docs.spring.io/spring-data/commons/docs/current/api/org/springframework/data/querydsl/ReactiveQuerydslPredicateExecutor.html)

Ниже пример модели объекта из базы данных

```java
package org.mypack;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static java.util.Arrays.copyOf;

public class TestEntity {

  private Long id;

  private String name;

  private List<String> listData;

  private String[] arrayData;

  public Long getId() {
    return id;
  }

  public TestEntity setId(Long id) {
    this.id = id;
    return this;
  }

  public String getName() {
    return name;
  }

  public TestEntity setName(String name) {
    this.name = name;
    return this;
  }

  public List<String> getListData() {
    return listData;
  }

  public TestEntity setListData(List<String> listData) {
    this.listData = listData;
    return this;
  }

  public String[] getArrayData() {
    return arrayData;
  }

  public TestEntity setArrayData(String[] arrayData) {
    this.arrayData = arrayData;
    return this;
  }
}
```

[к оглавлению документа](#Оглавление)

### Find-операции

#### Все записи

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
    List<TestEntity> entities = springData().find("All test entities", //описание того что выбирается
            all(testRepository)
                    //можно указать один-несколько критериев,
                    .criteria("Name == 'Some name'", testEntity -> testEntity.getName().equals("Some name"))
                    //которым должны соответствовать отобранные записи
                    .criteria("ID > 10", testEntity -> testEntity.getId() > 10L)
                    .timeOut(ofSeconds(5)) //можно указать время, за которое подходящие записи должны быть выбраны,
                    // если оно отличается от того, что указано в свойствах/переменных окружения
                    // SPRING_DATA_WAITING_FOR_SELECTION_RESULT_TIME_UNIT и SPRING_DATA_WAITING_FOR_SELECTION_RESULT_TIME_VALUE
                    .pollingInterval(ofMillis(200)) //можно указать время интервала между попытками получить
                    // необходимый результат в рамках отведенного для этого времени, если значение этого интервала
                    //должно отличаться от того, что указано в свойствах/переменных окружения
                    // SPRING_DATA_SLEEPING_TIME_UNIT и SPRING_DATA_SLEEPING_TIME_VALUE
                    .throwOnNoResult()); //можно указать, что должно быть выброшено исключение, если 
    // записей нет совсем или нет ни одной записи, которая бы соответствовала перечисленным 
    // критериям. Иначе - вернется null или пустая коллекция.
  }
}
```

Про свойства / переменные окружения `SPRING_DATA_SLEEPING_TIME_UNIT`, `SPRING_DATA_SLEEPING_TIME_VALUE`,
`SPRING_DATA_SLEEPING_TIME_UNIT` и `SPRING_DATA_SLEEPING_TIME_VALUE` можно прочитать [здесь](#Настройки).

Кроме записей целиком, можно сделать выборку их данных

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
    List<String> names = springData().find("Test names", //описание того что выбирается
            all(testRepository)
                    //опционально, для уточнения записей, из которых следует извлечь данные
                    .criteria("ID > 10", testEntity -> testEntity.getId() > 10L)
                    .timeOut(ofSeconds(5)) //опционально, если время на получение записей, из которых извлекаются
                    .pollingInterval(ofMillis(200)) //данные, отличается от дефолтного
                    .throwOnNoResult() //опционально, выброс исключения, если нужных записей нет
                    .thenGetList(TestEntity::getName) //необходимо вернуть лист, составленный из данных найденных записей,
                    //а так же описание того, как извлекаются данные из каждой найденной записи
                    .criteria("contains 'Some'", s -> s.contains("Some")) //можно указать один-несколько
                    // критериев, которым должен соответствовать каждый элемент, попадающий в результирующий лист
                    .throwOnNoResult()); //можно указать, что должно быть выброшено исключение,
    // если выбранные записи не содержат данные, которые бы соответствовали перечисленным
    // критериям. Иначе - вернется null или пустая коллекция.

    String name = springData().find("Test name", //описание того что выбирается
            all(testRepository)
                    //опциональные параметры выбираемых записей
                    //
                    .thenGetIterableItem(TestEntity::getName) //необходимо вернуть один объект (первый попавшийся 
                    // или подходящий по критериям) из данных найденных записей, собранных в лист,
                    // а так же описание того, как извлекаются данные из каждой найденной записи
                    .criteria("Name contains 'Some'", s -> s.contains("Some")) //можно указать один-несколько
                    // критериев, которым должен соответствовать искомый элемент агрегированного листа
                    .throwOnNoResult()); //можно указать, что должно быть выброшено исключение,
    // если выбранные записи не содержат данные, которые бы соответствовали перечисленным
    // критериям. Иначе - вернется null
  }
}
```

[к оглавлению документа](#Оглавление)

#### По ID записей

Может быть выполнено, если интерфейс-репозиторий расширяет один или несколько из перечисленных:
- [CrudRepository](https://docs.spring.io/spring-data/commons/docs/current/api/org/springframework/data/repository/CrudRepository.html)
- [ReactiveCrudRepository](https://docs.spring.io/spring-data/commons/docs/current/api/org/springframework/data/repository/reactive/ReactiveCrudRepository.html)
- [RxJava2CrudRepository](https://docs.spring.io/spring-data/commons/docs/current/api/org/springframework/data/repository/reactive/RxJava2CrudRepository.html)
- [RxJava3CrudRepository](https://docs.spring.io/spring-data/commons/docs/current/api/org/springframework/data/repository/reactive/RxJava3CrudRepository.html)

##### Выбор одной записи по ID

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
      TestEntity entity = springData().find("Test entity", //описание того что выбирается
              byId(testRepository, 1L)
                      //можно указать один-несколько критериев,
                      .criteria("Name == 'Some name'", testEntity -> testEntity.getName().equals("Some name"))
                      //которым должна соответствовать отобранная запись
                      .timeOut(ofSeconds(5)) //можно указать время, за которое подходящая запись должна быть выбрана,
                      // если оно отличается от того, что указано в свойствах/переменных окружения
                      // SPRING_DATA_WAITING_FOR_SELECTION_RESULT_TIME_UNIT и SPRING_DATA_WAITING_FOR_SELECTION_RESULT_TIME_VALUE
                      .pollingInterval(ofMillis(200)) //можно указать время интервала между попытками получить
                      // необходимый результат в рамках отведенного для этого времени, если значение этого интервала
                      //должно отличаться от того, что указано в свойствах/переменных окружения
                      // SPRING_DATA_SLEEPING_TIME_UNIT и SPRING_DATA_SLEEPING_TIME_VALUE
                      .throwOnNoResult()); //можно указать, что должно быть выброшено исключение, если 
      // записи с требуемым ID нет совсем или она не соответствует перечисленным критериям. Иначе - вернется null.
  }
}
```

Про свойства / переменные окружения `SPRING_DATA_SLEEPING_TIME_UNIT`, `SPRING_DATA_SLEEPING_TIME_VALUE`,
`SPRING_DATA_SLEEPING_TIME_UNIT` и `SPRING_DATA_SLEEPING_TIME_VALUE` можно прочитать [здесь](#Настройки).

Кроме записи целиком, можно сделать выборку ее данных

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
      String name = springData().find("Test name",
              byId(testRepository, 1L)
                      //опционально, для уточнения записи, из которой следует извлечь данные
                      .criteria("ListData has more than 1 item", testEntity -> testEntity.getListData().size() > 1)
                      .timeOut(ofSeconds(5)) //опционально, если время на получение записи, из которой извлекаются
                      .pollingInterval(ofMillis(200)) //данные, отличается от дефолтного
                      .throwOnNoResult() //опционально, выброс исключения, если нужной записи нет
                      .thenGetObject(TestEntity::getName) //необходимо вернуть объект из данных найденной записи,
                      // а так же описание того, как извлекаются данные из найденной записи
                      .criteria("contains 'Some'", s -> s.contains("Some")) //можно указать один-несколько
                      // критериев, которым должны соответствовать извлекаемые из выбранной записи данные
                      .throwOnNoResult()); //можно указать, что должно быть выброшено исключение,
      // если выбранная запись не содержат данные, которые бы соответствовали перечисленным
      // критериям. Иначе - вернется null

      List<String> listData = springData().find("Test list",
              byId(testRepository, 1L)
                      ///опциональные параметры выбираемой записи
                      //
                      .thenGetList(TestEntity::getListData) //необходимо вернуть лист, составленный из данных найденной записи,
                      //а так же описание того, как извлекаются данные из найденной записи
                      .criteria("Item contains 'Some'", s -> s.contains("Some")) //можно указать один-несколько
                      // критериев, которым должен соответствовать каждый элемент, попадающий в результирующий лист
                      .throwOnNoResult()); //можно указать, что должно быть выброшено исключение,
      // если выбранная запись не содержат данные, которые бы соответствовали перечисленным
      // критериям. Иначе - вернется null или пустая коллекция.

      String[] arrayData = springData().find("Test array",
              byId(testRepository, 1L)
                      ///опциональные параметры выбираемой записи
                      //
                      .thenGetArray(TestEntity::getArrayData) //необходимо вернуть массив, составленный из данных найденной записи,
                      //а так же описание того, как извлекаются данные из найденной записи
                      .criteria("Item contains 'Some'", s -> s.contains("Some")) //можно указать один-несколько
                      // критериев, которым должен соответствовать каждый элемент, попадающий в результирующий массив
                      .throwOnNoResult()); //можно указать, что должно быть выброшено исключение,
      // если выбранная запись не содержат данные, которые бы соответствовали перечисленным
      // критериям. Иначе - вернется null или пустой массив.

      String listItem = springData().find("Test list item",
              byId(testRepository, 1L)
                      ///опциональные параметры выбираемой записи
                      //
                      .thenGetIterableItem(TestEntity::getListData) //необходимо вернуть один объект (первый попавшийся
                      // или подходящий по критериям) из данных найденной записи, собранных в лист,
                      //а так же описание того, как извлекаются данные из найденной записи
                      .criteria("Contains 'Some'", s -> s.contains("Some")) //можно указать один-несколько
                      // критериев, которым должен соответствовать искомый элемент полученного листа
                      .throwOnNoResult()); //можно указать, что должно быть выброшено исключение,
      // если выбранная запись не содержат данные, которые бы соответствовали перечисленным
      // критериям. Иначе - вернется null.

      String arrayItem = springData().find("Test array item",
              byId(testRepository, 1L)
                      ///опциональные параметры выбираемой записи
                      //
                      .thenGetArrayItem(TestEntity::getArrayData) //необходимо вернуть один объект (первый попавшийся
                      // или подходящий по критериям) из данных найденной записи, собранных в массив,
                      //а так же описание того, как извлекаются данные из найденной записи
                      .criteria("Item contains 'Some'", s -> s.contains("Some")) ///можно указать один-несколько
                      // критериев, которым должен соответствовать искомый элемент полученного массива
                      .throwOnNoResult()); //можно указать, что должно быть выброшено исключение,
      // если выбранная запись не содержат данные, которые бы соответствовали перечисленным
      // критериям. Иначе - вернется null.
  }
}
```


[к оглавлению документа](#Оглавление)

##### Выбор нескольких записей по ID

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
        List<TestEntity> entities = springData().find("Test entities", //описание того что выбирается
                byIds(testRepository, 1L, 2L)
                        //можно указать один-несколько критериев,
                        .criteria("Name == 'Some name'", testEntity -> testEntity.getName().equals("Some name"))
                        //которым должны соответствовать отобранные записи
                        .timeOut(ofSeconds(5)) //можно указать время, за которое подходящие записи должны быть выбраны,
                        // если оно отличается от того, что указано в свойствах/переменных окружения
                        // SPRING_DATA_WAITING_FOR_SELECTION_RESULT_TIME_UNIT и SPRING_DATA_WAITING_FOR_SELECTION_RESULT_TIME_VALUE
                        .pollingInterval(ofMillis(200)) //можно указать время интервала между попытками получить
                        // необходимый результат в рамках отведенного для этого времени, если значение этого интервала
                        //должно отличаться от того, что указано в свойствах/переменных окружения
                        // SPRING_DATA_SLEEPING_TIME_UNIT и SPRING_DATA_SLEEPING_TIME_VALUE
                        .throwOnNoResult()); //можно указать, что должно быть выброшено исключение, если 
        // записей с требуемыми ID нет совсем или они не соответствуют перечисленным критериям.
        // Иначе - вернется null или пустая коллекция.
    }
}
```

Про свойства / переменные окружения `SPRING_DATA_SLEEPING_TIME_UNIT`, `SPRING_DATA_SLEEPING_TIME_VALUE`,
`SPRING_DATA_SLEEPING_TIME_UNIT` и `SPRING_DATA_SLEEPING_TIME_VALUE` можно прочитать [здесь](#Настройки).

Кроме записей целиком, можно сделать выборку их данных

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
    List<String> names = springData().find("Test names", //описание того что выбирается
            byIds(testRepository, 1L, 2L)
                    //опционально, для уточнения записей, из которых следует извлечь данные
                    .criteria("ListData has more than 1 item", testEntity -> testEntity.getListData().size() > 1)
                    .timeOut(ofSeconds(5)) //опционально, если время на получение записей, из которых извлекаются
                    .pollingInterval(ofMillis(200)) //данные, отличается от дефолтного
                    .throwOnNoResult() //опционально, выброс исключения, если нужных записей нет
                    .thenGetList(TestEntity::getName) //необходимо вернуть лист, составленный из данных найденных записей,
                    //а так же описание того, как извлекаются данные из каждой найденной записи
                    .criteria("contains 'Some'", s -> s.contains("Some")) //можно указать один-несколько
                    // критериев, которым должен соответствовать каждый элемент, попадающий в результирующий лист
                    .throwOnNoResult()); //можно указать, что должно быть выброшено исключение,
    // если выбранные записи не содержат данные, которые бы соответствовали перечисленным
    // критериям. Иначе - вернется null или пустая коллекция.

    String name = springData().find("Test name", //описание того что выбирается
            byIds(testRepository, 1L, 2L)
                    //опциональные параметры выбираемых записей
                    //
                    .thenGetIterableItem(TestEntity::getName) //необходимо вернуть один объект (первый попавшийся 
                    // или подходящий по критериям) из данных найденных записей, собранных в лист,
                    // а так же описание того, как извлекаются данные из каждой найденной записи
                    .criteria("Name contains 'Some'", s -> s.contains("Some")) //можно указать один-несколько
                    // критериев, которым должен соответствовать искомый элемент агрегированного листа
                    .throwOnNoResult()); //можно указать, что должно быть выброшено исключение,
    // если выбранные записи не содержат данные, которые бы соответствовали перечисленным
    // критериям. Иначе - вернется null
  }
}
```

[к оглавлению документа](#Оглавление)

#### Записи с сортировкой

Может быть выполнено, если интерфейс-репозиторий расширяет один или несколько из перечисленных:
- [PagingAndSortingRepository](https://docs.spring.io/spring-data/commons/docs/current/api/org/springframework/data/repository/PagingAndSortingRepository.html)
- [ReactiveSortingRepository](https://docs.spring.io/spring-data/commons/docs/current/api/org/springframework/data/repository/reactive/ReactiveSortingRepository.html)
- [RxJava2SortingRepository](https://docs.spring.io/spring-data/commons/docs/current/api/org/springframework/data/repository/reactive/RxJava2SortingRepository.html)
- [RxJava3SortingRepository](https://docs.spring.io/spring-data/commons/docs/current/api/org/springframework/data/repository/reactive/RxJava3SortingRepository.html)

```java
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.springframework.data.domain.Sort.Direction.ASC;
import static ru.tinkoff.qa.neptune.spring.data.SpringDataContext.springData;
import static ru.tinkoff.qa.neptune.spring.data.select.common.CommonSelectStepFactory.allBySorting;

@SpringBootTest
public class MyTest {

    @Autowired
    private TestRepository testRepository;

    @Test
    public void myTest() {
        List<TestEntity> entities = springData().find("Test entities", //описание того что выбирается
                //есть следующие варианты: 
                // - allBySorting(T, org.springframework.data.domain.Sort)
                // - allBySorting(T, java.lang.String...)
                // - allBySorting(T, java.util.List<org.springframework.data.domain.Sort.Order>)
                // - allBySorting(T, org.springframework.data.domain.Sort.Order...)
                // где T - тип расширяющий org.springframework.data.repository.Repository и один/несколько из перечисленных
                // выше допустимых интерфейсов
                allBySorting(testRepository, ASC, "id", "name")
                        //можно указать один-несколько критериев,
                        .criteria("Name == 'Some name'", testEntity -> testEntity.getName().equals("Some name"))
                        //которым должны соответствовать отобранные записи
                        .timeOut(ofSeconds(5)) //можно указать время, за которое подходящие записи должны быть выбраны,
                        // если оно отличается от того, что указано в свойствах/переменных окружения
                        // SPRING_DATA_WAITING_FOR_SELECTION_RESULT_TIME_UNIT и SPRING_DATA_WAITING_FOR_SELECTION_RESULT_TIME_VALUE
                        .pollingInterval(ofMillis(200)) //можно указать время интервала между попытками получить
                        // необходимый результат в рамках отведенного для этого времени, если значение этого интервала
                        //должно отличаться от того, что указано в свойствах/переменных окружения
                        // SPRING_DATA_SLEEPING_TIME_UNIT и SPRING_DATA_SLEEPING_TIME_VALUE
                        .throwOnNoResult()); //можно указать, что должно быть выброшено исключение, если 
        // записей нет совсем или нет ни одной записи, которая бы соответствовала перечисленным 
        // критериям. Иначе - вернется null или пустая коллекция.
    }
}
```

Про свойства / переменные окружения `SPRING_DATA_SLEEPING_TIME_UNIT`, `SPRING_DATA_SLEEPING_TIME_VALUE`,
`SPRING_DATA_SLEEPING_TIME_UNIT` и `SPRING_DATA_SLEEPING_TIME_VALUE` можно прочитать [здесь](#Настройки).

Кроме записей целиком, можно сделать выборку их данных. Данный механизм подробно описан
в [выбор записей по ID](#Выбор-нескольких-записей-по-ID)
и для описанного выше способа выборки он работает идентично.

[к оглавлению документа](#Оглавление)

#### Pageable

Может быть выполнено, если интерфейс-репозиторий расширяет один или несколько из перечисленных:

- [PagingAndSortingRepository](https://docs.spring.io/spring-data/commons/docs/current/api/org/springframework/data/repository/PagingAndSortingRepository.html)

```java
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.springframework.data.domain.Sort.by;
import static ru.tinkoff.qa.neptune.spring.data.SpringDataContext.springData;
import static ru.tinkoff.qa.neptune.spring.data.select.common.CommonSelectStepFactory.asAPage;

@SpringBootTest
public class MyTest {

    @Autowired
    private TestRepository testRepository;

    @Test
    public void myTest() {
        List<TestEntity> entities = springData().find("Test entities", //описание того что выбирается
                asAPage(testRepository)
                        .number(0) //опциональный параметр, значение которого должно быть > 0
                        .size(5) //опциональный параметр, значение которого должно быть > 1
                        .sort(by("id").descending().and(by("name"))) //опциональный параметр, вариант сортировки
                        //можно указать один-несколько критериев,
                        .criteria("Name == 'Some name'", testEntity -> testEntity.getName().equals("Some name"))
                        //которым должны соответствовать отобранные записи
                        .timeOut(ofSeconds(5)) //можно указать время, за которое подходящие записи должны быть выбраны,
                        // если оно отличается от того, что указано в свойствах/переменных окружения
                        // SPRING_DATA_WAITING_FOR_SELECTION_RESULT_TIME_UNIT и SPRING_DATA_WAITING_FOR_SELECTION_RESULT_TIME_VALUE
                        .pollingInterval(ofMillis(200)) //можно указать время интервала между попытками получить
                        // необходимый результат в рамках отведенного для этого времени, если значение этого интервала
                        //должно отличаться от того, что указано в свойствах/переменных окружения
                        // SPRING_DATA_SLEEPING_TIME_UNIT и SPRING_DATA_SLEEPING_TIME_VALUE
                        .throwOnNoResult()); //можно указать, что должно быть выброшено исключение, если 
        // записей нет совсем или нет ни одной записи, которая бы соответствовала перечисленным 
        // критериям. Иначе - вернется null или пустая коллекция.
    }
}
```

Про свойства / переменные окружения `SPRING_DATA_SLEEPING_TIME_UNIT`, `SPRING_DATA_SLEEPING_TIME_VALUE`,
`SPRING_DATA_SLEEPING_TIME_UNIT` и `SPRING_DATA_SLEEPING_TIME_VALUE` можно прочитать [здесь](#Настройки).

Кроме записей целиком, можно сделать выборку их данных. Данный механизм подробно описан
в [выбор записей по ID](#Выбор-нескольких-записей-по-ID)
и для описанного выше способа выборки он работает идентично.

[к оглавлению документа](#Оглавление)

#### Example

Может быть выполнено, если интерфейс-репозиторий расширяет один или несколько из перечисленных:

- [QueryByExampleExecutor](https://docs.spring.io/spring-data/commons/docs/current/api/org/springframework/data/repository/query/QueryByExampleExecutor.html)
- [ReactiveQueryByExampleExecutor](https://docs.spring.io/spring-data/commons/docs/current/api/org/springframework/data/repository/query/ReactiveQueryByExampleExecutor.html)

##### Example. Выбор одной записи.

```java
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.springframework.data.domain.ExampleMatcher.matchingAny;
import static ru.tinkoff.qa.neptune.spring.data.SpringDataContext.springData;
import static ru.tinkoff.qa.neptune.spring.data.select.common.CommonSelectStepFactory.byExample;

@SpringBootTest
public class MyTest {

    @Autowired
    private TestRepository testRepository;

    @Test
    public void myTest() {
        TestEntity entity = springData().find("Test entity", //описание того что выбирается
                byExample(testRepository, new TestEntity().setId(10L).setName("Something")) //указываем пример
                        .matcher(m -> m.withMatcher("id", exact())) //опционально,
                        .matcher(m -> m.withMatcher("name", ignoreCase())) //параметры проверки свойств
                        .initialMatcher(matchingAny()) //опционально, исходный объект ExampleMatcher,
                        // от которого выстраивается критерий отбора
                        //можно указать один-несколько критериев,
                        .criteria("ListData has more than 1 item", testEntity -> testEntity.getListData().size() > 1)
                        //которым должна соответствовать отобранная запись
                        .timeOut(ofSeconds(5)) //можно указать время, за которое подходящая запись должна быть выбрана,
                        // если оно отличается от того, что указано в свойствах/переменных окружения
                        // SPRING_DATA_WAITING_FOR_SELECTION_RESULT_TIME_UNIT и SPRING_DATA_WAITING_FOR_SELECTION_RESULT_TIME_VALUE
                        .pollingInterval(ofMillis(200)) //можно указать время интервала между попытками получить
                        // необходимый результат в рамках отведенного для этого времени, если значение этого интервала
                        //должно отличаться от того, что указано в свойствах/переменных окружения
                        // SPRING_DATA_SLEEPING_TIME_UNIT и SPRING_DATA_SLEEPING_TIME_VALUE
                        .throwOnNoResult()); //можно указать, что должно быть выброшено исключение, если
        // записи, соответствующей примеру, нет совсем или она не соответствует перечисленным критериям. Иначе - вернется null.
    }
}
```

Про свойства / переменные окружения `SPRING_DATA_SLEEPING_TIME_UNIT`, `SPRING_DATA_SLEEPING_TIME_VALUE`,
`SPRING_DATA_SLEEPING_TIME_UNIT` и `SPRING_DATA_SLEEPING_TIME_VALUE` можно прочитать [здесь](#Настройки).

Кроме записи целиком, можно сделать выборку ее данных. Данный механизм подробно описан
в [выбор записи по ID](#Выбор-одной-записи-по-ID)
и для описанного выше способа выборки он работает идентично.

[к оглавлению документа](#Оглавление)

##### Example. Выбор нескольких записей.

```java
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.springframework.data.domain.ExampleMatcher.matchingAny;
import static org.springframework.data.domain.Sort.Direction.ASC;
import static ru.tinkoff.qa.neptune.spring.data.SpringDataContext.springData;
import static ru.tinkoff.qa.neptune.spring.data.select.common.CommonSelectStepFactory.allByExample;

@SpringBootTest
public class MyTest {

  @Autowired
  private TestRepository testRepository;

  @Test
  public void myTest() {
    List<TestEntity> entities = springData().find("Test entities", //описание того что выбирается
            allByExample(testRepository,  new TestEntity().setId(10L).setName("Something")) //указываем пример
                    .matcher(m -> m.withMatcher("id", exact())) //опционально,
                    .matcher(m -> m.withMatcher("name", ignoreCase())) //параметры проверки свойств
                    .sorting(ASC, "id", "name") //опционально, вариант сортировки
                    .initialMatcher(matchingAny()) //опционально, исходный объект ExampleMatcher,
                    // от которого выстраивается критерий отбора
                    //можно указать один-несколько критериев,
                    .criteria("ListData has more than 1 item", testEntity -> testEntity.getListData().size() > 1)
                    //которым должны соответствовать отобранные записи
                    .timeOut(ofSeconds(5)) //можно указать время, за которое подходящие записи должны быть выбраны,
                    // если оно отличается от того, что указано в свойствах/переменных окружения
                    // SPRING_DATA_WAITING_FOR_SELECTION_RESULT_TIME_UNIT и SPRING_DATA_WAITING_FOR_SELECTION_RESULT_TIME_VALUE
                    .pollingInterval(ofMillis(200)) //можно указать время интервала между попытками получить
                    // необходимый результат в рамках отведенного для этого времени, если значение этого интервала
                    //должно отличаться от того, что указано в свойствах/переменных окружения
                    // SPRING_DATA_SLEEPING_TIME_UNIT и SPRING_DATA_SLEEPING_TIME_VALUE
                    .throwOnNoResult()); //можно указать, что должно быть выброшено исключение, если записей, 
    // соответствующих примеру, нет совсем или они не соответствуют перечисленным критериям. 
    // Иначе - вернется null или пустая коллекция.
  }
}
```

Про свойства / переменные окружения `SPRING_DATA_SLEEPING_TIME_UNIT`, `SPRING_DATA_SLEEPING_TIME_VALUE`,
`SPRING_DATA_SLEEPING_TIME_UNIT` и `SPRING_DATA_SLEEPING_TIME_VALUE` можно прочитать [здесь](#Настройки).

Кроме записей целиком, можно сделать выборку их данных. Данный механизм подробно описан
в [выбор записей по ID](#Выбор-нескольких-записей-по-ID)
и для описанного выше способа выборки он работает идентично.

[к оглавлению документа](#Оглавление)

#### Query DSL

Может быть выполнено, если интерфейс-репозиторий расширяет один или несколько из перечисленных:

- [QuerydslPredicateExecutor](https://docs.spring.io/spring-data/commons/docs/current/api/org/springframework/data/querydsl/QuerydslPredicateExecutor.html)
- [ReactiveQuerydslPredicateExecutor](https://docs.spring.io/spring-data/commons/docs/current/api/org/springframework/data/querydsl/ReactiveQuerydslPredicateExecutor.html)

[Query DSL](https://querydsl.com/)

##### Query DSL. Выбор одной записи.

```java
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static com.querydsl.core.types.dsl.BooleanExpression;
import static ru.tinkoff.qa.neptune.spring.data.SpringDataContext.springData;
import static ru.tinkoff.qa.neptune.spring.data.select.querydsl.QueryDSLSelectStepFactory.byPredicate;

@SpringBootTest
public class MyTest {

    @Autowired
    private TestRepository testRepository;

    @Test
    public void myTest() {
        QTestEntity q = QTestEntity.qTestEntity;
        BooleanExpression predicate = q.id.eq(1L) //динамически построенный
                .and(q.name.eq("Test name")) //запрос
                .and(q.listData.contains("A"));

        TestEntity entity = springData().find("Test entity", //описание того что выбирается
                byPredicate(testRepository, predicate)
                        //можно указать один-несколько критериев,
                        .criteria("ListData has more than 1 item", testEntity -> testEntity.getListData().size() > 1)
                        //которым должна соответствовать отобранная запись
                        .timeOut(ofSeconds(5)) //можно указать время, за которое подходящая запись должна быть выбрана,
                        // если оно отличается от того, что указано в свойствах/переменных окружения
                        // SPRING_DATA_WAITING_FOR_SELECTION_RESULT_TIME_UNIT и SPRING_DATA_WAITING_FOR_SELECTION_RESULT_TIME_VALUE
                        .pollingInterval(ofMillis(200)) //можно указать время интервала между попытками получить
                        // необходимый результат в рамках отведенного для этого времени, если значение этого интервала
                        //должно отличаться от того, что указано в свойствах/переменных окружения
                        // SPRING_DATA_SLEEPING_TIME_UNIT и SPRING_DATA_SLEEPING_TIME_VALUE
                        .throwOnNoResult()); //можно указать, что должно быть выброшено исключение, если
        // записи, найденной по запросу, нет совсем или она не соответствует перечисленным критериям. Иначе - вернется null.
    }
}
```

Про свойства / переменные окружения `SPRING_DATA_SLEEPING_TIME_UNIT`, `SPRING_DATA_SLEEPING_TIME_VALUE`,
`SPRING_DATA_SLEEPING_TIME_UNIT` и `SPRING_DATA_SLEEPING_TIME_VALUE` можно прочитать [здесь](#Настройки).

Кроме записи целиком, можно сделать выборку ее данных. Данный механизм подробно описан
в [выбор записи по ID](#Выбор-одной-записи-по-ID)
и для описанного выше способа выборки он работает идентично.

[к оглавлению документа](#Оглавление)

##### Query DSL. Выбор нескольких записей.

```java
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static com.querydsl.core.types.dsl.BooleanExpression;
import static org.springframework.data.domain.Sort.Direction.ASC;
import static ru.tinkoff.qa.neptune.spring.data.SpringDataContext.springData;
import static ru.tinkoff.qa.neptune.spring.data.select.querydsl.QueryDSLSelectStepFactory.allByPredicate;
import static ru.tinkoff.qa.neptune.spring.data.select.querydsl.QueryDSLSelectStepFactory.allOrdered;
import static ru.tinkoff.qa.neptune.spring.data.select.querydsl.QueryDSLSelectStepFactory.asAPageByPredicate;

@SpringBootTest
public class MyTest {

    @Autowired
    private TestRepository testRepository;

    @Test
    public void myTest() {
        QTestEntity q = QTestEntity.qTestEntity;
        BooleanExpression predicate = q.id.eq(1L) //динамически построенный
                .and(q.name.eq("Test name")) //запрос
                .and(q.listData.contains("A"));

        List<TestEntity> entities = springData().find("Test entities", //описание того что выбирается
                allByPredicate(testRepository, predicate)
                        //можно указать один-несколько критериев,
                        .criteria("ListData has more than 1 item", testEntity -> testEntity.getListData().size() > 1)
                        //которым должны соответствовать отобранные записи
                        .timeOut(ofSeconds(5)) //можно указать время, за которое подходящие записи должны быть выбраны,
                        // если оно отличается от того, что указано в свойствах/переменных окружения
                        // SPRING_DATA_WAITING_FOR_SELECTION_RESULT_TIME_UNIT и SPRING_DATA_WAITING_FOR_SELECTION_RESULT_TIME_VALUE
                        .pollingInterval(ofMillis(200)) //можно указать время интервала между попытками получить
                        // необходимый результат в рамках отведенного для этого времени, если значение этого интервала
                        //должно отличаться от того, что указано в свойствах/переменных окружения
                        // SPRING_DATA_SLEEPING_TIME_UNIT и SPRING_DATA_SLEEPING_TIME_VALUE
                        .throwOnNoResult()); //можно указать, что должно быть выброшено исключение, если записей, 
        // соответствующих примеру, нет совсем или они не соответствуют перечисленным критериям. 
        // Иначе - вернется null или пустая коллекция.

        //с сортировкой
        List<TestEntity> entities2 = springData().find("Test entities", //описание того что выбирается
                //есть следующие варианты: 
                // - allByPredicate(T, com.querydsl.core.types.Predicate, org.springframework.data.domain.Sort)
                // - allByPredicate(T, com.querydsl.core.types.Predicate, java.lang.String...)
                // - allByPredicate(T, com.querydsl.core.types.Predicate, java.util.List<org.springframework.data.domain.Sort.Order>)
                // - allByPredicate(T, com.querydsl.core.types.Predicate, org.springframework.data.domain.Sort.Order...)
                // где T - тип расширяющий org.springframework.data.repository.Repository и один/несколько из перечисленных
                // выше допустимых интерфейсов,
                allByPredicate(testRepository,
                        predicate,
                        ASC, "id", "name")
                //опциональные параметры выбираемых записей
                //
        );

        //с сортировкой, использование com.querydsl.core.types.OrderSpecifier
        List<TestEntity> entities3 = springData().find("Test entities", //описание того что выбирается
                allOrdered(testRepository, Order.ASC, q.id) //репозиторий и сортировка по свойству записи
                        .orderSpecifier(Order.DESC, q.name) //дополнительные варианты
                        .orderSpecifier(Order.ASC, q.arrayData) //сортировки
                        .predicate(predicate) //опционально, запрос, выпирающий записи, если не указывать - будут 
                // выбраны все записи с сортировкой
                //опциональные параметры выбираемых записей
                //
        );

        List<TestEntity> entities4 = springData().find("Test entities", //описание того что выбирается
                asAPageByPredicate(testRepository, predicate)
                        .number(0) //опциональный параметр, значение которого должно быть > 0
                        .size(5) //опциональный параметр, значение которого должно быть > 1
                        .orderSpecifier(Order.DESC, q.name) //опциональные варианты 
                        .orderSpecifier(Order.ASC, q.arrayData) //сортировки
                //опциональные параметры выбираемых записей
                //
        );
    }
}
```

Про свойства / переменные окружения `SPRING_DATA_SLEEPING_TIME_UNIT`, `SPRING_DATA_SLEEPING_TIME_VALUE`,
`SPRING_DATA_SLEEPING_TIME_UNIT` и `SPRING_DATA_SLEEPING_TIME_VALUE` можно прочитать [здесь](#Настройки).

Кроме записей целиком, можно сделать выборку их данных. Данный механизм подробно описан
в [выбор записей по ID](#Выбор-нескольких-записей-по-ID)
и для описанного выше способа выборки он работает идентично.

[к оглавлению документа](#Оглавление)

#### Произвольный вызов метода репозитория

##### Произвольный вызов метода репозитория. Выбор одной записи.

```java
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static ru.tinkoff.qa.neptune.spring.data.SpringDataContext.springData;
import static ru.tinkoff.qa.neptune.spring.data.select.common.CommonSelectStepFactory.byInvocation;

@SpringBootTest
public class MyTest {

    @Autowired
    private TestRepository testRepository;

    @Test
    public void myTest() {
        TestEntity entity = springData().find("Test entity", //описание того что выбирается
                byInvocation(testRepository, r -> r.findSomething(true, "ABCD", 123))
                        //можно указать один-несколько критериев,
                        .criteria("ListData has more than 1 item", testEntity -> testEntity.getListData().size() > 1)
                        //которым должна соответствовать отобранная запись
                        .timeOut(ofSeconds(5)) //можно указать время, за которое подходящая запись должна быть выбрана,
                        // если оно отличается от того, что указано в свойствах/переменных окружения
                        // SPRING_DATA_WAITING_FOR_SELECTION_RESULT_TIME_UNIT и SPRING_DATA_WAITING_FOR_SELECTION_RESULT_TIME_VALUE
                        .pollingInterval(ofMillis(200)) //можно указать время интервала между попытками получить
                        // необходимый результат в рамках отведенного для этого времени, если значение этого интервала
                        //должно отличаться от того, что указано в свойствах/переменных окружения
                        // SPRING_DATA_SLEEPING_TIME_UNIT и SPRING_DATA_SLEEPING_TIME_VALUE
                        .throwOnNoResult()); //можно указать, что должно быть выброшено исключение, если
        // записи, соответствующей примеру, нет совсем или она не соответствует перечисленным критериям. Иначе - вернется null.
    }
}
```

Про свойства / переменные окружения `SPRING_DATA_SLEEPING_TIME_UNIT`, `SPRING_DATA_SLEEPING_TIME_VALUE`,
`SPRING_DATA_SLEEPING_TIME_UNIT` и `SPRING_DATA_SLEEPING_TIME_VALUE` можно прочитать [здесь](#Настройки).

Кроме записи целиком, можно сделать выборку ее данных. Данный механизм подробно описан
в [выбор записи по ID](#Выбор-одной-записи-по-ID)
и для описанного выше способа выборки он работает идентично.

[к оглавлению документа](#Оглавление)

##### Произвольный вызов метода репозитория. Выбор нескольких записей.

```java
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static ru.tinkoff.qa.neptune.spring.data.SpringDataContext.springData;
import static ru.tinkoff.qa.neptune.spring.data.select.common.CommonSelectStepFactory.allByInvocation;

@SpringBootTest
public class MyTest {

    @Autowired
    private TestRepository testRepository;

    @Test
    public void myTest() {
        List<TestEntity> entities = springData().find("Test entities", //описание того что выбирается
                allByInvocation(testRepository, r -> r.findEntities(true, "ABCD", 123))
                        //можно указать один-несколько критериев,
                        .criteria("ListData has more than 1 item", testEntity -> testEntity.getListData().size() > 1)
                        //которым должны соответствовать отобранные записи
                        .timeOut(ofSeconds(5)) //можно указать время, за которое подходящие записи должны быть выбраны,
                        // если оно отличается от того, что указано в свойствах/переменных окружения
                        // SPRING_DATA_WAITING_FOR_SELECTION_RESULT_TIME_UNIT и SPRING_DATA_WAITING_FOR_SELECTION_RESULT_TIME_VALUE
                        .pollingInterval(ofMillis(200)) //можно указать время интервала между попытками получить
                        // необходимый результат в рамках отведенного для этого времени, если значение этого интервала
                        //должно отличаться от того, что указано в свойствах/переменных окружения
                        // SPRING_DATA_SLEEPING_TIME_UNIT и SPRING_DATA_SLEEPING_TIME_VALUE
                        .throwOnNoResult()); //можно указать, что должно быть выброшено исключение, если записей, 
        // соответствующих примеру, нет совсем или они не соответствуют перечисленным критериям. 
        // Иначе - вернется null или пустая коллекция.
    }
}
```

Про свойства / переменные окружения `SPRING_DATA_SLEEPING_TIME_UNIT`, `SPRING_DATA_SLEEPING_TIME_VALUE`,
`SPRING_DATA_SLEEPING_TIME_UNIT` и `SPRING_DATA_SLEEPING_TIME_VALUE` можно прочитать [здесь](#Настройки).

Кроме записей целиком, можно сделать выборку их данных. Данный механизм подробно описан
в [выбор записей по ID](#Выбор-нескольких-записей-по-ID)
и для описанного выше способа выборки он работает идентично.

[к оглавлению документа](#Оглавление)

#### Наличие / отсутствие записей или их данных

Подробнее о шагах, которые проверяют наличие или отсутствие / исчезновение чего-либо можно
прочитать [тут](./../../../core.api/doc/rus/STEPS.MD#Присутствие-и-отсутствие)

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
    //Определение наличия записи в базе с помощью запроса
    //Если на момент начала исполнения ее еще нет, то предполагается что она появится в течение 5 сек
    //Если запись была или появилась в течение 5 сек - вернется true. Иначе - false.
    var isEntityPresent = springData().presenceOf("Test entity",
            //запрос, с помощью которого извлекаются записи
            //тип запроса зависит от того, какие интерфейсы расширяет интерфейс-репозиторий
            byId(testRepository, 1L)
                    //опциональные параметры выбираемых записей
                    //
                    .timeOut(ofSeconds(5))); //тайм-аут опционален. Если не указывать, будет использовано значение,
    // указанное в свойствах/переменных окружения
    // SPRING_DATA_WAITING_FOR_SELECTION_RESULT_TIME_UNIT и SPRING_DATA_WAITING_FOR_SELECTION_RESULT_TIME_VALUE

    //Определение наличия записи в базе с помощью запроса
    //Если на момент начала исполнения ее еще нет, то предполагается что она появится в течение 5 сек
    //Если запись была или появилась в течение 5 сек - вернется true. Иначе - выбросится исключение.
    var isEntityPresent2 = springData().presenceOfOrThrow("Test entity",
            //запрос, с помощью которого извлекаются записи
            //тип запроса зависит от того, какие интерфейсы расширяет интерфейс-репозиторий
            byId(testRepository, 10L)
                    //опциональные параметры выбираемых записей
                    //
                    .timeOut(ofSeconds(5))); //тайм-аут опционален. Если не указывать, будет использовано значение,
    // указанное в свойствах/переменных окружения
    // SPRING_DATA_WAITING_FOR_SELECTION_RESULT_TIME_UNIT и SPRING_DATA_WAITING_FOR_SELECTION_RESULT_TIME_VALUE

    //Определение наличия специфических данных в базе с помощью запроса
    //Если на момент начала исполнения этих данных еще нет, то предполагается что они появятся в течение 5 сек
    //Если желаемые данные были или появились в течение 5 сек - вернется true. Иначе - false.
    var isRequiredNamePresent = springData().presenceOf("Test name",
            //запрос, с помощью которого извлекаются записи
            //тип запроса зависит от того, какие интерфейсы расширяет интерфейс-репозиторий
            byId(testRepository, 10L)
                    //опциональные параметры выбираемых записей
                    //
                    .timeOut(ofSeconds(5)) //тайм-аут опционален. Если не указывать, будет использовано значение,
                    // указанное в свойствах/переменных окружения
                    // SPRING_DATA_WAITING_FOR_SELECTION_RESULT_TIME_UNIT и SPRING_DATA_WAITING_FOR_SELECTION_RESULT_TIME_VALUE
                    .thenGetObject(TestEntity::getName)
                    .criteria("is 'Test'", s -> s.equals("Test")));


    //Определение наличия специфических данных в базе с помощью запроса
    //Если на момент начала исполнения этих данных еще нет, то предполагается что они появятся в течение 5 сек
    //Если желаемые данные были или появились в течение 5 сек - вернется true. Иначе - выбросится исключение.
    var isRequiredNamePresent2 = springData().presenceOfOrThrow("Test name",
            //запрос, с помощью которого извлекаются записи
            //тип запроса зависит от того, какие интерфейсы расширяет интерфейс-репозиторий
            byId(testRepository, 10L)
                    //опциональные параметры выбираемых записей
                    //
                    .timeOut(ofSeconds(5)) //тайм-аут опционален. Если не указывать, будет использовано значение,
                    // указанное в свойствах/переменных окружения
                    // SPRING_DATA_WAITING_FOR_SELECTION_RESULT_TIME_UNIT и SPRING_DATA_WAITING_FOR_SELECTION_RESULT_TIME_VALUE
                    .thenGetObject(TestEntity::getName)
                    .criteria("is 'Test'", s -> s.equals("Test")));

    //Аналогично для выборок из нескольких записей / производных или вычисленных с их помощью данных 
  }
}
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

    //Определение отсутствия записи в базе с помощью запроса
    //Если на момент начала исполнения она еще есть, то предполагается,
    //что она перестанет определяться запросом в течение 10 сек
    //Если записи не было или она перестала определяться в течение 10 сек - вернется true. Иначе - false.
    var isEntityAbsent = springData().absenceOf("Test entity",
            //запрос, с помощью которого извлекаются записи
            //тип запроса зависит от того, какие интерфейсы расширяет интерфейс-репозиторий
            byId(testRepository, 1L),
            //опциональные параметры выбираемых записей
            //
            //.timeOut(ofSeconds(5)) //тайм-аут не нужен, если указан - игнорируется
            ofSeconds(10));

    //Определение отсутствия записи в базе с помощью запроса
    //Если на момент начала исполнения она еще есть, то предполагается,
    //что она перестанет определяться запросом в течение 10 сек
    //Если записи не было или она перестала определяться в течение 10 сек - вернется true.
    // Иначе - выбросится исключение.
    var isEntityAbsent2 = springData().absenceOfOrThrow("Test entity",
            //запрос, с помощью которого извлекаются записи
            //тип запроса зависит от того, какие интерфейсы расширяет интерфейс-репозиторий
            byId(testRepository, 1L),
            //опциональные параметры выбираемых записей
            //
            //.timeOut(ofSeconds(5)) //тайм-аут не нужен, если указан - игнорируется
            ofSeconds(10));

    //Определение отсутствия специфических данных в базе с помощью запроса
    //Если на момент начала исполнения эти данные еще есть, то предполагается,
    // что они перестанут определяться в течение 10 сек
    //Если желаемых данных не было или они перестали определяться в течение 10 сек - вернется true. Иначе - false.
    var isRequiredNameAbsent = springData().absenceOf("Test name",
            //запрос, с помощью которого извлекаются записи
            //тип запроса зависит от того, какие интерфейсы расширяет интерфейс-репозиторий
            byId(testRepository, 10L)
                    //опциональные параметры выбираемых записей
                    //
                    //.timeOut(ofSeconds(5)) //тайм-аут не нужен, если указан - игнорируется
                    .thenGetObject(TestEntity::getName)
                    .criteria("is 'Test'", s -> s.equals("Test")),
            ofSeconds(10));


    //Определение отсутствия специфических данных в базе с помощью запроса
    //Если на момент начала исполнения эти данные еще есть, то предполагается,
    // что они перестанут определяться в течение 10 сек
    //Если желаемых данных не было или они перестали определяться в течение 10 сек - вернется true.
    // Иначе - выбросится исключение.
    var isRequiredNameAbsent2 = springData().absenceOfOrThrow("Test name",
            //запрос, с помощью которого извлекаются записи
            //тип запроса зависит от того, какие интерфейсы расширяет интерфейс-репозиторий
            byId(testRepository, 10L)
                    //опциональные параметры выбираемых записей
                    //
                    //.timeOut(ofSeconds(5)) //тайм-аут не нужен, если указан - игнорируется
                    .thenGetObject(TestEntity::getName)
                    .criteria("is 'Test'", s -> s.equals("Test")),
            ofSeconds(10));

    //Аналогично для выборок из нескольких записей / производных или вычисленных с их помощью данных
  }
}
```

Про свойства / переменные окружения `SPRING_DATA_SLEEPING_TIME_UNIT`, `SPRING_DATA_SLEEPING_TIME_VALUE`,
`SPRING_DATA_SLEEPING_TIME_UNIT` и `SPRING_DATA_SLEEPING_TIME_VALUE` можно прочитать [здесь](#Настройки).

Выбор данных записи [на примере записи, полученной с помощью ID](#Выбор-одной-записи-по-ID)

Выбор данных записей [на примере записей, полученных с помощью ID](#Выбор-нескольких-записей-по-ID)

[к оглавлению документа](#Оглавление)

### Delete-операции

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
import static ru.tinkoff.qa.neptune.spring.data.select.common.CommonSelectStepFactory.*;

@SpringBootTest
public class MyTest {

    @Autowired
    private TestRepository testRepository;

    @Test
    public void myTest() {
        //удаление записи, выбранной с помощью запроса
        springData().delete("Test entity",
                //тип запроса зависит от того, какие интерфейсы расширяет интерфейс-репозиторий
                byId(testRepository, 1L)
                //опциональные параметры выбираемой для последующего удаления записи
                //
        );

        //удаление записей, выбранных с помощью запроса
        springData().delete("Test entities",
                //тип запроса зависит от того, какие интерфейсы расширяет интерфейс-репозиторий
                byIds(testRepository, 1L, 2L)
                //опциональные параметры выбираемых для последующего удаления записей
                //
        );

        TestEntity testEntity; //ранее полученная запись
        TestEntity testEntity2; //ранее полученная запись

        //прямое удаление записи
        springData().delete("Test entity",
                testRepository,
                testEntity);

        //прямое удаление нескольких записей
        springData().delete("Test entities",
                testRepository,
                testEntity, testEntity2);

        //прямое удаление коллекции записей
        springData().delete("Test entities",
                testRepository,
                List.of(testEntity, testEntity2));

        //удаление записей по ID
        springData().deleteByIds("Test entities",
                testRepository,
                1L, 2L);
    }
}
```

[к оглавлению документа](#Оглавление)

### Save-операции

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
        TestEntity testEntity; //ранее полученная или новая запись
        TestEntity testEntity2; //ранее полученная или новая запись

        //прямое сохранение новой/измененной записи
        TestEntity saved = springData().save("Test entity",
                testRepository,
                testEntity);

        //прямое сохранение нескольких новых/измененных записей
        Iterable<TestEntity> savedEntities = springData().save("Test entities",
                testRepository,
                testEntity, testEntity2);

        //прямое сохранение коллекции новых/измененных записей
        Iterable<TestEntity> savedEntities2 = springData().save("Test entities",
                testRepository,
                List.of(testEntity, testEntity2));

        //прямое сохранение новой/измененной записи
        TestEntity saved2 = springData().save("Test entity",
                testRepository,
                testEntity,
                change("Set new ID = 10", e -> e.setId(10L)), //с документированием сделанных
                change("Set new name = \"Test Name 1\"", e -> e.setName("Test Name 1")), //изменений
                change("Update ListData", e -> e.setListData(List.of("A1", "B1", "C1", "D1"))),
                change("Update ArrayData", e -> e.setArrayData(new String[]{"A1", "B1", "C1", "D1"})));
                //в случае с прямым сохранением записей необязательно документировать сделанные изменения
        //тоже самое для прямого сохранения нескольких/коллекций записей

        //сохранение измененной записи, полученной с помощью запроса
        TestEntity saved3 = springData().save("Test entity",
                byId(testRepository, 1L),
                change("Set new ID = 10", e -> e.setId(10L)), //с документированием сделанных
                change("Set new name = \"Test Name 1\"", e -> e.setName("Test Name 1")), //изменений
                change("Update ListData", e -> e.setListData(List.of("A1", "B1", "C1", "D1"))),
                change("Update ArrayData", e -> e.setArrayData(new String[]{"A1", "B1", "C1", "D1"})));
                //в случае с сохранением записей, полученных запросом, обязательно документировать сделанные изменения

        //сохранение измененных записей, полученных с помощью запроса
        //аналогично примеру выше
        Iterable<TestEntity> savedEntities3 = springData().save("Test entities",
                byIds(testRepository, 1L, 2L),
                change("Set new ID = 10", e -> e.setId(10L)),
                change("Set new name = \"Test Name 1\"", e -> e.setName("Test Name 1")),
                change("Update ListData", e -> e.setListData(List.of("A1", "B1", "C1", "D1"))),
                change("Update ArrayData", e -> e.setArrayData(new String[]{"A1", "B1", "C1", "D1"})));
    }
}
```

[к оглавлению документа](#Оглавление)

