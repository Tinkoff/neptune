# Spring data. Find-операции

Важно:

- [Шаги, возвращающие объекты](../../../quick_start/steps/pattern_steps/get_step/index.md)

В описанных примерах все операции описаны с использованием интерфейса-репозитория

```java
package org.mypack;

import org.springframework.data.repository.Repository;

public interface TestRepository extends Repository<TestEntity, Long> {

  TestEntity findSomething(boolean p1, String p2, int p3);

  Iterable<TestEntity> findEntities(boolean p1, String p2, int p3);
}
```

который может расширять один или несколько из приведенных ниже интерфейсов:

- [CrudRepository](https://docs.spring.io/spring-data/commons/docs/current/api/org/springframework/data/repository/CrudRepository.html)
- [ReactiveCrudRepository](https://docs.spring.io/spring-data/commons/docs/current/api/org/springframework/data/repository/reactive/ReactiveCrudRepository.html)
- [RxJava3CrudRepository](https://docs.spring.io/spring-data/commons/docs/current/api/org/springframework/data/repository/reactive/RxJava3CrudRepository.html)
- [PagingAndSortingRepository](https://docs.spring.io/spring-data/commons/docs/current/api/org/springframework/data/repository/PagingAndSortingRepository.html)
- [ReactiveSortingRepository](https://docs.spring.io/spring-data/commons/docs/current/api/org/springframework/data/repository/reactive/ReactiveSortingRepository.html)
- [RxJava3SortingRepository](https://docs.spring.io/spring-data/commons/docs/current/api/org/springframework/data/repository/reactive/RxJava3SortingRepository.html)
- [QueryByExampleExecutor](https://docs.spring.io/spring-data/commons/docs/current/api/org/springframework/data/repository/query/QueryByExampleExecutor.html)
- [ReactiveQueryByExampleExecutor](https://docs.spring.io/spring-data/commons/docs/current/api/org/springframework/data/repository/query/ReactiveQueryByExampleExecutor.html)
- [QuerydslPredicateExecutor](https://docs.spring.io/spring-data/commons/docs/current/api/org/springframework/data/querydsl/QuerydslPredicateExecutor.html)
- [ReactiveQuerydslPredicateExecutor](https://docs.spring.io/spring-data/commons/docs/current/api/org/springframework/data/querydsl/ReactiveQuerydslPredicateExecutor.html)

Ниже пример модели объекта из базы данных

```java
package org.mypack;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static java.util.Arrays.copyOf;

public class TestEntity {

  private Long id;

  private String name;

  private List<String> listData;

  private String[] arrayData;

  public Long getId() {
    return id;
  }

  public TestEntity setId(Long id) {
    this.id = id;
    return this;
  }

  public String getName() {
    return name;
  }

  public TestEntity setName(String name) {
    this.name = name;
    return this;
  }

  public List<String> getListData() {
    return listData;
  }

  public TestEntity setListData(List<String> listData) {
    this.listData = listData;
    return this;
  }

  public String[] getArrayData() {
    return arrayData;
  }

  public TestEntity setArrayData(String[] arrayData) {
    this.arrayData = arrayData;
    return this;
  }
}
```

```{toctree}
:hidden:

all.md
Id.md
sorting.md
pageable.md
quryDSL.md
example.md
method_invocation.md
```