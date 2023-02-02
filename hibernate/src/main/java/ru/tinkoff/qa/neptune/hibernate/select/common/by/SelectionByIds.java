package ru.tinkoff.qa.neptune.hibernate.select.common.by;

import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;
import ru.tinkoff.qa.neptune.core.api.steps.parameters.StepParameterPojo;
import ru.tinkoff.qa.neptune.database.abstractions.dictionary.ObjectArrayParameterValueGetter;
import ru.tinkoff.qa.neptune.hibernate.HibernateContext;
import ru.tinkoff.qa.neptune.hibernate.HibernateFunction;

import java.io.Serializable;
import java.util.ArrayList;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

@SuppressWarnings("unchecked")
@Description("id(s) {ids}")
public abstract class SelectionByIds<R, ID extends Serializable, RESULT> extends HibernateFunction<R, RESULT>
        implements StepParameterPojo {

    @DescriptionFragment(value = "ids", makeReadableBy = ObjectArrayParameterValueGetter.class)
    final ID[] ids;

    @SafeVarargs
    private SelectionByIds(Class<R> entity, ID... ids) {
        super(entity);
        checkNotNull(ids);
        checkNotNull(entity);
        checkArgument(ids.length > 0, "Should be defined at least one id");
        this.ids = ids;
    }

    public static <R, ID extends Serializable> SelectionByIds<R, ID, R> getSingleById(Class<R> clazz, ID id) {
        return new SelectASingleById<>(clazz, id);
    }

    public static <R, ID extends Serializable> SelectionByIds<R, ID, Iterable<R>> getIterableByIds(Class<R> clazz, ID... ids) {
        return new SelectIterableById<>(clazz, ids);
    }

    private static final class SelectASingleById<R, ID extends Serializable> extends SelectionByIds<R, ID, R> {

        private SelectASingleById(Class<R> entity, ID id) {
            super(entity, id);
        }

        @Override
        public R apply(HibernateContext context) {
            var id = ids[0];
            var sessionFactory = context.getSessionFactoryByEntity(entity);
            var session = sessionFactory.getCurrentSession();

            session.beginTransaction();

            var result = session.get(entity, id);

            session.getTransaction().commit();

            return result;
        }
    }

    private static final class SelectIterableById<R, ID extends Serializable> extends SelectionByIds<R, ID, Iterable<R>> {

        private SelectIterableById(Class<R> entity, ID... ids) {
            super(entity, ids);
        }

        @Override
        public Iterable<R> apply(HibernateContext context) {
            var sessionFactory = context.getSessionFactoryByEntity(entity);
            var session = sessionFactory.getCurrentSession();

            session.beginTransaction();

            var result = new ArrayList<R>();

            for (var id : ids) {
                result.add(session.get(entity, id));
            }

            session.getTransaction().commit();

            return result;
        }
    }
}