package ru.tinkoff.qa.neptune.hibernate.select.common.by;

import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;
import ru.tinkoff.qa.neptune.database.abstractions.dictionary.ObjectArrayParameterValueGetter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.function.Function;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static ru.tinkoff.qa.neptune.core.api.localization.StepLocalization.translate;
import static ru.tinkoff.qa.neptune.hibernate.HibernateContext.getSessionFactoryByEntity;

@SuppressWarnings("unchecked")
@Description("id(s) {ids}")
public abstract class SelectionByIds<ID extends Serializable, RESULT> implements Function<Class<?>, RESULT> {

    @DescriptionFragment(value = "ids", makeReadableBy = ObjectArrayParameterValueGetter.class)
    final ID[] ids;

    @SafeVarargs
    private SelectionByIds(ID... ids) {
        checkNotNull(ids);
        checkArgument(ids.length > 0, "Should be defined at least one id");
        this.ids = ids;
    }

    public static <R, ID extends Serializable> SelectionByIds<ID, R> getSingleById(ID id) {
        return new SelectASingleById<>(id);
    }

    public static <R, ID extends Serializable> SelectionByIds<ID, Iterable<R>> getIterableByIds(ID... ids) {
        return new SelectIterableById<>(ids);
    }

    @Override
    public String toString() {
        return translate(this);
    }

    private static final class SelectASingleById<R, ID extends Serializable> extends SelectionByIds<ID, R> {

        private SelectASingleById(ID id) {
            super(id);
        }

        @Override
        public R apply(Class<?> t) {
            var id = ids[0];
            var sessionFactory = getSessionFactoryByEntity(t);
            var session = sessionFactory.getCurrentSession();
            return (R) session.get(t, id);
        }
    }

    private static final class SelectIterableById<R, ID extends Serializable> extends SelectionByIds<ID, Iterable<R>> {

        private SelectIterableById(ID... ids) {
            super(ids);
        }

        @Override
        public Iterable<R> apply(Class<?> t) {
            var sessionFactory = getSessionFactoryByEntity(t);
            var session = sessionFactory.getCurrentSession();
            var list = new ArrayList<R>();

            for (var id : ids) {
                list.add((R) session.get(t, id));
            }

            return list;
        }
    }
}