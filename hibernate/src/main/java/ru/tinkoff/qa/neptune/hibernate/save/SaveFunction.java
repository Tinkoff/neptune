package ru.tinkoff.qa.neptune.hibernate.save;

import com.google.common.base.Function;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.toList;
import static ru.tinkoff.qa.neptune.hibernate.HibernateContext.hibernate;

@SuppressWarnings("unchecked")
public abstract class SaveFunction<R, RESULT> implements Function<R, RESULT> {


    private SaveFunction() {
    }

    public List<Object> saveObjects(Iterable<Object> listToSave) {
        var savedList = new ArrayList<>();
        var sessions = new HashSet<Session>();

        for (var toSave : listToSave) {
            var sessionFactory = hibernate().getSessionFactoryByEntity(toSave.getClass());
            var session = sessionFactory.getCurrentSession();
            sessions.add(session);
            var persistenceUnitUtil = sessionFactory.getPersistenceUnitUtil();

            if (!session.getTransaction().isActive()) {
                session.beginTransaction();
            }

            if (persistenceUnitUtil.getIdentifier(toSave) != null) {
                var obj = session.merge(toSave);
                session.saveOrUpdate(obj);
            } else {
                var id = session.save(toSave);
                toSave = session.get(toSave.getClass(), id);
            }
            savedList.add(toSave);
        }

        for (var session : sessions) {
            session.getTransaction().commit();
        }

        return savedList;
    }

    static class SaveOne<R> extends SaveFunction<R, R> {

        SaveOne() {
            super();
        }

        @Override
        public R apply(R input) {
            return (R) saveObjects(List.of(input)).get(0);
        }
    }

    static class SaveMany<R> extends SaveFunction<Iterable<R>, Iterable<R>> {

        SaveMany() {
            super();
        }

        @Override
        public Iterable<R> apply(Iterable<R> input) {
            return (Iterable<R>) saveObjects(StreamSupport.stream(input.spliterator(), false)
                .collect(toList()));
        }
    }
}
