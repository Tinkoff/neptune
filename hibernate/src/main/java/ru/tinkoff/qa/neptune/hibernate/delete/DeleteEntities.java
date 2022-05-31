package ru.tinkoff.qa.neptune.hibernate.delete;

import com.google.common.base.Function;
import ru.tinkoff.qa.neptune.hibernate.HibernateContext;

import static java.util.Optional.ofNullable;


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
            ofNullable(toDelete).ifPresent(deleteSingle -> {
                var sessionFactory = context.getSessionFactoryByEntity(deleteSingle.getClass());
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
        public Void apply(HibernateContext context) {
            ofNullable(toDelete).ifPresent(toDeleteList -> toDeleteList.forEach(toDeleteSingle -> {
                var sessionFactory = context.getSessionFactoryByEntity(toDeleteSingle.getClass());
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
