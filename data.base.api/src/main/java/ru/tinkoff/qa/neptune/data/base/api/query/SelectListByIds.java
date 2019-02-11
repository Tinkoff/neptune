package ru.tinkoff.qa.neptune.data.base.api.query;

import org.datanucleus.api.jdo.JDOPersistenceManager;
import ru.tinkoff.qa.neptune.data.base.api.PersistableObject;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static java.lang.String.format;
import static java.lang.String.valueOf;
import static java.util.stream.Collectors.joining;
import static ru.tinkoff.qa.neptune.data.base.api.ListOfStoredObjects.INFO_PERSISTABLE_INFO;

class SelectListByIds<T extends PersistableObject> implements Function<JDOPersistenceManager, List<T>> {

    private final Object[] ids;
    private final Class<T> ofType;

    SelectListByIds(Object[] ids, Class<T> ofType) {
        this.ids = ids;
        this.ofType = ofType;
    }


    @Override
    public List<T> apply(JDOPersistenceManager jdoPersistenceManager) {
        var found = new ArrayList<T>();
        for (Object id : ids) {
            try {
                var p = jdoPersistenceManager.getObjectById(ofType, id);
                p.setQuery(format("Known Id: %s", id));
                found.add(p);
            }
            catch (RuntimeException ignored) {
            }
        }

        return new ListOfSelectObjects<>(found, INFO_PERSISTABLE_INFO::apply)
                .setQuery(format("Known Ids: %s", found.stream().map(t -> valueOf(t.getIdValue())).collect(joining(","))));
    }
}
