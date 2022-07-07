package ru.tinkoff.qa.neptune.spring.data.model;


import org.springframework.data.repository.CrudRepository;

public interface SelectAllRepository extends CrudRepository<TestEntity, Long> {
}
