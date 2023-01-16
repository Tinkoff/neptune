# Произвольный вызов метода репозитория

Возможности данной операции аналогичны возможностям [операции выбора по id](Id.md).

```java
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static ru.tinkoff.qa.neptune.spring.data.SpringDataContext.springData;
import static ru.tinkoff.qa.neptune.spring.data.select.common.CommonSelectStepFactory.allByInvocation;
import static ru.tinkoff.qa.neptune.spring.data.select.common.CommonSelectStepFactory.byInvocation;

@SpringBootTest
public class MyTest {

    @Autowired
    private TestRepository testRepository;

    @Test
    public void myTest() {
        TestEntity entity = springData().find(
            //описание того ЧТО выбирается,
            //в свободной форме или бизнес
            //терминологии
            "Test entity",
            byInvocation(testRepository,
                r -> r.findSomething(true, "ABCD", 123))
            //
            //Необходимые параметры
            //
        );

        List<TestEntity> entities = springData().find(
            "Test entities",
            allByInvocation(testRepository,
                r -> r.findEntities(true, "ABCD", 123))
            //
            //Необходимые параметры
            //
        );
    }
}
```