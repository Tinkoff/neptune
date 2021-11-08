package ru.tinkoff.qa.neptune.spring.data.model;

import org.springframework.data.querydsl.ReactiveQuerydslPredicateExecutor;
import org.springframework.data.repository.Repository;

public interface TestReactiveQuerydslPredicateExecutorRepository
        extends Repository<TestEntity, Long>,
        ReactiveQuerydslPredicateExecutor<TestEntity> {
}
