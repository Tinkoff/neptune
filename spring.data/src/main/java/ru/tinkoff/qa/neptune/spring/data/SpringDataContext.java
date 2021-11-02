package ru.tinkoff.qa.neptune.spring.data;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.data.repository.reactive.RxJava2CrudRepository;
import org.springframework.data.repository.reactive.RxJava3CrudRepository;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.database.abstractions.AbstractDatabaseStepContext;
import ru.tinkoff.qa.neptune.database.abstractions.InsertQuery;
import ru.tinkoff.qa.neptune.database.abstractions.SelectQuery;
import ru.tinkoff.qa.neptune.database.abstractions.UpdateAction;
import ru.tinkoff.qa.neptune.spring.data.delete.DeleteAllFromStepSupplier;
import ru.tinkoff.qa.neptune.spring.data.delete.DeleteByIdsStepSupplier;
import ru.tinkoff.qa.neptune.spring.data.delete.DeleteByQueryStepSupplier;
import ru.tinkoff.qa.neptune.spring.data.save.SaveStepSupplier;
import ru.tinkoff.qa.neptune.spring.data.select.*;

import java.time.Duration;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.Arrays.asList;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static ru.tinkoff.qa.neptune.core.api.localization.StepLocalization.translate;

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
                                                         SelectOneStepSupplier<R, ID, T> by) {
        checkArgument(isNotBlank(description), "Description should be defined");
        ((SetsDescription) by).changeDescription(translate(description));
        return select(by);
    }

    public <R, ID, T extends Repository<R, ID>> Iterable<R> select(String description,
                                                                   SelectManyStepSupplier<R, ID, T> by) {
        checkArgument(isNotBlank(description), "Description should be defined");
        ((SetsDescription) by).changeDescription(translate(description));
        return select(by);
    }

    public <S, R> S select(String description,
                           GetObjectFromEntity<S, R, ?> toGet) {
        checkArgument(isNotBlank(description), "Description should be defined");
        var impl = (GetObjectFromEntity.GetObjectFromEntityImpl<S, R>) toGet;
        return select(impl.setDescription(translate(description)));
    }

    public <ITEM, S extends Iterable<ITEM>, R> S select(String description,
                                                        GetIterableFromEntity<ITEM, S, R, ?> toGet) {
        checkArgument(isNotBlank(description), "Description should be defined");
        var impl = (GetIterableFromEntity.GetIterableFromEntityImpl<ITEM, S, R>) toGet;
        return select(impl.setDescription(translate(description)));
    }

    public <ITEM, R> ITEM[] select(String description,
                                   GetArrayFromEntity<ITEM, R, ?> toGet) {
        checkArgument(isNotBlank(description), "Description should be defined");
        var impl = (GetArrayFromEntity.GetArrayFromEntityImpl<ITEM, R>) toGet;
        return select(impl.setDescription(translate(description)));
    }

    public <ITEM, R> ITEM select(String description,
                                 GetItemOfIterableFromEntity<ITEM, ? extends Iterable<ITEM>, R, ?> toGet) {
        checkArgument(isNotBlank(description), "Description should be defined");
        var impl = (GetItemOfIterableFromEntity.GetItemOfIterableFromEntityImpl<ITEM, ? extends Iterable<ITEM>, R>) toGet;
        return select(impl.setDescription(translate(description)));
    }

    public <ITEM, R> ITEM select(String description,
                                 GetItemOfArrayFromEntity<ITEM, R, ?> toGet) {
        checkArgument(isNotBlank(description), "Description should be defined");
        var impl = (GetItemOfArrayFromEntity.GetItemOfArrayFromEntityImpl<ITEM, R>) toGet;
        return select(impl.setDescription(translate(description)));
    }

    public <ITEM, R> List<ITEM> select(String description,
                                       GetIterableFromEntities<ITEM, R, ?> toGet) {
        checkArgument(isNotBlank(description), "Description should be defined");
        var impl = (GetIterableFromEntities.GetIterableFromEntitiesImpl<ITEM, R>) toGet;
        return select(impl.setDescription(translate(description)));
    }

    public <ITEM, R> ITEM select(String description,
                                 GetIterableItemFromEntities<ITEM, R, ?> toGet) {
        checkArgument(isNotBlank(description), "Description should be defined");
        var impl = (GetIterableItemFromEntities.GetIterableItemFromEntitiesImpl<ITEM, R>) toGet;
        return select(impl.setDescription(translate(description)));
    }

    @Override
    protected <R, Q extends SequentialGetStepSupplier<SpringDataContext, R, ?, ?, ?> & SelectQuery<R>> R delete(Q query) {
        return get(query);
    }

    public <R, ID, T extends Repository<R, ID>> SpringDataContext delete(String description, SelectOneStepSupplier<R, ID, T> select) {
        delete(DeleteByQueryStepSupplier.delete(description, select));
        return this;
    }

    public <R, ID, T extends Repository<R, ID>> SpringDataContext delete(String description, SelectManyStepSupplier<R, ID, T> select) {
        delete(DeleteByQueryStepSupplier.delete(description, select));
        return this;
    }

    private <R, ID, T extends Repository<R, ID>> SpringDataContext delete(String description, T repository, R... toDelete) {
        checkNotNull(toDelete);
        if (toDelete.length == 1) {
            delete(DeleteByQueryStepSupplier.delete(description, repository, toDelete[0]));
        } else {
            delete(DeleteByQueryStepSupplier.delete(description, repository, asList(toDelete)));
        }
        return this;
    }

    public <R, ID, T extends CrudRepository<R, ID>> SpringDataContext delete(String description, T repository, R... toDelete) {
        return delete(description, (Repository<R, ID>) repository, toDelete);
    }

    public <R, ID, T extends ReactiveCrudRepository<R, ID>> SpringDataContext delete(String description, T repository, R... toDelete) {
        return delete(description, (Repository<R, ID>) repository, toDelete);
    }

    public <R, ID, T extends RxJava2CrudRepository<R, ID>> SpringDataContext delete(String description, T repository, R... toDelete) {
        return delete(description, (Repository<R, ID>) repository, toDelete);
    }

    public <R, ID, T extends RxJava3CrudRepository<R, ID>> SpringDataContext delete(String description, T repository, R... toDelete) {
        return delete(description, (Repository<R, ID>) repository, toDelete);
    }

    private <R, ID, T extends Repository<R, ID>> SpringDataContext delete(String description, T repository, Iterable<R> toDelete) {
        delete(DeleteByQueryStepSupplier.delete(description, repository, toDelete));
        return this;
    }

    public <R, ID, T extends CrudRepository<R, ID>> SpringDataContext delete(String description, T repository, Iterable<R> toDelete) {
        return delete(description, (Repository<R, ID>) repository, toDelete);
    }

    public <R, ID, T extends ReactiveCrudRepository<R, ID>> SpringDataContext delete(String description, T repository, Iterable<R> toDelete) {
        return delete(description, (Repository<R, ID>) repository, toDelete);
    }

    public <R, ID, T extends RxJava2CrudRepository<R, ID>> SpringDataContext delete(String description, T repository, Iterable<R> toDelete) {
        return delete(description, (Repository<R, ID>) repository, toDelete);
    }

    public <R, ID, T extends RxJava3CrudRepository<R, ID>> SpringDataContext delete(String description, T repository, Iterable<R> toDelete) {
        return delete(description, (Repository<R, ID>) repository, toDelete);
    }

    private <R, ID, T extends Repository<R, ID>> SpringDataContext deleteByIds(String description, T repository, ID... toDelete) {
        delete(DeleteByIdsStepSupplier.delete(description, repository, toDelete));
        return this;
    }

    public <R, ID, T extends CrudRepository<R, ID>> SpringDataContext deleteByIds(String description, T repository, ID... toDelete) {
        return deleteByIds(description, (Repository<R, ID>) repository, toDelete);
    }

    public <R, ID, T extends ReactiveCrudRepository<R, ID>> SpringDataContext deleteByIds(String description, T repository, ID... toDelete) {
        return deleteByIds(description, (Repository<R, ID>) repository, toDelete);
    }

    public <R, ID, T extends RxJava2CrudRepository<R, ID>> SpringDataContext deleteByIds(String description, T repository, ID... toDelete) {
        return deleteByIds(description, (Repository<R, ID>) repository, toDelete);
    }

    public <R, ID, T extends RxJava3CrudRepository<R, ID>> SpringDataContext deleteByIds(String description, T repository, ID... toDelete) {
        return deleteByIds(description, (Repository<R, ID>) repository, toDelete);
    }

    private <R, ID, T extends Repository<R, ID>> SpringDataContext deleteAllFrom(T repository) {
        delete(DeleteAllFromStepSupplier.deleteAllRecords(repository));
        return this;
    }

    public <R, ID, T extends CrudRepository<R, ID>> SpringDataContext deleteAllFrom(T repository) {
        return deleteAllFrom((Repository<R, ID>) repository);
    }

    public <R, ID, T extends ReactiveCrudRepository<R, ID>> SpringDataContext deleteAllFrom(T repository) {
        return deleteAllFrom((Repository<R, ID>) repository);
    }

    public <R, ID, T extends RxJava2CrudRepository<R, ID>> SpringDataContext deleteAllFrom(T repository) {
        return deleteAllFrom((Repository<R, ID>) repository);
    }

    public <R, ID, T extends RxJava3CrudRepository<R, ID>> SpringDataContext deleteAllFrom(T repository) {
        return deleteAllFrom((Repository<R, ID>) repository);
    }

    @Override
    protected <R, S, Q extends SequentialGetStepSupplier<SpringDataContext, S, ?, ?, ?> & SelectQuery<S>> S update(Q query, UpdateAction<R>... actions) {
        return get(((SaveStepSupplier<?, S, R, ?, ?>) query).setUpdates(actions));
    }

    @Override
    protected <R, Q extends SequentialGetStepSupplier<SpringDataContext, R, ?, ?, ?> & InsertQuery<R>> R insert(Q query) {
        return get(query);
    }

    public <R, ID, T extends Repository<R, ID>> R save(String description, T repository, R toSave) {
        return insert(SaveStepSupplier.save(description, repository, toSave));
    }

    public <R, ID, T extends Repository<R, ID>> Iterable<R> save(String description, T repository, Iterable<R> toSave) {
        return insert(SaveStepSupplier.save(description, repository, toSave));
    }

    public <R, ID, T extends Repository<R, ID>> Iterable<R> save(String description, T repository, R... toSave) {
        checkNotNull(toSave);
        return save(description, repository, asList(toSave));
    }

    public <R, ID, T extends Repository<R, ID>> R save(String description,
                                                       T repository,
                                                       R toSave,
                                                       UpdateAction<R>... updateActions) {
        return update(SaveStepSupplier.save(description, repository, toSave), updateActions);
    }

    public <R, ID, T extends Repository<R, ID>> R save(String description,
                                                       SelectOneStepSupplier<R, ID, T> select,
                                                       UpdateAction<R>... updateActions) {
        return update(SaveStepSupplier.save(description, select), updateActions);
    }

    public <R, ID, T extends Repository<R, ID>> Iterable<R> save(String description,
                                                                 T repository,
                                                                 Iterable<R> toSave,
                                                                 UpdateAction<R>... updateActions) {
        return update(SaveStepSupplier.save(description, repository, toSave), updateActions);
    }

    public <R, ID, T extends Repository<R, ID>> Iterable<R> save(String description,
                                                                 SelectManyStepSupplier<R, ID, T> select,
                                                                 UpdateAction<R>... updateActions) {
        return update(SaveStepSupplier.save(description, select), updateActions);
    }

    public <R, ID, T extends Repository<R, ID>> boolean presenceOf(String description,
                                                                   SelectOneStepSupplier<R, ID, T> by,
                                                                   Class<? extends Throwable>... toIgnore) {
        checkArgument(isNotBlank(description), "Description should be defined");
        ((SetsDescription) by).changeDescription(translate(description));
        return super.presenceOf(by, toIgnore);
    }

    public <R, ID, T extends Repository<R, ID>> boolean presenceOfOrThrow(String description,
                                                                          SelectOneStepSupplier<R, ID, T> by,
                                                                          Class<? extends Throwable>... toIgnore) {
        checkArgument(isNotBlank(description), "Description should be defined");
        ((SetsDescription) by).changeDescription(translate(description));
        return super.presenceOfOrThrow(by, toIgnore);
    }

    public <R, ID, T extends Repository<R, ID>> boolean presenceOf(String description,
                                                                   SelectManyStepSupplier<R, ID, T> by,
                                                                   Class<? extends Throwable>... toIgnore) {
        checkArgument(isNotBlank(description), "Description should be defined");
        ((SetsDescription) by).changeDescription(translate(description));
        return super.presenceOf(by, toIgnore);
    }

    public <R, ID, T extends Repository<R, ID>> boolean presenceOfOrThrow(String description,
                                                                          SelectManyStepSupplier<R, ID, T> by,
                                                                          Class<? extends Throwable>... toIgnore) {
        checkArgument(isNotBlank(description), "Description should be defined");
        ((SetsDescription) by).changeDescription(translate(description));
        return super.presenceOfOrThrow(by, toIgnore);
    }


    public <R, ID, T extends Repository<R, ID>> boolean absenceOf(String description,
                                                                  SelectOneStepSupplier<R, ID, T> by,
                                                                  Duration timeOut) {
        checkArgument(isNotBlank(description), "Description should be defined");
        ((SetsDescription) by).changeDescription(translate(description));
        return super.absenceOf(by, timeOut);
    }

    public <R, ID, T extends Repository<R, ID>> boolean absenceOfOrThrow(String description,
                                                                         SelectOneStepSupplier<R, ID, T> by,
                                                                         Duration timeOut) {
        checkArgument(isNotBlank(description), "Description should be defined");
        ((SetsDescription) by).changeDescription(translate(description));
        return super.absenceOfOrThrow(by, timeOut);
    }

    public <R, ID, T extends Repository<R, ID>> boolean absenceOf(String description,
                                                                  SelectManyStepSupplier<R, ID, T> by,
                                                                  Duration timeOut) {
        checkArgument(isNotBlank(description), "Description should be defined");
        ((SetsDescription) by).changeDescription(translate(description));
        return super.absenceOf(by, timeOut);
    }

    public <R, ID, T extends Repository<R, ID>> boolean absenceOfOrThrow(String description,
                                                                         SelectManyStepSupplier<R, ID, T> by,
                                                                         Duration timeOut) {
        checkArgument(isNotBlank(description), "Description should be defined");
        ((SetsDescription) by).changeDescription(translate(description));
        return super.absenceOfOrThrow(by, timeOut);
    }
}
