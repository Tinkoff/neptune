# Работа с Hibernate

Расширение [абстракций для работы с базами данных в Neptune](./../../../database.abstractions/README.md) с возможностью
взаимодействовать с [Hibernate ORM](https://hibernate.org/).

Данный модуль поддерживает работу с реляционными (via Hibernate ORM) и нереляционными (via Hibernate OGM) базами данных

## Оглавление

- [Конфигурация](#Конфигурация)
  - [Подключение к базе данных](#Подключение-к-базе-данных)
- [Настройки](#Настройки)
  - [Ожидание выполнения запросов. Таймаут](#Ожидание-выполнения-запросов.-Таймаут)
  - [Ожидание выполнения запросов. Интервал](#Ожидание-выполнения-запросов.-Интервал)
- [Шаги](#Шаги)
  - [SELECT операции](#SELECT-операции)
    - [Все записи](#Все-записи)
    - [По ID записей](#По-ID-записей)
      - [Выбор одной записи по ID](#Выбор-одной-записи-по-ID)
      - [Выбор нескольких записей по ID](#Выбор-нескольких-записей-по-ID) 
    - [Записи с сортировкой](#Записи-с-сортировкой)
    - [JPA Criteria](#JPA-Criteria)
    - [HQL запрос](#HQL-запрос)
    - [Query DSL](#QueryDSL)
      - [Query DSL. Выбор одной записи.](#QueryDSL.-Выбор-одной-записи.)
      - [Query DSL. Выбор нескольких записей.](#QueryDSL.-Выбор-нескольких-записей.)
    - [Наличие / отсутствие записей или их данных](#Наличие-/-отсутствие-записей-или-их-данных)
  - [DELETE операции](#DELETE-операции)
  - [INSERT/UPDATE операции](#INSERT/UPDATE-операции)

### Подключение к базе данных

Для подключения к базам данных модуль может принимать как конфигурационный файл META-INF/persistence.xml
(спецификация Java Persistence API), так и конфигурационные файлы Hibernate API с расширением *.cfg.xml.

Пример persistence.xml:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<persistence version="2.1"
    xmlns="http://xmlns.jcp.org/xml/ns/persistence" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/persistence http://xmlns.jcp.org/xml/ns/persistence/persistence_2_1.xsd">
    <persistence-unit name="MySql" transaction-type="JTA">
        <!-- Перечисление классов-маппингов таблиц/коллекций из БД -->
        <class>org.example.entities.TestEntity</class>      
        <properties>
            <!-- Настройки соединения с БД -->
            <property name="javax.persistence.jdbc.driver" value="com.mysql.jdbc.Driver" />
            <property name="javax.persistence.jdbc.url" value="jdbc:mysql://localhost:3306/test" />
            <property name="javax.persistence.jdbc.user" value="root" />
            <property name="javax.persistence.jdbc.password" value="password" />          
        </properties>
    </persistence-unit>
</persistence>
```

Пример hibernate.cfg.xml:

```xml
<?xml version='1.0' encoding='utf-8'?>
<!DOCTYPE hibernate-configuration PUBLIC
"-//Hibernate/Hibernate Configuration DTD//EN"
"https://www.hibernate.org/dtd/hibernate-configuration-3.0.dtd">

<hibernate-configuration>
    <session-factory>
        <!-- Настройки соединения с БД -->
        <property name="hibernate.connection.driver_class">com.mysql.jdbc.Driver</property>
        <property name="hibernate.connection.url">jdbc:mysql://localhost:3306/test</property>
        <property name="hibernate.connection.username">root</property>
        <property name="hibernate.connection.password">password</property>
      
        <!-- Настройки SQL диалекта -->
        <property name="dialect">org.hibernate.dialect.MySQLDialect</property>
        <property name="hibernate.current_session_context_class">thread</property>
        <property name="hibernate.connection.CharSet">utf8</property>
        <property name="hibernate.connection.characterEncoding">utf8</property>
        <property name="hibernate.connection.useUnicode">true</property>

        <!-- Перечисление классов-маппингов таблиц/коллекций из БД или пакетов с ними -->
        <mapping package="org.example.entities" />

    </session-factory>
</hibernate-configuration>
```

По умолчанию модуль сконфигурирован на работу с файлами *.cfg.xml, однако его можно переключить
на использование persistence.xml при помощи переменной окружения `USE_JPA_CONFIG=true`.

После создания конфигурационных файлов необходимо перечислить подключенные источники в переменной окружения
`PERSISTENCE_UNITS` как в примерах ниже:

```properties
# если используется конфигурация через persistence.xml, то в переменной перечисляются имена заданных Persistence Units,
# так как в одном конфигурационном файле можно перечислить подключения ко всем базам данных
PERSISTENCE_UNITS=MySql,OracleTest

# если используется конфигурация через *.cfg.xml, то в переменную прописываются названия конфигурационных файлов,
# так как в данном случае один файл описывает подключение к одной базе данных
PERSISTENCE_UNITS=mysql.cfg.xml,oracletest.cfg.xml
```

[к оглавлению документа](#Оглавление)

## Настройки 

[Подробнее о механизме настроек](./../../../core.api/doc/rus/SETTINGS.MD).

Описанный ниже набор настроек/свойств
дополняет [базовый набор настроек/свойств](./../../../core.api/doc/rus/SETTINGS.MD#Основной-набор-настроек/свойств),
которые могут быть включены в `neptune.global.properties` или в `neptune.properties`, или использованы в качестве
свойств/переменных окружения в той среде, в которой запускаются тесты:

### Ожидание выполнения запросов. Таймаут

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

[Подробнее о механизме работы подобных свойств](./../../../core.api/doc/rus/SETTINGS.MD#Свойства-для-возврата-величины-продолжительности)

[к оглавлению документа](#Оглавление)

### Ожидание выполнения запросов. Интервал

Свойства `HIBERNATE_SLEEPING_TIME_UNIT` и `HIBERNATE_SLEEPING_TIME_VALUE` определяют время интервала между попытками
получить необходимый результат в
рамках [времени ожидания выполнения запроса в базу данных](#Ожидание-выполнения-запросов.-Таймаут). Для
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

[Подробнее о механизме работы подобных свойств](./../../../core.api/doc/rus/SETTINGS.MD#Свойства-для-возврата-величины-продолжительности)

[к оглавлению документа](#Оглавление)

## Шаги

- О контекстах можно прочитать [здесь](./../../../core.api/doc/rus/STEPS.MD#Контекст).
  Объект [контекста для работы с Hibernate](https://tinkoff.github.io/neptune/hibernate/ru/tinkoff/qa/neptune/hibernate/HibernateContext.html)
  вызывается статическим методом `hibernate`.

- О принципах работы шагов, которые возвращают результат, можно
  прочитать [здесь](./../../../core.api/doc/rus/STEPS.MD#Шаги,-которые-возвращают-результат).

Пример модели объекта из базы данных:

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

### SELECT операции

#### Все записи

```java
import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.hibernate.model.TestEntity;

import static java.time.Duration.ofMillis;
import static java.time.Duration.ofSeconds;
import static ru.tinkoff.qa.neptune.hibernate.HibernateContext.hibernate;
import static ru.tinkoff.qa.neptune.hibernate.select.common.CommonSelectStepFactory.all;

public class MyTest {

  @Test
  public void test() {
    var entities = hibernate().select("Test entities",
            all(TestEntity.class)
                    //можно указать один-несколько критериев,
                    .criteria("Name == 'Some name'", testEntity -> testEntity.getName().equals("Some name"))
                    //которым должны соответствовать отобранные записи
                    .criteria("ID > 10", testEntity -> testEntity.getId() > 10L)
                    .timeOut(ofSeconds(5)) //можно указать время, за которое подходящие записи должны быть выбраны,
                    // если оно отличается от того, что указано в свойствах/переменных окружения
                    // HIBERNATE_WAITING_FOR_SELECTION_RESULT_TIME_UNIT и HIBERNATE_WAITING_FOR_SELECTION_RESULT_TIME_VALUE
                    .pollingInterval(ofMillis(200)) //можно указать время интервала между попытками получить
                    // необходимый результат в рамках отведенного для этого времени, если значение этого интервала
                    //должно отличаться от того, что указано в свойствах/переменных окружения
                    // HIBERNATE_SLEEPING_TIME_UNIT и HIBERNATE_SLEEPING_TIME_VALUE
                    .throwOnNoResult()); //можно указать, что должно быть выброшено исключение, если 
                    // записей нет совсем или нет ни одной записи, которая бы соответствовала перечисленным 
                    // критериям. Иначе - вернется null или пустая коллекция.
  }
}
```

Про свойства / переменные окружения `HIBERNATE_SLEEPING_TIME_UNIT`, `HIBERNATE_SLEEPING_TIME_VALUE`,
`HIBERNATE_SLEEPING_TIME_UNIT` и `HIBERNATE_SLEEPING_TIME_VALUE` можно прочитать [здесь](#Настройки).

Кроме записей целиком, можно сделать выборку их данных:

```java
import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.hibernate.model.TestEntity;

import static java.time.Duration.ofMillis;
import static java.time.Duration.ofSeconds;
import static ru.tinkoff.qa.neptune.hibernate.HibernateContext.hibernate;
import static ru.tinkoff.qa.neptune.hibernate.select.common.CommonSelectStepFactory.all;

public class MyTest {

    @Test
    public void test() {
        var entities = hibernate().select("Test entities",
                all(TestEntity.class)
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
    }
}
```

[к оглавлению документа](#Оглавление)

#### По ID записей

##### Выбор одной записи по ID

```java
import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.hibernate.model.TestEntity;

import static java.time.Duration.ofMillis;
import static java.time.Duration.ofSeconds;
import static ru.tinkoff.qa.neptune.hibernate.HibernateContext.hibernate;
import static ru.tinkoff.qa.neptune.hibernate.select.common.CommonSelectStepFactory.byId;

public class MyTest {

    @Test
    public void test() {
        TestEntity entity = hibernate().select("Test entity", //описание того что выбирается
                byId(TestEntity.class, 1L)
                        //можно указать один-несколько критериев,
                        .criteria("Name == 'Some name'", testEntity -> testEntity.getName().equals("Some name"))
                        //которым должна соответствовать отобранная запись
                        .timeOut(ofSeconds(5)) //можно указать время, за которое подходящая запись должна быть выбрана,
                        // если оно отличается от того, что указано в свойствах/переменных окружения
                        // HIBERNATE_WAITING_FOR_SELECTION_RESULT_TIME_UNIT и HIBERNATE_WAITING_FOR_SELECTION_RESULT_TIME_VALUE
                        .pollingInterval(ofMillis(200)) //можно указать время интервала между попытками получить
                        // необходимый результат в рамках отведенного для этого времени, если значение этого интервала
                        //должно отличаться от того, что указано в свойствах/переменных окружения
                        // HIBERNATE_SLEEPING_TIME_UNIT и HIBERNATE_SLEEPING_TIME_VALUE
                        .throwOnNoResult()); //можно указать, что должно быть выброшено исключение, если 
                        // записи с требуемым ID нет совсем или она не соответствует перечисленным критериям. Иначе - вернется null.
    }
}
```

Про свойства / переменные окружения `HIBERNATE_SLEEPING_TIME_UNIT`, `HIBERNATE_SLEEPING_TIME_VALUE`,
`HIBERNATE_SLEEPING_TIME_UNIT` и `HIBERNATE_SLEEPING_TIME_VALUE` можно прочитать [здесь](#Настройки).

Кроме записей целиком, можно сделать выборку их данных:

```java
import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.hibernate.model.TestEntity;

import java.util.List;

import static java.time.Duration.ofMillis;
import static java.time.Duration.ofSeconds;
import static ru.tinkoff.qa.neptune.hibernate.HibernateContext.hibernate;
import static ru.tinkoff.qa.neptune.hibernate.select.common.CommonSelectStepFactory.byId;

public class MyTest {

    @Test
    public void test() {
        String name = hibernate().select("Test entity",
                byId(TestEntity.class, 1L)
                        .criteria("ListData has more than 1 item", testEntity -> testEntity.getListData().size() > 1)
                        .timeOut(ofSeconds(5))
                        .pollingInterval(ofMillis(200))
                        .throwOnNoResult()
                        .thenGetObject(TestEntity::getName) //необходимо вернуть объект из данных найденной записи,
                        // а так же описание того, как извлекаются данные из найденной записи
                        .criteria("contains 'Some'", s -> s.contains("Some")) //можно указать один-несколько
                        .throwOnNoResult()); //можно указать, что должно быть выброшено исключение,
                        // если выбранная запись не содержат данные, которые бы соответствовали перечисленным
                        // критериям. Иначе - вернется null

        List<String> listData = hibernate().select("Test list",
                byId(TestEntity.class, 1L)
                        .thenGetList(TestEntity::getListData)
                        .criteria("Item contains 'Some'", s -> s.contains("Some"))
                        .throwOnNoResult());
        String[] arrayData = hibernate().select("Test array",
                byId(TestEntity.class, 1L)
                        .thenGetArray(TestEntity::getArrayData)
                        .criteria("Item contains 'Some'", s -> s.contains("Some"))
                        .throwOnNoResult());

        String listItem = hibernate().select("Test list item",
                byId(TestEntity.class, 1L)
                        .thenGetIterableItem(TestEntity::getListData)
                        .criteria("Contains 'Some'", s -> s.contains("Some"))
                        .throwOnNoResult());

        String arrayItem = hibernate().select("Test array item",
                byId(TestEntity.class, 1L)
                        .thenGetArrayItem(TestEntity::getArrayData)
                        .criteria("Item contains 'Some'", s -> s.contains("Some"))
                        .throwOnNoResult());
    }
}
```

[к оглавлению документа](#Оглавление)

##### Выбор нескольких записей по ID

```java
import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.hibernate.model.TestEntity;

import java.util.List;

import static java.time.Duration.ofMillis;
import static java.time.Duration.ofSeconds;
import static ru.tinkoff.qa.neptune.hibernate.HibernateContext.hibernate;
import static ru.tinkoff.qa.neptune.hibernate.select.common.CommonSelectStepFactory.byIds;

public class MyTest {

    @Test
    public void test() {
        List<TestEntity> entities = hibernate().select("Test entity", //описание того что выбирается
                byIds(TestEntity.class, 1L, 2L)
                        //можно указать один-несколько критериев,
                        .criteria("Name == 'Some name'", testEntity -> testEntity.getName().equals("Some name"))
                        //которым должна соответствовать отобранная запись
                        .timeOut(ofSeconds(5)) //можно указать время, за которое подходящая запись должна быть выбрана,
                        // если оно отличается от того, что указано в свойствах/переменных окружения
                        // HIBERNATE_WAITING_FOR_SELECTION_RESULT_TIME_UNIT и HIBERNATE_WAITING_FOR_SELECTION_RESULT_TIME_VALUE
                        .pollingInterval(ofMillis(200)) //можно указать время интервала между попытками получить
                        // необходимый результат в рамках отведенного для этого времени, если значение этого интервала
                        //должно отличаться от того, что указано в свойствах/переменных окружения
                        // HIBERNATE_SLEEPING_TIME_UNIT и HIBERNATE_SLEEPING_TIME_VALUE
                        .throwOnNoResult()); //можно указать, что должно быть выброшено исключение, если 
                        // записи с требуемым ID нет совсем или она не соответствует перечисленным критериям. Иначе - вернется null.
    }
}
```

Про свойства / переменные окружения `HIBERNATE_SLEEPING_TIME_UNIT`, `HIBERNATE_SLEEPING_TIME_VALUE`,
`HIBERNATE_SLEEPING_TIME_UNIT` и `HIBERNATE_SLEEPING_TIME_VALUE` можно прочитать [здесь](#Настройки).

Кроме записей целиком, можно сделать выборку их данных:

```java
import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.hibernate.model.TestEntity;

import java.util.List;

import static java.time.Duration.ofMillis;
import static java.time.Duration.ofSeconds;
import static ru.tinkoff.qa.neptune.hibernate.HibernateContext.hibernate;
import static ru.tinkoff.qa.neptune.hibernate.select.common.CommonSelectStepFactory.byIds;

public class MyTest {

    @Test
    public void test() {
        List<String> names = hibernate().select("Test entity",
                byIds(TestEntity.class, 1L, 2L)
                        .criteria("ListData has more than 1 item", testEntity -> testEntity.getListData().size() > 1)
                        .timeOut(ofSeconds(5))
                        .pollingInterval(ofMillis(200))
                        .throwOnNoResult()
                        .thenGetList(TestEntity::getName) //необходимо вернуть объект из данных найденной записи,
                        // а так же описание того, как извлекаются данные из найденной записи
                        .criteria("contains 'Some'", s -> s.contains("Some")) //можно указать один-несколько
                        // критериев, которым должны соответствовать извлекаемые из выбранной записи данные
                        .throwOnNoResult()); //можно указать, что должно быть выброшено исключение,
                        // если выбранная запись не содержат данные, которые бы соответствовали перечисленным
                        // критериям. Иначе - вернется null

        String name = hibernate().select("Test name", //описание того что выбирается
                byIds(TestEntity.class, 1L, 2L)
                        .thenGetIterableItem(TestEntity::getName) //необходимо вернуть один объект (первый попавшийся 
                        // или подходящий по критериям) из данных найденных записей, собранных в лист,
                        // а так же описание того, как извлекаются данные из каждой найденной записи
                        .criteria("Name contains 'Some'", s -> s.contains("Some"))
                        .throwOnNoResult());
    }
}
```

[к оглавлению документа](#Оглавление)

#### Записи с сортировкой

Для сортировки используется механизм из Java Persistence API:

```java
import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.hibernate.model.TestEntity;

import java.util.List;

import static java.time.Duration.ofMillis;
import static java.time.Duration.ofSeconds;
import static ru.tinkoff.qa.neptune.hibernate.HibernateContext.hibernate;
import static ru.tinkoff.qa.neptune.hibernate.select.common.CommonSelectStepFactory.allByOrder;

public class MyTest {

    @Test
    public void test() {
        var cb = hibernate().getCriteriaBuilder(TestEntity.class); // CriteriaBuilder для сущности TestEntity
        var cq = hibernate().getCriteriaQuery(TestEntity.class); // CriteriaQuery для сущности TestEntity
        var root = cq.from(TestEntity.class); // Root для сущности TestEntity
        
        List<TestEntity> entities = hibernate().select("Test entities", //описание того что выбирается
                //есть следующие варианты: 
                // - allByOrder(Class<R> entity, javax.persistence.criteria.Order... orders)
                // - allByOrder(Class<R> entity, java.util.List<javax.persistence.criteria.Order> orders)
                allByOrder(TestEntity.class, cb.desc(root.get("name")))
                        //можно указать один-несколько критериев,
                        .criteria("Name == 'Some name'", testEntity -> testEntity.getName().equals("Some name"))
                        //которым должны соответствовать отобранные записи
                        .timeOut(ofSeconds(5)) //можно указать время, за которое подходящие записи должны быть выбраны,
                        // если оно отличается от того, что указано в свойствах/переменных окружения
                        // HIBERNATE_WAITING_FOR_SELECTION_RESULT_TIME_UNIT и HIBERNATE_WAITING_FOR_SELECTION_RESULT_TIME_VALUE
                        .pollingInterval(ofMillis(200)) //можно указать время интервала между попытками получить
                        // необходимый результат в рамках отведенного для этого времени, если значение этого интервала
                        //должно отличаться от того, что указано в свойствах/переменных окружения
                        // HIBERNATE_SLEEPING_TIME_UNIT и HIBERNATE_SLEEPING_TIME_VALUE
                        .throwOnNoResult()); //можно указать, что должно быть выброшено исключение, если 
                        // записей нет совсем или нет ни одной записи, которая бы соответствовала перечисленным 
                        // критериям. Иначе - вернется null или пустая коллекция.
    }
}
```

Про свойства / переменные окружения `HIBERNATE_SLEEPING_TIME_UNIT`, `HIBERNATE_SLEEPING_TIME_VALUE`,
`HIBERNATE_SLEEPING_TIME_UNIT` и `HIBERNATE_SLEEPING_TIME_VALUE` можно прочитать [здесь](#Настройки).

Кроме записей целиком, можно сделать выборку их данных. Данный механизм подробно описан
в [выбор записей по ID](#Выбор-нескольких-записей-по-ID)
и для описанного выше способа выборки он работает идентично.

[к оглавлению документа](#Оглавление)

#### Pageable

При использовании Pageable-выборки параметр limit отвечает за количество подлежащих выбору элементов, а
параметр offset за сдвиг отрезка выборки вправо относительно всех выбранных с сортировкой по умолчанию элементов.

```java
import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.hibernate.model.TestEntity;

import java.util.List;

import static java.time.Duration.ofMillis;
import static java.time.Duration.ofSeconds;
import static ru.tinkoff.qa.neptune.hibernate.HibernateContext.hibernate;
import static ru.tinkoff.qa.neptune.hibernate.select.common.CommonSelectStepFactory.asAPage;

public class MyTest {

    @Test
    public void test() {
        List<TestEntity> entities = hibernate().select("Test entities", //описание того что выбирается
                asAPage(TestEntity.class)
                        .limit(10) // по умолчанию 1, значение должно быть >= 1
                        .offset(5) // по умолчанию 0, значение должно быть >= 0
                        //можно указать один-несколько критериев,
                        .criteria("Name == 'Some name'", testEntity -> testEntity.getName().equals("Some name"))
                        //которым должны соответствовать отобранные записи
                        .timeOut(ofSeconds(5)) //можно указать время, за которое подходящие записи должны быть выбраны,
                        // если оно отличается от того, что указано в свойствах/переменных окружения
                        // HIBERNATE_WAITING_FOR_SELECTION_RESULT_TIME_UNIT и HIBERNATE_WAITING_FOR_SELECTION_RESULT_TIME_VALUE
                        .pollingInterval(ofMillis(200)) //можно указать время интервала между попытками получить
                        // необходимый результат в рамках отведенного для этого времени, если значение этого интервала
                        //должно отличаться от того, что указано в свойствах/переменных окружения
                        // HIBERNATE_SLEEPING_TIME_UNIT и HIBERNATE_SLEEPING_TIME_VALUE
                        .throwOnNoResult()); //можно указать, что должно быть выброшено исключение, если 
                        // записей нет совсем или нет ни одной записи, которая бы соответствовала перечисленным 
                        // критериям. Иначе - вернется null или пустая коллекция.
    }
}
```

Про свойства / переменные окружения `HIBERNATE_SLEEPING_TIME_UNIT`, `HIBERNATE_SLEEPING_TIME_VALUE`,
`HIBERNATE_SLEEPING_TIME_UNIT` и `HIBERNATE_SLEEPING_TIME_VALUE` можно прочитать [здесь](#Настройки).

Кроме записей целиком, можно сделать выборку их данных. Данный механизм подробно описан
в [выбор записей по ID](#Выбор-нескольких-записей-по-ID)
и для описанного выше способа выборки он работает идентично.

[к оглавлению документа](#Оглавление)

#### JPA Criteria

Запросы можно составлять используя Criteria API из JPA:

```java
import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.hibernate.model.TestEntity;

import java.util.List;

import static java.time.Duration.ofMillis;
import static java.time.Duration.ofSeconds;
import static ru.tinkoff.qa.neptune.hibernate.HibernateContext.hibernate;
import static ru.tinkoff.qa.neptune.hibernate.select.common.CommonSelectStepFactory.allByCriteria;
import static ru.tinkoff.qa.neptune.hibernate.select.common.CommonSelectStepFactory.byCriteria;

public class MyTest {

  @Test
  public void test() {
    var cb = hibernate().getCriteriaBuilder(TestEntity.class); // CriteriaBuilder для сущности TestEntity
    var cq = hibernate().getCriteriaQuery(TestEntity.class); // CriteriaQuery для сущности TestEntity
    var root = cq.from(TestEntity.class); // Root для сущности TestEntity

    // выбор одной записи
    TestEntity entity = hibernate().select("Test entity", //описание того что выбирается
            byCriteria(TestEntity.class, cq.where(cb.ge(root.get("id"), 1L)))
                    .criteria("Name == 'Some name'", testEntity -> testEntity.getName().equals("Some name"))
                    //которым должны соответствовать отобранные записи
                    .timeOut(ofSeconds(5)) //можно указать время, за которое подходящие записи должны быть выбраны,
                    // если оно отличается от того, что указано в свойствах/переменных окружения
                    // HIBERNATE_WAITING_FOR_SELECTION_RESULT_TIME_UNIT и HIBERNATE_WAITING_FOR_SELECTION_RESULT_TIME_VALUE
                    .pollingInterval(ofMillis(200)) //можно указать время интервала между попытками получить
                    // необходимый результат в рамках отведенного для этого времени, если значение этого интервала
                    //должно отличаться от того, что указано в свойствах/переменных окружения
                    // HIBERNATE_SLEEPING_TIME_UNIT и HIBERNATE_SLEEPING_TIME_VALUE
                    .throwOnNoResult()); //можно указать, что должно быть выброшено исключение, если
                    // записей нет совсем или нет ни одной записи, которая бы соответствовала перечисленным
                    // критериям. Иначе - вернется null или пустая коллекция.);

    // выбор нескольких записей
    List<TestEntity> entities = hibernate().select("Test entity",
            allByCriteria(TestEntity.class, cq.where(cb.like(root.get("name"), "name"))));
  }
}
```

Про свойства / переменные окружения `HIBERNATE_SLEEPING_TIME_UNIT`, `HIBERNATE_SLEEPING_TIME_VALUE`,
`HIBERNATE_SLEEPING_TIME_UNIT` и `HIBERNATE_SLEEPING_TIME_VALUE` можно прочитать [здесь](#Настройки).

Кроме записей целиком, можно сделать выборку их данных. Данный механизм подробно описан
в [выбор записей по ID](#Выбор-нескольких-записей-по-ID)
и для описанного выше способа выборки он работает идентично.

[к оглавлению документа](#Оглавление)

#### HQL запрос

HQL - язык запросов Hibernate, синтаксически похожий на SQL.

[Документация по HQL](https://docs.jboss.org/hibernate/orm/3.5/reference/en/html/queryhql.html)

```java
import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.hibernate.model.TestEntity;

import java.util.List;

import static java.time.Duration.ofMillis;
import static java.time.Duration.ofSeconds;
import static ru.tinkoff.qa.neptune.hibernate.HibernateContext.hibernate;
import static ru.tinkoff.qa.neptune.hibernate.select.common.CommonSelectStepFactory.allByQuery;
import static ru.tinkoff.qa.neptune.hibernate.select.common.CommonSelectStepFactory.byQuery;

public class MyTest {

    @Test
    public void test() {
        // выбор одной записи
        TestEntity entity = hibernate().select("Test entity",
                byQuery(TestEntity.class, "select e from test_entities e where id = 1")
                        .criteria("Name == 'Some name'", testEntity -> testEntity.getName().equals("Some name"))
                        //которым должны соответствовать отобранные записи
                        .timeOut(ofSeconds(5)) //можно указать время, за которое подходящие записи должны быть выбраны,
                        // если оно отличается от того, что указано в свойствах/переменных окружения
                        // HIBERNATE_WAITING_FOR_SELECTION_RESULT_TIME_UNIT и HIBERNATE_WAITING_FOR_SELECTION_RESULT_TIME_VALUE
                        .pollingInterval(ofMillis(200)) //можно указать время интервала между попытками получить
                        // необходимый результат в рамках отведенного для этого времени, если значение этого интервала
                        //должно отличаться от того, что указано в свойствах/переменных окружения
                        // HIBERNATE_SLEEPING_TIME_UNIT и HIBERNATE_SLEEPING_TIME_VALUE
                        .throwOnNoResult()); //можно указать, что должно быть выброшено исключение, если
                        // записей нет совсем или нет ни одной записи, которая бы соответствовала перечисленным
                        // критериям. Иначе - вернется null или пустая коллекция.);

        // выбор нескольких записей
        List<TestEntity> entities = hibernate().select("Test entity",
                allByQuery(TestEntity.class, "select e from test_entities e where name = 'Some name'"));
    }
}
```

Про свойства / переменные окружения `HIBERNATE_SLEEPING_TIME_UNIT`, `HIBERNATE_SLEEPING_TIME_VALUE`,
`HIBERNATE_SLEEPING_TIME_UNIT` и `HIBERNATE_SLEEPING_TIME_VALUE` можно прочитать [здесь](#Настройки).

Кроме записей целиком, можно сделать выборку их данных. Данный механизм подробно описан
в [выбор записей по ID](#Выбор-нескольких-записей-по-ID)
и для описанного выше способа выборки он работает идентично.

[к оглавлению документа](#Оглавление)

#### QueryDSL

QueryDSL - инструмент для создания унифицированных гибких запросов к различным базам данных, имеющий
интеграции с большинством популярных ORM-библиотек.

[Официальный сайт Query DSL](https://querydsl.com/)

##### QueryDSL. Выбор одной записи.

```java
import com.querydsl.core.types.dsl.BooleanExpression;
import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.hibernate.model.QTestEntity;
import ru.tinkoff.qa.neptune.hibernate.model.TestEntity;

import static java.time.Duration.ofMillis;
import static java.time.Duration.ofSeconds;
import static ru.tinkoff.qa.neptune.hibernate.HibernateContext.hibernate;
import static ru.tinkoff.qa.neptune.hibernate.select.querydsl.QueryDSLSelectStepFactory.byPredicate;

public class MyTest {

    @Test
    public void test() {
        QTestEntity q = QTestEntity.qTestEntity;
        BooleanExpression predicate = q.id.eq(1L) //динамически построенный запрос
                .and(q.name.eq("Test name"))
                .and(q.listData.contains("A"));

        TestEntity entity = hibernate().select("Test entity", //описание того что выбирается
                byPredicate(TestEntity.class, q, predicate)
                        //можно указать один-несколько критериев,
                        .criteria("ListData has more than 1 item", testEntity -> testEntity.getListData().size() > 1)
                        //которым должна соответствовать отобранная запись
                        .timeOut(ofSeconds(5)) //можно указать время, за которое подходящая запись должна быть выбрана,
                        // если оно отличается от того, что указано в свойствах/переменных окружения
                        // HIBERNATE_WAITING_FOR_SELECTION_RESULT_TIME_UNIT и HIBERNATE_WAITING_FOR_SELECTION_RESULT_TIME_VALUE
                        .pollingInterval(ofMillis(200)) //можно указать время интервала между попытками получить
                        // необходимый результат в рамках отведенного для этого времени, если значение этого интервала
                        //должно отличаться от того, что указано в свойствах/переменных окружения
                        // HIBERNATE_SLEEPING_TIME_UNIT и HIBERNATE_SLEEPING_TIME_VALUE
                        .throwOnNoResult()); // можно указать, что должно быть выброшено исключение, если
                        // записи, найденной по запросу, нет совсем или она не соответствует перечисленным 
                        // критериям. Иначе - вернется null.
    }
}
```

Про свойства / переменные окружения `HIBERNATE_SLEEPING_TIME_UNIT`, `HIBERNATE_SLEEPING_TIME_VALUE`,
`HIBERNATE_SLEEPING_TIME_UNIT` и `HIBERNATE_SLEEPING_TIME_VALUE` можно прочитать [здесь](#Настройки).

Кроме записей целиком, можно сделать выборку их данных. Данный механизм подробно описан
в [выбор записей по ID](#Выбор-нескольких-записей-по-ID)
и для описанного выше способа выборки он работает идентично.

[к оглавлению документа](#Оглавление)

##### QueryDSL. Выбор нескольких записей.

```java
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.hibernate.model.QTestEntity;
import ru.tinkoff.qa.neptune.hibernate.model.TestEntity;

import java.util.List;

import static java.time.Duration.ofMillis;
import static java.time.Duration.ofSeconds;
import static ru.tinkoff.qa.neptune.hibernate.HibernateContext.hibernate;
import static ru.tinkoff.qa.neptune.hibernate.select.querydsl.QueryDSLSelectStepFactory.allByPredicate;
import static ru.tinkoff.qa.neptune.hibernate.select.querydsl.QueryDSLSelectStepFactory.allOrdered;
import static ru.tinkoff.qa.neptune.hibernate.select.querydsl.QueryDSLSelectStepFactory.asAPageByPredicate;

public class MyTest {

  @Test
  public void test() {
    QTestEntity q = QTestEntity.qTestEntity;
    BooleanExpression predicate = q.id.eq(1L)
            .and(q.name.eq("Test name"))
            .and(q.listData.contains("A"));

    List<TestEntity> entities = hibernate().select("Test entities",
            allByPredicate(TestEntity.class, q, predicate)
                    .criteria("ListData has more than 1 item", testEntity -> testEntity.getListData().size() > 1)
                    .timeOut(ofSeconds(5))
                    .pollingInterval(ofMillis(200))
                    .throwOnNoResult());
    //с сортировкой
    List<TestEntity> entities2 = hibernate().select("Test entities",
            //есть следующие варианты: 
            // - allOrdered(Class<R> entity, EntityPath<?> entityPath, OrderSpecifier<?> orderSpecifier)
            // - allOrdered(Class<R> entity, EntityPath<?> entityPath, Order order, Expression<C> target, OrderSpecifier.NullHandling handling)
            // - allOrdered(Class<R> entity, EntityPath<?> entityPath, Order order, Expression<C> target)
            allOrdered(TestEntity.class, q, new OrderSpecifier<>(Order.ASC, q.name))
    );

    //с сортировкой по нескольким параметрам
    List<TestEntity> entities3 = hibernate().select("Test entities",
            allOrdered(TestEntity.class, q, new OrderSpecifier<>(Order.ASC, q.name)) //репозиторий и сортировка по свойству записи
                    .orderSpecifier(Order.DESC, q.arrayData)
                    .predicate(predicate) //также можно указать предикат для более специфического запроса
    );

    //с пагинацией
    List<TestEntity> entities4 = hibernate().select("Test entities",
            asAPageByPredicate(TestEntity.class, q, predicate)
                    .limit(10) // по умолчанию 1, значение должно быть >= 1
                    .offset(5) // по умолчанию 0, значение должно быть >= 0
                    .orderSpecifier(Order.DESC, q.name)
                    .orderSpecifier(Order.ASC, q.arrayData)
    );

  }
}
```

Про свойства / переменные окружения `HIBERNATE_SLEEPING_TIME_UNIT`, `HIBERNATE_SLEEPING_TIME_VALUE`,
`HIBERNATE_SLEEPING_TIME_UNIT` и `HIBERNATE_SLEEPING_TIME_VALUE` можно прочитать [здесь](#Настройки).

Кроме записей целиком, можно сделать выборку их данных. Данный механизм подробно описан
в [выбор записей по ID](#Выбор-нескольких-записей-по-ID)
и для описанного выше способа выборки он работает идентично.

[к оглавлению документа](#Оглавление)

#### Наличие / отсутствие записей или их данных

Подробнее о шагах, которые проверяют наличие или отсутствие / исчезновение чего-либо можно
прочитать [тут](./../../../core.api/doc/rus/STEPS.MD#Присутствие-и-отсутствие)

```java
import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.hibernate.model.TestEntity;

import static java.time.Duration.ofSeconds;
import static ru.tinkoff.qa.neptune.hibernate.HibernateContext.hibernate;
import static ru.tinkoff.qa.neptune.hibernate.select.common.CommonSelectStepFactory.byId;

public class MyTest {

    @Test
    public void test() {
        //Определение наличия записи в базе с помощью запроса
        //Если на момент начала исполнения ее еще нет, то предполагается что она появится в течение 5 сек
        //Если запись была или появилась в течение 5 сек - вернется true. Иначе - false.
        var isEntityPresent = hibernate().presenceOf("Test entity",
                //запрос, с помощью которого извлекаются записи
                byId(TestEntity.class, 1L)
                        //опциональные параметры выбираемых записей
                        //
                        .timeOut(ofSeconds(5))); //тайм-аут опционален. Если не указывать, будет использовано значение,
                        // указанное в свойствах/переменных окружения
                        // HIBERNATE_WAITING_FOR_SELECTION_RESULT_TIME_UNIT и HIBERNATE_WAITING_FOR_SELECTION_RESULT_TIME_VALUE

        //Определение наличия записи в базе с помощью запроса
        //Если на момент начала исполнения ее еще нет, то предполагается что она появится в течение 5 сек
        //Если запись была или появилась в течение 5 сек - вернется true. Иначе - выбросится исключение.
        var isEntityPresent2 = hibernate().presenceOfOrThrow("Test entity",
                byId(TestEntity.class, 10L)
                        .timeOut(ofSeconds(5)));

        //Определение наличия специфических данных в базе с помощью запроса
        //Если на момент начала исполнения этих данных еще нет, то предполагается что они появятся в течение 5 сек
        //Если желаемые данные были или появились в течение 5 сек - вернется true. Иначе - false.
        var isRequiredNamePresent = hibernate().presenceOf("Test name",
                byId(TestEntity.class, 10L)
                        .timeOut(ofSeconds(5))
                        .thenGetObject(TestEntity::getName)
                        .criteria("is 'Test'", s -> s.equals("Test")));


        //Определение наличия специфических данных в базе с помощью запроса
        //Если на момент начала исполнения этих данных еще нет, то предполагается что они появятся в течение 5 сек
        //Если желаемые данные были или появились в течение 5 сек - вернется true. Иначе - выбросится исключение.
        var isRequiredNamePresent2 = hibernate().presenceOfOrThrow("Test name",
                byId(TestEntity.class, 10L)
                        .timeOut(ofSeconds(5))
                        .thenGetObject(TestEntity::getName)
                        .criteria("is 'Test'", s -> s.equals("Test")));

        //Аналогично для выборок из нескольких записей / производных или вычисленных с их помощью данных
    }
}
```

```java
import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.hibernate.model.TestEntity;

import static java.time.Duration.ofSeconds;
import static ru.tinkoff.qa.neptune.hibernate.HibernateContext.hibernate;
import static ru.tinkoff.qa.neptune.hibernate.select.common.CommonSelectStepFactory.byId;

public class MyTest {

    @Test
    public void test() {
        //Определение отсутствия записи в базе с помощью запроса
        //Если на момент начала исполнения она еще есть, то предполагается,
        //что она перестанет определяться запросом в течение 10 сек
        //Если записи не было или она перестала определяться в течение 10 сек - вернется true. Иначе - false.
        var isEntityAbsent = hibernate().absenceOf("Test entity",
                //запрос, с помощью которого извлекаются записи
                //тип запроса зависит от того, какие интерфейсы расширяет интерфейс-репозиторий
                byId(TestEntity.class, 1L),
                //.timeOut(ofSeconds(5)) //тайм-аут не нужен, если указан - игнорируется
                ofSeconds(10));

        //Определение отсутствия записи в базе с помощью запроса
        //Если на момент начала исполнения она еще есть, то предполагается,
        //что она перестанет определяться запросом в течение 10 сек
        //Если записи не было или она перестала определяться в течение 10 сек - вернется true.
        // Иначе - выбросится исключение.
        var isEntityAbsent2 = hibernate().absenceOfOrThrow("Test entity",
                byId(TestEntity.class, 1L),
                ofSeconds(10));

        //Определение отсутствия специфических данных в базе с помощью запроса
        //Если на момент начала исполнения эти данные еще есть, то предполагается,
        // что они перестанут определяться в течение 10 сек
        //Если желаемых данных не было или они перестали определяться в течение 10 сек - вернется true. Иначе - false.
        var isRequiredNameAbsent = hibernate().absenceOf("Test name",
                byId(TestEntity.class, 10L)
                        .thenGetObject(TestEntity::getName)
                        .criteria("is 'Test'", s -> s.equals("Test")),
                ofSeconds(10));


        //Определение отсутствия специфических данных в базе с помощью запроса
        //Если на момент начала исполнения эти данные еще есть, то предполагается,
        // что они перестанут определяться в течение 10 сек
        //Если желаемых данных не было или они перестали определяться в течение 10 сек - вернется true.
        // Иначе - выбросится исключение.
        var isRequiredNameAbsent2 = hibernate().absenceOfOrThrow("Test name",
                byId(TestEntity.class, 10L)
                        .thenGetObject(TestEntity::getName)
                        .criteria("is 'Test'", s -> s.equals("Test")),
                ofSeconds(10));

        //Аналогично для выборок из нескольких записей / производных или вычисленных с их помощью данных
    }
}
```

Про свойства / переменные окружения `HIBERNATE_SLEEPING_TIME_UNIT`, `HIBERNATE_SLEEPING_TIME_VALUE`,
`HIBERNATE_SLEEPING_TIME_UNIT` и `HIBERNATE_SLEEPING_TIME_VALUE` можно прочитать [здесь](#Настройки).

[к оглавлению документа](#Оглавление)

### DELETE операции

```java
import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.hibernate.model.TestEntity;

import java.util.List;

import static ru.tinkoff.qa.neptune.hibernate.HibernateContext.hibernate;
import static ru.tinkoff.qa.neptune.hibernate.select.common.CommonSelectStepFactory.byId;
import static ru.tinkoff.qa.neptune.hibernate.select.common.CommonSelectStepFactory.byIds;

public class MyTest {

  @Test
  public void test() {
    // удаление записи, выбранной с помощью запроса
    hibernate().delete("Test entity",
            //тип запроса зависит от того, какие интерфейсы расширяет интерфейс-репозиторий
            byId(TestEntity.class, 1L)
            //опциональные параметры выбираемой для последующего удаления записи
            //
    );

    // удаление записей, выбранных с помощью запроса
    hibernate().delete("Test entities",
            //тип запроса зависит от того, какие интерфейсы расширяет интерфейс-репозиторий
            byIds(TestEntity.class, 1L, 2L)
    );

    TestEntity testEntity; //ранее полученная запись
    TestEntity testEntity2; //ранее полученная запись

    // прямое удаление записи
    hibernate().delete("Test entity",
            TestEntity.class,
            testEntity);

    // прямое удаление нескольких записей
    hibernate().delete("Test entities",
            TestEntity.class,
            testEntity, testEntity2);

    // прямое удаление коллекции записей
    hibernate().delete("Test entities",
            TestEntity.class,
            List.of(testEntity, testEntity2));

    // удаление всех записей в таблице/коллекции
    hibernate().deleteAllFrom(TestEntity.class);
  }
}
```

[к оглавлению документа](#Оглавление)

### INSERT/UPDATE операции

```java
import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.hibernate.model.TestEntity;

import java.util.List;

import static ru.tinkoff.qa.neptune.database.abstractions.UpdateAction.change;
import static ru.tinkoff.qa.neptune.hibernate.HibernateContext.hibernate;
import static ru.tinkoff.qa.neptune.hibernate.select.common.CommonSelectStepFactory.byId;
import static ru.tinkoff.qa.neptune.hibernate.select.common.CommonSelectStepFactory.byIds;

public class MyTest {

    @Test
    public void test() {
        TestEntity testEntity; //ранее полученная или новая запись
        TestEntity testEntity2; //ранее полученная или новая запись

        //прямое сохранение новой/измененной записи
        TestEntity saved = hibernate().saveOne("Test entity", testEntity);

        //прямое сохранение нескольких новых/измененных записей
        Iterable<TestEntity> savedEntities = hibernate().saveAll("Test entities",
                testEntity, testEntity2);

        //прямое сохранение коллекции новых/измененных записей
        Iterable<TestEntity> savedEntities2 = hibernate().saveAll("Test entities",
                List.of(testEntity, testEntity2));

        //прямое сохранение новой/измененной записи
        TestEntity saved2 = hibernate().saveOne("Test entity",
                testEntity,
                change("Set new ID = 10", e -> e.setId(10L)), //с документированием сделанных
                change("Set new name = \"Test Name 1\"", e -> e.setName("Test Name 1")), //изменений
                change("Update ListData", e -> e.setListData(List.of("A1", "B1", "C1", "D1"))),
                change("Update ArrayData", e -> e.setArrayData(new String[]{"A1", "B1", "C1", "D1"})));
        //в случае с прямым сохранением записей необязательно документировать сделанные изменения
        //тоже самое для прямого сохранения нескольких/коллекций записей

        //сохранение измененной записи, полученной с помощью запроса
        TestEntity saved3 = hibernate().saveByQuery("Test entity",
                byId(TestEntity.class, 1L),
                change("Set new ID = 10", e -> e.setId(10L)), //с документированием сделанных
                change("Set new name = \"Test Name 1\"", e -> e.setName("Test Name 1")), //изменений
                change("Update ListData", e -> e.setListData(List.of("A1", "B1", "C1", "D1"))),
                change("Update ArrayData", e -> e.setArrayData(new String[]{"A1", "B1", "C1", "D1"})));
        //в случае с сохранением записей, полученных запросом, обязательно документировать сделанные изменения

        //сохранение измененных записей, полученных с помощью запроса
        //аналогично примеру выше
        Iterable<TestEntity> savedEntities3 = hibernate().saveAllByQuery("Test entities",
                byIds(TestEntity.class, 1L, 2L),
                change("Set new ID = 10", e -> e.setId(10L)),
                change("Set new name = \"Test Name 1\"", e -> e.setName("Test Name 1")),
                change("Update ListData", e -> e.setListData(List.of("A1", "B1", "C1", "D1"))),
                change("Update ArrayData", e -> e.setArrayData(new String[]{"A1", "B1", "C1", "D1"})));
    }
}
```

[к оглавлению документа](#Оглавление)
