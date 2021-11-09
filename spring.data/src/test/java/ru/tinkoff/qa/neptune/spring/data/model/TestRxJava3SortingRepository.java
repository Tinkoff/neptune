package ru.tinkoff.qa.neptune.spring.data.model;

import org.springframework.data.repository.reactive.RxJava3SortingRepository;

public interface TestRxJava3SortingRepository extends RxJava3SortingRepository<TestEntity, Long> {
}
