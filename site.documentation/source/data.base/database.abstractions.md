# database.abstractions

Модуль предоставляет абстракции для работы с базами данных в рамках _Neptune_.
Абстракции поддерживают как ORM, так и Repository паттерны и могут
быть использованы для реализации API для работы с конкретной библиотекой.

```{eval-rst}
.. include:: data_dase_dependencies.rst
```

[API](https://tinkoff.github.io/neptune/database.abstractions/index.html)

## Абстрактный [контекст](./../core/steps/context/index.md)

Класс AbstractDatabaseStepContext содержит абстрактные базовые методы для осуществления
CRUD-запросов в СУБД.

```java
public class HibernateDatabaseContext extends AbstractDatabaseStepContext<HibernateDatabaseContext> {

    @Override
    public void stop() {
        // реализация метода остановки подключения к базе 
    }

    @Override
    protected <R, Q extends SequentialGetStepSupplier<HibernateDatabaseContext, R, ?, ?, ?> & InsertQuery<R>> R insert(Q query) {
        // реализация Create-запроса
    }

    @Override
    protected <R, Q extends SequentialGetStepSupplier<HibernateDatabaseContext, R, ?, ?, ?> & SelectQuery<R>> R select(Q query) {
        // реализация Read-запроса
    }

    @Override
    protected <R, S, Q extends SequentialGetStepSupplier<HibernateDatabaseContext, S, ?, ?, ?> & SelectQuery<S>> S update(Q query, UpdateAction<R>... actions) {
        // реализация Update-запроса
    }

    @Override
    protected <R, Q extends SequentialGetStepSupplier<HibernateDatabaseContext, R, ?, ?, ?> & SelectQuery<R>> R delete(Q query) {
        // реализация Delete-запроса
    }
}
```

## Интерфейс OrmCompatible

Интерфейс OrmCompatible содержит дополнительные методы для работы с JDO или JPA-объектами.

```java
public class HibernateDatabaseContext extends AbstractDatabaseStepContext<HibernateDatabaseContext> implements OrmCompatible {

    // реализации методов AbstractDatabaseStepContext

    @Override
    public <R> R update(R toUpdate, UpdateAction<R>... actions) {
        // реализация Update-запроса
    }

    @Override
    public <R, Q extends Collection<R>> Q update(Q toUpdate, UpdateAction<R>... actions) {
        // реализация Update-запроса
    }

    @Override
    public <R> R delete(R toDelete) {
        // реализация Delete-запроса
    }

    @Override
    public <R, Q extends Collection<R>> Q delete(Q toDelete) {
        // реализация Delete-запроса
    }

    @Override
    public <R> R insert(R toInsert) {
        // реализация Create-запроса
    }

    @Override
    public <R, Q extends Collection<R>> Q insert(Q toInsert) {
        // реализация Create-запроса
    }
}
```