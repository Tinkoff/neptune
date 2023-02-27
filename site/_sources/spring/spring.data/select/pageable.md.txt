# Spring data. Pageable

Может быть выполнено, если интерфейс-репозиторий расширяет один или несколько из перечисленных:

- [PagingAndSortingRepository](https://docs.spring.io/spring-data/commons/docs/current/api/org/springframework/data/repository/PagingAndSortingRepository.html)

```java
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.springframework.data.domain.Sort.by;
import static ru.tinkoff.qa.neptune.spring.data.SpringDataContext.springData;
import static ru.tinkoff.qa.neptune.spring.data.select.common.CommonSelectStepFactory.asAPage;

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
            asAPage(testRepository)
                //опциональный параметр, значение которого 
                // должно быть > 0
                .number(0)
                //опциональный параметр, значение 
                // которого должно быть > 1
                .size(5)
                //опциональный параметр, вариант сортировки
                .sort(by("id").descending().and(by("name"))) 
            //
            //Необходимые параметры
            //
        );
    }
}
```

Возможности данной операции аналогичны возможностям [операции выбора всех записей](all.md).

