package ru.tinkoff.qa.neptune.spring.data;

import org.springframework.data.repository.Repository;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.database.abstractions.AbstractDatabaseStepContext;
import ru.tinkoff.qa.neptune.database.abstractions.InsertQuery;
import ru.tinkoff.qa.neptune.database.abstractions.SelectQuery;
import ru.tinkoff.qa.neptune.database.abstractions.UpdateAction;
import ru.tinkoff.qa.neptune.spring.data.select.*;

import java.util.List;

@SuppressWarnings("unchecked")
public class SpringDataContext extends AbstractDatabaseStepContext<SpringDataContext> {

    private static final SpringDataContext context = getInstance(SpringDataContext.class);

    public static SpringDataContext springData() {
        return context;
    }

    @Override
    public void stop() {
    }

    @Override
    protected final <R, Q extends SequentialGetStepSupplier<SpringDataContext, R, ?, ?, ?> & SelectQuery<R>> R select(Q query) {
        return get(query);
    }

    public <R, ID, T extends Repository<R, ID>> R select(String description,
                                                         T from,
                                                         SelectOneStepSupplier<R, ID, T> by) {
        var impl = (SelectOneStepSupplier.SelectOneStepSupplierImpl<R, ID, T>) by;
        ((SelectOneStepSupplier.SelectOneStepSupplierImpl<R, ID, T>) impl.setDescription(description)).from(from);
        return select(by);
    }

    public <R, ID, T extends Repository<R, ID>> Iterable<R> select(String description,
                                                                   T from,
                                                                   SelectManyStepSupplier<R, ID, T> by) {
        var impl = (SelectManyStepSupplier.SelectManyStepSupplierImpl<R, ID, T>) by;
        ((SelectManyStepSupplier.SelectManyStepSupplierImpl<R, ID, T>) impl.setDescription(description)).from(from);
        return select(by);
    }

    public <S, R, ID, T extends Repository<R, ID>> S select(String description,
                                                            T from,
                                                            GetObjectFromEntity<S, R, ?> toGet) {
        var impl = (GetObjectFromEntity.GetObjectFromEntityImpl<S, R>) toGet;
        return select(impl.setDescription(description).setRepository(from));
    }

    public <ITEM, S extends Iterable<ITEM>, R, ID, T extends Repository<R, ID>> S select(String description,
                                                                                         T from,
                                                                                         GetIterableFromEntity<ITEM, S, R, ?> toGet) {
        var impl = (GetIterableFromEntity.GetIterableFromEntityImpl<ITEM, S, R>) toGet;
        return select(impl.setDescription(description).setRepository(from));
    }

    public <ITEM, R, ID, T extends Repository<R, ID>> ITEM[] select(String description,
                                                                    T from,
                                                                    GetArrayFromEntity<ITEM, R, ?> toGet) {
        var impl = (GetArrayFromEntity.GetArrayFromEntityImpl<ITEM, R>) toGet;
        return select(impl.setDescription(description).setRepository(from));
    }

    public <ITEM, R, ID, T extends Repository<R, ID>> ITEM select(String description,
                                                                  T from,
                                                                  GetItemOfIterableFromEntity<ITEM, ? extends Iterable<ITEM>, R, ?> toGet) {
        var impl = (GetItemOfIterableFromEntity.GetItemOfIterableFromEntityImpl<ITEM, ? extends Iterable<ITEM>, R>) toGet;
        return select(impl.setDescription(description).setRepository(from));
    }

    public <ITEM, R, ID, T extends Repository<R, ID>> ITEM select(String description,
                                                                  T from,
                                                                  GetItemOfArrayFromEntity<ITEM, R, ?> toGet) {
        var impl = (GetItemOfArrayFromEntity.GetItemOfArrayFromEntityImpl<ITEM, R>) toGet;
        return select(impl.setDescription(description).setRepository(from));
    }

    public <ITEM, R, ID, T extends Repository<R, ID>> List<ITEM> select(String description,
                                                                        T from,
                                                                        GetIterableFromEntities<ITEM, R, ?> toGet) {
        var impl = (GetIterableFromEntities.GetIterableFromEntitiesImpl<ITEM, R>) toGet;
        return select(impl.setDescription(description).setRepository(from));
    }

    public <ITEM, R, ID, T extends Repository<R, ID>> ITEM select(String description,
                                                                  T from,
                                                                  GetIterableItemFromEntities<ITEM, R, ?> toGet) {
        var impl = (GetIterableItemFromEntities.GetIterableItemFromEntitiesImpl<ITEM, R>) toGet;
        return select(impl.setDescription(description).setRepository(from));
    }

    @Override
    protected <R, Q extends SequentialGetStepSupplier<SpringDataContext, R, ?, ?, ?> & SelectQuery<R>> R update(Q query, UpdateAction<R>... actions) {
        return null;
    }

    @Override
    protected <R, Q extends SequentialGetStepSupplier<SpringDataContext, R, ?, ?, ?> & SelectQuery<R>> R delete(Q query) {
        return get(query);
    }

    @Override
    protected <R, Q extends SequentialGetStepSupplier<SpringDataContext, R, ?, ?, ?> & InsertQuery<R>> R insert(Q query) {
        return null;
    }
}
