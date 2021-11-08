# Работа со Spring Data.

Расширение [абстракций для работы с базами данных в Neptune](./../../../database.abstractions/README.md) с возможностью 
взаимодействовать со [Spring data](https://spring.io/projects/spring-data).

Данный модуль поддерживает работу с любыми [репозиториями](https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#repositories), которые являются расширениями:

- [CrudRepository](https://docs.spring.io/spring-data/commons/docs/current/api/org/springframework/data/repository/CrudRepository.html)
- [ReactiveCrudRepository](https://docs.spring.io/spring-data/commons/docs/current/api/org/springframework/data/repository/reactive/ReactiveCrudRepository.html)
- [RxJava2CrudRepository](https://docs.spring.io/spring-data/commons/docs/current/api/org/springframework/data/repository/reactive/RxJava2CrudRepository.html)
- [RxJava3CrudRepository](https://docs.spring.io/spring-data/commons/docs/current/api/org/springframework/data/repository/reactive/RxJava3CrudRepository.html)
- [PagingAndSortingRepository](https://docs.spring.io/spring-data/commons/docs/current/api/org/springframework/data/repository/PagingAndSortingRepository.html)
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
- [QueryByExampleExecutor](https://docs.spring.io/spring-data/commons/docs/current/api/org/springframework/data/repository/query/QueryByExampleExecutor.html)
- [ReactiveQueryByExampleExecutor](https://docs.spring.io/spring-data/commons/docs/current/api/org/springframework/data/repository/query/ReactiveQueryByExampleExecutor.html)
- [QuerydslPredicateExecutor](https://docs.spring.io/spring-data/commons/docs/current/api/org/springframework/data/querydsl/QuerydslPredicateExecutor.html)
- [ReactiveQuerydslPredicateExecutor](https://docs.spring.io/spring-data/commons/docs/current/api/org/springframework/data/querydsl/ReactiveQuerydslPredicateExecutor.html)

[к оглавлению документа](#Оглавление)

### Find-операции

#### Все записи

#### По ID записей

#### Записи с сортировкой

#### Pageable

#### Example

#### Query DSL

### Произвольный вызов метода репозитория

### Delete-операции

### Save-операции

