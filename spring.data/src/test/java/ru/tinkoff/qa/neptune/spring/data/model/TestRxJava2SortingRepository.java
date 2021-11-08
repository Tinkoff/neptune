package ru.tinkoff.qa.neptune.spring.data.model;

import org.springframework.data.repository.reactive.RxJava2SortingRepository;

public interface TestRxJava2SortingRepository extends RxJava2SortingRepository<TestEntity, Long> {
}
