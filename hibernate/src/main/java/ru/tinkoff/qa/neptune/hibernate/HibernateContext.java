package ru.tinkoff.qa.neptune.hibernate;

import org.hibernate.SessionFactory;
import org.hibernate.UnknownEntityTypeException;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.database.abstractions.AbstractDatabaseStepContext;
import ru.tinkoff.qa.neptune.database.abstractions.InsertQuery;
import ru.tinkoff.qa.neptune.database.abstractions.SelectQuery;
import ru.tinkoff.qa.neptune.database.abstractions.UpdateAction;
import ru.tinkoff.qa.neptune.hibernate.delete.DeleteByQueryStepSupplier;
import ru.tinkoff.qa.neptune.hibernate.save.SaveStepSupplier;
import ru.tinkoff.qa.neptune.hibernate.select.*;
import ru.tinkoff.qa.neptune.hibernate.session.factory.DefaultSessionFactorySource;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.metamodel.EntityType;
import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.Arrays.asList;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static ru.tinkoff.qa.neptune.core.api.localization.StepLocalization.translate;
import static ru.tinkoff.qa.neptune.core.api.steps.context.ContextFactory.getCreatedContextOrCreate;
import static ru.tinkoff.qa.neptune.hibernate.properties.SessionFactorySourceProperty.SESSION_FACTORY_SOURCE_PROPERTY;

@SuppressWarnings("unchecked")
public class HibernateContext extends AbstractDatabaseStepContext<HibernateContext> {

    public static final String NO_DESC_ERROR_TEXT = "Description should be defined";

    private static final HibernateContext context = getCreatedContextOrCreate(HibernateContext.class);
    private final Set<SessionFactory> sessionFactorySetDefault = new HashSet<>();

    public static HibernateContext hibernate() {
        return context;
    }

    @Override
    public void stop() {
        sessionFactorySetDefault.forEach(SessionFactory::close);
        sessionFactorySetDefault.clear();
    }

    public Set<SessionFactory> getSessionFactories() {
        var sourceProperty = SESSION_FACTORY_SOURCE_PROPERTY.get();

        if (sourceProperty instanceof DefaultSessionFactorySource) {
            return sourceProperty.fillSessionFactories(sessionFactorySetDefault);
        }

        return sourceProperty.fillSessionFactories(new LinkedHashSet<>());
    }

    public CriteriaBuilder getCriteriaBuilder(Class<?> entityCls) {
        return getSessionFactoryByEntity(entityCls).getCriteriaBuilder();
    }

    public <R> CriteriaQuery<R> getCriteriaQuery(Class<R> entityCls) {
        return getSessionFactoryByEntity(entityCls).getCriteriaBuilder().createQuery(entityCls);
    }

    public SessionFactory getSessionFactoryByEntity(Class<?> entityCls) {
        for (var sessionFactory : context.getSessionFactories()) {
            var entities = sessionFactory.getMetamodel()
                    .getEntities()
                    .stream()
                    .map(EntityType::getJavaType)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());

            if (entities.contains(entityCls)) {
                return sessionFactory;
            }
        }

        throw new UnknownEntityTypeException("SessionFactory for entity " + entityCls.getName() +
                " not found");
    }

    @Override
    protected final <R, Q extends SequentialGetStepSupplier<HibernateContext, R, ?, ?, ?> & SelectQuery<R>> R select(Q query) {
        return get(query);
    }

    private static <R> SelectOneStepSupplier<R> setDescription(String description,
                                                               SelectOneStepSupplier<R> by) {
        checkArgument(isNotBlank(description), NO_DESC_ERROR_TEXT);
        ((SetsDescription) by).changeDescription(translate(description));
        return by;
    }

    private static <R> SelectManyStepSupplier<R> setDescription(String description, SelectManyStepSupplier<R> by) {
        checkArgument(isNotBlank(description), NO_DESC_ERROR_TEXT);
        ((SetsDescription) by).changeDescription(translate(description));
        return by;
    }

    private static <S, R> GetObjectFromEntity.GetObjectFromEntityImpl<S, R> setDescription(String description,
                                                                                           GetObjectFromEntity<S, R> toGet) {
        checkArgument(isNotBlank(description), NO_DESC_ERROR_TEXT);
        var impl = (GetObjectFromEntity.GetObjectFromEntityImpl<S, R>) toGet;
        return impl.setDescription(translate(description));
    }

    private static <ITEM, S extends Iterable<ITEM>, R> GetIterableFromEntity.GetIterableFromEntityImpl<ITEM, S, R>
    setDescription(String description,
                   GetIterableFromEntity<ITEM, S, R> toGet) {
        checkArgument(isNotBlank(description), NO_DESC_ERROR_TEXT);
        var impl = (GetIterableFromEntity.GetIterableFromEntityImpl<ITEM, S, R>) toGet;
        return impl.setDescription(translate(description));
    }

    private static <ITEM, R> GetArrayFromEntity.GetArrayFromEntityImpl<ITEM, R>
    setDescription(String description,
                   GetArrayFromEntity<ITEM, R> toGet) {
        checkArgument(isNotBlank(description), NO_DESC_ERROR_TEXT);
        var impl = (GetArrayFromEntity.GetArrayFromEntityImpl<ITEM, R>) toGet;
        return impl.setDescription(translate(description));
    }

    private static <ITEM, R> GetItemOfIterableFromEntity.GetItemOfIterableFromEntityImpl<ITEM, ? extends Iterable<ITEM>, R>
    setDescription(String description,
                   GetItemOfIterableFromEntity<ITEM, ? extends Iterable<ITEM>, R> toGet) {
        checkArgument(isNotBlank(description), NO_DESC_ERROR_TEXT);
        var impl = (GetItemOfIterableFromEntity.GetItemOfIterableFromEntityImpl<ITEM, ? extends Iterable<ITEM>, R>) toGet;
        return impl.setDescription(translate(description));
    }

    private static <ITEM, R> GetItemOfArrayFromEntity.GetItemOfArrayFromEntityImpl<ITEM, R>
    setDescription(String description,
                   GetItemOfArrayFromEntity<ITEM, R> toGet) {
        checkArgument(isNotBlank(description), NO_DESC_ERROR_TEXT);
        var impl = (GetItemOfArrayFromEntity.GetItemOfArrayFromEntityImpl<ITEM, R>) toGet;
        return impl.setDescription(translate(description));
    }

    private static <ITEM, S extends Iterable<ITEM>, R> GetListFromEntity.GetListFromEntityImpl<ITEM, S, R>
    setDescription(String description,
                   GetListFromEntity<ITEM, S, R> toGet) {
        checkArgument(isNotBlank(description), NO_DESC_ERROR_TEXT);
        var impl = (GetListFromEntity.GetListFromEntityImpl<ITEM, S, R>) toGet;
        return impl.setDescription(translate(description));
    }

    private static <ITEM, R> GetIterableFromEntities.GetIterableFromEntitiesImpl<ITEM, R>
    setDescription(String description,
                   GetIterableFromEntities<ITEM, R> toGet) {
        checkArgument(isNotBlank(description), NO_DESC_ERROR_TEXT);
        var impl = (GetIterableFromEntities.GetIterableFromEntitiesImpl<ITEM, R>) toGet;
        return impl.setDescription(translate(description));
    }

    private static <ITEM, R> GetIterableItemFromEntities.GetIterableItemFromEntitiesImpl<ITEM, R>
    setDescription(String description,
                   GetIterableItemFromEntities<ITEM, R> toGet) {
        checkArgument(isNotBlank(description), NO_DESC_ERROR_TEXT);
        var impl = (GetIterableItemFromEntities.GetIterableItemFromEntitiesImpl<ITEM, R>) toGet;
        return impl.setDescription(translate(description));
    }

    private static <ITEM, R> GetListFromEntities.GetListFromEntitiesImpl<ITEM, R>
    setDescription(String description,
                   GetListFromEntities<ITEM, R> toGet) {
        checkArgument(isNotBlank(description), NO_DESC_ERROR_TEXT);
        var impl = (GetListFromEntities.GetListFromEntitiesImpl<ITEM, R>) toGet;
        return impl.setDescription(translate(description));
    }

    public <R> R select(String description, SelectOneStepSupplier<R> by) {
        return select(setDescription(description, by));
    }

    public <R> List<R> select(String description, SelectManyStepSupplier<R> by) {
        return select(setDescription(description, by));
    }

    public <S, R> S select(String description,
                           GetObjectFromEntity<S, R> toGet) {
        return select(setDescription(description, toGet));
    }

    public <ITEM, S extends Iterable<ITEM>, R> List<ITEM> select(String description,
                                                                 GetIterableFromEntity<ITEM, S, R> toGet) {
        return select(setDescription(description, toGet));
    }

    public <ITEM, R> ITEM[] select(String description,
                                   GetArrayFromEntity<ITEM, R> toGet) {
        return select(setDescription(description, toGet));
    }

    public <ITEM, R> ITEM select(String description,
                                 GetItemOfIterableFromEntity<ITEM, ? extends Iterable<ITEM>, R> toGet) {
        return select(setDescription(description, toGet));
    }

    public <ITEM, R> ITEM select(String description,
                                 GetItemOfArrayFromEntity<ITEM, R> toGet) {
        return select(setDescription(description, toGet));
    }

    public <ITEM, S extends Iterable<ITEM>, R> List<ITEM> select(String description,
                                                                 GetListFromEntity<ITEM, S, R> toGet) {
        return select(setDescription(description, toGet));
    }

    public <ITEM, R> List<ITEM> select(String description,
                                       GetIterableFromEntities<ITEM, R> toGet) {
        return select(setDescription(description, toGet));
    }

    public <ITEM, R> ITEM select(String description,
                                 GetIterableItemFromEntities<ITEM, R> toGet) {
        return select(setDescription(description, toGet));
    }

    public <ITEM, R> List<ITEM> select(String description,
                                       GetListFromEntities<ITEM, R> toGet) {
        return select(setDescription(description, toGet));
    }

    @Override
    protected <R, Q extends SequentialGetStepSupplier<HibernateContext, R, ?, ?, ?> & SelectQuery<R>> R delete(Q query) {
        return get(query);
    }

    public <R> HibernateContext delete(String description, SelectOneStepSupplier<R> select) {
        delete(DeleteByQueryStepSupplier.delete(description, select));
        return this;
    }

    public <R> HibernateContext delete(String description, SelectManyStepSupplier<R> select) {
        delete(DeleteByQueryStepSupplier.delete(description, select));
        return this;
    }

    public <R> HibernateContext delete(String description, R... toDelete) {
        checkNotNull(toDelete);
        if (toDelete.length == 1) {
            delete(DeleteByQueryStepSupplier.delete(description, toDelete[0]));
        } else {
            delete(DeleteByQueryStepSupplier.delete(description, List.of(toDelete)));
        }
        return this;
    }

    public <R> HibernateContext delete(String description, Iterable<R> toDelete) {
        delete(DeleteByQueryStepSupplier.delete(description, toDelete));
        return this;
    }

    @Override
    protected <R, S, Q extends SequentialGetStepSupplier<HibernateContext, S, ?, ?, ?> & SelectQuery<S>> S update(Q query, UpdateAction<R>... actions) {
        return get(((SaveStepSupplier<?, S, R>) query).setUpdates(actions));
    }

    @Override
    protected <R, Q extends SequentialGetStepSupplier<HibernateContext, R, ?, ?, ?> & InsertQuery<R>> R insert(Q query) {
        return get(query);
    }

    @SafeVarargs
    public final <R> R saveByQuery(String description,
                                   SelectOneStepSupplier<R> select,
                                   UpdateAction<R>... updateActions) {
        return update(SaveStepSupplier.save(description, select), updateActions);
    }

    @SafeVarargs
    public final <R> Iterable<R> saveAllByQuery(String description,
                                                SelectManyStepSupplier<R> select,
                                                UpdateAction<R>... updateActions) {
        return update(SaveStepSupplier.save(description, select), updateActions);
    }

    public <R> R saveOne(String description, R toSave) {
        return insert(SaveStepSupplier.save(description, toSave));
    }

    public <R> Iterable<R> saveAll(String description, Iterable<R> toSave) {
        return insert(SaveStepSupplier.save(description, toSave));
    }

    @SafeVarargs
    public final <R> Iterable<R> saveAll(String description, R... toSave) {
        checkNotNull(toSave);
        return saveAll(description, asList(toSave));
    }

    @SafeVarargs
    public final <R> R saveOne(String description,
                               R toSave,
                               UpdateAction<R>... updateActions) {
        return update(SaveStepSupplier.save(description, toSave), updateActions);
    }

    @SafeVarargs
    public final <R> Iterable<R> saveAll(String description,
                                         Iterable<R> toSave,
                                         UpdateAction<R>... updateActions) {
        return update(SaveStepSupplier.save(description, toSave), updateActions);
    }

    @SafeVarargs
    public final <R> boolean presenceOf(String description,
                                        SelectOneStepSupplier<R> by,
                                        Class<? extends Throwable>... toIgnore) {
        return super.presenceOf(setDescription(description, by), toIgnore);
    }

    @SafeVarargs
    public final <R> boolean presenceOfOrThrow(String description,
                                               SelectOneStepSupplier<R> by,
                                               Class<? extends Throwable>... toIgnore) {
        return super.presenceOfOrThrow(setDescription(description, by), toIgnore);
    }

    @SafeVarargs
    public final <R> boolean presenceOf(String description,
                                        SelectManyStepSupplier<R> by,
                                        Class<? extends Throwable>... toIgnore) {
        return super.presenceOf(setDescription(description, by), toIgnore);
    }

    @SafeVarargs
    public final <R> boolean presenceOfOrThrow(String description,
                                               SelectManyStepSupplier<R> by,
                                               Class<? extends Throwable>... toIgnore) {
        return super.presenceOfOrThrow(setDescription(description, by), toIgnore);
    }

    public <R> boolean absenceOf(String description,
                                 SelectOneStepSupplier<R> by,
                                 Duration timeOut) {
        return super.absenceOf(setDescription(description, by), timeOut);
    }

    public <R> boolean absenceOfOrThrow(String description,
                                        SelectOneStepSupplier<R> by,
                                        Duration timeOut) {
        return super.absenceOfOrThrow(setDescription(description, by), timeOut);
    }

    public <R> boolean absenceOf(String description,
                                 SelectManyStepSupplier<R> by,
                                 Duration timeOut) {
        return super.absenceOf(setDescription(description, by), timeOut);
    }

    public <R> boolean absenceOfOrThrow(String description,
                                        SelectManyStepSupplier<R> by,
                                        Duration timeOut) {
        return super.absenceOfOrThrow(setDescription(description, by), timeOut);
    }
}
