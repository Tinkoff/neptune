# Example

Может быть выполнено, если интерфейс-репозиторий расширяет один или несколько из перечисленных:

- [QueryByExampleExecutor](https://docs.spring.io/spring-data/commons/docs/current/api/org/springframework/data/repository/query/QueryByExampleExecutor.html)
- [ReactiveQueryByExampleExecutor](https://docs.spring.io/spring-data/commons/docs/current/api/org/springframework/data/repository/query/ReactiveQueryByExampleExecutor.html)

Параметры данной операции аналогичны параметрам [операции выбора по id](Id.md).

## Example. Выбор записи.

```java
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.springframework.data.domain.ExampleMatcher.matchingAny;
import static ru.tinkoff.qa.neptune.spring.data.SpringDataContext.springData;
import static ru.tinkoff.qa.neptune.spring.data.select.common.CommonSelectStepFactory.byExample;

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
            byExample(testRepository,
                new TestEntity().setId(10L).setName("Something"))
                //опционально,
                .matcher(m -> m.withMatcher("id", exact()))
                //параметры проверки свойств
                .matcher(m -> m.withMatcher("name", ignoreCase()))
                .initialMatcher(matchingAny())
            //
            //Необходимые параметры
            //
        );
    }
}
```

## Example. Выбор записей.

```java
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.springframework.data.domain.ExampleMatcher.matchingAny;
import static org.springframework.data.domain.Sort.Direction.ASC;
import static ru.tinkoff.qa.neptune.spring.data.SpringDataContext.springData;
import static ru.tinkoff.qa.neptune.spring.data.select.common.CommonSelectStepFactory.allByExample;

@SpringBootTest
public class MyTest {

    @Autowired
    private TestRepository testRepository;

    @Test
    public void myTest() {
        List<TestEntity> entities = springData().find(
            //описание того ЧТО выбирается,
            //в свободной форме или бизнес
            //терминологии
            "Test entities",
            allByExample(testRepository,
                new TestEntity().setId(10L).setName("Something"))
                //опционально,
                .matcher(m -> m.withMatcher("id", exact()))
                //параметры проверки свойств
                .matcher(m -> m.withMatcher("name", ignoreCase()))
                //опционально, вариант сортировки
                .sorting(ASC, "id", "name")
                //опционально, исходный объект ExampleMatcher,
                // от которого выстраивается критерий отбора
                .initialMatcher(matchingAny())
            //
            //Необходимые параметры
            //
        );
    }
}
```