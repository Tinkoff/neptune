# HQL/SQL/JPQL запрос

HQL - язык запросов Hibernate, синтаксически похожий на SQL.

[Документация по HQL](https://docs.jboss.org/hibernate/orm/3.5/reference/en/html/queryhql.html)

Возможности данной операции аналогичны возможностям [операции выбора по id](Id.md).

## HQL/SQL/JPQL. Запись.

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
        TestEntity entity = hibernate().select(
            //описание того ЧТО выбирается,
            //в свободной форме или бизнес
            //терминологии
            "Test entity",
            byQuery(TestEntity.class, "select e from test_entities e where id = 1")
            //
            //Необходимые параметры
            //
        );
    }
}
```


## HQL/SQL/JPQL. Записи.

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
        // выбор нескольких записей
        List<TestEntity> entities = hibernate().select(
            //описание того ЧТО выбирается,
            //в свободной форме или бизнес
            //терминологии
            "Test entity",
            allByQuery(TestEntity.class, "select e from test_entities e where name = 'Some name'")
            //
            //Необходимые параметры
            //
        );
    }
}
```