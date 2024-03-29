# Spring Data

Данный модуль объединяет возможности _Neptune_ и [Spring Data](https://spring.io/projects/spring-data).
Является расширением [абстракций для работы с базами данных](./../../data.base/database.abstractions.md).

```{eval-rst}
.. include:: spring_data_dependencies.rst
```

[API](https://tinkoff.github.io/neptune/spring.data/index.html)

Поддерживается работа с
любыми [репозиториями](https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#repositories), которые
являются расширениями:

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

```{toctree}
:hidden:

settings.md
select/index.md
save/save.md
delete/delete.md
```