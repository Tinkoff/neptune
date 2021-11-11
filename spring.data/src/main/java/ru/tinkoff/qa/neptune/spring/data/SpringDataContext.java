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
import ru.tinkoff.qa.neptune.spring.data.select.common.CommonSelectStepFactory;
import ru.tinkoff.qa.neptune.spring.data.select.querydsl.QueryDSLSelectStepFactory;

import java.time.Duration;
import java.util.List;
import java.util.function.Function;

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

    private static <R, ID, T extends Repository<R, ID>> SelectOneStepSupplier<R, ID, T> setDescription(String description,
                                                                                                       SelectOneStepSupplier<R, ID, T> by) {
        checkArgument(isNotBlank(description), "Description should be defined");
        ((SetsDescription) by).changeDescription(translate(description));
        return by;
    }


    private static <R, ID, T extends Repository<R, ID>> SelectManyStepSupplier<R, ID, T> setDescription(String description,
                                                                                                        SelectManyStepSupplier<R, ID, T> by) {
        checkArgument(isNotBlank(description), "Description should be defined");
        ((SetsDescription) by).changeDescription(translate(description));
        return by;
    }


    private static <S, R> GetObjectFromEntity.GetObjectFromEntityImpl<S, R> setDescription(String description,
                                                                                           GetObjectFromEntity<S, R> toGet) {
        checkArgument(isNotBlank(description), "Description should be defined");
        var impl = (GetObjectFromEntity.GetObjectFromEntityImpl<S, R>) toGet;
        return impl.setDescription(translate(description));
    }

    private static <ITEM, S extends Iterable<ITEM>, R> GetListFromEntity.GetIterableFromEntityImpl<ITEM, S, R>
    setDescription(String description,
                   GetListFromEntity<ITEM, S, R> toGet) {
        checkArgument(isNotBlank(description), "Description should be defined");
        var impl = (GetListFromEntity.GetIterableFromEntityImpl<ITEM, S, R>) toGet;
        return impl.setDescription(translate(description));
    }

    private static <ITEM, R> GetArrayFromEntity.GetArrayFromEntityImpl<ITEM, R>
    setDescription(String description,
                   GetArrayFromEntity<ITEM, R> toGet) {
        checkArgument(isNotBlank(description), "Description should be defined");
        var impl = (GetArrayFromEntity.GetArrayFromEntityImpl<ITEM, R>) toGet;
        return impl.setDescription(translate(description));
    }

    private static <ITEM, R> GetItemOfIterableFromEntity.GetItemOfIterableFromEntityImpl<ITEM, ? extends Iterable<ITEM>, R>
    setDescription(String description,
                   GetItemOfIterableFromEntity<ITEM, ? extends Iterable<ITEM>, R> toGet) {
        checkArgument(isNotBlank(description), "Description should be defined");
        var impl = (GetItemOfIterableFromEntity.GetItemOfIterableFromEntityImpl<ITEM, ? extends Iterable<ITEM>, R>) toGet;
        return impl.setDescription(translate(description));
    }

    private static <ITEM, R> GetItemOfArrayFromEntity.GetItemOfArrayFromEntityImpl<ITEM, R>
    setDescription(String description,
                   GetItemOfArrayFromEntity<ITEM, R> toGet) {
        checkArgument(isNotBlank(description), "Description should be defined");
        var impl = (GetItemOfArrayFromEntity.GetItemOfArrayFromEntityImpl<ITEM, R>) toGet;
        return impl.setDescription(translate(description));
    }

    private static <ITEM, R> GetListFromEntities.GetIterableFromEntitiesImpl<ITEM, R>
    setDescription(String description,
                   GetListFromEntities<ITEM, R> toGet) {
        checkArgument(isNotBlank(description), "Description should be defined");
        var impl = (GetListFromEntities.GetIterableFromEntitiesImpl<ITEM, R>) toGet;
        return impl.setDescription(translate(description));
    }

    private static <ITEM, R> GetIterableItemFromEntities.GetIterableItemFromEntitiesImpl<ITEM, R>
    setDescription(String description,
                   GetIterableItemFromEntities<ITEM, R> toGet) {
        checkArgument(isNotBlank(description), "Description should be defined");
        var impl = (GetIterableItemFromEntities.GetIterableItemFromEntitiesImpl<ITEM, R>) toGet;
        return impl.setDescription(translate(description));
    }

    /**
     * Performs the selecting of a single instance of some entity-class from a repository
     *
     * @param description is a description of desired instance
     * @param by          is a step that performs the selecting
     * @param <R>         is a type of entity-class
     * @param <ID>        is a type of entity ID
     * @param <T>         is a type of repository
     * @return a single instance of the entity-class
     * @see CommonSelectStepFactory
     * @see QueryDSLSelectStepFactory
     */
    public <R, ID, T extends Repository<R, ID>> R find(String description,
                                                       SelectOneStepSupplier<R, ID, T> by) {
        return select(setDescription(description, by));
    }

    /**
     * Performs the selecting of a list of instances of some entity-class from a repository
     *
     * @param description is a description of desired instances
     * @param by          is a step that performs the selecting
     * @param <R>         is a type of entity-class
     * @param <ID>        is a type of entity ID
     * @param <T>         is a type of repository
     * @return a list of instances of the entity-class
     * @see CommonSelectStepFactory
     * @see QueryDSLSelectStepFactory
     */
    public <R, ID, T extends Repository<R, ID>> List<R> find(String description,
                                                             SelectManyStepSupplier<R, ID, T> by) {
        return select(setDescription(description, by));
    }

    /**
     * Performs the selecting of a single object from a repository
     *
     * @param description is a description of desired object
     * @param toGet       describes how to get an object from the repository
     * @param <S>         is a type of desired object
     * @param <R>         is a type of entity-class
     * @return a single object from the repository
     * @see CommonSelectStepFactory
     * @see QueryDSLSelectStepFactory
     * @see SelectOneStepSupplier#thenGetObject(Function)
     */
    public <S, R> S find(String description,
                         GetObjectFromEntity<S, R> toGet) {
        return select(setDescription(description, toGet));
    }

    /**
     * Performs the selecting of a list of objects from a repository
     *
     * @param description is a description of a list of desired objects
     * @param toGet       describes how to get a list from the repository
     * @param <ITEM>      is a type of list item
     * @param <S>         is a type of {@link Iterable} to be transformed to result list
     * @param <R>         is a type of entity-class
     * @return a list of objects from a repository
     * @see CommonSelectStepFactory
     * @see QueryDSLSelectStepFactory
     * @see SelectOneStepSupplier#thenGetList(Function)
     */
    public <ITEM, S extends Iterable<ITEM>, R> List<ITEM> find(String description,
                                                               GetListFromEntity<ITEM, S, R> toGet) {
        return select(setDescription(description, toGet));
    }

    /**
     * Performs the selecting of an array of objects from a repository
     *
     * @param description is a description of desired array
     * @param toGet       describes how to get an array from the repository
     * @param <ITEM>      is a type of array item
     * @param <R>         is a type of entity-class
     * @return an array of objects from a repository
     * @see CommonSelectStepFactory
     * @see QueryDSLSelectStepFactory
     * @see SelectOneStepSupplier#thenGetArray(Function)
     */
    public <ITEM, R> ITEM[] find(String description,
                                 GetArrayFromEntity<ITEM, R> toGet) {
        return select(setDescription(description, toGet));
    }

    /**
     * Performs the selecting of an object from a repository. The result object is taken
     * from an {@link Iterable}.
     *
     * @param description is a description of desired object
     * @param toGet       describes how to get an {@link Iterable} from the repository
     * @param <ITEM>      is a type of item if {@link Iterable}
     * @param <R>         is a type of entity-class
     * @return a single object from the repository
     * @see CommonSelectStepFactory
     * @see QueryDSLSelectStepFactory
     * @see SelectOneStepSupplier#thenGetIterableItem(Function)
     */
    public <ITEM, R> ITEM find(String description,
                               GetItemOfIterableFromEntity<ITEM, ? extends Iterable<ITEM>, R> toGet) {
        return select(setDescription(description, toGet));
    }

    /**
     * Performs the selecting of an object from a repository. The result object is taken
     * from an array.
     *
     * @param description is a description of desired object
     * @param toGet       describes how to get an array from the repository
     * @param <ITEM>      is a type of array item
     * @param <R>         is a type of entity-class
     * @return a single object from the repository
     * @see CommonSelectStepFactory
     * @see QueryDSLSelectStepFactory
     * @see SelectOneStepSupplier#thenGetArrayItem(Function)
     */
    public <ITEM, R> ITEM find(String description,
                               GetItemOfArrayFromEntity<ITEM, R> toGet) {
        return select(setDescription(description, toGet));
    }

    /**
     * Performs the selecting of a list of objects from a repository. Resulted
     * list is collected of data taken from multiple instances of an entity-class
     *
     * @param description is a description of a list of desired objects
     * @param toGet       describes how to get data from an instance of entity
     * @param <ITEM>      is a type of list item
     * @param <R>         is a type of entity-class
     * @return a list of objects from a repository
     * @see CommonSelectStepFactory
     * @see QueryDSLSelectStepFactory
     * @see SelectManyStepSupplier#thenGetList(Function)
     */
    public <ITEM, R> List<ITEM> find(String description,
                                     GetListFromEntities<ITEM, R> toGet) {
        return select(setDescription(description, toGet));
    }

    /**
     * Performs the selecting of an object from a repository. The result object is taken
     * from the list collected of data taken from multiple instances of an entity-class.
     *
     * @param description is a description of desired object
     * @param toGet       describes how to get data from an instance of entity
     * @param <ITEM>      is a type of item if a list
     * @param <R>         is a type of entity-class
     * @return a single object from the repository
     * @see CommonSelectStepFactory
     * @see QueryDSLSelectStepFactory
     * @see SelectManyStepSupplier#thenGetIterableItem(Function)
     */
    public <ITEM, R> ITEM find(String description,
                               GetIterableItemFromEntities<ITEM, R> toGet) {
        return select(setDescription(description, toGet));
    }

    @Override
    protected <R, Q extends SequentialGetStepSupplier<SpringDataContext, R, ?, ?, ?> & SelectQuery<R>> R delete(Q query) {
        return get(query);
    }

    /**
     * Performs the deleting of a single instance of entity-class selected by query
     *
     * @param description is a description of the object to be deleted
     * @param select      is how to perform the selecting
     * @param <R>         is a type of entity-class
     * @param <ID>        is a type of entity ID
     * @param <T>         is a type of repository
     * @return self-reference
     * @see CommonSelectStepFactory
     * @see QueryDSLSelectStepFactory
     */
    public <R, ID, T extends Repository<R, ID>> SpringDataContext delete(String description, SelectOneStepSupplier<R, ID, T> select) {
        delete(DeleteByQueryStepSupplier.delete(description, select));
        return this;
    }

    /**
     * Performs the deleting of multiple instances of entity-class selected by query
     *
     * @param description is a description of objects to be deleted
     * @param select      is how to perform the selecting
     * @param <R>         is a type of entity-class
     * @param <ID>        is a type of entity ID
     * @param <T>         is a type of repository
     * @return self-reference
     * @see CommonSelectStepFactory
     * @see QueryDSLSelectStepFactory
     */
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

    /**
     * Performs the deleting of one or more instances of entity-class
     *
     * @param description is a description of objects to be deleted
     * @param repository  is a repository to delete data from
     * @param toDelete    are objects to be deleted
     * @param <R>         is a type of entity-class
     * @param <ID>        is a type of entity ID
     * @param <T>         is a type of repository
     * @return self-reference
     */
    public <R, ID, T extends CrudRepository<R, ID>> SpringDataContext delete(String description, T repository, R... toDelete) {
        return delete(description, (Repository<R, ID>) repository, toDelete);
    }

    /**
     * Performs the deleting of one or more instances of entity-class
     *
     * @param description is a description of objects to be deleted
     * @param repository  is a repository to delete data from
     * @param toDelete    are objects to be deleted
     * @param <R>         is a type of entity-class
     * @param <ID>        is a type of entity ID
     * @param <T>         is a type of repository
     * @return self-reference
     */
    public <R, ID, T extends ReactiveCrudRepository<R, ID>> SpringDataContext delete(String description, T repository, R... toDelete) {
        return delete(description, (Repository<R, ID>) repository, toDelete);
    }

    /**
     * Performs the deleting of one or more instances of entity-class
     *
     * @param description is a description of objects to be deleted
     * @param repository  is a repository to delete data from
     * @param toDelete    are objects to be deleted
     * @param <R>         is a type of entity-class
     * @param <ID>        is a type of entity ID
     * @param <T>         is a type of repository
     * @return self-reference
     */
    public <R, ID, T extends RxJava2CrudRepository<R, ID>> SpringDataContext delete(String description, T repository, R... toDelete) {
        return delete(description, (Repository<R, ID>) repository, toDelete);
    }

    /**
     * Performs the deleting of one or more instances of entity-class
     *
     * @param description is a description of objects to be deleted
     * @param repository  is a repository to delete data from
     * @param toDelete    are objects to be deleted
     * @param <R>         is a type of entity-class
     * @param <ID>        is a type of entity ID
     * @param <T>         is a type of repository
     * @return self-reference
     */
    public <R, ID, T extends RxJava3CrudRepository<R, ID>> SpringDataContext delete(String description, T repository, R... toDelete) {
        return delete(description, (Repository<R, ID>) repository, toDelete);
    }

    private <R, ID, T extends Repository<R, ID>> SpringDataContext delete(String description, T repository, Iterable<R> toDelete) {
        delete(DeleteByQueryStepSupplier.delete(description, repository, toDelete));
        return this;
    }

    /**
     * Performs the deleting of iterable of instances of entity-class
     *
     * @param description is a description of objects to be deleted
     * @param repository  is a repository to delete data from
     * @param toDelete    are objects to be deleted
     * @param <R>         is a type of entity-class
     * @param <ID>        is a type of entity ID
     * @param <T>         is a type of repository
     * @return self-reference
     */
    public <R, ID, T extends CrudRepository<R, ID>> SpringDataContext delete(String description, T repository, Iterable<R> toDelete) {
        return delete(description, (Repository<R, ID>) repository, toDelete);
    }

    /**
     * Performs the deleting of iterable of instances of entity-class
     *
     * @param description is a description of objects to be deleted
     * @param repository  is a repository to delete data from
     * @param toDelete    are objects to be deleted
     * @param <R>         is a type of entity-class
     * @param <ID>        is a type of entity ID
     * @param <T>         is a type of repository
     * @return self-reference
     */
    public <R, ID, T extends ReactiveCrudRepository<R, ID>> SpringDataContext delete(String description, T repository, Iterable<R> toDelete) {
        return delete(description, (Repository<R, ID>) repository, toDelete);
    }

    /**
     * Performs the deleting of iterable of instances of entity-class
     *
     * @param description is a description of objects to be deleted
     * @param repository  is a repository to delete data from
     * @param toDelete    are objects to be deleted
     * @param <R>         is a type of entity-class
     * @param <ID>        is a type of entity ID
     * @param <T>         is a type of repository
     * @return self-reference
     */
    public <R, ID, T extends RxJava2CrudRepository<R, ID>> SpringDataContext delete(String description, T repository, Iterable<R> toDelete) {
        return delete(description, (Repository<R, ID>) repository, toDelete);
    }

    /**
     * Performs the deleting of iterable of instances of entity-class
     *
     * @param description is a description of objects to be deleted
     * @param repository  is a repository to delete data from
     * @param toDelete    are objects to be deleted
     * @param <R>         is a type of entity-class
     * @param <ID>        is a type of entity ID
     * @param <T>         is a type of repository
     * @return self-reference
     */
    public <R, ID, T extends RxJava3CrudRepository<R, ID>> SpringDataContext delete(String description, T repository, Iterable<R> toDelete) {
        return delete(description, (Repository<R, ID>) repository, toDelete);
    }

    private <R, ID, T extends Repository<R, ID>> SpringDataContext deleteByIds(String description, T repository, ID... toDelete) {
        delete(DeleteByIdsStepSupplier.delete(description, repository, toDelete));
        return this;
    }

    /**
     * Performs the deleting of one or more instances of entity-class by known IDs
     *
     * @param description is a description of objects to be deleted
     * @param repository  is a repository to delete data from
     * @param toDelete    IDs of objects to be deleted
     * @param <R>         is a type of entity-class
     * @param <ID>        is a type of entity ID
     * @param <T>         is a type of repository
     * @return self-reference
     */
    public <R, ID, T extends CrudRepository<R, ID>> SpringDataContext deleteByIds(String description, T repository, ID... toDelete) {
        return deleteByIds(description, (Repository<R, ID>) repository, toDelete);
    }

    /**
     * Performs the deleting of one or more instances of entity-class by known IDs
     *
     * @param description is a description of objects to be deleted
     * @param repository  is a repository to delete data from
     * @param toDelete    IDs of objects to be deleted
     * @param <R>         is a type of entity-class
     * @param <ID>        is a type of entity ID
     * @param <T>         is a type of repository
     * @return self-reference
     */
    public <R, ID, T extends ReactiveCrudRepository<R, ID>> SpringDataContext deleteByIds(String description, T repository, ID... toDelete) {
        return deleteByIds(description, (Repository<R, ID>) repository, toDelete);
    }

    /**
     * Performs the deleting of one or more instances of entity-class by known IDs
     *
     * @param description is a description of objects to be deleted
     * @param repository  is a repository to delete data from
     * @param toDelete    IDs of objects to be deleted
     * @param <R>         is a type of entity-class
     * @param <ID>        is a type of entity ID
     * @param <T>         is a type of repository
     * @return self-reference
     */
    public <R, ID, T extends RxJava2CrudRepository<R, ID>> SpringDataContext deleteByIds(String description, T repository, ID... toDelete) {
        return deleteByIds(description, (Repository<R, ID>) repository, toDelete);
    }

    /**
     * Performs the deleting of one or more instances of entity-class by known IDs
     *
     * @param description is a description of objects to be deleted
     * @param repository  is a repository to delete data from
     * @param toDelete    IDs of objects to be deleted
     * @param <R>         is a type of entity-class
     * @param <ID>        is a type of entity ID
     * @param <T>         is a type of repository
     * @return self-reference
     */
    public <R, ID, T extends RxJava3CrudRepository<R, ID>> SpringDataContext deleteByIds(String description, T repository, ID... toDelete) {
        return deleteByIds(description, (Repository<R, ID>) repository, toDelete);
    }

    /**
     * Performs the deleting of all objets from a repository
     *
     * @param repository is a repository to delete data from
     * @param <R>        is a type of entity-class
     * @param <ID>       is a type of entity ID
     * @param <T>        is a type of repository
     * @return self-reference
     */
    private <R, ID, T extends Repository<R, ID>> SpringDataContext deleteAllFrom(T repository) {
        delete(DeleteAllFromStepSupplier.deleteAllRecords(repository));
        return this;
    }

    /**
     * Performs the deleting of all objets from a repository
     *
     * @param repository is a repository to delete data from
     * @param <R>        is a type of entity-class
     * @param <ID>       is a type of entity ID
     * @param <T>        is a type of repository
     * @return self-reference
     */
    public <R, ID, T extends CrudRepository<R, ID>> SpringDataContext deleteAllFrom(T repository) {
        return deleteAllFrom((Repository<R, ID>) repository);
    }

    /**
     * Performs the deleting of all objets from a repository
     *
     * @param repository is a repository to delete data from
     * @param <R>        is a type of entity-class
     * @param <ID>       is a type of entity ID
     * @param <T>        is a type of repository
     * @return self-reference
     */
    public <R, ID, T extends ReactiveCrudRepository<R, ID>> SpringDataContext deleteAllFrom(T repository) {
        return deleteAllFrom((Repository<R, ID>) repository);
    }

    /**
     * Performs the deleting of all objets from a repository
     *
     * @param repository is a repository to delete data from
     * @param <R>        is a type of entity-class
     * @param <ID>       is a type of entity ID
     * @param <T>        is a type of repository
     * @return self-reference
     */
    public <R, ID, T extends RxJava2CrudRepository<R, ID>> SpringDataContext deleteAllFrom(T repository) {
        return deleteAllFrom((Repository<R, ID>) repository);
    }

    /**
     * Performs the deleting of all objets from a repository
     *
     * @param repository is a repository to delete data from
     * @param <R>        is a type of entity-class
     * @param <ID>       is a type of entity ID
     * @param <T>        is a type of repository
     * @return self-reference
     */
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

    /**
     * Performs the changing and saving of selected instance of entity-class
     *
     * @param description   description of an object to be changed
     * @param select        describes how to select an object to be changed and saved
     * @param updateActions describe changes
     * @param <R>           is a type of entity-class
     * @param <ID>          is a type of entity ID
     * @param <T>           is a type of repository
     * @return changed and saved object
     * @see CommonSelectStepFactory
     * @see QueryDSLSelectStepFactory
     */
    @SafeVarargs
    public final <R, ID, T extends Repository<R, ID>> R save(String description,
                                                             SelectOneStepSupplier<R, ID, T> select,
                                                             UpdateAction<R>... updateActions) {
        return update(SaveStepSupplier.save(description, select), updateActions);
    }

    /**
     * Performs the changing and saving of selected instances of entity-class
     *
     * @param description   description of objects to be changed
     * @param select        describes how to select objects to be changed and saved
     * @param updateActions describe changes
     * @param <R>           is a type of entity-class
     * @param <ID>          is a type of entity ID
     * @param <T>           is a type of repository
     * @return iterable of changed and saved objects
     * @see CommonSelectStepFactory
     * @see QueryDSLSelectStepFactory
     */
    @SafeVarargs
    public final <R, ID, T extends Repository<R, ID>> Iterable<R> save(String description,
                                                                       SelectManyStepSupplier<R, ID, T> select,
                                                                       UpdateAction<R>... updateActions) {
        return update(SaveStepSupplier.save(description, select), updateActions);
    }

    /**
     * Performs the saving of a (new or previously selected and changed) instance of entity-class
     *
     * @param description description of an object to be saved
     * @param repository  is a repository
     * @param toSave      is an object to be saved
     * @param <R>         is a type of entity-class
     * @param <ID>        is a type of entity ID
     * @param <T>         is a type of repository
     * @return saved object
     */
    public <R, ID, T extends CrudRepository<R, ID>> R save(String description, T repository, R toSave) {
        return insert(SaveStepSupplier.save(description, repository, toSave));
    }

    /**
     * Performs the saving of iterable of (new or previously selected and changed) instances of entity-class
     *
     * @param description description of objects to be saved
     * @param repository  is a repository
     * @param toSave      are objects to be saved
     * @param <R>         is a type of entity-class
     * @param <ID>        is a type of entity ID
     * @param <T>         is a type of repository
     * @return iterable of saved objects
     */
    public <R, ID, T extends CrudRepository<R, ID>> Iterable<R> save(String description, T repository, Iterable<R> toSave) {
        return insert(SaveStepSupplier.save(description, repository, toSave));
    }

    /**
     * Performs the saving of (new or previously selected and changed) instances of entity-class
     *
     * @param description description of objects to be saved
     * @param repository  is a repository
     * @param toSave      are objects to be saved
     * @param <R>         is a type of entity-class
     * @param <ID>        is a type of entity ID
     * @param <T>         is a type of repository
     * @return iterable of saved objects
     */
    public <R, ID, T extends CrudRepository<R, ID>> Iterable<R> save(String description, T repository, R... toSave) {
        checkNotNull(toSave);
        return save(description, repository, asList(toSave));
    }

    /**
     * Performs the changing and saving of an instance of entity-class
     *
     * @param description   description of an object to be changed and saved
     * @param repository    is a repository
     * @param toSave        is an object to be saved
     * @param updateActions describe changes
     * @param <R>           is a type of entity-class
     * @param <ID>          is a type of entity ID
     * @param <T>           is a type of repository
     * @return changed and saved object
     */
    @SafeVarargs
    public final <R, ID, T extends CrudRepository<R, ID>> R save(String description,
                                                                 T repository,
                                                                 R toSave,
                                                                 UpdateAction<R>... updateActions) {
        return update(SaveStepSupplier.save(description, repository, toSave), updateActions);
    }

    /**
     * Performs the changing and saving of iterable of instances of entity-class
     *
     * @param description   description of objects to be changed and saved
     * @param repository    is a repository
     * @param toSave        are objects to be changed and saved
     * @param updateActions describe changes
     * @param <R>           is a type of entity-class
     * @param <ID>          is a type of entity ID
     * @param <T>           is a type of repository
     * @return iterable of changed and saved objects
     */
    @SafeVarargs
    public final <R, ID, T extends CrudRepository<R, ID>> Iterable<R> save(String description,
                                                                           T repository,
                                                                           Iterable<R> toSave,
                                                                           UpdateAction<R>... updateActions) {
        return update(SaveStepSupplier.save(description, repository, toSave), updateActions);
    }

    /**
     * Performs the saving of a (new or previously selected and changed) instance of entity-class
     *
     * @param description description of an object to be saved
     * @param repository  is a repository
     * @param toSave      is an object to be saved
     * @param <R>         is a type of entity-class
     * @param <ID>        is a type of entity ID
     * @param <T>         is a type of repository
     * @return saved object
     */
    public <R, ID, T extends ReactiveCrudRepository<R, ID>> R save(String description, T repository, R toSave) {
        return insert(SaveStepSupplier.save(description, repository, toSave));
    }

    /**
     * Performs the saving of iterable of (new or previously selected and changed) instances of entity-class
     *
     * @param description description of objects to be saved
     * @param repository  is a repository
     * @param toSave      are objects to be saved
     * @param <R>         is a type of entity-class
     * @param <ID>        is a type of entity ID
     * @param <T>         is a type of repository
     * @return iterable of saved objects
     */
    public <R, ID, T extends ReactiveCrudRepository<R, ID>> Iterable<R> save(String description, T repository, Iterable<R> toSave) {
        return insert(SaveStepSupplier.save(description, repository, toSave));
    }

    /**
     * Performs the saving of (new or previously selected and changed) instances of entity-class
     *
     * @param description description of objects to be saved
     * @param repository  is a repository
     * @param toSave      are objects to be saved
     * @param <R>         is a type of entity-class
     * @param <ID>        is a type of entity ID
     * @param <T>         is a type of repository
     * @return iterable of saved objects
     */
    public <R, ID, T extends ReactiveCrudRepository<R, ID>> Iterable<R> save(String description, T repository, R... toSave) {
        checkNotNull(toSave);
        return save(description, repository, asList(toSave));
    }

    /**
     * Performs the changing and saving of an instance of entity-class
     *
     * @param description   description of an object to be changed and saved
     * @param repository    is a repository
     * @param toSave        is an object to be saved
     * @param updateActions describe changes
     * @param <R>           is a type of entity-class
     * @param <ID>          is a type of entity ID
     * @param <T>           is a type of repository
     * @return changed and saved object
     */
    @SafeVarargs
    public final <R, ID, T extends ReactiveCrudRepository<R, ID>> R save(String description,
                                                                         T repository,
                                                                         R toSave,
                                                                         UpdateAction<R>... updateActions) {
        return update(SaveStepSupplier.save(description, repository, toSave), updateActions);
    }

    /**
     * Performs the changing and saving of iterable of instances of entity-class
     *
     * @param description   description of objects to be changed and saved
     * @param repository    is a repository
     * @param toSave        are objects to be changed and saved
     * @param updateActions describe changes
     * @param <R>           is a type of entity-class
     * @param <ID>          is a type of entity ID
     * @param <T>           is a type of repository
     * @return iterable of changed and saved objects
     */
    @SafeVarargs
    public final <R, ID, T extends ReactiveCrudRepository<R, ID>> Iterable<R> save(String description,
                                                                                   T repository,
                                                                                   Iterable<R> toSave,
                                                                                   UpdateAction<R>... updateActions) {
        return update(SaveStepSupplier.save(description, repository, toSave), updateActions);
    }

    /**
     * Performs the saving of a (new or previously selected and changed) instance of entity-class
     *
     * @param description description of an object to be saved
     * @param repository  is a repository
     * @param toSave      is an object to be saved
     * @param <R>         is a type of entity-class
     * @param <ID>        is a type of entity ID
     * @param <T>         is a type of repository
     * @return saved object
     */
    public <R, ID, T extends RxJava2CrudRepository<R, ID>> R save(String description, T repository, R toSave) {
        return insert(SaveStepSupplier.save(description, repository, toSave));
    }

    /**
     * Performs the saving of iterable of (new or previously selected and changed) instances of entity-class
     *
     * @param description description of objects to be saved
     * @param repository  is a repository
     * @param toSave      are objects to be saved
     * @param <R>         is a type of entity-class
     * @param <ID>        is a type of entity ID
     * @param <T>         is a type of repository
     * @return iterable of saved objects
     */
    public <R, ID, T extends RxJava2CrudRepository<R, ID>> Iterable<R> save(String description, T repository, Iterable<R> toSave) {
        return insert(SaveStepSupplier.save(description, repository, toSave));
    }

    /**
     * Performs the saving of (new or previously selected and changed) instances of entity-class
     *
     * @param description description of objects to be saved
     * @param repository  is a repository
     * @param toSave      are objects to be saved
     * @param <R>         is a type of entity-class
     * @param <ID>        is a type of entity ID
     * @param <T>         is a type of repository
     * @return iterable of saved objects
     */
    public <R, ID, T extends RxJava2CrudRepository<R, ID>> Iterable<R> save(String description, T repository, R... toSave) {
        checkNotNull(toSave);
        return save(description, repository, asList(toSave));
    }

    /**
     * Performs the changing and saving of an instance of entity-class
     *
     * @param description   description of an object to be changed and saved
     * @param repository    is a repository
     * @param toSave        is an object to be saved
     * @param updateActions describe changes
     * @param <R>           is a type of entity-class
     * @param <ID>          is a type of entity ID
     * @param <T>           is a type of repository
     * @return changed and saved object
     */
    @SafeVarargs
    public final <R, ID, T extends RxJava2CrudRepository<R, ID>> R save(String description,
                                                                        T repository,
                                                                        R toSave,
                                                                        UpdateAction<R>... updateActions) {
        return update(SaveStepSupplier.save(description, repository, toSave), updateActions);
    }

    /**
     * Performs the changing and saving of iterable of instances of entity-class
     *
     * @param description   description of objects to be changed and saved
     * @param repository    is a repository
     * @param toSave        are objects to be changed and saved
     * @param updateActions describe changes
     * @param <R>           is a type of entity-class
     * @param <ID>          is a type of entity ID
     * @param <T>           is a type of repository
     * @return iterable of changed and saved objects
     */
    @SafeVarargs
    public final <R, ID, T extends RxJava2CrudRepository<R, ID>> Iterable<R> save(String description,
                                                                                  T repository,
                                                                                  Iterable<R> toSave,
                                                                                  UpdateAction<R>... updateActions) {
        return update(SaveStepSupplier.save(description, repository, toSave), updateActions);
    }

    /**
     * Performs the saving of a (new or previously selected and changed) instance of entity-class
     *
     * @param description description of an object to be saved
     * @param repository  is a repository
     * @param toSave      is an object to be saved
     * @param <R>         is a type of entity-class
     * @param <ID>        is a type of entity ID
     * @param <T>         is a type of repository
     * @return saved object
     */
    public <R, ID, T extends RxJava3CrudRepository<R, ID>> R save(String description, T repository, R toSave) {
        return insert(SaveStepSupplier.save(description, repository, toSave));
    }

    /**
     * Performs the saving of iterable of (new or previously selected and changed) instances of entity-class
     *
     * @param description description of objects to be saved
     * @param repository  is a repository
     * @param toSave      are objects to be saved
     * @param <R>         is a type of entity-class
     * @param <ID>        is a type of entity ID
     * @param <T>         is a type of repository
     * @return iterable of saved objects
     */
    public <R, ID, T extends RxJava3CrudRepository<R, ID>> Iterable<R> save(String description, T repository, Iterable<R> toSave) {
        return insert(SaveStepSupplier.save(description, repository, toSave));
    }

    /**
     * Performs the saving of (new or previously selected and changed) instances of entity-class
     *
     * @param description description of objects to be saved
     * @param repository  is a repository
     * @param toSave      are objects to be saved
     * @param <R>         is a type of entity-class
     * @param <ID>        is a type of entity ID
     * @param <T>         is a type of repository
     * @return iterable of saved objects
     */
    public <R, ID, T extends RxJava3CrudRepository<R, ID>> Iterable<R> save(String description, T repository, R... toSave) {
        checkNotNull(toSave);
        return save(description, repository, asList(toSave));
    }

    /**
     * Performs the changing and saving of an instance of entity-class
     *
     * @param description   description of an object to be changed and saved
     * @param repository    is a repository
     * @param toSave        is an object to be saved
     * @param updateActions describe changes
     * @param <R>           is a type of entity-class
     * @param <ID>          is a type of entity ID
     * @param <T>           is a type of repository
     * @return changed and saved object
     */
    @SafeVarargs
    public final <R, ID, T extends RxJava3CrudRepository<R, ID>> R save(String description,
                                                                        T repository,
                                                                        R toSave,
                                                                        UpdateAction<R>... updateActions) {
        return update(SaveStepSupplier.save(description, repository, toSave), updateActions);
    }

    /**
     * Performs the changing and saving of iterable of instances of entity-class
     *
     * @param description   description of objects to be changed and saved
     * @param repository    is a repository
     * @param toSave        are objects to be changed and saved
     * @param updateActions describe changes
     * @param <R>           is a type of entity-class
     * @param <ID>          is a type of entity ID
     * @param <T>           is a type of repository
     * @return iterable of changed and saved objects
     */
    @SafeVarargs
    public final <R, ID, T extends RxJava3CrudRepository<R, ID>> Iterable<R> save(String description,
                                                                                  T repository,
                                                                                  Iterable<R> toSave,
                                                                                  UpdateAction<R>... updateActions) {
        return update(SaveStepSupplier.save(description, repository, toSave), updateActions);
    }


    /**
     * Checks is instance of entity-class present or not
     *
     * @param description is description of an object to be present
     * @param by          how to select an object to be present
     * @param <R>         is a type of entity-class
     * @param <ID>        is a type of entity ID
     * @param <T>         is a type of repository
     * @return is the instance of entity-class present or not
     * @see CommonSelectStepFactory
     * @see QueryDSLSelectStepFactory
     */
    public <R, ID, T extends Repository<R, ID>> boolean presenceOf(String description,
                                                                   SelectOneStepSupplier<R, ID, T> by) {
        return super.presenceOf(setDescription(description, by));
    }

    /**
     * Checks is instance of entity-class present. If it is not present then it throws an exception
     *
     * @param description is description of an object to be present
     * @param by          how to select an object to be present
     * @param <R>         is a type of entity-class
     * @param <ID>        is a type of entity ID
     * @param <T>         is a type of repository
     * @return is the instance of entity-class present or not
     * @see CommonSelectStepFactory
     * @see QueryDSLSelectStepFactory
     */
    public <R, ID, T extends Repository<R, ID>> boolean presenceOfOrThrow(String description,
                                                                          SelectOneStepSupplier<R, ID, T> by) {
        return super.presenceOfOrThrow(setDescription(description, by));
    }

    /**
     * Checks are instances of entity-class present or not
     *
     * @param description is description objects to be present
     * @param by          how to select objects to be present
     * @param <R>         is a type of entity-class
     * @param <ID>        is a type of entity ID
     * @param <T>         is a type of repository
     * @return are instances of entity-class present or not
     * @see CommonSelectStepFactory
     * @see QueryDSLSelectStepFactory
     */
    public <R, ID, T extends Repository<R, ID>> boolean presenceOf(String description,
                                                                   SelectManyStepSupplier<R, ID, T> by) {
        return super.presenceOf(setDescription(description, by));
    }

    /**
     * Checks are instances of entity-class present. If they are not present then it throws an exception
     *
     * @param description is description objects to be present
     * @param by          how to select objects to be present
     * @param <R>         is a type of entity-class
     * @param <ID>        is a type of entity ID
     * @param <T>         is a type of repository
     * @return are instances of entity-class present or not
     * @see CommonSelectStepFactory
     * @see QueryDSLSelectStepFactory
     */
    public <R, ID, T extends Repository<R, ID>> boolean presenceOfOrThrow(String description,
                                                                          SelectManyStepSupplier<R, ID, T> by) {
        return super.presenceOfOrThrow(setDescription(description, by));
    }


    /**
     * Checks is a single object from a repository present or not
     *
     * @param description is description of an object to be present
     * @param toGet       describes how to get an object from the repository
     * @param <R>         is a type of entity-class
     * @return is the object present or not
     * @see CommonSelectStepFactory
     * @see QueryDSLSelectStepFactory
     * @see SelectOneStepSupplier#thenGetObject(Function)
     */
    public <S, R> boolean presenceOf(String description,
                                     GetObjectFromEntity<S, R> toGet) {
        return super.presenceOf(setDescription(description, toGet));
    }

    /**
     * Checks is a single object from a repository present. If it is not present then it throws an exception
     *
     * @param description is description of an object to be present
     * @param toGet       describes how to get an object from the repository
     * @param <R>         is a type of entity-class
     * @return is the object present or not
     * @see CommonSelectStepFactory
     * @see QueryDSLSelectStepFactory
     * @see SelectOneStepSupplier#thenGetObject(Function)
     */
    public <S, R> boolean presenceOfOrThrow(String description,
                                            GetObjectFromEntity<S, R> toGet) {
        return super.presenceOfOrThrow(setDescription(description, toGet));
    }


    /**
     * Checks are objects from a repository present or not
     *
     * @param description is a description of a list to be present
     * @param toGet       describes how to get a list from the repository
     * @param <ITEM>      is a type of list item
     * @param <S>         is a type of {@link Iterable} to be transformed to result list
     * @param <R>         is a type of entity-class
     * @return are objects present or not
     * @see CommonSelectStepFactory
     * @see QueryDSLSelectStepFactory
     * @see SelectOneStepSupplier#thenGetList(Function)
     */
    public <ITEM, S extends Iterable<ITEM>, R> boolean presenceOf(String description,
                                                                  GetListFromEntity<ITEM, S, R> toGet) {
        return super.presenceOf(setDescription(description, toGet));
    }

    /**
     * Checks are objects from a repository present. If they are not present then it throws an exception
     *
     * @param description is a description of a list to be present
     * @param toGet       describes how to get a list from the repository
     * @param <ITEM>      is a type of list item
     * @param <S>         is a type of {@link Iterable} to be transformed to result list
     * @param <R>         is a type of entity-class
     * @return are objects present or not
     * @see CommonSelectStepFactory
     * @see QueryDSLSelectStepFactory
     * @see SelectOneStepSupplier#thenGetList(Function)
     */
    public <ITEM, S extends Iterable<ITEM>, R> boolean presenceOfOrThrow(String description,
                                                                         GetListFromEntity<ITEM, S, R> toGet) {
        return super.presenceOfOrThrow(setDescription(description, toGet));
    }

    /**
     * Checks are objects from a repository present or not
     *
     * @param description is a description of an array to be present
     * @param toGet       describes how to get an array from the repository
     * @param <ITEM>      is a type of array item
     * @param <R>         is a type of entity-class
     * @return are objects present or not
     * @see CommonSelectStepFactory
     * @see QueryDSLSelectStepFactory
     * @see SelectOneStepSupplier#thenGetArray(Function)
     */
    public <ITEM, R> boolean presenceOf(String description,
                                              GetArrayFromEntity<ITEM, R> toGet) {
        return super.presenceOf(setDescription(description, toGet));
    }

    /**
     * Checks are objects from a repository present. If they are not present then it throws an exception
     *
     * @param description is a description of an array to be present
     * @param toGet       describes how to get an array from the repository
     * @param <ITEM>      is a type of array item
     * @param <R>         is a type of entity-class
     * @return are objects present or not
     * @see CommonSelectStepFactory
     * @see QueryDSLSelectStepFactory
     * @see SelectOneStepSupplier#thenGetArray(Function)
     */
    public <ITEM, R> boolean presenceOfOrThrow(String description,
                                               GetArrayFromEntity<ITEM, R> toGet) {
        return super.presenceOfOrThrow(setDescription(description, toGet));
    }


    @SafeVarargs
    public final <ITEM, R> boolean presenceOf(String description,
                                              GetItemOfIterableFromEntity<ITEM, ? extends Iterable<ITEM>, R> toGet,
                                              Class<? extends Throwable>... toIgnore) {
        return super.presenceOf(setDescription(description, toGet), toIgnore);
    }

    @SafeVarargs
    public final <ITEM, R> boolean presenceOfOrThrow(String description,
                                                     GetItemOfIterableFromEntity<ITEM, ? extends Iterable<ITEM>, R> toGet,
                                                     Class<? extends Throwable>... toIgnore) {
        return super.presenceOfOrThrow(setDescription(description, toGet), toIgnore);
    }


    @SafeVarargs
    public final <ITEM, R> boolean presenceOf(String description,
                                              GetItemOfArrayFromEntity<ITEM, R> toGet,
                                              Class<? extends Throwable>... toIgnore) {
        return super.presenceOf(setDescription(description, toGet), toIgnore);
    }

    @SafeVarargs
    public final <ITEM, R> boolean presenceOfOrThrow(String description,
                                                     GetItemOfArrayFromEntity<ITEM, R> toGet,
                                                     Class<? extends Throwable>... toIgnore) {
        return super.presenceOfOrThrow(setDescription(description, toGet), toIgnore);
    }


    @SafeVarargs
    public final <ITEM, R> boolean presenceOf(String description,
                                              GetListFromEntities<ITEM, R> toGet,
                                              Class<? extends Throwable>... toIgnore) {
        return super.presenceOf(setDescription(description, toGet), toIgnore);
    }

    @SafeVarargs
    public final <ITEM, R> boolean presenceOfOrThrow(String description,
                                                     GetListFromEntities<ITEM, R> toGet,
                                                     Class<? extends Throwable>... toIgnore) {
        return super.presenceOfOrThrow(setDescription(description, toGet), toIgnore);
    }

    @SafeVarargs
    public final <ITEM, R> boolean presenceOf(String description,
                                              GetIterableItemFromEntities<ITEM, R> toGet,
                                              Class<? extends Throwable>... toIgnore) {
        return super.presenceOf(setDescription(description, toGet), toIgnore);
    }

    @SafeVarargs
    public final <ITEM, R> boolean presenceOfOrThrow(String description,
                                                     GetIterableItemFromEntities<ITEM, R> toGet,
                                                     Class<? extends Throwable>... toIgnore) {
        return super.presenceOfOrThrow(setDescription(description, toGet), toIgnore);
    }


    public final <R, ID, T extends Repository<R, ID>> boolean absenceOf(String description,
                                                                        SelectOneStepSupplier<R, ID, T> by,
                                                                        Duration timeOut) {
        return super.absenceOf(setDescription(description, by), timeOut);
    }

    public final <R, ID, T extends Repository<R, ID>> boolean absenceOfOrThrow(String description,
                                                                               SelectOneStepSupplier<R, ID, T> by,
                                                                               Duration timeOut) {
        return super.absenceOfOrThrow(setDescription(description, by), timeOut);
    }

    public final <R, ID, T extends Repository<R, ID>> boolean absenceOf(String description,
                                                                        SelectManyStepSupplier<R, ID, T> by,
                                                                        Duration timeOut) {
        return super.absenceOf(setDescription(description, by), timeOut);
    }

    public final <R, ID, T extends Repository<R, ID>> boolean absenceOfOrThrow(String description,
                                                                               SelectManyStepSupplier<R, ID, T> by,
                                                                               Duration timeOut) {
        return super.absenceOfOrThrow(setDescription(description, by), timeOut);
    }


    public final <S, R> boolean absenceOf(String description,
                                          GetObjectFromEntity<S, R> toGet,
                                          Duration timeOut) {
        return super.absenceOf(setDescription(description, toGet), timeOut);
    }

    public final <S, R> boolean absenceOfOrThrow(String description,
                                                 GetObjectFromEntity<S, R> toGet,
                                                 Duration timeOut) {
        return super.absenceOfOrThrow(setDescription(description, toGet), timeOut);
    }


    public final <ITEM, S extends Iterable<ITEM>, R> boolean absenceOf(String description,
                                                                       GetListFromEntity<ITEM, S, R> toGet,
                                                                       Duration timeOut) {
        return super.absenceOf(setDescription(description, toGet), timeOut);
    }

    public final <ITEM, S extends Iterable<ITEM>, R> boolean absenceOfOrThrow(String description,
                                                                              GetListFromEntity<ITEM, S, R> toGet,
                                                                              Duration timeOut) {
        return super.absenceOfOrThrow(setDescription(description, toGet), timeOut);
    }


    public final <ITEM, R> boolean absenceOf(String description,
                                             GetArrayFromEntity<ITEM, R> toGet,
                                             Duration timeOut) {
        return super.absenceOf(setDescription(description, toGet), timeOut);
    }

    public final <ITEM, R> boolean absenceOfOrThrow(String description,
                                                    GetArrayFromEntity<ITEM, R> toGet,
                                                    Duration timeOut) {
        return super.absenceOfOrThrow(setDescription(description, toGet), timeOut);
    }


    public final <ITEM, R> boolean absenceOf(String description,
                                             GetItemOfIterableFromEntity<ITEM, ? extends Iterable<ITEM>, R> toGet,
                                             Duration timeOut) {
        return super.absenceOf(setDescription(description, toGet), timeOut);
    }

    public final <ITEM, R> boolean absenceOfOrThrow(String description,
                                                    GetItemOfIterableFromEntity<ITEM, ? extends Iterable<ITEM>, R> toGet,
                                                    Duration timeOut) {
        return super.absenceOfOrThrow(setDescription(description, toGet), timeOut);
    }


    public final <ITEM, R> boolean absenceOf(String description,
                                             GetItemOfArrayFromEntity<ITEM, R> toGet,
                                             Duration timeOut) {
        return super.absenceOf(setDescription(description, toGet), timeOut);
    }

    public final <ITEM, R> boolean absenceOfOrThrow(String description,
                                                    GetItemOfArrayFromEntity<ITEM, R> toGet,
                                                    Duration timeOut) {
        return super.absenceOfOrThrow(setDescription(description, toGet), timeOut);
    }


    public final <ITEM, R> boolean absenceOf(String description,
                                             GetListFromEntities<ITEM, R> toGet,
                                             Duration timeOut) {
        return super.absenceOf(setDescription(description, toGet), timeOut);
    }

    public final <ITEM, R> boolean absenceOfOrThrow(String description,
                                                    GetListFromEntities<ITEM, R> toGet,
                                                    Duration timeOut) {
        return super.absenceOfOrThrow(setDescription(description, toGet), timeOut);
    }


    public final <ITEM, R> boolean absenceOf(String description,
                                             GetIterableItemFromEntities<ITEM, R> toGet,
                                             Duration timeOut) {
        return super.absenceOf(setDescription(description, toGet), timeOut);
    }

    public final <ITEM, R> boolean absenceOfOrThrow(String description,
                                                    GetIterableItemFromEntities<ITEM, R> toGet,
                                                    Duration timeOut) {
        return super.absenceOfOrThrow(setDescription(description, toGet), timeOut);
    }
}
