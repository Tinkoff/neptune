package ru.tinkoff.qa.neptune.data.base.api.queries;

import org.datanucleus.api.jdo.JDOPersistenceManager;

import java.util.List;

/**
 * Describes objects that execute queries to a datastore
 *
 * @param <T> is a type of resulted objects
 * @param <S> is a type of resulted list
 */
@Deprecated(forRemoval = true)
public interface Query<T, S extends List<T>> {

    S execute(JDOPersistenceManager jdoPersistenceManager);
}
