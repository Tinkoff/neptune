package ru.tinkoff.qa.neptune.hibernate.select.querydsl.by;

import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.hibernate.HibernateQuery;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.hibernate.HibernateContext;
import ru.tinkoff.qa.neptune.hibernate.HibernateFunction;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;
import static ru.tinkoff.qa.neptune.core.api.localization.StepLocalization.translate;

@Description("By predicate")
@SuppressWarnings("unchecked")
public abstract class SelectByPredicateFunction<R, RESULT> extends HibernateFunction<R, RESULT> {

    final Predicate predicate;
    final EntityPath<?> entityPath;

    protected SelectByPredicateFunction(Class<R> entity, EntityPath<?> entityPath, Predicate predicate) {
        super(entity);
        checkNotNull(entityPath);
        checkNotNull(predicate);
        this.predicate = predicate;
        this.entityPath = entityPath;
    }

    public Predicate getPredicate() {
        return predicate;
    }

    @Override
    public String toString() {
        return translate(this);
    }

    public static final class SelectOneByPredicate<R> extends SelectByPredicateFunction<R, R> {

        public SelectOneByPredicate(Class<R> entity, EntityPath<?> entityPath, Predicate predicate) {
            super(entity, entityPath, predicate);
        }

        @Override
        public R apply(HibernateContext context) {
            var sessionFactory = context.getSessionFactoryByEntity(entity);
            var session = sessionFactory.getCurrentSession();

            session.beginTransaction();

            var result = (R) new HibernateQuery<>(session)
                    .select(entityPath)
                    .from(entityPath)
                    .where(predicate)
                    .fetchOne();

            session.getTransaction().commit();

            return result;
        }
    }

    public static final class SelectManyByPredicate<R> extends SelectByPredicateFunction<R, Iterable<R>> {

        public SelectManyByPredicate(Class<R> entity, EntityPath<?> entityPath, Predicate predicate) {
            super(entity, entityPath, predicate);
        }

        @Override
        public Iterable<R> apply(HibernateContext context) {
            var sessionFactory = context.getSessionFactoryByEntity(entity);
            var session = sessionFactory.getCurrentSession();

            session.beginTransaction();

            var result = (List<R>) new HibernateQuery<>(session)
                    .select(entityPath)
                    .from(entityPath)
                    .where(predicate)
                    .fetch();

            session.getTransaction().commit();

            return result;
        }
    }

    @Description("By predicate as page")
    public static final class SelectManyByPredicateAndPagination<R> extends SelectByPredicateFunction<R, Iterable<R>> {

        private int limit;
        private int offset;

        public SelectManyByPredicateAndPagination(Class<R> entity, EntityPath<?> entityPath, Predicate predicate) {
            super(entity, entityPath, predicate);
        }

        @Override
        public Iterable<R> apply(HibernateContext context) {
            var sessionFactory = context.getSessionFactoryByEntity(entity);
            var session = sessionFactory.getCurrentSession();

            session.beginTransaction();

            var result = (List<R>) new HibernateQuery<>(session)
                    .select(entityPath)
                    .from(entityPath)
                    .where(predicate)
                    .limit(limit)
                    .offset(offset)
                    .fetch();

            session.getTransaction().commit();

            return result;
        }


        public void setLimitOffset(int limit, int offset) {
            this.limit = limit;
            this.offset = offset;
        }
    }
}
