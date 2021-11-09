package ru.tinkoff.qa.neptune.spring.data.model;

import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveSortingRepository;

public interface TestReactiveRepository extends ReactiveSortingRepository<TestEntity, Long>, ReactiveQueryByExampleExecutor<TestEntity> {
}
