# Hibernate. Delete-операции

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
            byId(TestEntity.class, 1L)
            //опциональные параметры выбираемой для последующего удаления записи
            //
        );

        // удаление записей, выбранных с помощью запроса
        hibernate().delete("Test entities",
            byIds(TestEntity.class, 1L, 2L)
            //опциональные параметры
        );

        TestEntity testEntity; //ранее полученная запись
        TestEntity testEntity2; //ранее полученная запись

        // прямое удаление записи
        hibernate().delete("Test entity",
            testEntity);

        // прямое удаление нескольких записей
        hibernate().delete("Test entities",
            testEntity, testEntity2);

        // прямое удаление коллекции записей
        hibernate().delete("Test entities",
            List.of(testEntity, testEntity2));
    }
}
```