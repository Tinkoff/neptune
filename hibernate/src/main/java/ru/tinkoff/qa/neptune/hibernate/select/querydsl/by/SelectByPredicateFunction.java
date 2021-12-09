package ru.tinkoff.qa.neptune.hibernate.select.querydsl.by;

import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.hibernate.HibernateQuery;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;

import java.util.List;
import java.util.function.Function;

import static com.google.common.base.Preconditions.checkNotNull;
import static ru.tinkoff.qa.neptune.core.api.localization.StepLocalization.translate;
import static ru.tinkoff.qa.neptune.hibernate.HibernateContext.getSessionFactoryByEntity;

@Description("By predicate")
@SuppressWarnings("unchecked")
public abstract class SelectByPredicateFunction<R, RESULT> implements Function<Class<R>, RESULT> {

    final Predicate predicate;

    public SelectByPredicateFunction(Predicate predicate) {
        checkNotNull(predicate);
        this.predicate = predicate;
    }

    public Predicate getPredicate() {
        return predicate;
    }

    @Override
    public String toString() {
        return translate(this);
    }

    public static final class SelectOneByPredicate<R> extends SelectByPredicateFunction<R, R> {

        public SelectOneByPredicate(Predicate predicate) {
            super(predicate);
        }

        @Override
        public R apply(Class<R> t) {
            var sessionFactory = getSessionFactoryByEntity(t);
            var session = sessionFactory.getCurrentSession();

            return (R) new HibernateQuery<>(session).select(predicate).fetchOne();
        }
    }

    public static final class SelectManyByPredicate<R> extends SelectByPredicateFunction<R, Iterable<R>> {

        public SelectManyByPredicate(Predicate predicate) {
            super(predicate);
        }

        @Override
        public Iterable<R> apply(Class<R> t) {
            var sessionFactory = getSessionFactoryByEntity(t);
            var session = sessionFactory.getCurrentSession();

            return (List<R>) new HibernateQuery<>(session).select(predicate).fetch();
        }
    }

    @Description("By predicate as page")
    public static final class SelectManyByPredicateAndPagination<R> extends SelectByPredicateFunction<R, Iterable<R>> {

        private int limit;
        private int offset;

        public SelectManyByPredicateAndPagination(Predicate predicate) {
            super(predicate);
        }

        @Override
        public Iterable<R> apply(Class<R> t) {
            var sessionFactory = getSessionFactoryByEntity(t);
            var session = sessionFactory.getCurrentSession();

            return (List<R>) new HibernateQuery<>(session).select(predicate).limit(limit).offset(offset).fetch();
        }


        public void setLimitOffset(int limit, int offset) {
            this.limit = limit;
            this.offset = offset;
        }
    }
}
