package ru.tinkoff.qa.neptune.hibernate.save;

import java.util.ArrayList;
import java.util.function.Function;

import static ru.tinkoff.qa.neptune.hibernate.HibernateContext.getSessionFactoryByEntity;

@SuppressWarnings("unchecked")
abstract class SaveFunction<INPUT, RESULT> implements Function<INPUT, RESULT> {

    private SaveFunction() {
    }

    protected <R> R saveObject(R toSave) {
        var sessionFactory = getSessionFactoryByEntity(toSave.getClass());

        var session = sessionFactory.getCurrentSession();
        var persistenceUnitUtil = sessionFactory.getPersistenceUnitUtil();
        session.beginTransaction();

        if (persistenceUnitUtil.getIdentifier(toSave) != null) {
            session.saveOrUpdate(toSave);
        } else {
            var id = session.save(toSave);
            toSave = (R) session.get(toSave.getClass(), id);
        }

        session.getTransaction().commit();
        return toSave;
    }

    static class SaveOne<R> extends SaveFunction<R, R> {

        SaveOne() {
        }

        @Override
        public R apply(R toSave) {
            return saveObject(toSave);
        }
    }

    static class SaveMany<R> extends SaveFunction<Iterable<R>, Iterable<R>> {

        SaveMany() {
        }

        @Override
        public Iterable<R> apply(Iterable<R> toSave) {
            var savedList = new ArrayList<R>();

            for (var objToSave : toSave) {
                savedList.add(saveObject(objToSave));
            }

            return savedList;
        }
    }
}
