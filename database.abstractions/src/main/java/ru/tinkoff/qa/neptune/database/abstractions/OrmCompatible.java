package ru.tinkoff.qa.neptune.database.abstractions;

import java.util.Collection;
import java.util.List;

public interface OrmCompatible {

    default <R> R update(R toUpdate, UpdateAction<R>... actions) {
        return update(List.of(toUpdate), actions).get(0);
    }

    <R, Q extends Collection<R>> Q update(Q toUpdate, UpdateAction<R>... actions);

    default <R> R delete(R toDelete) {
        return delete(List.of(toDelete)).get(0);
    }

    <R, Q extends Collection<R>> Q delete(Q toDelete);

    default <R> R insert(R toInsert) {
        return insert(List.of(toInsert)).get(0);
    }

    <R, Q extends Collection<R>> Q insert(Q toInsert);
}
