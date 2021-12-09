package ru.tinkoff.qa.neptune.hibernate.delete;

import java.util.function.Function;

import static ru.tinkoff.qa.neptune.hibernate.HibernateContext.getSessionFactoryByEntity;

abstract class DeleteEntities<INPUT> implements Function<INPUT, Void> {

    private DeleteEntities() {
    }

    static class DeleteOne<R> extends DeleteEntities<R> {

        DeleteOne() {
        }

        @Override
        public Void apply(R entity) {
            var sessionFactory = getSessionFactoryByEntity(entity.getClass());
            var session = sessionFactory.getCurrentSession();
            session.beginTransaction();
            session.delete(entity);
            session.getTransaction().commit();

            return null;
        }
    }

    static class DeleteMany<R> extends DeleteEntities<Iterable<R>> {

        DeleteMany() {
        }

        @Override
        public Void apply(Iterable<R> entities) {
            entities.forEach(entity -> {
                var sessionFactory = getSessionFactoryByEntity(entity.getClass());
                var session = sessionFactory.getCurrentSession();
                session.beginTransaction();
                session.delete(entity);
                session.getTransaction().commit();
            });

            return null;
        }
    }
}
