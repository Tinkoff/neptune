# Записи с сортировкой

Может быть выполнено, если интерфейс-репозиторий расширяет один или несколько из перечисленных:

- [PagingAndSortingRepository](https://docs.spring.io/spring-data/commons/docs/current/api/org/springframework/data/repository/PagingAndSortingRepository.html)
- [ReactiveSortingRepository](https://docs.spring.io/spring-data/commons/docs/current/api/org/springframework/data/repository/reactive/ReactiveSortingRepository.html)
- [RxJava2SortingRepository](https://docs.spring.io/spring-data/commons/docs/current/api/org/springframework/data/repository/reactive/RxJava2SortingRepository.html)
- [RxJava3SortingRepository](https://docs.spring.io/spring-data/commons/docs/current/api/org/springframework/data/repository/reactive/RxJava3SortingRepository.html)

```java
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.springframework.data.domain.Sort.Direction.ASC;
import static ru.tinkoff.qa.neptune.spring.data.SpringDataContext.springData;
import static ru.tinkoff.qa.neptune.spring.data.select.common.CommonSelectStepFactory.allBySorting;

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
            //есть следующие варианты: 
            // - allBySorting(T, org.springframework.data.domain.Sort)
            // - allBySorting(T, java.lang.String...)
            // - allBySorting(T, java.util.List<org.springframework.data.domain.Sort.Order>)
            // - allBySorting(T, org.springframework.data.domain.Sort.Order...)
            // где T - тип расширяющий org.springframework.data.repository.Repository
            allBySorting(testRepository, ASC, "id", "name")
                //
                //Необходимые параметры
                //
        ); 
    }
}
```

Возможности данной операции аналогичны возможностям [операции выбора всех записей](all.md).