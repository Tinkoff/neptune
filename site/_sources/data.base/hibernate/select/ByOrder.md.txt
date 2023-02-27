# Hibernate. Записи с сортировкой

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

        List<TestEntity> entities = hibernate().select(
            //описание того ЧТО выбирается,
            //в свободной форме или бизнес
            //терминологии
            "Test entities",
            //есть следующие варианты: 
            // - allByOrder(Class<R> entity, javax.persistence.criteria.Order... orders)
            // - allByOrder(Class<R> entity, java.util.List<javax.persistence.criteria.Order> orders)
            allByOrder(TestEntity.class, cb.desc(root.get("name")))
            //
            //Необходимые параметры
            //
        );
    }
}
```

Возможности данной операции аналогичны возможностям [операции выбора всех записей](all.md).