# Hibernate. Pageable

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
        List<TestEntity> entities = hibernate().select(
            //описание того ЧТО выбирается,
            //в свободной форме или бизнес
            //терминологии
            "Test entities",
            asAPage(TestEntity.class)
                .limit(10) // по умолчанию 1, значение должно быть >= 1
                .offset(5) // по умолчанию 0, значение должно быть >= 0
            //
            //Необходимые параметры
            //
        );
    }
}
```

Возможности данной операции аналогичны возможностям [операции выбора всех записей](all.md).