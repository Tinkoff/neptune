# Hibernate. Query DSL

[Query DSL](https://querydsl.com/)

Возможности данной операции аналогичны возможностям [операции выбора по id](Id.md).

## QueryDSL. Выбор записи.

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

        TestEntity entity = hibernate().select(
            //описание того ЧТО выбирается,
            //в свободной форме или бизнес
            //терминологии
            "Test entity",
            byPredicate(TestEntity.class, q, predicate)
            //
            //Необходимые параметры
            //
        );
    }
}
```

## QueryDSL. Выбор записей.

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

    List<TestEntity> entities = hibernate().select(
        //описание того ЧТО выбирается,
        //в свободной форме или бизнес
        //терминологии
        "Test entities",
        allByPredicate(TestEntity.class, q, predicate)
        //
        //Необходимые параметры
        //
    );
    
    //с сортировкой
    List<TestEntity> entities2 = hibernate().select(
        "Test entities",
        //есть следующие варианты: 
        // - allOrdered(Class<R> entity, EntityPath<?> entityPath, OrderSpecifier<?> orderSpecifier)
        // - allOrdered(Class<R> entity, EntityPath<?> entityPath, Order order, Expression<C> target, OrderSpecifier.NullHandling handling)
        // - allOrdered(Class<R> entity, EntityPath<?> entityPath, Order order, Expression<C> target)
        allOrdered(TestEntity.class, q, new OrderSpecifier<>(Order.ASC, q.name))
    );

    //с сортировкой по нескольким параметрам
    List<TestEntity> entities3 = hibernate().select(
        "Test entities",
        allOrdered(TestEntity.class, q, new OrderSpecifier<>(Order.ASC, q.name))
            .orderSpecifier(Order.DESC, q.arrayData)
            .predicate(predicate) //также можно указать предикат для более специфического запроса
    );

    //с пагинацией
    List<TestEntity> entities4 = hibernate().select(
        "Test entities",
        asAPageByPredicate(TestEntity.class, q, predicate)
            .limit(10) // по умолчанию 1, значение должно быть >= 1
            .offset(5) // по умолчанию 0, значение должно быть >= 0
            .orderSpecifier(Order.DESC, q.name)
            .orderSpecifier(Order.ASC, q.arrayData)
    );
  }
}
```