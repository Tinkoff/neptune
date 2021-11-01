package ru.tinkoff.qa.neptune.spring.data.model;

import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.Repository;

public interface TestQuerydslPredicateExecutorRepository extends
        Repository<TestEntity, Long>,
        QuerydslPredicateExecutor<TestEntity> {
}
