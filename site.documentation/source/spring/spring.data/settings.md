# Spring Data. Свойства

Перечисленные ниже свойства дополняют [стандартный набор](./../../quick_start/settings/index.md)

## Выполнения запросов

### Таймаут

Свойства `SPRING_DATA_WAITING_FOR_SELECTION_RESULT_TIME_UNIT` и `SPRING_DATA_WAITING_FOR_SELECTION_RESULT_TIME_VALUE`
определяют время выполнения запроса с возвратом требуемого результата.

Для `SPRING_DATA_WAITING_FOR_SELECTION_RESULT_TIME_UNIT` следует указать значение, соответствующее одному из элементов
перечисления `java.time.temporal.ChronoUnit`.

Для `SPRING_DATA_WAITING_FOR_SELECTION_RESULT_TIME_VALUE`
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
    .SpringDataWaitingSelectedResultDuration
    .SPRING_DATA_WAITING_FOR_SELECTION_RESULT_TIME;

public class SomeClass {

    public void someVoid() {
        //пример доступа до величины тайм аута
        Duration timeOut = SPRING_DATA_WAITING_FOR_SELECTION_RESULT_TIME.get();
    }
}
```

Время ожидания по умолчанию = 1 секунда.

### Интервал

Свойства `SPRING_DATA_SLEEPING_TIME_UNIT` и `SPRING_DATA_SLEEPING_TIME_VALUE` определяют время интервала между попытками
получить необходимый результат в рамках таймаута выполнения запроса.

Для `SPRING_DATA_SLEEPING_TIME_UNIT` следует указать значение, соответствующее одному из элементов
перечисления `java.time.temporal.ChronoUnit`.

Для `SPRING_DATA_SLEEPING_TIME_VALUE` следует указать значение, которое может быть прочитано как положительное число
типа `java.lang.Long`.

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