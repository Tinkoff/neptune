# Hibernate. JPA Criteria

Возможности данной операции аналогичны возможностям [операции выбора по id](Id.md).

## JPA Criteria. Запись.

```java
import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.hibernate.model.TestEntity;

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
    TestEntity entity = hibernate().select(
        //описание того ЧТО выбирается,
        //в свободной форме или бизнес
        //терминологии
        "Test entity",
        byCriteria(TestEntity.class, cq.where(cb.ge(root.get("id"), 1L)))
        //
        //Необходимые параметры
        //
    );
  }
}
```

## JPA Criteria. Записи.

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
      

    // выбор нескольких записей
    List<TestEntity> entities = hibernate().select(
        //описание того ЧТО выбирается,
        //в свободной форме или бизнес
        //терминологии
        "Test entity",
        allByCriteria(TestEntity.class, cq.where(cb.like(root.get("name"), "name")))
        //
        //Необходимые параметры
        //
    );
  }
}
```