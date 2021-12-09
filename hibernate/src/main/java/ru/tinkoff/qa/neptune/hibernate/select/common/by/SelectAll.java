package ru.tinkoff.qa.neptune.hibernate.select.common.by;

import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;

import java.util.List;
import java.util.function.Function;

import static ru.tinkoff.qa.neptune.core.api.localization.StepLocalization.translate;
import static ru.tinkoff.qa.neptune.hibernate.HibernateContext.getSessionFactoryByEntity;

@SuppressWarnings("unchecked")
@Description("all")
public final class SelectAll<R> implements Function<Class<?>, Iterable<R>> {

    public SelectAll() {
    }

    @Override
    public String toString() {
        return translate(this);
    }

    @Override
    public Iterable<R> apply(Class<?> t) {
        var sessionFactory = getSessionFactoryByEntity(t);
        var session = sessionFactory.getCurrentSession();
        var criteria = sessionFactory.getCriteriaBuilder().createQuery(t);

        return (List<R>) session.createQuery(criteria).getResultList();
    }
}
