package ru.tinkoff.qa.neptune.hibernate.select.common.by;

import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.hibernate.HibernateContext;
import ru.tinkoff.qa.neptune.hibernate.HibernateFunction;

import static ru.tinkoff.qa.neptune.core.api.localization.StepLocalization.translate;

@Description("all")
public final class SelectAll<R> extends HibernateFunction<R, Iterable<R>> {

    public SelectAll(Class<R> entity) {
        super(entity);
    }

    @Override
    public String toString() {
        return translate(this);
    }

    @Override
    public Iterable<R> apply(HibernateContext context) {
        var sessionFactory = context.getSessionFactoryByEntity(entity);
        var session = sessionFactory.getCurrentSession();
        var criteria = session.getCriteriaBuilder().createQuery(entity);

        return session.createQuery(criteria).getResultList();
    }
}
