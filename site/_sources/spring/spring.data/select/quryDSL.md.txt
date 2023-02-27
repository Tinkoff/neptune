# Spring data. Query DSL

Может быть выполнено, если интерфейс-репозиторий расширяет один или несколько из перечисленных:

- [QuerydslPredicateExecutor](https://docs.spring.io/spring-data/commons/docs/current/api/org/springframework/data/querydsl/QuerydslPredicateExecutor.html)
- [ReactiveQuerydslPredicateExecutor](https://docs.spring.io/spring-data/commons/docs/current/api/org/springframework/data/querydsl/ReactiveQuerydslPredicateExecutor.html)

[Query DSL](https://querydsl.com/)

Возможности данной операции аналогичны возможностям [операции выбора по id](Id.md).

## Query DSL. Выбор записи.

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

        TestEntity entity = springData().find(
            //описание того ЧТО выбирается,
            //в свободной форме или бизнес
            //терминологии
            "Test entity",
            byPredicate(testRepository, predicate)
            //
            //Необходимые параметры
            //
        );
    }
}
```

## Query DSL. Выбор записей.

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

        List<TestEntity> entities = springData().find(
            //описание того ЧТО выбирается,
            //в свободной форме или бизнес
            //терминологии
            "Test entities",
            allByPredicate(testRepository, predicate)
            //
            //Необходимые параметры
            //
        );

        //с сортировкой
        List<TestEntity> entities2 = springData().find(
            "Test entities",
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
            //
            //Необходимые параметры
            //
        );

        //с сортировкой, 
        // использование com.querydsl.core.types.OrderSpecifier
        List<TestEntity> entities3 = springData().find(
            "Test entities",
            //репозиторий и сортировка по свойству записи
            allOrdered(testRepository, Order.ASC, q.id)
                //дополнительные варианты
                .orderSpecifier(Order.DESC, q.name)
                //сортировки
                .orderSpecifier(Order.ASC, q.arrayData)
                //опционально, запрос, выпирающий записи, если не указывать - будут
                // выбраны все записи с сортировкой
                .predicate(predicate)
            //
            //Необходимые параметры
            //
        );

        List<TestEntity> entities4 = springData().find(
            "Test entities",
            asAPageByPredicate(testRepository, predicate)
                //опциональный параметр, значение которого должно быть > 0
                .number(0)
                //опциональный параметр, значение которого должно быть > 1
                .size(5)
                //опциональные варианты сортировки
                .orderSpecifier(Order.DESC, q.name) 
                .orderSpecifier(Order.ASC, q.arrayData)
            //опциональные параметры выбираемых записей
            //
        );
    }
}
```