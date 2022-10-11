package ru.tinkoff.qa.neptune.hibernate.delete;

import ru.tinkoff.qa.neptune.hibernate.HibernateContext;
import ru.tinkoff.qa.neptune.hibernate.HibernateFunction;


public final class DeleteAll<R> extends HibernateFunction<R, Void> {

    DeleteAll(Class<R> entity) {
        super(entity);
    }

    @Override
    public Void apply(HibernateContext context) {
        var sessionFactory = context.getSessionFactoryByEntity(entity);
        var session = sessionFactory.getCurrentSession();

        session.beginTransaction();
        session.createQuery("delete from " + entity.getName()).executeUpdate();
        session.getTransaction().commit();

        return null;
    }
}
