package ru.tinkoff.qa.neptune.spring.data.model;


import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;

public interface TestRepository extends PagingAndSortingRepository<TestEntity, Long>, QueryByExampleExecutor<TestEntity> {

    TestEntity findSomething(Object... params);
}
