# Hibernate. Время ожидания запросов.

## Ожидание выполнения запросов. Таймаут

Свойства `HIBERNATE_WAITING_FOR_SELECTION_RESULT_TIME_UNIT` и `HIBERNATE_WAITING_FOR_SELECTION_RESULT_TIME_VALUE`
определяют время ожидания выполнения запроса в базу данных и возврата требуемого результата. Для свойства
`HIBERNATE_WAITING_FOR_SELECTION_RESULT_TIME_UNIT` следует указать значение, соответствующее одному из элементов
перечисления `java.time.temporal.ChronoUnit`, для свойства `HIBERNATE_WAITING_FOR_SELECTION_RESULT_TIME_VALUE`
следует указать значение, которое может быть прочитано как положительное число типа `java.lang.Long`.

```properties
#Укажем тайм аут
#2 секунды
HIBERNATE_WAITING_FOR_SELECTION_RESULT_TIME_UNIT=SECONDS
HIBERNATE_WAITING_FOR_SELECTION_RESULT_TIME_VALUE=2
```

```java
import java.time.Duration;

import static ru.tinkoff.qa.neptune.hibernate.properties
        .HibernateWaitingSelectedResultDuration.HIBERNATE_WAITING_FOR_SELECTION_RESULT_TIME;

public class SomeClass {

  public void someVoid() {
    //пример доступа до величины тайм аута
    Duration timeOut = HIBERNATE_WAITING_FOR_SELECTION_RESULT_TIME.get();
  }
}
```

Время ожидания по умолчанию = 1 секунда.

## Ожидание выполнения запросов. Интервал

Свойства `HIBERNATE_SLEEPING_TIME_UNIT` и `HIBERNATE_SLEEPING_TIME_VALUE` определяют время интервала между попытками
получить необходимый результат в рамках времени ожидания выполнения запроса в базу данных. Для
свойства `HIBERNATE_SLEEPING_TIME_UNIT` следует указать значение, соответствующее одному из элементов
перечисления `java.time.temporal.ChronoUnit`, для свойства `HIBERNATE_SLEEPING_TIME_VALUE` следует указать значение,
которое может быть прочитано как положительное число типа `java.lang.Long`.

```properties
#200 миллисекунд
HIBERNATE_SLEEPING_TIME_UNIT=MILLIS
HIBERNATE_SLEEPING_TIME_VALUE=200
```

```java
import java.time.Duration;

import static ru.tinkoff.qa.neptune.hibernate.properties
        .HibernateWaitingSelectedResultDuration.HIBERNATE_SLEEPING_TIME;

public class SomeClass {

  public void someVoid() {
    Duration sleepingTime = HIBERNATE_SLEEPING_TIME.get();
  }
}
```