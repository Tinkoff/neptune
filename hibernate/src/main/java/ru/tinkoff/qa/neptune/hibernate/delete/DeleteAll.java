package ru.tinkoff.qa.neptune.hibernate.delete;

import java.util.function.Function;

import static ru.tinkoff.qa.neptune.hibernate.HibernateContext.getSessionFactoryByEntity;

public final class DeleteAll<R> implements Function<Class<R>, Void> {

    DeleteAll() {
    }

    @Override
    public Void apply(Class<R> entityCls) {
        var sessionFactory = getSessionFactoryByEntity(entityCls);
        var session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        var criteriaDelete = session.getCriteriaBuilder().createCriteriaDelete(entityCls);
        session.createQuery(criteriaDelete).executeUpdate();
        session.getTransaction().commit();

        return null;
    }
}
