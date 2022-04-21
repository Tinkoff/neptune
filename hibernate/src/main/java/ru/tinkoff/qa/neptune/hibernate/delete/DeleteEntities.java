package ru.tinkoff.qa.neptune.hibernate.delete;

import com.google.common.base.Function;
import ru.tinkoff.qa.neptune.hibernate.HibernateContext;


abstract class DeleteEntities<R> implements Function<HibernateContext, Void> {

    protected R toDelete;

    private DeleteEntities() {
    }

    public void setToDelete(R toDelete) {
        this.toDelete = toDelete;
    }

    static class DeleteOne<R> extends DeleteEntities<R> {

        DeleteOne() {
            super();
        }

        @Override
        public Void apply(HibernateContext context) {
            var sessionFactory = context.getSessionFactoryByEntity(toDelete.getClass());
            var session = sessionFactory.getCurrentSession();
            session.beginTransaction();
            session.delete(toDelete);
            session.getTransaction().commit();

            return null;
        }
    }

    static class DeleteMany<R> extends DeleteEntities<Iterable<R>> {

        DeleteMany() {
            super();
        }

        @Override
        public Void apply(HibernateContext context) {
            toDelete.forEach(toDeleteSingle -> {
                var sessionFactory = context.getSessionFactoryByEntity(toDeleteSingle.getClass());
                var session = sessionFactory.getCurrentSession();
                session.beginTransaction();
                session.delete(toDeleteSingle);
                session.getTransaction().commit();
            });

            return null;
        }
    }
}
