package ru.tinkoff.qa.neptune.hibernate.delete;

import com.google.common.base.Function;

import static java.util.Optional.ofNullable;
import static ru.tinkoff.qa.neptune.hibernate.HibernateContext.hibernate;


abstract class DeleteEntities<R> implements Function<R, Void> {

    //protected R toDelete;

    private DeleteEntities() {
    }

    static class DeleteOne<R> extends DeleteEntities<R> {

        DeleteOne() {
            super();
        }

        @Override
        public Void apply(R toDelete) {
            ofNullable(toDelete).ifPresent(deleteSingle -> {
                var sessionFactory = hibernate().getSessionFactoryByEntity(deleteSingle.getClass());
                var session = sessionFactory.getCurrentSession();
                session.beginTransaction();
                var obj = session.merge(deleteSingle);
                session.delete(obj);
                session.getTransaction().commit();
            });

            return null;
        }
    }

    static class DeleteMany<R> extends DeleteEntities<Iterable<R>> {

        DeleteMany() {
            super();
        }

        @Override
        public Void apply(Iterable<R> toDelete) {
            ofNullable(toDelete).ifPresent(toDeleteList -> toDeleteList.forEach(toDeleteSingle -> {
                var sessionFactory = hibernate().getSessionFactoryByEntity(toDeleteSingle.getClass());
                var session = sessionFactory.getCurrentSession();
                session.beginTransaction();
                var obj = session.merge(toDeleteSingle);
                session.delete(obj);
                session.getTransaction().commit();
            }));

            return null;
        }
    }
}
