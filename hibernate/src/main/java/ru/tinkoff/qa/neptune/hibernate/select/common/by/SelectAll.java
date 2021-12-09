package ru.tinkoff.qa.neptune.hibernate.select.common.by;

import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;

import java.util.function.Function;

import static ru.tinkoff.qa.neptune.core.api.localization.StepLocalization.translate;
import static ru.tinkoff.qa.neptune.hibernate.HibernateContext.getSessionFactoryByEntity;

@Description("all")
public final class SelectAll<R> implements Function<Class<R>, Iterable<R>> {

    public SelectAll() {
    }

    @Override
    public String toString() {
        return translate(this);
    }

    @Override
    public Iterable<R> apply(Class<R> t) {
        var sessionFactory = getSessionFactoryByEntity(t);
        var session = sessionFactory.getCurrentSession();
        var criteria = sessionFactory.getCriteriaBuilder().createQuery(t);

        return session.createQuery(criteria).getResultList();
    }
}
