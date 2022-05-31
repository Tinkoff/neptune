package ru.tinkoff.qa.neptune.hibernate.save;

import com.google.common.base.Function;
import org.hibernate.Session;
import ru.tinkoff.qa.neptune.hibernate.HibernateContext;

import java.util.ArrayList;

import static com.google.common.base.Preconditions.checkNotNull;

@SuppressWarnings("unchecked")
public abstract class SaveFunction<R, RESULT> implements Function<HibernateContext, RESULT> {

    protected Iterable<R> listToSave;

    private SaveFunction() {
    }

    public void setToSave(Iterable<R> listToSave) {
        checkNotNull(listToSave);
        this.listToSave = listToSave;
    }

    public void saveObjects(HibernateContext context) {
        var savedList = new ArrayList<R>();
        var sessions = new ArrayList<Session>();

        for (var toSave : listToSave) {
            var sessionFactory = context.getSessionFactoryByEntity(toSave.getClass());
            var session = sessionFactory.getCurrentSession();
            sessions.add(session);
            var persistenceUnitUtil = sessionFactory.getPersistenceUnitUtil();
            session.beginTransaction();

            if (persistenceUnitUtil.getIdentifier(toSave) != null) {
                var obj = session.merge(toSave);
                session.saveOrUpdate(obj);
            } else {
                var id = session.save(toSave);
                toSave = (R) session.get(toSave.getClass(), id);
            }
            savedList.add(toSave);
        }

        for (var session : sessions) {
            session.getTransaction().commit();
        }

        listToSave = savedList;
    }

    static class SaveOne<R> extends SaveFunction<R, R> {

        SaveOne() {
            super();
        }

        @Override
        public R apply(HibernateContext context) {
            saveObjects(context);
            return listToSave.iterator().next();
        }
    }

    static class SaveMany<R> extends SaveFunction<R, Iterable<R>> {

        SaveMany() {
            super();
        }

        @Override
        public Iterable<R> apply(HibernateContext context) {
            saveObjects(context);
            return listToSave;
        }
    }
}
